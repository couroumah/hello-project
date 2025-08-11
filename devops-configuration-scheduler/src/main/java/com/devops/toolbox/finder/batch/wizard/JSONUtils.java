package com.devops.toolbox.finder.batch.wizard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONUtils {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    private JSONUtils(){

    }

    public static <T> String convertObjectToJson(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Cannot convert object to JSON. IOException: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> void convertObjectToJsonFile(T object, Path pathJsonOutput) {
        try {
            FileUtils.writeStringToFile(pathJsonOutput.toFile(), convertObjectToJson(object));
            if(pathJsonOutput.toFile().exists()){
                logger.info("Converted object[{}] to JSON file[{}].",object.getClass().getCanonicalName(), pathJsonOutput);
            }
        } catch (IOException e) {
            logger.error("Cannot convert object to JSON file[{}]. IOException: {}", pathJsonOutput, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertJsonToObject(Class clazz, String jsonAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(jsonAsString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertJsonFileToObject(Class clazz, Path pathJson) {
        try {
            return (T) convertJsonToObject(clazz, Files.readString(pathJson));
        } catch (IOException e) {
            logger.error("Cannot convert JSON file[{}] to object. IOException: {}", pathJson, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
