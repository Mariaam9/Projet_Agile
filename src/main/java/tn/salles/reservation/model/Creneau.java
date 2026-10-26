package tn.salles.reservation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Créneau (dates + salle). Chaque réservation possède son créneau associé. */
@Entity @Table(name="creneaux")
public class Creneau {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private LocalDateTime dateDebut;
    @Column(nullable=false) private LocalDateTime dateFin;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="salle_id",nullable=false) private Salle salle;
    public Creneau() {}
    public Creneau(Salle salle,LocalDateTime debut,LocalDateTime fin) {this.salle=salle;this.dateDebut=debut;this.dateFin=fin;}
    public boolean estValide(){return dateDebut!=null && dateFin!=null && dateDebut.isBefore(dateFin);}
    public boolean chevauche(Creneau autre){return this.dateDebut.isBefore(autre.dateFin)&&autre.dateDebut.isBefore(this.dateFin);}
    public Long getId(){return id;}
    public LocalDateTime getDateDebut(){return dateDebut;}
    public void setDateDebut(LocalDateTime d){dateDebut=d;}
    public LocalDateTime getDateFin(){return dateFin;}
    public void setDateFin(LocalDateTime f){dateFin=f;}
    public Salle getSalle(){return salle;}
    public void setSalle(Salle salle){this.salle=salle;}
}
