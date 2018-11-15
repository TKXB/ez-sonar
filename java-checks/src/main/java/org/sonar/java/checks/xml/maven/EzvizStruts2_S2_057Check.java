package org.sonar.java.checks.xml.maven;

import org.sonar.java.checks.xml.maven.helpers.MavenDependencyCollector;
import org.sonar.java.xml.maven.PomCheck;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.xml.maven.PomCheckContext;
import org.sonar.maven.model.LocatedAttribute;
import org.sonar.maven.model.maven2.Dependency;

import javax.annotation.Nullable;
import java.util.List;

@Rule(key = "EzvizStruts2_S2_057Check")
public class EzvizStruts2_S2_057Check implements PomCheck {

    @Override
    public void scanFile(PomCheckContext context) {
        List<Dependency> dependencies = new MavenDependencyCollector(context.getMavenProject()).allDependencies();
        for (Dependency dependency : dependencies) {
            LocatedAttribute artifactId = dependency.getArtifactId();
            LocatedAttribute version = dependency.getVersion();

            if (version != null && artifactId != null && "struts2-core".equalsIgnoreCase(artifactId.getValue()) && !strutsVerCompare(version.getValue())) {
                String message = "此版本Struts2包含高危漏洞";
                List<PomCheckContext.Location> secondaries = getSecondary(version);
                int line = version.startLocation().line();
                context.reportIssue(this, line, message, secondaries);
            }
        }
    }

    private static List<PomCheckContext.Location> getSecondary(@Nullable LocatedAttribute systemPath) {
        if (systemPath != null && StringUtils.isNotBlank(systemPath.getValue())) {
            return Lists.newArrayList(new PomCheckContext.Location("configure check", systemPath));
        }
        return ImmutableList.of();
    }

    private static boolean strutsVerCompare(String version){
        String StrutsVersion1 = "2.3.35";
        String StrutsVersion2 = "2.5.17";
        String[] versionArray1 = version.split("\\.");
        if(versionArray1[1].equalsIgnoreCase("3")){
            if(compareVersion(StrutsVersion1, version) > 0){
                return false;
            }
        }
        if(versionArray1[1].equalsIgnoreCase("5")){
            if(compareVersion(StrutsVersion2, version) > 0){
                return false;
            }
        }
        return true;
    }

    private static int compareVersion(String version1, String version2){
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

}
