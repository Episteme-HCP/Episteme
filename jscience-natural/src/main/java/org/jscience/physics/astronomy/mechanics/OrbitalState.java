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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Velocity;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OrbitalState {

    private final Quantity<Length> x, y, z;
    private final Quantity<Velocity> vx, vy, vz;

    public OrbitalState(Quantity<Length> x, Quantity<Length> y, Quantity<Length> z,
            Quantity<Velocity> vx, Quantity<Velocity> vy, Quantity<Velocity> vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public Quantity<Length> getX() {
        return x;
    }

    public Quantity<Length> getY() {
        return y;
    }

    public Quantity<Length> getZ() {
        return z;
    }

    public Quantity<Velocity> getVx() {
        return vx;
    }

    public Quantity<Velocity> getVy() {
        return vy;
    }

    public Quantity<Velocity> getVz() {
        return vz;
    }

    @Override
    public String toString() {
        return String.format("Pos[%s, %s, %s] Vel[%s, %s, %s]", x, y, z, vx, vy, vz);
    }
}


