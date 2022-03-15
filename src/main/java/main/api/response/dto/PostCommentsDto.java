package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("prototype")
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
