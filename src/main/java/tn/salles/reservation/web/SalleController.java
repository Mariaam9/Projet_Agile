package tn.salles.reservation.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import tn.salles.reservation.service.*;
import tn.salles.reservation.model.StatutSalle;
import tn.salles.reservation.repository.ResponsableRepository;
import tn.salles.reservation.web.form.SalleForm;

@Controller @RequestMapping("/salles")
public class SalleController {
 private final SalleService salles;private final ResponsableRepository responsables;
 public SalleController(SalleService salles,ResponsableRepository responsables){this.salles=salles;this.responsables=responsables;}
 @GetMapping String liste(Model model){model.addAttribute("salles",salles.toutes());return "salles/list";}
 @GetMapping("/nouvelle") String nouvelle(Model model){model.addAttribute("salleForm",new SalleForm());options(model);model.addAttribute("titre","Ajouter une salle");return "salles/form";}
 @GetMapping("/{id}/modifier") String modifier(@PathVariable Long id,Model model){model.addAttribute("salleForm",SalleForm.depuis(salles.parId(id)));model.addAttribute("id",id);options(model);model.addAttribute("titre","Modifier une salle");return "salles/form";}
 @PostMapping String enregistrer(@Valid @ModelAttribute("salleForm") SalleForm form,BindingResult errors,Model model){return save(null,form,errors,model);}
 @PostMapping("/{id}/modifier") String enregistrerModification(@PathVariable Long id,@Valid @ModelAttribute("salleForm") SalleForm form,BindingResult errors,Model model){return save(id,form,errors,model);}
 private String save(Long id,SalleForm form,BindingResult errors,Model model){
    if(!errors.hasErrors()){
     try {salles.enregistrer(id,form);return "redirect:/salles";}
     catch(RegleMetierException ex){errors.reject("regle",ex.getMessage());}
    }
    options(model);model.addAttribute("id",id);model.addAttribute("titre",id==null?"Ajouter une salle":"Modifier une salle");return "salles/form";
 }
 private void options(Model model){model.addAttribute("responsables",responsables.findAll());model.addAttribute("statuts",StatutSalle.values());}
}
