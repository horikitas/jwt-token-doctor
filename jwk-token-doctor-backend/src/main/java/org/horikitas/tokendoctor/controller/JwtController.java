package org.horikitas.tokendoctor.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.horikitas.tokendoctor.model.TokenDiagnosisRequest;
import org.horikitas.tokendoctor.model.TokenDiagnosisResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.horikitas.tokendoctor.model.TokenDiagnosis;
import org.horikitas.tokendoctor.service.JwtDiagnosisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tokens")
public class JwtController {

    private final @NonNull JwtDiagnosisService jwtDiagnosisService;

    @PostMapping("/diagnose")
    public ResponseEntity<TokenDiagnosisResponse> diagnose(
            @Valid @RequestBody TokenDiagnosisRequest request) {
        TokenDiagnosis diagnosis = jwtDiagnosisService.diagnose(request.token());
        return ResponseEntity.ok(TokenDiagnosisResponse.from(diagnosis));
    }

}
