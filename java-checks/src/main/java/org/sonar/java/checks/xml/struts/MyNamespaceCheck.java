package org.sonar.java.checks.xml.struts;

import com.google.common.collect.Iterables;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.java.xml.XPathXmlCheck;
import org.sonar.java.xml.XmlCheckContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpression;
import java.util.LinkedList;
import java.util.List;

@Rule(key = "MyNamespaceCheck",
        name = "MyNamespaceCheck",
        description = "damn bitch",
        priority = Priority.CRITICAL,
        tags = {"bug"})
public class MyNamespaceCheck extends XPathXmlCheck {
    private static final int DEFAULT_MAXIMUM_NUMBER_FORWARDS = 0;

    @RuleProperty(
            key = "threshold",
            description = "Maximum allowed number of ``<forward/>`` mappings in an ``<action>``",
            defaultValue = "" + DEFAULT_MAXIMUM_NUMBER_FORWARDS)
    public int maximumForwards = DEFAULT_MAXIMUM_NUMBER_FORWARDS;

    private XPathExpression actionsExpression;
    @Override
    public void precompileXPathExpressions(XmlCheckContext context) {
        actionsExpression = context.compile("struts/package");
    }

    @Override
    public void scanFileWithXPathExpressions(XmlCheckContext context) {
        List<XmlCheckContext.XmlDocumentLocation> secondaries = new LinkedList<>();
        for (Node pack : context.evaluateOnDocument(actionsExpression)) {
                NamedNodeMap ss = pack.getAttributes();
                Node ss2 = ss.getNamedItem("namespace");
                String namespaceURI = ss2.getNodeValue();
                if (namespaceURI.equals("") || namespaceURI.equals("/*") || namespaceURI.equals("*")){
                    secondaries.add(new XmlCheckContext.XmlDocumentLocation("BE CAREFUL!.", pack));
                    int cost = secondaries.size();
                    String message = "Be careful about this in the struts2 framework.";
                    context.reportIssue(this, pack, message, secondaries, cost);
                }
            }
        }

}
