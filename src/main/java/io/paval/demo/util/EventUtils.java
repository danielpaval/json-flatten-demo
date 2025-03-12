package io.paval.demo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.paval.demo.impl.elasticsearch.DataPath;
import lombok.experimental.UtilityClass;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@UtilityClass
public class EventUtils {
    
    public static List<DataPath> flatten(Map<String, Object> data) {
        if (data != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return new ArrayList<>(flatten(objectMapper.valueToTree(data), "").values());
        }
        return new ArrayList<>();
    }

    public static Map<String, DataPath> flatten(JsonNode node, String parentPath) {
        Map<String, DataPath> dataEntriesMap = new LinkedHashMap<>();
        if (node != null) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = parentPath.isEmpty() ? field.getKey() : parentPath + "." + field.getKey();
                JsonNode value = field.getValue();
                if (value.isObject()) {
                    dataEntriesMap.putAll(flatten(value, key));
                } else if (value.isArray()) {
                    for (int i = 0; i < value.size(); i++) {
                        dataEntriesMap.putAll(flatten(value.get(i), key + "[" + i + "]"));
                    }
                } else {
                    DataPath dataPath = new DataPath();
                    dataPath.setKey(key);
                    String text = value.asText();
                    dataPath.setText(text);
                    if (value.isTextual()) {
                        ZonedDateTime date = getDate(text);
                        if (date != null) {
                            dataPath.setType("date");
                            dataPath.setDate(date);
                        } else {
                            dataPath.setType("text");
                        }
                    } else if (value.isNumber()) {
                        dataPath.setNumber(value.numberValue());
                        dataPath.setType("number");
                    } else if (value.isBoolean()) {
                        dataPath.setBool(value.booleanValue());
                        dataPath.setType("bool");
                    } else if (value.isNull()) {
                        dataPath.setText(null);
                    }
                    dataEntriesMap.put(key, dataPath);
                }
            }
        }
        return dataEntriesMap;
    }

    private static ZonedDateTime getDate(String value) {
        try {
            return ZonedDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
}
