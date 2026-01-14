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

package org.jscience.apps.physics.spintronics.viewer;

import org.jscience.mathematics.numbers.real.Real;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;

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
    private final Map<String, Coord3d> arrowBases = new HashMap<>();

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
        arrowBases.put(id, base);
        chart.getScene().add(arrow);
    }

    @Override
    public void updateMagnetizationArrow(String id, Real mx, Real my, Real mz) {
        Scatter old = arrows.get(id);
        Coord3d base = arrowBases.get(id);

        if (old != null && base != null) {
            chart.getScene().remove(old);

            Coord3d tip = new Coord3d(
                base.x + (float)(mx.doubleValue() * 30),
                base.y + (float)(my.doubleValue() * 30),
                base.z + (float)(mz.doubleValue() * 30)
            );
            Scatter arrow = new Scatter(new Coord3d[]{base, tip}, Color.YELLOW, 8f);

            arrows.put(id, arrow);
            chart.getScene().add(arrow);
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
        arrowBases.clear();
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

    // ========== PHASE 3: 2D Spin Field Visualization ==========

    /**
     * Renders a 2D micromagnetic spin field as arrow scatter.
     * @param sim Micromagnetics2D simulation
     * @param scale Arrow length scaling factor
     */
    public void addSpinField2D(org.jscience.apps.physics.spintronics.Micromagnetics2D sim, float scale) {
        int nx = sim.getNx();
        int ny = sim.getNy();
        float cellSize = (float)(sim.getCellSize().doubleValue() * 1e9); // nm
        
        Coord3d[] bases = new Coord3d[nx * ny];
        Coord3d[] tips = new Coord3d[nx * ny];
        Color[] colors = new Color[nx * ny];
        
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                int idx = i * ny + j;
                float x = i * cellSize;
                float z = j * cellSize;
                
                org.jscience.mathematics.numbers.real.Real[] m = sim.getMagnetization(i, j);
                float mx = (float) m[0].doubleValue();
                float my = (float) m[1].doubleValue();
                float mz = (float) m[2].doubleValue();
                
                bases[idx] = new Coord3d(x, 0, z);
                tips[idx] = new Coord3d(x + mx * scale, my * scale, z + mz * scale);
                
                // Color by m_z: up = blue, down = red
                float r = (1 - mz) / 2;
                float b = (1 + mz) / 2;
                colors[idx] = new Color(r, 0.2f, b);
            }
        }
        
        // Add base points
        Scatter baseScatter = new Scatter(bases, Color.GRAY, 2f);
        chart.getScene().add(baseScatter);
        
        // Add tip points (showing direction)
        Scatter tipScatter = new Scatter(tips, colors);
        tipScatter.setWidth(4f);
        chart.getScene().add(tipScatter);
    }

    /**
     * Highlights skyrmion regions with topological density coloring.
     * @param sim Micromagnetics2D simulation
     */
    public void addSkyrmionTopology(org.jscience.apps.physics.spintronics.Micromagnetics2D sim) {
        int nx = sim.getNx();
        int ny = sim.getNy();
        float cellSize = (float)(sim.getCellSize().doubleValue() * 1e9);
        
        java.util.List<Coord3d> skyrmionPoints = new java.util.ArrayList<>();
        
        for (int i = 1; i < nx - 1; i++) {
            for (int j = 1; j < ny - 1; j++) {
                org.jscience.mathematics.numbers.real.Real[] m = sim.getMagnetization(i, j);
                
                // Simple skyrmion detection: m_z < -0.5 (core pointing down)
                if (m[2].doubleValue() < -0.5) {
                    skyrmionPoints.add(new Coord3d(i * cellSize, 5, j * cellSize));
                }
            }
        }
        
        if (!skyrmionPoints.isEmpty()) {
            Coord3d[] points = skyrmionPoints.toArray(new Coord3d[0]);
            Scatter skyrmionScatter = new Scatter(points, Color.MAGENTA, 10f);
            chart.getScene().add(skyrmionScatter);
        }
    }

    /**
     * Updates spin field visualization in real-time.
     */
    public void updateSpinField2D(org.jscience.apps.physics.spintronics.Micromagnetics2D sim, float scale) {
        clear();
        addSpinField2D(sim, scale);
    }
}
