package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.EditPasswordRequest;
import main.api.request.RestoreRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.ErrorsResponse;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.RestoreResponse;
import main.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * REST Controller managing authentication and authorization.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final RestoreService restoreService;
    private final PasswordService passwordService;


    /**
     * Service for handling captchas.
     */
    @GetMapping("/captcha")
    public CaptchaResponse captcha() throws IOException {
        return captchaService.getCaptcha();
    }

    /**
     * Service for user registration.
     *
     * @param registerForm Registration request.
     * @return Response containing errors if any.
     */
    @PostMapping("/register")
    public ErrorsResponse register(@RequestBody RegisterRequest registerForm) {
        return registerService.getRegister(registerForm);
    }

    /**
     * Service for user login.
     *
     * @param loginRequest Login request.
     * @return Response containing login information.
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return loginService.getUser(loginRequest);
    }


    /**
     * Logout endpoint.
     *
     * @param request  HTTP servlet request.
     * @param response HTTP servlet response.
     * @return Forward to the home page.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "forward:/";
    }

    /**
     * Check user authentication status.
     *
     * @param principal User principal.
     * @return Response containing login information.
     */
    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(LoginResponse.builder().build());
        }
        return ResponseEntity.ok(loginService.getCheck(principal.getName()));
    }

    /**
     * Service for restoring user account.
     *
     * @param request Restore request.
     * @return Response containing restore information.
     */
    @PostMapping("/restore")
    public RestoreResponse restore(@RequestBody RestoreRequest request) {
        return restoreService.getRestore(request);
    }

    /**
     * Service for editing user password.
     *
     * @param request Edit password request.
     * @return Response containing errors if any.
     */
    @PostMapping("/password")
    public ErrorsResponse editPassword(@RequestBody EditPasswordRequest request) {
        return passwordService.getPassword(request);
    }


}
