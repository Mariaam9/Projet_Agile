package tn.salles.reservation.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;

@Service
public class DisponibiliteService {
    private final SalleRepository salles; private final ReservationRepository reservations;
    public DisponibiliteService(SalleRepository salles,ReservationRepository reservations){this.salles=salles;this.reservations=reservations;}
    @Transactional(readOnly=true)
    public boolean verifierDisponibilite(Long salleId,LocalDateTime debut,LocalDateTime fin){
        if(debut==null||fin==null||!debut.isBefore(fin))return false;
        return salles.findById(salleId).filter(Salle::estDisponible).isPresent() &&
            reservations.compterConflits(salleId,debut,fin,StatutReservation.CONFIRMEE)==0;
    }
    @Transactional(readOnly=true)
    public List<Salle> rechercherSallesDisponibles(LocalDateTime debut,LocalDateTime fin,int capacite){
        if(debut==null||fin==null||!debut.isBefore(fin))throw new RegleMetierException("Période invalide.");
        if(capacite<1)throw new RegleMetierException("Capacité invalide.");
        return salles.findByStatutOrderByNomAsc(StatutSalle.DISPONIBLE).stream()
         .filter(s->s.getCapacite()>=capacite && reservations.compterConflits(s.getId(),debut,fin,StatutReservation.CONFIRMEE)==0)
         .toList();
    }
}
