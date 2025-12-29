package org.jscience.ui.devices;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jscience.device.sensors.Oscilloscope;

/**
 * Viewer for Oscilloscope.
 */
public class OscilloscopeViewer extends AbstractDeviceViewer<Oscilloscope> {

    private final Canvas canvas;
    private final AnimationTimer timer;

    public OscilloscopeViewer(Oscilloscope device) {
        super(device);

        this.canvas = new Canvas(300, 200);
        StackPane frame = new StackPane(canvas);
        frame.setStyle("-fx-background-color: black; -fx-border-color: #555; -fx-border-width: 2;");

        this.getChildren().add(frame);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
    }

    public void play() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK); // CRT phosphor persistence simulation could be here
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.LIME);
        gc.setLineWidth(1.5);

        try {
            double[] wave = device.captureWaveform(0);
            if (wave != null && wave.length > 0) {
                gc.beginPath();
                double w = canvas.getWidth();
                double h = canvas.getHeight();
                double step = w / wave.length;

                for (int i = 0; i < wave.length; i++) {
                    double x = i * step;
                    // Map -1..1 to h..0
                    double y = h / 2 - (wave[i] * h / 2.5);
                    if (i == 0)
                        gc.moveTo(x, y);
                    else
                        gc.lineTo(x, y);
                }
                gc.stroke();
            }
        } catch (Exception e) {
            // device might not be ready
        }
    }
}
