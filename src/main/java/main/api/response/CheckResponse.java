package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.response.dto.CheckUserDto;

@Data
public class CheckResponse {

    @JsonProperty("result")
    boolean result;

//    @JsonProperty("user")
//    CheckUserDto checkUserDto;
}
