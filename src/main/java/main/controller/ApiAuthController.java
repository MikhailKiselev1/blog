package main.controller;

import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
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
    public RegisterResponse register(@PathVariable @Value("e_mail") String email, @PathVariable String password,
                                     @PathVariable String name, @PathVariable String captcha,
                                     @PathVariable @Value("captcha_secret") String captchaSecret) {
        return registerService.getRegister(email, password, name, captcha, captchaSecret);
    }
}
