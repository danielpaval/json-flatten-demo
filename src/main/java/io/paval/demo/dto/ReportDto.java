package io.paval.demo.dto;

import io.paval.demo.service.ReportType;
import lombok.Data;

import java.util.Map;

@Data
public class ReportDto {
    
    private ReportType type;
    
    private Map<String, Object> data;
    
}
