package rs.manhut.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import rs.manhut.cli.Game;

import java.util.List;

/**
 * Created by mihailo on 28.9.16..
 */
public class GameDAO extends AbstractDAO<Game> {

    public GameDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Game> findGame(String gameId) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(gameId).append("%");
        return list(namedQuery("rs.manhut.game.find").setParameter("id", builder.toString()));
    }
}