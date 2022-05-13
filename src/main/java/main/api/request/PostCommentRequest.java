package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostCommentRequest {

    @JsonProperty("parent_id")
    private int parentId;

    @JsonProperty("post_id")
    private int postId;

    private String text;
}
