package tn.salles.reservation.web;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import tn.salles.reservation.service.*;

@Controller
public class StatistiqueController {
 private final StatistiqueService service;
 public StatistiqueController(StatistiqueService service){this.service=service;}
 @GetMapping("/statistiques") String stats(
   @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate debut,
   @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fin,Model model){
    LocalDate d=debut==null?LocalDate.now().withDayOfMonth(1):debut;
    LocalDate f=fin==null?d.plusMonths(1):fin;
    model.addAttribute("debut",d);model.addAttribute("fin",f);
    try{model.addAttribute("rapports",service.calculerStatistiques(d.atStartOfDay(),f.atStartOfDay()));}
    catch(RegleMetierException e){model.addAttribute("erreur",e.getMessage());}
    return "statistiques/index";
 }
}
