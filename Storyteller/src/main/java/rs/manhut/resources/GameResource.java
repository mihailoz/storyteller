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
                               @QueryParam("gamePassword") String gamePassword) {
        try {
            String playerId = uid.randomUUID().toString();

            Player p = new Player(playerId, null, "anonymous");
            GameInstance gi = new GameInstance(uid.randomUUID().toString(), gameName, gamePassword);

            gi.setGameOwner(p.getId());
            gi.addPlayer(p);

            queueingGamesList.add(gi);

            JsonObject responseJson = Json.createObjectBuilder()
                    .add("playerId", p.getId())
                    .add("gameId", gi.getGameId()).build();
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.ok("Failed to create game!", MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/status/{playerId}")
    public Response getStatus(@PathParam("playerId") String playerId) {
        try {
            GameInstance gi = null;
            for(GameInstance g: this.getGameInstanceList()) {
                for(Player p: g.getPlayerList()) {
                    if(p.getId().equals(playerId)) {
                        gi = g;
                    }
                }
            }
            if(gi != null) {
                return Response.ok("game-started", MediaType.TEXT_PLAIN).build();
            } else {
                for(GameInstance g: this.getQueueingGamesList()) {
                    for(Player p: g.getPlayerList()) {
                        if(p.getId().equals(playerId)) {
                            gi = g;
                        }
                    }
                }
                if(gi != null) {
                    Boolean pp = false;
                    if(gi.getGamePassword() != null) {
                        pp = true;
                    }

                    Game g = new Game();
                    g.setGameId(gi.getGameId());
                    g.setGameName(gi.getGameName());
                    g.setPlayerNumber(gi.getPlayerList().size());
                    g.setPasswordProtected(pp);
                    if(gi.getGameOwner().equals(playerId)) {
                        g.setOwner(true);
                    } else {
                        g.setOwner(false);
                    }

                    return Response.ok(mapper.writeValueAsString(g)).build();
                } else {
                    throw new Exception("Game not found");
                }
            }
        } catch (Exception e) {
            return Response.ok("Can't retrieve game data", MediaType.TEXT_PLAIN).build();
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
                    if(gi.getGameName().toLowerCase().contains(keyword.toLowerCase())) {
                        Boolean pp = false;

                        if(gi.getGamePassword() != null && !gi.getGamePassword().equals("")) {
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

                    if(gi.getGamePassword() != null && !gi.getGamePassword().equals("")) {
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

    @POST
    @Path("/startGame/{playerId}")
    public Response startGame(@PathParam("playerId") String playerId) {
        try {
            GameInstance gi = null;
            for(GameInstance g: this.getQueueingGamesList()) {
                if (g.getGameOwner().equals(playerId)) {
                    gi = g;
                }
            }
            if(gi != null) {
                this.getGameInstanceList().add(gi);
                this.getQueueingGamesList().remove(gi);

                final GameInstance runGi = gi;

                Runnable startGame = new Runnable() {
                    public void run() {
                        runGi.run();
                    }
                };

                new Thread(startGame).start();

                return Response.ok("Game started", MediaType.TEXT_PLAIN).build();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return Response.ok("Game cannot be started", MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/joinGame")
    public Response joinGame(@QueryParam("gameId") String gameId,
                               @QueryParam("gamePassword") String gamePassword) {
        try {
            GameInstance gi = null;

            for(GameInstance g: this.getQueueingGamesList()) {
                if(gameId.equals(g.getGameId())) {
                    if(g.getGamePassword() == null || g.getGamePassword().equals(gamePassword)) {
                        gi = g;
                    } else {
                        throw new Exception("Password incorrect");
                    }
                }
            }
            if(gi != null) {
                Player p = new Player(uid.randomUUID().toString(), gi, "anonymous");
                gi.addPlayer(p);
                return Response.ok(p.getId()).build();
            } else {
                throw new Exception("Password incorrect");
            }
        } catch (Exception e) {
            return Response.ok("Password incorrect", MediaType.TEXT_PLAIN).build();
        }
    }

    @POST
    @Path("/leave/{playerId}")
    public Response leaveGame(@PathParam("playerId") String playerId) {
        GameInstance gi = null;
        Player pl = null;

        for(GameInstance g: this.getQueueingGamesList()) {
            for(Player p: g.getPlayerList()) {
                if(p.getId().equals(playerId)) {
                    gi = g;
                    pl = p;
                }
            }
        }

        if(gi != null) {
            gi.getPlayerList().remove(pl);
            if (gi.getGameOwner().equals(playerId)) {
                if (gi.getPlayerList().size() > 0) {
                    gi.setGameOwner(gi.getPlayerList().get(0).getId());
                } else {
                    this.getQueueingGamesList().remove(gi);
                }
            }
        }

        return Response.ok("Left queue", MediaType.TEXT_PLAIN).build();
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

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }
}
