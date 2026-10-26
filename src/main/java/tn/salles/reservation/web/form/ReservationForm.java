package tn.salles.reservation.web.form;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import tn.salles.reservation.model.Reservation;
import java.time.LocalDateTime;

public class ReservationForm {
    @NotNull private Long employeId;
    @NotNull private Long salleId;
    @NotNull @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") private LocalDateTime dateDebut;
    @NotNull @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") private LocalDateTime dateFin;
    @NotBlank @Size(max=300) private String motif;
    @Min(1) private int nombreParticipants=1;
    public static ReservationForm depuis(Reservation r) {
        ReservationForm f=new ReservationForm();f.employeId=r.getEmploye().getId();
        f.salleId=r.getCreneau().getSalle().getId();f.dateDebut=r.getCreneau().getDateDebut();
        f.dateFin=r.getCreneau().getDateFin();f.motif=r.getMotif();f.nombreParticipants=r.getNombreParticipants();return f;
    }
    public Long getEmployeId(){return employeId;}
    public void setEmployeId(Long n){employeId=n;}
    public Long getSalleId(){return salleId;}
    public void setSalleId(Long n){salleId=n;}
    public LocalDateTime getDateDebut(){return dateDebut;}
    public void setDateDebut(LocalDateTime d){dateDebut=d;}
    public LocalDateTime getDateFin(){return dateFin;}
    public void setDateFin(LocalDateTime d){dateFin=d;}
    public String getMotif(){return motif;}
    public void setMotif(String n){motif=n;}
    public int getNombreParticipants(){return nombreParticipants;}
    public void setNombreParticipants(int n){nombreParticipants=n;}
}
