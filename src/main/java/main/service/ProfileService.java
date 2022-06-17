package main.service;

import main.api.request.MyProfileRequest;
import main.api.response.ErrorsResponse;
import main.model.User;
import main.other.RegularExpressions;
import main.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    private static int folderName = 0;
    private final UserRepository userRepository;


    @Autowired
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ErrorsResponse profileImageEdit(byte[] photo, String name, String email, String password,
                                      Integer removePhoto, Principal principal, HttpServletRequest request) throws IOException {
        System.out.println(photo.toString());
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).get();


        errors.putAll(checkEmail(email, user.getEmail(), errors));
        errors.putAll(checkName(email, errors));

        if (!errors.isEmpty()) {
            errorsResponse.setResult(false);
            errorsResponse.setErrors(errors);
            return errorsResponse;
        }

            System.out.println("фаил передал");
            String path = "upload/" + RandomStringUtils.randomAlphabetic(5);
            Files.createDirectories(Paths.get(path));
            File finalImageFolder = new File(path);
            InputStream is = new ByteArrayInputStream(photo);
            BufferedImage bufferedImage = ImageIO.read(is);
            bufferedImage = Scalr.resize(bufferedImage,Scalr.Method.SPEED, 36 * 2, 36 * 2);
            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, 36, 36);

            ImageIO.write(bufferedImage,"jpg", finalImageFolder);
            user.setPhoto(path + ".jpg");


        if (password != null) {
            errors = checkPassword(password, errors);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(passwordEncoder.encode(password).replaceFirst("\\{bcrypt}", ""));
        }

        if (removePhoto == 1) {
            user.setPhoto("");
        }

        errorsResponse.setResult(true);
        user.setName(name);
        user.setEmail(email);
        userRepository.saveAndFlush(user);

        return errorsResponse;
    }

    public ErrorsResponse profileEdit(MyProfileRequest request, Principal principal) throws IOException {
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).get();


        errors.putAll(checkEmail(request.getEmail(), user.getEmail(), errors));
        errors.putAll(checkName(request.getName(), errors));

        if (!errors.isEmpty()) {
            errorsResponse.setResult(false);
            errorsResponse.setErrors(errors);
            return errorsResponse;
        }

        if (request.getPassword() != null) {
            errors = checkPassword(request.getPassword(), errors);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(passwordEncoder.encode(request.getPassword()).replaceFirst("\\{bcrypt}", ""));
        }

        if (request.getRemovePhoto() == 1) {
            user.setPhoto("");
        }

        errorsResponse.setResult(true);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.saveAndFlush(user);

        return errorsResponse;
    }


    private HashMap<String, String> checkEmail(String email, String userEmail,
                                               HashMap<String, String> errors) {
        if (userRepository.findByEmail(email).isPresent()
                && !email.equals(userEmail)) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!email.matches(RegularExpressions.getRegularEmail())) {
            errors.put("email", "Введен неверный email");
        }
        return errors;
    }

    private HashMap<String, String> checkName(String name, HashMap<String, String> errors) {
        if ( name.length() < 3) {
            errors.put("name", "Имя указано неверно");
        }
        return errors;
    }

    private HashMap<String, String> checkPassword(String password, HashMap<String, String> errors) {
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        return errors;
    }
}
