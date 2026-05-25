package org.horikitas.tokendoctor.service.rules;


import org.horikitas.tokendoctor.model.DecodedJwt;
import org.horikitas.tokendoctor.model.Finding;

import java.util.List;

public interface JwtRule {
    List<Finding> evaluate(DecodedJwt jwt);
}
