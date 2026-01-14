/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.computing.logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Digital Logic Circuit Simulator Viewer.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DigitalLogicViewer extends AbstractViewer implements Simulatable {

    private List<Gate> gates = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();

    private Gate selectedGate = null;
    private Wire selectedWire = null;
    private Port dragSource = null;

    private double dragOffsetX, dragOffsetY;
    private boolean simulationMode = true; // Default to Simulation
    private Label statusLabel;
    private Canvas canvas;
    
    // Sim state
    private boolean playing = false;

    public DigitalLogicViewer() {
        canvas = new Canvas(1000, 700);

        canvas.setOnMousePressed(e -> {
            double mx = e.getX(), my = e.getY();
            canvas.requestFocus();

            selectedGate = null;
            selectedWire = null;

            if (simulationMode) {
                for (Gate g : gates) {
                    if (g.type == GateType.INPUT && g.contains(mx, my)) {
                        g.isOn = !g.isOn;
                        simulate();
                        draw();
                        return;
                    }
                }
                return;
            }

            for (Gate g : gates) {
                for (Port p : g.outputs) {
                    if (p.contains(mx, my)) {
                        dragSource = p;
                        return;
                    }
                }
            }

            for (Gate g : gates) {
                if (g.contains(mx, my)) {
                    selectedGate = g;
                    dragOffsetX = mx - g.x;
                    dragOffsetY = my - g.y;
                    if (g.type == GateType.INPUT) {
                        g.isOn = !g.isOn;
                        simulate();
                    }
                    draw();
                    return;
                }
            }

            for (Wire w : wires) {
                if (w.contains(mx, my)) {
                    selectedWire = w;
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
                gc.strokeLine(dragSource.x, dragSource.y, e.getX(), e.getY());
            } else if (selectedGate != null) {
                selectedGate.x = e.getX() - dragOffsetX;
                selectedGate.y = e.getY() - dragOffsetY;
                selectedGate.updatePorts();
                simulate();
                draw();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (simulationMode) return;
            if (dragSource != null) {
                for (Gate g : gates) {
                    for (Port p : g.inputs) {
                        if (p.contains(e.getX(), e.getY())) {
                            wires.add(new Wire(dragSource, p));
                            simulate();
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
                    if (selectedGate != null) {
                        deleteGate(selectedGate);
                        selectedGate = null;
                        draw();
                    } else if (selectedWire != null) {
                        wires.remove(selectedWire);
                        selectedWire = null;
                        simulate();
                        draw();
                    }
                }
            }
        });
        
        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: white;");
        setCenter(root);
        setRight(createControlPanel());
        
        // Resize
        root.widthProperty().addListener(o -> canvas.setWidth(root.getWidth()));
        root.heightProperty().addListener(o -> canvas.setHeight(root.getHeight()));

        loadDefaultCircuit();
        simulate();
        draw();
    }

    private VBox createControlPanel() {
        VBox toolbar = new VBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.TOP_LEFT);
        toolbar.setPrefWidth(200);
        toolbar.getStyleClass().add("dark-viewer-sidebar"); // Or keep default

        ToggleButton modeBtn = new ToggleButton(I18n.getInstance().get("digital.mode.simulation", "Simulation Mode"));
        modeBtn.setSelected(true);
        modeBtn.setMaxWidth(Double.MAX_VALUE);
        modeBtn.setOnAction(e -> {
            simulationMode = modeBtn.isSelected();
            modeBtn.setText(simulationMode ? I18n.getInstance().get("digital.mode.simulation", "Simulation Mode")
                    : I18n.getInstance().get("digital.mode.design", "Design Mode"));
            if (simulationMode) {
                selectedGate = null;
                selectedWire = null;
                simulate();
            }
            draw();
            updateStatus();
        });

        Label compLabel = new Label(I18n.getInstance().get("digital.components", "Components"));
        compLabel.setStyle("-fx-font-weight: bold;");

        Button btnAnd = new Button("AND Gate"); btnAnd.setMaxWidth(Double.MAX_VALUE);
        btnAnd.setOnAction(e -> addGate(new Gate(GateType.AND, 100, 100)));

        Button btnOr = new Button("OR Gate"); btnOr.setMaxWidth(Double.MAX_VALUE);
        btnOr.setOnAction(e -> addGate(new Gate(GateType.OR, 100, 150)));

        Button btnNot = new Button("NOT Gate"); btnNot.setMaxWidth(Double.MAX_VALUE);
        btnNot.setOnAction(e -> addGate(new Gate(GateType.NOT, 100, 200)));

        Button btnNand = new Button("NAND Gate"); btnNand.setMaxWidth(Double.MAX_VALUE);
        btnNand.setOnAction(e -> addGate(new Gate(GateType.NAND, 100, 250)));

        Button btnIn = new Button("Input Switch"); btnIn.setMaxWidth(Double.MAX_VALUE);
        btnIn.setOnAction(e -> addGate(new Gate(GateType.INPUT, 50, 100)));

        Button btnOut = new Button("Output LED"); btnOut.setMaxWidth(Double.MAX_VALUE);
        btnOut.setOnAction(e -> addGate(new Gate(GateType.OUTPUT, 300, 100)));

        Label actionLabel = new Label(I18n.getInstance().get("digital.actions", "Actions"));
        actionLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        Button clear = new Button(I18n.getInstance().get("digital.clear", "Clear All"));
        clear.setMaxWidth(Double.MAX_VALUE);
        clear.setOnAction(e -> { gates.clear(); wires.clear(); draw(); });

        Button saveBtn = new Button(I18n.getInstance().get("digital.btn.save", "Save Circuit"));
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> {
             if (canvas.getScene() != null && canvas.getScene().getWindow() instanceof Stage s) saveCircuit(s);
        });

        Button loadBtn = new Button(I18n.getInstance().get("digital.btn.load", "Load Circuit"));
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setOnAction(e -> {
            if (canvas.getScene() != null && canvas.getScene().getWindow() instanceof Stage s) loadCircuit(s);
        });

        statusLabel = new Label("Ready");
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 10px;");

        toolbar.getChildren().addAll(
                modeBtn, new Separator(),
                compLabel, btnIn, btnOut, btnAnd, btnOr, btnNot, btnNand,
                new Separator(),
                actionLabel, clear, saveBtn, loadBtn,
                new Separator(),
                statusLabel);

        updateStatus();
        return toolbar;
    }

    private void updateStatus() {
        if (statusLabel == null) return;
        if (simulationMode) statusLabel.setText("Simulation: Click Inputs to toggle.");
        else statusLabel.setText("Design: Drag gates/wires. Select & Delete to remove.");
    }

    private void addGate(Gate g) {
        if (simulationMode) return;
        gates.add(g); draw();
    }

    private void deleteGate(Gate g) {
        wires.removeIf(w -> w.start.parent == g || w.end.parent == g);
        gates.remove(g); simulate();
    }

    private void loadDefaultCircuit() {
        gates.clear(); wires.clear();
        Gate in1 = new Gate(GateType.INPUT, 50, 200); gates.add(in1);
        Gate in2 = new Gate(GateType.INPUT, 50, 300); gates.add(in2);
        Gate and = new Gate(GateType.AND, 200, 250); gates.add(and);
        Gate out = new Gate(GateType.OUTPUT, 350, 250); gates.add(out);
        wires.add(new Wire(in1.outputs.get(0), and.inputs.get(0)));
        wires.add(new Wire(in2.outputs.get(0), and.inputs.get(1)));
        wires.add(new Wire(and.outputs.get(0), out.inputs.get(0)));
    }

    private void saveCircuit(Stage stage) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Save Circuit");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));
        java.io.File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                for (Gate g : gates) pw.println("GATE " + g.type + " " + (int) g.x + " " + (int) g.y);
                for (Wire w : wires) {
                    int sGate = gates.indexOf(w.start.parent);
                    int eGate = gates.indexOf(w.end.parent);
                    int sPort = w.start.parent.outputs.indexOf(w.start);
                    int ePort = w.end.parent.inputs.indexOf(w.end);
                    if (sGate != -1 && eGate != -1) pw.println("WIRE " + sGate + ":" + sPort + " " + eGate + ":" + ePort);
                }
                statusLabel.setText("Saved: " + file.getName());
            } catch (Exception ex) { statusLabel.setText("Error saving: " + ex.getMessage()); }
        }
    }

    private void loadCircuit(Stage stage) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Load Circuit");
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));
        java.io.File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (java.util.Scanner sc = new java.util.Scanner(file)) {
                gates.clear(); wires.clear();
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    if (line.startsWith("GATE")) {
                        String[] parts = line.split(" ");
                        gates.add(new Gate(GateType.valueOf(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
                    } else if (line.startsWith("WIRE")) {
                        String[] parts = line.split(" ");
                        String[] s = parts[1].split(":");
                        String[] e = parts[2].split(":");
                        wires.add(new Wire(gates.get(Integer.parseInt(s[0])).outputs.get(Integer.parseInt(s[1])),
                                gates.get(Integer.parseInt(e[0])).inputs.get(Integer.parseInt(e[1]))));
                    }
                }
                simulate(); draw(); statusLabel.setText("Loaded: " + file.getName());
            } catch (Exception ex) { statusLabel.setText("Error: " + ex.getMessage()); }
        }
    }

    private void simulate() {
        boolean changed = true;
        int limit = 0;
        while (changed && limit < 100) {
            changed = false;
            for (Gate g : gates) {
                boolean old = g.isOn;
                g.evaluate(wires);
                if (g.isOn != old) changed = true;
            }
            limit++;
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.web("#eee")); gc.setLineWidth(1);
        for (int i = 0; i < canvas.getWidth(); i += 20) gc.strokeLine(i, 0, i, canvas.getHeight());
        for (int i = 0; i < canvas.getHeight(); i += 20) gc.strokeLine(0, i, canvas.getWidth(), i);

        for (Wire w : wires) {
            gc.setStroke(w == selectedWire ? Color.BLUE : (w.start.parent.isOn ? Color.RED : Color.BLACK));
            gc.setLineWidth(w == selectedWire ? 4 : 3);
            gc.beginPath(); gc.moveTo(w.start.x, w.start.y);
            double dist = Math.max(50, Math.abs(w.end.x - w.start.x) * 0.5);
            gc.bezierCurveTo(w.start.x + dist, w.start.y, w.end.x - dist, w.end.y, w.end.x, w.end.y);
            gc.stroke();
        }
        for (Gate g : gates) g.draw(gc, g == selectedGate);
    }
    
    // --- Simulatable Implementation ---
    @Override public void play() { playing = true; }
    @Override public void pause() { playing = false; }
    @Override public void stop() { playing = false; }
    @Override public void step() { simulate(); draw(); }
    @Override public void setSpeed(double speed) { }
    @Override public boolean isPlaying() { return playing; }

    @Override public String getName() { return "Digital Logic Viewer"; }
    @Override public String getCategory() { return "Computing"; }

    enum GateType { INPUT, OUTPUT, AND, OR, NOT, NAND }

    static class Port {
        double x, y; Gate parent; boolean isInput;
        Port(Gate p, boolean in) { parent = p; isInput = in; }
        boolean contains(double mx, double my) { return Math.hypot(x - mx, y - my) < 10; }
    }

    static class Wire {
        Port start, end;
        Wire(Port s, Port e) { start = s; end = e; }
        boolean contains(double mx, double my) {
            double dist = Math.max(50, Math.abs(end.x - start.x) * 0.5);
            for (double t = 0; t <= 1; t += 0.05) {
                double ix = Math.pow(1 - t, 3) * start.x + 3 * Math.pow(1 - t, 2) * t * (start.x + dist) + 3 * (1 - t) * t * t * (end.x - dist) + t * t * t * end.x;
                double iy = Math.pow(1 - t, 3) * start.y + 3 * Math.pow(1 - t, 2) * t * start.y + 3 * (1 - t) * t * t * end.y + t * t * t * end.y;
                if (Math.hypot(mx - ix, my - iy) < 6) return true;
            }
            return false;
        }
    }

    class Gate {
        GateType type; double x, y; boolean isOn = false;
        List<Port> inputs = new ArrayList<>(); List<Port> outputs = new ArrayList<>();

        Gate(GateType t, double x, double y) {
            this.type = t; this.x = x; this.y = y;
            if (t == GateType.INPUT) outputs.add(new Port(this, false));
            else if (t == GateType.OUTPUT || t == GateType.NOT) { inputs.add(new Port(this, true)); if (t == GateType.NOT) outputs.add(new Port(this, false)); }
            else { inputs.add(new Port(this, true)); inputs.add(new Port(this, true)); outputs.add(new Port(this, false)); }
            updatePorts();
        }

        void updatePorts() {
            if (type == GateType.INPUT) setP(outputs, 0, 40, 15);
            else if (type == GateType.OUTPUT || type == GateType.NOT) { setP(inputs, 0, 0, 15); if (type == GateType.NOT) setP(outputs, 0, 60, 15); }
            else { setP(inputs, 0, 0, 10); setP(inputs, 1, 0, 30); setP(outputs, 0, 60, 20); }
        }

        private void setP(List<Port> l, int i, double dx, double dy) { if (i < l.size()) { l.get(i).x = x + dx; l.get(i).y = y + dy; } }
        boolean contains(double mx, double my) { return mx >= x && mx <= x + 60 && my >= y && my <= y + 40; }

        void evaluate(List<Wire> wires) {
            if (type == GateType.INPUT) return;
            boolean i1 = getVal(0, wires); boolean i2 = inputs.size() > 1 && getVal(1, wires);
            switch (type) {
                case OUTPUT: isOn = i1; break;
                case NOT: isOn = !i1; break;
                case AND: isOn = i1 && i2; break;
                case OR: isOn = i1 || i2; break;
                case NAND: isOn = !(i1 && i2); break;
                default: break;
            }
        }

        boolean getVal(int idx, List<Wire> wires) {
            if (idx >= inputs.size()) return false;
            Port target = inputs.get(idx);
            for (Wire w : wires) if (w.end == target) return w.start.parent.isOn;
            return false;
        }

        void draw(GraphicsContext gc, boolean isSelected) {
            gc.setFill(Color.WHITE); gc.setStroke(isSelected ? Color.BLUE : Color.BLACK); gc.setLineWidth(isSelected ? 3 : 2);
            if (type == GateType.INPUT) {
                gc.setFill(isOn ? Color.LIGHTGREEN : Color.WHITE); gc.fillRect(x, y, 40, 30); gc.strokeRect(x, y, 40, 30);
                gc.setFill(Color.BLACK); gc.fillText(isOn ? "ON" : "OFF", x + 5, y + 20);
            } else if (type == GateType.OUTPUT) {
                gc.setFill(isOn ? Color.YELLOW : Color.GRAY); gc.fillOval(x, y, 40, 40); gc.strokeOval(x, y, 40, 40);
            } else {
                gc.beginPath();
                if (type == GateType.AND || type == GateType.NAND) {
                    gc.moveTo(x, y); gc.lineTo(x + 30, y); gc.arc(x + 30, y + 20, 20, 20, 90, -180); gc.lineTo(x, y + 40); gc.closePath(); gc.stroke();
                    if (type == GateType.NAND) gc.strokeOval(x + 50, y + 17, 6, 6);
                } else if (type == GateType.OR) {
                    gc.moveTo(x, y); gc.quadraticCurveTo(x + 15, y + 20, x, y + 40); gc.quadraticCurveTo(x + 35, y + 40, x + 60, y + 20); gc.quadraticCurveTo(x + 35, y, x, y); gc.closePath(); gc.stroke();
                } else if (type == GateType.NOT) {
                    gc.moveTo(x, y); gc.lineTo(x + 40, y + 20); gc.lineTo(x, y + 40); gc.closePath(); gc.stroke(); gc.strokeOval(x + 40, y + 17, 6, 6);
                }
                gc.setFill(Color.BLACK); gc.fillText(type.name(), x + 10, y + 25);
            }
            gc.setFill(Color.BLACK); for (Port p : inputs) gc.fillOval(p.x - 3, p.y - 3, 6, 6); for (Port p : outputs) gc.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }
}
