/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.architecture.traffic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;

import java.util.List;

/**
 * Car Traffic Simulation using Intelligent Driver Model (IDM).
 * Demonstrates phantom traffic jams on a circular track.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CarTrafficViewer extends Application {

    private static final double TRACK_RADIUS = 250;
    private static final double TRACK_LENGTH = 2 * Math.PI * TRACK_RADIUS;

    // IDM Parameters
    private double desiredVelocity = 30.0; // m/s (~108 km/h)
    private double timeGap = 1.5; // s
    private double minGap = 2.0; // m
    private double delta = 4.0;
    private double maxAccel = 1.0; // m/s^2
    private double breakingDecel = 2.0; // m/s^2

    private List<Car> cars = new ArrayList<>();
    private Canvas canvas;

    private Label jamStatusLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f8f8;");

        // --- Sidebar controls ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(300);
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 0 1 0 0;");

        Label title = new Label(I18n.getInstance().get("traffic.label.header"));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label desc = new Label(I18n.getInstance().get("traffic.label.desc"));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #666;");

        // Controls
        Slider densitySlider = new Slider(10, 60, 30);
        Label densityLabel = new Label(I18n.getInstance().get("traffic.label.cars", 30.0));
        densitySlider.valueProperty().addListener((o, old, val) -> {
            densityLabel.setText(I18n.getInstance().get("traffic.label.cars", val));
        });

        Slider speedSlider = new Slider(10, 50, 30);
        Label speedLabel = new Label(I18n.getInstance().get("traffic.label.speed", 30.0));
        speedSlider.valueProperty().addListener((o, old, val) -> {
            desiredVelocity = val.doubleValue();
            speedLabel.setText(I18n.getInstance().get("traffic.label.speed", desiredVelocity));
        });

        Slider gapSlider = new Slider(0.5, 3.0, 1.5);
        Label gapLabel = new Label(I18n.getInstance().get("traffic.label.gap", 1.5));
        gapSlider.valueProperty().addListener((o, old, val) -> {
            timeGap = val.doubleValue();
            gapLabel.setText(I18n.getInstance().get("traffic.label.gap", timeGap));
        });

        Button resetBtn = new Button(I18n.getInstance().get("traffic.button.reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> initCars((int) densitySlider.getValue()));

        Button perturbBtn = new Button(I18n.getInstance().get("traffic.button.perturb"));
        perturbBtn.setMaxWidth(Double.MAX_VALUE);
        perturbBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        perturbBtn.setOnAction(e -> perturbFirstCar());

        jamStatusLabel = new Label(
                I18n.getInstance().get("traffic.label.status", I18n.getInstance().get("traffic.status.free")));
        jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        // Legend
        HBox legendFast = createLegendItem(Color.GREEN, I18n.getInstance().get("traffic.legend.fast"));
        HBox legendSlow = createLegendItem(Color.ORANGE, I18n.getInstance().get("traffic.legend.slow"));
        HBox legendStop = createLegendItem(Color.RED, I18n.getInstance().get("traffic.legend.stop"));

        sidebar.getChildren().addAll(
                title, desc, new Separator(),
                densityLabel, densitySlider,
                speedLabel, speedSlider,
                gapLabel, gapSlider,
                new Separator(),
                resetBtn, perturbBtn,
                new Separator(),
                jamStatusLabel,
                new Label(I18n.getInstance().get("traffic.legend")), legendFast, legendSlow, legendStop);
        root.setRight(sidebar);

        // --- Canvas ---
        Pane canvasContainer = new Pane();
        canvas = new Canvas(800, 800);
        canvasContainer.getChildren().add(canvas);
        root.setCenter(canvasContainer);

        // Resize handling
        canvasContainer.widthProperty().addListener((obs, o, w) -> {
            canvas.setWidth(w.doubleValue());
            draw();
        });
        canvasContainer.heightProperty().addListener((obs, o, h) -> {
            canvas.setHeight(h.doubleValue());
            draw();
        });

        // Initialize
        initCars(30);

        // Loop
        new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double dt = (now - lastTime) / 1e9;
                lastTime = now;
                update(dt);
                draw();
            }
        }.start();

        Scene scene = new Scene(root, 1000, 700);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("traffic.title"));
        stage.setScene(scene);
        stage.show();
    }

    private void initCars(int count) {
        cars.clear();
        double spacing = TRACK_LENGTH / count;
        for (int i = 0; i < count; i++) {
            Car car = new Car();
            car.position = i * spacing;
            car.velocity = desiredVelocity * 0.8; // Start slightly slower
            // Perturb slightly to avoid perfect equilibrium immediately
            car.position += (Math.random() - 0.5) * 2.0;
            cars.add(car);
        }
    }

    private void perturbFirstCar() {
        if (!cars.isEmpty()) {
            cars.get(0).velocity *= 0.1; // Brake hard
        }
    }

    private void update(double dt) {
        if (dt > 0.1)
            dt = 0.1; // Cap dt

        int n = cars.size();
        double[] accelerations = new double[n];

        // Calculate accelerations
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Car leadCar = cars.get((i + 1) % n);

            double dx = leadCar.position - car.position;
            if (dx < 0)
                dx += TRACK_LENGTH; // Wrap around

            double dv = car.velocity - leadCar.velocity;

            // IDM Formula
            double term1 = Math.pow(car.velocity / desiredVelocity, delta);

            double sStar = minGap + car.velocity * timeGap +
                    (car.velocity * dv) / (2 * Math.sqrt(maxAccel * breakingDecel));

            double term2 = Math.pow(sStar / dx, 2);

            accelerations[i] = maxAccel * (1.0 - term1 - term2);
        }

        // Update physics
        double totalSpeed = 0;
        int stoppedCars = 0;

        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            car.velocity += accelerations[i] * dt;
            if (car.velocity < 0)
                car.velocity = 0; // No reversing

            car.position += car.velocity * dt;
            if (car.position > TRACK_LENGTH)
                car.position -= TRACK_LENGTH;

            totalSpeed += car.velocity;
            if (car.velocity < 5.0)
                stoppedCars++;
        }

        // Update labels
        if (n > 0) {
            double avgSpeed = totalSpeed / n;
            if (stoppedCars > n / 3) {
                jamStatusLabel.setText(I18n.getInstance().get("traffic.label.status",
                        I18n.getInstance().get("traffic.status.jammed")));
                jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            } else if (avgSpeed < desiredVelocity * 0.5) {
                jamStatusLabel.setText(I18n.getInstance().get("traffic.label.status",
                        I18n.getInstance().get("traffic.status.congested")));
                jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");
            } else {
                jamStatusLabel.setText(
                        I18n.getInstance().get("traffic.label.status", I18n.getInstance().get("traffic.status.free")));
                jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            }
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;

        gc.setFill(Color.web("#f8f8f8")); // Match bg
        gc.fillRect(0, 0, w, h);

        // Draw Track
        gc.setStroke(Color.web("#e0e0e0"));
        gc.setLineWidth(40);
        gc.strokeOval(cx - TRACK_RADIUS, cy - TRACK_RADIUS, TRACK_RADIUS * 2, TRACK_RADIUS * 2);

        gc.setStroke(Color.web("#ccc"));
        gc.setLineWidth(2);
        gc.strokeOval(cx - TRACK_RADIUS - 20, cy - TRACK_RADIUS - 20, (TRACK_RADIUS + 20) * 2, (TRACK_RADIUS + 20) * 2);
        gc.strokeOval(cx - TRACK_RADIUS + 20, cy - TRACK_RADIUS + 20, (TRACK_RADIUS - 20) * 2, (TRACK_RADIUS - 20) * 2);

        // Draw Cars
        for (Car car : cars) {
            double theta = (car.position / TRACK_LENGTH) * 2 * Math.PI; // 0 to 2PI
            // Adjust theta so 0 is right, increasing clockwise or counter-clockwise?
            // Usually math is CCW. Let's assume position increases CCW.

            double x = cx + TRACK_RADIUS * Math.cos(theta);
            double y = cy + TRACK_RADIUS * Math.sin(theta);

            // Color based on speed
            Color c;
            if (car.velocity < 5.0)
                c = Color.RED;
            else if (car.velocity > 25.0)
                c = Color.GREEN;
            else
                c = Color.ORANGE;

            gc.save();
            gc.translate(x, y);
            // Rotate car to face tangent
            double angleDeg = Math.toDegrees(theta) + 180; // Adjusted for Up-facing sprite
            gc.rotate(angleDeg);

            // Draw Car body
            gc.setFill(c);
            gc.fillRoundRect(-6, -12, 12, 24, 5, 5);

            // Headlights
            gc.setFill(Color.YELLOW);
            gc.fillOval(-5, -12, 3, 3);
            gc.fillOval(2, -12, 3, 3);

            gc.restore();
        }
    }

    private HBox createLegendItem(Color c, String text) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        javafx.scene.shape.Rectangle r = new javafx.scene.shape.Rectangle(15, 10, c);
        r.setArcWidth(3);
        r.setArcHeight(3);
        box.getChildren().addAll(r, new Label(text));
        return box;
    }

    private static class Car {
        double position;
        double velocity;
    }

    public static void show(Stage stage) {
        new CarTrafficViewer().start(stage);
    }
}


