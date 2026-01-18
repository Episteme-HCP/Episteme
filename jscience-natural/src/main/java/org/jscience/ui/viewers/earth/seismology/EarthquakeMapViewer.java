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
import org.jscience.ui.RealParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Earthquake Viewer.
 * Visualizes earthquakes on a map.
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

    private final org.jscience.ui.RealParameter minMagParam;

    public EarthquakeMapViewer() {
        super();
        this.minMagParam = new RealParameter(
            "Min Magnitude",
            I18n.getInstance().get("viewer.earthquakemapviewer.param.minmag", "Min Magnitude"), 
            0.0, 9.0, 0.1, 0.0, 
            v -> draw());
        
        generateMockData();
        setupSidebar();
        
        // Interactive Info in Map
        mapCanvas.setOnMouseMoved(e -> {
            double lon = xToLon(e.getX());
            double lat = yToLat(e.getY());
            coordLabel.setText(String.format(I18n.getInstance().get("earthquake.coord.fmt", "Lat: %.2f  Lon: %.2f"), lat, lon));
            
            boolean hover = false;
            for(Earthquake q : quakes) {
                 if (q.getMag() < minMagParam.getValue().doubleValue()) continue;

                 double qx = lonToX(q.getLon()); double qy = latToY(q.getLat());
                 double size = Math.pow(q.getMag(), 1.5) * 2 * zoom;
                 if(Math.hypot(e.getX() - qx, e.getY() - qy) < size) {
                     infoLabel.setText(String.format(I18n.getInstance().get("earthquake.info.format", "Mag: %.1f\nLat: %.2f\nLon: %.2f\nDepth: %.0f km"),
                             q.getMag(), q.getLat(), q.getLon(), q.getDepth()));
                     hover = true;
                     break;
                 }
            }
            if(!hover) infoLabel.setText(I18n.getInstance().get("earthquake.hover", "Hover over a quake for info."));
        });
        
        new AnimationTimer() {
            @Override public void handle(long now) {
                lastTime = now;
                draw(); // Redraw for pulse animation
            }
        }.start();
    }

    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return java.util.List.of(minMagParam);
    }

    private void setupSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.getStyleClass().add("viewer-sidebar");
        sidebar.setPrefWidth(220);

        Label titleLabel = new Label(I18n.getInstance().get("earthquake.label.title", "Earthquake Map"));
        titleLabel.getStyleClass().add("header-label");
        
        // Add Slider/Control for Min Mag
        VBox filterBox = new VBox(5);
        Label filterLabel = new Label("Filter: Min Magnitude");
        javafx.scene.control.Slider magSlider = new javafx.scene.control.Slider(0, 9, 0);
        magSlider.setShowTickLabels(true);
        magSlider.setShowTickMarks(true);
        magSlider.valueProperty().addListener((o, old, v) -> minMagParam.setValue(org.jscience.mathematics.numbers.real.Real.of(v.doubleValue())));
        
        filterBox.getChildren().addAll(filterLabel, magSlider);

        Label explainLabel = new Label(I18n.getInstance().get("earthquake.explanation", "Real-time visualization of seismic activity."));
        explainLabel.setWrapText(true);

        Label legendLabel = new Label(I18n.getInstance().get("earthquake.legend", "Magnitude"));
        legendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox legend = new VBox(3);
        legend.getChildren().addAll(
                createLegendItem(Color.hsb(120, 1, 1), "Minor (< 3.0)"),
                createLegendItem(Color.hsb(80, 1, 1), "Light (3.0 - 5.0)"),
                createLegendItem(Color.hsb(40, 1, 1), "Moderate (5.0 - 6.0)"),
                createLegendItem(Color.RED, "Strong (> 6.0)"));

        coordLabel = new Label(I18n.getInstance().get("generated.earthquakemap.lat.000.lon.000", "Lat: 0.00 Lon: 0.00"));
        coordLabel.setFont(Font.font("Consolas", 12));

        infoLabel = new Label(I18n.getInstance().get("earthquake.hover", "Hover over a quake for info."));
        infoLabel.setWrapText(true);

        sidebar.getChildren().addAll(titleLabel, new Separator(), filterBox, new Separator(), explainLabel, new Separator(),
                legendLabel, legend, new Separator(), coordLabel, infoLabel);
        
        setRight(sidebar);
    }
    
    private Label createLegendItem(Color color, String text) {
        Label l = new Label("- " + text);
        l.setTextFill(color);
        l.setFont(Font.font("Arial", 11));
        return l;
    }

    @Override
    protected void drawOverlay(GraphicsContext gc, double width, double height) {
        double minMag = minMagParam.getValue().doubleValue();
        
        for (Earthquake q : quakes) {
            if (q.getMag() < minMag) continue;

            double x = lonToX(q.getLon());
            double y = latToY(q.getLat());
            if(x < -50 || x > width+50 || y < -50 || y > height+50) continue; // Cull

            double size = Math.pow(q.getMag(), 1.5) * 2 * zoom;

            Color c;
            if (q.getMag() > 6) c = Color.hsb(0, 1, 1, 0.8);
            else if (q.getMag() > 5) c = Color.hsb(40, 1, 1, 0.7);
            else if (q.getMag() > 3) c = Color.hsb(80, 1, 1, 0.6);
            else c = Color.hsb(120, 1, 1, 0.5);

            double pulse = (Math.sin(lastTime / 200_000_000.0 + q.getLat()) + 1) / 2;
            if (q.getMag() > 6) size += pulse * 5;

            gc.setFill(c);
            gc.fillOval(x - size / 2, y - size / 2, size, size);
        }
        
        // Scale Info
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("Zoom: %.1fx", zoom), 10, 20);
        gc.fillText(String.format("Quakes: %d", quakes.size()), 10, 35);
    }

    private void generateMockData() {
        addQuakeCluster(35, -120, 50, 5.0, 15);
        addQuakeCluster(-30, -70, 30, 6.0, 50);
        addQuakeCluster(36, 138, 40, 5.5, 20);
        addQuakeCluster(-5, 120, 30, 6.5, 30);
        addQuakeCluster(40, 30, 20, 5.0, 10);
        addQuakeCluster(-40, 175, 25, 5.5, 25);
        Random r = new Random(42);
        for (int i = 0; i < 50; i++) {
            quakes.add(new Earthquake((r.nextDouble() * 140) - 70, (r.nextDouble() * 360) - 180, 2.0 + r.nextDouble() * 3, r.nextDouble() * 100));
        }
    }

    private void addQuakeCluster(double lat, double lon, int count, double baseMag, double baseDepth) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            double dLat = r.nextGaussian() * 5;
            double dLon = r.nextGaussian() * 5;
            double mag = baseMag + r.nextGaussian();
            double depth = baseDepth + r.nextGaussian() * 20;
            if (mag < 1) mag = 1; if (depth < 0) depth = 5;
            quakes.add(new Earthquake(lat + dLat, lon + dLon, mag, depth));
        }
    }
    
    @Override public String getName() { return I18n.getInstance().get("viewer.earthquakemapviewer.name", "Earthquake Map Viewer"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.earth", "Earth Sciences"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.earthquakemapviewer.desc", "Visualizes seismic activity on a world map."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.earthquakemapviewer.longdesc", "Interactive global map showing simulated earthquake data. Features pulse animations, magnitude color-coding, and detailed seismic information (latitude, longitude, depth, and magnitude) on hover."); }
}
