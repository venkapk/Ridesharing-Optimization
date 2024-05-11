package org.roux.rideshare.view;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.roux.rideshare.model.*;
import org.roux.rideshare.utils.DijkstraAlgorithm;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code MapWindow} class handles the GUI for the application. It initializes a
 * cab pool and provides methods to visualize the graph layout of map. It also handles
 * the interaction of the user with GUI and appropriately providing valid output to the
 * GUI for future interactions.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class MapWindow {
    private static final int windowWidth = 1000;
    private static final int windowHeight = 700;
    private static final int nodeSize = 10;
    private static final Color sourceColor = Color.GREEN;
    private static final Color destinationColor = Color.RED;
    private static final Random colorRandom = new Random();
    private Passenger passenger;
    private final MapLayout mapLayout;
    private final DijkstraAlgorithm dAlgo;
    private final java.util.Map<Integer, Circle> nodeMap = new HashMap<>();
    private final java.util.Map<Integer, List<Line>> edgesMap = new HashMap<>();
    private Label instructionLabel;
    private final CabPool cabPool;
    private Pane mapPane;
    private Label rideCostLabel;

    /**
     * Constructs a new {@code MapWindow} with specified {@link MapLayout} and {@link  DijkstraAlgorithm}.
     *
     * @param mapLayout layout of the map
     * @param dAlgo     Dijkstra's algorithm
     */
    public MapWindow(MapLayout mapLayout, DijkstraAlgorithm dAlgo) {
        this.mapLayout = mapLayout;
        this.dAlgo = dAlgo;
        this.cabPool = new CabPool(5);
        this.passenger = new Passenger(null, null, false);
    }

    /**
     * Record to maintain path and distance of the path
     *
     * @param distance distance of the path
     * @param path     path of the graph
     */
    public record PathAndDistances(double distance, List<Integer> path) {
    }

    /**
     * Visualizes the map-graph layout using the structure of the graph and positions
     * of vertices. Adds an {@link org.w3c.dom.events.MouseEvent} to handle the selection
     * of source and destination nodes of the passenger and display the cab's path.
     *
     * @param rideShare {@link Stage} of the application
     */
    public void visualize(Stage rideShare) {
        mapPane = new Pane();
        BorderPane borderPane = new BorderPane();
        mapPane.setPrefSize(windowWidth, windowHeight);

        java.util.Map<Integer, Double> xCoordinates = new HashMap<>();
        java.util.Map<Integer, Double> yCoordinates = new HashMap<>();

        for (int i = 0; i < mapLayout.getVerticalLanes().length; i++) {
            double xCoordinate = (i + 1) * ((double) windowWidth / (mapLayout.getVerticalLanes().length + 1));
            for (int node : mapLayout.getVerticalLanes()[i]) {
                xCoordinates.put(node, xCoordinate);
            }
        }

        for (int i = 0; i < mapLayout.getHorizontalLanes().length; i++) {
            double yCoordinate = (i + 1) * ((double) windowHeight / (mapLayout.getHorizontalLanes().length + 1));
            for (int node : mapLayout.getHorizontalLanes()[i]) {
                yCoordinates.put(node, yCoordinate);
            }
        }

        xCoordinates.forEach((node, xCoordinate) -> {
            double yCoordinate = yCoordinates.get(node);
            Circle circle = new Circle(xCoordinate, yCoordinate, nodeSize, Color.BLUE);
            mapPane.getChildren().add(circle);
            nodeMap.put(node, circle);
        });

        mapLayout.getVerticesAdjList().forEach((source, targets) -> targets.forEach(target -> {
            Circle sourceNode = nodeMap.get(source);
            Circle destinationNode = nodeMap.get(target.getTarget());
            Line edge = new Line(sourceNode.getCenterX(), sourceNode.getCenterY(),
                    destinationNode.getCenterX(), destinationNode.getCenterY());
            addEdge(source, edge);
            edge.setStroke(Color.GRAY);
            mapPane.getChildren().add(edge);
        }));

        nodeMap.forEach((nodeId, circle) -> circle.setOnMouseClicked(e -> handleNodeClick(nodeId)));

        instructionsPanel(borderPane);
        borderPane.setCenter(mapPane);
        Scene scene = new Scene(borderPane);
        rideShare.setScene(scene);
        rideShare.show();
    }

    /**
     * Adds an edge to a particular node
     *
     * @param node node of the graph
     * @param edge edge from the node
     */
    private void addEdge(Integer node, Line edge) {
        List<Line> existingEdges = edgesMap.get(node);
        if (existingEdges != null) {
            existingEdges.add(edge);
            edgesMap.put(node, existingEdges);
        } else {
            List<Line> newEdge = new ArrayList<>();
            newEdge.add(edge);
            edgesMap.put(node, newEdge);
        }
    }

    /**
     * Creates an interaction component on the right hand side of the {@link BorderPane}
     *
     * @param borderPane {@link BorderPane} of the user interface
     */
    private void instructionsPanel(BorderPane borderPane) {
        VBox interactionPaneLayout = new VBox(20);
        interactionPaneLayout.setPrefWidth(150);
        interactionPaneLayout.setPrefHeight(150);
        instructionLabel = new Label("Select the starting node.");
        rideCostLabel = new Label();
        interactionPaneLayout.getChildren().add(instructionLabel);
        interactionPaneLayout.getChildren().add(rideCostLabel);
        borderPane.setRight(interactionPaneLayout);
    }

    /**
     * Handles the click of mouse on the node and assigns the source and destination
     * accordingly. Once, the source and destination are selected, it triggers the
     * process of booking a cab and displaying it on the GUI.
     *
     * @param nodeId selected node
     */
    private void handleNodeClick(Integer nodeId) {
        Circle node = nodeMap.get(nodeId);
        if (passenger.getSource() == null) {
            passenger.setSource(nodeId);
            node.setFill(sourceColor);
            instructionLabel.setText("Select the ending node.");
        } else if (passenger.getDestination() == null && !nodeId.equals(passenger.getSource())) {
            passenger.setDestination(nodeId);
            node.setFill(destinationColor);
            shareCabPopup();
            instructionLabel.setText("Calculating shortest path...");
            Cab cab = this.cabPool.bookCab(passenger, nodeMap);
            if (cab == null) {
                noCab();
            } else {
                calculateShortestPath(cab);
            }
            this.passenger = new Passenger(null, null, false);
            instructionLabel.setText("Select the starting node.");
        }
    }

    /**
     * Display a popup window to ask for the preference of sharing ride.
     */
    private void shareCabPopup() {
        Alert shareCabAlert = new Alert(Alert.AlertType.CONFIRMATION);
        shareCabAlert.setTitle("Share Cab Popup");
        shareCabAlert.setHeaderText("Options");
        shareCabAlert.setContentText("Do you want to share ride?");
        Optional<ButtonType> shareCabResult = shareCabAlert.showAndWait();
        passenger.setShareRide(shareCabResult.isPresent() && shareCabResult.get() == ButtonType.OK);
    }

    /**
     * Display a popup window stating unavailability of cabs.
     */
    private void noCab() {
        Alert noCabAlert = new Alert(Alert.AlertType.INFORMATION);
        noCabAlert.setTitle("No Cab");
        noCabAlert.setContentText("No cabs available! Wait for some time.");
        noCabAlert.showAndWait();
    }

    /**
     * Calculates the shortest path for the ride of cab's passengers. Triggers methods to
     * modify the existing path in case of ride-sharing and highlights the ride's path.
     *
     * @param cab {@link Cab} booked cab
     */
    private void calculateShortestPath(Cab cab) {
        if (passenger.getSource() != null && passenger.getDestination() != null) {
            java.util.Map<Integer, Double> distancesFromSource = dAlgo.shortestPaths(passenger.getSource());
            PathAndDistances modifiedPath = modifyPath(cab, distancesFromSource);
            List<Integer> pathOfRide = modifiedPath.path();
            double totalDistanceOfRide = modifiedPath.distance();
            displayPath(pathOfRide, getRandomColor());
            cab.setMapPane(mapPane);
            cab.setupCar();
            displayCabRide(pathOfRide, totalDistanceOfRide, cab);
        }
    }

    /**
     * Modifies the path of the cab for ride-sharing or provides the normal path in case of
     * personal cab-ride.
     *
     * @param cab                 booked cab
     * @param distancesFromSource distancesFromSource calculated from Dijkstra's algorithm
     * @return {@link PathAndDistances} of the final path of cab
     */
    private PathAndDistances modifyPath(Cab cab, java.util.Map<Integer, Double> distancesFromSource) {
        List<Passenger> passengers = cab.getPassengers();
        PathAndDistances pathAndDistances = new PathAndDistances(0.0, new ArrayList<>());
        switch (passengers.size()) {
            case 1 -> {
                List<Integer> finalPath = generatePath(passenger.getSource(),
                        passenger.getDestination(), distancesFromSource);
                double distPath = calculateTotalDistanceRide(distancesFromSource, finalPath);
                pathAndDistances = new PathAndDistances(distPath, finalPath);
            }
            case 2 -> {
                List<Integer> finalPath = new ArrayList<>();
                double distPath = 0.0;
                Passenger passenger1 = passengers.get(0);
                List<Integer> initialPath = generatePath(passenger1.getSource(),
                        passenger.getSource(), distancesFromSource);
                List<Integer> path1 = generatePath(passenger.getSource(),
                        passenger1.getDestination(), distancesFromSource);
                List<Integer> path2 = generatePath(passenger.getSource(),
                        passenger.getDestination(), distancesFromSource);
                double initialPathDist = calculateTotalDistanceRide(distancesFromSource, initialPath);
                double distPath1 = calculateTotalDistanceRide(distancesFromSource, path1);
                double distPath2 = calculateTotalDistanceRide(distancesFromSource, path2);
                distPath = distPath + initialPathDist;
                if (distPath1 <= distPath2) {
                    finalPath.addAll(path1);
                    distPath = distPath + distPath1;
                    java.util.Map<Integer, Double> lastDistances = dAlgo.shortestPaths(passenger1.getDestination());
                    List<Integer> lastPath = generatePath(passenger1.getDestination(),
                            passenger.getDestination(), lastDistances);
                    double lastPathDistance = calculateTotalDistanceRide(lastDistances, lastPath);
                    distPath = distPath +lastPathDistance;
                    finalPath.addAll(lastPath);
                    pathAndDistances = new PathAndDistances(distPath, finalPath);
                } else {
                    finalPath.addAll(path2);
                    distPath = distPath + distPath2;
                    java.util.Map<Integer, Double> lastDistances = dAlgo.shortestPaths(passenger.getDestination());
                    List<Integer> lastPath = generatePath(passenger.getDestination(),
                            passenger1.getDestination(), lastDistances);
                    double lastPathDistance = calculateTotalDistanceRide(lastDistances, lastPath);
                    distPath = distPath + lastPathDistance;
                    finalPath.addAll(lastPath);
                    pathAndDistances = new PathAndDistances(distPath, finalPath);
                }
            }
            default -> {
            }
        }
        return pathAndDistances;
    }

    /**
     * Calculates the total distance of a particular ride path of a cab
     *
     * @param distancesFromSource distancesFromSource calculated from Dijkstra's algorithm
     * @param path                path of the cab-rides
     * @return total distance of the ride
     */
    private double calculateTotalDistanceRide(java.util.Map<Integer, Double> distancesFromSource,
                                              List<Integer> path) {
        double totalDistance = 0.0;
        for (Integer i : path) {
            totalDistance = totalDistance + distancesFromSource.getOrDefault(i, 0.0);
        }
        return totalDistance;
    }

    /**
     * Computes a path from a source node and destination node using the distances
     * calculated from Dijkstra's algorithm.
     *
     * @param sourceNode          source node of path
     * @param destinationNode     destination node of path
     * @param distancesFromSource distancesFromSource calculated from Dijkstra's algorithm
     * @return path for a ride
     */
    private List<Integer> generatePath(int sourceNode, int destinationNode,
                                       java.util.Map<Integer, Double> distancesFromSource) {
        List<Integer> path = new ArrayList<>();
        int currentNode = destinationNode;
        while (currentNode != sourceNode) {
            path.add(currentNode);
            int finalCurrentNode = currentNode;
            currentNode = mapLayout.getVerticesAdjList().get(currentNode).stream()
                    .filter(e -> distancesFromSource.getOrDefault(e.getTarget(),
                            Double.MAX_VALUE) == distancesFromSource.get(finalCurrentNode) - e.getWeight())
                    .findAny().orElse(new Lane(sourceNode, 0)).getTarget();
        }
        path.add(sourceNode);
        Collections.reverse(path);
        return path;
    }

    /**
     * Displays a path for a cab-ride using a particular {@link Color}
     *
     * @param path  path of cab-rde
     * @param color {@link Color} for path
     */
    private void displayPath(List<Integer> path, Color color) {
        for (int i = 0; i < path.size() - 1; i++) {
            int sourceNode = path.get(i);
            int destinationNode = path.get(i + 1);
            Line edge = new Line(nodeMap.get(sourceNode).getCenterX(),
                    nodeMap.get(sourceNode).getCenterY(),
                    nodeMap.get(destinationNode).getCenterX(),
                    nodeMap.get(destinationNode).getCenterY());
            edge.setStroke(color);
            edge.setStrokeWidth(3);
            ((Pane) nodeMap.get(sourceNode).getParent()).getChildren().add(edge);
        }
    }

    /**
     * Displays the ride transition of the cab using {@link PathTransition}.
     *
     * @param path                path of the ride
     * @param totalDistanceOfPath total distance of the path
     * @param cab                 booked cab
     */
    private void displayCabRide(List<Integer> path, double totalDistanceOfPath, Cab cab) {
        Path cabRoute = new Path();
        for (int i = 0; i < path.size() - 1; i++) {
            int node = path.get(i);
            int nextNode = path.get(i + 1);
            Line edge = findEdgeBetweenNodes(node, nextNode);
            if (edge != null) {
                PathElement routeElement = new LineTo(edge.getEndX(), edge.getEndY());
                if (i == 0) {
                    cabRoute.getElements().add(new MoveTo(edge.getStartX(), edge.getStartY()));
                }
                cabRoute.getElements().add(routeElement);
            }
        }
        PathTransition pathTransition;
        if (cab.getRoute() != null) {
            pathTransition = cab.getRoute();
            if (pathTransition.getStatus() == Animation.Status.RUNNING) {
                pathTransition.stop();
                pathTransition.setPath(cabRoute);
                pathTransition.playFromStart();
            }
        } else {
            pathTransition = new PathTransition(Duration.seconds(totalDistanceOfPath / 2),
                    cabRoute, cab.getCarSymbol());
            cab.setRoute(pathTransition);
        }
        cab.getCarSymbol().setVisible(true);
        pathTransition.play();
        pathTransition.setOnFinished(event -> {
            double fare = totalDistanceOfPath / (10);
            rideCostLabel.setText("Total fare = " + fare + "$\nfor " + cab.getPassengers().size() + " passenger(s)");
            cabPool.dropOffCab(cab);
        });

    }

    /**
     * Generate a random color for the path
     *
     * @return {@link Color} for the path
     */
    private Color getRandomColor() {
        float redValue = colorRandom.nextFloat();
        float greenValue = colorRandom.nextFloat();
        float blueValue = colorRandom.nextFloat();
        return new Color(redValue, greenValue, blueValue, 1.0);
    }

    /**
     * Checks if an edge is present between two nodes of a graph.
     *
     * @param node1 first node of an edge
     * @param node2 second node of an edge
     * @return {@link Line} between the nodes
     */
    private Line findEdgeBetweenNodes(int node1, int node2) {
        List<Line> edgesFromNode1 = edgesMap.get(node1);
        if (edgesFromNode1 != null) {
            for (Line edge : edgesFromNode1) {
                if (isEndpoint(edge, node2)) {
                    return edge;
                }
            }
        }
        return null;
    }

    /**
     * Checks if an edge terminates at a given node
     *
     * @param edge   edge of the graph
     * @param nodeID node of the graph
     * @return true if edge terminates at given node, else false
     */
    private boolean isEndpoint(Line edge, int nodeID) {
        Circle node = nodeMap.get(nodeID);
        return (Math.abs(edge.getEndX() - node.getCenterX()) < 1.0 &&
                Math.abs(edge.getEndY() - node.getCenterY()) < 1.0);
    }
}
