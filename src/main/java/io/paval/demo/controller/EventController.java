package io.paval.demo.controller;

import io.paval.demo.dto.EventDto;
import io.paval.demo.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events API", description = "API for managing events")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Save a new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class))}),
            @ApiResponse(responseCode = "400", description = "Event not valid", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestParam(defaultValue = "false") boolean flush, @RequestBody EventDto eventDto) {
        UUID uuid = eventService.save(eventDto, flush);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Event-Id", uuid.toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Get event by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class))}),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findById(@PathVariable UUID id) {
        Optional<EventDto> eventDto = eventService.findById(id);
        return eventDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get event by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = EventDto.class))}),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @PostMapping("/samples")
    public void generate() {
        eventService.generate(1000000);
    }

}
