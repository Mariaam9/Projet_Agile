# Salleo — Projet 4 : Gestionnaire de réservation de salles

> Réalisation du design fourni par l’utilisateur (« Site moderne avec authentification.zip »),
> en remplaçant **orange → bleu** (#2563eb) et **vert → gris** (#64748b et #e2e8f0).

## Accès et lancement Windows

1. Décompressez cette archive.
2. Ouvrez le dossier **Salleo_Bleu_Gris_SpringBoot** dans IntelliJ IDEA ou Eclipse (projet Maven).
3. Installez **JDK 21** et **Maven** si nécessaire.
4. Depuis le dossier avec `pom.xml`, lancez :

```cmd
mvn spring-boot:run
```

5. Ouvrez **http://localhost:8080** : vous serez redirigé(e) vers **/site/index.html**.

L'API Spring Boot (`/api/v1/salles`, `/api/v1/reservations`, etc.) est appelée automatiquement.
Par défaut : **H2** en mémoire et données de démonstration. Vous pourrez basculer vers MySQL avec
le profil `mysql` selon les propriétés du dossier `src/main/resources` et `README_BACKEND_ORIGINAL.md`.

## Comptes de démonstration de l'interface

| Email | Mot de passe affichage | Rôle de démonstration |
|---|---|---|
| sami@example.com | demo123 | Employé |
| amira@example.com | demo123 | Employée |
| responsable@example.com | demo123 | Responsable |

**⚠️ Sécurité : la page de connexion est uniquement une maquette interactive côté navigateur.**
Elle **ne protège pas** les routes Spring Boot ni l'API et **ne remplace pas Spring Security**.
À sécuriser avant toute mise en production : authentification serveur, hachage des mots de passe,
autorisations par rôle, contrôle de propriété des réservations, protection CSRF, validations.

## Ce qui fonctionne

- Tableau de bord avec nombres calculés (à partir du modèle ou de l'API).
- Cartes de salles avec capacité, état et bouton de réservation.
- Formulaire de réservation : motif, salle, participants, début, fin.
- Création, modification, annulation et affichage calendrier.
- Contrôles de période, capacité et chevauchement. En mode API, ils sont également vérifiés par Spring Boot.
- Calcul simple du taux d'occupation et statistiques.
- Filtre de recherche des cartes/lignes.
- Navigation responsive (mobile + PC).
- Un mode démo local si l'API n'est pas accessible, avec stockage navigateur (si autorisé).

## Structure des dossiers

- `src/main/resources/static/site/` : site Salleo bleu et gris, HTML/CSS/JavaScript sans build npm.
- `src/main/java/tn/salles/reservation/model/` : entités du diagramme UML (Employe, Salle, Creneau, Reservation, Responsable, Administrateur, StatistiqueUtilisation).
- `src/main/java/tn/salles/reservation/service/` : logique métier, anti-chevauchement, statistiques.
- `src/main/java/tn/salles/reservation/web/api/` : endpoints REST consommés par Salleo.
- `src/main/resources/templates/` : anciennes pages de gestion Thymeleaf toujours accessibles (les salles, disponibilités, rapports...).
- `docs/` : diagrammes UML fournis dans le projet précédent.

## Ouvrir le site sans Java

Le sous-dossier `src/main/resources/static/site` est utilisable comme site statique indépendant.
Pour voir le rendu hors ligne, lancez un petit serveur local si Python est installé :

```cmd
cd src\main\resources\static\site
python -m http.server 5500
```

Puis ouvrez http://localhost:5500. Sans le backend, les données sont celles de la démo locale.

## Exigences et limites

- Le schéma métier reste compatible : un `Creneau` appartient à une `Salle`; une `Reservation` associe un `Employe` et un `Creneau`.
- L'`Administrateur` métier est indépendant des autres entités.
- L'API REST ajoutée renvoie les IDs de salle et d'employé pour modifier une réservation correctement.
- Pas de vraie authentification serveur pour cette maquette, pas de déploiement public conseillé.
- Le navigateur permet une vérification de l'interface, mais la **compilation Maven/les tests Java n'ont pas été exécutés dans cet environnement**.

---
Pour le projet Maven et les tests JUnit d'origine, consultez `README_BACKEND_ORIGINAL.md`.
