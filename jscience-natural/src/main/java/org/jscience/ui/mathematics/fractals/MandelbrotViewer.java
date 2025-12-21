package org.jscience.ui.mathematics.fractals;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Mandelbrot Set Viewer.
 * Demonstrates complex number operations and fractal generation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MandelbrotViewer extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAX_ITER = 100;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        drawMandelbrot(canvas);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setTitle("JScience Mandelbrot Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void drawMandelbrot(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();

        double minRe = -2.0;
        double maxRe = 1.0;
        double minIm = -1.2;
        double maxIm = minIm + (maxRe - minRe) * HEIGHT / WIDTH;

        double reFactor = (maxRe - minRe) / (WIDTH - 1);
        double imFactor = (maxIm - minIm) / (HEIGHT - 1);

        for (int y = 0; y < HEIGHT; y++) {
            double cIm = maxIm - y * imFactor;
            for (int x = 0; x < WIDTH; x++) {
                double cRe = minRe + x * reFactor;
                double Z_re = cRe, Z_im = cIm;
                boolean isInside = true;
                int n = 0;
                for (; n < MAX_ITER; n++) {
                    double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
                    if (Z_re2 + Z_im2 > 4) {
                        isInside = false;
                        break;
                    }
                    Z_im = 2 * Z_re * Z_im + cIm;
                    Z_re = Z_re2 - Z_im2 + cRe;
                }

                if (isInside) {
                    pw.setColor(x, y, Color.BLACK);
                } else {
                    // Color mapping based on n
                    pw.setColor(x, y, Color.hsb((n * 5) % 360, 1.0, 1.0));
                }
            }
        }
    }

    public static void show(Stage stage) {
        new MandelbrotViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
