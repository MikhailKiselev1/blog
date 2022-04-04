package main.service;

import main.api.response.RegisterResponse;
import main.api.response.dto.RegisterDto;
import main.model.CaptchaCodes;
import main.model.User;
import main.repositories.CaptchaRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    public RegisterResponse getRegister(RegisterDto loginForm) {

        RegisterResponse response = new RegisterResponse();
        List<User> users = userRepository.findAll();
        HashMap<String, String> errors = new HashMap<>();

        users.forEach(user -> {
            if(user.getEmail().equals(loginForm.getEmail())) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if(!loginForm.getName().matches("[А-Я][а-я]+")) {
            errors.put("name", "Имя указано неверно");
        }
        if (loginForm.getPassword().length() < 5) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if(!loginForm.getCaptcha().equals(loginForm.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введен неверно");
        }
        if(errors.size() > 0) {
            response.setResult(false);
            response.setErrors(errors);
        }
        else {
            User user = new User();
            user.setIsModerator(0);
            user.setRegTime(LocalDateTime.now());
            user.setName(loginForm.getName());
            user.setEmail(loginForm.getEmail());
            user.setPassword(loginForm.getPassword());
            userRepository.save(user);
            response.setResult(true);
        }
        return response;
    }
}
