package model.graph.utils;

import model.data.persistence.Competence;
import model.data.persistence.Necessite;

import java.util.*;

// /////////////// // MatrixUtils - Utility class for skill dependencies /////////////// //

/**
 * MatrixUtils - This class provides utility methods for creating and manipulating matrices
 * related to skill dependencies in a graph structure.
 * It includes methods to create a skill dependency map, an adjacency matrix,
 * and to build superior dependencies for each competence.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class MatrixUtils {


    // /////////////// // Superior dependencies /////////////// //

    /**
     * Build all superior dependencies for each competence
     *
     * @param competences List of competences
     * @param necessites  List of necessites defining dependencies
     * @return Map where each competence is associated with a list of its superior competences
     */
    public Map<Competence, List<Competence>> buildAllSuperiorDependencies(List<Competence> competences, List<Necessite> necessites) {
        // Create a map to hold the superiors for each competence
        Map<Competence, List<Competence>> result = new HashMap<>();
        // For each competence, find all its superiors
        for (Competence c : competences) {

            // Use a set to track visited competences to avoid cycles (the set list cant take
            Set<Competence> visited = new HashSet<>();
            // Start the recursive search for superiors for the current competence
            // get all superiors of the competence c
            List<Competence> superiors = getAllSuperiors(c, necessites, visited);
            result.put(c, superiors);
        }
        return result;
    }

    /**
     * Recursively retrieves all superiors of a competence.
     *
     * @param c         The competence for which to find superiors
     * @param necessites List of dependencies (necessites)
     * @param visited   Set to track already visited competences to avoid cycles
     * @return List of all superiors of the given competence
     */
    private List<Competence> getAllSuperiors(Competence c, List<Necessite> necessites, Set<Competence> visited) {
        // Initialize a list to hold the superiors of the competence
        List<Competence> superiors = new ArrayList<>();

        // Iterate through all necessites to find superiors of the competence
        for (Necessite n : necessites) {

            // If the first competence in the necessite matches the given competence
            if (n.getComp1().equals(c)) {
                Competence sup = n.getComp2();
                if (visited.add(sup)) { // L'avantage du set, c'est qu'il ne peut pas contenir de doublons, donc si sup n'est pas déjà dans visited, il est ajouté est return true sinon false
                    // And so if the superior competence is not already visited, we add it to the list of superiors
                    superiors.add(sup); // Add the superior competence to the list
                    // And we start again with the superior competence to find its superiors
                    // by recursively calling the method.
                    superiors.addAll(getAllSuperiors(sup, necessites, visited));
                }
            }
        }
        return superiors;
    }




////// METHODE DE MATRICE  ADJACENTE //////////

    // Associe chaque intitulé de compétence (String) à un entier unique
    private final Map<String, Integer> MapSkillToHisInt = new HashMap<>();


    /**
     * createSkillDependencyMap
     *
     * @param elements a list of Necessite elements that define the dependencies between competences
     * @return A map where each key is an integer representing a competence and the value is a list of integers representing the competences that depend on it
     */
    public Map<Integer, List<Integer>> createSkillDependencyMap(List<String> competences, List<Necessite> elements) {
        // Réinitialise la map à chaque appel pour éviter les doublons ou incohérences
        MapSkillToHisInt.clear();
        Map<Integer, List<Integer>> skillDependencies = new HashMap<>();
        int index = 0;

        // D'abord, indexer toutes les compétences (par leur intitulé)
        for (String intitule : competences) {
            if (!MapSkillToHisInt.containsKey(intitule)) {
                MapSkillToHisInt.put(intitule, index++);
            }
        }

        // Ensuite, remplir la map des dépendances
        for (Necessite n : elements) {
            String fromIntitule = n.getComp1().getIntitule();
            String toIntitule = n.getComp2().getIntitule();
            Integer from = MapSkillToHisInt.get(fromIntitule);
            Integer to = MapSkillToHisInt.get(toIntitule);
            if (from != null && to != null) {
                skillDependencies.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            }
        }
        return skillDependencies;
    }


    /**
     * Create the Adjacency Matrix of a Map
     *
     * @param skillDependencies a map of int with all the link between vertices
     * @return A matrix of adjacency link
     */
    public int[][] createAdjacencyMatrix(Map<Integer, List<Integer>> skillDependencies) {
        // All the variables needed : size of the list = n and matrix full of 0
        int n = MapSkillToHisInt.size();
        int[][] matrix = new int[n][n];

        // ForEach renvoie type BiConsumer : En gros, on interagit avec une methode sur une deux arguments (par exemple key, values). c'est une boucle forEach pour un dico
        skillDependencies.forEach((key, values) -> {
            for (int val : values) {
                matrix[key][val] = 1 ;
            }
        });
        return matrix;
    }

    /**
     * Convert a matrix to a string representation
     * @param matrix the matrix to convert
     * @return a string representation of the matrix
     */
    public String MatrixToString(int[][] matrix) {
        String text = "";
        for (int[] line : matrix) {
            for (int column : line) {
                text+= column + " ";
            }
            text+="\n";
        }

        return text ;
    }

    /**
     * Get the map that associates each skill with its integer representation
     * @return a map where the key is the skill's name and the value is its integer representation
     */
    public Map<String, Integer> getMapSkillToHisInt() {
        return this.MapSkillToHisInt ;
    }
}
