package org.horikitas.tokendoctor.model;

import java.util.Map;

public record DecodedJwt(
        Map<String, Object> header,
        Map<String, Object> payload,
        String signature
) {
}