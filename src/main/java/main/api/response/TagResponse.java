package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TagResponse {

    @JsonProperty("name")
    String name;

    @JsonProperty("weight")
    double weight;
}
