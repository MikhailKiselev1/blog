package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
public class PostUserDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;
}
