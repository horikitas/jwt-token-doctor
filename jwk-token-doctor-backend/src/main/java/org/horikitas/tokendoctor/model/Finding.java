package org.horikitas.tokendoctor.model;

public record Finding(
        Severity severity,
        String code,
        String message,
        String recommendation
) {
}
