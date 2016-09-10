package rs.manhut.resources;

import com.codahale.metrics.annotation.Timed;
import rs.manhut.cli.Player;
import rs.manhut.core.GameInstance;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by mihailo on 2.9.16..
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    private UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private List<GameInstance> gameInstanceList;
    private List<Player> playersQueueing = new ArrayList<Player>();

    public GameResource(List<GameInstance> games) {
        this.setGameInstanceList(games);
    }

    @GET
    @Timed
    public Response startGame(@QueryParam("name") Optional<String> name) {
        String defaultName = "Anonymous";
        String playerId = uid.randomUUID().toString();

        if(name.isPresent()) {
            defaultName = name.get();
        }

        Player p = new Player(playerId, null, defaultName);
        playersQueueing.add(p);

        if(playersQueueing.size() > 4) {
            GameInstance gi = new GameInstance(uid.randomUUID().toString());

            for(Player pl: this.getPlayersQueueing()) {
                gi.addPlayer(pl);
                pl.setGame(gi);
            }

            for(Player pl: gi.getPlayerList()) {
                this.getPlayersQueueing().remove(pl);
            }

            gameInstanceList.add(gi);
            gi.start();
        }

        return Response.ok(playerId, MediaType.APPLICATION_JSON).build();
    }

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }

    public void setGameInstanceList(List<GameInstance> gameInstanceList) {
        this.gameInstanceList = gameInstanceList;
    }

    public List<Player> getPlayersQueueing() { return playersQueueing; }
}
