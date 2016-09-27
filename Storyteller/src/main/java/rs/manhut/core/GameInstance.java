package rs.manhut.core;

import rs.manhut.cli.Player;
import rs.manhut.cli.Poll;

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
    private List<Poll> endGamePoll;
    private Boolean pollActive = false;

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
        nextPlayer(false);
    }

    public void nextPlayer(Boolean isPoll) {
        if (this.getFuture() != null) {
            this.getFuture().cancel(true);
        }

        this.setFuture(executor.submit(new PlayerTurn()));

        if(isPoll == false) {
            Integer nextPlayer = (this.getPlayerIndex() + 1) % this.getPlayerList().size();
            this.setPlayerIndex(nextPlayer);
            this.setPlayerOnTurn(this.getPlayerList().get(nextPlayer));

            try {
                String result = this.getFuture().get(this.getTurnLength(), TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                this.nextPlayer();
            } catch (Exception e) {
                //DO NOTHING
            }
        } else {
            this.setEndGamePoll(new ArrayList<Poll>());

            for(Player pl: this.getPlayerList()) {
                Poll tmp = new Poll(pl);

                this.getEndGamePoll().add(tmp);
            }

            try {
                this.setPollActive(true);
                String result = this.getFuture().get(this.getTurnLength(), TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                this.setPollActive(false);

                Integer forEndGame = 0;
                Integer againstEndGame = 0;

                for(Poll p: this.getEndGamePoll()) {
                    if (p.getVote() != null) {
                        if (p.getVote() == true) forEndGame++;
                        if (p.getVote() == false) forEndGame++;
                    }
                }

                if(forEndGame > againstEndGame) {
                    this.setPlayerOnTurn(null);
                    this.setPlayerIndex(-1);
                } else {
                    this.nextPlayer();
                }
            } catch (Exception e) {
                //DO NOTHING
            }
        }
    }

    public void submitVote(String playerId, String vote) {
        Boolean voteValue;
        if(vote.equals("endGame")) {
            voteValue = true;
        } else {
            voteValue = false;
        }

        if(this.getPollActive()) {
            for(Poll p: this.getEndGamePoll()) {
                if(p.getP().getId().equals(playerId)) {
                    if(p.getVote() != null) {
                        p.setVote(voteValue);
                    }
                }
            }
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

    public List<Poll> getEndGamePoll() {
        return endGamePoll;
    }

    public void setEndGamePoll(List<Poll> endGamePoll) {
        this.endGamePoll = endGamePoll;
    }

    public Boolean getPollActive() {
        return pollActive;
    }

    public void setPollActive(Boolean pollActive) {
        this.pollActive = pollActive;
    }
}

class PlayerTurn implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(16000);
        return "Turn finished";
    }
}