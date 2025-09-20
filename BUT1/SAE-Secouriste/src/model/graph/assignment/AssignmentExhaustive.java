package model.graph.assignment;

import model.dao.*;
import model.data.persistence.*;
import model.data.service.BesoinManagement;
import model.graph.utils.MatrixUtils;

import java.util.*;

/**
 * Classe AssignmentExhaustive - Cette classe réalise une affectation exhaustive des secouristes
 * aux compétences demandées dans un DPS donné, en tenant compte des compétences supérieures.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class AssignmentExhaustive {

    MatrixUtils matrixUtils = new MatrixUtils();
    BesoinManagement besoinManagement = new BesoinManagement();

    /**
     * Map qui associe chaque compétence à un secouriste affecté.
     */
    private final HashMap<Competence, Secouriste> affectation;

    /**
     * Map qui stocke la meilleure affectation partielle trouvée
     */
    private HashMap<Competence, Secouriste> meilleureAffectation;
    private int maxCompetencesAffectees = 0;

    /**
     * Dépendances entre compétences, indiquant quelles compétences sont supérieures à d'autres.
     */
    private Map<Competence, List<Competence>> dependencies = new HashMap<>();



    /**
     * Initialise les dépendances entre compétences (compétences "supérieures" pour chaque compétence).
     */
    private void initDependencies(ArrayList<Competence> competencesBesoins) {

        NecessiteDAO necessiteDAO = new NecessiteDAO();
        List<Necessite> necessites = necessiteDAO.findAll();
        dependencies = matrixUtils.buildAllSuperiorDependencies(competencesBesoins, necessites);
    }


    /**
     * Constructeur AssignmentExhaustive qui effectue l'affectation des secouristes aux compétences du DPS.
     * @param dps - Le DPS à traiter (doit être non null)
     * @param - Liste des compétences à affecter (doit être non null)
     * @throws IllegalArgumentException si les arguments sont null ou si aucun secouriste disponible
     */
    public AssignmentExhaustive(DPS dps) {
        ArrayList<Competence> competences = this.besoinManagement.getBesoinByDPS(dps).getCompetences();

        this.affectation = new HashMap<>();
        this.meilleureAffectation = new HashMap<>();
        initDependencies(competences);

        if (dps == null || competences == null) {
            throw new IllegalArgumentException("Arguments null");
        }

        Journee journee = dps.getJournee();
        List<Secouriste> secouristes = secouristesDisponible(journee);
        if (secouristes.isEmpty()) {
            throw new IllegalArgumentException("Aucun secouriste disponible");
        }

        HashSet<Secouriste> dejaAffectes = new HashSet<>();

        // On lance le backtracking pour trouver la meilleure solution possible
        backtrack(0, competences, secouristes, affectation, dejaAffectes);

        // Même si on n'a pas trouvé de solution complète, on utilise la meilleure solution partielle
        System.out.println("Nombre de compétences affectées: " + maxCompetencesAffectees + "/" + competences.size());

        if (maxCompetencesAffectees > 0) {
            AffectationDAO affectationDAO = new AffectationDAO();
            for (Map.Entry<Competence, Secouriste> entry : meilleureAffectation.entrySet()) {
                Affectation aff = new Affectation(entry.getValue(), dps, entry.getKey());
                affectationDAO.insert(aff);
            }
        } else {
            throw new IllegalStateException("Aucune affectation possible");
        }
    }

    /**
     * Algorithme de backtracking pour affecter récursivement chaque compétence à un secouriste disponible.
     * Garde trace de la meilleure solution partielle trouvée.
     * @param index - Index courant dans la liste des compétences à affecter
     * @param competences - Liste des compétences à affecter
     * @param secouristes - Liste des secouristes disponibles
     * @param affectationActuelle - Affectation en cours des compétences aux secouristes
     * @param dejaAffectes - Ensemble des secouristes déjà affectés
     * @return true si une affectation complète est trouvée, false sinon
     */
    private boolean backtrack(int index, List<Competence> competences, List<Secouriste> secouristes, HashMap<Competence, Secouriste> affectationActuelle, HashSet<Secouriste> dejaAffectes) {

        // On met à jour la meilleure solution partielle si l'actuelle est meilleure
        if (affectationActuelle.size() > maxCompetencesAffectees) {
            maxCompetencesAffectees = affectationActuelle.size();
            meilleureAffectation = new HashMap<>(affectationActuelle);
            System.out.println("Nouvelle meilleure solution partielle: " + maxCompetencesAffectees + " compétences affectées");
        }

        // Si on a tout affecté, on a trouvé une solution complète
        if (index == competences.size()) {
            System.out.println("Toutes les compétences ont été assignées avec succès !");
            return true; // toutes les compétences ont été assignées
        }

        Competence competence = competences.get(index);
        System.out.println("Tentative d'affectation pour la compétence : " + competence.getIntitule() + " (index " + index + "/" + competences.size() + ")");

        for (Secouriste s : secouristes) {
            if (!dejaAffectes.contains(s)) {
                System.out.println("  Essai secouriste id=" + s.getIdSecouriste());
                List<Competence> competencesDuSecouriste = new PossessionDAO().find(s).getCompetencesSec();
                System.out.println("  Nombre de compétences : " + competencesDuSecouriste.size());

                if (possede(competencesDuSecouriste, competence)) {
                    System.out.println("  ✓ Secouriste " + s.getIdSecouriste() + " possède la compétence " + competence.getIntitule());
                    affectationActuelle.put(competence, s);
                    dejaAffectes.add(s);

                    if (backtrack(index + 1, competences, secouristes, affectationActuelle, dejaAffectes)) {
                        return true;
                    }

                    // backtrack
                    System.out.println("  ✗ Retour arrière pour compétence " + competence.getIntitule());
                    affectationActuelle.remove(competence);
                    dejaAffectes.remove(s);
                } else {
                    System.out.println("  ✗ Secouriste " + s.getIdSecouriste() + " ne possède pas la compétence " + competence.getIntitule());
                }
            } else {
                System.out.println("  ✗ Secouriste " + s.getIdSecouriste() + " déjà affecté");
            }
        }

        // On n'a pas pu affecter cette compétence, mais on continue avec les suivantes
        // car on cherche une solution partielle maximale
        return backtrack(index + 1, competences, secouristes, affectationActuelle, dejaAffectes);
    }

    /**
     * Vérifie si un secouriste possède une compétence donnée ou une compétence supérieure.
     * @param competencesSec - Liste des compétences du secouriste
     * @param cible - Compétence ciblée à vérifier
     * @return true si la compétence ciblée ou une compétence supérieure est possédée, false sinon
     */
    private boolean possede(List<Competence> competencesSec, Competence cible) {
        // Si le secouriste possède exactement la compétence ciblée
        if (competencesSec.contains(cible)) {
            return true;
        }

        // Sinon, on regarde si le secouriste possède une compétence supérieure
        List<Competence> compSup = dependencies.get(cible);
        if (compSup != null) {
            for (Competence sup : compSup) {
                System.out.println("Vérification compétence supérieure : " + cible.getIntitule() + " -> " + sup.getIntitule());
                // Recursion : si la compétence supérieure elle-même a des superclasses
                if (possede(competencesSec, sup)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retourne la map actuelle des affectations compétences -> secouristes.
     * @return la map des affectations
     */
    public HashMap<Competence, Secouriste> getAffectation() {
        return affectation;
    }


    /**
     * Récupère la liste des secouristes disponibles pour une journée donnée, c’est-à-dire ceux
     * qui ne sont pas déjà affectés ce jour-là.
     * @param journee - la journée concernée
     * @return la liste des secouristes disponibles
     */
    private List<Secouriste> secouristesDisponible(Journee journee) {
        System.out.println("Recherche des secouristes disponibles pour le " + journee);
        List<Secouriste> secouristesJour = new SecouristeDAO().findByDay(new JourneeDAO().findIdByJour(journee.getJour(), journee.getMois(), journee.getAnnee()));
        System.out.println("Nombre de secouristes pour ce jour : " + secouristesJour.size());

        List<Secouriste> ret = new ArrayList<>();
        for (Secouriste secouriste : secouristesJour) {
            long idJournee = new JourneeDAO().findIdByJour(journee.getJour(), journee.getMois(), journee.getAnnee());
            long idSecouriste = secouriste.getIdSecouriste();
            boolean estDejaAffecte = new AffectationDAO().rescuerThisDay(idJournee, idSecouriste);
            System.out.println("Secouriste " + secouriste.getIdSecouriste() + " déjà affecté ? " + estDejaAffecte);
            if (!estDejaAffecte) {
                ret.add(secouriste);
            }
        }
        System.out.println("Nombre final de secouristes disponibles : " + ret.size());
        return ret;
    }

    /**
     * Crée une instance de Competence à partir d’un nom.
     * @param nom - Nom de la compétence
     * @return L’objet Competence correspondant
     */
    private Competence getCompetence(String nom) {
        return new Competence(nom);
    }

}
