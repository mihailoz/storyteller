package rs.manhut.websockets;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import rs.manhut.StorytellerConfiguration;

/**
 * Created by mihailozdravkovic on 11/8/16.
 */
public class WebsocketBundle implements ConfiguredBundle<StorytellerConfiguration> {

    @Override
    public void run(StorytellerConfiguration configuration, Environment environment) {
        environment.servlets().addServlet("PlayWebSocketServlet", PlayWebSocketServlet.class).addMapping("/websocket/play");
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }
}
