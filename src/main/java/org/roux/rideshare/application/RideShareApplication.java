package org.roux.rideshare.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.roux.rideshare.view.MainUIWindow;

/**
 * The {@code RideShareApplication} class contains the main method and serves as the entry point
 * for the ride-sharing optimization application. This class initializes the {@link MainUIWindow}
 * window, setting up the GUI for the application.
 *
 * @author Venkateshprasad, Prajwal, Gaurav
 * @version 1.0
 */
public class RideShareApplication extends Application {
    /**
     * Initializes the main window of the application and displays
     * the initial user interface.
     *
     * @param rideShare the main window of the application
     */
    @Override
    public void start(Stage rideShare) {
        rideShare.setTitle("Ride Sharing Optimization Application");
        MainUIWindow rideShareWindow = new MainUIWindow(rideShare);
        rideShareWindow.display();
    }

    /**
     * Entry point of the application
     *
     * @param args command-line arguments to application
     */
    public static void main(String[] args) {
        launch(args);
    }
}