/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.UniversalDataModel;

/**
 * Universal data model for architectural constraints and simulations.
 * It provides a structured way to store rays (for visibility or acoustics) 
 * and vector fields (for structural loads or ventilation).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class ArchitecturalModel implements UniversalDataModel, Serializable {

    private static final long serialVersionUID = 2L;

    @Override
    public String getModelType() { return "ARCHITECTURAL_CONSTRAINTS"; }

    /**
     * Represents a linear constraint or path (e.g., sound ray, light path).
     */
    public record Ray(Real x1, Real y1, Real x2, Real y2, String type) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents a point in a vector field (e.g., structural force, wind velocity).
     */
    public record VectorField(Real x, Real y, Real dx, Real dy, Real magnitude) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    private final List<Ray> rays = new ArrayList<>();
    private final List<VectorField> loadPaths = new ArrayList<>();
    
    /**
     * Adds a ray to the architectural model.
     * 
     * @param x1 start x
     * @param y1 start y
     * @param x2 end x
     * @param y2 end y
     * @param type semantic type of the ray
     */
    public void addRay(Real x1, Real y1, Real x2, Real y2, String type) {
        rays.add(new Ray(x1, y1, x2, y2, type));
    }

    /**
     * Adds a vector field point to the model.
     * 
     * @param x point x
     * @param y point y
     * @param dx direction x
     * @param dy direction y
     * @param magnitude force or velocity magnitude
     */
    public void addLoadPath(Real x, Real y, Real dx, Real dy, Real magnitude) {
        loadPaths.add(new VectorField(x, y, dx, dy, magnitude));
    }

    public List<Ray> getRays() { return Collections.unmodifiableList(rays); }
    public List<VectorField> getLoadPaths() { return Collections.unmodifiableList(loadPaths); }

    @Override
    public java.util.Map<String, Object> getMetadata() {
        java.util.Map<String, Object> meta = new java.util.HashMap<>();
        meta.put("ray_count", rays.size());
        meta.put("load_path_count", loadPaths.size());
        return meta;
    }

    @Override
    public java.util.Map<String, org.episteme.core.measure.Quantity<?>> getQuantities() {
        java.util.Map<String, org.episteme.core.measure.Quantity<?>> q = new java.util.HashMap<>();
        double totalRayLength = rays.stream()
            .mapToDouble(r -> Math.sqrt(Math.pow(r.x2().subtract(r.x1()).doubleValue(), 2) + Math.pow(r.y2().subtract(r.y1()).doubleValue(), 2)))
            .sum();
        double totalMagnitude = loadPaths.stream().mapToDouble(vf -> vf.magnitude().doubleValue()).sum();
        
        q.put("total_path_length", org.episteme.core.measure.Quantities.create(totalRayLength, org.episteme.core.measure.Units.METER));
        q.put("total_structural_magnitude", org.episteme.core.measure.Quantities.create(totalMagnitude, org.episteme.core.measure.Units.NEWTON));
        return q;
    }
}



