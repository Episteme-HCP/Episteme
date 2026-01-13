package org.jscience.ui.viewers.physics.classical.waves;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;

import java.util.List;

/**
 * A JavaFX component that displays an audio waveform and its spectrogram.
 */
public class AudioViewer extends VBox {

    private final Canvas waveformCanvas;
    private final Canvas spectrogramCanvas;
    private final ScrollPane scrollPane;
    private final VBox contentBox;

    public AudioViewer() {
        this.setSpacing(5);
        
        waveformCanvas = new Canvas(800, 150);
        spectrogramCanvas = new Canvas(800, 250);
        
        contentBox = new VBox(5, waveformCanvas, spectrogramCanvas);
        
        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        
        this.getChildren().add(scrollPane);
    }

    public void loadAudio(File file) {
        try {
            AudioAnalyzer analyzer = new AudioAnalyzer(file);
            double[] samples = analyzer.getAudioData();
            
            // Render Waveform
            renderWaveform(samples);

            // Calculate and Render Spectrogram
            // Window size 1024 (power of 2), overlap 512
            List<double[]> spectrogram = analyzer.computeSpectrogram(1024, 512);
            renderSpectrogram(spectrogram);

        } catch (Exception e) {
            e.printStackTrace();
            // In a real app, show error dialog
        }
    }

    private void renderWaveform(double[] samples) {
        // Limit width to JavaFX texture limits (approx 8192 or 16384)
        // 8000 is safe.
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
        int step = (int) Math.max(1, samples.length / width); // Simple decimation
        
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

        double width = waveformCanvas.getWidth(); // Match waveform width
        spectrogramCanvas.setWidth(width);
        spectrogramCanvas.setHeight(bins); // 1 pixel per bin? or scale height
        
        // Let's fix height to 250 and scale
        double height = 250;
        spectrogramCanvas.setHeight(height);

        WritableImage image = new WritableImage((int)width, (int)height);
        PixelWriter pw = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            // Map x coordinate to spectrogram time index
            int tIndex = (int) ((double)x / width * timeSteps);
            if (tIndex >= timeSteps) break;

            double[] spectrum = spectrogram.get(tIndex);
            
            for (int y = 0; y < height; y++) {
                // Map y coordinate to frequency bin (low freq at bottom)
                // y=0 is top, y=height is bottom. We want low freq at bottom.
                int binIndex = (int) (((height - 1 - y) / height) * (bins)); 
                
                if (binIndex < 0) binIndex = 0;
                if (binIndex >= bins) binIndex = bins - 1;

                double magnitude = spectrum[binIndex];
                
                // Color mapping: Log scale usually looks better
                double intensity = Math.log10(magnitude + 1e-6); 
                // Normalize intensity roughly [-6 to 0] range maybe?
                // Just naive normalization for now
                double norm = (intensity + 5) / 5.0; 
                norm = Math.max(0, Math.min(1, norm));

                pw.setColor(x, y, getColor(norm));
            }
        }
        
        GraphicsContext gc = spectrogramCanvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
    }
    
    private Color getColor(double intensity) {
        // Simple distinct 'magma' or 'fire' style
        // 0.0 -> Black
        // 0.5 -> Red/Orange
        // 1.0 -> Yellow/White
        return Color.hsb(intensity * 60, 1.0, intensity); // Red-Yellow HSB
        // Or create a look-up table for 'inferno' style
    }
}
