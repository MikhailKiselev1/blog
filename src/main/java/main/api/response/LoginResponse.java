package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import main.api.response.dto.LoginUserDto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class LoginResponse {

    private boolean result;

    @JsonInclude(NON_NULL)
    private LoginUserDto user;
}
