
public class Appointment {

    private String patientName;
    private String patientId;
    private String department;
    private String appointmentDate;

    public Appointment(String patientName, String patientId,
                       String department, String appointmentDate) {

        this.patientName = patientName;
        this.patientId = patientId;
        this.department = department;
        this.appointmentDate = appointmentDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDepartment() {
        return department;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    @Override
    public String toString() {
        return "Patient Name: " + patientName +
               "\nPatient ID: " + patientId +
               "\nDepartment: " + department +
               "\nAppointment Date: " + appointmentDate;
    }
}
