package org.sonar.java.checks.xml.struts;

import org.junit.Before;
import org.junit.Test;
import org.sonar.java.checks.verifier.XmlCheckVerifier;

import static org.junit.Assert.*;

public class MyNamespaceCheckTest {
    private MyNamespaceCheck check;

    @Before
    public void setup() {
        check = new MyNamespaceCheck();
    }

    @Test
    public void struts_config_with_too_many_forwards() {
        XmlCheckVerifier.verify("src/test/files/checks/xml/struts/namespacecheck.xml", check);
    }

}