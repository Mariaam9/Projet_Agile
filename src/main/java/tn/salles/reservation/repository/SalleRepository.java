package tn.salles.reservation.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import tn.salles.reservation.model.*;
public interface SalleRepository extends JpaRepository<Salle,Long> { 
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Salle s where s.id = :id")
    Optional<Salle> verrouillerParId(@Param("id") Long id);
    List<Salle> findByStatutOrderByNomAsc(StatutSalle statut);
 }
