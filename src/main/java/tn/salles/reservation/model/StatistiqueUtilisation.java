package tn.salles.reservation.model;

import java.time.LocalDateTime;
/** Rapport calculé (non persisté en base). */
public record StatistiqueUtilisation(Salle salle, LocalDateTime periodeDebut, LocalDateTime periodeFin,
                                     long nombreReservations, double tauxOccupation) {}
