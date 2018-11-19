package com.copenhagen_interpretation;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class jDependTest {
    private static final String PACKAGE_PREFIX = "com.copenhagen_interpretation";
    private JDepend jDepend;

    @Before
    public void setUp() throws Exception {
        jDepend = new JDepend();
        String path = System.getProperty("user.dir") + "/target/the-soon-to-be-famous-kyubot/WEB-INF/classes";
        jDepend.addDirectory(path);
    }

    @Test
    public void containsCyclesTest() {
        int packagesChecked = 0;
        int expectedPackages = 7;
        Collection<JavaPackage> packages = jDepend.analyze();
        for (JavaPackage javaPackage : packages) {
            if (javaPackage.getName().startsWith(PACKAGE_PREFIX)) {
                packagesChecked++;
                assertFalse(javaPackage.getName() + " contains cycles ", javaPackage.containsCycle());
            }
        }
        assertEquals("Expected " + expectedPackages + " packages but actually checked " + packagesChecked, expectedPackages, packagesChecked);
        assertFalse(PACKAGE_PREFIX + " contains cycles.", jDepend.containsCycles());
    }
}