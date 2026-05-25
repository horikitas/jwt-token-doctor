package org.horikitas.tokendoctor.service.rules;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.horikitas.tokendoctor.Utils;
import org.horikitas.tokendoctor.model.DecodedJwt;
import org.horikitas.tokendoctor.model.Finding;
import org.horikitas.tokendoctor.model.Severity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Order(4)
@RequiredArgsConstructor
@Component
public class LongLivedTokenRule implements JwtRule {

    private final @NonNull Clock clock;

    private static final Duration MAX_RECOMMENDED_LIFETIME = Duration.ofHours(24);

    @Override
    public List<Finding> evaluate(DecodedJwt jwt) {
        Object iatValue = jwt.payload().get("iat");
        Object expValue = jwt.payload().get("exp");

        if (iatValue == null || expValue == null) {
            return List.of();
        }

        Long iat = Utils.toLong(iatValue);
        Long exp = Utils.toLong(expValue);

        if (iat == null || exp == null) {
            return List.of();
        }

        Instant issuedAt = Instant.ofEpochSecond(iat);
        Instant expiry = Instant.ofEpochSecond(exp);
        Duration lifetime = Duration.between(issuedAt, expiry);

        if (lifetime.isNegative() || lifetime.isZero()) {
            return List.of(new Finding(
                    Severity.MEDIUM,
                    "INVALID_TOKEN_LIFETIME",
                    "Token expiry time is not after issued-at time.",
                    "Ensure exp is later than iat."
            ));
        }

        if (lifetime.compareTo(MAX_RECOMMENDED_LIFETIME) > 0) {
            return List.of(new Finding(
                    Severity.MEDIUM,
                    "LONG_LIVED_TOKEN",
                    "Token lifetime is longer than the recommended 24 hours.",
                    "Use shorter-lived access tokens and refresh tokens where appropriate."
            ));
        }

        return List.of();
    }
}
