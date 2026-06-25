import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * PlayerService.java
 * This class talks to the database on behalf of the program.
 * It handles three main jobs:
 *   1. login()              - check if username + password are correct
 *   2. updateStatistics()   - save the game result to the database
 *   3. getTopFiveScorers()  - get the 5 best players from the database
 */
public class PlayerService {

    /**
     * login()
     * Checks the database for a player with the given username and password.
     * Returns a Player object if found, or null if the login is wrong.
     *
     * How it works:
     *   1. Build a SQL query using ? placeholders (safe, avoids SQL injection)
     *   2. Fill in the username and password
     *   3. Run the query
     *   4. If a row is found → create and return a Player object
     *   5. If no row found   → return null (login failed)
     */
    public Player login(String username, String password) {
        String sql = "SELECT * FROM players WHERE username = ? AND password = ?";

        try {
            Connection        conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);  // fill in first ?
            stmt.setString(2, password);  // fill in second ?

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                // A matching row was found → build a Player from the row data
                int    rowId    = result.getInt("id");
                String uname    = result.getString("username");
                int    wins     = result.getInt("wins");
                int    losses   = result.getInt("losses");
                int    draws    = result.getInt("draws");
                int    score    = result.getInt("score");

                return new Player(rowId, uname, wins, losses, draws, score);
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null; // login failed
    }

    /**
     * updateStatistics()
     * Updates the database after a game ends.
     *
     * result must be one of: "WIN", "LOSE", or "DRAW"
     *
     * Score rules:
     *   WIN  → +10 points
     *   DRAW → +3 points
     *   LOSE → +0 points
     */
    public void updateStatistics(Player player, String result) {
        int    addScore = 0;
        String sql      = "";

        if (result.equalsIgnoreCase("WIN")) {
            addScore = 10;
            sql = "UPDATE players SET wins = wins + 1, score = score + ? WHERE id = ?";

        } else if (result.equalsIgnoreCase("LOSE")) {
            addScore = 0;
            sql = "UPDATE players SET losses = losses + 1, score = score + ? WHERE id = ?";

        } else if (result.equalsIgnoreCase("DRAW")) {
            addScore = 3;
            sql = "UPDATE players SET draws = draws + 1, score = score + ? WHERE id = ?";
        }

        try {
            Connection        conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, addScore);       // fill in the score to add
            stmt.setInt(2, player.getId()); // fill in which player row to update

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Update statistics error: " + e.getMessage());
        }
    }

    /**
     * getTopFiveScorers()
     * Retrieves the 5 players with the highest score from the database.
     * If two players have the same score, the one with more wins comes first.
     *
     * Returns a list of Player objects (could be less than 5 if fewer players exist).
     */
    public ArrayList<Player> getTopFiveScorers() {
        ArrayList<Player> topList = new ArrayList<>();

        // MySQL / PostgreSQL use LIMIT 5
        // SQL Server uses TOP 5 — change this line if you use SQL Server:
        //   String sql = "SELECT TOP 5 * FROM players ORDER BY score DESC, wins DESC";
        String sql = "SELECT * FROM players ORDER BY score DESC, wins DESC LIMIT 5";

        try {
            Connection        conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet         result = stmt.executeQuery();

            while (result.next()) {
                int    rowId  = result.getInt("id");
                String uname  = result.getString("username");
                int    wins   = result.getInt("wins");
                int    losses = result.getInt("losses");
                int    draws  = result.getInt("draws");
                int    score  = result.getInt("score");

                topList.add(new Player(rowId, uname, wins, losses, draws, score));
            }

        } catch (Exception e) {
            System.out.println("Get top scorers error: " + e.getMessage());
        }

        return topList;
    }

    /**
     * getPlayerById()
     * Reloads fresh data for a player from the database.
     * Used to refresh stats after a game ends.
     */
    public Player getPlayerById(int playerId) {
        String sql = "SELECT * FROM players WHERE id = ?";

        try {
            Connection        conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerId);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int    rowId  = result.getInt("id");
                String uname  = result.getString("username");
                int    wins   = result.getInt("wins");
                int    losses = result.getInt("losses");
                int    draws  = result.getInt("draws");
                int    score  = result.getInt("score");

                return new Player(rowId, uname, wins, losses, draws, score);
            }

        } catch (Exception e) {
            System.out.println("Get player error: " + e.getMessage());
        }

        return null;
    }
}
