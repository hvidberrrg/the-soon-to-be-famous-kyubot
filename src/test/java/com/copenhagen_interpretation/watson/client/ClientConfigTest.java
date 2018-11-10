package com.copenhagen_interpretation.watson.client;

import com.copenhagen_interpretation.KyubotTestProps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class ClientConfigTest {

    @Test
    public void testClientConfig() {
        String credentials = KyubotTestProps.USERNAME + ":" + KyubotTestProps.PASSWORD;
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String url = KyubotTestProps.APIURL + "/" + KyubotTestProps.WORKSPACEID + "/" + KyubotTestProps.ENDPOINT + "?version=" + KyubotTestProps.VERSIONDATE;

        assertEquals(credentials, ClientConfig.CREDENTIALS); //NOSONAR
        assertEquals(url, ClientConfig.URL ); //NOSONAR
        assertEquals(KyubotTestProps.TERMINATEONACTION, ClientConfig.TERMINATE_ON_ACTION); //NOSONAR
    }

}