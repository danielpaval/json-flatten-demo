package io.paval.demo.service;

import java.util.Map;

public interface Report {
    
    Map<String, Object> execute(Map<String, Object> parameters);
    
    ReportType getType();
    
}
