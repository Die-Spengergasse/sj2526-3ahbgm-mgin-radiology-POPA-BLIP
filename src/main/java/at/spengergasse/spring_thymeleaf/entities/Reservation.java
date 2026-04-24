package at.spengergasse.spring_thymeleaf.entities;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")        //wie db heißen
public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Reservation()
    {

    }

    public Reservation(Patient patient, Device device, LocalDate date, String timeFrom, String timeTo, String bodyRegion, String comment) {
        this.patient = patient;
        this.device = device;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.bodyRegion = bodyRegion;
        this.comment = comment;
    }

    public Patient getPatient() {
        return patient;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }


    public void setPatient(Patient patient)
    {
        this.patient = patient;
    }
    private LocalDate date;
    private String timeFrom;
    private String timeTo;
    private String bodyRegion;
    private String comment;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTimeFrom() { return timeFrom; }
    public void setTimeFrom(String timeFrom) { this.timeFrom = timeFrom; }
    public String getTimeTo() { return timeTo; }
    public void setTimeTo(String timeTo) { this.timeTo = timeTo; }
    public String getBodyRegion() { return bodyRegion; }
    public void setBodyRegion(String bodyRegion) { this.bodyRegion = bodyRegion; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

}
