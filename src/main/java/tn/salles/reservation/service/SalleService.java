package tn.salles.reservation.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.salles.reservation.model.*;
import tn.salles.reservation.repository.*;
import tn.salles.reservation.web.form.SalleForm;
import java.util.List;

@Service
public class SalleService {
    private final SalleRepository salles; private final ResponsableRepository responsables;
    public SalleService(SalleRepository salles,ResponsableRepository responsables){this.salles=salles;this.responsables=responsables;}
    @Transactional(readOnly=true) public List<Salle> toutes(){return salles.findAll();}
    @Transactional(readOnly=true) public Salle parId(Long id){return salles.findById(id).orElseThrow(()->new RessourceIntrouvableException("Salle introuvable."));}
    @Transactional public Salle enregistrer(Long id,SalleForm form){
        Salle salle=id==null?new Salle():parId(id);
        salle.setNom(form.getNom().trim());salle.setCapacite(form.getCapacite());
        salle.setLocalisation(form.getLocalisation().trim());salle.setStatut(form.getStatut());
        salle.setResponsable(form.getResponsableId()==null?null:responsables.findById(form.getResponsableId())
            .orElseThrow(()->new RessourceIntrouvableException("Responsable introuvable.")));
        return salles.save(salle);
    }
}
