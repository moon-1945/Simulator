package org.example.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simulator.schemas.output.OutputSimulatorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
	private final KafkaTemplate<String, String> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendViolation(OutputSimulatorMessage violation) {
		ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonViolation = objectMapper.writeValueAsString(violation);

			kafkaTemplate.send("violations_topic", jsonViolation);
        } catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
        }
	}
}
