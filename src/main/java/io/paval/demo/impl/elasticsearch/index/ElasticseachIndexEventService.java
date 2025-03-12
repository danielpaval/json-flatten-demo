package io.paval.demo.impl.elasticsearch.index;

import io.paval.demo.dto.EventDto;
import io.paval.demo.impl.elasticsearch.ElasticsearchEvent;
import io.paval.demo.impl.elasticsearch.ElasticsearchEventMapper;
import io.paval.demo.impl.elasticsearch.ElasticsearchEventRepository;
import io.paval.demo.service.AbstractEventService;
import io.paval.demo.service.EventReport;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ElasticseachIndexEventService extends AbstractEventService {

    private final ElasticsearchEventRepository eventRepository;
    
    private final ElasticsearchEventMapper eventMapper;

    public ElasticseachIndexEventService(
            ElasticsearchEventRepository eventRepository,
            ElasticsearchEventMapper eventMapper,
            List<EventReport> reports) {
        super(reports);
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }
    
    @Override
    public UUID save(EventDto eventDto, boolean flush) {
        UUID uuid = UUID.randomUUID();
        ElasticsearchEvent event = eventMapper.eventDtoToEvent(eventDto);
        event.setId(uuid);
        event.setDate(ZonedDateTime.now(ZoneOffset.UTC));
        eventRepository.save(event);
        return uuid;
    }

    @Override
    public Optional<EventDto> findById(UUID id) {
        return eventRepository.findById(id)
                .map(eventMapper::eventToEventDto);
    }

}
