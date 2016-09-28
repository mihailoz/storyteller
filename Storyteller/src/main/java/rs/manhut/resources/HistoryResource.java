package rs.manhut.resources;

import rs.manhut.cli.Game;
import rs.manhut.db.GameDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by mihailo on 28.9.16..
 */
@Path("/history")
public class HistoryResource {

    private GameDAO dao;

    public HistoryResource (GameDAO dao) {
        this.setDao(dao);
    }

//    @GET
//    @Path("/{game-id}")
//    public Response findStory(@PathParam("game-id") String gameId){
//        return List<Game> story = dao.findGame(gameId);
//    }

    public GameDAO getDao() {
        return dao;
    }

    public void setDao(GameDAO dao) {
        this.dao = dao;
    }
}
