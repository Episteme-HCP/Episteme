/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.ui.engineering.instruments;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Interactive viewer for the Oscilloscope.
 * Generates test signals.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OscilloscopeViewer extends Application {

    private Oscilloscope scope;
    private double frequency = 1.0;
    private double amplitude = 0.8;
    private double time = 0;

    @Override
    public void start(Stage stage) {
        scope = new Oscilloscope();
        scope.setPrefSize(800, 400);

        // Controls
        Slider freqSlider = new Slider(0.1, 10.0, 1.0);
        freqSlider.setShowTickLabels(true);
        freqSlider.setShowTickMarks(true);
        Label freqLabel = new Label("Frequency: 1.0 Hz");
        freqSlider.valueProperty().addListener((obs, old, newV) -> {
            frequency = newV.doubleValue();
            freqLabel.setText(String.format("Frequency: %.1f Hz", frequency));
        });

        Slider ampSlider = new Slider(0.1, 1.0, 0.8);
        ampSlider.setShowTickLabels(true);
        ampSlider.setShowTickMarks(true);
        Label ampLabel = new Label("Amplitude: 0.8");
        ampSlider.valueProperty().addListener((obs, old, newV) -> {
            amplitude = newV.doubleValue();
            ampLabel.setText(String.format("Amplitude: %.1f", amplitude));
        });

        VBox controls = new VBox(10,
                new Label("Signal Frequency"), freqSlider, freqLabel,
                new Label("Signal Amplitude"), ampSlider, ampLabel);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #16213e;");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setCenter(scope);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Oscilloscope - Signal Generator");
        stage.setScene(scene);
        stage.show();

        // Signal Generator Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Generate sine wave
                time += 0.016; // Approx 60fps step
                double val = Math.sin(time * frequency * 2 * Math.PI) * amplitude;
                scope.addData(val);
            }
        }.start();
    }

    public static void show(Stage stage) {
        new OscilloscopeViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
