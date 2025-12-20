package org.jscience.ui.physics.electronics;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main entry point for JScience JavaFX Tools.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 4.2
 */
public class OscilloscopeDemo extends Application {

    private Oscilloscope oscilloscope;

    @Override
    public void start(Stage primaryStage) {
        oscilloscope = new Oscilloscope();

        StackPane root = new StackPane(oscilloscope);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("JScience Oscilloscope");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Simulate data feeding
        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 16_000_000) { // ~60 FPS
                    // Generate sine wave
                    double time = now / 1_000_000_000.0;
                    double val = Math.sin(time * 5) * Math.cos(time * 20);
                    oscilloscope.addData(val);
                    lastUpdate = now;
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
