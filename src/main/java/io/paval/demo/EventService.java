package io.paval.demo;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

    EventDto save(EventDto eventDto);

    Optional<EventDto> findById(UUID id);

}