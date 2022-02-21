package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TagDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("weight")
    private double weight;
}
