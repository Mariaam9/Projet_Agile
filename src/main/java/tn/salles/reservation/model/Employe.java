package tn.salles.reservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity @Table(name = "employes", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Employe {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false) private String nom;
    @NotBlank @Email @Column(nullable=false, unique=true) private String email;
    public Employe() {}
    public Employe(String nom, String email) { this.nom=nom; this.email=email; }
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom=nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
}
