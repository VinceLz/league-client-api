package com.hawolt.rman.util;

import java.util.Locale;

/**
 * Created: 05/01/2023 14:04
 * Author: Twitter @hawolt
 **/

public class Hex {
    public static String from(long id) {
        return from(id, 16);
    }

    public static String from(long id, int length) {
        StringBuilder builder = new StringBuilder(Long.toHexString(id).toUpperCase(Locale.ENGLISH));
        while (builder.length() > 0 && builder.charAt(0) == '0') builder.deleteCharAt(0);
        while (builder.length() < length) builder.insert(0, "0");
        return builder.toString();
    }
}
