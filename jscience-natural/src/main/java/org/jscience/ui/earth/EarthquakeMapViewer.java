/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.earth;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jscience.earth.seismology.Earthquake;

/**
 * Earthquake Map Viewer.
 * Visualizes seismic data on an interactive world map with explanations and
 * controls.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class EarthquakeMapViewer extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

    private final List<Earthquake> quakes = new ArrayList<>();
    private double offsetX = 0, offsetY = 0;
    private double zoom = 1.0;
    private Label coordLabel;
    private Label infoLabel;

    @Override
    public void start(Stage stage) {
        generateMockData();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        // Info Panel
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.getStyleClass().add("dark-viewer-sidebar");
        infoPanel.setPrefWidth(220);

        Label titleLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.label.title"));
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label explainLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.explanation"));
        explainLabel.setWrapText(true);

        // Legend
        Label legendLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.legend"));

        legendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox legend = new VBox(3);
        legend.getChildren().addAll(
                createLegendItem(Color.hsb(120, 1, 1),
                        org.jscience.ui.i18n.I18n.getInstance().get("earthquake.legend.minor")),
                createLegendItem(Color.hsb(80, 1, 1),
                        org.jscience.ui.i18n.I18n.getInstance().get("earthquake.legend.light")),
                createLegendItem(Color.hsb(40, 1, 1),
                        org.jscience.ui.i18n.I18n.getInstance().get("earthquake.legend.moderate")),
                createLegendItem(Color.RED,
                        org.jscience.ui.i18n.I18n.getInstance().get("earthquake.legend.strong")));

        coordLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.coord.default"));

        coordLabel.setFont(Font.font("Consolas", 12));

        infoLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.hover"));

        infoLabel.setWrapText(true);

        infoPanel.getChildren().addAll(
                titleLabel, new Separator(), explainLabel, new Separator(),
                legendLabel, legend, new Separator(),
                coordLabel, infoLabel);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setRight(infoPanel);
        root.getStyleClass().add("dark-viewer-root");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Mouse controls
        canvas.setOnMouseMoved(e -> {
            double mouseX = e.getX();
            double mouseY = e.getY();
            double lon = xToLon(mouseX);
            double lat = yToLat(mouseY);
            coordLabel.setText(
                    String.format(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.coord.fmt"), lat, lon));

            // Check if hovering over a quake
            for (Earthquake q : quakes) {
                double qx = lonToX(q.getLon());
                double qy = latToY(q.getLat());
                double size = Math.pow(q.getMag(), 1.5) * 2;
                if (Math.abs(e.getX() - qx) < size && Math.abs(e.getY() - qy) < size) {
                    infoLabel.setText(String.format(
                            org.jscience.ui.i18n.I18n.getInstance().get("earthquake.info.format"),
                            q.getMag(), q.getLat(), q.getLon(), q.getDepth()));
                    return;
                }
            }
            infoLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.hover"));
        });

        class DragContext {
            double x, y;
        }
        DragContext drag = new DragContext();

        canvas.setOnMousePressed(e -> {
            drag.x = e.getX();
            drag.y = e.getY();
        });

        canvas.setOnMouseDragged(e -> {
            offsetX += e.getX() - drag.x;
            offsetY += e.getY() - drag.y;
            drag.x = e.getX();
            drag.y = e.getY();
        });

        canvas.setOnScroll(e -> {
            double factor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            zoom *= factor;
            zoom = Math.max(0.5, Math.min(zoom, 5.0));
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw(gc, now);
            }
        }.start();

        Scene scene = new Scene(root, WIDTH + 220, HEIGHT);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.earthquake"));
        stage.setScene(scene);
        stage.show();
    }

    private Label createLegendItem(Color color, String text) {
        Label l = new Label(
                String.format(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.list.fmt"), text));
        l.setTextFill(color);
        l.setFont(Font.font("Arial", 11));
        return l;
    }

    private void generateMockData() {
        // Ring of Fire approximation
        addQuakeCluster(35, -120, 50, 5.0, 15); // West Coast Americas
        addQuakeCluster(-30, -70, 30, 6.0, 50); // Chile
        addQuakeCluster(36, 138, 40, 5.5, 20); // Japan
        addQuakeCluster(-5, 120, 30, 6.5, 30); // Indonesia
        addQuakeCluster(40, 30, 20, 5.0, 10); // Turkey
        addQuakeCluster(-40, 175, 25, 5.5, 25); // New Zealand

        // Random global
        Random r = new Random(42);
        for (int i = 0; i < 50; i++) {
            quakes.add(new Earthquake(
                    (r.nextDouble() * 140) - 70, // Lat -70 to 70
                    (r.nextDouble() * 360) - 180,
                    2.0 + r.nextDouble() * 3,
                    r.nextDouble() * 100));
        }
    }

    private void addQuakeCluster(double lat, double lon, int count, double baseMag, double baseDepth) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            double dLat = r.nextGaussian() * 5;
            double dLon = r.nextGaussian() * 5;
            double mag = baseMag + r.nextGaussian();
            double depth = baseDepth + r.nextGaussian() * 20;
            if (mag < 1)
                mag = 1;
            if (depth < 0)
                depth = 5;
            quakes.add(new Earthquake(lat + dLat, lon + dLon, mag, depth));
        }
    }

    private void draw(GraphicsContext gc, long now) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.web("#0a1929"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw Map Grid
        gc.setStroke(Color.web("#1a3a5c"));
        gc.setLineWidth(0.5);

        // Latitude lines
        for (int lat = -60; lat <= 60; lat += 30) {
            double y = latToY(lat);
            gc.strokeLine(0, y, WIDTH, y);
            gc.setFill(Color.GRAY);
            gc.fillText(lat + "°", 5, y - 2);
        }

        // Longitude lines
        for (int lon = -180; lon <= 180; lon += 30) {
            double x = lonToX(lon);
            gc.strokeLine(x, 0, x, HEIGHT);
            gc.setFill(Color.GRAY);
            gc.fillText(lon + "°", x + 2, HEIGHT - 5);
        }

        // Equator and Prime Meridian (highlighted)
        gc.setStroke(Color.web("#2a5a8c"));
        gc.setLineWidth(1);
        gc.strokeLine(0, latToY(0), WIDTH, latToY(0));
        gc.strokeLine(lonToX(0), 0, lonToX(0), HEIGHT);

        // Draw quakes
        for (Earthquake q : quakes) {
            double x = lonToX(q.getLon());
            double y = latToY(q.getLat());

            double size = Math.pow(q.getMag(), 1.5) * 2 * zoom;

            // Color by magnitude
            Color c;
            if (q.getMag() > 6)
                c = Color.hsb(0, 1, 1, 0.8);
            else if (q.getMag() > 5)
                c = Color.hsb(40, 1, 1, 0.7);
            else if (q.getMag() > 3)
                c = Color.hsb(80, 1, 1, 0.6);
            else
                c = Color.hsb(120, 1, 1, 0.5);

            // Pulse effect for strong quakes
            double pulse = (Math.sin(now / 200_000_000.0 + q.getLat()) + 1) / 2;
            if (q.getMag() > 6)
                size += pulse * 5;

            gc.setFill(c);
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }

        // Scale indicator
        gc.setFill(Color.WHITE);
        gc.fillText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.zoom.format"), zoom), 10,
                20);
        gc.fillText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("earthquake.count.format"),
                quakes.size()), 10, 35);
    }

    private double lonToX(double lon) {
        return ((lon + 180) / 360.0 * WIDTH * zoom) + offsetX;
    }

    private double latToY(double lat) {
        return ((1.0 - (lat + 90) / 180.0) * HEIGHT * zoom) + offsetY;
    }

    private double xToLon(double x) {
        return ((x - offsetX) / (WIDTH * zoom)) * 360.0 - 180;
    }

    private double yToLat(double y) {
        return 90 - ((y - offsetY) / (HEIGHT * zoom)) * 180.0;
    }

    public static void show(Stage stage) {
        new EarthquakeMapViewer().start(stage);
    }
}
