/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.computing.logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.Parameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;
import org.jscience.computing.logic.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Digital Logic Circuit Simulator Viewer.
 * Refactored to use LogicCircuit backend.
 */
public class DigitalLogicViewer extends AbstractViewer implements Simulatable {

    private final LogicCircuit circuit;
    private LogicCircuit.Component selectedComponent = null;
    private LogicCircuit.Connection selectedConnection = null;
    private LogicCircuit.Port dragSource = null;

    private double dragOffsetX, dragOffsetY;
    private boolean simulationMode = true; 
    private Label statusLabel;
    private Canvas canvas;
    private boolean playing = false;

    public DigitalLogicViewer() {
        this(new LogicCircuit());
    }

    public DigitalLogicViewer(LogicCircuit circuit) {
        this.circuit = circuit;
        initUI();
    }

    private void initUI() {
        canvas = new Canvas(1000, 700);

        canvas.setOnMousePressed(e -> {
            double mx = e.getX(), my = e.getY();
            canvas.requestFocus();

            selectedComponent = null;
            selectedConnection = null;

            if (simulationMode) {
                for (LogicCircuit.Component c : circuit.getComponents()) {
                    if ("INPUT".equals(c.typeName) && contains(c, mx, my)) {
                        c.currentState = (c.currentState == LogicState.HIGH) ? LogicState.LOW : LogicState.HIGH;
                        circuit.simulate();
                        draw();
                        return;
                    }
                }
                return;
            }

            for (LogicCircuit.Component c : circuit.getComponents()) {
                for (LogicCircuit.Port p : c.outputs) {
                    if (containsPort(p, mx, my)) {
                        dragSource = p;
                        return;
                    }
                }
            }

            for (LogicCircuit.Component c : circuit.getComponents()) {
                if (contains(c, mx, my)) {
                    selectedComponent = c;
                    dragOffsetX = mx - c.x;
                    dragOffsetY = my - c.y;
                    draw();
                    return;
                }
            }

            for (LogicCircuit.Connection conn : circuit.getConnections()) {
                if (containsConnection(conn, mx, my)) {
                    selectedConnection = conn;
                    draw();
                    return;
                }
            }
            draw();
        });

        canvas.setOnMouseDragged(e -> {
            if (simulationMode) return;
            if (dragSource != null) {
                draw();
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeLine(dragX(dragSource), dragY(dragSource), e.getX(), e.getY());
            } else if (selectedComponent != null) {
                selectedComponent.x = e.getX() - dragOffsetX;
                selectedComponent.y = e.getY() - dragOffsetY;
                circuit.simulate();
                draw();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (simulationMode) return;
            if (dragSource != null) {
                for (LogicCircuit.Component c : circuit.getComponents()) {
                    for (LogicCircuit.Port p : c.inputs) {
                        if (containsPort(p, e.getX(), e.getY())) {
                            circuit.addConnection(dragSource, p);
                            circuit.simulate();
                        }
                    }
                }
                dragSource = null;
                draw();
            }
        });

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            if (!simulationMode) {
                if (e.getCode() == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                    if (selectedComponent != null) {
                        circuit.removeComponent(selectedComponent);
                        selectedComponent = null;
                        draw();
                    } else if (selectedConnection != null) {
                        circuit.removeConnection(selectedConnection);
                        selectedConnection = null;
                        circuit.simulate();
                        draw();
                    }
                }
            }
        });
        
        StackPane root = new StackPane(canvas);
        root.getStyleClass().add("viewer-root");
        setCenter(root);
        setRight(createSidebar());
        
        root.widthProperty().addListener(o -> canvas.setWidth(root.getWidth()));
        root.heightProperty().addListener(o -> canvas.setHeight(root.getHeight()));

        updateStatus();
        draw();
    }

    private VBox createSidebar() {
        VBox toolbar = new VBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.TOP_LEFT);
        toolbar.setPrefWidth(200);
        toolbar.getStyleClass().add("viewer-sidebar");

        Label compLabel = new Label(I18n.getInstance().get("viewer.digitallogicviewer.components", "Components"));
        compLabel.getStyleClass().add("font-bold");

        Button btnAnd = new Button("AND Gate"); btnAnd.setMaxWidth(Double.MAX_VALUE);
        btnAnd.setOnAction(e -> addGate(new AndGate(), "AND", 2, 1));

        Button btnOr = new Button("OR Gate"); btnOr.setMaxWidth(Double.MAX_VALUE);
        btnOr.setOnAction(e -> addGate(new OrGate(), "OR", 2, 1));

        Button btnNot = new Button("NOT Gate"); btnNot.setMaxWidth(Double.MAX_VALUE);
        btnNot.setOnAction(e -> addGate(new NotGate(), "NOT", 1, 1));

        Button btnNand = new Button("NAND Gate"); btnNand.setMaxWidth(Double.MAX_VALUE);
        btnNand.setOnAction(e -> addGate(new NandGate(), "NAND", 2, 1));

        Button btnIn = new Button("Input Switch"); btnIn.setMaxWidth(Double.MAX_VALUE);
        btnIn.setOnAction(e -> addGate(null, "INPUT", 0, 1));

        Button btnOut = new Button("Output LED"); btnOut.setMaxWidth(Double.MAX_VALUE);
        btnOut.setOnAction(e -> addGate(null, "OUTPUT", 1, 0));

        statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.getStyleClass().add("description-label");

        toolbar.getChildren().addAll(new Separator(), compLabel, btnIn, btnOut, btnAnd, btnOr, btnNot, btnNand, new Separator(), statusLabel);
        return toolbar;
    }

    private void addGate(LogicGate gate, String type, int inCount, int outCount) {
        if (simulationMode) return;
        LogicCircuit.Component c = new LogicCircuit.Component(gate, type, 100, 100);
        for (int i = 0; i < inCount; i++) c.inputs.add(new LogicCircuit.Port(c, true));
        for (int i = 0; i < outCount; i++) c.outputs.add(new LogicCircuit.Port(c, false));
        circuit.addComponent(c);
        draw();
    }

    private void updateStatus() {
        if (statusLabel == null) return;
        if (simulationMode) statusLabel.setText("Simulation: Click Inputs.");
        else statusLabel.setText("Design: Drag components.");
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        gc.setStroke(Color.web("#eee")); gc.setLineWidth(1);
        for (int i = 0; i < canvas.getWidth(); i += 20) gc.strokeLine(i, 0, i, canvas.getHeight());
        for (int i = 0; i < canvas.getHeight(); i += 20) gc.strokeLine(0, i, canvas.getWidth(), i);

        for (LogicCircuit.Connection conn : circuit.getConnections()) {
            gc.setStroke(conn == selectedConnection ? Color.BLUE : (conn.start.parent.currentState == LogicState.HIGH ? Color.RED : Color.BLACK));
            gc.setLineWidth(conn == selectedConnection ? 4 : 3);
            gc.beginPath();
            double sx = dragX(conn.start), sy = dragY(conn.start);
            double ex = dragX(conn.end), ey = dragY(conn.end);
            gc.moveTo(sx, sy);
            double dist = Math.max(50, Math.abs(ex - sx) * 0.5);
            gc.bezierCurveTo(sx + dist, sy, ex - dist, ey, ex, ey);
            gc.stroke();
        }

        for (LogicCircuit.Component c : circuit.getComponents()) {
            drawComponent(gc, c, c == selectedComponent);
        }
    }

    private void drawComponent(GraphicsContext gc, LogicCircuit.Component c, boolean isSelected) {
        gc.setFill(Color.WHITE); gc.setStroke(isSelected ? Color.BLUE : Color.BLACK); gc.setLineWidth(isSelected ? 3 : 2);
        double x = c.x, y = c.y;
        if ("INPUT".equals(c.typeName)) {
            gc.setFill(c.currentState == LogicState.HIGH ? Color.LIGHTGREEN : Color.WHITE);
            gc.fillRect(x, y, 40, 30); gc.strokeRect(x, y, 40, 30);
            gc.setFill(Color.BLACK); gc.fillText(c.currentState.name(), x + 5, y + 20);
        } else if ("OUTPUT".equals(c.typeName)) {
            gc.setFill(c.currentState == LogicState.HIGH ? Color.YELLOW : Color.GRAY);
            gc.fillOval(x, y, 40, 40); gc.strokeOval(x, y, 40, 40);
        } else {
            gc.fillRect(x, y, 60, 40); gc.strokeRect(x, y, 60, 40);
            gc.setFill(Color.BLACK); gc.fillText(c.typeName, x + 10, y + 25);
        }
        
        gc.setFill(Color.BLACK);
        for (int i = 0; i < c.inputs.size(); i++) gc.fillOval(x - 3, y + (i + 1) * 40.0 / (c.inputs.size() + 1) - 3, 6, 6);
        for (int i = 0; i < c.outputs.size(); i++) gc.fillOval(x + componentWidth(c) - 3, y + (i + 1) * 40.0 / (c.outputs.size() + 1) - 3, 6, 6);
    }

    private double componentWidth(LogicCircuit.Component c) {
        return ("INPUT".equals(c.typeName) || "OUTPUT".equals(c.typeName)) ? 40 : 60;
    }

    private boolean contains(LogicCircuit.Component c, double mx, double my) {
        return mx >= c.x && mx <= c.x + componentWidth(c) && my >= c.y && my <= c.y + 40;
    }

    private boolean containsPort(LogicCircuit.Port p, double mx, double my) {
        return Math.hypot(dragX(p) - mx, dragY(p) - my) < 10;
    }

    private double dragX(LogicCircuit.Port p) {
        return p.isInput ? p.parent.x : p.parent.x + componentWidth(p.parent);
    }

    private double dragY(LogicCircuit.Port p) {
        int idx = p.isInput ? p.parent.inputs.indexOf(p) : p.parent.outputs.indexOf(p);
        int count = p.isInput ? p.parent.inputs.size() : p.parent.outputs.size();
        return p.parent.y + (idx + 1) * 40.0 / (count + 1);
    }

    private boolean containsConnection(LogicCircuit.Connection conn, double mx, double my) {
        double sx = dragX(conn.start), sy = dragY(conn.start);
        double ex = dragX(conn.end), ey = dragY(conn.end);
        double dist = Math.max(50, Math.abs(ex - sx) * 0.5);
        for (double t = 0; t <= 1; t += 0.05) {
            double ix = Math.pow(1 - t, 3) * sx + 3 * Math.pow(1 - t, 2) * t * (sx + dist) + 3 * (1 - t) * t * t * (ex - dist) + t * t * t * ex;
            double iy = Math.pow(1 - t, 3) * sy + 3 * Math.pow(1 - t, 2) * t * sy + 3 * (1 - t) * t * t * ey + t * t * t * ey;
            if (Math.hypot(mx - ix, my - iy) < 6) return true;
        }
        return false;
    }

    @Override public void play() { playing = true; }
    @Override public void pause() { playing = false; }
    @Override public void stop() { playing = false; circuit.clear(); draw(); }
    @Override public void step() { circuit.simulate(); draw(); }
    @Override public void setSpeed(double speed) { }
    @Override public boolean isPlaying() { return playing; }

    @Override public String getName() { return I18n.getInstance().get("viewer.digitallogicviewer.name", "Digital Logic Viewer"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.computing", "Computing"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.digitallogicviewer.desc", "Digital logic simulator."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.digitallogicviewer.longdesc", "Refactored logic simulator using LogicCircuit backend."); }
    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new BooleanParameter("viewer.digitallogicviewer.simulation", I18n.getInstance().get("viewer.digitallogicviewer.mode.simulation", "Simulation Mode"), simulationMode, v -> {
            simulationMode = v;
            if (simulationMode) {
                selectedComponent = null;
                selectedConnection = null;
                circuit.simulate();
            }
            draw();
            updateStatus();
        }));
        return params;
    }
}

