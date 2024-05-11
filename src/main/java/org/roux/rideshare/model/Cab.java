package org.roux.rideshare.model;

import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Cab} class represent a cab in the ride-sharing application. It is represented
 * graphically using a {@link Circle} symbol. A cab has a capacity of two passengers at max.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class Cab {
    private boolean isFull;
    private Pane mapPane;
    private Circle carSymbol;
    private List<Passenger> passengers;
    private PathTransition route;

    /**
     * Constructs a new {@code Cab} with specified boolean values and {@link Pane}.
     *
     * @param isFull  true if cab full, else false
     * @param mapPane {@link Pane} where cab will be displayed
     */
    public Cab(boolean isFull, Pane mapPane) {
        this.isFull = isFull;
        this.mapPane = mapPane;
        this.passengers = new ArrayList<>();
        this.route = null;
    }

    /**
     * Initializes the car symbol on the {@link Pane} of the graph.
     */
    public void setupCar() {
        carSymbol = new Circle(5, Color.RED);
        mapPane.getChildren().add(carSymbol);
        carSymbol.setVisible(false);
    }

    /**
     * Adds a new passenger to the cab if cab not full and updates the status of
     * cab, if cab is full after addition of new passenger.
     *
     * @param passenger {@link Passenger} of the cab
     * @return true if passenger added, else false
     */
    public boolean addPassenger(Passenger passenger) {
        if (isFull) {
            return false;
        }
        passengers.add(passenger);
        if (passengers.size() == 2) {
            isFull = true;
        }
        return true;
    }

    /**
     * Getter method for isFull attribute
     *
     * @return true if cab full, else false
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * Setter method for mapPane attribute
     *
     * @param mapPane {@link Pane} of the graph
     */
    public void setMapPane(Pane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * Getter method for carSymbol attribute
     *
     * @return {@link Circle} symbol of car
     */
    public Circle getCarSymbol() {
        return carSymbol;
    }

    /**
     * Getter method for passengers attribute
     *
     * @return {@link List} of car's passengers
     */
    public List<Passenger> getPassengers() {
        return passengers;
    }

    /**
     * Getter method for route attribute
     *
     * @return {@link PathTransition} of car
     */
    public PathTransition getRoute() {
        return route;
    }

    /**
     * Setter method for route attribute
     *
     * @param route {@link PathTransition} of car
     */
    public void setRoute(PathTransition route) {
        this.route = route;
    }

    /**
     * Setter method for passengers attribute
     *
     * @param passengers {@link List} of car's passengers
     */
    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    /**
     * Setter method for isFull attribute
     *
     * @param isFull boolean value
     */
    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    /**
     * To String method for {@code Cab}
     *
     * @return string containing instance details of {@code Cab}
     */
    @Override
    public String toString() {
        return "Cab{" +
                "isFull=" + isFull +
                ", mapPane=" + mapPane +
                ", carSymbol=" + carSymbol +
                ", passengers=" + passengers +
                ", route=" + route +
                '}';
    }
}
