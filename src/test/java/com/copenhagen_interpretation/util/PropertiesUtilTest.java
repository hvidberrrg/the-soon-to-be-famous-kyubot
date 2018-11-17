package com.copenhagen_interpretation.util;

import com.copenhagen_interpretation.KyubotTestProps;
import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(BlockJUnit4ClassRunner.class)
public class PropertiesUtilTest extends AbstractGuiceInjector {

    @Test
    public void testGetProperties() {
        Properties assistantProps = propertiesUtil.getProperties("assistant.properties");
        if (assistantProps == null) {
            fail("Couldn't load assistant.properties");
        } else {
            assertEquals(KyubotTestProps.USERNAME, assistantProps.getProperty("username"));
            assertEquals(KyubotTestProps.PASSWORD, assistantProps.getProperty("password"));
            assertEquals(KyubotTestProps.WORKSPACEID, assistantProps.getProperty("workspaceId"));
            assertEquals(KyubotTestProps.APIURL, assistantProps.getProperty("apiUrl"));
            assertEquals(KyubotTestProps.ENDPOINT, assistantProps.getProperty("endpoint"));
            assertEquals(KyubotTestProps.VERSIONDATE, assistantProps.getProperty("versionDate"));
            assertEquals(KyubotTestProps.TERMINATEONACTION, assistantProps.getProperty("terminateOnAction"));
        }
    }
}