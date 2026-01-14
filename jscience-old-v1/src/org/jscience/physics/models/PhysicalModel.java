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
 * <p> This abstract class represents a physical model. Instances of this
 * class determinate the current quantities dimensions.</p>
 * <p/>
 * <p> To select a model, one needs only to call the model <code>select</code>
 * static method. For example:[code]
 * public static void main(String[] args) {
 * // Global (LocalContext should be used for thread-local settings).
 * RelativisticModel.select();
 * ...
 * [/code]</p>
 * <p/>
 * <p> Selecting a predefined model automatically sets the dimension of
 * the {@link javax.measure.unit.BaseUnit base units}.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public abstract class PhysicalModel implements Dimension.Model {

    /**
     * Holds the current physical model.
     */
    private static PhysicalModel Current;

    /**
     * Holds the dimensional model.
     */
    private static final Dimension.Model DIMENSIONAL_MODEL
            = new Dimension.Model() {

        public Dimension getDimension(BaseUnit<?> unit) {
            return PhysicalModel.Current.getDimension(unit);
        }

        public UnitConverter getTransform(BaseUnit<?> unit) {
            return PhysicalModel.Current.getTransform(unit);
        }
    };

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicalModel() {
    }

    /**
     * Returns the current physical model (default: instance of
     * {@link StandardModel}).
     *
     * @return the context-local physical model.
     */
    public static final PhysicalModel current() {
        PhysicalModel physicalModel = PhysicalModel.Current;
        return (physicalModel == null) ? StandardModel.INSTANCE : physicalModel;
    }

    /**
     * Sets the current model (this method is called when the a predefined
     * model is selected).
     *
     * @param model the context-local physical model.
     * @see #current
     */
    protected static final void setCurrent(PhysicalModel model) {
        PhysicalModel.Current = model;
        Dimension.setModel(DIMENSIONAL_MODEL);
    }

}
