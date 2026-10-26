package tn.salles.reservation.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tn.salles.reservation.model.StatutReservation;
import tn.salles.reservation.repository.*;

@Controller
public class HomeController {
 private final SalleRepository salles;private final EmployeRepository employes;private final ReservationRepository reservations;
 public HomeController(SalleRepository salles,EmployeRepository employes,ReservationRepository reservations){this.salles=salles;this.employes=employes;this.reservations=reservations;}
 @GetMapping("/") String accueil(Model model){
   model.addAttribute("nbSalles",salles.count());model.addAttribute("nbEmployes",employes.count());
   model.addAttribute("nbReservations",reservations.countByStatut(StatutReservation.CONFIRMEE));
   model.addAttribute("reservations",reservations.findAllByOrderByIdDesc().stream().limit(5).toList());
   return "redirect:/site/index.html";
 }
}
