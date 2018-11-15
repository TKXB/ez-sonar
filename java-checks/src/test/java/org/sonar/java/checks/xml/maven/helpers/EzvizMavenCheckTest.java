package org.sonar.java.checks.xml.maven.helpers;

import org.junit.Test;
import org.sonar.java.checks.verifier.PomCheckVerifier;
import org.sonar.java.checks.xml.maven.Ezviz_CVE_2016_1000031Check;

public class EzvizMavenCheckTest {
    @Test
    public void test_check() {
        PomCheckVerifier.verify("src/test/files/checks/xml/maven/EzvizMavenTestXML.xml", new Ezviz_CVE_2016_1000031Check());
    }
}
