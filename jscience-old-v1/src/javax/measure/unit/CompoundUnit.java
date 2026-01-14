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
 * <p> This class represents the multi-radix units (such as "hour:min:sec"). 
 *     Instances of this class are created using the {@link Unit#compound
 *     Unit.compound} method.</p>
 *      
 * <p> Examples of compound units:[code]
 *     Unit<Duration> HOUR_MINUTE_SECOND = HOUR.compound(MINUTE).compound(SECOND);
 *     Unit<Angle> DEGREE_MINUTE_ANGLE = DEGREE_ANGLE.compound(MINUTE_ANGLE);
 *     [/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class CompoundUnit<Q extends Quantity> extends DerivedUnit<Q> {

    /**
     * Holds the higher unit.
     */
    private final Unit<Q> _high;

    /**
     * Holds the lower unit.
     */
    private final Unit<Q> _low;

    /**
     * Creates a compound unit from the specified units. 
     *
     * @param  high the high unit.
     * @param  low the lower unit(s)
     * @throws IllegalArgumentException if both units do not the same system
     *         unit.
     */
    CompoundUnit(Unit<Q> high, Unit<Q> low) {
        if (!high.getStandardUnit().equals(low.getStandardUnit()))
            throw new IllegalArgumentException(
                    "Both units do not have the same system unit");
        _high = high;
        _low = low;
        
    }

    /**
     * Returns the lower unit of this compound unit.
     *
     * @return the lower unit.
     */
    public Unit<Q> getLower() {
        return _low;
    }

    /**
     * Returns the higher unit of this compound unit.
     *
     * @return the higher unit.
     */
    public Unit<Q> getHigher() {
        return _high;
    }

    /**
     * Indicates if this compound unit is considered equals to the specified 
     * object (both are compound units with same composing units in the 
     * same order).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof CompoundUnit))
            return false;
        CompoundUnit<?> thatUnit = (CompoundUnit<?>) that;
        return this._high.equals(thatUnit._high)
                && this._low.equals(thatUnit._low);
    }

    @Override
    public int hashCode() {
        return _high.hashCode() ^ _low.hashCode();
    }

    @Override
    public Unit<? super Q> getStandardUnit() {
        return _low.getStandardUnit(); 
    }

    @Override
    public UnitConverter toStandardUnit() {
        return _low.toStandardUnit();
    }

    private static final long serialVersionUID = 1L;
}
