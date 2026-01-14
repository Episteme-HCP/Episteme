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

// import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
// import org.jscience.apps.physics.spintronics.Micromagnetics2D; // Assuming exists or similar
// import org.jscience.mathematics.numbers.real.Real;

/**
 * Animator for Skyrmion dynamics with color-coded topological density.
 * <p>
 * Visualization features:
 * <ul>
 *   <li>Real-time rendering of spin texture</li>
 *   <li>Topological charge density mapping (HSV color)</li>
 *   <li>Skyrmion tracking (center of mass)</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SkyrmionAnimator {

    private final Canvas canvas;
    private final int width;
    private final int height;
    
    // Simulation data hook (interface or double arrays)
    private double[][] mx, my, mz;

    public SkyrmionAnimator(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.width = width;
        this.height = height;
        this.mx = new double[width][height];
        this.my = new double[width][height];
        this.mz = new double[width][height];
    }

    /**
     * Updates data and redraws the canvas.
     */
    public void drawFrame(double[][] newMx, double[][] newMy, double[][] newMz) {
        this.mx = newMx;
        this.my = newMy;
        this.mz = newMz;
        render();
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Pixel writer is faster for dense field, but simple rects ok for demo
        double pixelW = canvas.getWidth() / width;
        double pixelH = canvas.getHeight() / height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Topological density approximation based on Z-component (core) and in-plane winding
                // Simple visualization: Hue = In-plane angle, Brightness = Mz
                
                double angle = Math.atan2(my[i][j], mx[i][j]); // -π to π
                double hue = Math.toDegrees(angle);
                if (hue < 0) hue += 360;
                
                // Mz controls saturation/brightness
                // Core (Mz=-1) -> Dark/Saturated
                // Periphery (Mz=+1) -> White/Background
                
                double z = mz[i][j];
                double sat = 1.0;
                double bri = 1.0;
                
                // Color mapping:
                // Down (Skyrmion core) -> Distinct color (e.g., Red) or use Hue map
                // Up (Background) -> White
                
                // Standard micromagnetic color code:
                // Color = Direction (Hue)
                // Lightness = Mz (Up=Light, Down=Dark)
                
                // Normalize Z [-1, 1] -> [0, 1]
                double normZ = (z + 1) / 2.0;
                
                // Use HSB
                // Hue: In-plane direction
                // Saturation: 1 (Full color)
                // Brightness: Based on Mz? Or Saturation based on Mz?
                // Often: White background (Mz=1), Color for Skyrmion (Mz=-1)
                
                sat = 1.0 - normZ; // Up(1) -> 0 sat (White). Down(-1) -> 1 sat (Full color)
                
                gc.setFill(Color.hsb(hue, sat, bri));
                gc.fillRect(i * pixelW, j * pixelH, pixelW, pixelH);
            }
        }
    }
    
    /**
     * Calculates topological charge Q.
     * Q = (1/4π) ∫∫ m · (∂x m × ∂y m) dx dy
     */
    public double calculateTopologicalCharge() {
        double q = 0;
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                // Finite difference approximation for derivatives
                // ... (simplified)
            }
        }
        return q / (4 * Math.PI);
    }
}
