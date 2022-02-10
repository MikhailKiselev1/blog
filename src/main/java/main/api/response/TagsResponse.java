package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TagsResponse {
    @JsonProperty("tags")
    private List<TagResponse> tags;
}
