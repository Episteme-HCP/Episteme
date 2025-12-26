/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.geometry;

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
 * Interactive 2D Geometry Board (GeoGebra-style).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class GeometryBoardViewer extends Application {

    private enum Tool {
        POINT, LINE, CIRCLE, SELECT
    }

    private Tool selectedTool = Tool.POINT;

    private static class GeometricObject {
        Tool type;
        double x1, y1, x2, y2;

        GeometricObject(Tool type, double x1, double y1, double x2, double y2) {
            this.type = type;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private List<GeometricObject> objects = new ArrayList<>();
    private Canvas canvas;
    private double startX, startY;
    private boolean dragging = false;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #f0f0f0;");

        ToggleGroup group = new ToggleGroup();
        toolbar.getChildren().addAll(
                createToolBtn("Point", Tool.POINT, group),
                createToolBtn("Line", Tool.LINE, group),
                createToolBtn("Circle", Tool.CIRCLE, group),
                createToolBtn("Select", Tool.SELECT, group),
                new Separator(),
                new Button("Clear") {
                    {
                        setOnAction(e -> {
                            objects.clear();
                            draw();
                        });
                    }
                });
        root.setTop(toolbar);

        canvas = new Canvas(1000, 700);
        draw();

        canvas.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            switch (selectedTool) {
                case SELECT:
                    // For SELECT tool, we might want to select an existing object
                    // For now, we'll just allow dragging behavior for potential future object
                    // movement
                    dragging = true;
                    break;
                case POINT:
                    objects.add(new GeometricObject(Tool.POINT, startX, startY, startX, startY));
                    draw();
                    break;
                default: // LINE, CIRCLE
                    dragging = true;
                    break;
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (dragging) {
                draw();
                drawGhost(startX, startY, e.getX(), e.getY());
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (dragging) {
                objects.add(new GeometricObject(selectedTool, startX, startY, e.getX(), e.getY()));
                dragging = false;
                draw();
            }
        });

        root.setCenter(new ScrollPane(canvas));

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #fafafa;");
        sidebar.getChildren()
                .add(new Label("Exploration:\n1. Place points\n2. Draw lines through points\n3. Create circles"));
        root.setRight(sidebar);

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.geometry"));
        stage.setScene(scene);
        stage.show();
    }

    private ToggleButton createToolBtn(String name, Tool tool, ToggleGroup group) {
        ToggleButton b = new ToggleButton(name);
        b.setToggleGroup(group);
        b.setOnAction(e -> selectedTool = tool);
        if (tool == Tool.POINT)
            b.setSelected(true);
        return b;
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Background grid dots
        gc.setFill(Color.LIGHTGRAY);
        for (int x = 0; x < canvas.getWidth(); x += 50) {
            for (int y = 0; y < canvas.getHeight(); y += 50) {
                gc.fillOval(x - 1, y - 1, 2, 2);
            }
        }

        for (GeometricObject obj : objects) {
            drawObject(gc, obj, Color.BLACK);
        }
    }

    private void drawObject(GraphicsContext gc, GeometricObject obj, Color color) {
        gc.setStroke(color);
        gc.setFill(color);
        gc.setLineWidth(2);

        switch (obj.type) {
            case SELECT -> {
            }
            case POINT -> gc.fillOval(obj.x1 - 4, obj.y1 - 4, 8, 8);
            case LINE -> {
                // Extend line to boundaries
                double dx = obj.x2 - obj.x1;
                double dy = obj.y2 - obj.y1;
                if (Math.abs(dx) < 0.1 && Math.abs(dy) < 0.1)
                    return;

                gc.strokeLine(obj.x1 - dx * 100, obj.y1 - dy * 100, obj.x2 + dx * 100, obj.y2 + dy * 100);
            }
            case CIRCLE -> {
                double r = Math.sqrt(Math.pow(obj.x2 - obj.x1, 2) + Math.pow(obj.y2 - obj.y1, 2));
                gc.strokeOval(obj.x1 - r, obj.y1 - r, 2 * r, 2 * r);
            }
        }
    }

    private void drawGhost(double x1, double y1, double x2, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setGlobalAlpha(0.3);
        drawObject(gc, new GeometricObject(selectedTool, x1, y1, x2, y2), Color.BLUE);
        gc.setGlobalAlpha(1.0);
    }

    public static void show(Stage stage) {
        new GeometryBoardViewer().start(stage);
    }
}
