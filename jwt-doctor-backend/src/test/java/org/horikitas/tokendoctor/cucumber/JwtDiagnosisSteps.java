package org.horikitas.tokendoctor.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class JwtDiagnosisSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String headerJson;
    private String payloadJson;
    private MvcResult result;
    private JsonNode response;

    @Given("a JWT with header:")
    public void aJwtWithHeader(String headerJson) {
        this.headerJson = headerJson;
    }

    @And("payload:")
    public void payload(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    @When("I diagnose the generated JWT")
    public void iDiagnoseTheGeneratedJwt() throws Exception {
        String token = encode(headerJson) + "." + encode(payloadJson) + ".test-signature";
        diagnose(token);
    }

    @When("I diagnose the token {string}")
    public void iDiagnoseTheToken(String token) throws Exception {
        diagnose(token);
    }

    @Then("the diagnosis response is successful")
    public void theDiagnosisResponseIsSuccessful() {
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @And("the response reports the token format as invalid")
    public void theResponseReportsTheTokenFormatAsInvalid() {
        assertThat(response.path("validFormat").asBoolean()).isFalse();
    }

    @And("the response reports the token format as valid")
    public void theResponseReportsTheTokenFormatAsValid() {
        assertThat(response.path("validFormat").asBoolean()).isTrue();
    }

    @And("the response contains finding code {string}")
    public void theResponseContainsFindingCode(String code) {
        assertThat(response.path("findings"))
                .anySatisfy(finding -> assertThat(finding.path("code").asText()).isEqualTo(code));
    }

    @And("the response severity is {string}")
    public void theResponseSeverityIs(String severity) {
        assertThat(response.path("severity").asText()).isEqualTo(severity);
    }

    private void diagnose(String token) throws Exception {
        result = mockMvc.perform(post("/api/v1/tokens/diagnose")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("token", token))))
                .andReturn();
        response = objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String encode(String json) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }
}
