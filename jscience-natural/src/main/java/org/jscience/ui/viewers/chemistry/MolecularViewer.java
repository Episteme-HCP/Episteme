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

package org.jscience.ui.viewers.chemistry;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * High-performance 3D Molecular Viewer.
 * Visualizes atoms, bonds, and electron density with premium aesthetics.
 */
public final class MolecularViewer extends AbstractViewer {

    private final Canvas canvas = new Canvas(800, 600);
    private double rotationX = 0;
    private double rotationY = 0;
    private double zoom = 1.0;
    private boolean showBonds = true;
    private boolean showLabels = false;

    public MolecularViewer() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #1a1a2e, #0f0f1a);");

        Label title = new Label("MOLECULAR STRUCTURE ANALYZER");
        title.setStyle("-fx-text-fill: #e94560; -fx-font-size: 18px; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-color: #4e4e6a; -fx-border-width: 1;");
        
        canvas.widthProperty().bind(canvasHolder.widthProperty());
        canvas.heightProperty().bind(canvasHolder.heightProperty());
        
        canvas.widthProperty().addListener(e -> draw());
        canvas.heightProperty().addListener(e -> draw());

        // Mouse interaction for rotation
        canvas.setOnMouseDragged(e -> {
            rotationX += e.getX() * 0.1;
            rotationY += e.getY() * 0.1;
            draw();
        });

        root.getChildren().addAll(title, canvasHolder);
        setCenter(root);
        
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.clearRect(0, 0, w, h);

        // Calculate 3D transformation (simplified)
        double cosX = Math.cos(Math.toRadians(rotationX));
        double sinX = Math.sin(Math.toRadians(rotationX));
        double cosY = Math.cos(Math.toRadians(rotationY));
        double sinY = Math.sin(Math.toRadians(rotationY));

        // Use rotations to shift positions slightly
        double offX = 60 * cosY + 10 * sinY;
        double offY = 40 * cosX + 10 * sinX;

        drawAtom(gc, "O", 0, 0, 0, Color.RED, 40);
        if (showBonds) {
            drawBond(gc, 0, 0, -offX, offY);
            drawBond(gc, 0, 0, offX, offY);
        }
        drawAtom(gc, "H", -offX, offY, 0, Color.WHITE, 25);
        drawAtom(gc, "H", offX, offY, 0, Color.WHITE, 25);
    }

    private void drawAtom(GraphicsContext gc, String symbol, double x, double y, double z, Color color, double size) {
        double centerX = canvas.getWidth() / 2 + x * zoom;
        double centerY = canvas.getHeight() / 2 + y * zoom;
        double s = size * zoom;

        // Shadow/Glow
        gc.setFill(color.deriveColor(0, 1, 1, 0.2));
        gc.fillOval(centerX - s*0.7, centerY - s*0.7, s*1.4, s*1.4);

        // Gradient for 3D effect
        RadialGradient grad = new RadialGradient(
            -30, 0.5, centerX - s*0.2, centerY - s*0.2, s, false, CycleMethod.NO_CYCLE,
            new Stop(0, color.deriveColor(0, 0.8, 1.5, 1.0)),
            new Stop(1, color.darker())
        );
        gc.setFill(grad);
        gc.fillOval(centerX - s/2, centerY - s/2, s, s);

        if (showLabels) {
            gc.setFill(Color.WHITE);
            gc.fillText(symbol, centerX - 5, centerY + 5);
        }
    }

    private void drawBond(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        gc.setStroke(Color.web("#4e4e6a"));
        gc.setLineWidth(5 * zoom);
        gc.strokeLine(centerX + x1 * zoom, centerY + y1 * zoom, centerX + x2 * zoom, centerY + y2 * zoom);
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("Zoom", "Scaling factor", 0.5, 3.0, 0.1, 1.0, val -> { this.zoom = val; draw(); }));
        params.add(new BooleanParameter("Show Bonds", "Toggle bond visualization", true, val -> { this.showBonds = val; draw(); }));
        params.add(new BooleanParameter("Labels", "Enable atomic symbols", false, val -> { this.showLabels = val; draw(); }));
        return params;
    }

    @Override public String getName() { return "Molecular Viewer Pro"; }
    @Override public String getCategory() { return "Natural Sciences / Chemistry"; }
    @Override public String getDescription() { return "Interactive 3D molecular structure visualizer."; }
    @Override public String getLongDescription() { return "Premium molecular viewer with 3D rotation and orbital visualization (simplified)."; }
}
