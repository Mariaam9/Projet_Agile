# Projet 4 — Gestionnaire de réservation de salles

Application web complète de démonstration, réalisée avec **Java 21 + Spring Boot 3.3.5 + Thymeleaf + Bootstrap 5 + Spring Data JPA**.

## 1. Démarrer facilement (sans MySQL)

**Prérequis** : Java JDK 21 et Maven (commande `mvn` reconnue). Compatible Windows / IntelliJ IDEA / Eclipse / VS Code.

1. Dézipper le dossier et l'ouvrir dans IntelliJ avec **File → Open → sélectionner `pom.xml`** (ou importer comme projet Maven dans Eclipse).
2. Dans un terminal, se placer dans le dossier contenant `pom.xml`.
3. Exécuter :

   ```bat
   mvn clean test
   mvn spring-boot:run
   ```

4. Ouvrir **http://localhost:8080**.

Par défaut le profil **dev** utilise une base **H2 en mémoire** et charge 4 salles, 3 employés, un responsable et 2 réservations de démonstration. Les données H2 sont réinitialisées à chaque redémarrage.

Si le port 8080 est occupé, exécuter `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090`.

### Pages disponibles

- `/` : accueil, indicateurs, réservations récentes
- `/salles` : consulter, créer et modifier une salle (état et responsable inclus)
- `/employes` : liste et ajout d'employés
- `/responsables` : gestion des responsables
- `/reservations` : création, modification, annulation
- `/disponibilites` : recherche selon les dates et la capacité
- `/calendrier` : agenda mensuel par date de début
- `/statistiques` : nombre de réservations et taux d'occupation par salle
- `/swagger-ui.html` : tester l'API REST avec Swagger UI
- `/h2-console` : interface H2 de démonstration

Connexion à H2 : **JDBC URL** `jdbc:h2:mem:reservations` ; **User Name** `sa` ; **Password** vide. Le serveur doit être lancé.

## 2. Utiliser MySQL (données conservées)

Installer MySQL et créer la base :

```sql
CREATE DATABASE reservations_salles CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Démarrer avec le profil MySQL :

```bat
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Configuration : `src/main/resources/application-mysql.properties`.
La valeur par défaut est `root` avec mot de passe vide. Pour une base protégée, définir la variable d'environnement `DB_PASSWORD`, ainsi que `DB_USER` et `DB_URL` si besoin.
**Le profil mysql ne charge pas les données de démonstration** : ajouter d'abord un responsable (facultatif), des salles et des employés depuis l'interface.

## 3. UML → classes du projet

| Diagramme | Classe Java / responsabilité |
|---|---|
| Employé | `model/Employe.java` et page `/employes` |
| Responsable | `model/Responsable.java` ; plusieurs salles possibles, responsable de salle optionnel |
| Administrateur | `model/Administrateur.java` : classe **isolée**, aucune relation vers les autres entités |
| Salle | `model/Salle.java` : capacité, localisation, statut et responsable |
| Créneau | `model/Creneau.java` : salle, début, fin ; `estValide()` et `chevauche()` |
| Réservation (classe d'association) | `model/Reservation.java` : porte les références `employe` + `creneau`, le motif, le statut et les participants |
| StatistiqueUtilisation | `model/StatistiqueUtilisation.java` : **record calculé**, sans table SQL |

**Note UML/JPA :** La classe d'association `Reservation` entre Employé et Créneau est représentée en pratique par deux références JPA. Chaque réservation possède son propre créneau et sa propre identité. Cela autorise un employé à réserver plusieurs fois la même salle à des dates différentes. Les navigations et les opérations de consultation passent par les services et contrôleurs (OOD) plutôt que d'ajouter des dépendances infrastructure aux entités (OOA).

Les opérations métier sont dans `service/` :

- `ReservationService` : réserver, modifier, annuler, valider la capacité et détecter les conflits.
- `DisponibiliteService` : disponibilité et recherche des salles.
- `CalendrierService` : consulter la vue mensuelle.
- `StatistiqueService` : compter et calculer les taux d'utilisation.
- `SalleService` : gérer les salles.

Les données se trouvent dans les interfaces `repository/` de Spring Data JPA.

## 4. Règles métier validées

1. `dateDebut < dateFin`.
2. Capacité de salle strictement positive ; participants ≥ 1 et ≤ capacité.
3. Aucune **réservation confirmée** ne peut chevaucher une autre réservation confirmée dans la même salle. Une réservation de 9 h à 10 h et une autre de 10 h à 11 h sont autorisées.
4. Les salles `INDISPONIBLE` et `MAINTENANCE` ne sont pas réservables.
5. L'annulation conserve l'historique mais libère le créneau pour une autre réservation.
6. Une réservation annulée ne peut plus être modifiée.
7. Verrouillage pessimiste de la ligne `Salle` pendant la création et la modification pour réduire les collisions concurrentes.
8. Le taux d'occupation est basé sur la durée cumulée des réservations confirmées sur la période, **24 h / jour** ; il n'utilise pas d'horaires d'ouverture. Les intervalles sont fusionnés et tronqués à la période de calcul.

## 5. Swagger / API REST

Après lancement, ouvrir **http://localhost:8080/swagger-ui.html**.

| Méthode | Route | Résultat |
|---|---|---|
| GET | `/api/v1/salles` | liste des salles |
| GET | `/api/v1/reservations` | liste des réservations |
| GET | `/api/v1/reservations/{id}` | détail |
| GET | `/api/v1/disponibilites?debut=2026-11-02T09:00:00&fin=2026-11-02T10:00:00&capacite=3` | salles libres |
| POST | `/api/v1/reservations` | création (201) |
| PUT | `/api/v1/reservations/{id}` | modification |
| DELETE | `/api/v1/reservations/{id}` | annulation logique (204) |

Corps JSON exemple (IDs à adapter aux données H2) :

```json
{
  "employeId": 1,
  "salleId": 1,
  "dateDebut": "2026-11-02T09:00:00",
  "dateFin": "2026-11-02T10:00:00",
  "motif": "Réunion d'équipe",
  "nombreParticipants": 4
}
```

**Statuts** : 201 Created pour une création, 400 Bad Request pour les champs invalides, 404 Not Found pour un identifiant inexistant, 409 Conflict pour une règle métier non respectée. Une réservation est créée directement avec le statut `CONFIRMEE` si tous les contrôles réussissent (workflow MVP).

## 6. Tests

Exécuter `mvn test`. Les tests comprennent : création, conflit temporel, créneaux adjacents, annulation, capacité, dates invalides, modification, statistiques et réponses de base de l'API.

## 7. Limites et évolutions à prévoir

Ce projet est un **MVP pédagogique**, pas une application prête à être exposée sur Internet : il **n'inclut pas de connexion, d'autorisation par rôle ni de restriction des actions selon l'utilisateur**. La classe `Administrateur` reste indépendante conformément au diagramme demandé, et il n'existe pas d'écran d'administration dédié. Pour un déploiement réel, ajouter Spring Security, les permissions (employé / responsable) et des journaux d'audit ; ce changement devra être validé avec le client et le diagramme.

Les ressources Bootstrap et Chart.js sont chargées depuis un CDN. Sans Internet, l'application conserve son HTML et son CSS local, mais les composants Bootstrap et le graphique Chart.js peuvent être dégradés.

## 8. Organisation du code

```
src/main/java/tn/salles/reservation/
├── config/DonneesDemo.java
├── model/          Employe, Administrateur, Responsable, Salle, Creneau, Reservation, ...
├── repository/     Interfaces Spring Data JPA
├── service/        Règles métier et calculs
├── web/            Contrôleurs MVC et formulaires
└── web/api/        Contrôleur REST et gestion des erreurs
src/main/resources/
├── templates/      Pages Thymeleaf et fragments communs
├── static/css/     Design responsive
└── application-*.properties
src/test/          Tests Spring Boot/JUnit 5

docs/              Diagrammes Draw.io du projet
```

**Remarque sur les choix de conception** : Le diagramme métier sert de référence, mais il ne représente pas directement les `Controller`, `Service`, `Repository`, `DTO` et `Form`. Ces éléments supplémentaires appartiennent à la conception et à l'implémentation, pas au modèle de domaine.
