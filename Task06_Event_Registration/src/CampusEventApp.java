
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;

public class CampusEventApp extends JFrame {

    private static final String URL =
        "jdbc:mysql://localhost:3306/campus_events?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    // Replace this with your actual MySQL root password.
    private static final String PASSWORD = "Candy@123";

    private JTextField studentIdField;
    private JTextField studentNameField;
    private JComboBox<String> eventBox;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public CampusEventApp() {
        setTitle("Campus Event Registration System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Student Registration"));

        studentIdField = new JTextField();
        studentNameField = new JTextField();

        eventBox = new JComboBox<>(new String[] {
            "Technical Symposium",
            "AI Workshop",
            "Coding Competition",
            "Cultural Fest",
            "Sports Meet"
        });

        JButton registerButton = new JButton("Register");
        JButton refreshButton = new JButton("Refresh");

        form.add(new JLabel("Student ID:"));
        form.add(studentIdField);
        form.add(new JLabel("Student Name:"));
        form.add(studentNameField);
        form.add(new JLabel("Select Event:"));
        form.add(eventBox);
        form.add(registerButton);
        form.add(refreshButton);

        model = new DefaultTableModel(
            new String[] {
                "Registration ID", "Student ID", "Student Name",
                "Event Name", "Registration Date"
            }, 0
        );

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton exportButton = new JButton("Export CSV");

        searchPanel.add(new JLabel("Search Student/Event:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(exportButton);

        add(form, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> registerStudent());
        refreshButton.addActionListener(e -> loadRegistrations(""));
        searchButton.addActionListener(e -> loadRegistrations(searchField.getText().trim()));
        exportButton.addActionListener(e -> exportCSV());

        loadRegistrations("");
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void registerStudent() {
        String studentId = studentIdField.getText().trim();
        String studentName = studentNameField.getText().trim();
        String eventName = (String) eventBox.getSelectedItem();

        if (studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter Student ID and Student Name.");
            return;
        }

        String sql = "INSERT INTO registrations " +
            "(student_id, student_name, event_name) VALUES (?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, studentName);
            ps.setString(3, eventName);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student registered successfully!");

            studentIdField.setText("");
            studentNameField.setText("");
            loadRegistrations("");

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                "This student is already registered for this event.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Registration failed: " + ex.getMessage());
        }
    }

    private void loadRegistrations(String search) {
        model.setRowCount(0);

        String sql = "SELECT * FROM registrations " +
            "WHERE student_id LIKE ? OR student_name LIKE ? " +
            "OR event_name LIKE ? ORDER BY registration_id";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + search + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[] {
                        rs.getInt("registration_id"),
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("event_name"),
                        rs.getTimestamp("registration_date")
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Could not load registrations: " + ex.getMessage());
        }
    }

    private void exportCSV() {
        try (FileWriter writer = new FileWriter("registrations.csv")) {
            writer.write("Registration ID,Student ID,Student Name,Event Name,Registration Date\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    String value = String.valueOf(model.getValueAt(i, j));
                    writer.write("\"" + value.replace("\"", "\"\"") + "\"");
                    if (j < model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(this,
                "CSV exported as registrations.csv in the src folder.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Export failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                new CampusEventApp().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "Unable to start application: " + ex.getMessage());
            }
        });
    }
}
