package rs.manhut.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import rs.manhut.StorytellerApplication;
import rs.manhut.cli.Player;
import rs.manhut.core.GameInstance;
import rs.manhut.core.WebsocketData;
import rs.manhut.resources.GameResource;

import java.io.IOException;

/**
 * Created by mihailozdravkovic on 11/8/16.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class PlaySocket {

    public static ObjectMapper mapper = new ObjectMapper();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("We got a message");
        try {
            WebsocketData data = mapper.readValue(message, WebsocketData.class);

            if(data.getPlayerId() != null) {
                Player pl = getPlayer(data.getPlayerId());
                GameInstance gi = getGameByPlayer(data.getPlayerId());

                if(data.getCode() != null && pl != null && gi != null) {
                    WebsocketData responseData = new WebsocketData();

                    switch(data.getCode()) {
                        // TODO dependent on code do something...
                        case "connect-websocket":
                            // If player is connecting for the first time store session;
                            pl.setSession(session);
                            responseData = WebsocketData.createSuccess("Successfully connected");
                            break;
                        case "start-game":
                            responseData = this.startGame(session, pl, gi);
                            break;
                        case "submit-turn":
                            // Submit turn in game

                            break;
                        case "end-game":
                            //
                            break;
                        case "end-game-vote":
                            break;
                        default:
                            break;
                    }

                    session.getRemote().sendString(mapper.writeValueAsString(responseData));
                } else {
                    WebsocketData dataResp = WebsocketData.createError("Player or game not found");

                    String dataRespJson = mapper.writeValueAsString(dataResp);
                    session.getRemote().sendString(dataRespJson);

                    session.close();
                }
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public WebsocketData startGame(Session session, Player player, GameInstance game) {
        try {
            if (game.getGameOwner().equals(player.getId())) {
                if (GameResource.queueingGamesList.indexOf(game) >= 0) {
                    GameResource.queueingGamesList.remove(game);
                    StorytellerApplication.gameInstanceList.add(game);

                    Runnable startGame = new Runnable() {
                        public void run() {
                            game.run();
                        }
                    };

                    new Thread(startGame).start();

                } else {
                    throw new Exception("Cannot start game. Game already started");
                }
            } else {
                throw new Exception("Cannot start game. Not game owner.");
            }
        } catch (Exception e) {
            return WebsocketData.createError(e.getMessage());
        }
    }

    public Player getPlayer(String playerId) {
        for(GameInstance gi : StorytellerApplication.gameInstanceList) {
            for(Player p : gi.getPlayerList()) {
                if(p.getId().equals(playerId)) {
                    return p;
                }
            }
        }
        return null;
    }

    public GameInstance getGameByPlayer(String playerId) {
        for(GameInstance gi: StorytellerApplication.gameInstanceList) {
            for(Player pl: gi.getPlayerList()) {
                if(pl.getId().equals(playerId)) {
                    return gi;
                }
            }
        }
        return null;
    }
}