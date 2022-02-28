package main.api.response.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("user")
    private PostUserDto user;

    @JsonProperty("title")
    private String title;

    @JsonProperty("announce")
    private String announce;

    @JsonProperty("likeCount")
    private int likeCount;

    @JsonProperty("dislikeCount")
    private int dislikeCount;

    @JsonProperty("commentCount")
    private int commentCount;

    @JsonProperty("viewCount")
    private int viewCount;
}
