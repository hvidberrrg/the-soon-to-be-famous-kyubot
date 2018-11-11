package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.watson.WatsonAssistant;
import com.copenhagen_interpretation.watson.WatsonMapper;
import com.copenhagen_interpretation.watson.client.SimpleHttpClient;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class KyubotModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SimpleHttpClient.class).in(Scopes.SINGLETON);
        bind(WatsonAssistant.class).in(Scopes.SINGLETON);
        bind(WatsonMapper.class).in(Scopes.SINGLETON);
    }
}

