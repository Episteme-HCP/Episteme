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

package org.jscience.ui.viewers.mathematics.geometry;

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.ui.i18n.I18n;


/**
 * Interactive 2D Geometry Board (GeoGebra-style).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeometryBoardViewer extends AbstractViewer implements Simulatable {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("GeometryBoard.title", "Geometry Board");
    }

    @Override public void play() { /* No animation */ }
    @Override public void pause() { /* No animation */ }
    @Override public void stop() { objects.clear(); draw(); }
    @Override public void step() { /* No animation */ }
    @Override public void setSpeed(double multiplier) { }
    @Override public boolean isPlaying() { return false; }

    private enum Tool {
        POINT, LINE, CIRCLE, TRIANGLE, SELECT
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

    public GeometryBoardViewer() {
        this.getStyleClass().add("viewer-root");

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.getStyleClass().add("viewer-controls");

        ToggleGroup group = new ToggleGroup();
        toolbar.getChildren().addAll(
                createToolBtn(I18n.getInstance().get("geometry.tool.point"), Tool.POINT, group),
                createToolBtn(I18n.getInstance().get("geometry.tool.line"), Tool.LINE, group),
                createToolBtn(I18n.getInstance().get("geometry.tool.circle"), Tool.CIRCLE, group),
                createToolBtn("Triangle", Tool.TRIANGLE, group),
                createToolBtn(I18n.getInstance().get("geometry.tool.select"), Tool.SELECT, group),
                new Separator(),
                new Button(I18n.getInstance().get("geometry.button.clear")) {
                    {
                        setOnAction(e -> {
                            objects.clear();
                            draw();
                        });
                    }
                });
        setTop(toolbar);

        canvas = new Canvas(1000, 700);
        draw();

        canvas.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            switch (selectedTool) {
                case SELECT:
                    dragging = true;
                    break;
                case POINT:
                    objects.add(new GeometricObject(Tool.POINT, startX, startY, startX, startY));
                    draw();
                    break;
                default: // LINE, CIRCLE, TRIANGLE
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

        setCenter(new ScrollPane(canvas));

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.getChildren()
                .add(new Label(I18n.getInstance().get("geometry.help")));
        setRight(sidebar);
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

        // Draw Intersections (Red Dots)
        drawIntersections(gc);

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
            case TRIANGLE -> {
                // double[] xPoints = { obj.x1, obj.x2, obj.x1 - (obj.x2 - obj.x1) };
                // double[] yPoints = { obj.y1, obj.y2, obj.y2 };
                // Isosceles using bounding box logic
                // Let's make it simpler: Side 1 is (x1,y1) to (x2,y2). Point 3 is calculated to
                // make isosceles?
                // Or just width/height box?
                // Let's use bounding box: Start(x1,y1) is top-center, End(x2,y2) is
                // bottom-right corner?
                // No, start-drag-end.
                // Let's define: P1(x1, y2), P2(x2, y2), P3((x1+x2)/2, y1). Triangle inside the
                // box defined by drag.
                double minX = Math.min(obj.x1, obj.x2);
                double maxX = Math.max(obj.x1, obj.x2);
                double minY = Math.min(obj.y1, obj.y2);
                double maxY = Math.max(obj.y1, obj.y2);

                double[] xs = { (minX + maxX) / 2, minX, maxX };
                double[] ys = { minY, maxY, maxY };
                gc.strokePolygon(xs, ys, 3);
            }
        }
    }

    private void drawGhost(double x1, double y1, double x2, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setGlobalAlpha(0.3);
        drawObject(gc, new GeometricObject(selectedTool, x1, y1, x2, y2), Color.BLUE);
        gc.setGlobalAlpha(1.0);
    }

    private void drawIntersections(GraphicsContext gc) {
        gc.setFill(Color.RED);
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                GeometricObject o1 = objects.get(i);
                GeometricObject o2 = objects.get(j);

                // Only Line-Line for simplicity initially, maybe Line-Circle
                if (o1.type == Tool.LINE && o2.type == Tool.LINE) {
                    // Check intersection
                    // Line 1: (x1, y1) -> (x2, y2)
                    // Line 2: (x3, y3) -> (x4, y4)
                    // Denom = (y4-y3)(x2-x1) - (x4-x3)(y2-y1)
                    double x1 = o1.x1, y1 = o1.y1, x2 = o1.x2, y2 = o1.y2;
                    double x3 = o2.x1, y3 = o2.y1, x4 = o2.x2, y4 = o2.y2;

                    // Extend lines infinitely for intersection? Or just segments?
                    // Usually geometry board implies segments or infinite lines.
                    // The drawing code extends lines infinitely:
                    // gc.strokeLine(obj.x1 - dx * 100, ...);
                    // So we should treat them as infinite lines.

                    double det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
                    if (Math.abs(det) > 1e-6) {
                        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / det;
                        double px = x1 + t * (x2 - x1);
                        double py = y1 + t * (y2 - y1);

                        // Check if on screen
                        if (px >= 0 && px <= canvas.getWidth() && py >= 0 && py <= canvas.getHeight()) {
                            gc.fillOval(px - 3, py - 3, 6, 6);
                        }
                    }
                }
            }
        }
    }


    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("GeometryBoardViewer.desc", "GeometryBoardViewer description");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}


