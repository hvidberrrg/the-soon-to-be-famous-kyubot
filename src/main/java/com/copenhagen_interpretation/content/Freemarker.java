package com.copenhagen_interpretation.content;

import com.google.inject.Inject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Freemarker {
    @Inject
    private Logger logger;

    private Configuration configuration;
    private String templatePath = "WEB-INF/templates";

    private void initializeConfiguration() throws IOException {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.28) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        configuration = new Configuration(Configuration.VERSION_2_3_28);

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        configuration.setDirectoryForTemplateLoading(new File(templatePath));

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        configuration.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        configuration.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s.
        configuration.setWrapUncheckedExceptions(true);

        logger.info("Freemarker configuration initialized.");
    }

    public Template getTemplate(String templateName) {
        try {
            // Lazy initialization allows us to override the template path before testing
            if (configuration == null) {
                initializeConfiguration();
            }
            return configuration.getTemplate(templateName);
        } catch (IOException e) {
            logger.severe("Failed to get template '" + templateName + "' - " + e);
            return null;
        }
    }

    public void setTemplatePath(String templatePath) {
        // Needed for testing
        this.templatePath = templatePath;
    }
}
