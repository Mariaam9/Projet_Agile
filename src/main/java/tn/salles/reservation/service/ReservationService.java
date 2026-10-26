package tn.salles.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;
import tn.salles.reservation.web.form.ReservationForm;

@Service
public class ReservationService {
    private final ReservationRepository reservations;
    private final SalleRepository salles;
    private final EmployeRepository employes;
    public ReservationService(ReservationRepository reservations,SalleRepository salles,EmployeRepository employes){
        this.reservations=reservations;this.salles=salles;this.employes=employes;
    }
    @Transactional(readOnly=true)
    public List<Reservation> toutes(){return reservations.findAllByOrderByIdDesc();}
    @Transactional(readOnly=true)
    public Reservation parId(Long id){return reservations.findById(id).orElseThrow(()->new RessourceIntrouvableException("Réservation introuvable."));}

    /** Verrouiller la salle avant de chercher des conflits évite deux confirmations concurrentes. */
    @Transactional
    public Reservation creer(ReservationForm f){
        verifierPeriode(f.getDateDebut(),f.getDateFin());
        Salle salle=salles.verrouillerParId(f.getSalleId()).orElseThrow(()->new RessourceIntrouvableException("Salle introuvable."));
        verifierSalleEtConflits(salle,f,null);
        Employe employe=employes.findById(f.getEmployeId()).orElseThrow(()->new RessourceIntrouvableException("Employé introuvable."));
        Creneau creneau=new Creneau(salle,f.getDateDebut(),f.getDateFin());
        Reservation r=new Reservation(employe,creneau,f.getMotif().trim(),f.getNombreParticipants());
        r.confirmer(); // Le MVP confirme immédiatement si aucune réservation confirmée ne chevauche.
        return reservations.save(r);
    }
    @Transactional
    public Reservation modifier(Long id,ReservationForm f){
        verifierPeriode(f.getDateDebut(),f.getDateFin());
        Salle salle=salles.verrouillerParId(f.getSalleId()).orElseThrow(()->new RessourceIntrouvableException("Salle introuvable."));
        Reservation r=parId(id);
        if(!r.estModifiable()) throw new RegleMetierException("Une réservation annulée ne peut plus être modifiée.");
        if(!r.getEmploye().getId().equals(f.getEmployeId()))throw new RegleMetierException("Le titulaire de la réservation ne peut pas être changé.");
        verifierSalleEtConflits(salle,f,id);
        r.getCreneau().setSalle(salle);
        r.getCreneau().setDateDebut(f.getDateDebut());
        r.getCreneau().setDateFin(f.getDateFin());
        r.setMotif(f.getMotif().trim());r.setNombreParticipants(f.getNombreParticipants());
        return reservations.save(r);
    }
    @Transactional
    public void annuler(Long id){Reservation r=parId(id);if(r.getStatut()!=StatutReservation.ANNULEE)r.annuler();}
    private void verifierPeriode(LocalDateTime debut,LocalDateTime fin){
        if(debut==null||fin==null||!debut.isBefore(fin))throw new RegleMetierException("La date de début doit précéder la date de fin.");
    }
    private void verifierSalleEtConflits(Salle salle,ReservationForm f,Long exclude){
        if(!salle.estDisponible())throw new RegleMetierException("La salle est indisponible ou en maintenance.");
        if(f.getNombreParticipants()<1 || f.getNombreParticipants()>salle.getCapacite())
           throw new RegleMetierException("Le nombre de participants doit être compris entre 1 et "+salle.getCapacite()+".");
        long conflits=exclude==null
          ?reservations.compterConflits(salle.getId(),f.getDateDebut(),f.getDateFin(),StatutReservation.CONFIRMEE)
          :reservations.compterConflitsSansReservation(salle.getId(),f.getDateDebut(),f.getDateFin(),StatutReservation.CONFIRMEE,exclude);
        if(conflits>0)
           throw new RegleMetierException("Conflit : cette salle possède déjà une réservation confirmée pendant cette période.");
    }
}
