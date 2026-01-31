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

package org.jscience.social.ui.viewers.architecture;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.social.architecture.ArchitecturalModel;

/**
 * High-precision architectural viewer for physical constraints and geometry.
 * Visualizes solar rays, load paths, and acoustic reflections.
 */
public final class ArchitecturalSystemViewer extends AbstractViewer {

    private final Canvas canvas = new Canvas(800, 600);
    private ArchitecturalModel model;

    public ArchitecturalSystemViewer() {
        setCenter(new Pane(canvas));
        canvas.widthProperty().bind(((Pane)getCenter()).widthProperty());
        canvas.heightProperty().bind(((Pane)getCenter()).heightProperty());
        
        canvas.widthProperty().addListener(e -> draw());
        canvas.heightProperty().addListener(e -> draw());
        getStyleClass().add("architectural-system-viewer");
    }

    public void setModel(ArchitecturalModel model) {
        this.model = model;
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (model == null) return;

        // Draw Rays (Solar / Acoustic)
        gc.setLineWidth(1.0);
        for (var ray : model.getRays()) {
            gc.setStroke(ray.type().equals("solar") ? Color.GOLD : Color.CYAN);
            gc.strokeLine(ray.x1().doubleValue(), ray.y1().doubleValue(), 
                          ray.x2().doubleValue(), ray.y2().doubleValue());
        }

        // Draw Load Paths (Vector fields)
        gc.setStroke(Color.ORANGERED);
        for (var path : model.getLoadPaths()) {
            double x = path.x().doubleValue();
            double y = path.y().doubleValue();
            double tx = x + path.dx().doubleValue() * 20;
            double ty = y + path.dy().doubleValue() * 20;
            gc.setLineWidth(path.magnitude().doubleValue() * 2);
            gc.strokeLine(x, y, tx, ty);
            
            // Arrowhead
            double angle = Math.atan2(ty - y, tx - x);
            gc.strokeLine(tx, ty, tx - 5 * Math.cos(angle - 0.5), ty - 5 * Math.sin(angle - 0.5));
            gc.strokeLine(tx, ty, tx - 5 * Math.cos(angle + 0.5), ty - 5 * Math.sin(angle + 0.5));
        }
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.social", "Social Sciences"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.architecture.name", "Architectural System Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.architecture.desc", "Visualizes physical constraints on architectural geometry."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.architecture.longdesc", "Scientific architecture tool for analyzing environmental and structural performance through ray tracing and load path visualization."); 
    }
}

