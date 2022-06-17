package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
public class PostCommentsDto {

    private int id;

    private long timestamp;

    private String text;

    private PostCommentsUserDto user;
}
