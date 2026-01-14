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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class AlternateCalendar {
    /** DOCUMENT ME! */
    public static long EPOCH;

    /** DOCUMENT ME! */
    public static boolean unicode;

    /** DOCUMENT ME! */
    public static final double JD_EPOCH = -1721424.5D;

    /** DOCUMENT ME! */
    protected long rd;

/**
     * Creates a new AlternateCalendar object.
     */
    public AlternateCalendar() {
        rd = EPOCH;
    }

/**
     * Creates a new AlternateCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public AlternateCalendar(long l) {
        rd = l;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long mod(long l, long l1) {
        return l - (l1 * fldiv(l, l1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int mod(long l, int i) {
        return (int) mod(l, i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long amod(long l, long l1) {
        return 1L + mod(l - 1L, l1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long fldiv(long l, long l1) {
        long l2 = l / l1;

        if ((l2 * l1) > l) {
            return l2 - 1L;
        } else {
            return l2;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public abstract void set(long l);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long toRD() {
        return rd;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double toJD() {
        return toJD(toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toJD(long l) {
        return (double) l - -1721424.5D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long fromJD(double d) {
        return (long) Math.floor(d + -1721424.5D);
    }

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean before(AlternateCalendar altcalendar) {
        return toRD() > altcalendar.toRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean after(AlternateCalendar altcalendar) {
        return toRD() < altcalendar.toRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     * @param altcalendar1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long difference(AlternateCalendar altcalendar,
        AlternateCalendar altcalendar1) {
        return altcalendar.toRD() - altcalendar1.toRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long difference(AlternateCalendar altcalendar) {
        return difference(this, altcalendar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void add(long l) {
        set(rd + l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void subtract(long l) {
        set(rd - l);
    }

    /**
     * DOCUMENT ME!
     */
    protected abstract void recomputeFromRD();

    /**
     * DOCUMENT ME!
     */
    protected abstract void recomputeRD();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();
}
