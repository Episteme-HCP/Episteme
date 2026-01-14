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

package org.jscience.physics.models;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Dimension;

/**
 * This class represents the standard model.
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public class StandardModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    final static StandardModel INSTANCE = new StandardModel();

    /**
     * Default constructor (allows for derivation).
     */
    protected StandardModel() {
    }

    /**
     * Selects the standard model as the current model.
     */
    public static void select() {
        PhysicalModel.setCurrent(INSTANCE);
    }

    // Implements Dimension.Model
    public Dimension getDimension(BaseUnit<?> unit) {
        return Dimension.Model.STANDARD.getDimension(unit);
    }

    // Implements Dimension.Model
    public UnitConverter getTransform(BaseUnit<?> unit) {
        return Dimension.Model.STANDARD.getTransform(unit);
    }

}
