package io.paval.demo.service;

import java.util.Map;

public interface EventReport {
    
    Map<String, Object> execute(Map<String, Object> parameters);
    
    EventReportType getType();
    
}
