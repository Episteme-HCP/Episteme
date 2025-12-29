package org.jscience.ui.devices;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jscience.device.sensors.Spectrometer;

/**
 * Viewer for Spectrometer.
 */
public class SpectrometerViewer extends AbstractDeviceViewer<Spectrometer> {

    private final Canvas canvas;

    public SpectrometerViewer(Spectrometer device) {
        super(device);

        canvas = new Canvas(300, 100);
        this.getChildren().add(canvas);
        drawSpectrum();
    }

    private void drawSpectrum() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw rainbow gradient mockup
        for (int i = 0; i < canvas.getWidth(); i++) {
            double hue = (i / canvas.getWidth()) * 360;
            gc.setStroke(Color.hsb(hue, 1, 1, 0.5));
            gc.strokeLine(i, 20, i, 80);
        }
    }

    @Override
    public void update() {
        // dynamic update
    }
}
