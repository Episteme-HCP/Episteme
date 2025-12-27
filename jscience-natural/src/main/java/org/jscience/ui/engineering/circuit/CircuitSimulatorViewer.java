/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Electrical Circuit Schematic Designer.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
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

    private static class Component {
        ComponentType type;
        double x1, y1, x2, y2;
        double value;
        String unit;

        Component(ComponentType type, double x1, double y1, double x2, double y2) {
            this.type = type;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            switch (type) {
                case RESISTOR -> {
                    value = 1000;
                    unit = "Ω";
                }
                case BATTERY -> {
                    value = 9;
                    unit = "V";
                }
                case CAPACITOR -> {
                    value = 10;
                    unit = "µF";
                }
                default -> {
                    value = 0;
                    unit = "";
                }
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

        // Toolbar
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
                new Separator(),
                new Button("Clear") {
                    {
                        setOnAction(e -> {
                            components.clear();
                            draw();
                        });
                    }
                });
        root.setTop(toolbar);

        // Canvas
        canvas = new Canvas(1000, 700);
        draw(); // Initial grid

        canvas.setOnMousePressed(e -> {
            startX = snap(e.getX());
            startY = snap(e.getY());
            dragging = true;
        });

        canvas.setOnMouseDragged(e -> {
            draw();
            drawComponentGhost(startX, startY, snap(e.getX()), snap(e.getY()));
        });

        canvas.setOnMouseReleased(e -> {
            if (dragging) {
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

        // Details panel
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.getChildren().add(new Label("Click and drag on grid to place components."));
        sidebar.getChildren().add(new Label("Components snap to 20px grid."));
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
        btn.setOnAction(e -> selectedType = type);
        if (type == ComponentType.WIRE)
            btn.setSelected(true);
        return btn;
    }

    private double snap(double val) {
        return Math.round(val / 20.0) * 20.0;
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Grid
        gc.setStroke(Color.web("#ddd"));
        gc.setLineWidth(1);
        for (int x = 0; x < canvas.getWidth(); x += 20) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (int y = 0; y < canvas.getHeight(); y += 20) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }

        // Components
        for (Component c : components) {
            drawComponent(gc, c);
        }
    }

    private void drawComponent(GraphicsContext gc, Component c) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        double midX = (c.x1 + c.x2) / 2;
        double midY = (c.y1 + c.y2) / 2;

        switch (c.type) {
            case WIRE -> gc.strokeLine(c.x1, c.y1, c.x2, c.y2);
            case RESISTOR -> {
                gc.strokeLine(c.x1, c.y1, midX - 10, midY); // Lead 1
                gc.strokeLine(midX + 10, midY, c.x2, c.y2); // Lead 2
                gc.strokeRect(midX - 10, midY - 5, 20, 10);
                gc.fillText(c.value + c.unit, midX - 15, midY - 10);
            }
            case BATTERY -> {
                gc.strokeLine(c.x1, c.y1, midX - 5, midY);
                gc.strokeLine(midX + 5, midY, c.x2, c.y2);
                gc.strokeLine(midX - 5, midY - 15, midX - 5, midY + 15); // Long bar (+)
                gc.strokeLine(midX + 5, midY - 8, midX + 5, midY + 8); // Short bar (-)
                gc.fillText(c.value + c.unit, midX - 10, midY - 20);
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
                gc.strokeLine(midX - 5, midY + 5, midX + 5, midY - 5); // Needle
            }
        }
    }

    private void drawComponentGhost(double x1, double y1, double x2, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setGlobalAlpha(0.5);
        Component ghost = new Component(selectedType, x1, y1, x2, y2);
        drawComponent(gc, ghost);
        gc.setGlobalAlpha(1.0);
    }

    public static void show(Stage stage) {
        new CircuitSimulatorViewer().start(stage);
    }
}
