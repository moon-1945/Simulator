package org.example.simulator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	@KafkaListener(topics = "my-topic", groupId = "group_id")
	public void consumeMessage(String message) {
		System.out.println("Received message: " + message);
	}
}
