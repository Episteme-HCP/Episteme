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

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.Parameter;

import org.jscience.physics.classical.waves.SpectrumAnalysisProvider;
import org.jscience.physics.classical.waves.RealSpectrumAnalysisProvider;
import org.jscience.physics.classical.waves.PrimitiveSpectrumAnalysisProvider;

import java.io.File;
import java.util.List;

/**
 * A JavaFX component that displays an audio waveform and its spectrogram.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AudioViewer extends AbstractViewer implements Simulatable {

    private final Canvas waveformCanvas;
    private final Canvas spectrogramCanvas;
    private final ScrollPane scrollPane;
    private final VBox contentBox;
    private File currentFile;
    private SpectrumAnalysisProvider analysisProvider = new PrimitiveSpectrumAnalysisProvider();
    private final List<Parameter<?>> parameters = new java.util.ArrayList<>();

    public AudioViewer() {
        setupParameters();

        waveformCanvas = new Canvas(800, 150);
        spectrogramCanvas = new Canvas(800, 250);
        
        contentBox = new VBox(5, waveformCanvas, spectrogramCanvas);
        contentBox.getStyleClass().add("viewer-root");
        
        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        
        setCenter(scrollPane);
        setRight(createSidebar());
    }

    private void setupParameters() {
        parameters.add(new Parameter<Boolean>(
            I18n.getInstance().get("audio.mode", "Scientific Mode"),
            I18n.getInstance().get("audio.mode.desc", "Toggles between primitive (double) and scientific (Real) FFT"),
            false,
            val -> {
                this.analysisProvider = val ? new RealSpectrumAnalysisProvider() : new PrimitiveSpectrumAnalysisProvider();
                if (currentFile != null) loadAudio(currentFile); // Recompute
            }
        ));
    }
    
    private VBox createSidebar() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(200);
        box.getStyleClass().add("viewer-sidebar");
        
        Label title = new Label(I18n.getInstance().get("audio.control", "Audio Control"));
        title.getStyleClass().add("header-label");
        
        Button openBtn = new Button(I18n.getInstance().get("audio.open", "Open File"));
        openBtn.setMaxWidth(Double.MAX_VALUE);
        openBtn.setOnAction(e -> openFile());

        box.getChildren().addAll(title, new Separator(), openBtn);
        return box;
    }
    
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.getInstance().get("generated.audio.open.audio.file", "Open Audio File"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aif", "*.au", "*.snd")
        );
        File file = fileChooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            loadAudio(file);
        }
    }

    public void loadAudio(File file) {
        this.currentFile = file;
        try {
            AudioAnalyzer analyzer = new AudioAnalyzer(file);
            double[] samples = analyzer.getAudioData();
            
            // Render Waveform
            renderWaveform(samples);

            // Calculate and Render Spectrogram
            // Window size 1024 (power of 2), overlap 512
            List<double[]> spectrogram = analyzer.computeSpectrogram(1024, 512, analysisProvider);
            renderSpectrogram(spectrogram);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderWaveform(double[] samples) {
        double computedWidth = Math.max(800, samples.length / 50.0); 
        double width = Math.min(computedWidth, 8000.0);
        
        waveformCanvas.setWidth(width);
        contentBox.setPrefWidth(width);
        
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();
        gc.setFill(Color.web("#f0f0f0"));
        gc.fillRect(0, 0, width, waveformCanvas.getHeight());
        
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

        double centerY = waveformCanvas.getHeight() / 2;
        double yScale = waveformCanvas.getHeight() / 2 * 0.9;

        gc.beginPath();
        int step = (int) Math.max(1, samples.length / width); 
        
        for (int i = 0; i < samples.length; i+=step) {
            double x = (double) i / samples.length * width;
            double y = centerY - (samples[i] * yScale);
            
            if (i == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();
    }

    private void renderSpectrogram(List<double[]> spectrogram) {
        if (spectrogram.isEmpty()) return;

        int timeSteps = spectrogram.size();
        int bins = spectrogram.get(0).length;

        double width = waveformCanvas.getWidth(); 
        spectrogramCanvas.setWidth(width);
        double height = 250;
        spectrogramCanvas.setHeight(height);

        WritableImage image = new WritableImage((int)width, (int)height);
        PixelWriter pw = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            int tIndex = (int) ((double)x / width * timeSteps);
            if (tIndex >= timeSteps) break;

            double[] spectrum = spectrogram.get(tIndex);
            
            for (int y = 0; y < height; y++) {
                int binIndex = (int) (((height - 1 - y) / height) * (bins)); 
                
                if (binIndex < 0) binIndex = 0;
                if (binIndex >= bins) binIndex = bins - 1;

                double magnitude = spectrum[binIndex];
                double intensity = Math.log10(magnitude + 1e-6); 
                double norm = (intensity + 5) / 5.0; 
                norm = Math.max(0, Math.min(1, norm));

                pw.setColor(x, y, getColor(norm));
            }
        }
        GraphicsContext gc = spectrogramCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.drawImage(image, 0, 0);
    }
    
    private Color getColor(double intensity) {
        return Color.hsb(intensity * 60, 1.0, intensity);
    }

    // --- Simulatable ---
    @Override public void play() { }
    @Override public void pause() { }
    @Override public void stop() { }
    @Override public void step() { }
    @Override public void setSpeed(double speed) { }
    @Override public boolean isPlaying() { return false; }
    
    @Override public String getName() { return I18n.getInstance().get("viewer.audioviewer.name", "Audio Viewer"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }

    @Override public String getDescription() { return I18n.getInstance().get("viewer.audioviewer.desc", "Displays audio waveforms and their spectrograms."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.audioviewer.longdesc", "Comprehensive audio analysis tool that displays time-domain waveforms and frequency-domain spectrograms. Supports various audio formats and scientific FFT analysis."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
