package com.copenhagen_interpretation.guice;

import com.google.inject.*;
import org.junit.Before;

public abstract class AbstractGuiceInjector {

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
