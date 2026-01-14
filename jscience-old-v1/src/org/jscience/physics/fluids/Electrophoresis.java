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

package org.jscience.physics.fluids;

import org.jscience.physics.PhysicsConstants;


/**
 * The class defines several methods to describe Electrophoresis
 * properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//also see org.jscience.biology.BiologyUtils
public class Electrophoresis extends Object {
    /**
     * Creates a new Electrophoresis object.
     */
    private Electrophoresis() {
    }

    //The velocity with which a solute moves through the conductive medium due to its electrophoretic mobility.
    /**
     * DOCUMENT ME!
     *
     * @param charge DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param viscosity DOCUMENT ME!
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getElectrophoreticVelocity(double charge,
        double field, double viscosity, double radius) {
        return (charge * field) / (6 * Math.PI * viscosity * radius);
    }

    //The velocity with which the solute moves through the capillary due to the electroosmotic flow
    /**
     * DOCUMENT ME!
     *
     * @param dielectric DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param viscosity DOCUMENT ME!
     * @param zeta DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getElectroosmoticFlowVelocity(double dielectric,
        double field, double viscosity, double zeta) {
        return (dielectric * zeta * field) / (4 * Math.PI * viscosity);
    }

    //A solute?s total velocity is the sum of its electrophoretic velocity and the electroosmotic flow velocity
    /**
     * DOCUMENT ME!
     *
     * @param pressureDrop DOCUMENT ME!
     * @param viscosity DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getVoluminalLaminarFlow(double pressureDrop,
        double viscosity, double length, double radius) {
        return (Math.PI * Math.pow(radius, 4) * pressureDrop) / (8 * length * viscosity);
    }

    //Re is the Reynolds number and ? fluid density
    /**
     * DOCUMENT ME!
     *
     * @param density DOCUMENT ME!
     * @param viscosity DOCUMENT ME!
     * @param velocity DOCUMENT ME!
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getReynoldsNumber(double density, double viscosity,
        double velocity, double radius) {
        return (2 * density * velocity * radius) / viscosity;
    }

    //http://en.wikipedia.org/wiki/Prony_equation
    /**
     * DOCUMENT ME!
     *
     * @param friction DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param diameter DOCUMENT ME!
     * @param velocity DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getHeadLoss(double friction, double length,
        double diameter, double velocity) {
        return (friction * (length / diameter) * velocity * velocity) / (2 * PhysicsConstants.G);
    }
}
