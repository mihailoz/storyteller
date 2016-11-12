package rs.manhut.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Created by mihailozdravkovic on 11/8/16.
 */
public class WebsocketData {

    // codes:
    // "success" - server success response
    // "start-game" - start game
    // "submit-turn" - submit turn
    // "end-game" - end game start poll
    // "end-game-vote" - end game poll vote
    // "error" - an error has happened, send error to client
    @JsonProperty("code")
    public String code;

    @JsonProperty("playerId")
    public String playerId;

    @JsonProperty("gameId")
    public String gameId;

    public HashMap<String, String> data;


    public static WebsocketData createError(String message) {
        WebsocketData wd = new WebsocketData();
        wd.setCode("error");
        wd.setData(new HashMap<String, String>());

        wd.getData().put("message", message);

        return wd;
    }

    public static WebsocketData createSuccess(String message) {
        WebsocketData wd = new WebsocketData();
        wd.setCode("success");
        wd.setData(new HashMap<String, String>());

        wd.getData().put("message", message);

        return wd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
