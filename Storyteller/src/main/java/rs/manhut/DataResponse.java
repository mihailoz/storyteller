package rs.manhut;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mihailozdravkovic on 11/8/16.
 */
public class DataResponse {

    // codes:
    // 100 - start game
    // 101 - submit turn
    // 102 - end game vote
    @JsonProperty("code")
    public Integer code;

    @JsonProperty("playerId")
    public String playerId;

    @JsonProperty("message")
    public String message;

    @JsonProperty("gameId")
    public String gameId;
}
