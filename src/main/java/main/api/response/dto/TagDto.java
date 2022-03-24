package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data

public class TagDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("weight")
    private double weight;
}
