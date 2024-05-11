package org.roux.rideshare.model;

import javafx.scene.shape.Circle;

import java.util.*;

/**
 * The {@code CabPool} class represents a pool of cabs present in the ride-sharing
 * application. It provides methods to book and drop-off cabs present in the pool.
 * It considers the sharing ride preference and books the nearest cab if available
 * for different passengers.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class CabPool {
    private final Queue<Cab> availableCabs;
    private final List<Cab> bookedCabs;

    /**
     * Constructs a new {@code CabPool} with specified size.
     *
     * @param size size of cab pool
     */
    public CabPool(int size) {
        availableCabs = new LinkedList<>();
        bookedCabs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Cab cab = new Cab(false, null);
            availableCabs.offer(cab);
        }
    }

    /**
     * Books an available cab for a passenger based on ride-sharing preference.
     * It finds the nearby available car for ride-sharing preference, else, it
     * books a new cab
     *
     * @param passenger passenger to ride the cab
     * @param nodeMap   map of all the nodes present in the graph
     * @return booked cab
     */
    public Cab bookCab(Passenger passenger,
                       Map<Integer, Circle> nodeMap) {
        if (passenger.isShareRide()) {
            return findNearestCab(passenger, nodeMap);
        } else {
            return newCab(passenger);
        }
    }

    /**
     * Finds the nearest cab available for ride-sharing. It could be a pre-booked cab
     * or a new cab based on the location of passenger.
     *
     * @param passenger passenger to ride the cab
     * @param nodeMap   map of all the nodes present in the graph
     * @return booked cab
     */
    private Cab findNearestCab(Passenger passenger,
                               Map<Integer, Circle> nodeMap) {
        Cab bookedCab = null;
        for (Cab cab : bookedCabs) {
            if (!cab.isFull()) {
                Passenger exisitingPassenger = cab.getPassengers().get(0);
                if (exisitingPassenger.isShareRide()) {
                    double cabPositionX = cab.getCarSymbol().getTranslateX() + cab.getCarSymbol().getLayoutX();
                    double cabPositionY = cab.getCarSymbol().getTranslateY() + cab.getCarSymbol().getLayoutY();
                    double passengerPositionX = nodeMap.get(passenger.getSource()).getCenterX();
                    double passengerPositionY = nodeMap.get(passenger.getSource()).getCenterY();
                    double distance = Math.sqrt(Math.pow(passengerPositionX - cabPositionX, 2)
                            + Math.pow(passengerPositionY - cabPositionY, 2));
                    if (distance <= 250) {
                        bookedCab = cab;
                        break;
                    }
                }
            }
        }
        if (bookedCab == null) {
            return newCab(passenger);
        }
        bookedCab.addPassenger(passenger);
        return bookedCab;
    }

    /**
     * Books a new cab from the carpool.
     *
     * @param passenger passenger to ride the cab
     * @return booked cab
     */
    private Cab newCab(Passenger passenger) {
        if (availableCabs.isEmpty()) {
            return null;
        }
        Cab cab = availableCabs.poll();
        cab.addPassenger(passenger);
        bookedCabs.add(cab);
        return cab;
    }

    /**
     * Drops off a cab and adds it to the available carpool.
     *
     * @param cab booked cab
     */
    public void dropOffCab(Cab cab) {
        availableCabs.offer(cab);
        bookedCabs.remove(cab);
        cab.setPassengers(new ArrayList<>());
        cab.setFull(false);
        cab.setRoute(null);
    }
}
