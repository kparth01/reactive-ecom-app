package com.byondxtremes.ecom.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {

    private JsonUtil() {
    }

    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T convertToObject(String jsonString, Class<T> clazz) throws RuntimeException {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException("ERR-099 : IOException occurred while parsing json to " +
                "object.");
        }
    }

    public static String convertToString(Object obj) throws RuntimeException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("ERR-099 : IOException occurred while parsing json to " +
                "object.");
        }
    }
}
