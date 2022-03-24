package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCodes;
import main.repositories.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CaptchaService {


    private CaptchaCodes captchaCodes;
    private CaptchaResponse captchaResponse;
//    @Autowired
    private CaptchaRepository captchaRepository;


    public CaptchaResponse getCaptcha() {
        StringBuilder image = new StringBuilder("data:image/png;base64, ");
        captchaResponse = new CaptchaResponse();
        captchaCodes = new CaptchaCodes();
        LocalDateTime time = LocalDateTime.now();

        Cage cage = new GCage();
        String captchaImage = cage.getTokenGenerator().next();

        image.append(captchaImage);
        captchaCodes.setTime(time);
        captchaRepository.save(captchaCodes);
        captchaResponse.setSecret(captchaCodes.getSecretCode());
        captchaResponse.setImage(image.toString());
        return captchaResponse;
    }
}
