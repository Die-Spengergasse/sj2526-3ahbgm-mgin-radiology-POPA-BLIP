package at.spengergasse.spring_thymeleaf.entities;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "device")        //wie db tabelle  heißt
public class Device
{
    @Id
    private String id;
    private String type;
    private String location;

    public Device(String type, String location)
    {
        this.type = type;
        this.location = location;
    }

    public Device()
    {

    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

}
