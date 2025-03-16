package io.paval.demo.dto;

import io.paval.demo.service.ReportType;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class ReportCriteria {

    private ReportType type;

    private Map<String, Object> parameters;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

}
