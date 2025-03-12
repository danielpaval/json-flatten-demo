package io.paval.demo.impl.elasticsearch;

import io.paval.demo.dto.EventDto;
import io.paval.demo.util.EventUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;

@Profile("elasticsearch")
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ElasticsearchEventMapper {

    @Mapping(target = "paths", source = "data", qualifiedByName = "dataToPaths")
    ElasticsearchEvent eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(ElasticsearchEvent event);

    @Named("dataToPaths")
    default List<DataPath> dataToPaths(Map<String, Object> data) {
        return EventUtils.flatten(data);
    }

}
