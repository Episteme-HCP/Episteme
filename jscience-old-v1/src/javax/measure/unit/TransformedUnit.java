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

package javax.measure.unit;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
        
/**
 * <p> This class represents the units derived from other units using
 *     {@link UnitConverter converters}.</p>
 *     
 * <p> Examples of transformed units:[code]
 *         CELSIUS = KELVIN.add(273.15);
 *         FOOT = METER.multiply(0.3048);
 *         MILLISECOND = MILLI(SECOND); 
 *     [/code]</p>
 *     
 * <p> Transformed units have no label. But like any other units,
 *     they may have labels attached to them:[code]
 *         UnitFormat.getStandardInstance().label(FOOT, "ft");
 *     [/code]
 *     or aliases: [code]
 *         UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimeter");
 *         UnitFormat.getStandardInstance().alias(CENTI(METER)), "centimetre");
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 * @see     Unit#plus(double)
 * @see     Unit#times(double)
 * @see     Unit#transform(UnitConverter)
 * @see     UnitFormat
 */
public final class TransformedUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the parent unit (not a transformed unit).
     */
    private final Unit<Q> _parentUnit;

    /**
     * Holds the converter to the parent unit.
     */
    private final UnitConverter _toParentUnit;

    /**
     * Creates a transformed unit from the specified parent unit.
     *
     * @param parentUnit the untransformed unit from which this unit is 
     *        derived.
     * @param  toParentUnit the converter to the parent units.
     * @throws IllegalArgumentException if <code>toParentUnit == 
     *         {@link UnitConverter#IDENTITY UnitConverter.IDENTITY}</code>
     */
    TransformedUnit(Unit<Q> parentUnit, UnitConverter toParentUnit) {
        if (toParentUnit == UnitConverter.IDENTITY)
            throw new IllegalArgumentException("Identity not allowed");
        _parentUnit = parentUnit;
        _toParentUnit = toParentUnit;
    }
        
    /**
     * Returns the parent unit for this unit. The parent unit is the 
     * untransformed unit from which this unit is derived.
     *
     * @return the untransformed unit from which this unit is derived.
     */
    public Unit<Q> getParentUnit() {
        return _parentUnit;
    }
        
    /**
     * Returns the converter to the parent unit.
     *
     * @return the converter to the parent unit.
     */
    public UnitConverter toParentUnit() {
        return _toParentUnit;
    }
        
    /**
     * Indicates if this transformed unit is considered equals to the specified 
     * object (both are transformed units with equal parent unit and equal
     * converter to parent unit).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof TransformedUnit)) return false;
        TransformedUnit<?> thatUnit = (TransformedUnit<?>) that; 
        return this._parentUnit.equals(thatUnit._parentUnit) &&
                 this._toParentUnit.equals(thatUnit._toParentUnit);
    }

    // Implements abstract method.
    public int hashCode() {
        return _parentUnit.hashCode() + _toParentUnit.hashCode();
    }

    // Implements abstract method.
    public Unit<? super Q> getStandardUnit() {
        return _parentUnit.getStandardUnit();
    }

    // Implements abstract method.
    public UnitConverter toStandardUnit() {
        return _parentUnit.toStandardUnit().concatenate(_toParentUnit);
    }

    private static final long serialVersionUID = 1L;

}
