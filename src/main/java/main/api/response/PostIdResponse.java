package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.dto.PostCommentsDto;
import main.api.response.dto.PostDto;
import main.model.PostComments;

import java.util.List;

@Data
public class PostIdResponse extends PostDto {

    @JsonProperty("active")
    private boolean isActive;

    @JsonProperty("comments")
    private List<PostCommentsDto> comments;

    @JsonProperty("tags")
    private List<String> tags;
}
