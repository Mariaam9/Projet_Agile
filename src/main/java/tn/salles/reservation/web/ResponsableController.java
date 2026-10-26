package tn.salles.reservation.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotBlank;
import tn.salles.reservation.model.Responsable;
import tn.salles.reservation.repository.ResponsableRepository;

@Controller @RequestMapping("/responsables")
public class ResponsableController {
 private final ResponsableRepository responsables;
 public ResponsableController(ResponsableRepository responsables){this.responsables=responsables;}
 @GetMapping String liste(Model model){model.addAttribute("responsables",responsables.findAll());return "responsables/list";}
 @PostMapping String creer(@RequestParam String nom){
    if(!nom.isBlank())responsables.save(new Responsable(nom.trim()));
    return "redirect:/responsables";
 }
}
