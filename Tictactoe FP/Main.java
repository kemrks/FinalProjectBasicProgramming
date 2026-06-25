import javax.swing.SwingUtilities;

/**
 * Main.java
 * This is the starting point of the program.
 * It opens the Login Window when the program runs.
 */
public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater makes sure the GUI starts safely
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame loginWindow = new LoginFrame();
                loginWindow.setVisible(true);
            }
        });
    }
}
