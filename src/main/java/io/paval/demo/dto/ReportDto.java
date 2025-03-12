package io.paval.demo.dto;

import io.paval.demo.service.EventReportType;
import lombok.Data;

import java.util.Map;

@Data
public class ReportDto {
    
    private EventReportType type;
    
    private Map<String, Object> data;
    
}
