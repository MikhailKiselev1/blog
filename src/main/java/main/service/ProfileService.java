package main.service;

import main.api.request.profileRequest.MyProfileRequest;
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.Random;

@Service
public class ProfileService {

    private static int folderName = 0;
    private final UserRepository userRepository;


    @Autowired
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ErrorsResponse profileEdit(MyProfileRequest request, Principal principal) throws IOException {
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).get();


        errors = checkEmail(request.getEmail(), user.getEmail(), errors);
        errors = checkName(request.getName(), errors);

//        if (request.getPhoto() != null && errors.isEmpty()) {
//            System.out.println("фаил передал");
//            BufferedImage bufferedImage = ImageIO.read(request.getPhoto());
//            BufferedImage newBufferedImage = Scalr.resize(bufferedImage,Scalr.Method.SPEED, 36 * 2, 36 * 2);
//            newBufferedImage = Scalr.resize(newBufferedImage, Scalr.Method.ULTRA_QUALITY, 36, 36);
//            ImageIO.write(newBufferedImage,"jpg", finalImageFolder);
//        }

        if (request.getPassword() != null && errors.isEmpty()) {
            errors = checkPassword(request.getPassword(), errors);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(passwordEncoder.encode(request.getPassword()).replaceFirst("\\{bcrypt}", ""));
        }

        if (request.getRemovePhoto() == 1 && errors.isEmpty()) {
            user.setPhoto("");
        }

        if (!errors.isEmpty()) {
            errorsResponse.setResult(false);
            errorsResponse.setErrors(errors);
        }
        else {
            errorsResponse.setResult(true);
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            userRepository.save(user);
        }

        return errorsResponse;
    }


    public ErrorsResponse postImage(MultipartFile image) throws IOException {

        int maxSizeImage = 5;
        // максимальный размер в Мбайтах
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();

//        if (!image.getOriginalFilename().matches(".+\\.png") || !image.getOriginalFilename().matches(".+\\.jpg")) {
//            errors.put("image", "Неверный формат изображения");
//            errorsResponse.setErrors(errors);
//        }
        if ((double) image.getSize()/(1024*1024) > maxSizeImage) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }


        if (!errors.isEmpty()) {
            errorsResponse.setErrors(errors);
            errorsResponse.setResult(false);
        }
        else {
            String fileName = image.getOriginalFilename();
            String location = getImageGenerationPath();
            File pathFile = new File(location);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());


            pathFile = new File(location + fileName);
            ImageIO.write(bufferedImage, "jpg", pathFile);


            System.out.println(location + fileName);
            System.out.println(pathFile.getPath());
            errorsResponse.setImage(location + fileName);
        }
        return errorsResponse;
    }

    private static String getImageGenerationPath() {
        return new StringBuilder("src/main/upload/").append(RandomStringUtils.randomAlphabetic(4))
                .append("/").append(RandomStringUtils.randomAlphabetic(4))
                .append("/").append(RandomStringUtils.randomAlphabetic(4))
                .append("/").toString();
    }

    private HashMap<String, String> checkEmail(String email, String userEmail,
                                               HashMap<String, String> errors) {
        if(userRepository.findByEmail(email).isPresent()
                && !email.equals(userEmail)) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!email.matches(RegularExpressions.getRegularEmail())) {
            errors.put("email", "Введен неверный email");
        }
        return errors;
    }

    private HashMap<String, String> checkName(String name, HashMap<String, String> errors) {
        if(!name.matches(RegularExpressions.getRegularName()) || name.length() < 3) {
            errors.put("name", "Имя указано неверно");
        }
        return errors;
    }

    private HashMap<String, String> checkPassword(String password, HashMap<String, String> errors) {
        if(password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        return errors;
    }
}
