package io.paval.demo.service;

import io.paval.demo.dto.ReportDto;

import java.util.Map;

public interface ReportService {

    ReportDto execute(ReportType type, Map<String, Object> parameters);

}
