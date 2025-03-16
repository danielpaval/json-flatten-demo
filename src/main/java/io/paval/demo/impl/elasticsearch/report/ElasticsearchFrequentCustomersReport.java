package io.paval.demo.impl.elasticsearch.report;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import io.paval.demo.service.Report;
import io.paval.demo.service.ReportType;
import io.paval.demo.impl.elasticsearch.ElasticsearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Report on the top three customers by their order count (minimum 2 orders)
 */
@Profile("elasticsearch")
@Service
@RequiredArgsConstructor
public class ElasticsearchFrequentCustomersReport implements Report {

    private final ElasticsearchClient elasticsearchClient;
    
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Map<String, Object> execute(Map<String, Object> parameters) {
        try {
            Map<String, Object> results = new HashMap<>();
            SearchRequest request = SearchRequest.of(s -> s // TODO: Add event type filter
                    .index("events-data-stream") // TODO: Move to application properties
                    .size(0)
                    .aggregations("paths", a -> a
                            .nested(n -> n
                                    .path("paths")
                            )
                            .aggregations("customer_email_path", a2 -> a2
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("paths.key")
                                                    .value("customer.email")
                                            )
                                    )
                                    .aggregations("email", a3 -> a3
                                            .terms(t -> t
                                                    .field("paths.text.keyword")
                                                    .size(10)
                                                    .minDocCount(3)
                                            )
                                    )
                            )
                    )
            );
            SearchResponse<ElasticsearchEvent> response = elasticsearchClient.search(request, ElasticsearchEvent.class);
            var nestedAgg = response.aggregations().get("paths").nested();
            var filterAgg = nestedAgg.aggregations().get("customer_email_path").filter();
            var termsAgg = filterAgg.aggregations().get("email").sterms();
            termsAgg.buckets().array().forEach(bucket -> {
                String email = bucket.key().stringValue();
                long count = bucket.docCount();
                results.put(email, count);
            });
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Could not execute report", e);
        }
    }

    @Override
    public ReportType getType() {
        return ReportType.FREQUENT_CUSTOMER_REPORT;
    }

}
