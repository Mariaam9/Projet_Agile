package tn.salles.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;

@Service
public class StatistiqueService {
    private final SalleRepository salles; private final ReservationRepository reservations;
    public StatistiqueService(SalleRepository salles,ReservationRepository reservations){this.salles=salles;this.reservations=reservations;}
    @Transactional(readOnly=true)
    public List<StatistiqueUtilisation> calculerStatistiques(LocalDateTime debut,LocalDateTime fin){
        if(debut==null||fin==null||!debut.isBefore(fin))throw new RegleMetierException("Période invalide.");
        List<StatistiqueUtilisation> rapports=new ArrayList<>();
        for(Salle salle:salles.findAll())rapports.add(calculerPourSalle(salle,debut,fin));
        return rapports;
    }
    private StatistiqueUtilisation calculerPourSalle(Salle salle,LocalDateTime debut,LocalDateTime fin){
        List<Reservation> liste=reservations.trouverSallePeriode(salle.getId(),debut,fin,StatutReservation.CONFIRMEE);
        // Fusionner les plages temporelles pour ne jamais compter deux fois le même temps.
        LocalDateTime depuis=null,jusqua=null; long minutes=0;
        for(Reservation r:liste){
            LocalDateTime a=r.getCreneau().getDateDebut().isBefore(debut)?debut:r.getCreneau().getDateDebut();
            LocalDateTime b=r.getCreneau().getDateFin().isAfter(fin)?fin:r.getCreneau().getDateFin();
            if(depuis==null){depuis=a;jusqua=b;}
            else if(!a.isAfter(jusqua)){if(b.isAfter(jusqua))jusqua=b;}
            else {minutes+=Duration.between(depuis,jusqua).toMinutes();depuis=a;jusqua=b;}
        }
        if(depuis!=null)minutes+=Duration.between(depuis,jusqua).toMinutes();
        long total=Duration.between(debut,fin).toMinutes();
        double taux=total<=0?0:Math.round((10000.0*minutes/total))/100.0;
        return new StatistiqueUtilisation(salle,debut,fin,liste.size(),Math.min(100,taux));
    }
}
