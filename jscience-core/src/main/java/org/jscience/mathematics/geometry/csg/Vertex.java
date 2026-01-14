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

package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a vertex in the CSG mesh.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


