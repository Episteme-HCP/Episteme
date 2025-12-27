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

        private Gate selected = null;
        private Port dragSource = null;

        private double dragOffsetX, dragOffsetY;

        @Override
        public void start(Stage stage) {
            BorderPane root = new BorderPane();
            Canvas canvas = new Canvas(1000, 700);
            root.setCenter(canvas);

            VBox toolbar = new VBox(10);
            toolbar.setStyle("-fx-padding: 10; -fx-background-color: #ddd;");

            Button btnAnd = new Button(I18n.getInstance().get("digital.add.and"));
            btnAnd.setOnAction(e -> addGate(new Gate(GateType.AND, 100, 100)));
            Button btnOr = new Button(I18n.getInstance().get("digital.add.or"));
            btnOr.setOnAction(e -> addGate(new Gate(GateType.OR, 100, 150)));
            Button btnNot = new Button(I18n.getInstance().get("digital.add.not"));
            btnNot.setOnAction(e -> addGate(new Gate(GateType.NOT, 100, 200)));
            Button btnIn = new Button(I18n.getInstance().get("digital.add.in"));
            btnIn.setOnAction(e -> addGate(new Gate(GateType.INPUT, 50, 100)));
            Button btnOut = new Button(I18n.getInstance().get("digital.add.out"));
            btnOut.setOnAction(e -> addGate(new Gate(GateType.OUTPUT, 300, 100)));

            Button clear = new Button(I18n.getInstance().get("digital.clear"));
            clear.setOnAction(e -> {
                gates.clear();
                wires.clear();
                draw(canvas);
            });

            toolbar.getChildren().addAll(new Label(I18n.getInstance().get("digital.components")), btnIn, btnOut, btnAnd,
                    btnOr, btnNot, clear);
            root.setLeft(toolbar);

            // Interaction
            canvas.setOnMousePressed(e -> {
                double mx = e.getX(), my = e.getY();
                // Check Ports first
                for (Gate g : gates) {
                    for (Port p : g.inputs) {
                        if (p.contains(mx, my)) {
                            // Trying to connect to input? Usually drag FROM output TO input
                        }
                    }
                    for (Port p : g.outputs) {
                        if (p.contains(mx, my)) {
                            dragSource = p;
                            return;
                        }
                    }
                }

                // Check Body
                for (Gate g : gates) {
                    if (g.contains(mx, my)) {
                        selected = g;
                        dragOffsetX = mx - g.x;
                        dragOffsetY = my - g.y;
                        if (g.type == GateType.INPUT) {
                            g.isOn = !g.isOn; // Toggle switch
                            simulate();
                            draw(canvas);
                        }
                        return;
                    }
                }
                selected = null;
            });

            canvas.setOnMouseDragged(e -> {
                if (dragSource != null) {
                    // Draw temp line
                    draw(canvas);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.BLACK);
                    gc.strokeLine(dragSource.x, dragSource.y, e.getX(), e.getY());
                } else if (selected != null) {
                    selected.x = e.getX() - dragOffsetX;
                    selected.y = e.getY() - dragOffsetY;
                    selected.updatePorts();
                    simulate(); // Wires move
                    draw(canvas);
                }
            });

            canvas.setOnMouseReleased(e -> {
                if (dragSource != null) {
                    // Check if dropped on input port
                    for (Gate g : gates) {
                        for (Port p : g.inputs) {
                            if (p.contains(e.getX(), e.getY())) {
                                wires.add(new Wire(dragSource, p));
                                simulate();
                            }
                        }
                    }
                    dragSource = null;
                    draw(canvas);
                }
            });

            // Initial Demo Circuit
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

            simulate();
            draw(canvas);

            Scene scene = new Scene(root, 1000, 700);
            org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
            stage.setTitle(I18n.getInstance().get("digital.title"));
            stage.setScene(scene);
            stage.show();
        }

        private void addGate(Gate g) {
            gates.add(g);
            draw(null);
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
            if (canvas == null)
                return;
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Wires
            for (Wire w : wires) {
                gc.setStroke(w.start.parent.isOn ? Color.RED : Color.BLACK); // Red = High signal
                gc.setLineWidth(3);
                // Bezier
                gc.beginPath();
                gc.moveTo(w.start.x, w.start.y);
                gc.bezierCurveTo(w.start.x + 50, w.start.y, w.end.x - 50, w.end.y, w.end.x, w.end.y);
                gc.stroke();
            }

            // Gates
            for (Gate g : gates) {
                g.draw(gc);
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

        void draw(GraphicsContext gc) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);

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
                gc.strokeRect(x, y, 60, 40);
                gc.fillText(type.toString(), x + 10, y + 25);
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
