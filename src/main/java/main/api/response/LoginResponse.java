package main.api.response;

import lombok.Data;
import main.api.response.dto.LoginUserDto;

@Data
public class LoginResponse {

    private boolean result;

    private LoginUserDto user;
}
