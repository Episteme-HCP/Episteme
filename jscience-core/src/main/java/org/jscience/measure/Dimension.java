/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.measure;

/**
 * Represents the physical dimension of a quantity.
 * <p>
 * A dimension is defined by the exponents of the seven SI base dimensions:
 * Length (L), Mass (M), Time (T), Electric Current (I), Temperature (Θ),
 * Amount of Substance (N), and Luminous Intensity (J).
 * </p>
 * <p>
 * Dimensions enable dimensional analysis - checking that equations are
 * dimensionally consistent. For example:
 * </p>
 * 
 * <pre>
 * Force = Mass × Acceleration
 * [F] = [M] × [L][T]⁻²
 * [F] = [M¹L¹T⁻²]
 * </pre>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * // Base dimensions
 * Dimension length = Dimension.LENGTH; // [L¹]
 * Dimension mass = Dimension.MASS; // [M¹]
 * Dimension time = Dimension.TIME; // [T¹]
 * 
 * // Derived dimensions
 * Dimension velocity = length.divide(time); // [L¹T⁻¹]
 * Dimension force = mass.multiply(length).divide(time.pow(2)); // [M¹L¹T⁻²]
 * 
 * // Dimensional checking
 * assert force.equals(Dimension.of(1, 1, -2, 0, 0, 0, 0));
 * }</pre>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Dimension {

    /**
     * Dimensionless (no dimension) - all exponents are zero.
     */
    /**
     * Dimensionless (no dimension) - all exponents are zero.
     */
    Dimension NONE = of(0, 0, 0, 0, 0, 0, 0);

    /**
     * Dimensionless (alias for NONE).
     */
    Dimension DIMENSIONLESS = NONE;

    /**
     * Length dimension [L].
     */
    Dimension LENGTH = of(1, 0, 0, 0, 0, 0, 0);

    /**
     * Mass dimension [M].
     */
    Dimension MASS = of(0, 1, 0, 0, 0, 0, 0);

    /**
     * Time dimension [T].
     */
    Dimension TIME = of(0, 0, 1, 0, 0, 0, 0);

    /**
     * Electric current dimension [I].
     */
    Dimension ELECTRIC_CURRENT = of(0, 0, 0, 1, 0, 0, 0);

    /**
     * Temperature dimension [Θ].
     */
    Dimension TEMPERATURE = of(0, 0, 0, 0, 1, 0, 0);

    /**
     * Amount of substance dimension [N].
     */
    Dimension AMOUNT_OF_SUBSTANCE = of(0, 0, 0, 0, 0, 1, 0);

    /**
     * Luminous intensity dimension [J].
     */
    Dimension LUMINOUS_INTENSITY = of(0, 0, 0, 0, 0, 0, 1);

    /**
     * Returns the length exponent (L).
     * 
     * @return the length exponent
     */
    int getLengthExponent();

    /**
     * Returns the mass exponent (M).
     * 
     * @return the mass exponent
     */
    int getMassExponent();

    /**
     * Returns the time exponent (T).
     * 
     * @return the time exponent
     */
    int getTimeExponent();

    /**
     * Returns the electric current exponent (I).
     * 
     * @return the electric current exponent
     */
    int getElectricCurrentExponent();

    /**
     * Returns the temperature exponent (Θ).
     * 
     * @return the temperature exponent
     */
    int getTemperatureExponent();

    /**
     * Returns the amount of substance exponent (N).
     * 
     * @return the amount of substance exponent
     */
    int getAmountOfSubstanceExponent();

    /**
     * Returns the luminous intensity exponent (J).
     * 
     * @return the luminous intensity exponent
     */
    int getLuminousIntensityExponent();

    /**
     * Returns the product of this dimension with another.
     * <p>
     * Multiplying dimensions adds their exponents.
     * </p>
     * 
     * @param other the dimension to multiply with
     * @return the product dimension
     */
    Dimension multiply(Dimension other);

    /**
     * Returns the quotient of this dimension by another.
     * <p>
     * Dividing dimensions subtracts their exponents.
     * </p>
     * 
     * @param other the dimension to divide by
     * @return the quotient dimension
     */
    Dimension divide(Dimension other);

    /**
     * Returns this dimension raised to the specified power.
     * <p>
     * Raising to a power multiplies all exponents by the power.
     * </p>
     * 
     * @param exponent the exponent
     * @return the dimension raised to the power
     */
    Dimension pow(int exponent);

    /**
     * Returns the square root of this dimension.
     * <p>
     * All exponents must be even for the square root to be defined.
     * </p>
     * 
     * @return the square root dimension
     * @throws IllegalArgumentException if any exponent is odd
     */
    Dimension sqrt();

    /**
     * Checks if this is a dimensionless quantity.
     * 
     * @return true if all exponents are zero
     */
    default boolean isDimensionless() {
        return equals(NONE);
    }

    /**
     * Creates a dimension with the specified exponents.
     * 
     * @param length            length exponent (L)
     * @param mass              mass exponent (M)
     * @param time              time exponent (T)
     * @param electricCurrent   electric current exponent (I)
     * @param temperature       temperature exponent (Θ)
     * @param amountOfSubstance amount of substance exponent (N)
     * @param luminousIntensity luminous intensity exponent (J)
     * @return the dimension
     */
    static Dimension of(int length, int mass, int time, int electricCurrent,
            int temperature, int amountOfSubstance, int luminousIntensity) {
        return new StandardDimension(length, mass, time, electricCurrent,
                temperature, amountOfSubstance, luminousIntensity);
    }
}