package rs.manhut;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import rs.manhut.health.TemplateHealthCheck;
import rs.manhut.resources.HelloWorldResource;

public class WordByWordApplication extends Application<WordByWordConfiguration> {

    public static void main(final String[] args) throws Exception {
        new WordByWordApplication().run(args);
    }

    @Override
    public String getName() {
        return "WordByWord";
    }

    @Override
    public void initialize(final Bootstrap<WordByWordConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(final WordByWordConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().setUrlPattern("/api/*");
    }
}
