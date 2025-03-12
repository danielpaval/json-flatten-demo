package io.paval.demo.service;

import io.paval.demo.dto.ReportDto;
import io.paval.demo.util.EventGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventService implements EventService {
    
    private final Map<EventReportType, EventReport> reportMap;
    
    public AbstractEventService(List<EventReport> reports) {
        reportMap = reports.stream()
                .collect(Collectors.toMap(EventReport::getType, Function.identity()));
    }
    
    public void generate(Integer count) {
        EventGenerator.generate(count);
    }

    @Override
    public ReportDto execute(EventReportType type, Map<String, Object> parameters) {
        EventReport report = reportMap.get(type);
        if (report != null) {
            ReportDto reportDto = new ReportDto();
            reportDto.setType(type);
            reportDto.setData(report.execute(parameters));
            return reportDto;
        } else {
            throw new UnsupportedOperationException("Report not implemented");
        }
    }
    
}
