package rs.manhut.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import rs.manhut.cli.Game;
import rs.manhut.cli.Player;
import rs.manhut.core.GameInstance;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mihailo on 3.10.16..
 */

@Path("/game/")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    private UUID uid;
    private List<GameInstance> gameInstanceList;
    private List<GameInstance> queueingGamesList = new ArrayList<GameInstance>();
    ObjectMapper mapper = new ObjectMapper();

    public GameResource(List<GameInstance> games, UUID uid) {
        this.setGameInstanceList(games);
        this.setUid(uid);
    }

    @GET
    @Path("/createGame")
    public Response randomGame(@QueryParam("gameName") String gameName,
                               @QueryParam("playerName") String playerName,
                               @QueryParam("gamePassword") String gamePassword) {
        try {
            String playerId = uid.randomUUID().toString();

            Player p = new Player(playerId, null, playerName);
            GameInstance gi = new GameInstance(uid.randomUUID().toString(), gameName, gamePassword);

            queueingGamesList.add(gi);

            JsonObject responseJson = Json.createObjectBuilder()
                    .add("playerId", p.getId())
                    .add("gameId", gi.getGameId()).build();
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.ok("Failed to create game!").build();
        }
    }

    @GET
    @Path("/listGames")
    public Response listGames(@QueryParam("q") String keyword) {
        try {
            List<String> responseList = new ArrayList<String>();

            if(keyword != null) {
                for(Integer i = 0; i < 20 && i < this.getQueueingGamesList().size(); i++) {
                    GameInstance gi = this.getQueueingGamesList().get(i);
                    if(gi.getGameName().contains(keyword)) {
                        Boolean pp = false;

                        if(gi.getGamePassword() != null) {
                            pp = true;
                        }

                        Game g = new Game();
                        g.setGameId(gi.getGameId());
                        g.setGameName(gi.getGameName());
                        g.setPlayerNumber(gi.getPlayerList().size());
                        g.setPasswordProtected(pp);
                        responseList.add(mapper.writeValueAsString(g));
                    }
                }

                GenericEntity<List<String>> entity =
                        new GenericEntity<List<String>>(responseList) {};
                return Response.ok(entity).build();
            } else {
                for(Integer i = 0; i < 20 && i < this.getQueueingGamesList().size(); i++) {
                    GameInstance gi = this.getQueueingGamesList().get(i);
                    Boolean pp = false;

                    if(gi.getGamePassword() != null) {
                        pp = true;
                    }

                    Game g = new Game();
                    g.setGameId(gi.getGameId());
                    g.setGameName(gi.getGameName());
                    g.setPlayerNumber(gi.getPlayerList().size());
                    g.setPasswordProtected(pp);
                    responseList.add(mapper.writeValueAsString(g));
                }

                GenericEntity<List<String>> entity =
                        new GenericEntity<List<String>>(responseList) {};
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            return Response.ok("[]").build();
        }
    }

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }

    public void setGameInstanceList(List<GameInstance> gameInstanceList) {
        this.gameInstanceList = gameInstanceList;
    }

    public List<GameInstance> getQueueingGamesList() {
        return queueingGamesList;
    }

    public void setQueueingGamesList(List<GameInstance> queueingGamesList) {
        this.queueingGamesList = queueingGamesList;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
