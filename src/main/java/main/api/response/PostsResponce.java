package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.model.service.CustomizedPostsImpl;

import java.util.List;

public class PostsResponce {

    @JsonProperty("id")
    private int count;

    @JsonProperty("posts")
    private List<PostResponce> postResponces;

    CustomizedPostsImpl customizedPosts;

    public PostsResponce() {
        customizedPosts = new CustomizedPostsImpl();
        postResponces = customizedPosts.getActionCurrentNewPosts();
        count = postResponces.size();
    }
}
