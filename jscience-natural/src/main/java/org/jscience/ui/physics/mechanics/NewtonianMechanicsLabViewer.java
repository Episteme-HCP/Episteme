/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.physics.mechanics;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;

/**
 * Newtonian Mechanics Lab - A tabbed collection of physics experiments.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class NewtonianMechanicsLabViewer {

    public static void show(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                createSpringTab(),
                createPendulumTab(),
                createGravityTab(),
                createCollisionTab());

        BorderPane root = new BorderPane(tabPane);
        root.setStyle("-fx-background-color: #f5f5f5;");

        Scene scene = new Scene(root, 900, 700);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("mechanics.lab.title"));
        stage.setScene(scene);
        stage.show();
    }

    // ==== TAB 1: Spring Oscillator ====
    private static Tab createSpringTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.spring.tab"));

        Canvas canvas = new Canvas(600, 500);
        VBox controls = createSpringControls(canvas);

        HBox layout = new HBox(20, canvas, controls);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        tab.setContent(layout);

        return tab;
    }

    private static VBox createSpringControls(Canvas canvas) {
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #eee; -fx-background-radius: 8;");
        controls.setPrefWidth(250);

        Label title = new Label("Spring Oscillator");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Parameters
        final double[] mass = { 2.0 };
        final double[] k = { 5.0 }; // Slower default (was 10.0)
        final double[] damping = { 0.1 };
        final double[] position = { 100.0 };
        final double[] velocity = { 0.0 };

        Slider massSlider = labeledSlider("Mass (kg)", 0.5, 10, mass[0], v -> mass[0] = v);
        Slider kSlider = labeledSlider("Spring k (N/m)", 1, 50, k[0], v -> k[0] = v);
        Slider dampSlider = labeledSlider("Damping", 0, 1, damping[0], v -> damping[0] = v);

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> {
            position[0] = 100;
            velocity[0] = 0;
        });

        Label energyLabel = new Label("Energy: 0 J");

        // Phase Diagram
        Canvas phaseCanvas = new Canvas(200, 200);
        List<Point2D> phaseTrail = new ArrayList<>();

        controls.getChildren().addAll(title, new Separator(),
                massSlider.getParent(), kSlider.getParent(), dampSlider.getParent(),
                resetBtn, energyLabel, new Separator(), new Label("Phase Space (V vs X)"), phaseCanvas);

        // Animation
        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                // Physics
                double force = -k[0] * position[0] - damping[0] * velocity[0];
                double accel = force / mass[0];
                velocity[0] += accel * dt * 60;
                position[0] += velocity[0] * dt * 60;

                // Energy
                double pe = 0.5 * k[0] * position[0] * position[0] / 10000; // scale
                double ke = 0.5 * mass[0] * velocity[0] * velocity[0] / 10000;
                energyLabel.setText(String.format("PE: %.2f J  KE: %.2f J", pe, ke));

                drawSpring(canvas, position[0], mass[0]);

                // Update Phase Trail
                if (frame % 5 == 0) { // Sample every 5th frame
                    phaseTrail.add(new Point2D(position[0], velocity[0]));
                    if (phaseTrail.size() > 200)
                        phaseTrail.remove(0);
                }
                NewtonianMechanicsLabViewer.drawPhaseplot(phaseCanvas, phaseTrail, position[0], velocity[0], 150, 10);
            }

            int frame = 0;
        }.start();

        return controls;
    }

    private static void drawSpring(Canvas canvas, double pos, double mass) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        double cx = w / 2;
        double anchorY = 50;
        double eqY = 250;
        double massY = eqY + pos;

        // Ceiling
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(4);
        gc.strokeLine(cx - 60, anchorY, cx + 60, anchorY);

        // Spring zigzag
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        int segs = 12;
        double segH = (massY - anchorY) / segs;
        double zw = 20;
        double px = cx, py = anchorY;
        for (int i = 0; i < segs; i++) {
            double ny = py + segH;
            double nx = cx + ((i % 2 == 0) ? zw : -zw);
            if (i == segs - 1)
                nx = cx;
            gc.strokeLine(px, py, nx, ny);
            px = nx;
            py = ny;
        }

        // Mass block
        gc.setFill(Color.STEELBLUE);
        gc.fillRoundRect(cx - 30, massY, 60, 50, 8, 8);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(14));
        gc.fillText(String.format("%.1f kg", mass), cx - 20, massY + 30);

        // Equilibrium line
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineDashes(5);
        gc.strokeLine(cx - 80, eqY, cx + 80, eqY);
        gc.setLineDashes();
    }

    // ==== TAB 2: Simple Pendulum ====
    private static Tab createPendulumTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.pendulum.tab"));

        Canvas canvas = new Canvas(600, 500);
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #eee; -fx-background-radius: 8;");
        controls.setPrefWidth(250);

        Label title = new Label("Simple Pendulum");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        final double[] length = { 200.0 }; // pixels
        final double[] angle = { Math.PI / 4 }; // radians
        final double[] angVel = { 0 };
        final double g = 9.81;

        Slider lenSlider = labeledSlider("Length (px)", 100, 300, length[0], v -> length[0] = v);

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> {
            angle[0] = Math.PI / 4;
            angVel[0] = 0;
        });

        Label periodLabel = new Label("Period: ");

        // Phase Diagram
        Canvas phaseCanvas = new Canvas(200, 200);
        List<Point2D> phaseTrail = new ArrayList<>();

        controls.getChildren().addAll(title, new Separator(), lenSlider.getParent(), resetBtn, periodLabel,
                new Separator(), new Label("Phase Space (Ω vs θ)"), phaseCanvas);

        HBox layout = new HBox(20, canvas, controls);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        tab.setContent(layout);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                // Simple pendulum: theta'' = -(g/L) * sin(theta)
                double L = length[0] / 100.0; // convert to meters-ish
                double angAccel = -(g / L) * Math.sin(angle[0]);
                angVel[0] += angAccel * dt;
                angle[0] += angVel[0] * dt;

                // Damping
                angVel[0] *= 0.999;

                double T = 2 * Math.PI * Math.sqrt(L / g);
                periodLabel.setText(String.format("Period: %.2f s", T));

                drawPendulum(canvas, length[0], angle[0]);

                if (frame++ % 5 == 0) {
                    phaseTrail.add(new Point2D(angle[0], angVel[0]));
                    if (phaseTrail.size() > 200)
                        phaseTrail.remove(0);
                }
                // Scale: Angle +/- 1 rad, Vel +/- 5 rad/s
                NewtonianMechanicsLabViewer.drawPhaseplot(phaseCanvas, phaseTrail, angle[0], angVel[0], 1.5, 3.0);
            }

            int frame = 0;
        }.start();

        return tab;
    }

    private static void drawPendulum(Canvas canvas, double len, double angle) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        double pivotX = w / 2, pivotY = 80;
        double bobX = pivotX + len * Math.sin(angle);
        double bobY = pivotY + len * Math.cos(angle);

        // Pivot
        gc.setFill(Color.DARKGRAY);
        gc.fillOval(pivotX - 8, pivotY - 8, 16, 16);

        // Rod
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);
        gc.strokeLine(pivotX, pivotY, bobX, bobY);

        // Bob
        gc.setFill(Color.CRIMSON);
        gc.fillOval(bobX - 20, bobY - 20, 40, 40);
    }

    // ==== TAB 3: Free Fall / Gravity ====
    private static Tab createGravityTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.gravity.tab"));

        Canvas canvas = new Canvas(600, 500);
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #eee; -fx-background-radius: 8;");
        controls.setPrefWidth(250);

        Label title = new Label("Free Fall");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        final double[] y = { 50.0 };
        final double[] vy = { 0.0 };
        final double[] time = { 0.0 };
        final boolean[] running = { false };

        Label timeLabel = new Label("Time: 0.00 s");
        Label velLabel = new Label("Velocity: 0.0 m/s");

        Button dropBtn = new Button("Drop");
        dropBtn.setOnAction(e -> {
            running[0] = true;
            y[0] = 50;
            vy[0] = 0;
            time[0] = 0;
        });
        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> {
            running[0] = false;
            y[0] = 50;
            vy[0] = 0;
            time[0] = 0;
        });

        controls.getChildren().addAll(title, new Separator(), dropBtn, resetBtn, timeLabel, velLabel);

        HBox layout = new HBox(20, canvas, controls);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        tab.setContent(layout);

        final double g = 9.81 * 30; // scaled
        final double floor = 420;

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                if (running[0] && y[0] < floor) {
                    vy[0] += g * dt;
                    y[0] += vy[0] * dt;
                    time[0] += dt;
                    if (y[0] >= floor) {
                        y[0] = floor;
                        running[0] = false;
                    }
                }

                timeLabel.setText(String.format("Time: %.2f s", time[0]));
                velLabel.setText(String.format("Velocity: %.1f m/s", vy[0] / 30));
                drawGravity(canvas, y[0], floor);
            }
        }.start();

        return tab;
    }

    private static void drawGravity(Canvas canvas, double y, double floor) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        double cx = w / 2;

        // Floor
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, floor + 30, w, h - floor - 30);

        // Ball
        gc.setFill(Color.ORANGE);
        gc.fillOval(cx - 25, y, 50, 50);

        // Height ruler
        gc.setStroke(Color.GRAY);
        gc.strokeLine(100, 50, 100, floor + 30);
        for (int i = 0; i <= 10; i++) {
            double ry = 50 + (floor - 20) * i / 10.0;
            gc.strokeLine(95, ry, 105, ry);
        }
    }

    // ==== TAB 4: 1D Collision ====
    private static Tab createCollisionTab() {
        Tab tab = new Tab(I18n.getInstance().get("mechanics.collision.tab"));

        Canvas canvas = new Canvas(700, 300);
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #eee; -fx-background-radius: 8;");
        controls.setPrefWidth(250);

        Label title = new Label("1D Elastic Collision");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        final double[] m1 = { 2.0 }, m2 = { 1.0 };
        final double[] v1 = { 3.0 }, v2 = { -1.0 };
        final double[] x1 = { 100 }, x2 = { 400 };
        final boolean[] collided = { false };

        Slider m1Slider = labeledSlider("Mass 1 (kg)", 0.5, 5, m1[0], v -> m1[0] = v);
        Slider m2Slider = labeledSlider("Mass 2 (kg)", 0.5, 5, m2[0], v -> m2[0] = v);
        Slider v1Slider = labeledSlider("v1 (m/s)", -5, 5, v1[0], v -> v1[0] = v);
        Slider v2Slider = labeledSlider("v2 (m/s)", -5, 5, v2[0], v -> v2[0] = v);

        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
            x1[0] = 100;
            x2[0] = 400;
            collided[0] = false;
        });

        Label momentumLabel = new Label("Momentum: ");

        controls.getChildren().addAll(title, new Separator(),
                m1Slider.getParent(), v1Slider.getParent(),
                m2Slider.getParent(), v2Slider.getParent(),
                startBtn, momentumLabel);

        VBox layout = new VBox(20, canvas, new HBox(20, controls));
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        tab.setContent(layout);

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                x1[0] += v1[0] * dt * 50;
                x2[0] += v2[0] * dt * 50;

                // Collision detection
                // Collision detection
                double r1 = 20 + m1[0] * 5;
                double r2 = 20 + m2[0] * 5;
                double dist = Math.abs(x1[0] - x2[0]);
                boolean touching = dist < (r1 + r2);

                if (!collided[0] && touching) {
                    // Elastic collision formulas
                    double newV1 = ((m1[0] - m2[0]) * v1[0] + 2 * m2[0] * v2[0]) / (m1[0] + m2[0]);
                    double newV2 = ((m2[0] - m1[0]) * v2[0] + 2 * m1[0] * v1[0]) / (m1[0] + m2[0]);
                    v1[0] = newV1;
                    v2[0] = newV2;
                    collided[0] = true;
                } else if (collided[0] && !touching) {
                    collided[0] = false; // Reset when separated
                }

                // Reset if out of bounds
                if (x1[0] > 800 || x2[0] < -100) {
                    collided[0] = false;
                }

                double p = m1[0] * v1[0] + m2[0] * v2[0];
                momentumLabel.setText(String.format("Total Momentum: %.2f kg·m/s", p));
                drawCollision(canvas, x1[0], x2[0], m1[0], m2[0]);
            }
        }.start();

        return tab;
    }

    private static void drawCollision(Canvas canvas, double x1, double x2, double m1, double m2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        double y = h / 2;

        // Track
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(2);
        gc.strokeLine(0, y + 30, w, y + 30);

        // Ball 1
        double r1 = 20 + m1 * 5;
        gc.setFill(Color.STEELBLUE);
        gc.fillOval(x1 - r1, y - r1, r1 * 2, r1 * 2);
        gc.setFill(Color.WHITE);
        gc.fillText("1", x1 - 5, y + 5);

        // Ball 2
        double r2 = 20 + m2 * 5;
        gc.setFill(Color.CORAL);
        gc.fillOval(x2 - r2, y - r2, r2 * 2, r2 * 2);
        gc.setFill(Color.WHITE);
        gc.fillText("2", x2 - 5, y + 5);
    }

    // ==== Helper ====
    private static Slider labeledSlider(String name, double min, double max, double val,
            java.util.function.DoubleConsumer action) {
        VBox box = new VBox(2);
        Label label = new Label(String.format("%s: %.1f", name, val));
        Slider slider = new Slider(min, max, val);
        slider.valueProperty().addListener((o, ov, nv) -> {
            action.accept(nv.doubleValue());
            label.setText(String.format("%s: %.1f", name, nv.doubleValue()));
        });
        box.getChildren().addAll(label, slider);
        slider.setUserData(box);
        return slider;
    }

    private static void drawPhaseplot(Canvas canvas, List<Point2D> trail, double x, double v, double scaleX,
            double scaleV) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        // Axis
        gc.setStroke(Color.DARKGRAY);
        gc.strokeLine(0, cy, w, cy); // x-axis
        gc.strokeLine(cx, 0, cx, h); // y-axis

        // Trail
        gc.setStroke(Color.LIME);
        gc.setLineWidth(1);
        gc.beginPath();
        boolean first = true;
        for (Point2D p : trail) {
            double px = cx + (p.getX() / scaleX) * (w / 2 * 0.8);
            double py = cy - (p.getY() / scaleV) * (h / 2 * 0.8);
            if (first) {
                gc.moveTo(px, py);
                first = false;
            } else
                gc.lineTo(px, py);
        }
        gc.stroke();

        // Current Point
        double curX = cx + (x / scaleX) * (w / 2 * 0.8);
        double curY = cy - (v / scaleV) * (h / 2 * 0.8);
        gc.setFill(Color.RED);
        gc.fillOval(curX - 3, curY - 3, 6, 6);
    }
}
