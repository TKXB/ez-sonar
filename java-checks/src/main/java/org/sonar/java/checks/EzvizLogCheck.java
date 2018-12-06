/*
 * SonarQube Java
 * Copyright (C) 2012-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
//based on PredictableSeedCheck
package org.sonar.java.checks;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.sonar.check.Rule;
import org.sonar.java.checks.methods.AbstractMethodDetection;
import org.sonar.java.matcher.MethodMatcher;
import org.sonar.plugins.java.api.tree.*;

@Rule(key = "EzvizLogCheck")
public class EzvizLogCheck extends AbstractMethodDetection {
    private static final Set<String> SUSPICIOUS_TOKEN_VALUES = ImmutableSet.of(
            "pwd",
            "secretkey",
            "password",
            "passwd",
            "密码",
            "身份证");
    private static final String JAVA_SLF4J_CLASS_NAME = "org.slf4j.Logger";
    private static final String JAVA_APACHE_LOG4J_CLASS_NAME = "org.apache.log4j.Logger";
    private static final String JAVA_APACHE_LOGGING_LOG4J_CLASS_NAME = "org.apache.logging.log4j.Logger";
    private static final String JAVA_HIK_LOG_CLASS_NAME = "com.hikvision.core.logging.Log";

    @Override
    protected List<MethodMatcher> getMethodInvocationMatchers() {
        return Arrays.asList(
//                MethodMatcher.create().typeDefinition(JAVA_SLF4J_CLASS_NAME).name("debug").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_SLF4J_CLASS_NAME).name("info").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_SLF4J_CLASS_NAME).name("warn").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_SLF4J_CLASS_NAME).name("error").withAnyParameters(),
//                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOG4J_CLASS_NAME).name("debug").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOG4J_CLASS_NAME).name("info").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOG4J_CLASS_NAME).name("warn").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOG4J_CLASS_NAME).name("error").withAnyParameters(),
//                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOGGING_LOG4J_CLASS_NAME).name("debug").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOGGING_LOG4J_CLASS_NAME).name("info").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOGGING_LOG4J_CLASS_NAME).name("warn").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_APACHE_LOGGING_LOG4J_CLASS_NAME).name("error").withAnyParameters(),
//                MethodMatcher.create().typeDefinition(JAVA_HIK_LOG_CLASS_NAME).name("debug").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_HIK_LOG_CLASS_NAME).name("info").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_HIK_LOG_CLASS_NAME).name("warn").withAnyParameters(),
                MethodMatcher.create().typeDefinition(JAVA_HIK_LOG_CLASS_NAME).name("error").withAnyParameters()
        );
    }

    @Override
    protected void onMethodInvocationFound(MethodInvocationTree mit) {
        checkLog(mit.arguments().get(0));
    }

    @Override
    protected void onConstructorFound(NewClassTree newClassTree) {
        checkLog(newClassTree.arguments().get(0));
    }

    private void checkLog(ExpressionTree logExpression) {
        SyntaxToken logToken = logExpression.firstToken();
        if (isSuspiciousToken(logToken)){
            reportIssue(logExpression, "请勿将敏感信息写入到日志中");
        }
    }

    private static boolean isSuspiciousToken(SyntaxToken firstToken) {
        for(String key: SUSPICIOUS_TOKEN_VALUES){
            if (firstToken.text().toLowerCase().contains(key)){return true;};
        }
        return false;
    }
}
