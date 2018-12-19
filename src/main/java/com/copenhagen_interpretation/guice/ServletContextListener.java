package com.copenhagen_interpretation.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

import java.util.ArrayList;
import java.util.Collection;

public class ServletContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(getModules());
    }

    protected Collection<Module> getModules() {
        Collection<Module> modules = new ArrayList<>();
        modules.add(new KyubotModule());
        modules.add(new KyubotServletModule());

        return  modules;
    }
}

