package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCodes;
import main.repositories.CaptchaRepository;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;


    public CaptchaResponse getCaptcha() throws IOException {

        captchaRepository.findAll().forEach(captcha -> {
            if (captcha.getTime().isBefore(LocalDateTime.now().minusHours(1))) {
                captchaRepository.delete(captcha);
            }
        });
        StringBuilder image = new StringBuilder("data:image/png;base64, ");
        CaptchaCodes captchaCodes = new CaptchaCodes();
        LocalDateTime time = LocalDateTime.now();
        Cage cage = new GCage();
        String captchaCode = cage.getTokenGenerator().next();
        String secretCode = cage.getTokenGenerator().next();

        OutputStream os = new FileOutputStream("captcha.jpg", false);
        cage.draw(captchaCode, os);
        os.flush();
        os.close();

        byte[] captcha = Files.readAllBytes(Paths.get("captcha.jpg"));
        Files.delete(Paths.get("captcha.jpg"));
        String encodedCaptcha = DatatypeConverter.printBase64Binary(captcha);
        image.append(encodedCaptcha);

        captchaCodes.setTime(time);
        captchaCodes.setCode(captchaCode);
        captchaCodes.setSecretCode(secretCode);
        captchaRepository.save(captchaCodes);

        return CaptchaResponse.builder()
                .secret(captchaCode)
                .image(image.toString())
                .build();
    }
}
