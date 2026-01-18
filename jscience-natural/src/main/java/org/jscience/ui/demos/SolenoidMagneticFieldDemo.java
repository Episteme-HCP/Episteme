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
import javafx.scene.shape.Cylinder;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field.MagneticFieldViewer;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field.SourceVisualizer;
import org.jscience.technical.backend.algorithms.MulticoreMaxwellProvider;
import org.jscience.technical.backend.algorithms.MaxwellSource;

/**
 * Demo for a Magnetic solenoid or rod using the generic viewer.
 */
public class SolenoidMagneticFieldDemo extends AbstractDemo {

    @Override public String getCategory() { return "Physics"; }
    @Override public String getName() { return "Solenoid Magnetic Field"; }
    @Override public String getDescription() { return "Visualizes the field of a magnetic rod or solenoid coil."; }

    @Override
    public Node createViewerNode() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        
        // A long dipole approximation
        provider.addSource(new MulticoreMaxwellProvider.DipoleSource(
            new double[]{0, 0, 0}, new double[]{0, 500, 0}, 5.0, 0));
            
        MagneticFieldViewer viewer = new MagneticFieldViewer(provider);
        
        viewer.addVisualizer(new SourceVisualizer() {
            @Override public boolean supports(MaxwellSource s) { return true; }
            @Override public Node getVisualRepresentation(MaxwellSource s) {
                Cylinder rod = new Cylinder(10, 150);
                rod.setMaterial(new PhongMaterial(Color.web("#884444")));
                return rod;
            }
        });
        
        return viewer;
    }

    @Override public String getLongDescription() { return getDescription(); }
}
