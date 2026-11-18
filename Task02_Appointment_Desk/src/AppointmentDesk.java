
import javax.swing.*;
import java.awt.*;

public class AppointmentDesk extends JFrame {

    private final JTextField patientName = new JTextField(20);
    private final JTextField patientId = new JTextField(20);
    private final JTextField appointmentDate = new JTextField(20);

    private final JComboBox<String> department =
            new JComboBox<>(new String[]{
                "Cardiology",
                "Neurology",
                "Orthopaedics",
                "General Medicine",
                "Dermatology"
            });

    private final AppointmentService service =
            new AppointmentService();

    private final JTextArea output = new JTextArea(8, 35);

    public AppointmentDesk() {

        super("Hospital Appointment Desk");

        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "Hospital Appointment Booking",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 20));

        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        form.setBorder(BorderFactory.createEmptyBorder(
                15, 20, 15, 20
        ));

        form.add(new JLabel("Patient Name:"));
        form.add(patientName);

        form.add(new JLabel("Patient ID:"));
        form.add(patientId);

        form.add(new JLabel("Department:"));
        form.add(department);

        form.add(new JLabel("Appointment Date (DD-MM-YYYY):"));
        form.add(appointmentDate);

        JButton bookButton = new JButton("Book Appointment");
        JButton viewButton = new JButton("View Appointments");
        JButton clearButton = new JButton("Clear");

        JPanel buttons = new JPanel();

        buttons.add(bookButton);
        buttons.add(viewButton);
        buttons.add(clearButton);

        JPanel center = new JPanel(new BorderLayout());

        center.add(form, BorderLayout.NORTH);
        center.add(buttons, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        output.setEditable(false);

        add(new JScrollPane(output), BorderLayout.SOUTH);

        bookButton.addActionListener(e -> bookAppointment());

        viewButton.addActionListener(e -> viewAppointments());

        clearButton.addActionListener(e -> clearFields());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(650, 500);

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void bookAppointment() {

        String name = patientName.getText().trim();
        String id = patientId.getText().trim();
        String dept = (String) department.getSelectedItem();
        String date = appointmentDate.getText().trim();

        if (name.isEmpty() || id.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all the fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter date in DD-MM-YYYY format.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Appointment appointment = new Appointment(
                name, id, dept, date
        );

        String result = service.bookAppointment(appointment);

        JOptionPane.showMessageDialog(this, result);

        if (result.equals("Appointment booked successfully!")) {
            output.append(
                    "Appointment booked: " + name +
                    " - " + dept + "\n"
            );
        }
    }

    private void viewAppointments() {

        output.setText("");

        for (Appointment a : service.getAppointments()) {
            output.append(a.toString() + "\n\n");
        }

        if (service.getAppointments().isEmpty()) {
            output.setText("No appointments booked yet.");
        }
    }

    private void clearFields() {
        patientName.setText("");
        patientId.setText("");
        appointmentDate.setText("");
        department.setSelectedIndex(0);
        output.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppointmentDesk::new);
    }
}
