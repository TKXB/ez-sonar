package org.sonar.java.checks.spring;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class MyUuidCheckTest {
    @Test
    public void test() {
        JavaCheckVerifier.verify("src/test/files/checks/spring/RequestMappingMethodPublicCheck.java", new EzvizUuidCheck());
        JavaCheckVerifier.verifyNoIssueWithoutSemantic("src/test/files/checks/spring/RequestMappingMethodPublicCheck.java",
                new EzvizUuidCheck());
    }

}