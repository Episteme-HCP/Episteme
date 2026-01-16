/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.physics.classical.waves;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Real-time Spectrograph visualization showing frequency analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectrographViewer extends AbstractViewer implements Simulatable {

    private final int BANDS = 128;
    private double[] spectrum = new double[BANDS];
    private double sensitivity = 1.0;

    private SpectrumAnalysisProvider analysisProvider;
    private double[] currentSamples;
    private Label fpsLabel;
    private long lastFrameTime = 0;
    private int frameCount = 0;
    private boolean playing = false;

    private Canvas spectrumCanvas;
    private Canvas spectrogramCanvas;

    private final List<Parameter<?>> parameters = new ArrayList<>();
    private AnimationTimer timer;

    private javafx.scene.image.WritableImage spectrogramBuffer;
    private int spectrogramX = 0;

    public SpectrographViewer() {
        setupParameters();
        buildUI();

        analysisProvider = new PrimitiveSpectrumAnalysisProvider();
        setupAnimation();
    }

    public void setAnalysisProvider(SpectrumAnalysisProvider provider) {
        this.analysisProvider = provider;
    }

    /**
     * Feed time-domain samples to the viewer for real-time analysis.
     * @param samples time-domain samples (should be power of 2 for FFT)
     */
    public void feedSamples(double[] samples) {
        this.currentSamples = samples;
    }
    
    @Override
    public String getName() { return "Spectrograph Viewer"; }
    
    @Override
    public String getCategory() { return "Physics"; }

    private void setupParameters() {
        parameters.add(new NumericParameter(
                I18n.getInstance().get("spectrograph.sensitivity", "Sensitivity"),
                I18n.getInstance().get("spectrograph.sensitivity.desc", "Adjusts the signal responsiveness"),
                0.1, 5.0, 0.1, 1.0,
                val -> this.sensitivity = val));

        parameters.add(new Parameter<Boolean>(
                I18n.getInstance().get("spectrograph.mode", "Scientific Mode"),
                I18n.getInstance().get("spectrograph.mode.desc", "Toggles between primitive and object-based engines"),
                false,
                val -> {
                    if (val) {
                        analysisProvider = new RealSpectrumAnalysisProvider();
                    } else {
                        analysisProvider = new PrimitiveSpectrumAnalysisProvider();
                    }
                }));
    }

    private void buildUI() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("viewer-root");

        spectrumCanvas = new Canvas(600, 200);
        spectrogramCanvas = new Canvas(600, 250);

        spectrumCanvas.widthProperty().bind(vbox.widthProperty().subtract(20));
        spectrogramCanvas.widthProperty().bind(vbox.widthProperty().subtract(20));

        fpsLabel = new Label(I18n.getInstance().get("spectrograph.fps", "FPS: --"));
        fpsLabel.getStyleClass().add("description-label");
        fpsLabel.setStyle("-fx-font-size: 10px;");
        
        vbox.getChildren().addAll(spectrumCanvas, spectrogramCanvas, fpsLabel);
        setCenter(vbox);
    }

    private void setupAnimation() {
        LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#00ff00")),
                new Stop(0.5, Color.web("#ffff00")),
                new Stop(1.0, Color.web("#ff0000")));

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!playing) return;

                updateSpectrum();
                renderSpectrum(spectrumCanvas.getGraphicsContext2D(), gradient);
                renderSpectrogram(spectrogramCanvas.getGraphicsContext2D());

                if (lastFrameTime > 0) {
                    double fps = 1_000_000_000.0 / (now - lastFrameTime);
                    if (frameCount++ % 60 == 0) {
                        fpsLabel.setText(I18n.getInstance().get("spectrograph.fps.fmt", "FPS: %.1f", fps));
                    }
                }
                lastFrameTime = now;
            }
        };
    }

    @Override public void play() { this.playing = true; if (timer != null) timer.start(); }
    @Override public void pause() { this.playing = false; }
    @Override public void stop() { this.playing = false; reset(); }
    @Override public void step() { updateSpectrum(); renderSpectrum(spectrumCanvas.getGraphicsContext2D(), null); }
    @Override public void setSpeed(double multiplier) { /* No-op */ }
    @Override public boolean isPlaying() { return playing; }

    public void reset() {
        spectrogramX = 0;
        if (spectrogramBuffer != null) spectrogramBuffer = null;
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return parameters;
    }

    private void updateSpectrum() {
        if (currentSamples != null && analysisProvider != null) {
            spectrum = analysisProvider.computeSpectrum(currentSamples, BANDS, sensitivity);
        }
    }

    private void renderSpectrum(GraphicsContext gc, LinearGradient gradient) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        gc.clearRect(0, 0, w, h);

        double bandWidth = w / BANDS;
        gc.setFill(gradient != null ? gradient : Color.GREEN);

        for (int i = 0; i < BANDS; i++) {
            double barHeight = spectrum[i] * h * 0.8;
            gc.fillRect(i * bandWidth, h - barHeight, bandWidth - 1, barHeight);
        }
    }

    private void renderSpectrogram(GraphicsContext gc) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        if (spectrogramBuffer == null || (int)w <= 0 || (int)h <= 0) {
             if (w > 0 && h > 0)
                spectrogramBuffer = new javafx.scene.image.WritableImage((int) w, (int) h);
             else return;
        }

        javafx.scene.image.PixelWriter writer = spectrogramBuffer.getPixelWriter();
        spectrogramX = (spectrogramX + 2) % (int) w;

        double bandHeight = h / BANDS;
        for (int i = 0; i < BANDS; i++) {
            double intensity = spectrum[i];
            intensity = Math.max(0, Math.min(1.0, intensity));

            Color c;
            if (intensity < 0.2) c = Color.hsb(240, 1.0, intensity * 5.0);
            else c = Color.hsb((1.0 - intensity) * 240.0, 1.0, 1.0);

            for (int dx = 0; dx < 2; dx++) {
                int px = (spectrogramX + dx) % (int) w;
                for (int py = 0; py < bandHeight; py++) {
                    int y = (int) (h - (i + 1) * bandHeight + py);
                    if (y >= 0 && y < h) writer.setColor(px, y, c);
                }
            }
        }

        gc.clearRect(0, 0, w, h);
        int part1W = (int) w - spectrogramX - 2;
        if (part1W > 0) gc.drawImage(spectrogramBuffer, spectrogramX + 2, 0, part1W, h, 0, 0, part1W, h);
        int part2W = spectrogramX + 2;
        if (part2W > 0) gc.drawImage(spectrogramBuffer, 0, 0, part2W, h, part1W, 0, part2W, h);
    }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.spectrograph.desc"); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.spectrograph.longdesc"); }
}
