package io.paval.demo.service;

import io.paval.demo.util.EventGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractEventService implements EventService {

    public void generate(Integer count) {
        EventGenerator.generate(count);
    }
    
}
