package io.paval.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<EventDto> save(@RequestBody EventDto eventDto) {

        EventDto savedEvent = eventService.save(eventDto);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
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

}
