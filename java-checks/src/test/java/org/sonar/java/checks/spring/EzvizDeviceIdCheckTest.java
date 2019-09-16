package org.sonar.java.checks.spring;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class EzvizDeviceIdCheckTest {
    @Test
    public void test() {
        JavaCheckVerifier.verify("src/test/files/checks/spring/EzvizAPICheck2.java", new EzvizDeviceIdCheck());
        //JavaCheckVerifier.verifyNoIssueWithoutSemantic("src/test/files/checks/spring/EzvizAPICheck.java",
        //        new EzvizDeviceIdCheck());
    }
}
