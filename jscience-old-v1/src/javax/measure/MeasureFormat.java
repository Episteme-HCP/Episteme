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

package javax.measure;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.measure.unit.CompoundUnit;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

/**
 * <p> This class provides the interface for formatting and parsing {@link 
 *     Measure measures}.</p>
 *     
 * <p> As a minimum, instances of this class should be able to parse/format
 *     measure using {@link CompoundUnit}. </p>    
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.2, August 26, 2007
 */
public abstract class MeasureFormat extends Format {
   
    /**
     * Returns the measure format for the default locale.
     * 
     *  @return <code>getInstance(Number.getInstance(), Unit.getInstance())</code>
     */
    public static MeasureFormat getInstance() {
        return DEFAULT;
    }

    static final NumberUnit DEFAULT = new NumberUnit(NumberFormat
            .getInstance(), UnitFormat.getInstance());

    /**
     * Returns the measure format using the specified number format and 
     * unit format (the number and unit are separated by a space).
     * 
     * @param numberFormat the number format.
     * @param unitFormat the unit format.
     * @return the corresponding format.
     */
    public static MeasureFormat getInstance(NumberFormat numberFormat,
            UnitFormat unitFormat) {
        return new NumberUnit(numberFormat, unitFormat);
    }

    // Holds default implementation.
    static final class NumberUnit extends MeasureFormat {
        private final NumberFormat _numberFormat;

        private final UnitFormat _unitFormat;

        private NumberUnit(NumberFormat numberFormat, UnitFormat unitFormat) {
            _numberFormat = numberFormat;
            _unitFormat = unitFormat;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo,
                FieldPosition pos) {
            Measure<?, ?> measure = (Measure<?, ?>) obj;
            Object value = measure.getValue();
            Unit<?> unit = measure.getUnit();
            if (value instanceof Number) {
                if (unit instanceof CompoundUnit)
                    return formatCompound(((Number) value).doubleValue(),
                            (CompoundUnit<?>) unit, toAppendTo, pos);
                _numberFormat.format(value, toAppendTo, pos);
            } else {
                toAppendTo.append(value);
            }
            if (!measure.getUnit().equals(Unit.ONE)) {
                toAppendTo.append(' ');
                _unitFormat.format(unit, toAppendTo, pos);
            }
            return toAppendTo;
        }

        // Measure using Compound unit have no separators in their representation.
        StringBuffer formatCompound(double value, Unit<?> unit,
                StringBuffer toAppendTo, FieldPosition pos) {
            if (!(unit instanceof CompoundUnit)) {
                toAppendTo.append((long) value);
                return _unitFormat.format(unit, toAppendTo, pos);
            }
            Unit<?> high = ((CompoundUnit<?>) unit).getHigher();
            Unit<?> low = ((CompoundUnit<?>) unit).getLower(); // The unit in which the value is stated.
            long highValue = (long) low.getConverterTo(high).convert(value);
            double lowValue = value
                    - high.getConverterTo(low).convert(highValue);
            formatCompound(highValue, high, toAppendTo, pos);
            formatCompound(lowValue, low, toAppendTo, pos);
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            int start = pos.getIndex();
            try {
                int i = start;
                Number value = _numberFormat.parse(source, pos);
                if (i == pos.getIndex())
                    return null; // Cannot parse.
                i = pos.getIndex();
                if (i >= source.length())
                    return measureOf(value, Unit.ONE); // No unit.
                boolean isCompound = !Character.isWhitespace(source.charAt(i));
                if (isCompound)
                    return parseCompound(value, source, pos);
                if (++i >= source.length())
                    return measureOf(value, Unit.ONE); // No unit.
                pos.setIndex(i); // Skips separator.
                Unit<?> unit = _unitFormat.parseProductUnit(source, pos);
                return measureOf(value, unit);
            } catch (ParseException e) {
                pos.setIndex(start);
                pos.setErrorIndex(e.getErrorOffset());
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        private Object parseCompound(Number highValue, String source,
                ParsePosition pos) throws ParseException {
            Unit high = _unitFormat.parseSingleUnit(source, pos);
            int i = pos.getIndex();
            if (i >= source.length()
                    || Character.isWhitespace(source.charAt(i)))
                return measureOf(highValue, high);
            Measure lowMeasure = (Measure) parseObject(source, pos);
            Unit unit = lowMeasure.getUnit();
            long l = lowMeasure.longValue(unit)
                    + (long) high.getConverterTo(unit).convert(
                            highValue.longValue());
            return Measure.valueOf(l, unit);
        }

        @SuppressWarnings("unchecked")
        private static Measure measureOf(Number value, Unit unit) {
            if (value instanceof Double) {
                return Measure.valueOf(value.doubleValue(), unit);
            } else if (value instanceof Long) {
                return Measure.valueOf(value.longValue(), unit);
            } else if (value instanceof Float) {
                return Measure.valueOf(value.floatValue(), unit);
            } else if (value instanceof Integer) {
                return Measure.valueOf(value.intValue(), unit);
            } else if (value instanceof BigDecimal) {
                return DecimalMeasure.valueOf((BigDecimal) value, unit);
            } else {
                return Measure.valueOf(value.doubleValue(), unit);
            }
        }

        private static final long serialVersionUID = 1L;
    }
}
