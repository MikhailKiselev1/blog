package main.service;

import main.api.request.MyProfileRequest;
import main.api.response.ErrorsResponse;
import main.model.User;
import main.other.RegularExpressions;
import main.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
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

/**
 * Service responsible for handling user profile-related operations.
 */
@Service
public class ProfileService {

    private static int folderName = 0;
    private final UserRepository userRepository;


    @Autowired
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles profile image editing, including email, name, password, and photo.
     *
     * @param photo          the byte array representing the user's photo
     * @param name           the user's name
     * @param email          the user's email
     * @param password       the user's password
     * @param removePhoto    flag indicating whether to remove the user's photo
     * @param principal      the authenticated user principal
     * @param request        the HTTP servlet request
     * @return ErrorsResponse indicating the status of the profile image edit operation
     * @throws IOException if an IO exception occurs
     */
    public ErrorsResponse profileImageEdit(byte[] photo, String name, String email, String password,
                                      Integer removePhoto, Principal principal, HttpServletRequest request) throws IOException {
        System.out.println(photo.toString());
        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).get();

        errors.putAll(checkEmail(email, user.getEmail(), errors));
        errors.putAll(checkName(email, errors));

        if (!errors.isEmpty()) {

            return ErrorsResponse.builder()
                    .result(false)
                    .errors(errors)
                    .build();
        }

        addImage(photo, request, principal);

        if (password != null) {
            errors = checkPassword(password, errors);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(passwordEncoder.encode(password).replaceFirst("\\{bcrypt}", ""));
        }

        if (removePhoto == 1) {
            user.setPhoto("");
        }

        user.setName(name);
        user.setEmail(email);
        userRepository.saveAndFlush(user);

        return ErrorsResponse.builder().result(true).build();
    }

    /**
     * Adds a user image to the system.
     *
     * @param photo      the byte array representing the user's photo
     * @param request    the HTTP servlet request
     * @param principal  the authenticated user principal
     * @throws IOException if an IO exception occurs
     */
    public void addImage(byte[] photo,
                         HttpServletRequest request,
                         Principal principal) throws IOException {
        String path = "upload/" + principal.hashCode() + ".jpg";
        String realPath = request.getServletContext().getRealPath(path);

        File file = new File(realPath);
        FileUtils.writeByteArrayToFile(file, photo);

        User user = userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow();
        user.setPhoto(path);
        userRepository.save(user);
        System.out.println(userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow().getPhoto());
    }

    /**
     * Handles profile editing, including email, name, password, and photo removal.
     *
     * @param request    the MyProfileRequest containing details of the profile edit
     * @param principal  the authenticated user principal
     * @return ErrorsResponse indicating the status of the profile edit operation
     * @throws IOException if an IO exception occurs
     */
    public ErrorsResponse profileEdit(MyProfileRequest request, Principal principal) throws IOException {

        HashMap<String, String> errors = new HashMap<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).get();


        errors.putAll(checkEmail(request.getEmail(), user.getEmail(), errors));
        errors.putAll(checkName(request.getName(), errors));

        if (!errors.isEmpty()) {
            return ErrorsResponse.builder()
                    .result(false)
                    .errors(errors)
                    .build();
        }

        if (request.getPassword() != null) {
            errors = checkPassword(request.getPassword(), errors);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(passwordEncoder.encode(request.getPassword()).replaceFirst("\\{bcrypt}", ""));
        }

        if (request.getRemovePhoto() == 1) {
            user.setPhoto("");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.saveAndFlush(user);

        return ErrorsResponse
                .builder()
                .result(true)
                .build();
    }

    /**
     * Checks the provided email against the current user's email and validates its format.
     *
     * @param email      the email to be checked
     * @param userEmail  the current user's email
     * @param errors     the map to store validation errors
     * @return the updated errors map
     */
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

    /**
     * Checks the provided name for its length.
     *
     * @param name    the name to be checked
     * @param errors  the map to store validation errors
     * @return the updated errors map
     */
    private HashMap<String, String> checkName(String name, HashMap<String, String> errors) {
        if ( name.length() < 3) {
            errors.put("name", "Имя указано неверно");
        }
        return errors;
    }

    /**
     * Checks the provided password for its length.
     *
     * @param password  the password to be checked
     * @param errors    the map to store validation errors
     * @return the updated errors map
     */
    private HashMap<String, String> checkPassword(String password, HashMap<String, String> errors) {
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        return errors;
    }
}
