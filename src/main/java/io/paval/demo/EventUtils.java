package io.paval.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.paval.demo.elasticsearch.DataPath;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class EventUtils {
    
    public static List<DataPath> flatten(Map<String, Object> data) {
        if (data != null) {
            ObjectMapper mapper = new ObjectMapper();
            return new ArrayList<>(flatten(mapper.valueToTree(data), "").values());
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
                    dataPath.setText(value.asText());
                /*if (value.isNumber()) {
                    propertyMap.put("number", value.numberValue());
                } else if (value.isBoolean()) {
                    propertyMap.put("boolean", value.booleanValue());
                } else if (value.isObject()) {
                    propertyMap.put("object", value.toString());
                } else if (value.isNull()) {
                    propertyMap.put("text", null);
                }*/
                    dataEntriesMap.put(key, dataPath);
                }
            }
        }
        return dataEntriesMap;
    }

}
