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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.ui.Parameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.ChoiceParameter;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interactive Electrical Circuit Schematic Designer.
 * Refactored to be 100% parameter-based.
 */
public class CircuitSimulatorViewer extends org.jscience.ui.AbstractViewer implements org.jscience.ui.Simulatable {

    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.circuitsimulatorviewer.name", "Circuit Simulator"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.circuitsimulatorviewer.desc", "Interactive analog circuit simulator."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.circuitsimulatorviewer.longdesc", "A comprehensive interactive analog circuit simulator. Design electronic schematics using a variety of components including resistors, capacitors, inductors, batteries, and ground points. Use the integrated solver to calculate nodal voltages and analyze circuit behavior in real-time."); }

    private enum ComponentType {
        WIRE("circuit.component.wire"),
        RESISTOR("circuit.component.resistor"),
        CAPACITOR("circuit.component.capacitor"),
        INDUCTOR("circuit.component.inductor"),
        BATTERY("circuit.component.battery"),
        GROUND("circuit.component.ground"),
        METER("circuit.component.meter"),
        SELECT("circuit.btn.select");

        private final String i18nKey;
        ComponentType(String i18nKey) { this.i18nKey = i18nKey; }
        @Override public String toString() { return I18n.getInstance().get(i18nKey); }
        public static ComponentType fromString(String s) {
            for (ComponentType t : values()) if (t.toString().equals(s)) return t;
            return WIRE;
        }
    }

    private ComponentType selectedType = ComponentType.WIRE;
    private Component selectedComponent = null;

    private static class Component {
        ComponentType type;
        double x1, y1, x2, y2;
        Quantity<?> value;
        Component(ComponentType type, double x1, double y1, double x2, double y2) {
            this.type = type; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
            switch (type) {
                case RESISTOR -> value = Quantities.create(1000.0, Units.OHM);
                case BATTERY -> value = Quantities.create(9.0, Units.VOLT);
                case CAPACITOR -> value = Quantities.create(10e-6, Units.FARAD);
                case INDUCTOR -> value = Quantities.create(1e-3, Units.HENRY);
                default -> value = Quantities.create(0.0, Units.ONE);
            }
        }
    }

    private java.util.Map<String, Double> nodeVoltages = null;
    private List<Component> components = new ArrayList<>();
    private double startX, startY;
    private boolean dragging = false;
    private Canvas canvas;
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public CircuitSimulatorViewer() {
        setupParameters();
        initUI();
    }

    private void setupParameters() {
        List<String> types = Arrays.stream(ComponentType.values()).map(Object::toString).collect(Collectors.toList());
        parameters.add(new ChoiceParameter("circuit.tool", I18n.getInstance().get("circuit.tool", "Tool"), types, ComponentType.WIRE.toString(), v -> {
            selectedType = ComponentType.fromString(v);
            selectedComponent = null;
            draw();
        }));

        parameters.add(new BooleanParameter("circuit.action.solve", I18n.getInstance().get("circuit.btn.solve", "Solve"), false, v -> { if(v) solveCircuit(); }));
        parameters.add(new BooleanParameter("circuit.action.delete", I18n.getInstance().get("circuit.btn.delete", "Delete Selected"), false, v -> {
            if(v && selectedComponent != null) { components.remove(selectedComponent); selectedComponent = null; draw(); }
        }));
        parameters.add(new BooleanParameter("circuit.action.clear", I18n.getInstance().get("circuit.btn.clear", "Clear All"), false, v -> {
            if(v) { components.clear(); selectedComponent = null; nodeVoltages = null; draw(); }
        }));
    }

    private void initUI() {
        canvas = new Canvas(1000, 700);
        setCenter(new ScrollPane(canvas));

        canvas.setOnMousePressed(e -> {
            if (selectedType == ComponentType.SELECT) {
                handleSelection(e.getX(), e.getY());
            } else {
                startX = snap(e.getX()); startY = snap(e.getY()); dragging = true;
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (selectedType != ComponentType.SELECT) {
                draw();
                drawComponentGhost(startX, startY, snap(e.getX()), snap(e.getY()));
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (dragging && selectedType != ComponentType.SELECT) {
                double endX = snap(e.getX()), endY = snap(e.getY());
                if (startX != endX || startY != endY) components.add(new Component(selectedType, startX, startY, endX, endY));
                dragging = false; draw();
            }
        });
        draw();
    }

    private void handleSelection(double x, double y) {
        selectedComponent = null;
        for (Component c : components) {
            if (distToSeg(x, y, c.x1, c.y1, c.x2, c.y2) < 10) { selectedComponent = c; break; }
        }
        draw();
    }

    private double distToSeg(double x, double y, double x1, double y1, double x2, double y2) {
        double l2 = Math.pow(x2-x1,2) + Math.pow(y2-y1,2);
        if (l2 == 0) return Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2));
        double t = ((x-x1)*(x2-x1) + (y-y1)*(y2-y1)) / l2;
        t = Math.max(0, Math.min(1, t));
        return Math.sqrt(Math.pow(x-(x1+t*(x2-x1)),2) + Math.pow(y-(y1+t*(y2-y1)),2));
    }

    private double snap(double v) { return Math.round(v/20.0)*20.0; }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.web("#eee"));
        for (int x=0; x<canvas.getWidth(); x+=20) gc.strokeLine(x,0,x,canvas.getHeight());
        for (int y=0; y<canvas.getHeight(); y+=20) gc.strokeLine(0,y,canvas.getWidth(),y);
        for (Component c : components) drawComponent(gc, c, c == selectedComponent);
        if (nodeVoltages != null) {
            gc.setFill(Color.BLUE);
            for (java.util.Map.Entry<String, Double> e : nodeVoltages.entrySet()) {
                String[] p = e.getKey().split(",");
                gc.fillText(String.format("%.2fV", e.getValue()), Double.parseDouble(p[0])+5, Double.parseDouble(p[1])-5);
            }
        }
    }

    private void drawComponent(GraphicsContext gc, Component c, boolean sel) {
        gc.setStroke(sel ? Color.RED : Color.BLACK);
        gc.setLineWidth(sel ? 3 : 2);
        double mx = (c.x1+c.x2)/2, my = (c.y1+c.y2)/2;
        switch(c.type) {
            case WIRE -> gc.strokeLine(c.x1, c.y1, c.x2, c.y2);
            case RESISTOR -> {
                gc.strokeLine(c.x1, c.y1, mx-10, my); gc.strokeLine(mx+10, my, c.x2, c.y2);
                gc.strokeRect(mx-10, my-5, 20, 10); gc.fillText(c.value.toString(), mx-15, my-10);
            }
            case BATTERY -> {
                gc.strokeLine(c.x1, c.y1, mx-5, my); gc.strokeLine(mx+5, my, c.x2, c.y2);
                gc.strokeLine(mx-5, my-15, mx-5, my+15); gc.strokeLine(mx+5, my-8, mx+5, my+8);
                gc.fillText(c.value.toString(), mx-10, my-20);
            }
            case GROUND -> { gc.strokeLine(c.x1, c.y1, mx, my); gc.strokeLine(mx-10, my, mx+10, my); }
            case CAPACITOR -> {
                gc.strokeLine(c.x1, c.y1, mx - 5, my); gc.strokeLine(mx + 5, my, c.x2, c.y2);
                gc.strokeLine(mx - 5, my - 10, mx - 5, my + 10); gc.strokeLine(mx + 5, my - 10, mx + 5, my + 10);
            }
            case INDUCTOR -> {
                gc.strokeLine(c.x1, c.y1, mx - 10, my); gc.strokeLine(mx + 10, my, c.x2, c.y2);
                gc.strokeArc(mx - 10, my - 5, 10, 10, 0, 180, javafx.scene.shape.ArcType.OPEN);
                gc.strokeArc(mx, my - 5, 10, 10, 0, 180, javafx.scene.shape.ArcType.OPEN);
            }
            case METER -> {
                gc.strokeLine(c.x1, c.y1, mx - 10, my); gc.strokeLine(mx + 10, my, c.x2, c.y2);
                gc.strokeOval(mx - 10, my - 10, 20, 20); gc.strokeLine(mx - 5, my + 5, mx + 5, my - 5);
            }
            case SELECT -> { /* No visual for selection tool ghost */ }
        }
    }

    private void drawComponentGhost(double x1, double y1, double x2, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setGlobalAlpha(0.5); drawComponent(gc, new Component(selectedType, x1, y1, x2, y2), false); gc.setGlobalAlpha(1.0);
    }

    private void solveCircuit() {
        if (components.isEmpty()) return;
        java.util.Map<String, Integer> coordToNode = new java.util.HashMap<>();
        int nodeCount = 0;
        for (Component c : components) {
            String p1 = (int)c.x1 + "," + (int)c.y1; String p2 = (int)c.x2 + "," + (int)c.y2;
            if (!coordToNode.containsKey(p1)) coordToNode.put(p1, nodeCount++);
            if (!coordToNode.containsKey(p2)) coordToNode.put(p2, nodeCount++);
        }
        int groundNode = -1;
        for (Component c : components) if (c.type == ComponentType.GROUND) { groundNode = coordToNode.get((int)c.x1 + "," + (int)c.y1); break; }
        if (groundNode == -1) groundNode = 0;
        int size = nodeCount;
        double[][] gMatrix = new double[size][size];
        double[] iVector = new double[size];
        for (Component c : components) {
            int n1 = coordToNode.get((int)c.x1 + "," + (int)c.y1);
            int n2 = coordToNode.get((int)c.x2 + "," + (int)c.y2);
            double val = c.value.getValue().doubleValue();
            switch (c.type) {
                case RESISTOR -> { double g = 1.0 / val; gMatrix[n1][n1] += g; gMatrix[n2][n2] += g; gMatrix[n1][n2] -= g; gMatrix[n2][n1] -= g; }
                case BATTERY -> {
                    iVector[n1] -= val / 10.0; iVector[n2] += val / 10.0;
                    double g = 1.0 / 10.0; gMatrix[n1][n1] += g; gMatrix[n2][n2] += g; gMatrix[n1][n2] -= g; gMatrix[n2][n1] -= g;
                }
                case WIRE -> { double g = 1e6; gMatrix[n1][n1] += g; gMatrix[n2][n2] += g; gMatrix[n1][n2] -= g; gMatrix[n2][n1] -= g; }
                default -> {}
            }
        }
        int reducedSize = size - 1;
        org.jscience.mathematics.numbers.real.Real[][] reducedG = new org.jscience.mathematics.numbers.real.Real[reducedSize][reducedSize];
        org.jscience.mathematics.numbers.real.Real[] reducedI = new org.jscience.mathematics.numbers.real.Real[reducedSize];
        int rRow = 0;
        for (int i = 0; i < size; i++) {
            if (i == groundNode) continue;
            int rCol = 0;
            for (int j = 0; j < size; j++) {
                if (j == groundNode) continue;
                reducedG[rRow][rCol] = org.jscience.mathematics.numbers.real.Real.of(gMatrix[i][j]); rCol++;
            }
            reducedI[rRow] = org.jscience.mathematics.numbers.real.Real.of(iVector[i]); rRow++;
        }
        try {
            org.jscience.mathematics.linearalgebra.Matrix<org.jscience.mathematics.numbers.real.Real> G = 
                org.jscience.mathematics.linearalgebra.matrices.GenericMatrix.of(reducedG, org.jscience.mathematics.numbers.real.Real.ZERO);
            org.jscience.mathematics.linearalgebra.matrices.solvers.LUDecomposition lu = 
                org.jscience.mathematics.linearalgebra.matrices.solvers.LUDecomposition.decompose(G);
            org.jscience.mathematics.numbers.real.Real[] solution = lu.solve(reducedI);
            nodeVoltages = new java.util.HashMap<>();
            int vIdx = 0;
            for (int i = 0; i < size; i++) {
                if (i == groundNode) { for (java.util.Map.Entry<String, Integer> e : coordToNode.entrySet()) if (e.getValue() == groundNode) nodeVoltages.put(e.getKey(), 0.0); }
                else {
                    double volt = solution[vIdx++].doubleValue();
                    for (java.util.Map.Entry<String, Integer> e : coordToNode.entrySet()) if (e.getValue() == i) nodeVoltages.put(e.getKey(), volt);
                }
            }
            draw();
        } catch (Exception e) {}
    }

    @Override public void play() {}
    @Override public void pause() {}
    @Override public void stop() {}
    @Override public void step() {}
    @Override public void setSpeed(double s) {}
    @Override public boolean isPlaying() { return false; }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
