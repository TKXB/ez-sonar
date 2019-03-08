package org.sonar.java.checks.security;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;

import java.util.regex.*;


import java.util.List;

@Rule(key = "EzvizHTTPUrlCheck")
public class EzvizHTTPUrlCheck extends IssuableSubscriptionVisitor {
    private static String pattern = "^(\"|)http://.*";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(
                Tree.Kind.STRING_LITERAL);
    }

    @Override
    public void visitNode(Tree tree) {
        if (hasSemantic()) {
            if (tree.is(Tree.Kind.STRING_LITERAL)) {
                LiteralTree literalTree = (LiteralTree) tree;
                String content = literalTree.value();
                boolean isMatch = Pattern.matches(pattern, content);
                if (isMatch) {
                    reportIssue(literalTree, "该URL未使用HTTPS");
                }
            }
        }
    }

}
