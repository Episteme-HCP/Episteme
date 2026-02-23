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
 * Sphere collision shape defined by its radius.
 */
public class SphereShape implements CollisionShape {

    private final Real radius;

    public SphereShape(double radius) {
        this.radius = Real.of(radius);
    }

    public Real getRadius() {
        return radius;
    }

    @Override
    public Vector<Real> getSupportPoint(Vector<Real> direction) {
        // v = (dir / |dir|) * radius
        Real norm = direction.norm();
        if (norm.isZero()) return Vector.of(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance());
        
        Real scale = radius.divide(norm);
        return direction.multiply(scale);
    }

    @Override
    public Vector<Real>[] getAABB() {
        Vector<Real> offset = Vector.of(Arrays.asList(radius, radius, radius), Reals.getInstance());
        return new Vector[]{offset.negate(), offset};
    }
}
