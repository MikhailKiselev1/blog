package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.dto.CheckUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class CheckResponse {

    @JsonProperty("result")
    boolean result;

    @Autowired
    @JsonProperty("user")
    CheckUserDto checkUserDto;
}
