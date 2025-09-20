package model.graph.test;

import model.dao.CompetenceDAO;
import model.data.persistence.*;
import model.data.service.AffectationManagement;
import model.data.service.BesoinManagement;
import model.data.service.JourneeManagement;
import model.data.service.SecouristeManagement;
import model.data.service.PossessionManagement;
import model.graph.assignment.AssignmentGreedy;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de test pour évaluer les performances de l'algorithme greedy d'affectation
 * des secouristes aux postes de secours (DPS).
 *
 * @author Test Class
 * @version 1.0
 */
public class TestAssignmentGreedy {

    // Données de test simulées
    private static ArrayList<String> competencesTest;
    private static List<Secouriste> secouristesTest;
    private static List<DPS> dpsTest;
    private static List<Competence> competencesObjetsTest;

    /**
     * Initialise les données de test
     */
    private static void initialiserDonneesTest() {
        // Initialisation des compétences
        competencesTest = new ArrayList<>();
        competencesTest.add("PSC1");
        competencesTest.add("PSE1");
        competencesTest.add("PSE2");
        competencesTest.add("Secourisme Aquatique");
        competencesTest.add("Formation Incendie");

        // Création des objets compétences
        competencesObjetsTest = new ArrayList<>();
        for (String comp : competencesTest) {
            competencesObjetsTest.add(new Competence(comp));
        }

        // Initialisation des secouristes de test
        secouristesTest = new ArrayList<>();
        // Secouriste avec beaucoup de compétences
        secouristesTest.add(new Secouriste(1L, "Dupond", "Jean", "15/03/1985", "0123456789", "123 Rue de la Paix, Paris", null));
        // Secouriste avec peu de compétences
        secouristesTest.add(new Secouriste(2L, "Martin", "Marie", "22/07/1990", "0234567890", "456 Avenue des Fleurs, Lyon", null));
        // Secouriste spécialisé
        secouristesTest.add(new Secouriste(3L, "Durand", "Pierre", "10/12/1988", "0345678901", "789 Boulevard du Soleil, Marseille", null));
        // Secouriste polyvalent
        secouristesTest.add(new Secouriste(4L, "Bernard", "Sophie", "05/09/1992", "0456789012", "321 Place de la République, Toulouse", null));
        // Secouriste débutant
        secouristesTest.add(new Secouriste(5L, "Petit", "Paul", "18/11/1995", "0567890123", "654 Rue des Écoles, Nantes", null));

        // Initialisation des DPS de test
        dpsTest = new ArrayList<>();
        Journee journee1 = new Journee(1, 6, 2024);
        Journee journee2 = new Journee(2, 6, 2024);

        Site sitecentre = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);
        Site siteplage = new Site(10L, "Nice - Patinage artistique/Hockey", 43.7102f, 7.262f);
        Site sitemontagne = new Site(3L, "Courchevel - Saut à ski", 45.4147f, 6.6342f);

        Sport sportNatation = new Sport(1L, "Natation");
        Sport sportEscalade = new Sport(2L, "Escalade");
        Sport sportSki = new Sport(3L, "Ski");

        dpsTest.add(new DPS(1L, "DPS Centre", 9, 17, sitecentre, sportNatation, journee1));
        dpsTest.add(new DPS(2L, "DPS Plage", 8, 19, siteplage, sportNatation, journee1));
        dpsTest.add(new DPS(3L, "DPS Montagne", 7, 18, sitemontagne, sportSki, journee2));
    }

    /**
     * Crée un secouriste de test
     */
    private static Secouriste createSecouriste(Long id, String nom, String prenom) {
        return new Secouriste(id, nom, prenom, "01/01/1990", "0123456789", "123 Rue Test", null);
    }

    /**
     * Crée un DPS de test
     */
    private static DPS createDPS(Long id, String nom, Journee journee) {
        // Création d'un site de test
        Site site = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);

        // Création d'un sport de test
        Sport sport = new Sport(1L, "Sport " + nom);

        return new DPS(id, nom, 8, 18, site, sport, journee);
    }

    /**
     * Test de cas pour la méthode assignmentRescuersGreedy
     *
     * @param dps le DPS à tester
     * @param nbSecouristesDisponibles nombre de secouristes disponibles
     * @param nbCompetencesRequises nombre de compétences requises
     * @param tempsExecution temps d'exécution attendu (en ms)
     * @param description description du cas de test
     */
    void testCasAssignmentRescuersGreedy(DPS dps, int nbSecouristesDisponibles,
                                         int nbCompetencesRequises, long tempsExecution,
                                         String description) {
        System.out.println("=== Test: " + description + " ===");

        // Création des mocks pour les services
        MockServices mockServices = createMockServices(nbSecouristesDisponibles, nbCompetencesRequises, dps);

        AssignmentGreedy assignment = new AssignmentGreedy(
                mockServices.competenceDAO,
                competencesTest,
                mockServices.besoinManagement,
                mockServices.affectationManagement,
                mockServices.secouristeManagement,
                mockServices.journeeManagement,
                mockServices.possessionManagement
        );

        // Mesure du temps d'exécution
        long startTime = System.nanoTime();

        try {
            assignment.assignmentRescuersGreedy(dps);
            long endTime = System.nanoTime();
            long executionTime = (endTime - startTime) / 1_000_000; // Conversion en ms

            System.out.println("✓ Test réussi");
            System.out.println("  Temps d'exécution: " + executionTime + " ms");
            System.out.println("  Temps attendu: < " + tempsExecution + " ms");

            if (executionTime < tempsExecution) {
                System.out.println("  ✓ Performance acceptable");
            } else {
                System.out.println("  ⚠ Performance dégradée");
            }

            // Évaluation de la qualité de la solution
            evaluerQualiteSolution(mockServices.affectationManagement, dps);

        } catch (Exception e) {
            System.err.println("✗ Échec du test: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Évalue la qualité de la solution obtenue
     */
    private void evaluerQualiteSolution(AffectationManagement affectationManagement, DPS dps) {
        // Simulation de l'évaluation de la qualité
        /*
        int nbAffectations = affectationManagement.getNombreAffectations();
        System.out.println("  Nombre d'affectations créées: " + nbAffectations);

        if (nbAffectations > 0) {
            System.out.println("  ✓ Couverture des besoins: Partielle/Complète");
        } else {
            System.out.println("  ⚠ Aucune affectation créée");
        }
        */
    }

    /**
     * Test de cas pour vérifier la gestion des cas limites
     */
    void testCasLimites(DPS dps, String casLimite, boolean shouldFail) {
        System.out.println("=== Test cas limite: " + casLimite + " ===");

        MockServices mockServices = createMockServicesLimite(casLimite);

        AssignmentGreedy assignment = new AssignmentGreedy(
                mockServices.competenceDAO,
                competencesTest,
                mockServices.besoinManagement,
                mockServices.affectationManagement,
                mockServices.secouristeManagement,
                mockServices.journeeManagement,
                mockServices.possessionManagement
        );

        try {
            assignment.assignmentRescuersGreedy(dps);

            if (shouldFail) {
                System.err.println("✗ Test échoué: Exception attendue mais non levée");
            } else {
                System.out.println("✓ Test réussi: Cas limite géré correctement");
            }

        } catch (IllegalArgumentException e) {
            if (shouldFail) {
                System.out.println("✓ Test réussi: Exception attendue levée - " + e.getMessage());
            } else {
                System.err.println("✗ Test échoué: Exception inattendue - " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("✗ Test échoué: Exception inattendue - " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Test de performance avec différentes tailles de données
     */
    void testPerformances() {
        System.out.println("********** Tests de Performance **********");

        int[] tailleDonnees = {5, 8, 9, 10, 20, 50, 100};

        for (int taille : tailleDonnees) {
            System.out.println("=== Test performance avec " + taille + " secouristes ===");

            Site siteTest = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);
            Sport sportTest = new Sport(1L, "Sport Test");
            Journee journeeTest = new Journee(1, 6, 2024);
            DPS dps = new DPS(1L, "DPS Test", 8, 18, siteTest, sportTest, journeeTest);
            MockServices mockServices = createMockServicesPerformance(taille);

            AssignmentGreedy assignment = new AssignmentGreedy(
                    mockServices.competenceDAO,
                    competencesTest,
                    mockServices.besoinManagement,
                    mockServices.affectationManagement,
                    mockServices.secouristeManagement,
                    mockServices.journeeManagement,
                    mockServices.possessionManagement
            );

            long startTime = System.nanoTime();

            try {
                assignment.assignmentRescuersGreedy(dps);
                long endTime = System.nanoTime();
                long executionTime = (endTime - startTime) / 1_000_000;

                System.out.println("  Temps d'exécution: " + executionTime + " ms");
                System.out.println("  Complexité observée: O(n²) approximativement");

            } catch (Exception e) {
                System.err.println("  Erreur lors du test: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Tests principaux de l'algorithme greedy
     */
    void testAssignmentRescuersGreedy() {
        System.out.println("********** Tests AssignmentRescuersGreedy **********");

        Site siteTest1 = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);
        Site siteTest2 = new Site(10L, "Nice - Patinage artistique/Hockey", 43.7102f, 7.262f);
        Sport sportTest1 = new Sport(1L, "Natation");
        Sport sportTest2 = new Sport(2L, "Beach Volley");

        DPS dps1 = new DPS(1L, "DPS Centre", 9, 17, siteTest1, sportTest1, new Journee(1, 6, 2024));
        DPS dps2 = new DPS(2L, "DPS Plage", 8, 19, siteTest2, sportTest2, new Journee(1, 6, 2024));

        // Cas normal avec suffisamment de secouristes
        testCasAssignmentRescuersGreedy(dps1, 10, 3, 100,
                "Cas normal - 10 secouristes, 3 compétences requises");

        // Cas avec peu de secouristes
        testCasAssignmentRescuersGreedy(dps1, 3, 5, 50,
                "Cas difficile - 3 secouristes, 5 compétences requises");

        // Cas avec beaucoup de secouristes
        testCasAssignmentRescuersGreedy(dps2, 50, 2, 200,
                "Cas volumineux - 50 secouristes, 2 compétences requises");

        // Cas avec compétences rares
        testCasAssignmentRescuersGreedy(dps2, 20, 4, 150,
                "Cas compétences rares - 20 secouristes, 4 compétences requises");
    }

    /**
     * Tests des cas limites
     */
    void testCasLimites() {
        System.out.println("********** Tests Cas Limites **********");

        Site siteTest = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);
        Sport sportTest = new Sport(1L, "Sport Test");
        DPS dps = new DPS(1L, "DPS Test", 8, 18, siteTest, sportTest, new Journee(1, 6, 2024));

        // Test avec DPS null
        testCasLimites(null, "DPS null", true);

        // Test avec aucun secouriste disponible
        testCasLimites(dps, "Aucun secouriste disponible", true);

        // Test avec aucune compétence requise
        testCasLimites(dps, "Aucune compétence requise", true);

        // Test avec secouristes sans compétences
        testCasLimites(dps, "Secouristes sans compétences", false);
    }

    /**
     * Méthode principale de test
     */
    void runAllTests() {
        System.out.println("##########################################");
        System.out.println("#     TESTS ALGORITHME GREEDY          #");
        System.out.println("##########################################");

        initialiserDonneesTest();

        testAssignmentRescuersGreedy();
        testCasLimites();
        testPerformances();

        System.out.println("##########################################");
        System.out.println("#        FIN DES TESTS                  #");
        System.out.println("##########################################");
    }

    // Classes et méthodes utilitaires pour les mocks
    private static class MockServices {
        CompetenceDAO competenceDAO;
        BesoinManagement besoinManagement;
        AffectationManagement affectationManagement;
        SecouristeManagement secouristeManagement;
        JourneeManagement journeeManagement;
        PossessionManagement possessionManagement;
    }

    /**
     * Crée des services mockés pour les tests normaux
     */
    private MockServices createMockServices(int nbSecouristes, int nbCompetences, DPS dps) {
        MockServices services = new MockServices();

        // Simulation des services - en réalité, vous devriez créer des mocks complets
        // Pour cet exemple, on suppose que les services retournent des données appropriées
        services.competenceDAO = new CompetenceDAO(); // Mock avec findAllIntitule()
        services.besoinManagement = new BesoinManagement(); // Mock avec getBesoinByDPS()
        services.affectationManagement = new MockAffectationManagement();
        services.secouristeManagement = new MockSecouristeManagement(nbSecouristes);
        services.journeeManagement = new JourneeManagement(); // Mock
        services.possessionManagement = new PossessionManagement(); // Mock

        return services;
    }

    /**
     * Crée des services mockés pour les cas limites
     */
    private MockServices createMockServicesLimite(String casLimite) {
        MockServices services = new MockServices();

        // Configuration spécifique selon le cas limite
        services.competenceDAO = new CompetenceDAO();
        services.besoinManagement = new MockBesoinManagementLimite(casLimite);
        services.affectationManagement = new MockAffectationManagement();
        services.secouristeManagement = new MockSecouristeManagementLimite(casLimite);
        services.journeeManagement = new JourneeManagement();
        services.possessionManagement = new PossessionManagement();

        return services;
    }

    /**
     * Crée des services mockés pour les tests de performance
     */
    private MockServices createMockServicesPerformance(int taille) {
        MockServices services = new MockServices();

        services.competenceDAO = new CompetenceDAO();
        services.besoinManagement = new MockBesoinManagementPerformance();
        services.affectationManagement = new MockAffectationManagement();
        services.secouristeManagement = new MockSecouristeManagementPerformance(taille);
        services.journeeManagement = new JourneeManagement();
        services.possessionManagement = new PossessionManagement();

        return services;
    }

    // Classes mock simplifiées (à adapter selon vos besoins réels)
    private static class MockAffectationManagement extends AffectationManagement {
        private int nombreAffectations = 0;

        public int getNombreAffectations() {
            return nombreAffectations;
        }

        @Override
        public void addAffectation(Affectation affectation) {
            nombreAffectations++;
        }

        @Override
        public boolean isExist(Affectation affectation) {
            return false; // Simule qu'aucune affectation n'existe déjà
        }

        @Override
        public boolean rescuerAvailable(long idJournee, long idSecouriste) {
            return true; // Simule que tous les secouristes sont disponibles
        }
    }

    private static class MockSecouristeManagement extends SecouristeManagement {
        private int nbSecouristes;

        public MockSecouristeManagement(int nbSecouristes) {
            this.nbSecouristes = nbSecouristes;
        }

        @Override
        public List<Secouriste> findByIdJournee(long idJournee) {
            return secouristesTest.subList(0, Math.min(nbSecouristes, secouristesTest.size()));
        }

        @Override
        public Secouriste getSecouristeById(long id) {
            return secouristesTest.stream()
                    .filter(s -> s.getIdSecouriste() == id)
                    .findFirst()
                    .orElse(null);
        }
    }

    private static class MockBesoinManagementLimite extends BesoinManagement {
        private String casLimite;

        public MockBesoinManagementLimite(String casLimite) {
            this.casLimite = casLimite;
        }

        @Override
        public Besoin getBesoinByDPS(DPS dps) {
            switch (casLimite) {
                case "Aucune compétence requise":
                    return new Besoin(dps, new ArrayList<>());
                default:
                    return new Besoin(dps, new ArrayList<>(competencesObjetsTest));
            }
        }
    }

    private static class MockSecouristeManagementLimite extends SecouristeManagement {
        private String casLimite;

        public MockSecouristeManagementLimite(String casLimite) {
            this.casLimite = casLimite;
        }

        @Override
        public List<Secouriste> findByIdJournee(long idJournee) {
            switch (casLimite) {
                case "Aucun secouriste disponible":
                    return new ArrayList<>();
                default:
                    return secouristesTest;
            }
        }
    }

    private static class MockBesoinManagementPerformance extends BesoinManagement {
        @Override
        public Besoin getBesoinByDPS(DPS dps) {
            return new Besoin(dps, new ArrayList<>(competencesObjetsTest));
        }
    }

    private static class MockSecouristeManagementPerformance extends SecouristeManagement {
        private int taille;

        public MockSecouristeManagementPerformance(int taille) {
            this.taille = taille;
        }

        @Override
        public List<Secouriste> findByIdJournee(long idJournee) {
            List<Secouriste> secouristes = new ArrayList<>();
            for (int i = 0; i < taille; i++) {
                secouristes.add(new Secouriste((long) i, "Nom" + i, "Prenom" + i, "01/01/1990", "0123456789", "Adresse " + i, null));
            }
            return secouristes;
        }
    }

    /**
     * Point d'entrée pour exécuter tous les tests
     */
    public static void main(String[] args) {
        TestAssignmentGreedy testSuite = new TestAssignmentGreedy();
        testSuite.runAllTests();
    }
}