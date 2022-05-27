package main.service;

import main.api.response.ErrorsResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class ImageDownloadService{

    private static final Path rootLocation = Paths.get("blog/upload");
    private static final Pattern FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpeg)$");

    public static ErrorsResponse postImage(MultipartFile image) throws IOException {


        String filename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));

        int maxSizeImage = 5;
        // максимальный размер в Мбайтах
        ErrorsResponse errorsResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();

        if (!FILE_PATTERN.matcher(filename).matches()) {
            errors.put("image", "Введенный фаил не совпадает с кодировкой .jpg .png");
        }



        if ((double) image.getSize() / (1024 * 1024) > maxSizeImage) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }


        if (!errors.isEmpty()) {
            errorsResponse.setErrors(errors);
            errorsResponse.setResult(false);
        } else {
            InputStream inputStream = image.getInputStream();
            Path randomPath = Paths.get(getImageGenerationPath(4));
            Path uploadPath = rootLocation.resolve(randomPath);
            Path fullFilePath = uploadPath.resolve(filename);

            Files.createDirectories(fullFilePath);

            Files.copy(inputStream, fullFilePath, StandardCopyOption.REPLACE_EXISTING);
            String finishPath = rootLocation.relativize(fullFilePath).toString().replace("\\","/");
            errorsResponse.setImage(finishPath);
        }
        return errorsResponse;
    }

    private static String getImageGenerationPath(int nameLength) {
        return new StringBuilder("src/main/upload/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").toString();
    }
}
