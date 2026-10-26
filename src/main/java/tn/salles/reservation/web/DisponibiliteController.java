package tn.salles.reservation.web;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import tn.salles.reservation.service.*;

@Controller
public class DisponibiliteController {
 private final DisponibiliteService service;
 public DisponibiliteController(DisponibiliteService service){this.service=service;}
 @GetMapping("/disponibilites") String rechercher(
       @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime debut,
       @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") LocalDateTime fin,
       @RequestParam(defaultValue="1") int capacite,Model model){
   model.addAttribute("debut",debut==null?LocalDate.now().plusDays(1).atTime(9,0):debut);
   model.addAttribute("fin",fin==null?LocalDate.now().plusDays(1).atTime(10,0):fin);
   model.addAttribute("capacite",capacite);
   if(debut!=null&&fin!=null){
      try{model.addAttribute("salles",service.rechercherSallesDisponibles(debut,fin,capacite));model.addAttribute("rechercheEffectuee",true);}
      catch(RegleMetierException e){model.addAttribute("erreur",e.getMessage());}
   }
   return "disponibilites/index";
 }
}
