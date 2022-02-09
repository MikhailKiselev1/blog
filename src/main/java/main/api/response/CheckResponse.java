package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.User;

@Data
public class CheckResponse {

    @JsonProperty("result")
    boolean result = false;

    @JsonProperty("user")
    CheckUserResponse checkUserResponse = null;
}
