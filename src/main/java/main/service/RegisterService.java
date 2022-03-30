package main.service;

import main.api.response.RegisterResponse;
import main.model.CaptchaCodes;
import main.model.User;
import main.repositories.CaptchaRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    public RegisterResponse getRegister(String email, String password, String name,
                                        String captcha, String captchaSecret) {
        
        RegisterResponse response = new RegisterResponse();
        List<User> users = userRepository.findAll();
        List<CaptchaCodes> captchaCodes = captchaRepository.findAll();
        HashMap<String, String> errors = new HashMap<>();
        boolean result = true;

        users.forEach(user -> {
            if(user.getEmail().equals(email)) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if(!name.matches("[А-Я][а-я]+")) {
            errors.put("name", "Имя указано неверно");
        }
        if (password.length() < 5) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        captchaCodes.forEach(c -> {
            if(c.getCode().equals(captcha)) {
                if(!c.getSecretCode().equals(captchaSecret)) {
                    errors.put("captcha", "Код с картинки введен неверно");
                }
            }
        });
        if(errors.size() > 0) {
            result = false;
            response.setErrors(errors);
        }

        response.setResult(result);
        return response;
    }
}
