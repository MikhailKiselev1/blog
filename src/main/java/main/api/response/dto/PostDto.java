package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private int id;

    private long timestamp;

    private PostUserDto user;

    private String title;

    private String announce;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;
}
