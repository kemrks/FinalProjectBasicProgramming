import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame.java
 * This is the first window the user sees.
 * It has a username field, a password field, and a Login button.
 *
 * What it does:
 *   1. Reads the username and password the user typed
 *   2. Calls PlayerService.login() to check against the database
 *   3. If correct → opens MainMenuFrame
 *   4. If wrong   → shows an error popup
 */
public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private PlayerService  playerService;

    public LoginFrame() {
        playerService = new PlayerService();

        buildGUI();
        connectButtonActions();
    }

    /**
     * buildGUI()
     * Creates and arranges all the visual components on the window.
     */
    private void buildGUI() {
        setTitle("Tic-Tac-Toe — Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window on screen
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 5, 8, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // --- Title label ---
        JLabel titleLabel = new JLabel("Tic-Tac-Toe Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 80, 160));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // --- Username label and field ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(txtUsername, gbc);

        // --- Password label and field ---
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(txtPassword, gbc);

        // --- Login button ---
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(30, 80, 160));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(btnLogin, gbc);

        add(mainPanel);

        // Allow pressing Enter to trigger login
        getRootPane().setDefaultButton(btnLogin);
    }

    /**
     * connectButtonActions()
     * Attaches the login logic to the Login button.
     */
    private void connectButtonActions() {
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    /**
     * handleLogin()
     * Reads the username and password, calls the database, and reacts.
     */
    private void handleLogin() {
        // Read what the user typed
        String typedUsername = txtUsername.getText().trim();
        String typedPassword = new String(txtPassword.getPassword()).trim();

        // Basic check: don't allow empty fields
        if (typedUsername.isEmpty() || typedPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Missing Input",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Ask PlayerService to check the database
        Player foundPlayer = playerService.login(typedUsername, typedPassword);

        if (foundPlayer != null) {
            // Login succeeded → open the main menu
            JOptionPane.showMessageDialog(
                this,
                "Welcome, " + foundPlayer.getUsername() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            MainMenuFrame mainMenu = new MainMenuFrame(foundPlayer);
            mainMenu.setVisible(true);
            this.dispose(); // close the login window

        } else {
            // Login failed → show error
            JOptionPane.showMessageDialog(
                this,
                "Wrong username or password. Please try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );

            // Clear the password field so user can retype
            txtPassword.setText("");
        }
    }
}
