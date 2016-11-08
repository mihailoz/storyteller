package rs.manhut;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import rs.manhut.core.GameInstance;
import rs.manhut.resources.GameResource;
import rs.manhut.resources.PlayResource;
import rs.manhut.resources.HistoryResource;
import rs.manhut.websockets.WebsocketBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorytellerApplication extends Application<StorytellerConfiguration> {

    public static List<GameInstance> gameInstanceList = new ArrayList<GameInstance>();

    public static void main(final String[] args) throws Exception {
        new StorytellerApplication().run(args);
    }

    @Override
    public String getName() {
        return "StorywriteRS";
    }

    @Override
    public void initialize(final Bootstrap<StorytellerConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
        bootstrap.addBundle(new WebsocketBundle());
    }

    @Override
    public void run(final StorytellerConfiguration configuration,
                    final Environment environment) {

        UUID storytellerUid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        final PlayResource playResource = new PlayResource(StorytellerApplication.gameInstanceList, storytellerUid);
        final GameResource gameResource = new GameResource(StorytellerApplication.gameInstanceList, storytellerUid);
        final HistoryResource historyResource = new HistoryResource();

        environment.jersey().register(playResource);
        environment.jersey().register(historyResource);
        environment.jersey().register(gameResource);

        environment.jersey().setUrlPattern("/api/*");
    }

}
