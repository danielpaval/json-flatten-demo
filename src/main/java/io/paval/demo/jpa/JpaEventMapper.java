package io.paval.demo.jpa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.paval.demo.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Profile("jpa")
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JpaEventMapper {

    @Mapping(target = "data", source = "data", qualifiedByName = "objectToString")
    JpaEvent eventDtoToEvent(EventDto eventDto);

    @Mapping(target = "data", source = "data", qualifiedByName = "stringToObject")
    EventDto eventToEventDto(JpaEvent event);

    @Named("objectToString")
    default String objectToString(Map<String, Object> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }

    @Named("stringToObject")
    default Map<String, Object> stringToObject(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

}
