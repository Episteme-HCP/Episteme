package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class ArchitectureStabilityDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Social Sciences";
    }

    @Override
    public String getName() {
        return "Architecture: Structural Stability";
    }

    @Override
    public String getDescription() {
        return "Visual demonstration of center of mass and stability in a tower.";
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(600, 600);
        root.setCenter(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Button addBlockBtn = new Button("Add Block (Random Offset)");
        Button resetBtn = new Button("Reset");
        Label status = new Label("Stable");
        status.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        // State
        class State {
            int blocks = 0;
            double topX = 300;
            double comX = 300; // Center of Mass X
            boolean collapsed = false;
            java.util.List<Double> offsets = new java.util.ArrayList<>();
        }
        State s = new State();

        Runnable draw = () -> {
            gc.clearRect(0, 0, 600, 600);

            // Ground
            gc.setFill(Color.BROWN);
            gc.fillRect(0, 550, 600, 50);

            // Base
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(250, 500, 100, 50);

            double currentY = 500;
            double totalMassX = 300 * 10; // Base mass
            double totalMass = 10;

            for (int i = 0; i < s.blocks; i++) {
                currentY -= 50;
                double off = s.offsets.get(i);
                double bx = 250 + off;

                gc.setFill(s.collapsed ? Color.RED : Color.GRAY);
                gc.setStroke(Color.BLACK);
                gc.fillRect(bx, currentY, 100, 50);
                gc.strokeRect(bx, currentY, 100, 50);

                if (!s.collapsed) {
                    totalMassX += (bx + 50) * 1;
                    totalMass += 1;
                }
            }

            s.comX = totalMassX / totalMass;

            // Draw COM Line
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(s.comX, 0, s.comX, 600);

            // Text
            gc.setFill(Color.BLACK);
            gc.fillText("Center of Mass: " + String.format("%.2f", s.comX), 10, 20);

            // Stability check (Simplified: if COM outside base [250, 350])
            if (s.comX < 250 || s.comX > 350) {
                s.collapsed = true;
                status.setText("COLLAPSED!");
                status.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        };

        addBlockBtn.setOnAction(e -> {
            if (s.collapsed)
                return;
            s.blocks++;
            s.offsets.add((Math.random() - 0.5) * 60); // Random shift -30 to +30
            draw.run();
        });

        resetBtn.setOnAction(e -> {
            s.blocks = 0;
            s.offsets.clear();
            s.collapsed = false;
            status.setText("Stable");
            status.setStyle("-fx-text-fill: green;");
            draw.run();
        });

        VBox controls = new VBox(10, addBlockBtn, resetBtn, status);
        controls.setStyle("-fx-padding: 10;");
        root.setRight(controls);

        draw.run();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
