package rs.manhut.websockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

/**
 * Created by mihailozdravkovic on 11/8/16.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class PlaySocket {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

//    public Player getPlayer(String playerId) {
//        for(GameInstance gi : StorytellerApplication.gameInstanceList) {
//            for(Player p : gi.getPlayerList()) {
//                if(p.getId().equals(playerId)) {
//                    return p;
//                }
//            }
//        }
//        return null;
//    }
//
//    public GameInstance getGameByPlayer(String playerId) {
//        for(GameInstance gi: StorytellerApplication.gameInstanceList) {
//            for(Player pl: gi.getPlayerList()) {
//                if(pl.getId().equals(playerId)) {
//                    return gi;
//                }
//            }
//        }
//        return null;
//    }
}