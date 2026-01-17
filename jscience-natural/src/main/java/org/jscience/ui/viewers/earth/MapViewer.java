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

package org.jscience.ui.viewers.earth;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractViewer;

/**
 * Abstract World Map Viewer.
 * Provides basic map projection (Equirectangular), grid drawing, and zoom/pan controls.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class MapViewer extends AbstractViewer {

    protected Canvas mapCanvas;
    protected double offsetX = 0;
    protected double offsetY = 0;
    protected double zoom = 1.0;
    private double dragX, dragY;

    public MapViewer() {
        mapCanvas = new Canvas(800, 400);
        
        // Initial Center
        setCenter(mapCanvas);

        // Resize Logic
        widthProperty().addListener(o -> resizing());
        heightProperty().addListener(o -> resizing());

        // Interaction
        mapCanvas.setOnMousePressed(e -> {
            dragX = e.getX();
            dragY = e.getY();
        });
        mapCanvas.setOnMouseDragged(e -> {
            offsetX += e.getX() - dragX;
            offsetY += e.getY() - dragY;
            dragX = e.getX();
            dragY = e.getY();
            draw();
        });
        mapCanvas.setOnScroll(e -> {
            double factor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            zoom *= factor;
            zoom = Math.max(0.5, Math.min(zoom, 10.0));
            draw();
        });
    }

    protected void resizing() {
        if (getWidth() > 0 && getHeight() > 0) {
            // Adjust canvas size based on available center space (handling sidebars)
            // A simple approach is taking getWidth() - (left+right width)
            // But AbstractViewer is BorderPane.
            // setCenter will stretch the Canvas? No, Canvas is not resizable by default.
            // We need to bind or listener.
            // Using a parent Pane holder is safer, but here we manage Canvas size directly.
            double w = getWidth();
            if (getRight() != null) w -= getRight().getLayoutBounds().getWidth(); // Approx
            if (getLeft() != null) w -= getLeft().getLayoutBounds().getWidth();
            
            w = Math.max(100, w);
            mapCanvas.setWidth(w);
            mapCanvas.setHeight(getHeight());
            draw();
        }
    }

    protected void draw() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        double w = mapCanvas.getWidth();
        double h = mapCanvas.getHeight();

        gc.clearRect(0, 0, w, h);
        
        // Background (Ocean)
        gc.setFill(Color.web("#0a1929"));
        gc.fillRect(0, 0, w, h);

        drawGrid(gc, w, h);
        drawOverlay(gc, w, h);
    }
    
    protected void drawGrid(GraphicsContext gc, double w, double h) {
        gc.setStroke(Color.web("#1a3a5c"));
        gc.setLineWidth(0.5);

        // Latitude lines
        for (int lat = -60; lat <= 60; lat += 30) {
            double y = latToY(lat);
            if (y >= 0 && y <= h) {
                gc.strokeLine(0, y, w, y);
                gc.setFill(Color.GRAY);
                gc.fillText(lat + "°", 5, y - 2);
            }
        }
        // Longitude lines
        for (int lon = -180; lon <= 180; lon += 30) {
            double x = lonToX(lon);
            if (x >= 0 && x <= w) {
                gc.strokeLine(x, 0, x, h);
                gc.setFill(Color.GRAY);
                gc.fillText(lon + "°", x + 2, h - 5);
            }
        }
        // Equator and Prime Meridian
        gc.setStroke(Color.web("#2a5a8c"));
        gc.setLineWidth(1);
        double eqY = latToY(0);
        if (eqY >= 0 && eqY <= h) gc.strokeLine(0, eqY, w, eqY);
        double pmX = lonToX(0);
        if (pmX >= 0 && pmX <= w) gc.strokeLine(pmX, 0, pmX, h);
    }

    protected abstract void drawOverlay(GraphicsContext gc, double width, double height);

    // Coordinate Transforms
    protected double lonToX(double lon) {
        // Map 360 deg to width*zoom
        return ((lon + 180) / 360.0 * mapCanvas.getWidth() * zoom) + offsetX;
    }

    protected double latToY(double lat) {
        return ((1.0 - (lat + 90) / 180.0) * mapCanvas.getHeight() * zoom) + offsetY;
    }

    protected double xToLon(double x) {
        return ((x - offsetX) / (mapCanvas.getWidth() * zoom)) * 360.0 - 180;
    }

    protected double yToLat(double y) {
        return 90 - ((y - offsetY) / (mapCanvas.getHeight() * zoom)) * 180.0;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.earth", "Earth Sciences");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.mapviewer.name", "Map Viewer");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.mapviewer.desc", "Abstract world map viewer with projection support.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.mapviewer.longdesc", "Base class for map-based visualizations. Provides an equirectangular projection, interactive zoom and pan, and coordinate grid drawing.");
    }
}
