package org.jscience.mathematics.geometry.collision;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for 3D collision shapes.
 * 
 * @author Silvere Martin-Michiellot
 * @since 3.0
 */
public interface CollisionShape {

    /**
     * Gets the support point in a given direction (furthest point in that
     * direction).
     * Used for GJK and SAT algorithms.
     * 
     * @param direction the direction vector
     * @return the support point in local coordinates
     */
    Vector<Real> getSupportPoint(Vector<Real> direction);

    /**
     * compute AABB (Axis Aligned Bounding Box).
     * 
     * @return array of two Vectors {min, max}
     */
    Vector<Real>[] getAABB();
}
