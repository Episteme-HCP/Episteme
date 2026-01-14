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

/*
 * PrimitiveGaussian.java
 *
 * Created on July 22, 2004, 7:09 AM
 */

package org.jscience.chemistry.quantum.basis;

import org.jscience.chemistry.quantum.integral.Integrals;
import org.jscience.chemistry.quantum.math.util.MathUtils;
import org.jscience.chemistry.quantum.math.util.Point3D;

/**
 * The class defines a primitive gaussian (PG) and the operations on it.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class PrimitiveGaussian {

    /**
     * Holds value of property exponent.
     */
    private double exponent;

    /**
     * Holds value of property origin.
     */
    private Point3D origin;

    /**
     * Holds value of property powers.
     */
    private Power powers;

    /**
     * Holds value of property coefficient.
     */
    private double coefficient;

    /**
     * normalization factor
     */
    private double normalization;

    /**
     * Creates a new instance of PrimitiveGaussian
     *
     * @param origin      - the (x, y, z) on which this gaussian is centered
     * @param powers      - the powers of this gaussian
     * @param exponent    - the exponent for this PG
     * @param coefficient - the coefficient for this PG
     */
    public PrimitiveGaussian(Point3D origin, Power powers,
                             double exponent, double coefficient) {
        this.origin = origin;
        this.powers = powers;
        this.exponent = exponent;
        this.coefficient = coefficient;

        this.normalization = 1;

        // normalise this PG
        normalize();
    }

    /**
     * Getter for property exponent.
     *
     * @return Value of property exponent.
     */
    public double getExponent() {
        return this.exponent;
    }

    /**
     * Setter for property exponent.
     *
     * @param exponent New value of property exponent.
     */
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    /**
     * Getter for property origin.
     *
     * @return Value of property origin.
     */
    public Point3D getOrigin() {
        return this.origin;
    }

    /**
     * Setter for property origin.
     *
     * @param origin New value of property origin.
     */
    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    /**
     * Getter for property powers.
     *
     * @return Value of property powers.
     */
    public Power getPowers() {
        return this.powers;
    }

    /**
     * Setter for property powers.
     *
     * @param powers New value of property powers.
     */
    public void setPowers(Power powers) {
        this.powers = powers;
    }

    /**
     * Getter for property coefficient.
     *
     * @return Value of property coefficient.
     */
    public double getCoefficient() {
        return this.coefficient;
    }

    /**
     * Setter for property coefficient.
     *
     * @param coefficient New value of property coefficient.
     */
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Normalize this basis function.
     * <p/>
     * <br> <i>H. Phys. Soc. Japan,</i> <b>21</b>, 2313, 1966 <br>
     */
    public void normalize() {
        int l = powers.getL(),
                m = powers.getM(),
                n = powers.getN();

        normalization = Math.sqrt(Math.pow(2, 2 * (l + m + n) + 1.5) *
                Math.pow(exponent, l + m + n + 1.5) /
                MathUtils.factorial2(2 * l - 1) /
                MathUtils.factorial2(2 * m - 1) /
                MathUtils.factorial2(2 * n - 1) /
                Math.pow(Math.PI, 1.5));
    }

    /**
     * Overlap matrix element with another PrimitiveGaussian
     *
     * @param pg the PrimitiveGaussian with which the overlap is to be
     *           be determined.
     * @return the overlap value
     */
    public double overlap(PrimitiveGaussian pg) {
        return (normalization * pg.normalization
                * Integrals.overlap(exponent, powers, origin,
                pg.exponent, pg.powers, pg.origin)
        );
    }

    /**
     * Kinetic Energy (KE) matrix element with another PrimitiveGaussian
     *
     * @param pg the PrimitiveGaussian with which KE is to be determined.
     * @return the KE value
     */
    public double kinetic(PrimitiveGaussian pg) {
        return (normalization * pg.normalization
                * Integrals.kinetic(exponent, powers, origin,
                pg.exponent, pg.powers, pg.origin)
        );
    }

    /**
     * Nuclear matrix element with another PrimitiveGaussian
     *
     * @param pg     the PrimitiveGaussian with which nuclear interaction
     *               is to be determined.
     * @param center the center at which nuclear energy is to be computed
     * @return the nuclear value
     */
    public double nuclear(PrimitiveGaussian pg, Point3D center) {
        return (Integrals.nuclearAttraction(origin, normalization, powers,
                exponent, pg.origin, pg.normalization, pg.powers,
                pg.exponent, center)
        );
    }

    /**
     * Getter for property normalization.
     *
     * @return Value of property normalization.
     */
    public double getNormalization() {
        return this.normalization;
    }

    /**
     * Setter for property normalization.
     *
     * @param normalization New value of property normalization.
     */
    public void setNormalization(double normalization) {
        this.normalization = normalization;
    }

    /**
     * overloaded toString()
     */
    public String toString() {
        return "Origin : " + origin + " Powers : " + powers
                + " Normalization : " + normalization
                + " Coefficient : " + coefficient
                + " Exponent : " + exponent;
    }
} // end of class PrimitiveGaussian
