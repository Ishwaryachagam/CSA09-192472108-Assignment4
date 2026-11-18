
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterC extends JFrame {

    JTextField amountField;
    JComboBox<String> currencyBox;
    JLabel resultLabel;

    ConverterC() {
        setTitle("Currency Converter - Anonymous Class");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2, 10, 10));

        amountField = new JTextField();
        currencyBox = new JComboBox<>(new String[]{"USD", "EUR"});

        JButton convertButton = new JButton("Convert");
        resultLabel = new JLabel("Result: ");

        add(new JLabel("Enter INR:"));
        add(amountField);
        add(new JLabel("Convert To:"));
        add(currencyBox);
        add(convertButton);
        add(resultLabel);

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountField.getText());

                    if (amount < 0) {
                        throw new NumberFormatException();
                    }

                    String currency =
                        (String) currencyBox.getSelectedItem();

                    double rate = currency.equals("USD") ? 0.012 : 0.011;

                    resultLabel.setText(String.format(
                        "Result: %.2f %s", amount * rate, currency
                    ));

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ConverterC.this,
                        "Enter a valid non-negative amount.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ConverterC().setVisible(true)
        );
    }
}
