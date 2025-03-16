package io.paval.demo.impl.jpa;

import io.paval.demo.dto.EventDto;
import io.paval.demo.service.AbstractEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Profile("jpa")
@Service
@RequiredArgsConstructor
public class JpaEventService extends AbstractEventService {

    private final JpaEventRepository eventRepository;
    
    private final JpaEventMapper eventMapper;
    
    @Override
    public UUID save(EventDto eventDto, boolean flush) {
        return save(eventDto);
    }
    
    @Override
    public UUID save(EventDto eventDto) {
        UUID uuid = UUID.randomUUID();
        JpaEvent event = eventMapper.eventDtoToEvent(eventDto);
        event.setId(uuid);
        return uuid;
    }

    @Override
    public Optional<EventDto> findById(UUID id) {
        return eventRepository.findById(id)
                .map(eventMapper::eventToEventDto);
    }

}
