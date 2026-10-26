package tn.salles.reservation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import tn.salles.reservation.model.*;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("""
      select count(r) from Reservation r
      where r.creneau.salle.id = :salleId and r.statut = :statut
      and r.creneau.dateDebut < :fin and r.creneau.dateFin > :debut
      """)
    long compterConflits(@Param("salleId") Long salleId,@Param("debut") LocalDateTime debut,
                         @Param("fin") LocalDateTime fin,@Param("statut") StatutReservation statut);
    @Query("""
      select count(r) from Reservation r
      where r.creneau.salle.id = :salleId and r.statut = :statut
      and r.creneau.dateDebut < :fin and r.creneau.dateFin > :debut
      and r.id <> :exclureId
      """)
    long compterConflitsSansReservation(@Param("salleId") Long salleId,@Param("debut") LocalDateTime debut,
                         @Param("fin") LocalDateTime fin,@Param("statut") StatutReservation statut,
                         @Param("exclureId") Long exclureId);
    @Query("""
      select r from Reservation r where r.statut = :statut
      and r.creneau.dateDebut < :fin and r.creneau.dateFin > :debut
      order by r.creneau.dateDebut asc
      """)
    List<Reservation> trouverSurPeriode(@Param("debut") LocalDateTime debut,@Param("fin") LocalDateTime fin,
                                        @Param("statut") StatutReservation statut);
    @Query("""
      select r from Reservation r where r.creneau.salle.id = :salleId and r.statut = :statut
      and r.creneau.dateDebut < :fin and r.creneau.dateFin > :debut
      order by r.creneau.dateDebut asc
      """)
    List<Reservation> trouverSallePeriode(@Param("salleId") Long salleId,@Param("debut") LocalDateTime debut,
                                          @Param("fin") LocalDateTime fin,@Param("statut") StatutReservation statut);
    List<Reservation> findAllByOrderByIdDesc();
    long countByStatut(StatutReservation statut);
}
