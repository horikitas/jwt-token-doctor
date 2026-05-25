package org.horikitas.tokendoctor.service.rules;
import org.horikitas.tokendoctor.model.DecodedJwt;
import org.horikitas.tokendoctor.model.Finding;
import org.horikitas.tokendoctor.model.Severity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
public class AlgNoneRule implements JwtRule {

    @Override
    public List<Finding> evaluate(DecodedJwt jwt) {
        Object alg = jwt.header().get("alg");

        if ("none".equalsIgnoreCase(String.valueOf(alg))) {
            return List.of(new Finding(
                    Severity.CRITICAL,
                    "ALG_NONE",
                    "Token uses alg=none, which means it is unsigned.",
                    "Do not accept unsigned JWTs. Use a secure signing algorithm such as RS256 or ES256."
            ));
        }

        return List.of();
    }
}
