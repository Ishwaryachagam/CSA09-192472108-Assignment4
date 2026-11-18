
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    private final List<Appointment> appointments =
            new ArrayList<>();

    public String bookAppointment(Appointment appointment) {

        if (appointment.getPatientName().trim().isEmpty()) {
            return "Please enter patient name.";
        }

        if (appointment.getPatientId().trim().isEmpty()) {
            return "Please enter patient ID.";
        }

        for (Appointment a : appointments) {
            if (a.getPatientId().equalsIgnoreCase(
                    appointment.getPatientId())) {
                return "Appointment already exists for this Patient ID.";
            }
        }

        appointments.add(appointment);

        return "Appointment booked successfully!";
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
