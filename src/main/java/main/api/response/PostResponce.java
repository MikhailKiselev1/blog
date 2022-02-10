package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.enums.PostMode;
import main.model.Post;
import main.model.User;

import java.time.LocalDateTime;

@Data
public class PostResponce {

    @JsonProperty("id")
    int id;

    @JsonProperty("timestamp")
    LocalDateTime timestamp;

    @JsonProperty("user")
    UserResponse user;

    @JsonProperty("title")
    String title;

    @JsonProperty("announce")
    String announce;

    @JsonProperty("likeCount")
    int likeCount;

    @JsonProperty("dislikeCount")
    int dislikeCount;

    @JsonProperty("commentCount")
    int commentCount;

    @JsonProperty("viewCount")
    int viewCount;
}
