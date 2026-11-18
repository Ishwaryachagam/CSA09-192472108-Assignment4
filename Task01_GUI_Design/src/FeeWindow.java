
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FeeWindow extends JFrame implements ActionListener {

    private JTextField patientField;
    private JTextField amountField;
    private JButton payButton;
    private JButton receiptButton;
    private JLabel statusLabel;

    private FeeService feeService;
    private String lastReceipt = "";

    public FeeWindow() {

        feeService = new FeeService();

        setTitle("Hospital Fee Payment System");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        panel.add(new JLabel("Patient Name:"));
        patientField = new JTextField();
        panel.add(patientField);

        panel.add(new JLabel("Fee Amount (Rs.):"));
        amountField = new JTextField();
        panel.add(amountField);

        payButton = new JButton("Pay Fee");
        receiptButton = new JButton("View Receipt");

        panel.add(payButton);
        panel.add(receiptButton);

        statusLabel = new JLabel("Enter patient details.");
        panel.add(statusLabel);

        add(panel);

        payButton.addActionListener(this);
        receiptButton.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    FeeWindow.this,
                    "Are you sure you want to close?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == payButton) {

            String patientName = patientField.getText();
            String amountText = amountField.getText();

            lastReceipt = feeService.processPayment(
                patientName, amountText
            );

            statusLabel.setText(lastReceipt.replace("\n", " "));

            JOptionPane.showMessageDialog(this, lastReceipt);

        } else if (e.getSource() == receiptButton) {

            if (lastReceipt.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "No payment receipt available."
                );
            } else {
                JOptionPane.showMessageDialog(
                    this, lastReceipt, "Payment Receipt",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FeeWindow window = new FeeWindow();
            window.setVisible(true);
        });
    }
}
