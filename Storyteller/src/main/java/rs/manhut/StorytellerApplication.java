package rs.manhut;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import rs.manhut.core.GameInstance;
import rs.manhut.resources.GameResource;
import rs.manhut.resources.HistoryResource;

import java.util.ArrayList;
import java.util.List;

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
        final GameResource gameResource = new GameResource(this.getGameInstanceList());
        final HistoryResource historyResource = new HistoryResource();

        environment.jersey().register(gameResource);
        environment.jersey().register(historyResource);

        environment.jersey().setUrlPattern("/play/*");
    }

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }

    public void setGameInstanceList(List<GameInstance> gameInstanceList) {
        this.gameInstanceList = gameInstanceList;
    }
}
