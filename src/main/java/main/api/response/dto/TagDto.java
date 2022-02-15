package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TagDto {

    @JsonProperty("name")
    String name;

    @JsonProperty("weight")
    double weight;
}
