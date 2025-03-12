package io.paval.demo.impl.elasticsearch;

import io.paval.demo.impl.elasticsearch.datastream.ElasticsearchDataStreamEventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Profile("elasticsearch")
@Repository
public interface ElasticsearchEventRepository extends ElasticsearchRepository<ElasticsearchEvent, UUID>, ElasticsearchDataStreamEventRepository {
}
