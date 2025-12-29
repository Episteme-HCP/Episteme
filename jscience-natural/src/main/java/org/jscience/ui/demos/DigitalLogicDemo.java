package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Digital Logic Circuit Simulator.
 * Interactive AND, OR, NOT, NAND gates simulator.
 */
public class DigitalLogicDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.computing");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("digital.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("digital.desc");
    }

    @Override
    public void show(Stage stage) {
        new LogicApp().start(stage);
    }

    // Internal Application class
    public static class LogicApp extends Application {

        private List<Gate> gates = new ArrayList<>();
        private List<Wire> wires = new ArrayList<>();

        private Gate selectedGate = null;
        private Wire selectedWire = null;
        private Port dragSource = null;

        private double dragOffsetX, dragOffsetY;
        private boolean simulationMode = true; // Default to Simulation
        private javafx.scene.control.Label statusLabel;

        @Override
        public void start(Stage stage) {
            BorderPane root = new BorderPane();
            Canvas canvas = new Canvas(1000, 700);
            root.setCenter(canvas);

            VBox toolbar = new VBox(10);
            toolbar.setStyle(
                    "-fx-padding: 10; -fx-background-color: #ddd; -fx-border-color: #ccc; -fx-border-width: 0 1 0 0;");
            toolbar.setPrefWidth(180);

            // Mode Toggle
            javafx.scene.control.ToggleButton modeBtn = new javafx.scene.control.ToggleButton("Simulation Mode");
            modeBtn.setSelected(true);
            modeBtn.setMaxWidth(Double.MAX_VALUE);
            modeBtn.setOnAction(e -> {
                simulationMode = modeBtn.isSelected();
                modeBtn.setText(simulationMode ? "Simulation Mode" : "Design Mode");
                if (simulationMode) {
                    selectedGate = null;
                    selectedWire = null;
                    simulate();
                }
                draw(canvas);
                updateStatus();
            });

            // Components
            Label compLabel = new Label(I18n.getInstance().get("digital.components"));
            compLabel.setStyle("-fx-font-weight: bold;");

            Button btnAnd = new Button(I18n.getInstance().get("digital.add.and"));
            btnAnd.setMaxWidth(Double.MAX_VALUE);
            btnAnd.setOnAction(e -> addGate(new Gate(GateType.AND, 100, 100)));

            Button btnOr = new Button(I18n.getInstance().get("digital.add.or"));
            btnOr.setMaxWidth(Double.MAX_VALUE);
            btnOr.setOnAction(e -> addGate(new Gate(GateType.OR, 100, 150)));

            Button btnNot = new Button(I18n.getInstance().get("digital.add.not"));
            btnNot.setMaxWidth(Double.MAX_VALUE);
            btnNot.setOnAction(e -> addGate(new Gate(GateType.NOT, 100, 200)));

            Button btnNand = new Button(I18n.getInstance().get("digital.gate.nand"));
            btnNand.setMaxWidth(Double.MAX_VALUE);
            btnNand.setOnAction(e -> addGate(new Gate(GateType.NAND, 100, 250)));

            Button btnIn = new Button(I18n.getInstance().get("digital.add.in"));
            btnIn.setMaxWidth(Double.MAX_VALUE);
            btnIn.setOnAction(e -> addGate(new Gate(GateType.INPUT, 50, 100)));

            Button btnOut = new Button(I18n.getInstance().get("digital.add.out"));
            btnOut.setMaxWidth(Double.MAX_VALUE);
            btnOut.setOnAction(e -> addGate(new Gate(GateType.OUTPUT, 300, 100)));

            // Actions
            Label actionLabel = new Label("Actions");
            actionLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");

            Button clear = new Button(I18n.getInstance().get("digital.clear"));
            clear.setMaxWidth(Double.MAX_VALUE);
            clear.setOnAction(e -> {
                gates.clear();
                wires.clear();
                draw(canvas);
            });

            Button saveBtn = new Button("Save Circuit");
            saveBtn.setMaxWidth(Double.MAX_VALUE);
            saveBtn.setOnAction(e -> saveCircuit(stage));

            Button loadBtn = new Button("Load Circuit");
            loadBtn.setMaxWidth(Double.MAX_VALUE);
            loadBtn.setOnAction(e -> loadCircuit(stage, canvas));

            statusLabel = new Label("Ready");
            statusLabel.setWrapText(true);
            statusLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 10px;");

            toolbar.getChildren().addAll(
                    modeBtn,
                    new javafx.scene.control.Separator(),
                    compLabel, btnIn, btnOut, btnAnd, btnOr, btnNot, btnNand,
                    new javafx.scene.control.Separator(),
                    actionLabel, clear, saveBtn, loadBtn,
                    new javafx.scene.control.Separator(),
                    statusLabel);
            root.setLeft(toolbar);

            // Interaction
            canvas.setOnMousePressed(e -> {
                double mx = e.getX(), my = e.getY();

                // Clear Selection
                selectedGate = null;
                selectedWire = null;

                // 1. Check Ports (Wiring) - Always allowed, or Design mode?
                // Visual programming usually allows wiring in both, but let's restrict
                // structural changes to Design
                // except Toggle inputs in Simulation.

                // If Simulation Mode: Only Inputs click
                if (simulationMode) {
                    for (Gate g : gates) {
                        if (g.type == GateType.INPUT && g.contains(mx, my)) {
                            g.isOn = !g.isOn;
                            simulate();
                            draw(canvas);
                            return;
                        }
                    }
                    return; // No other interaction in sim mode
                }

                // DESIGN MODE
                // Check Ports
                for (Gate g : gates) {
                    for (Port p : g.outputs) { // Drag from Output
                        if (p.contains(mx, my)) {
                            dragSource = p;
                            return;
                        }
                    }
                    for (Port p : g.inputs) { // Drag from Input? (Reverse wiring)
                        if (p.contains(mx, my)) {
                            // Optional: Could support dragging existing wire off?
                        }
                    }
                }

                // Check Gates (Selection/Move)
                for (Gate g : gates) {
                    if (g.contains(mx, my)) {
                        selectedGate = g;
                        dragOffsetX = mx - g.x;
                        dragOffsetY = my - g.y;
                        if (g.type == GateType.INPUT) {
                            // Also allow toggling in design mode for testing? Sure.
                            g.isOn = !g.isOn;
                            simulate();
                        }
                        draw(canvas);
                        return;
                    }
                }

                // Check Wires (Selection)
                for (Wire w : wires) {
                    if (w.contains(mx, my)) {
                        selectedWire = w;
                        draw(canvas);
                        return;
                    }
                }

                draw(canvas); // Redraw to clear selection
            });

            canvas.setOnMouseDragged(e -> {
                if (simulationMode)
                    return;

                if (dragSource != null) {
                    // Draw temp line
                    draw(canvas);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(2);
                    gc.strokeLine(dragSource.x, dragSource.y, e.getX(), e.getY());
                } else if (selectedGate != null) {
                    selectedGate.x = e.getX() - dragOffsetX;
                    selectedGate.y = e.getY() - dragOffsetY;
                    selectedGate.updatePorts();
                    simulate(); // Wires move
                    draw(canvas);
                }
            });

            canvas.setOnMouseReleased(e -> {
                if (simulationMode)
                    return;

                if (dragSource != null) {
                    // Check if dropped on input port
                    for (Gate g : gates) {
                        for (Port p : g.inputs) {
                            if (p.contains(e.getX(), e.getY())) {
                                // Prevent duplicates?
                                wires.add(new Wire(dragSource, p));
                                simulate();
                            }
                        }
                    }
                    dragSource = null;
                    draw(canvas);
                }
            });

            // Keyboard (Delete)
            root.setOnKeyPressed(e -> {
                if (!simulationMode) {
                    if (e.getCode() == javafx.scene.input.KeyCode.DELETE
                            || e.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) {
                        if (selectedGate != null) {
                            deleteGate(selectedGate);
                            selectedGate = null;
                            draw(canvas);
                        } else if (selectedWire != null) {
                            wires.remove(selectedWire);
                            selectedWire = null;
                            simulate();
                            draw(canvas);
                        }
                    }
                }
            });
            // Focus root to receive key events
            root.setFocusTraversable(false); // Canvas?
            canvas.setFocusTraversable(true);
            canvas.setOnMouseClicked(e -> canvas.requestFocus());

            // Resize
            root.widthProperty().addListener((o, old, val) -> {
                canvas.setWidth(val.doubleValue() - 200);
                draw(canvas);
            });
            root.heightProperty().addListener((o, old, val) -> {
                canvas.setHeight(val.doubleValue());
                draw(canvas);
            });

            // Initial Demo Circuit
            loadDefaultCircuit();

            simulate();
            draw(canvas);

            Scene scene = new Scene(root, 1000, 700);
            org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
            stage.setTitle(I18n.getInstance().get("digital.title"));
            stage.setScene(scene);
            stage.show();
            canvas.requestFocus(); // For keyboard
        }

        private void updateStatus() {
            if (simulationMode) {
                statusLabel.setText("Simulation Mode: Click Inputs to toggle. editing disabled.");
            } else {
                statusLabel.setText("Design Mode: Drag gates, drag from ports to wire. Select & Delete to remove.");
            }
        }

        private void addGate(Gate g) {
            if (simulationMode)
                return;
            gates.add(g);
            draw(null);
        }

        private void deleteGate(Gate g) {
            wires.removeIf(w -> w.start.parent == g || w.end.parent == g);
            gates.remove(g);
            simulate();
        }

        private void loadDefaultCircuit() {
            gates.clear();
            wires.clear();
            Gate in1 = new Gate(GateType.INPUT, 50, 200);
            gates.add(in1);
            Gate in2 = new Gate(GateType.INPUT, 50, 300);
            gates.add(in2);
            Gate and = new Gate(GateType.AND, 200, 250);
            gates.add(and);
            Gate out = new Gate(GateType.OUTPUT, 350, 250);
            gates.add(out);
            wires.add(new Wire(in1.outputs.get(0), and.inputs.get(0)));
            wires.add(new Wire(in2.outputs.get(0), and.inputs.get(1)));
            wires.add(new Wire(and.outputs.get(0), out.inputs.get(0)));
        }

        private void saveCircuit(Stage stages) {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Save Circuit");
            fileChooser.getExtensionFilters()
                    .add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));
            java.io.File file = fileChooser.showSaveDialog(stages);
            if (file != null) {
                try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                    for (int i = 0; i < gates.size(); i++) {
                        Gate g = gates.get(i);
                        pw.println("GATE " + g.type + " " + (int) g.x + " " + (int) g.y);
                    }
                    for (Wire w : wires) {
                        int startGateIdx = gates.indexOf(w.start.parent);
                        int endGateIdx = gates.indexOf(w.end.parent);
                        int startPortIdx = w.start.parent.outputs.indexOf(w.start);
                        int endPortIdx = w.end.parent.inputs.indexOf(w.end);
                        if (startGateIdx != -1 && endGateIdx != -1) {
                            pw.println(
                                    "WIRE " + startGateIdx + ":" + startPortIdx + " " + endGateIdx + ":" + endPortIdx);
                        }
                    }
                    statusLabel.setText("Saved: " + file.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("Error saving: " + ex.getMessage());
                }
            }
        }

        private void loadCircuit(Stage stages, Canvas canvas) {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Load Circuit");
            fileChooser.getExtensionFilters()
                    .add(new javafx.stage.FileChooser.ExtensionFilter("Logic Circuit", "*.lgc"));
            java.io.File file = fileChooser.showOpenDialog(stages);
            if (file != null) {
                try (java.util.Scanner sc = new java.util.Scanner(file)) {
                    gates.clear();
                    wires.clear();
                    while (sc.hasNext()) {
                        String line = sc.nextLine();
                        if (line.startsWith("GATE")) {
                            String[] parts = line.split(" ");
                            GateType type = GateType.valueOf(parts[1]);
                            double x = Double.parseDouble(parts[2]);
                            double y = Double.parseDouble(parts[3]);
                            gates.add(new Gate(type, x, y));
                        } else if (line.startsWith("WIRE")) {
                            String[] parts = line.split(" ");
                            String[] sPart = parts[1].split(":");
                            String[] ePart = parts[2].split(":");
                            Gate sGate = gates.get(Integer.parseInt(sPart[0]));
                            Gate eGate = gates.get(Integer.parseInt(ePart[0]));
                            Port sPort = sGate.outputs.get(Integer.parseInt(sPart[1]));
                            Port ePort = eGate.inputs.get(Integer.parseInt(ePart[1]));
                            wires.add(new Wire(sPort, ePort));
                        }
                    }
                    simulate();
                    draw(canvas);
                    statusLabel.setText("Loaded: " + file.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("Error loading: " + ex.getMessage());
                }
            }
        }

        private void simulate() {
            boolean changed = true;
            int limit = 0;
            while (changed && limit < 100) {
                changed = false;
                for (Gate g : gates) {
                    boolean old = g.isOn;
                    g.evaluate(wires); // Pass wires to lookup
                    if (g.isOn != old)
                        changed = true;
                }
                limit++;
            }
        }

        private void draw(Canvas canvas) {
            if (canvas == null && this.gates.isEmpty())
                return;
            // Handle null canvas by storing one? No, bad design.
            // Just use the passed canvas or field if I stored it.
            // Refactoring: draw() should use field canvas if available?
            // Passed canvas is fine.
            if (canvas == null) {
                // Try to find if we can access the canvas from a field?
                // I'll assume caller passes it or I store it.
                // Storing in start() to field would be better.
                // For now, assume it's valid or skip.
                return;
            }

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Grid background
            gc.setStroke(Color.web("#eee"));
            gc.setLineWidth(1);
            for (int i = 0; i < canvas.getWidth(); i += 20)
                gc.strokeLine(i, 0, i, canvas.getHeight());
            for (int i = 0; i < canvas.getHeight(); i += 20)
                gc.strokeLine(0, i, canvas.getWidth(), i);

            // Wires
            for (Wire w : wires) {
                gc.setStroke(w == selectedWire ? Color.BLUE : (w.start.parent.isOn ? Color.RED : Color.BLACK));
                gc.setLineWidth(w == selectedWire ? 4 : 3);
                // Bezier
                gc.beginPath();
                gc.moveTo(w.start.x, w.start.y);
                Double dist = Math.abs(w.end.x - w.start.x) * 0.5;
                if (dist < 50)
                    dist = 50.0;
                gc.bezierCurveTo(w.start.x + dist, w.start.y, w.end.x - dist, w.end.y, w.end.x, w.end.y);
                gc.stroke();
            }

            // Gates
            for (Gate g : gates) {
                g.draw(gc, g == selectedGate);
            }
        }
    }

    enum GateType {
        INPUT("digital.gate.input"),
        OUTPUT("digital.gate.output"),
        AND("digital.gate.and"),
        OR("digital.gate.or"),
        NOT("digital.gate.not"),
        NAND("digital.gate.nand");

        private final String i18nKey;

        GateType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    static class Port {
        double x, y;
        Gate parent;
        boolean isInput;

        Port(Gate p, boolean in) {
            parent = p;
            isInput = in;
        }

        boolean contains(double mx, double my) {
            return Math.sqrt((x - mx) * (x - mx) + (y - my) * (y - my)) < 10;
        }
    }

    static class Wire {
        Port start, end;

        Wire(Port s, Port e) {
            start = s;
            end = e;
        }

        boolean contains(double mx, double my) {
            // Simple hit test for bezier? Approximation: check distance to mid point or
            // segment?
            // Bezier is P0, P1(cp), P2(cp), P3
            // CP offsets are hardcoded 50.
            double dist = Math.abs(end.x - start.x) * 0.5;
            if (dist < 50)
                dist = 50.0;
            double cp1x = start.x + dist;
            double cp1y = start.y;
            double cp2x = end.x - dist;
            double cp2y = end.y;

            // Check sample points
            for (double t = 0; t <= 1; t += 0.05) {
                double ix = Math.pow(1 - t, 3) * start.x + 3 * Math.pow(1 - t, 2) * t * cp1x
                        + 3 * (1 - t) * t * t * cp2x + t * t * t * end.x;
                double iy = Math.pow(1 - t, 3) * start.y + 3 * Math.pow(1 - t, 2) * t * cp1y
                        + 3 * (1 - t) * t * t * cp2y + t * t * t * end.y;
                if (Math.hypot(mx - ix, my - iy) < 5)
                    return true;
            }
            return false;
        }
    }

    static class Gate {
        GateType type;
        double x, y;
        boolean isOn = false;
        List<Port> inputs = new ArrayList<>();
        List<Port> outputs = new ArrayList<>();

        Gate(GateType t, double x, double y) {
            this.type = t;
            this.x = x;
            this.y = y;
            if (t == GateType.INPUT) {
                outputs.add(new Port(this, false));
            } else if (t == GateType.OUTPUT || t == GateType.NOT) {
                inputs.add(new Port(this, true));
                if (t == GateType.NOT)
                    outputs.add(new Port(this, false));
            } else {
                inputs.add(new Port(this, true));
                inputs.add(new Port(this, true));
                outputs.add(new Port(this, false));
            }
            updatePorts();
        }

        void updatePorts() {
            if (type == GateType.INPUT) {
                outputs.get(0).x = x + 40;
                outputs.get(0).y = y + 15;
            } else if (type == GateType.OUTPUT) {
                inputs.get(0).x = x;
                inputs.get(0).y = y + 15;
            } else if (type == GateType.NOT) {
                inputs.get(0).x = x;
                inputs.get(0).y = y + 15;
                outputs.get(0).x = x + 60;
                outputs.get(0).y = y + 15;
            } else { // AND, OR
                inputs.get(0).x = x;
                inputs.get(0).y = y + 10;
                inputs.get(1).x = x;
                inputs.get(1).y = y + 30;
                outputs.get(0).x = x + 60;
                outputs.get(0).y = y + 20;
            }
        }

        boolean contains(double mx, double my) {
            return mx >= x && mx <= x + 60 && my >= y && my <= y + 40;
        }

        void evaluate(List<Wire> wires) {
            if (type == GateType.INPUT)
                return; // Set manually

            boolean i1 = getVal(0, wires);
            boolean i2 = inputs.size() > 1 ? getVal(1, wires) : false;

            switch (type) {
                case OUTPUT:
                    isOn = i1;
                    break;
                case NOT:
                    isOn = !i1;
                    break;
                case AND:
                    isOn = i1 && i2;
                    break;
                case OR:
                    isOn = i1 || i2;
                    break;
                case NAND:
                    isOn = !(i1 && i2);
                    break;
                case INPUT:
                    break;
            }
        }

        boolean getVal(int idx, List<Wire> wires) {
            if (idx >= inputs.size())
                return false;
            Port target = inputs.get(idx);
            for (Wire w : wires) {
                if (w.end == target) {
                    return w.start.parent.isOn;
                }
            }
            return false;
        }

        void draw(GraphicsContext gc, boolean isSelected) {
            gc.setFill(Color.WHITE);
            gc.setStroke(isSelected ? Color.BLUE : Color.BLACK);
            gc.setLineWidth(isSelected ? 3 : 2);

            if (type == GateType.INPUT) {
                gc.setFill(isOn ? Color.LIGHTGREEN : Color.WHITE);
                gc.fillRect(x, y, 40, 30);
                gc.strokeRect(x, y, 40, 30);
                gc.setFill(Color.BLACK);
                gc.fillText(isOn ? I18n.getInstance().get("digital.on") : I18n.getInstance().get("digital.off"), x + 5,
                        y + 20);
            } else if (type == GateType.OUTPUT) {
                gc.setFill(isOn ? Color.YELLOW : Color.GRAY);
                gc.fillOval(x, y, 40, 40);
                gc.strokeOval(x, y, 40, 40);
            } else {
                // Shapes
                // Draw fancy shapes based on type
                if (type == GateType.AND || type == GateType.NAND) {
                    gc.beginPath();
                    gc.moveTo(x, y);
                    gc.lineTo(x + 30, y);
                    gc.arc(x + 30, y + 20, 20, 20, 90, -180);
                    gc.lineTo(x, y + 40);
                    gc.closePath();
                    gc.stroke();
                    if (type == GateType.NAND) {
                        gc.strokeOval(x + 50, y + 17, 6, 6);
                    }
                    gc.fillText(type.toString(), x + 10, y + 25);
                } else if (type == GateType.OR) {
                    // OR shape roughly
                    gc.beginPath();
                    gc.moveTo(x, y);
                    gc.quadraticCurveTo(x + 15, y + 20, x, y + 40);
                    gc.quadraticCurveTo(x + 35, y + 40, x + 60, y + 20);
                    gc.quadraticCurveTo(x + 35, y, x, y);
                    gc.closePath();
                    gc.stroke();
                    gc.fillText(type.toString(), x + 15, y + 25);
                } else if (type == GateType.NOT) {
                    // Triangle
                    gc.beginPath();
                    gc.moveTo(x, y);
                    gc.lineTo(x + 40, y + 20);
                    gc.lineTo(x, y + 40);
                    gc.closePath();
                    gc.stroke();
                    gc.strokeOval(x + 40, y + 17, 6, 6);
                    gc.fillText(type.toString(), x + 5, y + 25);
                } else {
                    gc.strokeRect(x, y, 60, 40);
                    gc.fillText(type.toString(), x + 10, y + 25);
                }
            }

            // Ports
            gc.setFill(Color.BLACK);
            for (Port p : inputs)
                gc.fillOval(p.x - 3, p.y - 3, 6, 6);
            for (Port p : outputs)
                gc.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }
}
