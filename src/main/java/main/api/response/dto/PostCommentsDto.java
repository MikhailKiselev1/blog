package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostCommentsDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private PostCommentsUserDto user;
}
