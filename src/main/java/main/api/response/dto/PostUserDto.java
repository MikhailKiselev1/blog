package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostUserDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;
}
