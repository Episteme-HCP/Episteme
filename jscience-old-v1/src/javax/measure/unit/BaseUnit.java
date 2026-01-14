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
 * <p> This class represents the building blocks on top of which all others
 *     units are created. Base units are typically dimensionally independent.
 *     The actual unit dimension is determinated by the current 
 *     {@link Dimension.Model model}. For example using the {@link 
 *     Dimension.Model#STANDARD standard} model, {@link SI#CANDELA} 
 *     has the dimension of {@link SI#WATT watt}:[code]
 *     // Standard model.
 *     BaseUnit<Length> METER = new BaseUnit<Length>("m");
 *     BaseUnit<LuminousIntensity> CANDELA = new BaseUnit<LuminousIntensity>("cd");
 *     System.out.println(METER.getDimension());
 *     System.out.println(CANDELA.getDimension());
 *     
 *     > [L]
 *     > [L]²·[M]/[T]³
 *     [/code]</p>
 * <p> This class represents the "standard base units" which includes SI base 
 *     units and possibly others user-defined base units. It does not represent 
 *     the base units of any specific {@link SystemOfUnits} (they would have 
 *     be base units accross all possible systems otherwise).</p> 
 *           
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 * @see <a href="http://en.wikipedia.org/wiki/SI_base_unit">
 *       Wikipedia: SI base unit</a>
 */
public class BaseUnit<Q extends Quantity> extends Unit<Q> {

    /**
     * Holds the symbol.
     */
    private final String _symbol;

    /**
     * Creates a base unit having the specified symbol. 
     *
     * @param symbol the symbol of this base unit.
     * @throws IllegalArgumentException if the specified symbol is 
     *         associated to a different unit.
     */
    public BaseUnit(String symbol) {
        _symbol = symbol;
        // Checks if the symbol is associated to a different unit.
        synchronized (Unit.SYMBOL_TO_UNIT) {
            Unit<?> unit = Unit.SYMBOL_TO_UNIT.get(symbol);
            if (unit == null) {
                Unit.SYMBOL_TO_UNIT.put(symbol, this);
                return;
            }
            if (!(unit instanceof BaseUnit)) 
               throw new IllegalArgumentException("Symbol " + symbol
                    + " is associated to a different unit");
        }
    }

    /**
     * Returns the unique symbol for this base unit. 
     *
     * @return this base unit symbol.
     */
    public final String getSymbol() {
        return _symbol;
    }

    /**
     * Indicates if this base unit is considered equals to the specified 
     * object (both are base units with equal symbol, standard dimension and 
     * standard transform).
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if <code>this</code> and <code>that</code>
     *         are considered equals; <code>false</code>otherwise. 
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof BaseUnit))
            return false;
        BaseUnit<?> thatUnit = (BaseUnit<?>) that;
        return this._symbol.equals(thatUnit._symbol); 
    }
        
    @Override
    public int hashCode() {
        return _symbol.hashCode();
    }

    @Override
    public Unit<? super Q> getStandardUnit() {
        return this;
    }

    @Override
    public UnitConverter toStandardUnit() {
        return UnitConverter.IDENTITY;
    }

    private static final long serialVersionUID = 1L;
}
