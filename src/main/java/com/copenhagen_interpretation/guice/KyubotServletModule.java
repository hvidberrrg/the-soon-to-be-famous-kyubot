package com.copenhagen_interpretation.guice;

import com.copenhagen_interpretation.kyubot.ContentHandler;
import com.copenhagen_interpretation.kyubot.ConversationHandler;
import com.copenhagen_interpretation.kyubot.cron.KeepAlive;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

public class KyubotServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(ConversationHandler.class).in(Scopes.SINGLETON);
        requestStaticInjection(ConversationHandler.class);
        serve("/conversation").with(ConversationHandler.class);

        bind(ContentHandler.class).in(Scopes.SINGLETON);
        requestStaticInjection(ContentHandler.class);
        serve("/kyudo*").with(ContentHandler.class);

        bind(KeepAlive.class).in(Scopes.SINGLETON);
        requestStaticInjection(KeepAlive.class);
        serve("/cron/keepAlive").with(KeepAlive.class);
    }
}

