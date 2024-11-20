package org.example.simulator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

public class JsonValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validateJson(String kafkaMessage) {
        try {
            JsonNode jsonNode = objectMapper.readTree(kafkaMessage);
            JsonSchemaFactory schemaFactory = new JsonSchemaFactory.Builder().build();
            InputStream schemaStream = Objects.requireNonNull(JsonValidator.class.getClassLoader().getResourceAsStream("building-schema.json"));
            JsonSchema schema = schemaFactory.getSchema(schemaStream);
            Set<ValidationMessage> validationMessages = schema.validate(jsonNode);
            if (!validationMessages.isEmpty()) {
                System.out.println("JSON validation errors:");
                for (ValidationMessage message : validationMessages) {
                    System.out.println(message.getMessage());
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error during JSON validation: " + e.getMessage());
            return false;
        }
    }

    public static void processKafkaMessage(ConsumerRecord<String, String> record) {
        String kafkaMessage = record.value();
        boolean isValid = validateJson(kafkaMessage);
        if (isValid) {
            System.out.println("Kafka message is valid: " + kafkaMessage);
        } else {
            System.out.println("Kafka message is invalid: " + kafkaMessage);
        }
    }
}
