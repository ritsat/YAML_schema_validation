package com.ecl;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.core.report.ProcessingReport;

import java.nio.file.Files;
import java.nio.file.Path;

public class YAMLValidationExample {

    public static void main(String[] args) throws Exception {
        // Load YAML document from file
        String yamlString = new String(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("person.yaml").toURI()))); //Using NIO to read file

        // Parse YAML document using SnakeYAML
        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        Object yamlObject = yaml.load(yamlString);

        // Convert YAML object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(yamlObject, JsonNode.class);

        // Load JSON Schema from file
        String schemaString = new String(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("schema.json").toURI())));
        JsonNode schemaNode = objectMapper.readTree(schemaString);

        // Validate JSON against JSON Schema
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
        JsonSchema schema = schemaFactory.getJsonSchema(schemaNode);
        ProcessingReport report = schema.validate(jsonNode);

        // Print validation result
        if (report.isSuccess()) {
            System.out.println("YAML document is valid.");
        } else {
            System.out.println("YAML document is invalid:");
        }
    }
}
