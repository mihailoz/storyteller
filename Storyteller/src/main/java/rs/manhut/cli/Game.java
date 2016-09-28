package rs.manhut.cli;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mihailo on 28.9.16..
 */

@Entity
@Table(name = "played_games")
@NamedQueries({
        @NamedQuery(name = "rs.manhut.game.find",
                query = "select pg from played_games pg where id = :id"),
        @NamedQuery(name = "rs.manhut.game.insert",
                query = "insert pg(id, story) from played_games pg values (:id, :story)")
})
public class Game {

    /**
     * Entity's unique identifier.
     */
    @Column(name = "id")
    private String id;

    @Column(name = "story")
    private String story;


    /**
     * A no-argument constructor.
     */
    public Game() {
    }

    public Game(String id, String story) {
        this.id = id;
        this.story = story;
    }

}
