package org.jscience.physics.astronomy.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.physics.astronomy.StarSystem;

/**
 * Demo application for the 3D Solar System Viewer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 */
public class SolarSystemDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the solar system data
            // Note: The path must be absolute resource path inside classpath or relative to
            // loader
            // SolarSystemLoader handles both.
            StarSystem system = SolarSystemLoader.load("/solarsystem.json");

            // Create and launch the viewer
            JavaFXStarSystemViewer viewer = new JavaFXStarSystemViewer(primaryStage);
            viewer.display(system);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
