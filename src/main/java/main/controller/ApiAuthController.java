package main.controller;

import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.api.response.dto.RegisterDto;
import main.service.CaptchaService;
import main.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private CaptchaService captchaService;
    private RegisterService registerService;

    @Autowired
    public ApiAuthController(CaptchaService captchaService, RegisterService registerService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
    }

    @GetMapping("/captcha")
    public CaptchaResponse captcha() throws IOException {
        return captchaService.getCaptcha();
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterDto loginForm) {
        return registerService.getRegister(loginForm);
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}
