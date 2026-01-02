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

package org.jscience.ui.engineering.circuit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Electrical Circuit Schematic Designer using JScience Quantity
 * types.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitSimulatorViewer extends Application {

    private enum ComponentType {
        RESISTOR("circuit.component.resistor"),
        CAPACITOR("circuit.component.capacitor"),
        INDUCTOR("circuit.component.inductor"),
        BATTERY("circuit.component.battery"),
        WIRE("circuit.component.wire"),
        GROUND("circuit.component.ground"),
        METER("circuit.component.meter");

        private final String i18nKey;

        ComponentType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    private ComponentType selectedType = ComponentType.WIRE;
    private boolean isSelectMode = false;
    private Component selectedComponent = null;

    private static class Component {
        ComponentType type;
        double x1, y1, x2, y2;
        Quantity<?> value;

        Component(ComponentType type, double x1, double y1, double x2, double y2) {
            this.type = type;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            switch (type) {
                case RESISTOR -> value = Quantities.create(1000.0, Units.OHM);
                case BATTERY -> value = Quantities.create(9.0, Units.VOLT);
                case CAPACITOR -> value = Quantities.create(10e-6, Units.FARAD);
                case INDUCTOR -> value = Quantities.create(1e-3, Units.HENRY);
                default -> value = Quantities.create(0.0, Units.ONE);
            }
        }
    }

    private List<Component> components = new ArrayList<>();
    private double startX, startY;
    private boolean dragging = false;
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.getStyleClass().add("viewer-toolbar");

        ToggleGroup group = new ToggleGroup();
        toolbar.getChildren().addAll(
                createToolButton("Wire", ComponentType.WIRE, group),
                createToolButton("Resistor", ComponentType.RESISTOR, group),
                createToolButton("Battery", ComponentType.BATTERY, group),
                createToolButton("Capacitor", ComponentType.CAPACITOR, group),
                createToolButton("Inductor", ComponentType.INDUCTOR, group),
                createToolButton("Ground", ComponentType.GROUND, group),
                createToolButton("Meter", ComponentType.METER, group),
                new Separator(),
                createSelectButton(group),
                new Button("Delete") {
                    {
                        setOnAction(e -> {
                            if (selectedComponent != null) {
                                components.remove(selectedComponent);
                                selectedComponent = null;
                                draw();
                            }
                        });
                    }
                },
                new Button("Clear") {
                    {
                        setOnAction(e -> {
                            components.clear();
                            selectedComponent = null;
                            draw();
                        });
                    }
                });
        root.setTop(toolbar);

        canvas = new Canvas(1000, 700);
        draw();

        canvas.setOnMousePressed(e -> {
            if (isSelectMode) {
                handleSelection(e.getX(), e.getY());
            } else {
                startX = snap(e.getX());
                startY = snap(e.getY());
                dragging = true;
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (!isSelectMode) {
                draw();
                drawComponentGhost(startX, startY, snap(e.getX()), snap(e.getY()));
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (dragging && !isSelectMode) {
                double endX = snap(e.getX());
                double endY = snap(e.getY());
                if (startX != endX || startY != endY) {
                    components.add(new Component(selectedType, startX, startY, endX, endY));
                }
                dragging = false;
                draw();
            }
        });

        root.setCenter(new ScrollPane(canvas));

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.getChildren().addAll(
                new Label("Click and drag on grid to place components."),
                new Label("Components snap to 20px grid."),
                new Label("JScience Quantities used for Electromagnetism."));
        root.setRight(sidebar);

        Scene scene = new Scene(root, 1200, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.circuit"));
        stage.setScene(scene);
        stage.show();
    }

    private ToggleButton createToolButton(String name, ComponentType type, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(name);
        btn.setToggleGroup(group);
        btn.setOnAction(e -> {
            selectedType = type;
            isSelectMode = false;
            selectedComponent = null;
            draw();
        });
        if (type == ComponentType.WIRE)
            btn.setSelected(true);
        return btn;
    }

    private ToggleButton createSelectButton(ToggleGroup group) {
        ToggleButton btn = new ToggleButton("Select");
        btn.setToggleGroup(group);
        btn.setOnAction(e -> isSelectMode = true);
        return btn;
    }

    private void handleSelection(double x, double y) {
        selectedComponent = null;
        for (Component c : components) {
            double dist = distancePointToSegment(x, y, c.x1, c.y1, c.x2, c.y2);
            if (dist < 10) {
                selectedComponent = c;
                break;
            }
        }
        draw();
    }

    private double distancePointToSegment(double x, double y, double x1, double y1, double x2, double y2) {
        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;
        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = (len_sq != 0) ? dot / len_sq : -1;
        double xx, yy;
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        return Math.sqrt(Math.pow(x - xx, 2) + Math.pow(y - yy, 2));
    }

    private double snap(double val) {
        return Math.round(val / 20.0) * 20.0;
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.web("#ddd"));
        gc.setLineWidth(1);
        for (int x = 0; x < canvas.getWidth(); x += 20)
            gc.strokeLine(x, 0, x, canvas.getHeight());
        for (int y = 0; y < canvas.getHeight(); y += 20)
            gc.strokeLine(0, y, canvas.getWidth(), y);
        for (Component c : components)
            drawComponent(gc, c, c == selectedComponent);
    }

    private void drawComponent(GraphicsContext gc, Component c, boolean selected) {
        gc.setStroke(selected ? Color.RED : Color.BLACK);
        gc.setLineWidth(selected ? 3 : 2);
        double midX = (c.x1 + c.x2) / 2;
        double midY = (c.y1 + c.y2) / 2;
        switch (c.type) {
            case WIRE -> gc.strokeLine(c.x1, c.y1, c.x2, c.y2);
            case RESISTOR -> {
                gc.strokeLine(c.x1, c.y1, midX - 10, midY);
                gc.strokeLine(midX + 10, midY, c.x2, c.y2);
                gc.strokeRect(midX - 10, midY - 5, 20, 10);
                gc.fillText(c.value.toString(), midX - 15, midY - 10);
            }
            case BATTERY -> {
                gc.strokeLine(c.x1, c.y1, midX - 5, midY);
                gc.strokeLine(midX + 5, midY, c.x2, c.y2);
                gc.strokeLine(midX - 5, midY - 15, midX - 5, midY + 15);
                gc.strokeLine(midX + 5, midY - 8, midX + 5, midY + 8);
                gc.fillText(c.value.toString(), midX - 10, midY - 20);
            }
            case CAPACITOR -> {
                gc.strokeLine(c.x1, c.y1, midX - 5, midY);
                gc.strokeLine(midX + 5, midY, c.x2, c.y2);
                gc.strokeLine(midX - 5, midY - 10, midX - 5, midY + 10);
                gc.strokeLine(midX + 5, midY - 10, midX + 5, midY + 10);
            }
            case GROUND -> {
                gc.strokeLine(c.x1, c.y1, midX, midY);
                gc.strokeLine(midX - 10, midY, midX + 10, midY);
                gc.strokeLine(midX - 6, midY + 4, midX + 6, midY + 4);
                gc.strokeLine(midX - 2, midY + 8, midX + 2, midY + 8);
            }
            case INDUCTOR -> {
                gc.strokeLine(c.x1, c.y1, midX - 10, midY);
                gc.strokeLine(midX + 10, midY, c.x2, c.y2);
                gc.strokeArc(midX - 10, midY - 5, 10, 10, 0, 180, javafx.scene.shape.ArcType.OPEN);
                gc.strokeArc(midX, midY - 5, 10, 10, 0, 180, javafx.scene.shape.ArcType.OPEN);
            }
            case METER -> {
                gc.strokeLine(c.x1, c.y1, midX - 10, midY);
                gc.strokeLine(midX + 10, midY, c.x2, c.y2);
                gc.strokeOval(midX - 10, midY - 10, 20, 20);
                gc.strokeLine(midX - 5, midY + 5, midX + 5, midY - 5);
            }
        }
    }

    private void drawComponentGhost(double x1, double y1, double x2, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setGlobalAlpha(0.5);
        Component ghost = new Component(selectedType, x1, y1, x2, y2);
        drawComponent(gc, ghost, false);
        gc.setGlobalAlpha(1.0);
    }

    public static void show(Stage stage) {
        new CircuitSimulatorViewer().start(stage);
    }
}
