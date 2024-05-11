package org.roux.rideshare.model;

import java.util.*;

/**
 * The {@code MapLayout} class creates the layout of the map of the application.
 * It represents the map as a graph with nodes and edges. It contains an adjacency
 * list of vertices connecting to each other and matrices containing vertices present
 * in horizontal and vertical lanes. All the edges are created with a random weight
 * between 1 and 10 units.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class MapLayout {
    private final Map<Integer, List<Lane>> verticesAdjList = new HashMap<>();
    private static final Random random = new Random();
    private final int[][] horizontalLanes = {
            {1, 2, 3, 4, 5},        // Lane 1
            {6, 7, 8},              // Lane 2
            {9, 10, 11, 12},        // Lane 3
            {13, 14, 15, 16, 17, 18}, // Lane 4
            {19, 20, 21},           // Lane 5
            {26, 27, 28, 29, 30},   // Lane 6
            {22, 23, 24, 25},       // Lane 7
            {31, 32, 33, 34, 35, 36, 37, 38, 39, 40} // Lane 8
    };

    private final int[][] verticalLanes = {
            {1, 19, 31},        // Lane 1
            {20, 32},           // Lane 2
            {2, 21, 22, 33},    // Lane 3
            {3, 6, 9, 13, 23, 34}, // Lane 4
            {14, 24, 35},       // Lane 5
            {4, 7, 10, 15, 26, 25, 36}, // Lane 6
            {16, 27, 37},       // Lane 7
            {11, 17, 28, 38},   // Lane 8
            {29, 39},           // Lane 9
            {5, 8, 12, 18, 30, 40} // Lane 10
    };

    private final int[][] verticesData = {
            {1, 2}, {1, 19},
            {2, 3}, {2, 21},
            {3, 4}, {3, 6},
            {4, 5}, {4, 7},
            {5, 4}, {5, 8},
            {6, 3}, {6, 7}, {6, 9},
            {7, 4}, {7, 6}, {7, 8}, {7, 10},
            {8, 5}, {8, 7}, {8, 12},
            {9, 6}, {9, 10}, {9, 13},
            {10, 7}, {10, 9}, {10, 11}, {10, 15},
            {11, 10}, {11, 12}, {11, 17},
            {12, 11}, {12, 8}, {12, 18},
            {13, 9}, {13, 14}, {13, 23},
            {14, 13}, {14, 15}, {14, 24},
            {15, 10}, {15, 14}, {15, 16}, {15, 26},
            {16, 15}, {16, 17}, {16, 27},
            {17, 11}, {17, 16}, {17, 18}, {17, 28},
            {18, 12}, {18, 17}, {18, 30},
            {19, 1}, {19, 20}, {19, 31},
            {20, 19}, {20, 21}, {20, 32},
            {21, 2}, {21, 20}, {21, 22},
            {22, 21}, {22, 23}, {22, 33},
            {23, 13}, {23, 22}, {23, 24}, {23, 34},
            {24, 14}, {24, 23}, {24, 25}, {24, 35},
            {25, 24}, {25, 26}, {25, 36},
            {26, 15}, {26, 25}, {26, 27},
            {27, 16}, {27, 26}, {27, 28}, {27, 37},
            {28, 17}, {28, 27}, {28, 29}, {28, 38},
            {29, 28}, {29, 30}, {29, 39},
            {30, 18}, {30, 29}, {30, 40},
            {31, 19}, {31, 32},
            {32, 20}, {32, 31}, {32, 33},
            {33, 22}, {33, 32}, {33, 34},
            {34, 23}, {34, 33}, {34, 35},
            {35, 24}, {35, 34}, {35, 36},
            {36, 25}, {36, 35}, {36, 37},
            {37, 27}, {37, 36}, {37, 38},
            {38, 28}, {38, 37}, {38, 39},
            {39, 29}, {39, 38}, {39, 40},
            {40, 30}, {40, 39}
    };

    /**
     * Generates a map of adjacency list implementation of graph using the vertices
     * data.
     */
    public void generateGraphStructure() {
        for (int[] verticesPair : verticesData) {
            int source = verticesPair[0];
            int target = verticesPair[1];
            double weight = 1 + random.nextInt(10); // Random weight between 1 and 10
            verticesAdjList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Lane(target, weight));
            verticesAdjList.computeIfAbsent(target, k -> new ArrayList<>()).add(new Lane(source, weight));
        }
    }

    /**
     * Getter method for verticalLanes attribute
     *
     * @return matrix of vertices in vertical lanes
     */
    public int[][] getVerticalLanes() {
        return verticalLanes;
    }

    /**
     * Getter method for horizontalLanes attribute
     *
     * @return matrix of vertices in horizontal lanes
     */
    public int[][] getHorizontalLanes() {
        return horizontalLanes;
    }

    /**
     * Getter method for verticesAdjList attribute
     *
     * @return adjacency map of vertices
     */
    public Map<Integer, List<Lane>> getVerticesAdjList() {
        return verticesAdjList;
    }
}
