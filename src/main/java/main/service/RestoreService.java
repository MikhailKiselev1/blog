package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.RestoreRequest;
import main.api.response.RestoreResponse;
import main.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling password restoration requests.
 */
@Service
@RequiredArgsConstructor
public class RestoreService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final String FROM_EMAIL_ADDRESS = "blogengine@mail.ru";
    private final String link = "https://kiselev-java.herokuapp.com/login/change-password/";

    /**
     * Processes a password restoration request.
     *
     * @param request the RestoreRequest containing user email for password restoration
     * @return RestoreResponse indicating the result of the restoration process
     */
    public RestoreResponse getRestore(RestoreRequest request) {
        RestoreResponse response = new RestoreResponse();
        response.setResult(false);
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            response.setResult(true);
            String hash = RandomStringUtils.randomAlphabetic(15);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL_ADDRESS);
            message.setTo(request.getEmail());
            message.setSubject ("Subject: Simple Mail");
            message.setText ("Ссылка для восстановления пароля: " + link + hash);

            mailSender.send(message);

            user.setCode(hash);
            userRepository.save(user);
        });
        return response;
    }
}
