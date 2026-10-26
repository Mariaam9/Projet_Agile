package tn.salles.reservation.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendrierService {
    private final ReservationRepository reservations;
    public CalendrierService(ReservationRepository reservations){this.reservations=reservations;}
    @Transactional(readOnly=true) public List<Reservation> afficherCalendrier(LocalDateTime debut,LocalDateTime fin){
      return reservations.trouverSurPeriode(debut,fin,StatutReservation.CONFIRMEE);
    }
    @Transactional(readOnly=true) public Map<LocalDate,List<Reservation>> afficherMois(YearMonth mois){
        return afficherCalendrier(mois.atDay(1).atStartOfDay(),mois.plusMonths(1).atDay(1).atStartOfDay()).stream()
          .collect(Collectors.groupingBy(r->r.getCreneau().getDateDebut().toLocalDate(),TreeMap::new,Collectors.toList()));
    }
}
