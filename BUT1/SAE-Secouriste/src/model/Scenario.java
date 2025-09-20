package model;

import model.data.persistence.DPS;
import model.data.persistence.Journee;
import model.data.persistence.Site;
import model.data.persistence.Sport;
import model.data.service.AuthentificationManagement;
import model.data.service.DPSManagement;

public class Scenario {

    private static final DPSManagement dpsManagement = new DPSManagement();


    public static void main(String[] args) {

        System.out.println("Scenario class is ready to be implemented.");

        // L'admin crée un nouveau dps.

        // 1. Il se connecte à l'application.
        // 2. Il accède à la page de gestion des DPS.
        // 3. Il clique sur le bouton "Créer un nouveau DPS".
        // 4. Il remplit les informations nécessaires (nom, date, lieu, etc.).
        // 5. Il enregistre le DPS.
        // 6. Il peut voir le nouveau DPS dans la liste des DPS.


        // 1. Connexion à l'application en tant qu'admin

        AuthentificationManagement auth = AuthentificationManagement.getInstanceAuthentificationManagement();
        AuthentificationManagement.LoginResult result = auth.login("admin@secouriste.fr", "admin");
        if (result != AuthentificationManagement.LoginResult.SUCCESS || !auth.isAdmin()) {
            System.out.println("Connexion admin échouée !");
            return;
        }

        System.out.println("Admin connecté.");



        // 2. Il accède à la page de gestion des DPS.

        DPSManagement dpsManagement = new DPSManagement();
        System.out.println("Accès à la page de gestion des DPS.");

        // 3. Il clique sur le bouton "Créer un nouveau DPS".
        // + 4. Il remplit les informations nécessaires (nom, date, lieu, etc.).

        int dpsId = 1337; // ID fictif pour le DPS, assez important pour ne pas avoir de conflit avec les IDs existants
        // La gestion se fait normalement par dans le controller, mais pour le scénario, on utilise un ID fixe.
        String nomDps = "DPS Test"; // Nom du DPS à créer

        // Création d'un site et d'un sport pour le DPS, même problème que pour l'ID du DPS, on utilise des IDs fictifs.
        Site siteScenario = new Site(1000, "Site Test", 48.8566f, 2.3522f); // Exemple de site
        Sport sportScenario = new Sport(1000, "Description du sport test");
        Journee journeeScenario = new Journee(1, 10, 2025);
        int horaireDepart = 800; // 08:00 AM
        int horaireFin = 1800; // 06:00 PM

        DPS nouveauDps = new DPS(dpsId, nomDps, horaireDepart, horaireFin, siteScenario, sportScenario, journeeScenario);
        System.out.println("Création d'un nouveau DPS : " + nouveauDps.getName());

        // 5 . Il enregistre le DPS.
        dpsManagement.addDps(nouveauDps);

        // 6. Il peut voir le nouveau DPS dans la liste des DPS.
        DPS retrieved = dpsManagement.getDpsById(dpsId);
        if (retrieved != null) {
            System.out.println("DPS créé avec succès : " + retrieved.getName() + " de " + retrieved.getHoraireDepart() + " à " + retrieved.getHoraireFin());
        } else {
            System.out.println("Échec de la création du DPS.");
        }

    }
}
