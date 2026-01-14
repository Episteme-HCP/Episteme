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

package org.jscience.economics.resources;

import com.sun.j3d.utils.behaviors.interpolators.KBRotPosScaleSplinePathInterpolator;

import org.jscience.economics.Community;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * A class representing a built track, namely a road.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//every path (from somewhere to somewhere)
//railroads and roads mostly
//corresponds to something that is actually built and not only a footstep path
public class Road extends Artifact {
    //the following classes are especially good candidates for an actual Track implementation
    //com.sun.j3d.utils.behaviors.interpolators.KBRotPosScaleSplinePathInterpolator
    //com.sun.j3d.utils.behaviors.interpolators.RotPosScaleTCBSplinePathInterpolator
    //javax.media.j3d.RotPosScalePathInterpolator
    /** DOCUMENT ME! */
    private KBRotPosScaleSplinePathInterpolator path;

/**
     * Creates a new Road object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Road(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KBRotPosScaleSplinePathInterpolator getPath() {
        return path;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public void setPath(KBRotPosScaleSplinePathInterpolator path) {
        this.path = path;
    }
}
