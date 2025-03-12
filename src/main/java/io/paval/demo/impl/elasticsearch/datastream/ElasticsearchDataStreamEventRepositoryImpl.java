package io.paval.demo.impl.elasticsearch.datastream;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.paval.demo.impl.elasticsearch.ElasticsearchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchDataStreamEventRepositoryImpl implements ElasticsearchDataStreamEventRepository {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public IndexResponse saveToDataStream(ElasticsearchEvent event, boolean flush) {
        try {
            IndexRequest<ElasticsearchEvent> request = IndexRequest.of(i -> i
                    .index(elasticsearchOperations.getIndexCoordinatesFor(ElasticsearchEvent.class).getIndexName()) // TODO: Move to application properties
                    .id(event.getId().toString())
                    .document(event)
                    .refresh(flush ? Refresh.True : Refresh.False)
                    .opType(OpType.Create)
            );
            return elasticsearchClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("Could not save event to data stream", e);
        }
    }

    @Override
    public Optional<ElasticsearchEvent> findByIdFromDataStream(UUID id) {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index(elasticsearchOperations.getIndexCoordinatesFor(ElasticsearchEvent.class).getIndexName())
                    .query(q -> q
                            .term(t -> t
                                    .field("_id")
                                    .value(id.toString())
                            )
                    )
            );
            SearchResponse<ElasticsearchEvent> response = elasticsearchClient.search(request, ElasticsearchEvent.class);
            return response.hits().hits().stream()
                    .findFirst()
                    .map(Hit::source);
        } catch (Exception e) {
            throw new RuntimeException("Could not save event to data stream", e);
        }
    }

}
