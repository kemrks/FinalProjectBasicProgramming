/**
 * Player.java
 * This is a simple data class (model).
 * It stores one player's information that was loaded from the database.
 *
 * Think of it like a box that holds:
 *   - id        : the row number in the database
 *   - username  : the player's login name
 *   - wins      : how many times the player won
 *   - losses    : how many times the player lost
 *   - draws     : how many times the game was a draw
 *   - score     : the player's total score
 */
public class Player {

    private int    id;
    private String username;
    private int    wins;
    private int    losses;
    private int    draws;
    private int    score;

    // Constructor: called when we create a new Player object
    public Player(int id, String username, int wins, int losses, int draws, int score) {
        this.id       = id;
        this.username = username;
        this.wins     = wins;
        this.losses   = losses;
        this.draws    = draws;
        this.score    = score;
    }

    // --- Getters (methods to read the values) ---

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getScore() {
        return score;
    }
}
