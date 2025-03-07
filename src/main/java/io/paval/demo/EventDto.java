package io.paval.demo;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class EventDto {

    private UUID id;

    private Map<String, Object> data;

}
