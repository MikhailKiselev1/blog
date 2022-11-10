package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
public class ErrorsResponse {

    @JsonInclude(NON_NULL)
    private Boolean result;

    @JsonInclude(NON_NULL)
    private HashMap<String, String> errors;

    //ответ для успешной загрузки изображений
    @JsonInclude(NON_NULL)
    private String image;
}
