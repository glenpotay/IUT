package model.graph.test;

import model.data.persistence.DPS;
import model.data.persistence.Journee;
import model.data.persistence.Site;
import model.data.persistence.Sport;
import model.graph.assignment.AssignmentExhaustive;
import model.graph.assignment.AssignmentGreedy;

public class TestAssignmentExhaustive {




    /**
     * Main method to run the exhaustive test for assignment.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create an instance of the test class
        TestAssignmentExhaustive test = new TestAssignmentExhaustive();

        // Run the exhaustive test
        test.testPerformances();
    }


    /**
     * Test de performance avec différentes tailles de données
     */
    void testPerformances() {
        System.out.println("********** Tests de Performance **********");

        int[] tailles = {1, 2, 4, 8, 16};

        for (int taille : tailles) {
            System.out.println("=== Test performance avec " + taille + " secouristes ===");

            Site siteTest = new Site(1L, "La Clusaz - Ski nordique/Biathlon", 45.9044f, 6.4231f);
            Sport sportTest = new Sport(1L, "Sport Test");
            Journee journeeTest = new Journee(1, 7, 2025);
            DPS dps = new DPS(1L, "DPS Test", 8, 18, siteTest, sportTest, journeeTest);


            long startTime = System.nanoTime();

            try {
                new AssignmentExhaustive(dps);
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


}
