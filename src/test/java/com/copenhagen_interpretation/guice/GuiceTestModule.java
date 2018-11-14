package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.util.TestUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class GuiceTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestUtil.class).in(Scopes.SINGLETON);
    }
}
