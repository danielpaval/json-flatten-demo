package io.paval.demo.elasticsearch;

import io.paval.demo.EventDto;
import io.paval.demo.EventUtils;
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

    @Mapping(target = "dataPaths", source = "data", qualifiedByName = "dataToDataPaths")
    ElasticsearchEvent eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(ElasticsearchEvent event);

    @Named("dataToDataPaths")
    default List<DataPath> dataToDataPaths(Map<String, Object> data) {
        return EventUtils.flatten(data);
    }

}
