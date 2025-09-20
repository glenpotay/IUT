package model.graph.utils;

/**
 * Classe GraphUtils - this class provides utility methods for working with graphs,
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public class GraphUtils {

    /**
     * It checks if the graph is oriented or not
     * @param matrix - the adjacency matrix given
     * @return true if the graph is oriented, false otherwise
     */
    public static boolean isOriented(int[][] matrix) {
        int n = matrix.length;
        // Parcourt toutes les cases de la matrice
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Si une case n'est pas symétrique par rapport à la diagonale, le graphe est orienté
                if (matrix[i][j] != matrix[j][i]) {
                    return true;
                }
            }
        }
        // Si toutes les arêtes sont symétriques, le graphe est non orienté
        return false;
    }

    /**
     * It checks if e cycle is detected during the DFS
     * @param adjMatrix - the adjacency matrix given
     * @param v - the vertex we are going to check
     * @param visited - the list that contains true if a vertex is visited, false otherwise
     * @param inProcess - the list that contains true if a vertex is going to be studied from the current vertex, false otherwise
     * @return true if a cycle is detected, false otherwise
     */
    public static boolean cycleDetected(int[][] adjMatrix, int v, boolean[] visited, boolean[] inProcess) {
        visited[v] = true; // Marque le sommet comme visité
        inProcess[v] = true;  // Marque le sommet comme en cours d'exploration

        // Parcourt tous les sommets pour voir s'il existe une arête de v vers un autre sommet
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[v][i] == 1) {
                // Appel récursif sur un sommet non encore visité
                if (!visited[i]) {
                    if (cycleDetected(adjMatrix, i, visited, inProcess)) return true;
                } else if (inProcess[i]) {
                    // Si le sommet est déjà en cours d'exploration, alors il y a un cycle
                    return true; // Cycle trouvé
                }
            }
        }

        inProcess[v] = false;
        return false;
    }




    /**
     * It checks if the adjacency matrix is valid
     * @param matrix - the matrix given
     * @return true is the adjacency matrix is valid, false otherwise
     */
    public static boolean adjMatrixValid(int[][] matrix) {

        if(matrix == null) {
            return false;
        } else {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i].length != matrix.length) {
                    return false;
                }
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] != 0 && matrix[i][j] != 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * It checks if the graph is a DAG (Directed acyclic graph)
     * @param adjMatrix - the adjacency matrix given
     * @return true if the graph is a DAG, false otherwise
     */
    public boolean isDAG(int[][] adjMatrix){
        // Vérifie que la matrice est valide et que le graphe est orienté
        if (!isOriented(adjMatrix) || !adjMatrixValid(adjMatrix)) {
            return false;
        }

        // Initialise les tableaux de suivi pour la DFS
        boolean[] visited = new boolean[adjMatrix.length];
        boolean[] inProcess = new boolean[adjMatrix.length];

        // Lance une DFS sur chaque sommet non encore visité
        for (int i = 0; i < adjMatrix.length; i++) {
            if (!visited[i]) {
                if (cycleDetected(adjMatrix, i, visited, inProcess)) {
                    return false; // Un cycle est détecté, donc ce n'est pas un DAG
                }
            }
        }
        return true; // Aucun cycle détecté, le graphe est un DAG
    }
}