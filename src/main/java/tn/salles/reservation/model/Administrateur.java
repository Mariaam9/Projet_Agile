package tn.salles.reservation.model;

import jakarta.persistence.*;

/** Entité isolée, sans relation ORM, conformément au diagramme fourni. */
@Entity @Table(name="administrateurs")
public class Administrateur {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long idAdmin;
    @Column(nullable=false) private String nom;
    @Column(nullable=false, unique=true) private String email;
    public Administrateur() {}
    public Administrateur(String nom,String email) { this.nom=nom; this.email=email; }
    public Long getIdAdmin(){return idAdmin;}
    public String getNom(){return nom;}
    public void setNom(String nom){this.nom=nom;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
}
