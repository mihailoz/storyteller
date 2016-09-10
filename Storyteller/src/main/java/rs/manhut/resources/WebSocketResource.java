package rs.manhut.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.PathParam;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mihailo on 2.9.16..
 */
@ManagedService(path = "/play/{playerId}")
public class WebSocketResource{

    private final ObjectMapper mapper = new ObjectMapper();
    private static Map<String, WebSocketResource> playerSockets = new HashMap<String, WebSocketResource>();

    @PathParam("playerId")
    private String playerId;

    @Inject
    private AtmosphereResource resource;

    @Ready
    public void ready(AtmosphereResource r) {
        WebSocketResource.playerSockets.put(playerId, this);
    }

    @Message
    public void message(Response response) {

    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
