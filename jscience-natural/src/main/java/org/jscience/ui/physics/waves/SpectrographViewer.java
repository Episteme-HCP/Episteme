
/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.waves;

import org.jscience.ui.i18n.I18n;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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

    private final int BANDS = 128; // Increased resolution
    private double[] spectrum = new double[BANDS];
    private double time = 0;
    private double sensitivity = 1.0;

    private SpectrumProvider provider;
    private Label fpsLabel;
    private long lastFrameTime = 0;
    private int frameCount = 0;

    // UI
    private Canvas spectrumCanvas;
    private Canvas spectrogramCanvas;
    private javafx.scene.control.ComboBox<String> sourceCombo;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("dark-viewer-root");

        Label title = new Label(I18n.getInstance().get("spectrograph.title"));
        title.getStyleClass().add("dark-header");

        // Controls
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(5));

        Label sensLbl = new Label(I18n.getInstance().get("spectrograph.sensitivity"));
        sensLbl.getStyleClass().add("dark-label");

        Label sourceLbl = new Label(I18n.getInstance().get("spectrograph.source"));
        sourceLbl.getStyleClass().add("dark-label");

        sourceCombo = new javafx.scene.control.ComboBox<>();
        sourceCombo.getItems().addAll(
                I18n.getInstance().get("spectrograph.source.voice"),
                I18n.getInstance().get("spectrograph.source.sine"),
                I18n.getInstance().get("spectrograph.source.harmonics"),
                I18n.getInstance().get("spectrograph.source.noise"));
        sourceCombo.setValue(I18n.getInstance().get("spectrograph.source.voice"));
        sourceCombo.setOnAction(e -> {
            if (provider != null) {
                String val = sourceCombo.getValue();
                String internalPattern = "Voice";
                if (val.equals(I18n.getInstance().get("spectrograph.source.sine")))
                    internalPattern = "Sine";
                else if (val.equals(I18n.getInstance().get("spectrograph.source.harmonics")))
                    internalPattern = "Harmonics";
                else if (val.equals(I18n.getInstance().get("spectrograph.source.noise")))
                    internalPattern = "Noise";
                provider.setSourcePattern(internalPattern);
            }
        });

        fpsLabel = new Label(I18n.getInstance().get("spectrograph.fps"));
        fpsLabel.getStyleClass().add("dark-label-muted");

        ToggleButton engineSwitch = new ToggleButton(I18n.getInstance().get("spectrograph.mode.primitive"));
        engineSwitch.setOnAction(e -> {
            boolean selected = engineSwitch.isSelected();
            String currentVal = sourceCombo.getValue();
            String internalPattern = "Voice";
            if (currentVal.equals(I18n.getInstance().get("spectrograph.source.sine")))
                internalPattern = "Sine";
            else if (currentVal.equals(I18n.getInstance().get("spectrograph.source.harmonics")))
                internalPattern = "Harmonics";
            else if (currentVal.equals(I18n.getInstance().get("spectrograph.source.noise")))
                internalPattern = "Noise";

            if (selected) {
                provider = new ObjectSpectrumProvider(BANDS);
                engineSwitch.setText(I18n.getInstance().get("spectrograph.mode.scientific"));
            } else {
                provider = new PrimitiveSpectrumProvider(BANDS);
                engineSwitch.setText(I18n.getInstance().get("spectrograph.mode.primitive"));
            }
            provider.setSourcePattern(internalPattern);
        });

        controls.getChildren().addAll(sensLbl, sourceLbl, sourceCombo, engineSwitch, fpsLabel);

        // Canvases
        spectrumCanvas = new Canvas(600, 200);
        spectrogramCanvas = new Canvas(600, 250);

        root.getChildren().addAll(title, controls, spectrumCanvas, spectrogramCanvas);

        // Gradient for Spectrum
        LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#00ff00")),
                new Stop(0.5, Color.web("#ffff00")),
                new Stop(1.0, Color.web("#ff0000")));

        // Provider init
        provider = new PrimitiveSpectrumProvider(BANDS);
        provider.setSourcePattern("Voice");

        // Animation Loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSpectrum();
                renderSpectrum(spectrumCanvas.getGraphicsContext2D(), gradient);
                renderSpectrogram(spectrogramCanvas.getGraphicsContext2D());

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
        Scene scene = new Scene(root, 640, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setScene(scene);
        stage.show();
    }

    private void updateSpectrum() {
        time += 0.05; // Slower time for better visual scrolling
        provider.update(time, sensitivity);
        spectrum = provider.getSpectrum();
    }

    private void renderSpectrum(GraphicsContext gc, LinearGradient gradient) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.clearRect(0, 0, w, h);

        // Background Grid
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(0.5);
        for (int x = 0; x < w; x += 50)
            gc.strokeLine(x, 0, x, h);
        for (int y = 0; y < h; y += 50)
            gc.strokeLine(0, y, w, y);

        double bandWidth = w / BANDS;
        gc.setFill(gradient);

        for (int i = 0; i < BANDS; i++) {
            double barHeight = spectrum[i] * h * 0.8;
            gc.fillRect(i * bandWidth, h - barHeight, bandWidth - 1, barHeight);
        }
    }

    private void renderSpectrogram(GraphicsContext gc) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        // Optimized scrolling using PixelWriter and Snapshot to shift (faster than
        // iterating pixel by pixel for everything)
        // Shifting with drawImage(canvas, -2, 0) is actually fast if not overused
        // but it requires a snapshot or a buffer.
        // Let's use a WritableImage as a persistent buffer to avoid canvas.snapshot()
        // overhead.

        if (spectrogramBuffer == null) {
            spectrogramBuffer = new javafx.scene.image.WritableImage((int) w, (int) h);
        }

        // 1. Shift buffer content
        javafx.scene.image.PixelReader reader = spectrogramBuffer.getPixelReader();
        javafx.scene.image.PixelWriter writer = spectrogramBuffer.getPixelWriter();

        // Drawing is better than reading/writing individual pixels for scrolling
        // We can draw the image onto itself with a shift if we use a temporary canvas
        // or a second buffer.
        // But for simplicity and decent performance, we'll draw the buffer onto the GC
        // shifted,
        // then snapshot the result back to buffer (though snapshot is what we want to
        // avoid).

        // ALTERNATIVE: Use a circular buffer of columns (double[][]) and render all at
        // once?
        // No, let's use the GC's drawImage for scrolling, but we need an OFFSCREEN
        // Buffer.

        // Actually, we can just draw the new column onto the buffer at a sliding x
        // index
        // and draw the buffer twice (two halves) to the canvas to simulate scrolling.
        // This is the classic circular buffer approach for scrolling textures.

        spectrogramX = (spectrogramX + 2) % (int) w;

        double bandHeight = h / BANDS;
        for (int i = 0; i < BANDS; i++) {
            double intensity = spectrum[i];
            if (intensity > 1.0)
                intensity = 1.0;
            if (intensity < 0.0)
                intensity = 0.0;

            // Heatmap Color: Dark Blue -> Cyan -> Green -> Yellow -> Red
            Color c;
            if (intensity < 0.2)
                c = Color.hsb(240, 1.0, intensity * 5.0); // transition to blue
            else
                c = Color.hsb((1.0 - intensity) * 240.0, 1.0, 1.0);

            // Write 2px wide column at current spectrogramX
            for (int dx = 0; dx < 2; dx++) {
                int px = (spectrogramX + dx) % (int) w;
                for (int py = 0; py < bandHeight; py++) {
                    int y = (int) (h - (i + 1) * bandHeight + py);
                    if (y >= 0 && y < h) {
                        writer.setColor(px, y, c);
                    }
                }
            }
        }

        // Draw the buffer in two parts to the canvas to show continuous scroll
        gc.clearRect(0, 0, w, h);
        int part1W = (int) w - spectrogramX - 2;
        if (part1W > 0) {
            gc.drawImage(spectrogramBuffer, spectrogramX + 2, 0, part1W, h, 0, 0, part1W, h);
        }
        int part2W = spectrogramX + 2;
        if (part2W > 0) {
            gc.drawImage(spectrogramBuffer, 0, 0, part2W, h, part1W, 0, part2W, h);
        }
    }

    private javafx.scene.image.WritableImage spectrogramBuffer;
    private int spectrogramX = 0;

    public static void show(Stage stage) {
        new SpectrographViewer().start(stage);
    }
}
