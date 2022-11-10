package main.service;

import main.api.response.ErrorsResponse;
import main.api.request.RegisterRequest;
import main.model.User;
import main.other.RegularExpressions;
import main.repositories.UserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class RegisterService {

    private final UserRepository userRepository;


    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ErrorsResponse getRegister(RegisterRequest registerForm) {

        if (getErrors(registerForm).size() != 0) {
            return ErrorsResponse.builder()
                    .result(false)
                    .errors(getErrors(registerForm))
                    .build();
        }

        User user = saveUser(registerForm);
        userRepository.save(user);
        return ErrorsResponse.builder()
                .result(true)
                .build();

    }

    private User saveUser(RegisterRequest registerForm) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User user = new User();
        user.setIsModerator(0);
        user.setRegTime(LocalDateTime.now());
        user.setName(registerForm.getName());
        user.setEmail(registerForm.getEmail());

        user.setPassword(passwordEncoder.encode(registerForm.getPassword())
                .replaceFirst("\\{bcrypt}", ""));
        return user;
    }

    private HashMap<String, String> getErrors(RegisterRequest registerForm) {

        HashMap<String, String> errors = new HashMap<>();
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            if (user.getEmail().equals(registerForm.getEmail())) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        });
        if (!registerForm.getName().matches(RegularExpressions.getRegularName())) {
            errors.put("name", "Имя указано неверно");
        }
        if (registerForm.getPassword().length() < 5) {
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (!registerForm.getCaptcha().equals(registerForm.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введен неверно");
        }

        return errors;
    }
}
