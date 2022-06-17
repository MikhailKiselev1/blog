package main.other;

import org.apache.commons.lang3.RandomStringUtils;

public class RegularExpressions {

    public static String getRegularName() {
        return "[А-ЯA-Z][а-яa-z]+";
    }

    public static String getRegularEmail() {
        return "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    }

    public static String getImageGenerationPath(int nameLength) {
        return new StringBuilder("src/main/upload/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").append(RandomStringUtils.randomAlphabetic(nameLength))
                .append("/").toString();
    }
}
