package org.horikitas.tokendoctor.model;

import java.util.List;
import java.util.Map;

public record TokenDiagnosisResponse(
        boolean validFormat,
        Map<String, Object> header,
        Map<String, Object> payload,
        List<Finding> findings,
        int riskScore,
        String severity
) {
    public static TokenDiagnosisResponse from(TokenDiagnosis diagnosis) {
        return new TokenDiagnosisResponse(
                diagnosis.validFormat(),
                diagnosis.decodedJwt() != null ? diagnosis.decodedJwt().header() : null,
                diagnosis.decodedJwt() != null ? diagnosis.decodedJwt().payload() : null,
                diagnosis.findings(),
                diagnosis.riskScore(),
                diagnosis.severity().name()
        );
    }
}