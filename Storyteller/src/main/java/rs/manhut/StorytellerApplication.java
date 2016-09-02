package rs.manhut;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import rs.manhut.health.TemplateHealthCheck;
import rs.manhut.resources.HelloWorldResource;

import javax.servlet.ServletRegistration;

public class StorytellerApplication extends Application<StorytellerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new StorytellerApplication().run(args);
    }

    @Override
    public String getName() {
        return "WordByWord";
    }

    @Override
    public void initialize(final Bootstrap<StorytellerConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(final StorytellerConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().setUrlPattern("/api/*");

        AtmosphereServlet servlet = new AtmosphereServlet();
        servlet.framework().addInitParameter("com.sun.jersey.config.property.packages", "rs.manhut.resources.WebSocketResource");
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json");
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");

        ServletRegistration.Dynamic servletHolder = environment.servlets().addServlet("Play", servlet);
        servletHolder.addMapping("/play/*");
    }
}
