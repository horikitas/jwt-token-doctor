package org.horikitas.tokendoctor;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }

        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
