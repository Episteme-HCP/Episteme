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

import java.util.Enumeration;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class EthiopicCalendar extends CopticCalendar {
    /** DOCUMENT ME! */
    public static long EPOCH = (new JulianCalendar(8, 29, 8)).toRD();

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Maskaram", "Teqemt", "Khedar", "Takhsas", "Ter", "Yakatit",
            "Magabit", "Miyazya", "Genbot", "Sane", "Hamle", "Nahase",
            "Paguemen"
        };

/**
     * Creates a new EthiopicCalendar object.
     */
    public EthiopicCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public EthiopicCalendar(long l) {
        set(l);
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public EthiopicCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new EthiopicCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public EthiopicCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        super.rd = (EPOCH - 1L) + (long) (365 * (super.year - 1)) +
            AlternateCalendar.fldiv(super.year, 4L) +
            (long) (30 * (super.month - 1)) + (long) super.day;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((4L * (super.rd - EPOCH)) +
                1463L, 1461L);
        super.month = (int) AlternateCalendar.fldiv(super.rd -
                (new EthiopicCalendar(1, 1, super.year)).toRD(), 30L) + 1;
        super.day = (int) ((super.rd + 1L) -
            (new EthiopicCalendar(super.month, 1, super.year)).toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration(MONTHS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        EthiopicCalendar ethiopic = new EthiopicCalendar(gregorian);
        System.out.println(gregorian + ": " + ethiopic);
        ethiopic.set(i, j, k);
        System.out.println("EthiopicCalendar(" + i + "," + j + "," + k + "): " +
            ethiopic);
        System.out.println(ethiopic.toRD());
    }
}
