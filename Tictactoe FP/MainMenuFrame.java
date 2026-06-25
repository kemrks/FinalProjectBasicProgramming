import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MainMenuFrame.java
 * This window appears after the user logs in successfully.
 * It shows the player's name and four buttons:
 *   - Start Game
 *   - My Statistics
 *   - Top 5 Scorers
 *   - Exit
 */
public class MainMenuFrame extends JFrame {

    private Player  currentPlayer; // the player who is logged in
    private JButton btnStartGame;
    private JButton btnMyStats;
    private JButton btnTopScorers;
    private JButton btnExit;

    public MainMenuFrame(Player player) {
        this.currentPlayer = player;

        buildGUI();
        connectButtonActions();
    }

    /**
     * buildGUI()
     * Creates all the buttons and labels for the main menu.
     */
    private void buildGUI() {
        setTitle("Tic-Tac-Toe — Main Menu");
        setSize(320, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0;

        // --- Welcome label ---
        JLabel welcomeLabel = new JLabel(
            "Hello, " + currentPlayer.getUsername() + "!",
            SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(30, 80, 160));
        gbc.gridy = 0;
        mainPanel.add(welcomeLabel, gbc);

        // --- Subtitle ---
        JLabel subtitleLabel = new JLabel("What would you like to do?", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // --- Buttons ---
        btnStartGame  = makeMenuButton("▶  Start Game",       new Color(34, 139, 34));
        btnMyStats    = makeMenuButton("📊  My Statistics",   new Color(30, 80, 160));
        btnTopScorers = makeMenuButton("🏆  Top 5 Scorers",  new Color(184, 134, 11));
        btnExit       = makeMenuButton("✖  Exit",             new Color(160, 30, 30));

        gbc.gridy = 2; mainPanel.add(btnStartGame,  gbc);
        gbc.gridy = 3; mainPanel.add(btnMyStats,    gbc);
        gbc.gridy = 4; mainPanel.add(btnTopScorers, gbc);
        gbc.gridy = 5; mainPanel.add(btnExit,        gbc);

        add(mainPanel);
    }

    /**
     * makeMenuButton()
     * Helper that creates a styled button with a given label and color.
     */
    private JButton makeMenuButton(String label, Color bgColor) {
        JButton btn = new JButton(label);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    /**
     * connectButtonActions()
     * Attaches click actions to each menu button.
     */
    private void connectButtonActions() {

        // Start Game → open the game window
        btnStartGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameFrame gameWindow = new GameFrame(currentPlayer);
                gameWindow.setVisible(true);
                dispose(); // close the menu while playing
            }
        });

        // My Statistics → open the stats window
        btnMyStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StatisticsFrame statsWindow = new StatisticsFrame(currentPlayer);
                statsWindow.setVisible(true);
            }
        });

        // Top 5 Scorers → open the leaderboard window
        btnTopScorers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TopScorersFrame topWindow = new TopScorersFrame();
                topWindow.setVisible(true);
            }
        });

        // Exit → close the whole application
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    MainMenuFrame.this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
}
