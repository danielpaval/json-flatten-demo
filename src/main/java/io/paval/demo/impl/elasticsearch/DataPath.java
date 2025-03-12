package io.paval.demo.impl.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class DataPath {

    public String key;

    public String type;
    
    public String text;

    public Number number;

    @JsonProperty("boolean")
    public Boolean bool;
    
    public ZonedDateTime date;

}
