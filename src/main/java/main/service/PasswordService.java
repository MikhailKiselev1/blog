package main.service;

import main.api.request.EditPasswordRequest;
import main.api.response.ErrorsResponse;
import main.model.CaptchaCodes;
import main.model.User;
import main.repositories.CaptchaRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PasswordService {

    private final CaptchaRepository captchaRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordService(CaptchaRepository captchaRepository, UserRepository userRepository) {
        this.captchaRepository = captchaRepository;
        this.userRepository = userRepository;
    }

    public ErrorsResponse getPassword(EditPasswordRequest request) {
        User user = userRepository.findByCode(request.getCode()).orElse(null);
        CaptchaCodes captchaCodes = captchaRepository.findByCode(request.getCaptcha()).orElse(null);
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();

        if (user == null) {
            errors.put("code", "ссылка на восстановления пароля устарела.\n" +
                    "<a href=\n" +
                    "\"/auth/restore\">Запросить ссылку снова</a>");
        }
        if (!captchaCodes.getSecretCode().equals(request.getCaptchaSecret())) {
            errors.put("captcha", "код с картинки введен неверно");
        }
        if (request.getPassword().length() < 6) {
            errors.put("password", "пароль короче 6 символов");
        }
        if (errors.size() > 0) {
            errorsResponse.setResult(false);
            errorsResponse.setErrors(errors);
        } else {
            errorsResponse.setResult(true);
        }
        return errorsResponse;
    }
}
