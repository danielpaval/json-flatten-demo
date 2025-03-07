package io.paval.demo.jpa;

import io.paval.demo.EventDto;
import io.paval.demo.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Profile("jpa")
@Service
@RequiredArgsConstructor
public class JpaEventService implements EventService {

    private final JpaEventRepository jpaEventRepository;
    private final JpaEventMapper jpaEventMapper;

    @Override
    public EventDto save(EventDto eventDto) {
        JpaEvent Event = jpaEventMapper.eventDtoToEvent(eventDto);
        JpaEvent savedEvent = jpaEventRepository.save(Event);
        return jpaEventMapper.eventToEventDto(savedEvent);
    }

    @Override
    public Optional<EventDto> findById(UUID id) {
        return jpaEventRepository.findById(id)
                .map(jpaEventMapper::eventToEventDto);
    }

}
