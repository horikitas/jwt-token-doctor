package org.horikitas.tokendoctor.service.rules;
import org.horikitas.tokendoctor.model.DecodedJwt;
import org.horikitas.tokendoctor.model.Finding;
import org.horikitas.tokendoctor.model.Severity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
public class MissingExpRule implements JwtRule {

    @Override
    public List<Finding> evaluate(DecodedJwt jwt) {
        if (!jwt.payload().containsKey("exp")) {
            return List.of(new Finding(
                    Severity.HIGH,
                    "EXP_MISSING",
                    "Token does not contain an exp claim.",
                    "Add an exp claim so the token has a clear expiration time."
            ));
        }

        return List.of();
    }
}