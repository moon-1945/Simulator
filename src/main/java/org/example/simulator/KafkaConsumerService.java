package org.example.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simulator.schemas.Building;
import org.example.simulator.utils.JsonValidator;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@KafkaListener(topics = "simulator_topic", groupId = "simulator_group")
public class KafkaConsumerService {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final JsonValidator jsonValidator;

	public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate, JsonValidator jsonValidator) {
		this.kafkaTemplate = kafkaTemplate;
		this.jsonValidator = jsonValidator;
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

				Building.getInstance().storeBuildingData(building.getBuildingId(), building.getFloors());

				for (int i = 1; i <= 10; i++) {
					// TODO: add algorithm to generate and send violations
					Map<String, String> violation = Map.of(
							"violationId", String.valueOf(i),
							"description", "Violation #" + i
					);
					kafkaTemplate.send("violations_topic", violation.toString());
				}
			} else {
				System.out.println("Schema validation failed.");
			}
		} catch (Exception e) {
			System.out.println("Error parsing or validating the schema: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
