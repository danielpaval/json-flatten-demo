package io.paval.demo.elasticsearch;

import io.paval.demo.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.context.annotation.Profile;

@Profile("elasticsearch")
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ElasticsearchEventMapper {

    ElasticsearchEvent eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(ElasticsearchEvent event);

}
