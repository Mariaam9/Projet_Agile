package tn.salles.reservation;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;
import tn.salles.reservation.service.*;
import tn.salles.reservation.web.form.ReservationForm;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @ActiveProfiles("test") @Transactional
class ReservationServiceTest {
 @Autowired ReservationService service;
 @Autowired DisponibiliteService disponibilite;
 @Autowired StatistiqueService statistiques;
 @Autowired EmployeRepository employes;
 @Autowired SalleRepository salles;
 Long salleId, employeId;
 LocalDateTime debut,fin;
 @BeforeEach void init(){
   Salle salle=salles.save(new Salle("Salle test",5,"Bureau 1",StatutSalle.DISPONIBLE,null));
   Employe employe=employes.save(new Employe("Test User","test"+System.nanoTime()+"@example.org"));
   salleId=salle.getId();employeId=employe.getId();
   debut=LocalDate.now().plusDays(3).atTime(10,0);fin=debut.plusHours(2);
 }
 private ReservationForm form(LocalDateTime d,LocalDateTime f,int participants){
    ReservationForm form=new ReservationForm();form.setSalleId(salleId);form.setEmployeId(employeId);
    form.setDateDebut(d);form.setDateFin(f);form.setMotif("Réunion test");form.setNombreParticipants(participants);return form;
 }
 @Test void creationSansConflitConfirmeLaReservation(){
    Reservation r=service.creer(form(debut,fin,3));
    assertNotNull(r.getId());assertEquals(StatutReservation.CONFIRMEE,r.getStatut());
    assertFalse(disponibilite.verifierDisponibilite(salleId,debut.plusMinutes(30),fin));
 }
 @Test void deuxReservationsQuiSeChevauchentSontRefusees(){
    service.creer(form(debut,fin,3));
    assertThrows(RegleMetierException.class,()->service.creer(form(debut.plusHours(1),fin.plusHours(1),2)));
 }
 @Test void reservationsContiguesSontPermises(){
    service.creer(form(debut,fin,2));
    assertDoesNotThrow(()->service.creer(form(fin,fin.plusHours(1),2)));
 }
 @Test void annulationLibereLaSalle(){
    Reservation r=service.creer(form(debut,fin,2));service.annuler(r.getId());
    assertTrue(disponibilite.verifierDisponibilite(salleId,debut,fin));
    assertDoesNotThrow(()->service.creer(form(debut,fin,2)));
 }
 @Test void capaciteEstRespectee(){
    assertThrows(RegleMetierException.class,()->service.creer(form(debut,fin,6)));
 }
 @Test void datesInvalidesSontRefusees(){
    assertThrows(RegleMetierException.class,()->service.creer(form(fin,debut,2)));
 }
 @Test void modifierIgnoreSonPropreCreneauMaisRefuseLeConflit(){
    Reservation r=service.creer(form(debut,fin,2));
    assertDoesNotThrow(()->service.modifier(r.getId(),form(debut,fin.plusMinutes(30),3)));
    assertEquals(3,service.parId(r.getId()).getNombreParticipants());
 }
 @Test void lesStatistiquesCalculentDeuxHeuresSurVingtQuatre(){
    service.creer(form(debut,fin,2));
    List<StatistiqueUtilisation> rapports=statistiques.calculerStatistiques(debut.toLocalDate().atStartOfDay(),debut.toLocalDate().plusDays(1).atStartOfDay());
    StatistiqueUtilisation resultat=rapports.stream().filter(s->s.salle().getId().equals(salleId)).findFirst().orElseThrow();
    assertEquals(1,resultat.nombreReservations());assertEquals(8.33,resultat.tauxOccupation(),0.01);
 }
}
