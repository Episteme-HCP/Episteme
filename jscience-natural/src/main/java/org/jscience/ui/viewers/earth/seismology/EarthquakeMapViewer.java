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

package org.jscience.ui.viewers.earth.seismology;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jscience.earth.seismology.Earthquake;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.earth.MapViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Earthquake Viewer.
 * Visualizes earthquakes on a map.
 * Refactored to be 100% parameter-based.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EarthquakeMapViewer extends MapViewer {

    private final List<Earthquake> quakes = new ArrayList<>();
    private Label coordLabel;
    private Label infoLabel;
    private long lastTime = 0;
    private double minMagnitude = 0.0;

    private final List<Parameter<?>> parameters = new ArrayList<>();

    public EarthquakeMapViewer() {
        super();
        setupParameters();
        generateMockData();
        setupSidebar();
        
        mapCanvas.setOnMouseMoved(e -> {
            double lon = xToLon(e.getX());
            double lat = yToLat(e.getY());
            coordLabel.setText(String.format("Lat: %.2f  Lon: %.2f", lat, lon));
            
            boolean hover = false;
            for(Earthquake q : quakes) {
                 if (q.getMag() < minMagnitude) continue;
                 double qx = lonToX(q.getLon()), qy = latToY(q.getLat());
                 double size = Math.pow(q.getMag(), 1.5) * 2 * zoom;
                 if(Math.hypot(e.getX() - qx, e.getY() - qy) < size) {
                     infoLabel.setText(String.format("Mag: %.1f\nLat: %.2f\nLon: %.2f\nDepth: %.0f km",
                             q.getMag(), q.getLat(), q.getLon(), q.getDepth()));
                     hover = true;
                     break;
                 }
            }
            if(!hover) infoLabel.setText("Hover over a quake.");
        });
        
        new AnimationTimer() {
            @Override public void handle(long now) {
                lastTime = now;
                draw();
            }
        }.start();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("earthquake.minmag", I18n.getInstance().get("viewer.earthquakemapviewer.param.minmag", "Min Magnitude"), 0.0, 9.0, 0.1, minMagnitude, v -> {
            minMagnitude = v;
            draw();
        }));
    }

    private void setupSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.setPrefWidth(220);

        Label titleLabel = new Label("Earthquake Map");
        titleLabel.getStyleClass().add("header-label");

        coordLabel = new Label("Lat: 0.00 Lon: 0.00");
        infoLabel = new Label("Hover over a quake.");
        infoLabel.setWrapText(true);

        sidebar.getChildren().addAll(titleLabel, new Separator(), coordLabel, infoLabel);
        setRight(sidebar);
    }

    @Override
    protected void drawOverlay(GraphicsContext gc, double width, double height) {
        for (Earthquake q : quakes) {
            if (q.getMag() < minMagnitude) continue;
            double x = lonToX(q.getLon()), y = latToY(q.getLat());
            if(x < -50 || x > width+50 || y < -50 || y > height+50) continue;

            double size = Math.pow(q.getMag(), 1.5) * 2 * zoom;
            double pulse = (Math.sin(lastTime / 200_000_000.0 + q.getLat()) + 1) / 2;
            if (q.getMag() > 6) size += pulse * 5;

            gc.setFill(q.getMag() > 6 ? Color.RED : Color.ORANGE);
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }

    private void generateMockData() {
        Random r = new Random(42);
        for (int i = 0; i < 200; i++) {
            quakes.add(new Earthquake((r.nextDouble() * 140) - 70, (r.nextDouble() * 360) - 180, 2.0 + r.nextDouble() * 6, r.nextDouble() * 100));
        }
    }

    @Override public String getName() { return I18n.getInstance().get("viewer.earthquakemapviewer.name", "Earthquake Map"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.earth", "Earth Sciences"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.earthquakemapviewer.desc", "Visualizes seismic activity."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.earthquakemapviewer.longdesc", "Seismic map."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
