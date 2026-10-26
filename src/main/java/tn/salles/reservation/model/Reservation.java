package tn.salles.reservation.model;

import jakarta.persistence.*;

/** Classe d'association UML Employé <-> Créneau matérialisée en JPA par deux références.
 * Une réservation dispose de sa propre identité et de son cycle de vie. */
@Entity @Table(name="reservations")
public class Reservation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="employe_id",nullable=false) private Employe employe;
    @OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL,orphanRemoval=true,optional=false)
    @JoinColumn(name="creneau_id",nullable=false,unique=true) private Creneau creneau;
    @Column(nullable=false,length=300) private String motif;
    @Column(nullable=false) private int nombreParticipants=1;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private StatutReservation statut=StatutReservation.EN_ATTENTE;
    @Version private Long version;
    public Reservation(){}
    public Reservation(Employe employe,Creneau creneau,String motif,int participants){
      this.employe=employe;this.creneau=creneau;this.motif=motif;this.nombreParticipants=participants;
    }
    public void confirmer(){if(statut==StatutReservation.ANNULEE)throw new IllegalStateException("Réservation annulée"); statut=StatutReservation.CONFIRMEE;}
    public void annuler(){if(statut==StatutReservation.ANNULEE)throw new IllegalStateException("Déjà annulée"); statut=StatutReservation.ANNULEE;}
    public boolean estModifiable(){return statut!=StatutReservation.ANNULEE;}
    public Long getId(){return id;}
    public Employe getEmploye(){return employe;}
    public void setEmploye(Employe e){employe=e;}
    public Creneau getCreneau(){return creneau;}
    public void setCreneau(Creneau c){creneau=c;}
    public String getMotif(){return motif;}
    public void setMotif(String motif){this.motif=motif;}
    public int getNombreParticipants(){return nombreParticipants;}
    public void setNombreParticipants(int n){nombreParticipants=n;}
    public StatutReservation getStatut(){return statut;}
    public void setStatut(StatutReservation s){statut=s;}
}
