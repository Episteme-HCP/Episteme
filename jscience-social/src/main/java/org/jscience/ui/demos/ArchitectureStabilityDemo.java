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

package org.jscience.ui.demos;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.ui.i18n.SocialI18n;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureStabilityDemo extends AbstractSimulationDemo {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.architecture");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.architecturestabilitydemo.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.architecturestabilitydemo.desc");
    }

    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;
    private List<Block> blocks = new ArrayList<>();
    private Quantity<Length> comX = Quantities.create(300.0, Units.METER);
    private boolean collapsed = false;

    private static class Block {
        Quantity<Length> offset;
        Quantity<Mass> mass;
        public Block(Quantity<Length> o, Quantity<Mass> m) {
            this.offset = o;
            this.mass = m;
        }
    }

    @Override
    protected Node createViewerNode() {
        BorderPane root = new BorderPane();
        canvas = new Canvas(600, 600);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        draw();
        return root;
    }

    @Override
    protected VBox createControlPanel() {
        SocialI18n i18n = SocialI18n.getInstance();
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(280);

        Button addBlockBtn = new Button(i18n.get("arch.stability.btn.add", "Add Block"));
        addBlockBtn.setMaxWidth(Double.MAX_VALUE);
        addBlockBtn.setOnAction(e -> {
            if (collapsed) return;
            blocks.add(new Block(Quantities.create((Math.random() - 0.5) * 60, Units.METER), Quantities.create(1.0, Units.KILOGRAM)));
            draw();
        });

        Button resetBtn = new Button(i18n.get("arch.stability.btn.reset", "Reset Simulation"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> {
            blocks.clear();
            collapsed = false;
            comX = Quantities.create(300.0, Units.METER);
            statusLabel.setText(i18n.get("arch.stability.label.stable", "Stable"));
            statusLabel.setStyle("-fx-text-fill: green;");
            draw();
        });

        statusLabel = new Label(i18n.get("arch.stability.label.stable", "Stable"));
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        panel.getChildren().addAll(
            new Label(i18n.get("arch.stability.controls", "Stability Controls")),
            new Separator(),
            addBlockBtn,
            resetBtn,
            new Separator(),
            statusLabel
        );
        return panel;
    }

    private void draw() {
        if (gc == null) return;
        SocialI18n i18n = SocialI18n.getInstance();
        gc.clearRect(0, 0, 600, 600);
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 550, 600, 50);
        
        // Base ground block
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(250, 500, 100, 50);

        double currentY = 500;
        Quantity<Mass> baseMass = Quantities.create(10.0, Units.KILOGRAM);
        Quantity<Length> basePos = Quantities.create(300.0, Units.METER);
        Quantity<?> totalMoment = baseMass.multiply(basePos);
        Quantity<Mass> totalMass = baseMass;

        for (Block b : blocks) {
            currentY -= 50;
            double bx = 250 + b.offset.getValue().doubleValue();
            gc.setFill(collapsed ? Color.RED : Color.GRAY);
            gc.setStroke(Color.BLACK);
            gc.fillRect(bx, currentY, 100, 50);
            gc.strokeRect(bx, currentY, 100, 50);

            if (!collapsed) {
                Quantity<Length> blockX = Quantities.create(bx + 50, Units.METER);
                Quantity<?> moment = blockX.multiply(b.mass);
                @SuppressWarnings({"unchecked", "rawtypes"})
                Quantity<?> tm = totalMoment.add((Quantity) moment);
                totalMoment = tm;
                totalMass = totalMass.add(b.mass);
            }
        }

        comX = totalMoment.divide(totalMass).asType(Length.class);
        double comValue = comX.getValue().doubleValue();
        
        // Center of mass line
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(comValue, 0, comValue, 600);
        
        gc.setFill(Color.BLACK);
        gc.fillText(String.format(i18n.get("arch.stability.label.com", "CoM: %.2f"), comValue), 10, 20);

        if (comValue < 250 || comValue > 350) {
            collapsed = true;
            if (statusLabel != null) {
                statusLabel.setText(i18n.get("arch.stability.label.collapsed", "COLLAPSED!"));
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        }
    }

    @Override
    protected String getLongDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.architecturestabilitydemo.longdesc");
    }
}
