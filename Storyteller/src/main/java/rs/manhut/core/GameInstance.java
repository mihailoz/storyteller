package rs.manhut.core;

import rs.manhut.cli.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihailo on 3.9.16..
 */
public class GameInstance extends Thread {
    private String gameId;
    private String storyString = "";
    private List<Player> playerList = new ArrayList<Player>();
    private Player playerOnTurn;

    public GameInstance (String id) {
        this.setGameId(id);
    }

    public void run () {
        this.setGameId(this.getGameId());
        System.out.print("GAME STARTED");
    }

    public void addPlayer(Player p) {
        this.getPlayerList().add(p);
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String id) {
        this.gameId = id;
    }

    public String getStoryString() {
        return storyString;
    }

    public void setStoryString(String storyString) {
        this.storyString = storyString;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Player getPlayerOnTurn() {
        return playerOnTurn;
    }

    public void setPlayerOnTurn(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
    }
}
