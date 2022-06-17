package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.dto.PostCommentsDto;
import main.api.response.dto.PostDto;
import main.api.response.dto.PostUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class PostIdResponse {

    private int id;

    private long timestamp;

    @JsonProperty("active")
    private boolean isActive;

    private PostUserDto user;

    private String title;

    private String text;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private int viewCount;

    private List<PostCommentsDto> comments;

    private List<String> tags;
}
