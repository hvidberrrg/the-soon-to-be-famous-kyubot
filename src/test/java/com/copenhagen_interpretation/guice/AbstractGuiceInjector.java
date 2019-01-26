package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.content.ContentHandlerUtil;
import com.copenhagen_interpretation.content.ContentMapper;
import com.copenhagen_interpretation.kyubot.ContentHandler;
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

import java.util.Collection;

public abstract class AbstractGuiceInjector extends ServletContextListener {
    // Inject dependencies needed by tests
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected ContentHandlerUtil contentHandlerUtil;
    @Inject
    protected ContentMapper contentMapper;
    @Inject
    protected GcsUtil gcsUtil;
    @Inject
    protected PropertiesUtil propertiesUtil;
    @Inject
    protected SimpleHttpClient simpleHttpClient;
    @Inject
    protected WatsonAssistant watsonAssistant;
    @Inject
    protected WatsonMapper watsonMapper;
    @Inject
    protected TestUtil testUtil;

    // Servlet injection
    @Inject
    protected ContentHandler contentHandler;
    @Inject
    protected ConversationHandler conversationHandler;
    @Inject
    protected KeepAlive keepAlive;

    @Override
    protected Collection<Module> getModules() {
        Collection<Module> modules = super.getModules();
        modules.add(new GuiceTestModule());

        return  modules;
    }

    private Injector injector = getInjector();

    @Before
    public void setUp() throws Exception {
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
