package io.paval.demo.service;

import io.paval.demo.dto.ReportDto;
import io.paval.demo.util.EventGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final Map<ReportType, Report> reportMap;

    public ReportServiceImpl(List<Report> reports) {
        reportMap = reports.stream()
                .collect(Collectors.toMap(Report::getType, Function.identity()));
    }

    @Override
    public ReportDto execute(ReportType type, Map<String, Object> parameters) {
        Report report = reportMap.get(type);
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
