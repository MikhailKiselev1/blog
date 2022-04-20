package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class ErrorsResponse {

    private boolean result;

    @JsonInclude(NON_NULL)
    private HashMap<String, String> errors;
}
