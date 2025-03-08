package io.paval.demo;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class EventDto {

    private UUID id;

    private ZonedDateTime date;

    private Map<String, Object> data;

}
