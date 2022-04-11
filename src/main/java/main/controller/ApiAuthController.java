package main.controller;

import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.RegisterResponse;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.service.CaptchaService;
import main.service.LoginService;
import main.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private CaptchaService captchaService;
    private RegisterService registerService;
    private LoginService loginService;


    @Autowired
    public ApiAuthController(CaptchaService captchaService, RegisterService registerService,
                             LoginService loginService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @GetMapping("/captcha")
    public CaptchaResponse captcha() throws IOException {
        return captchaService.getCaptcha();
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerForm) {
        return registerService.getRegister(registerForm);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.getUser(loginRequest);
    }
}
