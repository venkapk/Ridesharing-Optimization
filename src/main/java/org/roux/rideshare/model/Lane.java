package org.roux.rideshare.model;

/**
 * The {@code Lane} class represent an edge in the map graph. It contains the details
 * of the edge, i.e., the target node and weight of the edge.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class Lane {
    private final int target;
    private final double weight;

    /**
     * Constructs a new {@code Lane} with specified target node and weight.
     *
     * @param target target node
     * @param weight weight of the lane(edge)
     */
    public Lane(int target, double weight) {
        this.target = target;
        this.weight = weight;
    }

    /**
     * Getter method for target attribute
     *
     * @return target node
     */
    public int getTarget() {
        return target;
    }

    /**
     * Getter method for weight attribute
     *
     * @return weight of lane
     */
    public double getWeight() {
        return weight;
    }
}
