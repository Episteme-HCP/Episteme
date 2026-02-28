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

package org.episteme.core.mathematics.geometry.boundaries;

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A composite boundary supporting inclusions (exclaves) and exclusions (enclaves).
 * 
 * @param <P> the point type
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompositeBoundary<P> implements Boundary<P> {

    private static final long serialVersionUID = 1L;

    private final List<Boundary<P>> inclusions = new ArrayList<>();
    private final List<Boundary<P>> exclusions = new ArrayList<>();

    public CompositeBoundary() {
    }

    public void addInclusion(Boundary<P> boundary) {
        if (boundary != null) inclusions.add(boundary);
    }

    public void addExclusion(Boundary<P> boundary) {
        if (boundary != null) exclusions.add(boundary);
    }

    public List<Boundary<P>> getInclusions() {
        return Collections.unmodifiableList(inclusions);
    }

    public List<Boundary<P>> getExclusions() {
        return Collections.unmodifiableList(exclusions);
    }

    @Override
    public int getDimension() {
        return inclusions.isEmpty() ? 0 : inclusions.get(0).getDimension();
    }

    @Override
    public boolean contains(P point) {
        boolean included = false;
        for (Boundary<P> b : inclusions) {
            if (b.contains(point)) {
                included = true;
                break;
            }
        }
        if (!included) return false;
        for (Boundary<P> b : exclusions) {
            if (b.contains(point)) return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return inclusions.isEmpty();
    }

    @Override
    public P getCentroid() {
        if (inclusions.isEmpty()) return null;
        // Simplified: return centroid of the first inclusion
        return inclusions.get(0).getCentroid();
    }

    @Override
    public BoundingBox<P> getBoundingBox() {
        if (inclusions.isEmpty()) return null;
        BoundingBox<P> bbox = inclusions.get(0).getBoundingBox();
        for (int i = 1; i < inclusions.size(); i++) {
            bbox = bbox.merge(inclusions.get(i).getBoundingBox());
        }
        return bbox;
    }

    @Override
    public Real getMeasure() {
        Real total = Real.ZERO;
        for (Boundary<P> b : inclusions) total = total.add(b.getMeasure());
        for (Boundary<P> b : exclusions) total = total.subtract(b.getMeasure());
        return total;
    }

    @Override
    public Real getBoundaryMeasure() {
        Real total = Real.ZERO;
        for (Boundary<P> b : inclusions) total = total.add(b.getBoundaryMeasure());
        for (Boundary<P> b : exclusions) total = total.add(b.getBoundaryMeasure());
        return total;
    }

    @Override
    public boolean intersects(Boundary<P> other) {
        for (Boundary<P> b : inclusions) {
            if (b.intersects(other)) return true;
        }
        return false;
    }

    @Override
    public Boundary<P> union(Boundary<P> other) {
        CompositeBoundary<P> result = new CompositeBoundary<>();
        result.inclusions.addAll(this.inclusions);
        result.exclusions.addAll(this.exclusions);
        result.addInclusion(other);
        return result;
    }

    @Override
    public Boundary<P> intersection(Boundary<P> other) {
        return null; // Complex to implement generally
    }

    @Override
    public Boundary<P> convexHull() {
        return null; // Complex to implement generally
    }

    @Override
    public Boundary<P> translate(P offset) {
        CompositeBoundary<P> result = new CompositeBoundary<>();
        for (Boundary<P> b : inclusions) result.addInclusion(b.translate(offset));
        for (Boundary<P> b : exclusions) result.addExclusion(b.translate(offset));
        return result;
    }

    @Override
    public Boundary<P> scale(Real factor) {
        CompositeBoundary<P> result = new CompositeBoundary<>();
        for (Boundary<P> b : inclusions) result.addInclusion(b.scale(factor));
        for (Boundary<P> b : exclusions) result.addExclusion(b.scale(factor));
        return result;
    }
}
