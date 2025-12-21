package org.jscience.ui.mathematics.statistics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Galton Board (Bean Machine) Viewer.
 * Demonstrates the Central Limit Theorem.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GaltonBoardViewer extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private static final int ROWS = 12;
    private static final double PEG_SPACING = 40;
    private static final double START_X = WIDTH / 2;
    private static final double START_Y = 100;

    private List<Ball> balls = new ArrayList<>();
    private int[] bins = new int[ROWS + 1];
    private Random rand = new Random();

    private static class Ball {
        double x, y, vx, vy;
        boolean settled = false;

        Ball(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.setCenter(canvas);

        AnimationTimer timer = new AnimationTimer() {
            long lastAdd = 0;

            @Override
            public void handle(long now) {
                if (now - lastAdd > 50_000_000) { // Add ball every 50ms
                    balls.add(new Ball(START_X + (rand.nextDouble() - 0.5), START_Y - 20));
                    lastAdd = now;
                }
                update();
                draw(canvas.getGraphicsContext2D());
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("JScience Galton Board");
        stage.setScene(scene);
        stage.show();
    }

    private void update() {
        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball b = it.next();
            if (b.settled)
                continue;

            b.vy += 0.2; // Gravity
            b.y += b.vy;
            b.x += b.vx;

            // Interaction with pegs
            // Pegs are at rows
            int row = (int) ((b.y - START_Y) / PEG_SPACING);
            if (row >= 0 && row < ROWS) {
                double pegY = START_Y + row * PEG_SPACING;
                // Check collision roughly
                if (Math.abs(b.y - pegY) < 5) {

                    // Approximate columns
                    // Actually simpler logic: just randomness at decision points
                    // We simulate "bounce"
                    if (rand.nextDouble() < 0.05) { // Chance to hit peg
                        b.vx = (rand.nextBoolean() ? 1 : -1) * (1 + rand.nextDouble());
                        b.vy *= -0.5; // Damping
                    }
                }
            }

            // Floor / Bins
            double floorY = START_Y + ROWS * PEG_SPACING + 50;
            if (b.y > floorY) {
                b.settled = true;
                // Determine bin
                int bin = (int) ((b.x - (WIDTH / 2 - ROWS * PEG_SPACING / 2)) / PEG_SPACING);
                if (bin >= 0 && bin <= ROWS) {
                    bins[bin]++;
                }
                it.remove(); // Remove simplified version, or keep to stack?
                // Let's remove active ball and increment Histogram
            }
        }
    }

    private void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw Pegs
        gc.setFill(Color.GRAY);
        for (int r = 0; r < ROWS; r++) {

            // Actually usually rectangle or triangle? Triangle for Galton.
            // Triangle: Row 0 has 1 peg, Row 1 has 2...
            double rowY = START_Y + r * PEG_SPACING;
            double rowStartX = WIDTH / 2 - r * PEG_SPACING / 2.0;
            for (int c = 0; c <= r; c++) {
                gc.fillOval(rowStartX + c * PEG_SPACING - 3, rowY - 3, 6, 6);
            }
        }

        // Draw Balls
        gc.setFill(Color.RED);
        for (Ball b : balls) {
            gc.fillOval(b.x - 4, b.y - 4, 8, 8);
        }

        // Draw Histogram
        double baseX = WIDTH / 2 - ROWS * PEG_SPACING / 2.0;
        double baseY = HEIGHT - 50;
        gc.setFill(Color.GREEN);
        for (int i = 0; i <= ROWS; i++) {
            double h = bins[i] * 5;
            gc.fillRect(baseX + i * PEG_SPACING - 10, baseY - h, 20, h);
        }
    }

    public static void show(Stage stage) {
        new GaltonBoardViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
