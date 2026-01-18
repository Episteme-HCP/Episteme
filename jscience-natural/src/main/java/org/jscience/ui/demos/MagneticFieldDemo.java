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

package org.jscience.ui.demos;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field.MagneticFieldViewer;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field.SourceVisualizer;
import org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider;
import org.jscience.technical.backend.algorithms.MaxwellSource;

/**
 * Demo Provider for Magnetic Field Visualization with physical source bodies.
 */
public class MagneticFieldDemo extends AbstractDemo {

    @Override public String getCategory() { return "Physics"; }

    @Override
    public String getName() {
        return "Planetary Magnetic Field";
    }

    @Override
    public String getDescription() {
        return "High-fidelity visualization of the Earth's magnetic dipole using streamlines and field gradients.";
    }

    @Override
    public Node createViewerNode() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        
        // Single central dipole representing Earth
        provider.addSource(new MulticoreMaxwellProvider.DipoleSource(
            new double[]{0, 0, 0}, new double[]{0, 1500, 0}, 10.0, 0));
            
        MagneticFieldViewer viewer = new MagneticFieldViewer(provider);
        
        // Add a visualizer for the central planet (Globe)
        viewer.addVisualizer(new SourceVisualizer() {
            @Override
            public boolean supports(MaxwellSource source) {
                // If at center, treat as the planet
                return Math.abs(source.getPosition()[0]) < 1;
            }

            @Override
            public Node getVisualRepresentation(MaxwellSource source) {
                Sphere earth = new Sphere(80);
                PhongMaterial mat = new PhongMaterial(Color.web("#224488"));
                mat.setSpecularColor(Color.WHITE);
                earth.setMaterial(mat);
                return earth;
            }
        });
        
        return viewer;
    }

    @Override public String getLongDescription() { return getDescription(); }
}
