package org.horikitas.tokendoctor.model;

import java.util.List;

public record TokenDiagnosis(
        boolean validFormat,
        DecodedJwt decodedJwt,
        List<Finding> findings,
        int riskScore,
        Severity severity
) {
}
