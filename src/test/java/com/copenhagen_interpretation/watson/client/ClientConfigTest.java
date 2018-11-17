package com.copenhagen_interpretation.watson.client;

import com.copenhagen_interpretation.KyubotTestProps;
import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class ClientConfigTest extends AbstractGuiceInjector {

    @Test
    public void testClientConfig() {
        String credentials = KyubotTestProps.USERNAME + ":" + KyubotTestProps.PASSWORD;
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String url = KyubotTestProps.APIURL + "/" + KyubotTestProps.WORKSPACEID + "/" + KyubotTestProps.ENDPOINT + "?version=" + KyubotTestProps.VERSIONDATE;

        assertEquals(credentials, clientConfig.getCredentials()); //NOSONAR
        assertEquals(url, clientConfig.getUrl() ); //NOSONAR
        assertEquals(KyubotTestProps.TERMINATEONACTION, clientConfig.getTerminateOnAction()); //NOSONAR
    }

}