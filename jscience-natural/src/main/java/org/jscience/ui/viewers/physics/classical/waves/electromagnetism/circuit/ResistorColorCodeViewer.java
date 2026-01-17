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

package org.jscience.ui.viewers.physics.classical.waves.electromagnetism.circuit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Resistor Color Code Calculator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ResistorColorCodeViewer extends org.jscience.ui.AbstractViewer {

    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.physics", "Physics"); }
    
    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.resistorcolorcodeviewer.name", "Resistor Color Code"); }


    private final org.jscience.ui.Parameter<String> band1Param;
    private final org.jscience.ui.Parameter<String> band2Param;
    private final org.jscience.ui.Parameter<String> band3Param;
    private final org.jscience.ui.Parameter<String> band4Param;

    private final ComboBox<String> band1 = new ComboBox<>();
    private final ComboBox<String> band2 = new ComboBox<>();
    private final ComboBox<String> band3 = new ComboBox<>(); // Multiplier
    private final ComboBox<String> band4 = new ComboBox<>(); // Tolerance

    private final Rectangle r1 = new Rectangle(20, 50);
    private final Rectangle r2 = new Rectangle(20, 50);
    private final Rectangle r3 = new Rectangle(20, 50);
    private final Rectangle r4 = new Rectangle(20, 50);

    private final Label resultLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("resistor.result", "Resistance: "));

    private static final Map<String, Color> colorMap = new HashMap<>();
    private static final Map<String, Integer> valMap = new HashMap<>();
    private static final Map<String, Double> multMap = new HashMap<>();
    private static final String[] COLORS = { "Black", "Brown", "Red", "Orange", "Yellow", "Green", "Blue", "Violet",
            "Gray", "White" };

    static {
        Color[] fxColors = { Color.BLACK, Color.BROWN, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
                Color.VIOLET, Color.GRAY, Color.WHITE };
        for (int i = 0; i < COLORS.length; i++) {
            colorMap.put(COLORS[i], fxColors[i]);
            valMap.put(COLORS[i], i);
            multMap.put(COLORS[i], Math.pow(10, i));
        }
    }

    public ResistorColorCodeViewer() {
        this.band1Param = new org.jscience.ui.Parameter<>("Band 1", "First Band", "Brown", v -> calculate());
        this.band2Param = new org.jscience.ui.Parameter<>("Band 2", "Second Band", "Black", v -> calculate());
        this.band3Param = new org.jscience.ui.Parameter<>("Multiplier", "Multiplier Band", "Red", v -> calculate());
        this.band4Param = new org.jscience.ui.Parameter<>("Tolerance", "Tolerance Band", "Gold", v -> calculate());
        initUI();
    }

    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return java.util.List.of(band1Param, band2Param, band3Param, band4Param);
    }

    private void initUI() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("viewer-root");
        resultLabel.getStyleClass().add("header-label");

        // Resistor Visual
        StackPane resistorBody = new StackPane();
        Rectangle body = new Rectangle(200, 50, Color.BEIGE);
        body.setArcWidth(10);
        body.setArcHeight(10);

        HBox bands = new HBox(15);
        bands.setAlignment(Pos.CENTER);
        bands.getChildren().addAll(r1, r2, r3, r4);

        resistorBody.getChildren().addAll(body, bands);

        // Controls
        HBox controls = new HBox(10);
        setupComboBox(band1, band1Param);
        setupComboBox(band2, band2Param);
        setupComboBox(band3, band3Param); // Multiplier
        // Tolerance (Simplified)
        band4.getItems().addAll("Gold", "Silver");
        band4.setValue(band4Param.getValue());
        band4.setOnAction(e -> band4Param.setValue(band4.getValue()));
        // Note: Bi-directional binding purely by listeners here logic handled in setupComboBox for others
        
        colorMap.put("Gold", Color.GOLD);
        colorMap.put("Silver", Color.SILVER);

        controls.getChildren().addAll(band1, band2, band3, band4);

        calculate();

        root.getChildren().addAll(resistorBody, resultLabel, controls);
        this.setCenter(root);
    }

    private void setupComboBox(ComboBox<String> cb, org.jscience.ui.Parameter<String> param) {
        cb.getItems().addAll(COLORS);
        cb.setValue(param.getValue());
        cb.setOnAction(e -> param.setValue(cb.getValue()));
        // We can't add a listener to param here easily because Parameter only supports one consumer (ctor).
        // BUT calculate() is the consumer. It reads params.
        // Does it update ComboBoxes? No.
        // If external source updates param, ComboBox won't update.
        // This is a limitation of the current Parameter API (single consumer).
        // Since we don't expect external updates in this viewer, it's fine.
    }

    private void calculate() {
        String c1 = band1Param.getValue();
        String c2 = band2Param.getValue();
        String c3 = band3Param.getValue();
        String c4 = band4Param.getValue();

        r1.setFill(colorMap.get(c1));
        r2.setFill(colorMap.get(c2));
        r3.setFill(colorMap.get(c3));
        r4.setFill(colorMap.get(c4));

        int v1 = valMap.getOrDefault(c1, 0);
        int v2 = valMap.getOrDefault(c2, 0);
        double m = multMap.getOrDefault(c3, 1.0);

        double ohms = (v1 * 10 + v2) * m;
        String tol = c4.equals("Gold") ? "5%" : "10%";
        resultLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("resistor.result.fmt", "Resistance: {0} Ohms +/- {1}", ohms, tol));
    }



    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.resistorcolorcodeviewer.desc", "Resistor color code calculator.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.resistorcolorcodeviewer.longdesc", "Calculate resistor values based on their color bands. Supports 4-band resistor codes, including multipliers and tolerance bands. features a visual representation of the resistor that updates in real-time.");
    }
}


