package org.roux.rideshare.model;

/**
 * The {@code Passenger} class represents a passenger in the cab of ride-sharing
 * application. Each passenger has a source, destination and preference to share
 * ride.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class Passenger {
    private Integer source;
    private Integer destination;
    private boolean shareRide;

    /**
     * Constructs a new {@code Passenger} with specified source, destination and
     * sharing ride preference.
     *
     * @param source      source location of passenger's ride
     * @param destination destination location of passenger's ride
     * @param shareRide   preference of sharing ride
     */
    public Passenger(Integer source, Integer destination, boolean shareRide) {
        this.shareRide = shareRide;
        this.destination = destination;
        this.source = source;
    }

    /**
     * Getter method for source attribute
     *
     * @return source of passenger's ride
     */
    public Integer getSource() {
        return source;
    }

    /**
     * Setter method for source attribute
     *
     * @param source source of passenger's ride
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * Getter method for destination attribute
     *
     * @return destination of passenger's ride
     */
    public Integer getDestination() {
        return destination;
    }

    /**
     * Setter method for destination attribute
     *
     * @param destination destination of passenger's ride
     */
    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    /**
     * Getter method for shareRide attribute
     *
     * @return preference of sharing ride
     */
    public boolean isShareRide() {
        return shareRide;
    }

    /**
     * Setter method for shareRide attribute
     *
     * @param shareRide preference of sharing ride
     */
    public void setShareRide(boolean shareRide) {
        this.shareRide = shareRide;
    }

    /**
     * To String method for {@code Passenger}
     *
     * @return string containing instance details of {@code Passenger}
     */
    @Override
    public String toString() {
        return "Passenger{" +
                "source=" + source +
                ", destination=" + destination +
                ", shareRide=" + shareRide +
                '}';
    }
}
