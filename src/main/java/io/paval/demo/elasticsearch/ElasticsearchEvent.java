package io.paval.demo.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.UUID;

@Document(indexName = "events")
@Getter
@Setter
public class ElasticsearchEvent {

    @Id
    private UUID id;

    @Field(type = FieldType.Object)
    private Map<String, Object> data;

}
