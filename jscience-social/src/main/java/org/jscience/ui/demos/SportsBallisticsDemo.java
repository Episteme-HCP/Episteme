package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class SportsBallisticsDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Social Sciences"; // Sports section
    }

    @Override
    public String getName() {
        return "Sports: Ballistics Trajectory";
    }

    @Override
    public String getDescription() {
        return "Simulates projectile motion for different sports balls (gravity, drag).";
    }

    private static class Ball {
        double x, y, vx, vy, radius;
        Color color;
        double mass; // kg
        double area; // m^2
        double dragCoeff;
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(800, 500);
        root.setCenter(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        ComboBox<String> sportSelector = new ComboBox<>();
        sportSelector.getItems().addAll("Basketball", "Tennis Ball", "Golf Ball", "Bowling Ball");
        sportSelector.setValue("Basketball");

        Slider angleSlider = new Slider(0, 90, 45);
        Slider powerSlider = new Slider(0, 50, 20);
        Button launchBtn = new Button("Launch");

        VBox controls = new VBox(10, sportSelector, angleSlider, powerSlider, launchBtn);
        root.setRight(controls);

        new AnimationTimer() {
            Ball b = null;
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (launchBtn.isPressed()) { // Simple hack to detect launch event cleanly would be better
                    // Handled by action
                }

                gc.clearRect(0, 0, 800, 500);
                gc.setFill(Color.LIGHTSKYBLUE);
                gc.fillRect(0, 0, 800, 500);
                gc.setFill(Color.GREEN);
                gc.fillRect(0, 480, 800, 20);

                if (b != null) {
                    double dt = 0.016; // fixed step

                    // Physics
                    double speed = Math.sqrt(b.vx * b.vx + b.vy * b.vy);
                    double dragForce = 0.5 * 1.225 * speed * speed * b.dragCoeff * b.area;
                    double fx = -dragForce * (b.vx / speed);
                    double fy = 9.81 * b.mass + dragForce * (b.vy / speed); // Gravity is down (+y)

                    double ax = fx / b.mass;
                    double ay = 9.81; // Simplified gravity down

                    b.vx += ax * dt;
                    b.vy += ay * dt;
                    b.x += b.vx * dt * 10; // Scale pixels
                    b.y += b.vy * dt * 10;

                    gc.setFill(b.color);
                    gc.fillOval(b.x, b.y, b.radius * 2, b.radius * 2);

                    if (b.y > 480) {
                        b.y = 480;
                        b.vy = -b.vy * 0.7; // Bounce
                        if (Math.abs(b.vy) < 1)
                            b = null; // Stop
                    }
                }
            }

            {
                launchBtn.setOnAction(e -> {
                    b = new Ball();
                    b.x = 50;
                    b.y = 400;
                    double angRad = Math.toRadians(-angleSlider.getValue()); // Up is negative
                    double pow = powerSlider.getValue();

                    b.vx = Math.cos(angRad) * pow;
                    b.vy = Math.sin(angRad) * pow;

                    String sport = sportSelector.getValue();
                    switch (sport) {
                        case "Basketball":
                            b.color = Color.ORANGE;
                            b.radius = 15;
                            b.mass = 0.62;
                            b.area = 0.045;
                            b.dragCoeff = 0.47;
                            break;
                        case "Tennis Ball":
                            b.color = Color.YELLOW;
                            b.radius = 5;
                            b.mass = 0.058;
                            b.area = 0.0033;
                            b.dragCoeff = 0.5;
                            break;
                        case "Golf Ball":
                            b.color = Color.WHITE;
                            b.radius = 3;
                            b.mass = 0.046;
                            b.area = 0.0014;
                            b.dragCoeff = 0.3;
                            break; // Dimples help
                        case "Bowling Ball":
                            b.color = Color.BLACK;
                            b.radius = 20;
                            b.mass = 7.0;
                            b.area = 0.07;
                            b.dragCoeff = 0.47;
                            break;
                    }
                });
            }
        }.start();

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
