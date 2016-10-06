package rs.manhut.cli;

/**
 * Created by mihailo on 28.9.16..
 */

public class Game {
    private String gameId;
    private String gameName;
    private Integer playerNumber;
    private Boolean passwordProtected;
    private Boolean isOwner;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Boolean getPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(Boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }
}
