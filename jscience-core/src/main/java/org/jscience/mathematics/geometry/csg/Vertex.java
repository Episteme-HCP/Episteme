package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a vertex in the CSG mesh.
 * Contains position and normal.
 */
public class Vertex {
    public final Point3D pos;
    public final Vector3D normal;

    public Vertex(Point3D pos, Vector3D normal) {
        this.pos = pos;
        this.normal = normal;
    }

    public Vertex clone() {
        return new Vertex(pos, normal);
    }

    public void flip() {
        // Immutable flip requires returning new Vertex, but CSG.js flips in place or
        // creates new.
        // Let's create new.
        // But for splitPolygon interpolation we need new vertices.
        // Wait, flip logic in CSG often flips normal.
    }

    public Vertex flipped() {
        return new Vertex(pos, normal.multiply(Real.of(-1)));
    }

    public Vertex interpolate(Vertex other, Real t) {
        Vector3D p1 = new Vector3D(pos.getX(), pos.getY(), pos.getZ());
        Vector3D p2 = new Vector3D(other.pos.getX(), other.pos.getY(), other.pos.getZ());
        Vector3D n1 = normal;
        Vector3D n2 = other.normal;

        Vector3D p = p1.add(p2.subtract(p1).multiply(t));
        Vector3D n = n1.add(n2.subtract(n1).multiply(t)).normalize();

        return new Vertex(Point3D.of(p.x(), p.y(), p.z()), n);
    }
}
