/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

import java.util.ArrayList;
import java.util.List;

import org.jscience.ui.i18n.SocialI18n;
import javafx.geometry.Pos;

/**
 * Car Traffic Simulation Demo.
 * Merged implementation of Viewer and Demo.
 */
public class CarTrafficDemo extends AbstractSimulationDemo {

    // --- Simulation State ---
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
    private boolean running = false;
    private AnimationTimer timer;
    private Label jamStatusLabel;

    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("CarTraffic.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("CarTraffic.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        BorderPane root = new BorderPane();
        canvas = new Canvas(800, 800);
        Pane canvasContainer = new Pane(canvas);
        
        canvasContainer.widthProperty().addListener((o, old, w) -> { canvas.setWidth(w.doubleValue()); draw(); });
        canvasContainer.heightProperty().addListener((o, old, h) -> { canvas.setHeight(h.doubleValue()); draw(); });
        
        root.setCenter(canvasContainer);
        
        initCars(30);
        
        timer = new AnimationTimer() {
             long lastTime = 0;
             @Override
             public void handle(long now) {
                 if (lastTime == 0) { lastTime = now; return; }
                 double dt = (now - lastTime) / 1e9;
                 lastTime = now;
                 if (running) {
                     update(Quantities.create(dt, Units.SECOND));
                     draw();
                 }
             }
        };
        timer.start();
        
        return root;
    }

    @Override
    protected VBox createControlPanel() {
        SocialI18n i18n = SocialI18n.getInstance();
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(300);

        Label title = new Label(i18n.get("traffic.label.header", "Traffic Parameters"));
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Slider densitySlider = new Slider(10, 60, 30);
        Label densityLabel = new Label(i18n.get("traffic.label.cars", "Cars: ") + "30");
        densitySlider.valueProperty().addListener((o, old, val) -> {
            densityLabel.setText(i18n.get("traffic.label.cars", "Cars: ") + val.intValue());
        });

        Slider speedSlider = new Slider(10, 50, 30);
        Label speedLabel = new Label(i18n.get("traffic.label.speed", "Target Speed: ") + "30.0");
        speedSlider.valueProperty().addListener((o, old, val) -> {
            desiredVelocity = Quantities.create(val.doubleValue(), Units.METER_PER_SECOND);
            speedLabel.setText(i18n.get("traffic.label.speed", "Target Speed: ") + String.format("%.1f", val.doubleValue()));
        });

        Slider gapSlider = new Slider(0.5, 3.0, 1.5);
        Label gapLabel = new Label(i18n.get("traffic.label.gap", "Time Gap: ") + "1.5");
        gapSlider.valueProperty().addListener((o, old, val) -> {
            timeGap = Quantities.create(val.doubleValue(), Units.SECOND);
            gapLabel.setText(i18n.get("traffic.label.gap", "Time Gap: ") + String.format("%.1f", val.doubleValue()));
        });

        Button resetBtn = new Button(i18n.get("traffic.button.reset", "Reset Sim"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> initCars((int) densitySlider.getValue()));

        Button perturbBtn = new Button(i18n.get("traffic.button.perturb", "Perturb Traffic"));
        perturbBtn.setMaxWidth(Double.MAX_VALUE);
        perturbBtn.setOnAction(e -> {
            if (!cars.isEmpty()) {
                cars.get(0).velocity = cars.get(0).velocity.multiply(0.1).asType(Velocity.class);
            }
        });

        jamStatusLabel = new Label(i18n.get("traffic.label.status", "Status: ") + i18n.get("traffic.status.free", "Free"));
        jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        VBox legend = new VBox(5,
            new Label(i18n.get("traffic.legend", "Legend:")),
            createLegendItem(Color.GREEN, i18n.get("traffic.legend.fast", "Fast")),
            createLegendItem(Color.ORANGE, i18n.get("traffic.legend.slow", "Slow")),
            createLegendItem(Color.RED, i18n.get("traffic.legend.stop", "Stopped"))
        );

        panel.getChildren().addAll(title, new Separator(), densityLabel, densitySlider, speedLabel, speedSlider,
                gapLabel, gapSlider, new Separator(), resetBtn, perturbBtn, new Separator(), jamStatusLabel, legend);
        return panel;
    }

    private HBox createLegendItem(Color c, String text) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(new javafx.scene.shape.Rectangle(15, 10, c), new Label(text));
        return box;
    }
    
    // --- Simulatable Implementation ---
    @Override
    public void play() { running = true; }
    
    @Override
    public void pause() { running = false; }
    
    @Override
    public void stop() { running = false; initCars(30); draw(); }

    @Override
    public void step() {
        if (!running) {
             update(Quantities.create(0.1, Units.SECOND));
             draw();
        }
    }

    @Override
    public void setSpeed(double multiplier) {
        // Optional: adjust simulation speed
    }

    @Override
    public boolean isPlaying() {
        return running;
    }

    private void initCars(int count) {
        cars.clear();
        Quantity<Length> spacing = TRACK_LENGTH.divide(count).asType(Length.class);
        for (int i = 0; i < count; i++) {
            Car car = new Car();
            car.position = spacing.multiply(i).asType(Length.class);
            car.velocity = desiredVelocity.multiply(0.8).asType(Velocity.class);
            car.position = car.position.add(Quantities.create((Math.random() - 0.5) * 2.0, Units.METER))
                    .asType(Length.class);
            cars.add(car);
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
            Quantity<Length> sStar = minGap.add(car.velocity.multiply(timeGap).asType(Length.class))
                    .add(moment.divide(sqrtAB.multiply(2.0)).asType(Length.class));
            double sRatio = sStar.divide(dx).getValue().doubleValue();
            accels.add((Quantity<Acceleration>) maxAccel.multiply(1.0 - term1 - (sRatio * sRatio)));
        }
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            car.velocity = car.velocity.add(accels.get(i).multiply(dt).asType(Velocity.class));
            if (car.velocity.getValue().doubleValue() < 0) car.velocity = Quantities.create(0.0, Units.METER_PER_SECOND);
            car.position = car.position.add(car.velocity.multiply(dt).asType(Length.class));
            if (car.position.getValue().doubleValue() > TRACK_LENGTH.getValue().doubleValue())
                car.position = car.position.subtract(TRACK_LENGTH).asType(Length.class);
        }

        updateStatusLabel();
    }

    private void updateStatusLabel() {
        if (jamStatusLabel == null || cars.isEmpty()) return;
        SocialI18n i18n = SocialI18n.getInstance();
        double totalSpeed = 0;
        int stoppedCars = 0;
        for (Car car : cars) {
            double v = car.velocity.getValue().doubleValue();
            totalSpeed += v;
            if (v < 5.0) stoppedCars++;
        }
        double avgSpeed = totalSpeed / cars.size();
        if (stoppedCars > cars.size() / 3) {
            jamStatusLabel.setText(i18n.get("traffic.label.status", "Status: ") + i18n.get("traffic.status.jammed", "Jammed"));
            jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        } else if (avgSpeed < desiredVelocity.getValue().doubleValue() * 0.5) {
            jamStatusLabel.setText(i18n.get("traffic.label.status", "Status: ") + i18n.get("traffic.status.congested", "Congested"));
            jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");
        } else {
            jamStatusLabel.setText(i18n.get("traffic.label.status", "Status: ") + i18n.get("traffic.status.free", "Free"));
            jamStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        }
    }

    private void draw() {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        gc.clearRect(0, 0, w, h);
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

    @Override
    protected String getLongDescription() {
         return getDescription();
    }

    private static class Car {
        Quantity<Length> position;
        Quantity<Velocity> velocity;
    }
}
