package io.paval.demo.service;

import io.paval.demo.dto.EventDto;
import io.paval.demo.dto.ReportDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    default UUID save(EventDto eventDto) {
        return save(eventDto, false);
    }

    UUID save(EventDto eventDto, boolean flush);
    
    Optional<EventDto> findById(UUID id);

    void generate(Integer count);

    ReportDto execute(EventReportType type, Map<String, Object> parameters);
    
}