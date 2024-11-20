package org.example.simulator;

import org.example.simulator.violationGenerators.FloorViolationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simulator.schemas.input.*;
import org.example.simulator.schemas.output.OutputSimulatorMessage;
import org.example.simulator.schemas.output.RoomChangesParameter;
import org.example.simulator.utils.JsonValidator;
import org.example.simulator.utils.RoomStateExtractor;
import org.example.simulator.violationGenerators.BuildingViolationGenerator;
import org.example.simulator.violationGenerators.RoomState;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@KafkaListener(topics = "simulator_topic", groupId = "simulator_group")
public class KafkaConsumerService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
	private final KafkaProducerService kafkaProducerService;
	private final JsonValidator jsonValidator;
	private final ExecutorService executorService;

	public KafkaConsumerService(KafkaProducerService kafkaProducerService, JsonValidator jsonValidator) {
		this.kafkaProducerService = kafkaProducerService;
		this.jsonValidator = jsonValidator;
		this.executorService = Executors.newFixedThreadPool(8);
	}

	@KafkaHandler
	public void consumeSchema(String schema) {
		System.out.println("Received schema: " + schema);

		try {
			boolean isValid = jsonValidator.validateJson(schema, schema);

			if (isValid) {
				System.out.println("Schema is valid.");

				ObjectMapper objectMapper = new ObjectMapper();
				Building building = objectMapper.readValue(schema, Building.class);

				//I'm not sure about that singelton...
				Building.getInstance().storeBuildingData(building.getBuildingId(), building.getFloors());

				BuildingViolationGenerator buildingViolationGenerator = getBuildingViolationGenerator(building);
				buildingViolationGenerator.startGenerateViolations();

			} else {
				logger.error("Schema validation failed.");
			}
		} catch (Exception e) {
			logger.error("Error parsing or validating the schema: " + e.getMessage());
			e.printStackTrace();
		}

		// received schema -> parse schema -> algorithm for generation violations() -> send message to api (use multithreading)
		// Генерація порушень
	}

	private BuildingViolationGenerator getBuildingViolationGenerator(Building building) {

		var idToRoomState = building.getFloors().stream()
				.flatMap(floor -> floor.getRooms().stream())
				.collect(Collectors.toMap(
						Room::getRoomId,
						room -> new RoomStateExtractor(room).extractRoomState()
				));

		BuildingViolationGenerator buildingViolationGenerator = new BuildingViolationGenerator(building, executorService);
		FloorViolationEventListener violationEventListener = violations -> {
            for (Map.Entry<Long, RoomState> entry : violations.entrySet()) {
                Long roomId = entry.getKey();
                RoomState violation = entry.getValue();
                RoomState currentRoomState = idToRoomState.get(roomId);

                RoomChangesParameter roomChanges = new RoomChangesParameter(
                        (int)(violation.getTemperature() - currentRoomState.getTemperature()),
                        violation.getSmokePercent() - currentRoomState.getSmokePercent(),
                        violation.getMovementLevel() - currentRoomState.getMovementLevel()
                );

                OutputSimulatorMessage outputSimulatorMessage = new OutputSimulatorMessage(roomId, roomChanges);

                kafkaProducerService.sendViolation(outputSimulatorMessage);
            }
        };

		buildingViolationGenerator.addFloorViolationEventListener(violationEventListener);
		return buildingViolationGenerator;
	}
}

