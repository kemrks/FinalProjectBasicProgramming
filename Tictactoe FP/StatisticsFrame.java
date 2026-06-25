import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StatisticsFrame.java
 * Shows the personal statistics of the currently logged-in player.
 *
 * It displays:
 *   - Username
 *   - Number of wins
 *   - Number of losses
 *   - Number of draws
 *   - Total score
 *
 * It also reloads fresh data from the database when opened,
 * so the numbers are always up to date.
 */
public class StatisticsFrame extends JFrame {

    private Player        currentPlayer;
    private PlayerService playerService;

    public StatisticsFrame(Player player) {
        this.currentPlayer = player;
        this.playerService = new PlayerService();

        // Reload the player's latest stats from the database
        Player freshData = playerService.getPlayerById(player.getId());
        if (freshData != null) {
            this.currentPlayer = freshData;
        }

        buildGUI();
    }

    /**
     * buildGUI()
     * Creates the stats display with labels and a close button.
     */
    private void buildGUI() {
        setTitle("My Statistics — " + currentPlayer.getUsername());
        setSize(300, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0;

        // --- Title ---
        JLabel titleLabel = new JLabel("📊 My Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 80, 160));
        gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // --- Divider ---
        JSeparator sep = new JSeparator();
        gbc.gridy = 1;
        mainPanel.add(sep, gbc);

        // --- Player name ---
        gbc.gridwidth = 1;
        addStatRow(mainPanel, gbc, 2, "Player:", currentPlayer.getUsername(), new Color(30, 80, 160));
        addStatRow(mainPanel, gbc, 3, "Wins:",   String.valueOf(currentPlayer.getWins()),   new Color(34, 139, 34));
        addStatRow(mainPanel, gbc, 4, "Losses:", String.valueOf(currentPlayer.getLosses()), new Color(160, 30, 30));
        addStatRow(mainPanel, gbc, 5, "Draws:",  String.valueOf(currentPlayer.getDraws()),  new Color(184, 134, 11));
        addStatRow(mainPanel, gbc, 6, "Score:",  String.valueOf(currentPlayer.getScore()),  new Color(80, 0, 120));

        // --- Close button ---
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(30, 80, 160));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridy = 7; gbc.gridwidth = 2; gbc.gridx = 0;
        mainPanel.add(btnClose, gbc);

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // just close this window, menu stays open
            }
        });

        add(mainPanel);
    }

    /**
     * addStatRow()
     * Helper that adds one label-value row to the panel.
     * Example: "Wins:"  →  "5"
     */
    private void addStatRow(JPanel panel, GridBagConstraints gbc,
                             int row, String labelText, String valueText, Color valueColor) {

        // Left label (e.g. "Wins:")
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(label, gbc);

        // Right value (e.g. "5")
        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Arial", Font.PLAIN, 13));
        value.setForeground(valueColor);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(value, gbc);
    }
}
