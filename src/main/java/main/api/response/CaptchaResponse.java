package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CaptchaResponse {

    @JsonProperty("secret")
    private String secret;

    @JsonProperty("image")
    private String image;
}
