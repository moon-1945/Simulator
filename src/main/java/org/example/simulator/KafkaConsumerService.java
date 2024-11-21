package org.example.simulator;

import org.example.simulator.violationGenerators.FloorViolationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simulator.schemas.input.*;
import org.example.simulator.utils.JsonValidator;
import org.example.simulator.violationGenerators.BuildingViolationGenerator;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

				FloorViolationEvent floorViolationEvent = new FloorViolationEvent();
				floorViolationEvent.addListener(new FloorViolationEventListener(kafkaProducerService, building));

				BuildingViolationGenerator buildingViolationGenerator =
						new BuildingViolationGenerator(building, executorService, floorViolationEvent);

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
}

