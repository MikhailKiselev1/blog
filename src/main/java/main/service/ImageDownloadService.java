package main.service;

import main.api.response.ErrorsResponse;
import main.model.User;
import main.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class ImageDownloadService{

    private final Path rootLocation = Paths.get("blog/upload");
    private static final Pattern FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpeg)$");
    private static UserRepository userRepository;

    /**
     * Service class handling image-related operations.
     */
    @Autowired
    public ImageDownloadService(UserRepository userRepository) {
        ImageDownloadService.userRepository = userRepository;
    }

    /**
     * Handles the upload of an image.
     *
     * @param image the image file to upload
     * @return ErrorsResponse indicating the status of the image upload
     * @throws IOException if an I/O error occurs
     */
    public ErrorsResponse postImage(MultipartFile image) throws IOException {


        String filename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));

        int maxSizeImage = 5;
        // максимальный размер в Мбайтах
        HashMap<String, String> errors = new HashMap<>();

        if (!FILE_PATTERN.matcher(filename).matches()) {
            errors.put("image", "Введенный фаил не совпадает с кодировкой .jpg .png");
        }



        if ((double) image.getSize() / (1024 * 1024) > maxSizeImage) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }


        if (!errors.isEmpty()) {
            return ErrorsResponse.builder()
                    .errors(errors)
                    .result(false)
                    .build();
        } else {
            InputStream inputStream = image.getInputStream();
            Path randomPath = Paths.get(getImageGenerationPath(4));
            Path uploadPath = rootLocation.resolve(randomPath);
            Path fullFilePath = uploadPath.resolve(filename);

            Files.createDirectories(fullFilePath);

            Files.copy(inputStream, fullFilePath, StandardCopyOption.REPLACE_EXISTING);
            String finishPath = rootLocation.relativize(fullFilePath).toString().replace("\\","/");
            return ErrorsResponse.builder()
                    .image(finishPath)
                    .build();
        }
    }

    /**
     * Handles the upload of an image with user association.
     *
     * @param image    the image file to upload
     * @param request  the HTTP servlet request
     * @param principal the authenticated user principal
     * @return ErrorsResponse indicating the status of the image upload
     * @throws IOException if an I/O error occurs
     */
    public static ErrorsResponse addImage(MultipartFile image,
                                HttpServletRequest request,
                                Principal principal) throws IOException {

        String filename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));


        int maxSizeImage = 5;
        // максимальный размер в Мбайтах

        HashMap<String, String> errors = new HashMap<>();

        if (!FILE_PATTERN.matcher(filename).matches()) {
            errors.put("image", "Введенный фаил не совпадает с кодировкой .jpg .png");
        }



        if ((double) image.getSize() / (1024 * 1024) > maxSizeImage) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }


        if (!errors.isEmpty()) {
            return ErrorsResponse.builder()
                    .errors(errors)
                    .result(false)
                    .build();

        } else {
            String path = "upload/" + principal.hashCode() + ".jpg";
            String realPath = request.getServletContext().getRealPath(path);

            File file = new File(realPath);
            FileUtils.writeByteArrayToFile(file, image.getBytes());

            User user = userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow();
            user.setPhoto(path);
            userRepository.save(user);
            return ErrorsResponse.builder()
                    .image(path)
                    .build();
        }
    }

/**
 * Generates a random path for image storage.
 *
 * @param nameLength the length of the random path name
 * @return the generated image storage path
 */
    public static String getImageGenerationPath(int nameLength) {
        return new StringBuilder("src/main/upload/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").toString();
    }
}
