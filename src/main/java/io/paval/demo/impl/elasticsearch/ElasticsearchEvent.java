package io.paval.demo.impl.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.paval.demo.dto.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(indexName = "events-data-stream", storeIdInSource = false)
@Getter
@Setter
public class ElasticsearchEvent {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private EventType type;
    
    @JsonProperty("@timestamp")
    @Field(name = "@timestamp", type = FieldType.Date)
    private ZonedDateTime date;

    @Field(type = FieldType.Object)
    private Map<String, Object> data;

    @Field(type = FieldType.Nested)
    private List<DataPath> paths;
    
}
