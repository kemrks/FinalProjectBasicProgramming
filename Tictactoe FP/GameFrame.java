import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameFrame.java
 * This window shows the Tic-Tac-Toe board.
 *
 * The board has 9 buttons arranged in a 3x3 grid.
 * Player clicks a button → 'X' appears.
 * Computer then picks a cell → 'O' appears.
 *
 * After a win, loss, or draw:
 *   1. Statistics are saved to the database
 *   2. A result message is shown
 *   3. The user is returned to the main menu
 */
public class GameFrame extends JFrame {

    private Player        currentPlayer;
    private PlayerService playerService;
    private GameLogic     gameLogic;

    private JButton[] cellButtons; // the 9 board buttons
    private JLabel    lblStatus;   // shows whose turn it is or the result
    private JButton   btnRestart;  // button to start a new game
    private JButton   btnBack;     // button to go back to main menu

    private boolean gameOver; // true when the game has ended

    public GameFrame(Player player) {
        this.currentPlayer = player;
        this.playerService = new PlayerService();
        this.gameLogic     = new GameLogic();
        this.gameOver      = false;

        buildGUI();
        connectButtonActions();
    }

    /**
     * buildGUI()
     * Creates the 3x3 grid of buttons and the status label.
     */
    private void buildGUI() {
        setTitle("Tic-Tac-Toe — " + currentPlayer.getUsername() + " (X) vs Computer (O)");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        // --- Status label at the top ---
        lblStatus = new JLabel("Your turn! Click a cell to place X.", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setForeground(new Color(30, 80, 160));
        mainPanel.add(lblStatus, BorderLayout.NORTH);

        // --- 3x3 grid of buttons ---
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 6, 6));
        boardPanel.setBackground(new Color(30, 80, 160));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        cellButtons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            cellButtons[i] = new JButton("");
            cellButtons[i].setFont(new Font("Arial", Font.BOLD, 36));
            cellButtons[i].setBackground(Color.WHITE);
            cellButtons[i].setFocusPainted(false);
            boardPanel.add(cellButtons[i]);
        }

        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // --- Bottom buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBackground(new Color(240, 248, 255));

        btnRestart = new JButton("New Game");
        btnRestart.setBackground(new Color(34, 139, 34));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setFont(new Font("Arial", Font.BOLD, 12));

        btnBack = new JButton("Back to Menu");
        btnBack.setBackground(new Color(30, 80, 160));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 12));

        bottomPanel.add(btnRestart);
        bottomPanel.add(btnBack);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * connectButtonActions()
     * Links each cell button and control button to their action methods.
     */
    private void connectButtonActions() {
        // Each of the 9 board cells
        for (int i = 0; i < cellButtons.length; i++) {
            final int cellIndex = i; // must be final to use inside the anonymous class
            cellButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handlePlayerMove(cellIndex);
                }
            });
        }

        // Restart button → reset the board
        btnRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // Back button → go to main menu
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBackToMenu();
            }
        });
    }

    /**
     * handlePlayerMove()
     * Called when the player clicks a cell button.
     *
     * Steps:
     *   1. Try to place 'X' on that cell
     *   2. Update the button text
     *   3. Check if player won or if it's a draw
     *   4. Let the computer make its move
     *   5. Check if computer won or if it's a draw
     */
    private void handlePlayerMove(int cellIndex) {
        // Don't allow moves after the game is over
        if (gameOver) {
            return;
        }

        // Step 1: Try to place X on the chosen cell
        boolean moveMade = gameLogic.makeMove(cellIndex, 'X');

        if (!moveMade) {
            // Cell is already taken — ignore the click
            return;
        }

        // Step 2: Show X on the button
        cellButtons[cellIndex].setText("X");
        cellButtons[cellIndex].setForeground(new Color(30, 80, 160));
        cellButtons[cellIndex].setEnabled(false);

        // Step 3: Check if player won
        if (gameLogic.checkWinner('X')) {
            finishGame("WIN");
            return;
        }

        // Check if it's a draw after the player's move
        if (gameLogic.isDraw()) {
            finishGame("DRAW");
            return;
        }

        // Step 4: Computer makes its move
        lblStatus.setText("Computer is thinking...");

        int computerCell = gameLogic.computerMove();

        if (computerCell != -1) {
            gameLogic.makeMove(computerCell, 'O');
            cellButtons[computerCell].setText("O");
            cellButtons[computerCell].setForeground(new Color(160, 30, 30));
            cellButtons[computerCell].setEnabled(false);
        }

        // Step 5: Check if computer won
        if (gameLogic.checkWinner('O')) {
            finishGame("LOSE");
            return;
        }

        // Check if it's a draw after the computer's move
        if (gameLogic.isDraw()) {
            finishGame("DRAW");
            return;
        }

        // Game continues
        lblStatus.setText("Your turn! Click a cell to place X.");
    }

    /**
     * finishGame()
     * Called when the game ends (win, lose, or draw).
     *
     * 1. Marks the game as over
     * 2. Updates the database
     * 3. Shows a result popup
     * 4. Updates the status label
     */
    private void finishGame(String result) {
        gameOver = true;

        // Disable all remaining buttons so no more moves can be made
        for (JButton btn : cellButtons) {
            btn.setEnabled(false);
        }

        // Save the result to the database
        playerService.updateStatistics(currentPlayer, result);

        // Decide what message to show
        String message;
        String statusText;
        int    messageType;

        if (result.equals("WIN")) {
            message     = "🎉 You won! +10 points added to your score.";
            statusText  = "You won! Great job!";
            messageType = JOptionPane.INFORMATION_MESSAGE;

        } else if (result.equals("LOSE")) {
            message     = "😞 Computer won! Better luck next time.";
            statusText  = "Computer won! Try again.";
            messageType = JOptionPane.WARNING_MESSAGE;

        } else { // DRAW
            message     = "🤝 It's a draw! +3 points added to your score.";
            statusText  = "It's a draw!";
            messageType = JOptionPane.INFORMATION_MESSAGE;
        }

        lblStatus.setText(statusText);

        JOptionPane.showMessageDialog(this, message, "Game Over", messageType);
    }

    /**
     * resetGame()
     * Starts a brand new game without going back to the menu.
     */
    private void resetGame() {
        gameLogic.resetBoard();
        gameOver = false;

        for (JButton btn : cellButtons) {
            btn.setText("");
            btn.setEnabled(true);
        }

        lblStatus.setText("Your turn! Click a cell to place X.");
    }

    /**
     * goBackToMenu()
     * Closes this window and returns to the main menu.
     * Reloads the player's data first so stats are fresh.
     */
    private void goBackToMenu() {
        // Reload fresh player data (in case stats changed during the game)
        Player freshPlayer = playerService.getPlayerById(currentPlayer.getId());
        if (freshPlayer == null) {
            freshPlayer = currentPlayer; // fallback
        }

        MainMenuFrame menuWindow = new MainMenuFrame(freshPlayer);
        menuWindow.setVisible(true);
        dispose();
    }
}
