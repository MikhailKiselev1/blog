package main.other;

public class RegularExpressions {

    public static String getRegularName() {
        return "[А-Я][а-я]+";
    }

    public static String getRegularEmail() {
        return "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    }
}
