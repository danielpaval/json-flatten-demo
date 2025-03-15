package io.paval.demo.controller;

import io.paval.demo.service.EventReportType;
import io.paval.demo.service.EventService;
import io.paval.demo.dto.ReportDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Event Reports API")
public class EventReportController {

    private final EventService eventService;
    
    @PostMapping
    public ResponseEntity<ReportDto> execute(@RequestParam EventReportType type, @RequestBody(required = false) Map<String, Object> parameters) {
        return new ResponseEntity<>(eventService.execute(type, parameters), HttpStatus.OK);
    }

}
