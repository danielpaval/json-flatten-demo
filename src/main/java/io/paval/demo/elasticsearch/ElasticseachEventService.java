package io.paval.demo.elasticsearch;

import io.paval.demo.EventDto;
import io.paval.demo.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Profile("elasticsearch")
@Service
@RequiredArgsConstructor
public class ElasticseachEventService implements EventService {

    private final ElasticsearchEventRepository eventRepository;
    private final ElasticsearchEventMapper eventMapper;

    @Override
    public EventDto save(EventDto eventDto) {
        eventDto.setId(UUID.randomUUID());
        ElasticsearchEvent event = eventMapper.eventDtoToEvent(eventDto);
        ElasticsearchEvent savedEvent = eventRepository.save(event);
        return eventMapper.eventToEventDto(savedEvent);
    }

    @Override
    public Optional<EventDto> findById(UUID id) {
        return eventRepository.findById(id)
                .map(eventMapper::eventToEventDto);
    }

}
