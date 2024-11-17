package org.example.simulator;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@KafkaListener(topics = "simulator_topic", groupId = "simulator_group")
public class KafkaConsumerService {
	private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
	public void consumeSchema(String schema) {
		System.out.println("Received schema: " + schema);

		// Генерація порушень
		for (int i = 1; i <= 10; i++) {
			Map<String, String> violation = Map.of(
					"violationId", String.valueOf(i),
					"description", "Violation #" + i
			);

			kafkaTemplate.send("violations_topic", violation.toString());
		}
	}
}
