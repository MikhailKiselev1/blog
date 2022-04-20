package main.service;

import main.api.response.ErrorsResponse;
import main.api.request.RegisterRequest;
import main.model.User;
import main.repositories.CaptchaRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public ErrorsResponse getRegister(RegisterRequest registerForm) {

        ErrorsResponse response = new ErrorsResponse();
        List<User> users = userRepository.findAll();
        HashMap<String, String> errors = new HashMap<>();

        users.forEach(user -> {
            if(user.getEmail().equals(registerForm.getEmail())) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if(!registerForm.getName().matches("[А-Я][а-я]+")) {
            errors.put("name", "Имя указано неверно");
        }
        if (registerForm.getPassword().length() < 5) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if(!registerForm.getCaptcha().equals(registerForm.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введен неверно");
        }
        if(errors.size() > 0) {
            response.setResult(false);
            response.setErrors(errors);
        }
        else {
            User user = saveUser(registerForm);
            userRepository.save(user);
            response.setResult(true);
        }
        return response;
    }

    private User saveUser(RegisterRequest registerForm) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User user = new User();
        user.setIsModerator(0);
        user.setRegTime(LocalDateTime.now());
        user.setName(registerForm.getName());
        user.setEmail(registerForm.getEmail());

        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        return user;
    }


}
