package tn.salles.reservation.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.Valid;
import tn.salles.reservation.model.Employe;
import tn.salles.reservation.repository.EmployeRepository;

@Controller @RequestMapping("/employes")
public class EmployeController {
 private final EmployeRepository employes;
 public EmployeController(EmployeRepository employes){this.employes=employes;}
 @GetMapping String liste(Model model){model.addAttribute("employes",employes.findAll());model.addAttribute("employe",new Employe());return "employes/list";}
 @PostMapping String creer(@Valid @ModelAttribute("employe") Employe employe,BindingResult errors,Model model){
   if(employe.getEmail()!=null && employes.existsByEmailIgnoreCase(employe.getEmail()))
      errors.rejectValue("email","email.existe","Cette adresse e-mail existe déjà.");
   if(errors.hasErrors()){model.addAttribute("employes",employes.findAll());return "employes/list";}
   try{employes.save(employe);}
   catch(DataIntegrityViolationException e){errors.rejectValue("email","email.existe","Adresse e-mail déjà utilisée.");model.addAttribute("employes",employes.findAll());return "employes/list";}
   return "redirect:/employes";
 }
}
