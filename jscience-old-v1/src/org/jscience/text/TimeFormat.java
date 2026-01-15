package org.jscience.text;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import java.util.Date;


/**
 * The TimeFormat class formats a number as a date string, by first scaling
 * the number and adding an offset, and then treating the result as a time in
 * milliseconds.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class TimeFormat extends NumberFormat {
    /** DOCUMENT ME! */
    private DateFormat dateFormat;

    /** DOCUMENT ME! */
    private double scale;

    /** DOCUMENT ME! */
    private long offset;

/**
     * Constructs a TimeFormat object.
     *
     * @param dateFormat DOCUMENT ME!
     * @param scale      DOCUMENT ME!
     * @param offset     in milliseconds.
     */
    public TimeFormat(DateFormat dateFormat, double scale, long offset) {
        this.dateFormat = dateFormat;
        this.scale = scale;
        this.offset = offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Date toDate(double x) {
        return new Date(((long) (scale * x)) + offset);
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     * @param toAppendTo DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StringBuffer format(double number, StringBuffer toAppendTo,
        FieldPosition pos) {
        pos.setBeginIndex(-1);
        pos.setEndIndex(-1);

        return toAppendTo.append(dateFormat.format(toDate(number)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     * @param toAppendTo DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StringBuffer format(long number, StringBuffer toAppendTo,
        FieldPosition pos) {
        pos.setBeginIndex(-1);
        pos.setEndIndex(-1);

        return toAppendTo.append(dateFormat.format(toDate(number)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Number toNumber(Date date) {
        return new Double((date.getTime() - offset) / scale);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     * @param parsePosition DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number parse(String text, ParsePosition parsePosition) {
        return toNumber(dateFormat.parse(text, parsePosition));
    }
}
