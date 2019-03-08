package org.sonar.java.checks.security;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class EzvizHTTPUrlCheckTest {
    @Test
    public void test() {
        JavaCheckVerifier.verify("src/test/files/checks/security/EzvizHTTPUrlCheck.java", new EzvizHTTPUrlCheck());
        JavaCheckVerifier.verifyNoIssueWithoutSemantic("src/test/files/checks/security/EzvizHTTPUrlCheck.java", new EzvizHTTPUrlCheck());
    }
}
