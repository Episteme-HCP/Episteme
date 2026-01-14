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

package org.jscience.physics.kinematics;


//import com.dautelle.physics.*;
//import com.dautelle.physics.models.*;
import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;

import org.jscience.physics.Particle;

import org.jscience.util.IllegalDimensionException;
import org.jscience.util.Positioned;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing various properties of particles.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//may be we could rename applyForce to attachForce
//may be we could use org.jscience.geoggrpahy.Coordinates interface
public abstract class AbstractClassicalParticle extends Particle
    implements Positioned {
    /** DOCUMENT ME! */
    private Set forces;

/**
     * Creates a new AbstractClassicalParticle object.
     */
    public AbstractClassicalParticle() {
        forces = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getDimension();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double getMass();

    //electrostatic charge if any
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double getCharge();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractDoubleVector getPosition();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractDoubleVector getVelocity();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getMomentum() {
        return getVelocity().scalarMultiply(getMass());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double speed() {
        return getVelocity().norm();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        AbstractDoubleVector vel = getVelocity();

        return (getMass() * vel.scalarProduct(vel)) / 2.0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractClassicalParticle move(double dt);

    /**
     * DOCUMENT ME!
     *
     * @param F DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AbstractClassicalParticle applyForce(Force F, double t,
        double dt);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Set getForces() {
        return forces;
    }

    //force must be of the same number of dimension than the particle
    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void addForce(Force f) throws IllegalDimensionException {
        if (f != null) {
            if (f.getParticle().equals(this)) {
                if (f.getDimension() == getDimension()) {
                    forces.add(f);
                } else {
                    throw new IllegalDimensionException(
                        "Force must be of dimension getDimension().");
                }
            } else {
                throw new IllegalArgumentException(
                    "You can only add a Force which acts on this particle.");
            }
        } else {
            throw new IllegalArgumentException(
                "Can't add a Force with null argument.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public final void removeForce(Force f) {
        forces.remove(f);
    }

    /**
     * DOCUMENT ME!
     */
    public final void removeAllForces() {
        forces = Collections.EMPTY_SET;
    }

    //each element of the set must be an force and with the same number of dimensions than the particle
    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public final void setForces(Set set)
        throws IllegalDimensionException, IllegalArgumentException {
        Iterator iterator;
        boolean valid;

        if (set != null) {
            iterator = set.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Force;
            }

            if (valid) {
                iterator = set.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = (((Force) iterator.next()).getParticle().equals(this));
                }

                if (valid) {
                    iterator = set.iterator();
                    valid = true;

                    while (iterator.hasNext() && valid) {
                        valid = (((Force) iterator.next()).getDimension() == getDimension());
                    }

                    if (valid) {
                        forces.add(set);
                    } else {
                        throw new IllegalDimensionException(
                            "Forces must all be of dimension getDimension().");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "You can only add Forces which act on this particle.");
                }
            } else {
                throw new IllegalArgumentException(
                    "Forces in the Set must all be Forces.");
            }
        } else {
            throw new IllegalArgumentException(
                "Forces doesn't accpet null argument.");
        }
    }
}
