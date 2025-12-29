/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.chaos;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.i18n.I18n;
// import org.jscience.ui.ThemeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Lorenz Attractor Chaos Theory Visualization.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class LorenzViewer extends Application {

    private double x = 0.1, y = 0, z = 0;
    private double sigma = 10, rho = 28, beta = 8.0 / 3.0;
    private List<Point3D> points = new ArrayList<>();
    private Canvas canvas;

    private static class Point3D {
        double x, z;

        Point3D(double x, double y, double z) {
            this.x = x;
            this.z = z;
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        // Use light background instead of black
        root.setStyle("-fx-background-color: #f8f8f8;");

        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #eee;");

        Label title = new Label(I18n.getInstance().get("lorenz.title"));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        sidebar.getChildren().addAll(title, new Separator(),
                createSlider(I18n.getInstance().get("lorenz.param.sigma"), 0, 50, sigma, v -> sigma = v),
                createSlider(I18n.getInstance().get("lorenz.param.rho"), 0, 100, rho, v -> rho = v),
                createSlider(I18n.getInstance().get("lorenz.param.beta"), 0, 10, beta, v -> beta = v));
        root.setRight(sidebar);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                step();
                render();
            }
        }.start();

        Scene scene = new Scene(root, 1050, 650);
        stage.setTitle(I18n.getInstance().get("viewer.lorenz"));
        stage.setScene(scene);
        // ThemeManager.getInstance().applyTheme(scene); // User requested light
        // background
        stage.show();
    }

    private void step() {
        double dt = 0.01;
        double dx = (sigma * (y - x)) * dt;
        double dy = (x * (rho - z) - y) * dt;
        double dz = (x * y - beta * z) * dt;

        x += dx;
        y += dy;
        z += dz;

        points.add(new Point3D(x, y, z));
        if (points.size() > 2000)
            points.remove(0);
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 800, 600);

        gc.setLineWidth(1.0);
        double centerX = 400, centerY = 300, scale = 10.0;

        for (int i = 1; i < points.size(); i++) {
            Point3D p1 = points.get(i - 1);
            Point3D p2 = points.get(i);

            // Project 3D to 2D (Rotation omitted for simplicity)
            double x1 = centerX + p1.x * scale;
            double y1 = centerY - p1.z * scale + 250;
            double x2 = centerX + p2.x * scale;
            double y2 = centerY - p2.z * scale + 250;

            gc.setStroke(Color.hsb((i / (double) 2000) * 360, 0.8, 1.0));
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private VBox createSlider(String name, double min, double max, double val, java.util.function.DoubleConsumer c) {
        Label l = new Label(name);
        Slider s = new Slider(min, max, val);
        s.valueProperty().addListener((o, ov, nv) -> {
            c.accept(nv.doubleValue());
            points.clear(); // Restart attractor on param change
        });
        return new VBox(5, l, s);
    }

    public static void show(Stage stage) {
        new LorenzViewer().start(stage);
    }
}
