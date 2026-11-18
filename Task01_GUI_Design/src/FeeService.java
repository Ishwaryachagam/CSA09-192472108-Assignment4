
public class FeeService {

    public String processPayment(String patientName, String amountText) {

        if (patientName == null || patientName.trim().isEmpty()) {
            return "Error: Please enter patient name.";
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            return "Error: Enter a valid amount.";
        }

        if (amount <= 0) {
            return "Error: Amount must be greater than zero.";
        }

        // Simulated payment processing
        return String.format(
            "Payment Successful!\nPatient: %s\nAmount: Rs. %.2f",
            patientName, amount
        );
    }
}
