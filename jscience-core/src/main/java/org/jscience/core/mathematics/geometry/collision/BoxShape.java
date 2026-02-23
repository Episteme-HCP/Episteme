/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.geometry.collision;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;

import java.util.Arrays;

/**
 * Box collision shape defined by its half-extents.
 */
public class BoxShape implements CollisionShape {

    private final Vector<Real> halfExtents;

    public BoxShape(double x, double y, double z) {
        this.halfExtents = Vector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    public Vector<Real> getHalfExtents() {
        return halfExtents;
    }

    @Override
    public Vector<Real> getSupportPoint(Vector<Real> direction) {
        Real[] support = new Real[3];
        for (int i = 0; i < 3; i++) {
            support[i] = direction.get(i).isPositive() ? halfExtents.get(i) : halfExtents.get(i).negate();
        }
        return Vector.of(Arrays.asList(support), Reals.getInstance());
    }

    @Override
    public Vector<Real>[] getAABB() {
        Vector<Real> min = halfExtents.negate();
        Vector<Real> max = halfExtents;
        return new Vector[]{min, max};
    }
}
