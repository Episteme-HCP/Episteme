/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.geology;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Earthquake Viewer.
 * Visualizes seismic data on a map of Earth.
 */
public class EarthquakeMapViewer extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

    private final List<Earthquake> quakes = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        generateMockData();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: #111;");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw(gc, now);
            }
        }.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("JScience Earthquake Map");
        stage.setScene(scene);
        stage.show();
    }

    private void generateMockData() {
        // Ring of Fire approximation
        // West Coast Americas
        addQuakeCluster(35, -120, 50, 5.0);
        // Chile
        addQuakeCluster(-30, -70, 30, 6.0);
        // Japan
        addQuakeCluster(36, 138, 40, 5.5);
        // Indonesia
        addQuakeCluster(-5, 120, 30, 6.5);
        // Random usage
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            quakes.add(new Earthquake((r.nextDouble() * 180) - 90, (r.nextDouble() * 360) - 180,
                    2.0 + r.nextDouble() * 3));
        }
    }

    private void addQuakeCluster(double lat, double lon, int count, double baseMag) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            double dLat = r.nextGaussian() * 5;
            double dLon = r.nextGaussian() * 5;
            double mag = baseMag + r.nextGaussian();
            if (mag < 1)
                mag = 1;
            quakes.add(new Earthquake(lat + dLat, lon + dLon, mag));
        }
    }

    private void draw(GraphicsContext gc, long now) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw Map Grid/Outline
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(1);

        // Equator
        double eqY = latToY(0);
        gc.strokeLine(0, eqY, WIDTH, eqY);

        // Prime Meridian
        double pmX = lonToX(0);
        gc.strokeLine(pmX, 0, pmX, HEIGHT);

        // Continents (Very crude rectangles for demo context or skipped)
        // Just dots for quakes

        for (Earthquake q : quakes) {
            double x = lonToX(q.lon);
            double y = latToY(q.lat);

            double size = Math.pow(q.mag, 1.5) * 2;

            // Color by magnitude
            Color c = Color.hsb(120 - (q.mag * 12), 1.0, 1.0, 0.6);
            if (q.mag > 6)
                c = Color.hsb(0, 1.0, 1.0, 0.8); // Red for big ones

            // Pulse effect
            double pulse = (Math.sin(now / 200_000_000.0 + q.lat) + 1) / 2;
            if (q.mag > 6)
                size += pulse * 5;

            gc.setFill(c);
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }

    private double lonToX(double lon) {
        // -180 to 180 -> 0 to WIDTH
        return (lon + 180) / 360.0 * WIDTH;
    }

    private double latToY(double lat) {
        // 90 to -90 -> 0 to HEIGHT
        return (1.0 - (lat + 90) / 180.0) * HEIGHT;
    }

    private static class Earthquake {
        double lat, lon, mag;

        Earthquake(double lat, double lon, double mag) {
            this.lat = lat;
            this.lon = lon;
            this.mag = mag;
        }
    }

    public static void show(Stage stage) {
        new EarthquakeMapViewer().start(stage);
    }
}
