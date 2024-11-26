package com.example.blog_app.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a JSON string to a Map.
     *
     * @param content the JSON string
     * @return the resulting Map
     */
    public static Map<String, Object> jsonToMap(String content) {
        try {
            return content != null ? objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {}) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Map", e);
        }
    }

    /**
     * Converts a Map to a JSON string.
     *
     * @param content the Map to convert
     * @return the resulting JSON string
     */
    public static String mapToJson(Map<String, Object> content) {
        try {
            return content != null ? objectMapper.writeValueAsString(content) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Map to JSON", e);
        }
    }
}
