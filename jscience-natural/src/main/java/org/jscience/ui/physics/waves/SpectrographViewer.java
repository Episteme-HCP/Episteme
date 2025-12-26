
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.waves;

import org.jscience.natural.i18n.I18n;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

/**
 * Real-time Spectrograph visualization showing frequency analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpectrographViewer extends Application {

    private final int BANDS = 64;
    private double[] spectrum = new double[BANDS];
    private double time = 0;
    private double sensitivity = 1.0;

    private SpectrumProvider provider;
    private Label fpsLabel;
    private long lastFrameTime = 0;
    private int frameCount = 0;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dark-viewer-root");

        Label title = new Label(I18n.getInstance().get("spectrograph.title"));
        // ...
        Label sensLbl = new Label(I18n.getInstance().get("spectrograph.sensitivity"));
        // ...
        fpsLabel = new Label(I18n.getInstance().get("spectrograph.fps"));
        // ...
        ToggleButton engineSwitch = new ToggleButton(I18n.getInstance().get("spectrograph.mode.primitive"));
        engineSwitch.setOnAction(e -> {
            if (engineSwitch.isSelected()) {
                provider = new ObjectSpectrumProvider(BANDS);
                engineSwitch.setText(I18n.getInstance().get("spectrograph.mode.scientific"));
            } else {
                provider = new PrimitiveSpectrumProvider(BANDS);
                engineSwitch.setText(I18n.getInstance().get("spectrograph.mode.primitive"));
            }
        });
        // Canvas setup
        Canvas canvas = new Canvas(560, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().addAll(title, sensLbl, fpsLabel, engineSwitch, canvas);

        // Gradient
        LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#00ff00")),
                new Stop(0.5, Color.web("#ffff00")),
                new Stop(1.0, Color.web("#ff0000")));

        // Provider init
        provider = new PrimitiveSpectrumProvider(BANDS);

        // Animation Loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSpectrum();
                render(gc, gradient);

                // FPS calculation
                if (lastFrameTime > 0) {
                    double fps = 1_000_000_000.0 / (now - lastFrameTime);
                    if (frameCount++ % 60 == 0) {
                        fpsLabel.setText(String.format("FPS: %.1f", fps));
                    }
                }
                lastFrameTime = now;
            }
        };
        timer.start();

        stage.setTitle(I18n.getInstance().get("spectrograph.window"));
        Scene scene = new Scene(root, 600, 500);

        // Theme
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);

        stage.setScene(scene);
        stage.show();
    }

    private void updateSpectrum() {
        time += 0.1;
        provider.update(time, sensitivity);
        spectrum = provider.getSpectrum();
    }

    private void render(GraphicsContext gc, LinearGradient gradient) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        double bandWidth = gc.getCanvas().getWidth() / BANDS;
        gc.setFill(gradient);

        for (int i = 0; i < BANDS; i++) {
            double h = spectrum[i] * 350;
            gc.fillRect(i * bandWidth + 1, 400 - h, bandWidth - 2, h);

            // Small glow on top
            gc.setFill(Color.WHITE);
            gc.setGlobalAlpha(0.3);
            gc.fillRect(i * bandWidth + 1, 395 - h, bandWidth - 2, 5);
            gc.setGlobalAlpha(1.0);
            gc.setFill(gradient);
        }
    }

    public static void show(Stage stage) {
        new SpectrographViewer().start(stage);
    }
}
