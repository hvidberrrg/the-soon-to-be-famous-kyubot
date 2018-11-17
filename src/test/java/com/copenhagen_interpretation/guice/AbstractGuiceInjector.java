package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.kyubot.ConversationHandler;
import com.copenhagen_interpretation.kyubot.cron.KeepAlive;
import com.copenhagen_interpretation.util.GcsUtil;
import com.copenhagen_interpretation.util.PropertiesUtil;
import com.copenhagen_interpretation.util.TestUtil;
import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.client.ClientConfig;
import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.google.inject.*;
import org.junit.Before;

public abstract class AbstractGuiceInjector {
    // Inject dependencies needed by tests
    @Inject
    public ClientConfig clientConfig;
    @Inject
    public GcsUtil gcsUtil;
    @Inject
    public PropertiesUtil propertiesUtil;
    @Inject
    public SimpleHttpClient simpleHttpClient;
    @Inject
    public WatsonAssistant watsonAssistant;
    @Inject
    public WatsonMapper watsonMapper;
    @Inject
    public TestUtil testUtil;

    // Servlet injection
    @Inject
    public ConversationHandler conversationHandler;
    @Inject
    public KeepAlive keepAlive;

    protected Injector injector = Guice.createInjector(
            new GuiceTestModule(),
            new KyubotModule(),
            new KyubotServletModule()
    );

    @Before
    public void setup() throws Exception {
        injector.injectMembers(this);
    }

    protected void singletonDependencyInjection(Class binding) {
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(binding).in(Scopes.SINGLETON);
            }
        };
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(this);
    }
}
