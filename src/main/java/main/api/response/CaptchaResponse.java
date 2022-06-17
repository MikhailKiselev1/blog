package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
public class CaptchaResponse {

    private String secret;

    private String image;
}
