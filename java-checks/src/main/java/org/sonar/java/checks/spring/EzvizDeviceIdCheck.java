package org.sonar.java.checks.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.*;

@Rule(key = "EzvizDeviceIdCheck")

public class EzvizDeviceIdCheck extends IssuableSubscriptionVisitor {

    private static List<String> lines;

    @Override
    public List<Tree.Kind> nodesToVisit() { return Collections.singletonList(Tree.Kind.METHOD); }

    @Override
    public void scanFile(JavaFileScannerContext context){
        lines = context.getFileLines();
        super.scanFile(context);

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

    //设备序列号别名
    private static final List<String> DEVICE_IDS = Arrays.asList(
            "deviceid",
            "subserial",
            "deviceserial"
    );

    private static final List<String> AUTH_KEYS = Arrays.asList(
            "userid",
            "session",
            "userinfo",
            "accesstoken"
    );

    private static final List<String> AUTH_ANNOTATIONS = Arrays.asList(
            "com.ys.product.common.annotation.UserDeviceAccessControl"
    );

    @Override
    public void visitNode(Tree tree) {
        if (!hasSemantic()) {
            return;
        }

        MethodTree methodTree = (MethodTree) tree;
        BlockTree block = methodTree.block();
        if (block != null) {
            int beginline = block.openBraceToken().line();
            int endline = block.closeBraceToken().line();

            List<VariableTree> parameters = methodTree.parameters();
            Symbol.MethodSymbol methodSymbol = methodTree.symbol();

            for (VariableTree parameter : parameters) {
                if (isClassController(methodSymbol)
                        && isRequestMappingAnnotated(methodSymbol)
                        && isDeviceid(parameter.simpleName().name())
                        && !isAuthorization(lines, beginline, endline)
                        && !isAuthAnnotated(methodSymbol)) {
                    reportIssue(methodTree.simpleName(), "请检查是否有权限校验");
                }
            }
        }
    }

    private static boolean isClassController(Symbol.MethodSymbol methodSymbol) {
        return CONTROLLER_ANNOTATIONS.stream().anyMatch(methodSymbol.owner().metadata()::isAnnotatedWith);
    }

    private static boolean isRequestMappingAnnotated(Symbol.MethodSymbol methodSymbol) {
        return REQUEST_ANNOTATIONS.stream().anyMatch(methodSymbol.metadata()::isAnnotatedWith);
    }

    private static boolean isAuthAnnotated(Symbol.MethodSymbol methodSymbol) {
        return AUTH_ANNOTATIONS.stream().anyMatch(methodSymbol.metadata()::isAnnotatedWith);
    }

    private static boolean isDeviceid(String name) {
        return DEVICE_IDS.stream().anyMatch(name::equalsIgnoreCase);
    }

    private static boolean isAuthorization(List<String> lines, int beginline, int endline){
        for (int lineindex = beginline; lineindex <= endline; lineindex = lineindex + 1){
            String line = lines.get(lineindex);
            if (AUTH_KEYS.stream().anyMatch(line.toLowerCase()::contains)){
                return true;

            }
        }
        return false;
    }
}
