package org.horikitas.tokendoctor.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.horikitas.tokendoctor.model.DecodedJwt;
import org.horikitas.tokendoctor.model.Finding;
import org.horikitas.tokendoctor.model.Severity;
import org.horikitas.tokendoctor.model.TokenDiagnosis;
import org.horikitas.tokendoctor.service.rules.JwtRule;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtDiagnosisService {

    private final @NonNull ObjectMapper objectMapper;

    private final @NonNull List<JwtRule> jwtRules;

    public TokenDiagnosis diagnose(String token) {
        if (token == null || token.isBlank()) {
            return invalidToken("Token must not be blank.");
        }

        String[] parts = token.split("\\.", -1);

        if (parts.length != 3) {
            return invalidToken("Token must have exactly three parts: header.payload.signature.");
        }

        try {
            Map<String, Object> header = decodeJsonPart(parts[0]);
            Map<String, Object> payload = decodeJsonPart(parts[1]);
            String signature = parts[2];

            DecodedJwt decodedJwt = new DecodedJwt(header, payload, signature);

            List<Finding> findings = jwtRules.stream()
                    .flatMap(rule -> rule.evaluate(decodedJwt).stream())
                    .toList();

            int riskScore = calculateRiskScore(findings);
            Severity severity = deriveSeverity(riskScore);

            return new TokenDiagnosis(
                    true,
                    decodedJwt,
                    findings,
                    riskScore,
                    severity
            );

        } catch (Exception ex) {
            log.debug("Failed to decode JWT", ex);
            return invalidToken("Token header or payload is not valid Base64URL encoded JSON.");
        }
    }

    private Map<String, Object> decodeJsonPart(String encodedPart) throws Exception {
        if (encodedPart == null || encodedPart.isBlank()) {
            throw new IllegalArgumentException("JWT part is blank");
        }

        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPart);
        String json = new String(decodedBytes, StandardCharsets.UTF_8);

        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    private int calculateRiskScore(List<Finding> findings) {
        int score = findings.stream()
                .mapToInt(finding -> switch (finding.severity()) {
                    case INFO -> 5;
                    case LOW -> 10;
                    case MEDIUM -> 25;
                    case HIGH -> 40;
                    case CRITICAL -> 70;
                })
                .sum();

        return Math.min(score, 100);
    }

    private Severity deriveSeverity(int riskScore) {
        if (riskScore >= 70) {
            return Severity.CRITICAL;
        }
        if (riskScore >= 40) {
            return Severity.HIGH;
        }
        if (riskScore >= 25) {
            return Severity.MEDIUM;
        }
        if (riskScore >= 10) {
            return Severity.LOW;
        }
        return Severity.INFO;
    }

    private TokenDiagnosis invalidToken(String message) {
        Finding finding = new Finding(
                Severity.CRITICAL,
                "INVALID_FORMAT",
                message,
                "Provide a valid JWT in header.payload.signature format."
        );

        return new TokenDiagnosis(
                false,
                null,
                List.of(finding),
                100,
                Severity.CRITICAL
        );
    }
}