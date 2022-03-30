package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;

@Data
public class RegisterResponse {

    @JsonProperty("result")
    private boolean result;

    @JsonProperty("errors")
    private HashMap<String, String> errors;
}
