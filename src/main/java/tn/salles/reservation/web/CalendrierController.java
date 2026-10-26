package tn.salles.reservation.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import tn.salles.reservation.service.CalendrierService;

@Controller
public class CalendrierController {
 private final CalendrierService service;
 public CalendrierController(CalendrierService service){this.service=service;}
 @GetMapping("/calendrier") String calendrier(@RequestParam(required=false) String mois,Model model){
    YearMonth ym;
    try{ym=mois==null?YearMonth.now():YearMonth.parse(mois);}catch(Exception e){ym=YearMonth.now();}
    model.addAttribute("mois",ym.toString());model.addAttribute("moisPrecedent",ym.minusMonths(1).toString());
    model.addAttribute("moisSuivant",ym.plusMonths(1).toString());model.addAttribute("jours",service.afficherMois(ym));
    return "calendrier/index";
 }
}
