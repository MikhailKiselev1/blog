package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class PostsResponse {

    @JsonProperty("count")
    private int count;

    @JsonProperty("posts")
    private List<PostDto> postsDto;
}
