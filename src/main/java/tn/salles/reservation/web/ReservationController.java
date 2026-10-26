package tn.salles.reservation.web;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import tn.salles.reservation.repository.*;
import tn.salles.reservation.service.*;
import tn.salles.reservation.web.form.ReservationForm;

@Controller @RequestMapping("/reservations")
public class ReservationController {
 private final ReservationService service;private final EmployeRepository employes;private final SalleRepository salles;
 public ReservationController(ReservationService service,EmployeRepository employes,SalleRepository salles){this.service=service;this.employes=employes;this.salles=salles;}
 @GetMapping String liste(Model model){model.addAttribute("reservations",service.toutes());return "reservations/list";}
 @GetMapping("/nouvelle") String nouvelle(@RequestParam(required=false) Long salleId,Model model){
    ReservationForm f=new ReservationForm();f.setSalleId(salleId);
    f.setDateDebut(LocalDate.now().plusDays(1).atTime(9,0));f.setDateFin(LocalDate.now().plusDays(1).atTime(10,0));
    model.addAttribute("reservationForm",f);remplir(model,null);return "reservations/form";
 }
 @GetMapping("/{id}/modifier") String modifier(@PathVariable Long id,Model model){
    model.addAttribute("reservationForm",ReservationForm.depuis(service.parId(id)));remplir(model,id);return "reservations/form";
 }
 @PostMapping String creer(@Valid @ModelAttribute("reservationForm") ReservationForm form,BindingResult errors,Model model,RedirectAttributes redirect){
    return sauver(null,form,errors,model,redirect);
 }
 @PostMapping("/{id}/modifier") String modifierPost(@PathVariable Long id,@Valid @ModelAttribute("reservationForm") ReservationForm form,BindingResult errors,Model model,RedirectAttributes redirect){
    return sauver(id,form,errors,model,redirect);
 }
 private String sauver(Long id,ReservationForm f,BindingResult errors,Model model,RedirectAttributes redirect){
   if(!errors.hasErrors()){
      try {
       if(id==null)service.creer(f);else service.modifier(id,f);
       redirect.addFlashAttribute("succes",id==null?"Réservation créée et confirmée.":"Réservation modifiée.");
       return "redirect:/reservations";
      }catch(RegleMetierException ex){errors.reject("regle",ex.getMessage());}
   }
   remplir(model,id);return "reservations/form";
 }
 @PostMapping("/{id}/annuler") String annuler(@PathVariable Long id,RedirectAttributes redirect){
    try{service.annuler(id);redirect.addFlashAttribute("succes","Réservation annulée.");}
    catch(RegleMetierException ex){redirect.addFlashAttribute("erreur",ex.getMessage());}
    return "redirect:/reservations";
 }
 private void remplir(Model model,Long id){model.addAttribute("id",id);model.addAttribute("titre",id==null?"Nouvelle réservation":"Modifier la réservation");model.addAttribute("salles",salles.findAll());model.addAttribute("employes",employes.findAll());}
}
