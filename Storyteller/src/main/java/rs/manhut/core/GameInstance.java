package rs.manhut.core;

import rs.manhut.cli.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by mihailo on 3.9.16..
 */
public class GameInstance extends Thread {
    private String gameId;
    private String storyString = "";
    private List<Player> playerList = new ArrayList<Player>();
    private Player playerOnTurn;
    private Integer playerIndex;
    private Integer wordCount;
    private Future<String> future;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static Integer turnLength = 9;

    public GameInstance (String id) {
        this.setGameId(id);
    }

    public void run () {
        this.setGameId(this.getGameId());
        this.setPlayerOnTurn(this.getPlayerList().get(0));
        this.setPlayerIndex(0);
        this.setWordCount(0);
        this.setFuture(executor.submit(new PlayerTurn()));

        try {
            String result = this.getFuture().get(this.getTurnLength(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            this.nextPlayer();
        } catch (Exception e) {
            //DO NOTHING
        }
    }

    public void nextPlayer() {
        Integer nextPlayer = (this.getPlayerIndex() + 1) % this.getPlayerList().size();
        this.setPlayerIndex(nextPlayer);
        this.setPlayerOnTurn(this.getPlayerList().get(nextPlayer));

        if(this.getFuture() != null) {
            this.getFuture().cancel(true);
        }

        this.setFuture(executor.submit(new PlayerTurn()));

        try {
            String result = this.getFuture().get(this.getTurnLength(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            this.nextPlayer();
        } catch (Exception e) {
            //DO NOTHING
        }

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

    public Integer getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(Integer playerIndex) {
        this.playerIndex = playerIndex;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public Future<String> getFuture() {
        return future;
    }

    public void setFuture(Future<String> future) {
        this.future = future;
    }

    public static Integer getTurnLength() {
        return turnLength;
    }

    public static void setTurnLength(Integer turnLength) {
        GameInstance.turnLength = turnLength;
    }
}

class PlayerTurn implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(16000);
        return "Turn finished";
    }
}