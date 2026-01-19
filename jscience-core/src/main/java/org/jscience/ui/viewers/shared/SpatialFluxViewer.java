package org.jscience.ui.viewers.shared;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.geometry.SpatialDataSet;
import org.jscience.mathematics.numbers.real.Real;

import java.util.*;

/**
 * Universal viewer for geographic and spatial fluxes.
 * Supports heatmaps, vector flows, and point data.
 */
public final class SpatialFluxViewer extends AbstractViewer {

    private final Canvas canvas = new Canvas(1000, 700);
    private SpatialDataSet data;
    
    private Real heatIntensity = Real.of(1.0);
    private Real flowThickness = Real.of(1.0);
    private String activeValueKey = "";

    public SpatialFluxViewer() {
        setCenter(new Pane(canvas));
        canvas.widthProperty().bind(((Pane)getCenter()).widthProperty());
        canvas.heightProperty().bind(((Pane)getCenter()).heightProperty());
        
        getStyleClass().add("spatial-flux-viewer");
        
        // Listeners for redraw
        canvas.widthProperty().addListener(e -> draw());
        canvas.heightProperty().addListener(e -> draw());
    }

    public void setData(SpatialDataSet data, String valueKey) {
        this.data = data;
        this.activeValueKey = valueKey;
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        if (data == null || data.getLocations().isEmpty()) return;

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        
        Real minX = data.getMinX();
        Real maxX = data.getMaxX();
        Real minY = data.getMinY();
        Real maxY = data.getMaxY();
        
        Real rangeX = maxX.subtract(minX).add(Real.of(0.001));
        Real rangeY = maxY.subtract(minY).add(Real.of(0.001));

        // 1. Draw Heatmap Layer
        for (var loc : data.getLocations()) {
            Real val = loc.values().getOrDefault(activeValueKey, Real.ZERO);
            if (val.isPositive()) {
                double x = loc.x().subtract(minX).divide(rangeX).doubleValue() * (w - 100) + 50;
                double y = loc.y().subtract(minY).divide(rangeY).doubleValue() * (h - 100) + 50;
                double radius = val.multiply(heatIntensity).multiply(Real.of(50)).doubleValue();
                
                gc.setFill(new RadialGradient(0, 0, x, y, radius, false, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#ff4500", 0.6)), // Orange-Red
                    new Stop(1, Color.TRANSPARENT)));
                gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            }
        }

        // 2. Draw Flows Layer
        gc.setStroke(Color.web("#00eeff", 0.4));
        gc.setLineWidth(flowThickness.doubleValue());
        for (var flow : data.getFlows()) {
            SpatialDataSet.Location src = null, dst = null;
            for (var l : data.getLocations()) {
                if (l.id().equals(flow.fromId())) src = l;
                if (l.id().equals(flow.toId())) dst = l;
            }
            if (src != null && dst != null) {
                double x1 = src.x().subtract(minX).divide(rangeX).doubleValue() * (w - 100) + 50;
                double y1 = src.y().subtract(minY).divide(rangeY).doubleValue() * (h - 100) + 50;
                double x2 = dst.x().subtract(minX).divide(rangeX).doubleValue() * (w - 100) + 50;
                double y2 = dst.y().subtract(minY).divide(rangeY).doubleValue() * (h - 100) + 50;
                gc.strokeLine(x1, y1, x2, y2);
                
                // Arrowhead (simplified)
                double angle = Math.atan2(y2 - y1, x2 - x1);
                gc.strokeLine(x2, y2, x2 - 10 * Math.cos(angle - 0.5), y2 - 10 * Math.sin(angle - 0.5));
                gc.strokeLine(x2, y2, x2 - 10 * Math.cos(angle + 0.5), y2 - 10 * Math.sin(angle + 0.5));
            }
        }

        // 3. Draw Points
        gc.setFill(Color.WHITE);
        for (var loc : data.getLocations()) {
            double x = loc.x().subtract(minX).divide(rangeX).doubleValue() * (w - 100) + 50;
            double y = loc.y().subtract(minY).divide(rangeY).doubleValue() * (h - 100) + 50;
            gc.fillOval(x - 3, y - 3, 6, 6);
            gc.fillText(loc.label(), x + 8, y + 4);
        }
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new RealParameter(
            I18n.getInstance().get("viewer.spatial.heat", "Heat Intensity"), 
            "Gamma correction for heatmap visualization", 0.1, 5.0, 0.1, 1.0, val -> {
                this.heatIntensity = val; draw();
            }));
        params.add(new RealParameter(
            I18n.getInstance().get("viewer.spatial.flow", "Flow Thickness"), 
            "Width of flow vector lines", 0.1, 10.0, 0.5, 1.0, val -> {
                this.flowThickness = val; draw();
            }));
        return params;
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.shared", "General Purpose"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.spatial.name", "Spatial Flux Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.spatial.desc", "Visualizes geographic data, hazards, and flows."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("viewer.spatial.longdesc", "Advanced multi-layer mapping tool for scientific datasets. Supports heatmaps for intensity and vector lines for directional fluxes."); 
    }
}
