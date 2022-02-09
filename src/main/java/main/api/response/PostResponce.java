package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.response.enums.PostMode;
import main.model.Post;
import main.model.User;

import java.time.LocalDateTime;

public class PostResponce {
    Post post;

    public PostResponce(Post post) {
        this.post = post;
    }

    @JsonProperty("id")
    int id = post.getId();

    @JsonProperty("timestamp")
    LocalDateTime timestamp = post.getTime();

    @JsonProperty("user")
    UserResponse user = new UserResponse(post.getUserId());

    @JsonProperty("title")
    String title = post.getTitle();

    @JsonProperty("announce")
    String announce = post.getText();

    @JsonProperty("likeCount")
    int likeCount = (int) post.getPostVotesList().stream().filter(u -> u.getValue() == 1).count();

    @JsonProperty("dislikeCount")
    int dislikeCount = (int) post.getPostVotesList().stream().filter(u -> u.getValue() == -1).count();

    @JsonProperty("commentCount")
    int commentCount = post.getPostComments().size();

    @JsonProperty("viewCount")
    int viewCount = post.getViewCount();

}
