package com.tikitaka.tikitaka.global.util;

public class CustomStringUtil {
    public static String hideName(String name) {
        if (name == null) {
            return null;
        } else if (name.length() == 1) {
            return "*";
        } else if (name.length() == 2) {
            return name.charAt(0) + "*";
        } else if (name.length() > 2) {
            return name.charAt(0)
                    + "*".repeat(name.length() - 2)
                    + name.charAt(name.length() - 1);
        }
        return name;
    }
}
