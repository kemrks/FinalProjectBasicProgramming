import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * TopScorersFrame.java
 * Shows the Top 5 players by score in a table (JTable).
 *
 * The data comes directly from the database via PlayerService.
 * Nothing is hardcoded — it always reflects the latest results.
 *
 * Table columns:
 *   Rank | Username | Wins | Losses | Draws | Score
 */
public class TopScorersFrame extends JFrame {

    private PlayerService playerService;
    private JTable        table;

    public TopScorersFrame() {
        this.playerService = new PlayerService();

        buildGUI();
    }

    /**
     * buildGUI()
     * Fetches the top 5 from the database and builds the table.
     */
    private void buildGUI() {
        setTitle("🏆 Top 5 Scorers");
        setSize(500, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        // --- Title label ---
        JLabel titleLabel = new JLabel("🏆 Top 5 Scorers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(184, 134, 11));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- Table column headers ---
        String[] columnHeaders = {"Rank", "Username", "Wins", "Losses", "Draws", "Score"};

        // DefaultTableModel holds the data for the JTable
        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0) {
            // Make all cells non-editable (read-only table)
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // --- Fetch top 5 from database and fill the table ---
        ArrayList<Player> topPlayers = playerService.getTopFiveScorers();

        if (topPlayers.isEmpty()) {
            // No players in the database yet
            tableModel.addRow(new Object[]{"—", "No data yet", "—", "—", "—", "—"});
        } else {
            for (int rank = 0; rank < topPlayers.size(); rank++) {
                Player p = topPlayers.get(rank);
                tableModel.addRow(new Object[]{
                    rank + 1,           // Rank (1-based)
                    p.getUsername(),
                    p.getWins(),
                    p.getLosses(),
                    p.getDraws(),
                    p.getScore()
                });
            }
        }

        // --- Create the JTable ---
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(30, 80, 160));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));

        // Center-align all columns
        DefaultTableCellRenderer centerAlign = new DefaultTableCellRenderer();
        centerAlign.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col = 0; col < table.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerAlign);
        }

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // Rank
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Username
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Wins
        table.getColumnModel().getColumn(3).setPreferredWidth(60);  // Losses
        table.getColumnModel().getColumn(4).setPreferredWidth(60);  // Draws
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Score

        // Wrap the table in a scroll pane (standard for JTable)
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Close button ---
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(184, 134, 11));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.add(btnClose);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
