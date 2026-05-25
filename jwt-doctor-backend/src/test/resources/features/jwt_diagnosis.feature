Feature: JWT diagnosis

  Scenario: Reject malformed JWT tokens
    When I diagnose the token "not-a-jwt"
    Then the diagnosis response is successful
    And the response reports the token format as invalid
    And the response contains finding code "INVALID_FORMAT"
    And the response severity is "CRITICAL"

  Scenario: Flag unsigned tokens
    Given a JWT with header:
      """
      {"alg":"none","typ":"JWT"}
      """
    And payload:
      """
      {"sub":"user-123","exp":4102444800}
      """
    When I diagnose the generated JWT
    Then the diagnosis response is successful
    And the response reports the token format as valid
    And the response contains finding code "ALG_NONE"
    And the response severity is "CRITICAL"

  Scenario: Flag tokens without expiration
    Given a JWT with header:
      """
      {"alg":"RS256","typ":"JWT"}
      """
    And payload:
      """
      {"sub":"user-123","iat":4102441200}
      """
    When I diagnose the generated JWT
    Then the diagnosis response is successful
    And the response reports the token format as valid
    And the response contains finding code "EXP_MISSING"
    And the response severity is "HIGH"

  Scenario: Flag long-lived tokens
    Given a JWT with header:
      """
      {"alg":"RS256","typ":"JWT"}
      """
    And payload:
      """
      {"sub":"user-123","iat":4102441200,"exp":4102700400}
      """
    When I diagnose the generated JWT
    Then the diagnosis response is successful
    And the response reports the token format as valid
    And the response contains finding code "LONG_LIVED_TOKEN"
    And the response severity is "MEDIUM"
