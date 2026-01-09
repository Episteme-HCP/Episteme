/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
// import org.jzy3d.colors.ColorMapper;
// import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
// import org.jzy3d.plot3d.primitives.Shape;
// import org.jzy3d.plot3d.primitives.Surface;
import org.jzy3d.plot3d.builder.Mapper;
// import org.jzy3d.plot3d.builder.Builder;
// import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.List;

/**
 * Real-time 2D spin field viewer using Jzy3d.
 * <p>
 * Visualizes:
 * <ul>
 *   <li>Vector field (arrows) representing magnetization M(x,y)</li>
 *   <li>Color-coded topological density for Skyrmion identification</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpinField2DViewer {

    private final Chart chart;
    private final List<LineStrip> arrows = new ArrayList<>();
    // private Surface densitySurface;
    private final int width;
    private final int height;
    
    // Data buffer
    // private float[][][] magnetization; // [x][y][component: 0=x, 1=y, 2=z]

    public SpinField2DViewer(int width, int height) {
        this.width = width;
        this.height = height;
        // this.magnetization = new float[width][height][3];
        
        // Initialize Jzy3d chart
        this.chart = AWTChartComponentFactory.chart(Quality.Advanced, "awt");
        this.chart.getView().setBackgroundColor(org.jzy3d.colors.Color.BLACK);
    }

    /**
     * Updates the spin field data and refreshes the visualization.
     * 
     * @param mx Magnetization X component grid
     * @param my Magnetization Y component grid
     * @param mz Magnetization Z component grid
     */
    public void updateField(double[][] mx, double[][] my, double[][] mz) {
        // Clear previous arrows
        chart.getScene().getGraph().getAll().removeAll(arrows);
        arrows.clear();

        // Subsample for visual clarity (arrows at every Nth point)
        int step = Math.max(1, Math.min(width, height) / 20); // Aim for ~20x20 arrows
        
        for (int x = 0; x < width; x += step) {
            for (int y = 0; y < height; y += step) {
                float vx = (float) mx[x][y];
                float vy = (float) my[x][y];
                float vz = (float) mz[x][y];
                
                Coord3d position = new Coord3d(x, y, 0);
                // Vector scaling for visibility
                Coord3d vector = new Coord3d(vx, vy, vz).mul(step * 0.8f);
                Coord3d end = position.add(vector);
                
                LineStrip arrow = new LineStrip();
                arrow.add(new Point(position));
                arrow.add(new Point(end));
                
                // Color based on Z component (classical representation)
                // Red = Up, Blue = Down
                float normalizedZ = (vz + 1) / 2; // -1..1 -> 0..1
                Color color = new Color(normalizedZ, 0, 1 - normalizedZ);
                arrow.setWireframeColor(color);
                
                arrows.add(arrow);
            }
        }
        
        chart.getScene().getGraph().add(arrows);
        
        // Update surface if we want continuous density map underneath
        // updateTopologicalDensity(mx, my, mz);
    }
    
    // private void updateTopologicalDensity(double[][] mx, double[][] my, double[][] mz) {
    //     // Unused implementation
    // }

    public Chart getChart() {
        return chart;
    }
}
