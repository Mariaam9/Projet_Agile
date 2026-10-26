package tn.salles.reservation.model;

import jakarta.persistence.*;

@Entity @Table(name="responsables")
public class Responsable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String nom;
    public Responsable() {}
    public Responsable(String nom) { this.nom=nom; }
    public Long getId(){return id;}
    public String getNom(){return nom;}
    public void setNom(String nom){this.nom=nom;}
}
