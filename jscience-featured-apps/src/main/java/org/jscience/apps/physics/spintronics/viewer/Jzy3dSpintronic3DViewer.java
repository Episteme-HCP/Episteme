/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics.viewer;

import org.jscience.mathematics.numbers.real.Real;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.HashMap;
import java.util.Map;

/**
 * Jzy3d (OpenGL) implementation of Spintronic3DViewer.
 * Provides high-performance 3D rendering for large spin arrays.
 */
public class Jzy3dSpintronic3DViewer implements Spintronic3DViewer {

    private Chart chart;
    private final Map<String, Scatter> arrows = new HashMap<>();

    public Jzy3dSpintronic3DViewer() {
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
    }

    @Override
    public void addLayer(double y, double thickness, String color, String label) {
        // Build box as 8-corner scatter (simplified)
        float w = 100, h = (float)(thickness * 1e9), d = 80;
        float yf = (float) y;
        Coord3d[] corners = new Coord3d[] {
            new Coord3d(-w, yf - h/2, -d), new Coord3d(w, yf - h/2, -d),
            new Coord3d(w, yf + h/2, -d), new Coord3d(-w, yf + h/2, -d),
            new Coord3d(-w, yf - h/2, d), new Coord3d(w, yf - h/2, d),
            new Coord3d(w, yf + h/2, d), new Coord3d(-w, yf + h/2, d)
        };
        Scatter box = new Scatter(corners, parseColor(color), 5f);
        chart.getScene().add(box);
    }

    @Override
    public void addMagnetizationArrow(double x, double y, double z, Real mx, Real my, Real mz, String id) {
        Coord3d base = new Coord3d((float)x, (float)y, (float)z);
        Coord3d tip = new Coord3d(
            (float)(x + mx.doubleValue() * 30),
            (float)(y + my.doubleValue() * 30),
            (float)(z + mz.doubleValue() * 30)
        );
        Scatter arrow = new Scatter(new Coord3d[]{base, tip}, Color.YELLOW, 8f);
        arrows.put(id, arrow);
        chart.getScene().add(arrow);
    }

    @Override
    public void updateMagnetizationArrow(String id, Real mx, Real my, Real mz) {
        Scatter old = arrows.get(id);
        if (old != null) {
            Coord3d base = old.getCoordinates()[0];
            Coord3d tip = new Coord3d(
                base.x + (float)(mx.doubleValue() * 30),
                base.y + (float)(my.doubleValue() * 30),
                base.z + (float)(mz.doubleValue() * 30)
            );
            old.setCoordinates(new Coord3d[]{base, tip});
        }
    }

    @Override
    public void addSpinCurrentFlow(double fromY, double toY, int density) {
        Coord3d[] spins = new Coord3d[density];
        double step = (toY - fromY) / density;
        for (int i = 0; i < density; i++) {
            spins[i] = new Coord3d(-50 + (i % 10) * 10, (float)(fromY + i * step), 0);
        }
        Scatter flow = new Scatter(spins, Color.CYAN, 3f);
        chart.getScene().add(flow);
    }

    @Override
    public void clear() {
        chart.getScene().getGraph().getAll().clear();
        arrows.clear();
    }

    @Override
    public Object getComponent() {
        return (java.awt.Component) chart.getCanvas();
    }

    @Override
    public void setViewAngle(double azimuth, double elevation) {
        chart.getView().rotate(new org.jzy3d.maths.Coord2d((float)azimuth, (float)elevation));
    }

    private Color parseColor(String hex) {
        java.awt.Color c = java.awt.Color.decode(hex);
        return new Color(c.getRed(), c.getGreen(), c.getBlue());
    }
}
