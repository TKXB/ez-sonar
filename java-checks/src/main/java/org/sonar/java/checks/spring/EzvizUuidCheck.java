package org.sonar.java.checks.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "EzvizUuidCheck")

public class EzvizUuidCheck extends IssuableSubscriptionVisitor {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.METHOD);
    }

    private static final List<String> CONTROLLER_ANNOTATIONS = Arrays.asList(
            "org.springframework.stereotype.Controller",
            "org.springframework.web.bind.annotation.RestController"
    );

    private static final List<String> REQUEST_ANNOTATIONS = Arrays.asList(
            "org.springframework.web.bind.annotation.RequestMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            "org.springframework.web.bind.annotation.PatchMapping"
    );

    private static final List<String> USERID = Arrays.asList(
            "uuid"
            );

    @Override
    public void visitNode(Tree tree) {
        if (!hasSemantic()) {
            return;
        }

        MethodTree methodTree = (MethodTree) tree;
        List<VariableTree> parameters = methodTree.parameters();
        Symbol.MethodSymbol methodSymbol = methodTree.symbol();

        for (VariableTree parameter:parameters) {
            if (isClassController(methodSymbol)
                    && isRequestMappingAnnotated(methodSymbol)
                    && isUserId(parameter.simpleName().name())){
                reportIssue(methodTree.simpleName(), "直接使用uuid时需小心越权问题");
            }
        }
    }

    private static boolean isClassController(Symbol.MethodSymbol methodSymbol) {
        return CONTROLLER_ANNOTATIONS.stream().anyMatch(methodSymbol.owner().metadata()::isAnnotatedWith);
    }

    private static boolean isRequestMappingAnnotated(Symbol.MethodSymbol methodSymbol) {
        return REQUEST_ANNOTATIONS.stream().anyMatch(methodSymbol.metadata()::isAnnotatedWith);
    }

    private static boolean isUserId(String userid){
        return USERID.stream().anyMatch(userid::equalsIgnoreCase);
    }

}
