package rs.manhut.resources;

import com.codahale.metrics.annotation.Timed;
import rs.manhut.cli.Player;
import rs.manhut.core.GameHistoryController;
import rs.manhut.core.GameInstance;
import rs.manhut.core.TRIE;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mihailo on 2.9.16..
 */

@Path("/play/")
@Produces(MediaType.APPLICATION_JSON)
public class PlayResource {

    private UUID uid;
    private List<GameInstance> gameInstanceList;
    private List<Player> playersQueueing = new ArrayList<Player>();
    private TRIE wordChecker = new TRIE();
    private GameHistoryController gsc = new GameHistoryController();

    public PlayResource(List<GameInstance> games, UUID uid) {
        this.setGameInstanceList(games);
        this.setUid(uid);
    }


    // TODO keep this method, no rework needed
    @GET
    @Timed
    public Response randomGame(@QueryParam("name") Optional<String> name) {
        String defaultName = "Anonymous";
        String playerId = uid.randomUUID().toString();

        if(name.isPresent()) {
            defaultName = name.get();
        }

        Player p = new Player(playerId, null, defaultName);
        playersQueueing.add(p);

        if(playersQueueing.size() > 3) {
            GameInstance gi = new GameInstance(uid.randomUUID().toString());

            //TODO for first n players
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


    // TODO remove gameStatus function, websockets make this deprecated
    @GET
    @Path("/status/{playerId}")
    public Response gameStatus(@PathParam("playerId") String playerId) {
        Boolean queueing = false;
        JsonObject responseJson;

        for(Player pl: this.getPlayersQueueing()) {
            if(pl.getId().equals(playerId)) {
                queueing = true;
            }
        }

        if(queueing) {
            responseJson = Json.createObjectBuilder()
                            .add("status", "queueing").build();
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } else {
            GameInstance game = this.getGameByPlayer(playerId);
            Boolean onTurn = false;

            if(game != null && game.getPlayerIndex() > -1) {
                onTurn = game.getPlayerOnTurn().getId().equals(playerId);
                if(!game.getPollActive()) {
                    responseJson = Json.createObjectBuilder()
                            .add("status", "inGame")
                            .add("onTurn", onTurn.toString())
                            .add("gameId", game.getGameId())
                            .add("story", game.getStoryString()).build();
                    return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
                } else {
                    responseJson = Json.createObjectBuilder()
                            .add("status", "inGame")
                            .add("onTurn", "endGamePoll")
                            .add("gameId", game.getGameId())
                            .add("story", game.getStoryString()).build();
                    return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
                }
            } else {
                if(game != null) {
                    this.getGsc().writeGameHistory(game.getGameId(), game.getStoryString());
                    try {
                        this.getGameInstanceList().remove(game);
                    } catch (Exception e){
                        /* ignore */
                    }
                }

                responseJson = Json.createObjectBuilder()
                                .add("status", "gameFinished").build();
                return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
            }
        }
    }

    // TODO change this to websocket message with code = "play-turn"
    @POST
    @Path("/turn/{playerId}/{word}")
    public Response playTurn(@PathParam("playerId") String playerId,
                             @PathParam("word") String word) {
        Boolean queueing = false;
        JsonObject responseJson;

        for(Player pl: this.getPlayersQueueing()) {
            if(pl.getId().equals(playerId)) {
                queueing = true;
            }
        }

        if(queueing) {
            responseJson = Json.createObjectBuilder()
                    .add("status", "queueing").build();
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } else {
            GameInstance game = this.getGameByPlayer(playerId);
            String currentStory = "";

            word = word.toLowerCase();

            if(game != null) {
                if(game.getPollActive()) {
                    return Response.ok("endGamePoll", MediaType.TEXT_PLAIN).build();
                }

                if(game.getPlayerOnTurn().getId().equals(playerId)) {
                    if(word.equals("endofgamepoll")) {
                        Runnable turn = new Runnable () {
                            public void run() {
                                game.nextPlayer(true);
                            }
                        };

                        new Thread(turn).start();
                    } else {
                        Pattern pattern = Pattern.compile("\\s");
                        Matcher matcher = pattern.matcher(word);
                        boolean found = matcher.find();

                        if((wordChecker.checkWord(word) || (!game.getLiteratureMode() && !found)) || word.equals("endsentencedot") || word.equals("endsentencequestionmark") || word.equals("endsentenceexclamationmark")) {


                            if (game.getWordCount() != 0) {
                                if(word.equals("endsentencedot")) {
                                    currentStory = game.getStoryString() + ".";
                                }
                                else if(word.equals("endsentencequestionmark")) {
                                    currentStory = game.getStoryString() + "?";
                                }
                                else if(word.equals("endsentenceexclamationmark")) {
                                    currentStory = game.getStoryString() + "!";
                                }
                                else {
                                    currentStory = game.getStoryString() + " " + word;
                                }
                            } else {
                                if(word.equals("endsentencedot")) {
                                    currentStory = game.getStoryString() + ".";
                                }
                                else if(word.equals("endsentencequestionmark")) {
                                    currentStory = game.getStoryString() + "?";
                                }
                                else if(word.equals("endsentenceexclamationmark")) {
                                    currentStory = game.getStoryString() + "!";
                                }
                                else {
                                    currentStory = word;
                                }
                            }
                            game.setWordCount(game.getWordCount() + 1);
                            game.setStoryString(currentStory);

                            Runnable turn = new Runnable() {
                                public void run() {
                                    game.nextPlayer();
                                }
                            };

                            new Thread(turn).start();
                        } else {
                            return Response.ok("Invalid word", MediaType.TEXT_PLAIN).build();
                        }
                    }
                }
            }

            return Response.ok("Turn played", MediaType.TEXT_PLAIN).build();
        }
    }

    // TODO change this to websocket message with code = "end-game-vote"
    @POST
    @Path("/poll/{playerId}/{vote}")
    public Response pollVote(@PathParam("playerId") String playerId,
                             @PathParam("vote") String vote) {
        GameInstance game = this.getGameByPlayer(playerId);

        if(game != null) {
            game.submitVote(playerId, vote);
        }

        return Response.ok("Vote submitted", MediaType.TEXT_PLAIN).build();
    }


    // TODO change this to websocket method @OnWebSocketClose
    @POST
    @Path("/leave/{playerId}")
    public Response leaveGame(@PathParam("playerId") String playerId) {
        Player p = null;
        for(Player pl: this.getPlayersQueueing()) {
            if(pl.getId().equals(playerId)) {
                p = pl;
            }
        }
        if(p != null) {
            this.getPlayersQueueing().remove(p);
        }
        return Response.ok("Left queue", MediaType.TEXT_PLAIN).build();
    }

    // TODO keep this method, no rework needed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/words")
    public Response getSuggestedWords(@QueryParam("q") String keyword,
                                      @QueryParam("gameId") String gameId) {
        try {
            keyword = keyword.toLowerCase();
            boolean literature = false;

            for(GameInstance gi: this.getGameInstanceList()) {
                if(gi.getGameId().equals(gameId)) {
                    literature = gi.getLiteratureMode();
                }
            }

            List suggested=new ArrayList<String>();
            if(keyword.equals(".")) {
                suggested.add(".");

            } else if(keyword.equals("?")) {
                suggested.add("?");
            }
            else if(keyword.equals("!"))
            {
                suggested.add("!");
            }
            else
             {
                Integer number;
                try
                {
                    number=Integer.parseInt(keyword);
                    suggested.add(keyword);
                }
                catch(Exception e)
                {
                    suggested = wordChecker.suggestWord(keyword);
                    if(!literature) {
                        suggested.add(keyword);
                    }
                }
            }

            GenericEntity<List<String>> entity =
                    new GenericEntity<List<String>>(suggested) {};
            return Response.ok(entity).build();
        } catch (Exception e) {
            return Response.ok("[]").build();
        }
    }


    public GameInstance getGameByPlayer(String playerId) {
        for(GameInstance gi: this.getGameInstanceList()) {
            for(Player pl: gi.getPlayerList()) {
                if(pl.getId().equals(playerId)) {
                    return gi;
                }
            }
        }
        return null;
    }

    public GameHistoryController getGsc() {
        return gsc;
    }

    public void setGsc(GameHistoryController gsc) {
        this.gsc = gsc;
    }

    public List<GameInstance> getGameInstanceList() {
        return gameInstanceList;
    }

    public void setGameInstanceList(List<GameInstance> gameInstanceList) {
        this.gameInstanceList = gameInstanceList;
    }

    public List<Player> getPlayersQueueing() { return playersQueueing; }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
