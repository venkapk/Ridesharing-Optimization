package org.roux.rideshare.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.roux.rideshare.model.MapLayout;
import org.roux.rideshare.utils.DijkstraAlgorithm;

/**
 * The {@code MainUIWindow} class handles the GUI for the application. It initializes a map layout
 * using graph(nodes and edges) and provides method to visualize the map layout.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class MainUIWindow {
    private final Stage rideShare;
    private final MapLayout mapLayout;
    private final DijkstraAlgorithm dAlgo;

    /**
     * Constructs a new {@code MainUIWindow} with specified {@link Stage}.
     *
     * @param rideShare stage window
     */
    public MainUIWindow(Stage rideShare) {
        this.rideShare = rideShare;
        this.mapLayout = new MapLayout();
        this.mapLayout.generateGraphStructure();
        this.dAlgo = new DijkstraAlgorithm(mapLayout);
    }

    /**
     * Displays the main stage window by adding a {@link Scene} that contains
     * the behavior when the mouse enters the button area.
     */
    public void display() {
        VBox layoutMainWindow = new VBox(15); //arranges components in vertical stack with spacing of 10 pixels
        Button startButton = new Button("Start Application!!!");
        startButton.setOnAction(e -> showMap());
        layoutMainWindow.getChildren().add(startButton);
        Scene mainWindowContents = new Scene(layoutMainWindow, 300, 200); //contains the physical contents of a JavaFX application's window
        rideShare.setScene(mainWindowContents);
        rideShare.show();
    }

    /**
     * Display the map(graph) by calling the visualization method of {@link MapWindow}.
     * The showMap method is triggered by the button click action in the main window.
     */
    private void showMap() {
        MapWindow mapWindow = new MapWindow(mapLayout, dAlgo);
        mapWindow.visualize(rideShare);
    }
}
