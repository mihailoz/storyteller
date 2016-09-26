package rs.manhut.cli;

/**
 * Created by mihailozdravkovic on 9/26/16.
 */
public class Poll {

    private Boolean vote;
    private Player p;

    public Poll(Player p){
        this.p = p;
        this.vote = null;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }
}
