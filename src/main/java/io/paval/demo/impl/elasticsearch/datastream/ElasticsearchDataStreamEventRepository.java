package io.paval.demo.impl.elasticsearch.datastream;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import io.paval.demo.impl.elasticsearch.ElasticsearchEvent;

import java.util.Optional;
import java.util.UUID;

public interface ElasticsearchDataStreamEventRepository {

    IndexResponse saveToDataStream(ElasticsearchEvent event, boolean flush);

    Optional<ElasticsearchEvent> findByIdFromDataStream(UUID id);
    
}
