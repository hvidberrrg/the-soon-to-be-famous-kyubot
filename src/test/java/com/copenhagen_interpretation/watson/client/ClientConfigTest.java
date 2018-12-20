package com.copenhagen_interpretation.watson.client;

import com.copenhagen_interpretation.KyubotTestProps;
import com.copenhagen_interpretation.guice.AbstractGuiceInjector;
import com.copenhagen_interpretation.util.PropertiesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
public class ClientConfigTest extends AbstractGuiceInjector {
    @Mock
    private Logger mockLogger;
    @Mock
    private PropertiesUtil mockPropertiesUtil;
    @InjectMocks
    private ClientConfig clientConfigWithMocks;

    @Test
    public void testClientConfig() {
        String credentials = KyubotTestProps.USERNAME + ":" + KyubotTestProps.PASSWORD;
        credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        String url = KyubotTestProps.APIURL + "/" + KyubotTestProps.WORKSPACEID + "/" + KyubotTestProps.ENDPOINT + "?version=" + KyubotTestProps.VERSIONDATE;

        assertEquals(credentials, clientConfig.getCredentials()); //NOSONAR
        assertEquals(url, clientConfig.getUrl() ); //NOSONAR
        assertEquals(KyubotTestProps.TERMINATEONACTION, clientConfig.getTerminateOnAction()); //NOSONAR
    }

    @Test
    public void testClientConfig_NullProps() {
        MockitoAnnotations.initMocks(this);
        when(mockPropertiesUtil.getProperties(any(String.class))).thenReturn(null);

        assertNull(clientConfigWithMocks.getCredentials());
        assertNull(clientConfigWithMocks.getUrl() );
        assertNull(clientConfigWithMocks.getTerminateOnAction());

    }

}