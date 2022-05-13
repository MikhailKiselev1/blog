package main.controller;

import main.api.request.EditPasswordRequest;
import main.api.request.RestoreRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.ErrorsResponse;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.RestoreResponse;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final RestoreService restoreService;
    private final PasswordService passwordService;


    @Autowired
    public ApiAuthController(CaptchaService captchaService, RegisterService registerService,
                             LoginService loginService, RestoreService restoreService, PasswordService passwordService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.loginService = loginService;
        this.restoreService = restoreService;
        this.passwordService = passwordService;
    }

    @GetMapping("/captcha")
    public CaptchaResponse captcha() throws IOException {
        return captchaService.getCaptcha();
    }

    @PostMapping("/register")
    public ErrorsResponse register(@RequestBody RegisterRequest registerForm) {
        return registerService.getRegister(registerForm);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.getUser(loginRequest);
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "index";
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new LoginResponse());
        }
        return ResponseEntity.ok(loginService.getCheck(principal.getName()));
    }

    @PostMapping("/restore")
    public RestoreResponse restore(@RequestBody RestoreRequest request) {
        return restoreService.getRestore(request);
    }

    @PostMapping("/password")
    public ErrorsResponse editPassword(@RequestBody EditPasswordRequest request) {
        return passwordService.getPassword(request);
    }


}
