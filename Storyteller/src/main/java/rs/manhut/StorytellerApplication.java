package rs.manhut;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import rs.manhut.core.GameInstance;
import rs.manhut.resources.GameResource;
import rs.manhut.resources.PlayResource;
import rs.manhut.resources.HistoryResource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorytellerApplication extends Application<StorytellerConfiguration> {

    private List<GameInstance> gameInstanceList = new ArrayList<GameInstance>();

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
    }

    @Override
    public void run(final StorytellerConfiguration configuration,
                    final Environment environment) {

        UUID storytellerUid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        final PlayResource playResource = new PlayResource(this.getGameInstanceList(), storytellerUid);
        final GameResource gameResource = new GameResource(this.getGameInstanceList(), storytellerUid);
        final HistoryResource historyResource = new HistoryResource();

        environment.jersey().register(playResource);
        environment.jersey().register(historyResource);
        environment.jersey().register(gameResource);

        environment.jersey().setUrlPattern("/api/*");
    }

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }

    public void setGameInstanceList(List<GameInstance> gameInstanceList) {
        this.gameInstanceList = gameInstanceList;
    }
}
