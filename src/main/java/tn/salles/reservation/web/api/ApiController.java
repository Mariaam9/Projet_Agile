package tn.salles.reservation.web.api;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import tn.salles.reservation.model.*;
import tn.salles.reservation.service.*;
import tn.salles.reservation.web.form.ReservationForm;

@RestController @RequestMapping("/api/v1")
public class ApiController {
 private final SalleService salles;private final ReservationService reservations;private final DisponibiliteService disponibilite;
 public ApiController(SalleService salles,ReservationService reservations,DisponibiliteService disponibilite){this.salles=salles;this.reservations=reservations;this.disponibilite=disponibilite;}
 public record SalleDto(Long id,String nom,int capacite,String localisation,StatutSalle statut){}
 public record ReservationDto(Long id,Long employeId,Long salleId,String employe,String salle,LocalDateTime dateDebut,LocalDateTime dateFin,String motif,int participants,StatutReservation statut){}
 public static SalleDto salleDto(Salle s){return new SalleDto(s.getId(),s.getNom(),s.getCapacite(),s.getLocalisation(),s.getStatut());}
 public static ReservationDto reservationDto(Reservation r){return new ReservationDto(r.getId(),r.getEmploye().getId(),r.getCreneau().getSalle().getId(),r.getEmploye().getNom(),r.getCreneau().getSalle().getNom(),r.getCreneau().getDateDebut(),r.getCreneau().getDateFin(),r.getMotif(),r.getNombreParticipants(),r.getStatut());}
 @GetMapping("/salles") List<SalleDto> salles(){return salles.toutes().stream().map(ApiController::salleDto).toList();}
 @GetMapping("/reservations") List<ReservationDto> reservations(){return reservations.toutes().stream().map(ApiController::reservationDto).toList();}
 @GetMapping("/disponibilites") List<SalleDto> disponibilites(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,@RequestParam(defaultValue="1")int capacite){
    return disponibilite.rechercherSallesDisponibles(debut,fin,capacite).stream().map(ApiController::salleDto).toList();
 }
 @PostMapping("/reservations") ResponseEntity<ReservationDto> reserver(@Valid @RequestBody ReservationForm form){
   Reservation r=reservations.creer(form);return ResponseEntity.created(URI.create("/api/v1/reservations/"+r.getId())).body(reservationDto(r));
 }
 @GetMapping("/reservations/{id}") ReservationDto trouver(@PathVariable Long id){return reservationDto(reservations.parId(id));}
 @PutMapping("/reservations/{id}") ReservationDto modifier(@PathVariable Long id,@Valid @RequestBody ReservationForm form){return reservationDto(reservations.modifier(id,form));}
 @DeleteMapping("/reservations/{id}") ResponseEntity<Void> annuler(@PathVariable Long id){reservations.annuler(id);return ResponseEntity.noContent().build();}
}
