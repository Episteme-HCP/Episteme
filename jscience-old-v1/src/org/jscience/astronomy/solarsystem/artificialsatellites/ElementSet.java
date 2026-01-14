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

package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * 
 */
public class ElementSet {
    /**
     * DOCUMENT ME!
     */
    double XMO = 0.0;

    /**
     * DOCUMENT ME!
     */
    double XNODEO = 0.0;

    /**
     * DOCUMENT ME!
     */
    double OMEGAO = 0.0;

    /**
     * DOCUMENT ME!
     */
    double EO = 0.0;

    /**
     * DOCUMENT ME!
     */
    double XINCL = 0.0;

    /**
     * DOCUMENT ME!
     */
    double XNO = 0.0;

    /**
     * DOCUMENT ME!
     */
    double XNDT2O = 0.0;

    /**
     * DOCUMENT ME!
     */
    double XNDD6O = 0.0;

    /**
     * DOCUMENT ME!
     */
    double BSTAR = 0.0;

    /**
     * DOCUMENT ME!
     */
    double EPOCH = 0.0;

    /**
     * DOCUMENT ME!
     */
    double DS50 = 0.0;

    /**
     * DOCUMENT ME!
     */
    private final String line1;

    /**
     * DOCUMENT ME!
     */
    private final String line2;

/**
     * Constructs an instance of this class from a NORAD two-line element set.
     */
    public ElementSet(String[] tle) {
        this(tle[0], tle[1]);
    }

/**
     * Constructs an instance of this class from a NORAD two-line element set.
     */
    public ElementSet(String tleLine1, String tleLine2) {
        this.line1 = tleLine1;
        this.line2 = tleLine2;

        String ITYPE = line1.substring(79, 80);
        int IEXP = 0;
        int IBEXP = 0;

        if (!ITYPE.equals("G")) {
            //          702 FORMAT(18X,D14.8,1X,F10.8,2(1X,F6.5,I2),/,7X,2(1X,F8.4),1X,F7.7,2(1X,F8.4),1X,F11.8)
            EPOCH = Double.parseDouble(line1.substring(18, 32));
            XNDT2O = Double.parseDouble(line1.substring(33, 43));
            XNDD6O = Double.parseDouble("." + line1.substring(45, 50));
            IEXP = Integer.parseInt(line1.substring(50, 52));
            BSTAR = Double.parseDouble("." + line1.substring(54, 59));
            IBEXP = Integer.parseInt(line1.substring(59, 61));
            XINCL = Double.parseDouble(line2.substring(8, 16));
            XNODEO = Double.parseDouble(line2.substring(17, 25));
            EO = Double.parseDouble("." + line2.substring(26, 33));
            OMEGAO = Double.parseDouble(line2.substring(34, 42));
            XMO = Double.parseDouble(line2.substring(43, 51));
            XNO = Double.parseDouble(line2.substring(52, 63));
        } else {
            //          701 FORMAT(29X,D14.8,1X,3F8.4,/,6X,F8.7,F8.4,1X,2F11.9,1X,F6.5,I2,4X,F8.7,I2)
            EPOCH = Double.parseDouble(line1.substring(29, 43));
            XMO = Double.parseDouble(line1.substring(44, 52));
            XNODEO = Double.parseDouble(line1.substring(52, 60));
            OMEGAO = Double.parseDouble(line1.substring(60, 68));
            EO = Double.parseDouble(line2.substring(6, 14));
            XINCL = Double.parseDouble(line2.substring(14, 22));
            XNO = Double.parseDouble(line2.substring(23, 34));
            XNDT2O = Double.parseDouble(line2.substring(34, 45));
            XNDD6O = Double.parseDouble(line2.substring(46, 52));
            IEXP = Integer.parseInt(line2.substring(52, 54));
            BSTAR = Double.parseDouble(line2.substring(58, 66));
            IBEXP = Integer.parseInt(line2.substring(66, 68));
        }

        XNDD6O = XNDD6O * Math.pow(10., IEXP);
        XNODEO = XNODEO * C2.DE2RA;
        OMEGAO = OMEGAO * C2.DE2RA;
        XMO = XMO * C2.DE2RA;
        XINCL = XINCL * C2.DE2RA;

        double TEMP = C2.TWOPI / C1.XMNPDA / C1.XMNPDA;
        XNO = XNO * TEMP * C1.XMNPDA;
        XNDT2O = XNDT2O * TEMP;
        XNDD6O = (XNDD6O * TEMP) / C1.XMNPDA;
        BSTAR = (BSTAR * Math.pow(10., IBEXP)) / C1.AE;
        DS50 = MathUtils.computeDS50(EPOCH);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return Line 2 of the two-line element set.
     */
    public String getLine1() {
        return line1;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return Line 2 of the two-line element set.
     */
    public String getLine2() {
        return line2;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return true if the element set is for a deep-space orbit, false if it's
     *         for a near-earth orbit.
     */
    public boolean isDeep() {
        double A1 = Math.pow((C1.XKE / XNO), C1.TOTHRD);
        double TEMP = (1.5 * C1.CK2 * ((3. * Math.pow(Math.cos(XINCL), 2)) -
            1.)) / Math.pow((1. - (EO * EO)), 1.5);
        double DEL1 = TEMP / (A1 * A1);
        double AO = A1 * (1. -
            (DEL1 * ((.5 * C1.TOTHRD) + (DEL1 * (1. + (134. / 81. * DEL1))))));
        double DELO = TEMP / (AO * AO);
        double XNODP = XNO / (1. + DELO);

        return ((C2.TWOPI / XNODP / C1.XMNPDA) >= .15625);
    }
}
