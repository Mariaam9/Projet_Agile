package tn.salles.reservation.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import tn.salles.reservation.model.*;
public interface EmployeRepository extends JpaRepository<Employe,Long> { boolean existsByEmailIgnoreCase(String email); }
