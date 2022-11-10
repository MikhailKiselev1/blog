package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
public class PostCommentResponse {
    @JsonInclude(NON_NULL)
    private int id;

    @JsonInclude(NON_NULL)
    private Boolean result;

    @JsonInclude(NON_NULL)
    private HashMap<String, String> errors;
}
