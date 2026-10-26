package tn.salles.reservation.web.api;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import tn.salles.reservation.service.RegleMetierException;
import tn.salles.reservation.service.RessourceIntrouvableException;
import java.util.Map;

@RestControllerAdvice(basePackages="tn.salles.reservation.web.api")
public class ApiErrorHandler {
 @ExceptionHandler(RessourceIntrouvableException.class)
 ResponseEntity<Map<String,String>> manquant(RessourceIntrouvableException e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erreur",e.getMessage()));}
 @ExceptionHandler(RegleMetierException.class)
 ResponseEntity<Map<String,String>> metier(RegleMetierException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erreur",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<Map<String,String>> invalide(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("erreur","Données invalides : vérifier dates, salle, employé, motif et participants."));}
 @ExceptionHandler(HttpMessageNotReadableException.class)
 ResponseEntity<Map<String,String>> json(HttpMessageNotReadableException e){return ResponseEntity.badRequest().body(Map.of("erreur","JSON invalide ou date mal formatée."));}
}
