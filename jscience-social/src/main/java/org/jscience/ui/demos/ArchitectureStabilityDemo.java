package org.jscience.ui.demos;

import javafx.scene.Scene;
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
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.architecture");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("ArchitectureStability.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("ArchitectureStability.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(600, 600);
        root.setCenter(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Button addBlockBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.btn.add"));
        Button resetBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.btn.reset"));
        Label status = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.stable"));
        status.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        // State
        class Block {
            double offset;
            double mass;

            public Block(double o, double m) {
                offset = o;
                mass = m;
            }
        }
        class State {
            java.util.List<Block> blocks = new java.util.ArrayList<>();
            double comX = 300; // Center of Mass X
            boolean collapsed = false;
        }
        State s = new State();

        Runnable draw = () -> {
            gc.clearRect(0, 0, 600, 600);

            // Ground
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(0, 550, 600, 50);

            // Base (Table) - Changed Color
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(250, 500, 100, 50);

            double currentY = 500;
            double totalMassX = 300 * 10; // Base mass (10) at 300
            double totalMass = 10;

            for (Block b : s.blocks) {
                currentY -= 50;
                double bx = 250 + b.offset;

                // Optimization: Don't draw if off-screen
                if (currentY > -50 && currentY < 600) {
                    gc.setFill(s.collapsed ? Color.RED : Color.GRAY);
                    gc.setStroke(Color.BLACK);
                    gc.fillRect(bx, currentY, 100, 50);
                    gc.strokeRect(bx, currentY, 100, 50);
                }

                if (!s.collapsed) {
                    totalMassX += (bx + 50) * b.mass;
                    totalMass += b.mass;
                }
            }

            s.comX = totalMassX / totalMass;

            // Draw COM Line
            gc.setStroke(Color.BLUE); // Changed to Blue for visibility
            gc.setLineWidth(2);
            gc.strokeLine(s.comX, 0, s.comX, 600);

            // Text
            gc.setFill(Color.BLACK);
            gc.fillText(
                    String.format(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.com"),
                            s.comX),
                    10, 20);

            // Stability check (Simplified: if COM outside base [250, 350])
            if (s.comX < 250 || s.comX > 350) {
                s.collapsed = true;
                status.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.collapsed"));
                status.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        };

        addBlockBtn.setOnAction(e -> {
            if (s.collapsed)
                return;
            s.blocks.add(new Block((Math.random() - 0.5) * 60, 1.0));
            draw.run();
        });

        resetBtn.setOnAction(e -> {
            s.blocks.clear();
            s.collapsed = false;
            status.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.stable"));
            status.setStyle("-fx-text-fill: green;");
            draw.run();
        });

        VBox controls = new VBox(10, addBlockBtn, resetBtn, status);
        controls.setStyle("-fx-padding: 10;");
        root.setRight(controls);

        draw.run();

        Scene scene = new Scene(root, 800, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
