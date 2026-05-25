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
import java.time.Instant;
import java.util.List;

@Order(3)
@RequiredArgsConstructor
@Component
public class TokenExpiredRule implements JwtRule {

    private final @NonNull Clock clock;

    @Override
    public List<Finding> evaluate(DecodedJwt jwt) {
        Object expValue = jwt.payload().get("exp");

        if (expValue == null) {
            return List.of();
        }

        Long exp = Utils.toLong(expValue);

        if (exp == null) {
            return List.of(new Finding(
                    Severity.MEDIUM,
                    "EXP_INVALID",
                    "Token contains an exp claim, but it is not a valid numeric timestamp.",
                    "Use a valid Unix timestamp in seconds for the exp claim."
            ));
        }

        Instant expiryTime = Instant.ofEpochSecond(exp);

        if (expiryTime.isBefore(clock.instant())) {
            return List.of(new Finding(
                    Severity.HIGH,
                    "TOKEN_EXPIRED",
                    "Token has already expired.",
                    "Refresh or regenerate the token."
            ));
        }

        return List.of();
    }
}
