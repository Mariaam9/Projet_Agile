package tn.salles.reservation.web.form;

import jakarta.validation.constraints.*;
import tn.salles.reservation.model.*;

public class SalleForm {
    @NotBlank private String nom;
    @Min(1) private int capacite=2;
    @NotBlank private String localisation;
    @NotNull private StatutSalle statut=StatutSalle.DISPONIBLE;
    private Long responsableId;
    public static SalleForm depuis(Salle s){SalleForm f=new SalleForm();f.nom=s.getNom();f.capacite=s.getCapacite();f.localisation=s.getLocalisation();f.statut=s.getStatut();f.responsableId=s.getResponsable()==null?null:s.getResponsable().getId();return f;}
    public String getNom(){return nom;}
    public void setNom(String s){nom=s;}
    public int getCapacite(){return capacite;}
    public void setCapacite(int c){capacite=c;}
    public String getLocalisation(){return localisation;}
    public void setLocalisation(String l){localisation=l;}
    public StatutSalle getStatut(){return statut;}
    public void setStatut(StatutSalle s){statut=s;}
    public Long getResponsableId(){return responsableId;}
    public void setResponsableId(Long id){responsableId=id;}
}
