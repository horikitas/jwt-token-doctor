package org.horikitas.tokendoctor.model;

import jakarta.validation.constraints.NotBlank;

public record TokenDiagnosisRequest(@NotBlank(message = "Input Token must not be blank") String token) {}
