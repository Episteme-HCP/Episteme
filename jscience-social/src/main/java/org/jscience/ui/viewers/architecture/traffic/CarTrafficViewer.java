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

package org.jscience.ui.viewers.architecture.traffic;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Car Traffic Simulation using Intelligent Driver Model (IDM).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CarTrafficViewer extends AbstractViewer implements Simulatable {

    private static final double TRACK_RADIUS_PIXELS = 250;
    private static final Quantity<Length> TRACK_LENGTH = Quantities.create(2 * Math.PI * TRACK_RADIUS_PIXELS, Units.METER);

    private Quantity<Velocity> desiredVelocity = Quantities.create(30.0, Units.METER_PER_SECOND);
    private Quantity<Time> timeGap = Quantities.create(1.5, Units.SECOND);
    private Quantity<Length> minGap = Quantities.create(2.0, Units.METER);
    private double delta = 4.0;
    private Quantity<Acceleration> maxAccel = Quantities.create(1.0, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> breakingDecel = Quantities.create(2.0, Units.METERS_PER_SECOND_SQUARED);

    private List<Car> cars = new ArrayList<>();
    private Canvas canvas;
    private Label jamStatusLabel;
    private boolean running = true;
    private AnimationTimer timer;
    private double speed = 1.0;

    @Override
    public String getName() { return I18n.getInstance().get("traffic.title", "Traffic Simulation"); }
    
    @Override
    public String getCategory() { return "Social"; }

    public CarTrafficViewer() {
        initUI();
    }

    private void initUI() {
        this.setStyle("-fx-background-color: #f8f8f8;");

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(300);
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 0 1 0 0;");

        Label title = new Label(I18n.getInstance().get("traffic.label.header", "Traffic Simulation"));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label desc = new Label(I18n.getInstance().get("traffic.label.desc", "Intelligent Driver Model simulation"));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #666;");

        Slider densitySlider = new Slider(10, 60, 30);
        Label densityLabel = new Label(String.format("Cars: %.0f", 30.0));
        densitySlider.valueProperty().addListener((o, old, val) -> densityLabel.setText(String.format("Cars: %.0f", val.doubleValue())));

        Slider speedSlider = new Slider(10, 50, 30);
        Label speedLabel = new Label(String.format("Target Speed: %.0f m/s", 30.0));
        speedSlider.valueProperty().addListener((o, old, val) -> {
            desiredVelocity = Quantities.create(val.doubleValue(), Units.METER_PER_SECOND);
            speedLabel.setText(String.format("Target Speed: %.0f m/s", val.doubleValue()));
        });

        Slider gapSlider = new Slider(0.5, 3.0, 1.5);
        Label gapLabel = new Label(String.format("Time Gap: %.1f s", 1.5));
        gapSlider.valueProperty().addListener((o, old, val) -> {
            timeGap = Quantities.create(val.doubleValue(), Units.SECOND);
            gapLabel.setText(String.format("Time Gap: %.1f s", val.doubleValue()));
        });

        Button resetBtn = new Button(I18n.getInstance().get("traffic.button.reset", "Reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        resetBtn.setOnAction(e -> initCars((int) densitySlider.getValue()));

        Button perturbBtn = new Button(I18n.getInstance().get("traffic.button.perturb", "Perturb"));
        perturbBtn.setMaxWidth(Double.MAX_VALUE);
        perturbBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        perturbBtn.setOnAction(e -> perturbFirstCar());

        jamStatusLabel = new Label("Status: Free Flow");
        jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        HBox legendFast = createLegendItem(Color.GREEN, "Fast (>25 m/s)");
        HBox legendSlow = createLegendItem(Color.ORANGE, "Slow");
        HBox legendStop = createLegendItem(Color.RED, "Stopped (<5 m/s)");

        sidebar.getChildren().addAll(title, desc, new Separator(), densityLabel, densitySlider, speedLabel, speedSlider, gapLabel, gapSlider, new Separator(), resetBtn, perturbBtn, new Separator(), jamStatusLabel, new Label("Legend:"), legendFast, legendSlow, legendStop);
        this.setRight(sidebar);

        Pane canvasContainer = new Pane();
        canvas = new Canvas(600, 600);
        canvasContainer.getChildren().add(canvas);
        this.setCenter(canvasContainer);

        canvasContainer.widthProperty().addListener((obs, o, w) -> { canvas.setWidth(w.doubleValue()); draw(); });
        canvasContainer.heightProperty().addListener((obs, o, h) -> { canvas.setHeight(h.doubleValue()); draw(); });

        initCars(30);

        timer = new AnimationTimer() {
            long lastTime = 0;
            @Override
            public void handle(long now) {
                if (!running) return;
                if (lastTime == 0) { lastTime = now; return; }
                double dt = (now - lastTime) / 1e9 * speed;
                lastTime = now;
                update(Quantities.create(dt, Units.SECOND));
                draw();
            }
        };
        timer.start();
    }

    @Override public void play() { running = true; }
    @Override public void pause() { running = false; }
    @Override public void stop() { running = false; initCars(30); }
    @Override public boolean isPlaying() { return running; }
    @Override public void setSpeed(double s) { this.speed = s; }
    @Override public void step() { update(Quantities.create(0.05, Units.SECOND)); draw(); }

    private void initCars(int count) {
        cars.clear();
        Quantity<Length> spacing = TRACK_LENGTH.divide(count).asType(Length.class);
        for (int i = 0; i < count; i++) {
            Car car = new Car();
            car.position = spacing.multiply(i).asType(Length.class);
            car.velocity = desiredVelocity.multiply(0.8).asType(Velocity.class);
            car.position = car.position.add(Quantities.create((Math.random() - 0.5) * 2.0, Units.METER)).asType(Length.class);
            cars.add(car);
        }
    }

    private void perturbFirstCar() {
        if (!cars.isEmpty()) {
            cars.get(0).velocity = cars.get(0).velocity.multiply(0.1).asType(Velocity.class);
        }
    }

    private void update(Quantity<Time> dt) {
        if (dt.getValue().doubleValue() > 0.1) dt = Quantities.create(0.1, Units.SECOND);

        int n = cars.size();
        List<Quantity<Acceleration>> accels = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Car leadCar = cars.get((i + 1) % n);

            Quantity<Length> dx = leadCar.position.subtract(car.position).asType(Length.class);
            if (dx.getValue().doubleValue() < 0) dx = dx.add(TRACK_LENGTH).asType(Length.class);

            Quantity<Velocity> dv = car.velocity.subtract(leadCar.velocity).asType(Velocity.class);

            double vRatio = car.velocity.divide(desiredVelocity).getValue().doubleValue();
            double term1 = Math.pow(vRatio, delta);

            Quantity<?> moment = car.velocity.multiply(dv);
            Quantity<?> sqrtAB = maxAccel.multiply(breakingDecel).sqrt();
            Quantity<Length> velocityComponent = car.velocity.multiply(timeGap).asType(Length.class);
            Quantity<Length> breakComponent = moment.divide(sqrtAB.multiply(2.0)).asType(Length.class);
            Quantity<Length> sStar = minGap.add(velocityComponent).add(breakComponent).asType(Length.class);

            double sRatio = sStar.divide(dx).getValue().doubleValue();
            double term2 = sRatio * sRatio;

            accels.add((Quantity<Acceleration>) maxAccel.multiply(1.0 - term1 - term2));
        }

        double totalSpeed = 0;
        int stoppedCars = 0;

        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Quantity<Velocity> velocityChange = accels.get(i).multiply(dt).asType(Velocity.class);
            car.velocity = car.velocity.add(velocityChange);
            if (car.velocity.getValue().doubleValue() < 0) car.velocity = Quantities.create(0.0, Units.METER_PER_SECOND);

            Quantity<Length> distanceChange = car.velocity.multiply(dt).asType(Length.class);
            car.position = car.position.add(distanceChange);
            if (car.position.getValue().doubleValue() > TRACK_LENGTH.getValue().doubleValue()) {
                car.position = car.position.subtract(TRACK_LENGTH).asType(Length.class);
            }

            double vVal = car.velocity.getValue().doubleValue();
            totalSpeed += vVal;
            if (vVal < 5.0) stoppedCars++;
        }

        if (n > 0) {
            double avgSpeed = totalSpeed / n;
            if (stoppedCars > n / 3) {
                jamStatusLabel.setText("Status: JAMMED");
                jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            } else if (avgSpeed < desiredVelocity.getValue().doubleValue() * 0.5) {
                jamStatusLabel.setText("Status: Congested");
                jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");
            } else {
                jamStatusLabel.setText("Status: Free Flow");
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

        gc.setFill(Color.web("#f8f8f8"));
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.web("#e0e0e0"));
        gc.setLineWidth(40);
        gc.strokeOval(cx - TRACK_RADIUS_PIXELS, cy - TRACK_RADIUS_PIXELS, TRACK_RADIUS_PIXELS * 2, TRACK_RADIUS_PIXELS * 2);

        for (Car car : cars) {
            double posRatio = car.position.divide(TRACK_LENGTH).getValue().doubleValue();
            double theta = posRatio * 2 * Math.PI;

            double x = cx + TRACK_RADIUS_PIXELS * Math.cos(theta);
            double y = cy + TRACK_RADIUS_PIXELS * Math.sin(theta);

            double vVal = car.velocity.getValue().doubleValue();
            Color c = (vVal < 5.0) ? Color.RED : (vVal > 25.0 ? Color.GREEN : Color.ORANGE);

            gc.save();
            gc.translate(x, y);
            gc.rotate(Math.toDegrees(theta) + 180);
            gc.setFill(c);
            gc.fillRoundRect(-6, -12, 12, 24, 5, 5);
            gc.restore();
        }
    }

    private HBox createLegendItem(Color c, String text) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(new javafx.scene.shape.Rectangle(15, 10, c), new Label(text));
        return box;
    }

    private static class Car {
        Quantity<Length> position;
        Quantity<Velocity> velocity;
    }
}
