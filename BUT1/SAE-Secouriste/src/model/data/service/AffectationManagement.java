package model.data.service;
import model.dao.AffectationDAO;
import model.dao.CompetenceDAO;
import model.data.persistence.*;
import model.graph.assignment.AssignmentExhaustive;
import model.graph.assignment.AssignmentGreedy;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static model.data.service.AuthentificationManagement.getInstanceAuthentificationManagement;
import static model.data.service.SecouristeManagement.getInstanceSecouristeManagement;
import static model.utils.Settings.useGreedy;

/**
 * Class allow to collect all affectation
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AffectationManagement {

    /**
     * Singleton instance of AffectationManagement.
     */
    private final AffectationDAO affectationDAO = new AffectationDAO();




    /**
     * Get the list of rescuers assigned to a specific DPS.
     *
     * @param idDps The ID of the DPS for which to retrieve assigned rescuers.
     * @return A list of Secouriste objects representing the rescuers assigned to the specified DPS.
     */
    public List<Integer> getIdRescuersByDps(long idDps) {
        return this.affectationDAO.findIdRescuerByDPS(idDps);
    }

    /**
     * Checks if a rescuer is available for a specific day.
     *
     * @param idDay The ID of the day to check.
     * @param idRescuer The ID of the rescuer to check.
     * @return true if the rescuer is available, false otherwise.
     */
    public boolean rescuerAvailable(long idDay, long idRescuer) {
        return !this.affectationDAO.rescuerThisDay(idDay, idRescuer);
    }

    /**
     * To check if an affectation already exists in the database.
     *
     * @param affectation The affectation to check for existence.
     * @return true if the affectation exists, false otherwise.
     */
    public boolean isExist(Affectation affectation) {
        return this.affectationDAO.exists(affectation);
    }

    /**
     * Adds a new affectation to the database.
     */
    public void addAffectation(Affectation affectation) {
        this.affectationDAO.insert(affectation);
    }

    /**
     * Retrieves all affectations for a given rescuer.
     *
     * @param secouriste The rescuer for whom to retrieve affectations.
     * @return A list of Affectation objects associated with the specified rescuer.
     */
    public List<Affectation> getAffectationsByRescuer(Secouriste secouriste) {
        return this.affectationDAO.findByRescuer(secouriste.getIdSecouriste());
    }

    /**
     * Retrieves all affectations for a given DPS.
     *
     * @param dps The DPS for which to retrieve affectations.
     * @return A list of Affectation objects associated with the specified DPS.
     */
    public List<Affectation> getAffectationsByDps(DPS dps) {
        return this.affectationDAO.findByDPS(dps.getId());
    }

    /**
     * Launches a greedy or exhaustive assignment of rescuers to a DPS.
     *
     * @param dps The DPS for which the assignment is to be made.
     */
    public void launchAffectation(DPS dps) {
        BesoinManagement besoinManagement = new BesoinManagement();
        Besoin besoin = besoinManagement.getBesoinByDPS(dps);
        List<Competence> competencesOriginal = besoin != null ? new ArrayList<>(besoin.getCompetences()) : new ArrayList<>();

        try {
            if (useGreedy()) {
                new AssignmentGreedy().assignmentRescuersGreedy(dps);
            } else {
                new AssignmentExhaustive(dps);
            }

            // Met à jour le besoin en retirant les compétences qui ont été affectées avec succès
            if (besoin != null) {
                List<Affectation> affectations = this.affectationDAO.findByDPS(dps.getId());
                List<Competence> competencesAffectees = new ArrayList<>();

                for (Affectation affectation : affectations) {
                    competencesAffectees.add(affectation.getCompetenceAffect());
                }

                // Compter les occurrences de chaque compétence dans la liste originale
                // et dans la liste des compétences affectées
                java.util.Map<String, Integer> compteurOriginal = new java.util.HashMap<>();
                java.util.Map<String, Integer> compteurAffectees = new java.util.HashMap<>();

                // Compter les compétences originales
                for (Competence comp : competencesOriginal) {
                    String intitule = comp.getIntitule();
                    compteurOriginal.put(intitule, compteurOriginal.getOrDefault(intitule, 0) + 1);
                }

                // Compter les compétences affectées
                for (Competence comp : competencesAffectees) {
                    String intitule = comp.getIntitule();
                    compteurAffectees.put(intitule, compteurAffectees.getOrDefault(intitule, 0) + 1);
                }

                // Conserver uniquement les compétences qui n'ont pas toutes été affectées
                ArrayList<Competence> competencesRestantes = new ArrayList<>();
                for (Competence comp : competencesOriginal) {
                    String intitule = comp.getIntitule();
                    int nbOriginal = compteurOriginal.getOrDefault(intitule, 0);
                    int nbAffectees = compteurAffectees.getOrDefault(intitule, 0);

                    // Si le nombre d'affectations est inférieur au nombre demandé,
                    // ajouter autant de compétences que nécessaire
                    if (nbAffectees < nbOriginal) {
                        competencesRestantes.add(comp);
                        // Incrémenter le compteur d'affectations pour ne pas ajouter
                        // cette compétence plusieurs fois
                        compteurAffectees.put(intitule, compteurAffectees.getOrDefault(intitule, 0) + 1);
                    }
                }

                // Mettre à jour le besoin avec uniquement les compétences non affectées ou partiellement affectées
                besoin.setCompetences(competencesRestantes);
                besoinManagement.updateBesoin(besoin);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affectation : " + e.getMessage());
        }
    }

    /**
     * Removes an affectation from the database.
     *
     * @param affectation The affectation to remove.
     */
    public void removeAffectation(Affectation affectation) {
        this.affectationDAO.delete(affectation);
    }


    /**
     * Exports the affectations of rescuers to a CSV file.
     * If the user is an admin, it exports all assignments.
     * Otherwise, it exports only the assignments of the specified rescuer.
     *
     * @param idRescuer The ID of the rescuer for whom to export assignments.
     */
    public void exportYourAffectationToCSV(long idRescuer) {
        if (getInstanceAuthentificationManagement().isAdmin()) {
            List<Secouriste> secouristes = getInstanceSecouristeManagement().findAll();
            String fileName = "Toutes_les_affectations.csv";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("IdSecouriste;Nom;Prenom;IdDPS;NomDPS;Competence\n");
                int total = 0;
                for (Secouriste secouriste : secouristes) {
                    List<Affectation> affectations = this.affectationDAO.findByRescuer(secouriste.getIdSecouriste());
                    for (Affectation aff : affectations) {
                        Secouriste s = aff.getSecouristeAffect();
                        String nom = s != null ? s.getNom() : "";
                        String prenom = s != null ? s.getPrenom() : "";
                        String competence = aff.getCompetenceAffect() != null ? aff.getCompetenceAffect().getIntitule() : "";
                        String nomDps = aff.getDPSAffect() != null ? aff.getDPSAffect().getName() : "";
                        writer.write(
                            aff.getSecouristeAffect().getIdSecouriste() + ";" +
                            nom + ";" +
                            prenom + ";" +
                            aff.getDPSAffect().getId() + ";" +
                            nomDps + ";" +
                            competence + "\n"
                        );
                        total++;
                    }
                }
                writer.flush();
                // System.out.println("Nombre total d'affectations exportées : " + total);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'exportation des affectations : " + e.getMessage());
            }
        } else {
            List<Affectation> affectations = this.affectationDAO.findByRescuer(idRescuer);
            String fileName = "Vos_affectations.csv";
            try (FileWriter writer = new FileWriter(fileName)) {
                System.out.println("Export CSV lancé, nb affectations = " + affectations.size());
                writer.write("IdSecouriste;Nom;Prenom;IdDPS;NomDPS;Competence\n");
                for (Affectation aff : affectations) {
                    Secouriste s = aff.getSecouristeAffect();
                    String nom = s != null ? s.getNom() : "";
                    String prenom = s != null ? s.getPrenom() : "";
                    String competence = aff.getCompetenceAffect() != null ? aff.getCompetenceAffect().getIntitule() : "";
                    String nomDps = aff.getDPSAffect() != null ? aff.getDPSAffect().getName() : "";
                    writer.write(
                        aff.getSecouristeAffect().getIdSecouriste() + ";" +
                        nom + ";" +
                        prenom + ";" +
                        aff.getDPSAffect().getId() + ";" +
                        nomDps + ";" +
                        competence + "\n"
                    );
                }
                writer.flush();
            } catch (IOException e) {
                System.err.println("Erreur lors de l'exportation des affectations : " + e.getMessage());
            }
        }
    }
}
