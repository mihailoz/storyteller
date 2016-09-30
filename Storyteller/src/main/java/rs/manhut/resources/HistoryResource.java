package rs.manhut.resources;

import com.codahale.metrics.annotation.Timed;
import rs.manhut.core.GameHistoryController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mihailo on 28.9.16..
 */
@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
public class HistoryResource {

    private GameHistoryController gsc = new GameHistoryController();

    @GET
    @Timed
    @Path("/{gameId}")
    public Response getGameStory(@PathParam("gameId") String gameId) {
        String story = this.getGsc().readGameHistory(gameId);
        return Response.ok(story, MediaType.TEXT_PLAIN).build();
    }

    public GameHistoryController getGsc() {
        return gsc;
    }

    public void setGsc(GameHistoryController gsc) {
        this.gsc = gsc;
    }
}
