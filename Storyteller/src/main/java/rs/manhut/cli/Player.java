package rs.manhut.cli;

import org.eclipse.jetty.websocket.api.Session;
import rs.manhut.core.GameInstance;

/**
 * Created by mihailo on 3.9.16..
 */
public class Player {

    private String id;
    private GameInstance game;
    private String playerName;
    private Session session;

    public Player (String playerId, GameInstance newGameId, String name) {
        this.setId(playerId);
        this.setGame(newGameId);
        this.setPlayerName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameInstance getGame() {
        return game;
    }

    public void setGame(GameInstance game) {
        this.game = game;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
