package com.copenhagen_interpretation;

import jdepend.framework.DependencyConstraint;
import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;


public class jDependTest {
    private static final String PACKAGE_PREFIX = "com.copenhagen_interpretation";
    private static final int EXPECTED_NO_OF_PACKAGES = 8;
    private JDepend jDepend;

    @Before
    public void setUp() throws Exception {
        PackageFilter filter = PackageFilter.all().including(PACKAGE_PREFIX).excludingRest();
        jDepend = new JDepend(filter);
        String path = System.getProperty("user.dir") + "/target/the-soon-to-be-famous-kyubot/WEB-INF/classes";
        jDepend.addDirectory(path);
    }

    @Test
    public void testPackageDependencyConstraints() {
        DependencyConstraint constraint = new DependencyConstraint();
        JavaPackage content = constraint.addPackage(PACKAGE_PREFIX + ".content");
        JavaPackage guice = constraint.addPackage(PACKAGE_PREFIX + ".guice");
        JavaPackage kyubot = constraint.addPackage(PACKAGE_PREFIX + ".kyubot");
        JavaPackage kyubot_cron = constraint.addPackage(PACKAGE_PREFIX + ".kyubot.cron");
        JavaPackage util = constraint.addPackage(PACKAGE_PREFIX + ".util");
        JavaPackage watson = constraint.addPackage(PACKAGE_PREFIX + ".watson");
        JavaPackage watson_client = constraint.addPackage(PACKAGE_PREFIX + ".watson.client");
        JavaPackage watson_model = constraint.addPackage(PACKAGE_PREFIX + ".watson.model");

        guice.dependsUpon(content, kyubot, kyubot_cron, util, watson, watson_client);
        kyubot.dependsUpon(content, watson, watson_model);
        kyubot_cron.dependsUpon(util, watson, watson_model);
        watson_client.dependsUpon(util);
        watson.dependsUpon(watson_client, watson_model);

        jDepend.analyze();
        assertTrue("Package dependency mismatch!", jDepend.dependencyMatch(constraint));
    }

    @Test
    public void testPackageDependencyCycles() {
        int packagesChecked = 0;
        Collection<JavaPackage> packages = jDepend.analyze();
        for (JavaPackage javaPackage : packages) {
            packagesChecked++;
            assertFalse(javaPackage.getName() + " contains cycles ", javaPackage.containsCycle());
        }
        assertEquals("Expected " + EXPECTED_NO_OF_PACKAGES + " packages but actually checked " + packagesChecked, EXPECTED_NO_OF_PACKAGES, packagesChecked);
        assertFalse(PACKAGE_PREFIX + " contains cycles.", jDepend.containsCycles());
    }
}