package org.sonar.java.checks.spring;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import static org.junit.Assert.*;

public class MyUuidCheckTest {
    @Test
    public void test() {
        JavaCheckVerifier.verify("src/test/files/checks/spring/RequestMappingMethodPublicCheck.java", new MyUuidCheck());
        JavaCheckVerifier.verifyNoIssueWithoutSemantic("src/test/files/checks/spring/RequestMappingMethodPublicCheck.java",
                new MyUuidCheck());
    }

}