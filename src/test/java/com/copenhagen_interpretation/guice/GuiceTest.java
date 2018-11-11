package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.watson.WatsonMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;

public class GuiceTest { //NOSONAR
    protected Injector injector = Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
            bind(WatsonMapper.class);
        }
    });

    @Before
    public void setup() {
        injector.injectMembers(this);
    }
}
