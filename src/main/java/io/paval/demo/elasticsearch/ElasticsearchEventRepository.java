package io.paval.demo.elasticsearch;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Profile("elasticsearch")
@Repository
public interface ElasticsearchEventRepository extends ElasticsearchRepository<ElasticsearchEvent, UUID> {
}
