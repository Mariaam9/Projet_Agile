package tn.salles.reservation.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;
import java.time.*;

@Configuration @Profile("dev")
public class DonneesDemo {
 @Bean CommandLineRunner initialiser(EmployeRepository employes,ResponsableRepository responsables,
          SalleRepository salles,ReservationRepository reservations,AdministrateurRepository administrateurs){
   return args->{
     if(salles.count()>0)return;
     Responsable r=responsables.save(new Responsable("Nadia Mansour"));
     salles.save(new Salle("Atlas",12,"Bâtiment A · étage 1",StatutSalle.DISPONIBLE,r));
     salles.save(new Salle("Carthage",6,"Bâtiment B · étage 2",StatutSalle.DISPONIBLE,r));
     salles.save(new Salle("Sfax",20,"Bâtiment C · RDC",StatutSalle.DISPONIBLE,null));
     salles.save(new Salle("Studio",4,"Bâtiment A · étage 2",StatutSalle.MAINTENANCE,r));
     Employe sami=employes.save(new Employe("Sami Trabelsi","sami@example.com"));
     Employe amira=employes.save(new Employe("Amira Ben Salah","amira@example.com"));
     employes.save(new Employe("Youssef Haddad","youssef@example.com"));
     employes.save(new Employe("Nadia Mansour","responsable@example.com"));
     administrateurs.save(new Administrateur("Administration","admin@example.com"));
     LocalDate demain=LocalDate.now().plusDays(1);
     Reservation premiere=new Reservation(sami,new Creneau(salles.findAll().get(0),demain.atTime(9,0),demain.atTime(10,30)),"Réunion équipe produit",6);
     premiere.confirmer();reservations.save(premiere);
     Reservation deuxieme=new Reservation(amira,new Creneau(salles.findAll().get(1),demain.atTime(14,0),demain.atTime(15,0)),"Point d'avancement",4);
     deuxieme.confirmer();reservations.save(deuxieme);
   };
 }
}
