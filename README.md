# Ridesharing-Optimization
The RideShareApplication is a simple Java application designed to connect cab drivers and passengers for ride-sharing purposes. It aims to make commuting easier by allowing users to find rides with similar ride-sharing preference.

## Features
- **Ride Matching**: Automatically matches passengers with drivers based on proximity and ride-share preference.
- **Graphical User Interface**: Users can interact with a GUI to book their rides and receive updates.

## Requirements
- Java Runtime Environment (JRE) 8 or later.
- An IDE platform to run the application

## Setup and Running the Application
### Setup
No installation is necessary. Ensure you have Java installed on your machine and an IDE to run the code. Download the `RideShare.zip` file to a convenient location on your computer. Unzip and import the project into the IDE. Build the project to compile the dependencies and code.
### Running the Application
To run the application, open the file RideShareApplication.java located under 'src/main/java/org/roux/rideshare/' and run this file.

## Usage
After launching the application, follow the on-screen prompts to select the source, destination, and ride-sharing preferences. To test the ride-sharing functionality, choose a new source, destination, and ride-share preference near a booked cab’s location. Once the second passenger selects these options, the nearby cab is automatically assigned to this new passenger as well. If no nearby cab meets the ride-sharing preferences, a new cab will be assigned. The new route is highlighted, and at the end, the fare is displayed on the prompt.

## Closing the Application
To exit the application, close the GUI window or stop the application from the IDE.