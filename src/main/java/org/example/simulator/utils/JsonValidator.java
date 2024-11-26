package org.example.simulator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class JsonValidator {

    private final JsonSchemaFactory schemaFactory;

    public JsonValidator() {
        this.schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    }

    public boolean validateJson(String schema, String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode schemaNode = objectMapper.readTree(schema);
            JsonNode jsonNode = objectMapper.readTree(json);

            JsonSchema jsonSchema = schemaFactory.getSchema(schemaNode);
            Set<ValidationMessage> validationMessages = jsonSchema.validate(jsonNode);

            if (!validationMessages.isEmpty()) {
                validationMessages.forEach(msg -> System.out.println("Validation error: " + msg.getMessage()));
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error during validation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
