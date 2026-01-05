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
import org.jscience.ui.AppProvider;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArchitectureStabilityDemo implements AppProvider {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Architecture";
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
        // State using JScience Quantities
        class Block {
            Quantity<Length> offset;
            Quantity<Mass> mass;

            public Block(Quantity<Length> o, Quantity<Mass> m) {
                offset = o;
                mass = m;
            }
        }
        class State {
            java.util.List<Block> blocks = new java.util.ArrayList<>();
            Quantity<Length> comX = Quantities.create(300.0, Units.METER); // Using 1 pixel = 1 meter for simplicity
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

            // Initial Moment: 300m * 10kg
            Quantity<Mass> baseMass = Quantities.create(10.0, Units.KILOGRAM);
            Quantity<Length> basePos = Quantities.create(300.0, Units.METER);

            // Moment = Mass * Position
            // Result is a Quantity<?> since Mass*Length is not a base quantity in our
            // predefined set
            Quantity<?> totalMoment = baseMass.multiply(basePos);
            Quantity<Mass> totalMass = baseMass;

            for (Block b : s.blocks) {
                currentY -= 50;
                double bx = 250 + b.offset.getValue().doubleValue();

                // Drawing logic
                if (currentY > -50 && currentY < 600) {
                    gc.setFill(s.collapsed ? Color.RED : Color.GRAY);
                    gc.setStroke(Color.BLACK);
                    gc.fillRect(bx, currentY, 100, 50);
                    gc.strokeRect(bx, currentY, 100, 50);
                }

                if (!s.collapsed) {
                    // Position of block center: bx + 50
                    Quantity<Length> blockX = Quantities.create(bx + 50, Units.METER);
                    Quantity<?> moment = blockX.multiply(b.mass);
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Quantity<?> newMoment = totalMoment.add((Quantity) moment);
                    totalMoment = newMoment;
                    totalMass = totalMass.add(b.mass);
                }
            }

            // COM = totalMoment / totalMass
            s.comX = totalMoment.divide(totalMass).asType(Length.class);
            double comValue = s.comX.getValue().doubleValue();

            // Draw COM Line
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            gc.strokeLine(comValue, 0, comValue, 600);

            // Text
            gc.setFill(Color.BLACK);
            gc.fillText(
                    String.format(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.com"),
                            comValue),
                    10, 20);

            // Stability check (COM outside base [250, 350])
            if (comValue < 250 || comValue > 350) {
                s.collapsed = true;
                status.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("arch.stability.label.collapsed"));
                status.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }

        };

        addBlockBtn.setOnAction(e -> {
            if (s.collapsed)
                return;
            s.blocks.add(new Block(
                    Quantities.create((Math.random() - 0.5) * 60, Units.METER),
                    Quantities.create(1.0, Units.KILOGRAM)));
            draw.run();
        });

        resetBtn.setOnAction(e -> {
            s.blocks.clear();
            s.collapsed = false;
            s.comX = Quantities.create(300.0, Units.METER);
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
