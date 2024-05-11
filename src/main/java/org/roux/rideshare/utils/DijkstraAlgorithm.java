package org.roux.rideshare.utils;

import org.roux.rideshare.model.Lane;
import org.roux.rideshare.model.MapLayout;

import java.util.*;

/**
 * The {@code DijkstraAlgorithm} class contains the Dijkstra's algorithm to find the shortest
 * path in the {@link MapLayout}(graph layout) from a starting point to all other places.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class DijkstraAlgorithm {
    private final MapLayout mapLayout;

    /**
     * Constructs a new {@code DijkstraAlgorithm} for specified {@link MapLayout}.
     *
     * @param mapLayout graph layout of the map
     */
    public DijkstraAlgorithm(MapLayout mapLayout) {
        this.mapLayout = mapLayout;
    }

    /**
     * Calculates the shortest paths from a source node to all other nodes in the graph using
     * Dijkstra's algorithm. The algorithm uses {@link PriorityQueue} for an efficient implementation.
     *
     * @param source source node of the graph
     * @return distances of all nodes from source
     */
    public Map<Integer, Double> shortestPaths(int source) {
        Map<Integer, Double> distancesFromSource = new HashMap<>();
        PriorityQueue<Lane> priorityQueueNodes = new PriorityQueue<>(Comparator
                .comparingDouble(Lane::getWeight));
        priorityQueueNodes.add(new Lane(source, 0));

        while (!priorityQueueNodes.isEmpty()) {
            Lane currentNode = priorityQueueNodes.poll();
            if (distancesFromSource.containsKey(currentNode.getTarget())) continue;

            distancesFromSource.put(currentNode.getTarget(), currentNode.getWeight());
            List<Lane> adjacentLanes = mapLayout.getVerticesAdjList()
                    .getOrDefault(currentNode.getTarget(), Collections.emptyList());
            for (Lane lane : adjacentLanes) {
                if (!distancesFromSource.containsKey(lane.getTarget())) {
                    priorityQueueNodes.add(new Lane(lane.getTarget(),
                            currentNode.getWeight() + lane.getWeight()));
                }
            }
        }

        return distancesFromSource;
    }
}
