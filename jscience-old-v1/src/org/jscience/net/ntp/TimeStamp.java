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

//////////////////////license & copyright header///////////////////////
//                                                                   //
//                Copyright (c) 1999 by Michel Van den Bergh         //
//                                                                   //
// This library is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU Lesser General Public        //
// License as published by the Free Software Foundation; either      //
// version 2 of the License, or (at your option) any later version.  //
//                                                                   //
// This library is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// Lesser General Public License for more details.                   //
//                                                                   //
// You should have received a copy of the GNU Lesser General Public  //
// License along with this library; if not, write to the             //
// Free Software Foundation, Inc., 59 Temple Place, Suite 330,       //
// Boston, MA  02111-1307  USA, or contact the author:               //
//                                                                   //
//                  Michel Van den Bergh  <vdbergh@luc.ac.be>        //
//                                                                   //
////////////////////end license & copyright header/////////////////////
package org.jscience.net.ntp;

import java.util.*;


/**
 * This class encapsulates the notion of a timestamp as in rfc2030.
 * Logically it is the number of seconds since the beginning of the centure
 * (in UTC time). It is represented as  an 8 byte array, the first four bytes
 * representing seconds and the next 4 bytes representing second fractions.
 *
 * @author Michel Van den Bergh
 * @version 1.0
 */
public class TimeStamp {
    /** DOCUMENT ME! */
    static private TimeZone UTC = new SimpleTimeZone(0, "UTC");

    /** DOCUMENT ME! */
    static private Calendar c = new GregorianCalendar(1900, Calendar.JANUARY,
            1, 0, 0, 0);

    /** DOCUMENT ME! */
    static private Date startOfCentury;

    static {
        c.setTimeZone(UTC);
        startOfCentury = c.getTime();
    }

    /** DOCUMENT ME! */
    private static final byte[] emptyArray = { 0, 0, 0, 0, 0, 0, 0, 0 };

    /** The timestamp corresponding to the beginning of the century. */
    public static final TimeStamp zero = new TimeStamp(emptyArray);

    /** DOCUMENT ME! */
    private long integerPart;

    /** DOCUMENT ME! */
    private long fractionalPart;

    /** DOCUMENT ME! */
    private byte[] data;

    /** DOCUMENT ME! */
    private Date date;

/**
     * This constructs a timestamp initialized with the current date.
     */
    public TimeStamp() {
        this(new Date());
    }

/**
     * This constructs a timestamp initialized with a given date.
     *
     * @param date DOCUMENT ME!
     */
    public TimeStamp(Date date) {
        data = new byte[8];
        this.date = date;

        long msSinceStartOfCentury = date.getTime() - startOfCentury.getTime();
        integerPart = msSinceStartOfCentury / 1000;
        fractionalPart = ((msSinceStartOfCentury % 1000) * 0x100000000L) / 1000;

        long temp = integerPart;

        for (int i = 3; i >= 0; i--) {
            data[i] = (byte) (temp % 256);
            temp = temp / 256;
        }

        temp = fractionalPart;

        for (int i = 7; i >= 4; i--) {
            data[i] = (byte) (temp % 256);
            temp = temp / 256;
        }
    }

/**
     * This constructs a timestamp starting from an eight byte array.
     *
     * @param data DOCUMENT ME!
     */
    public TimeStamp(byte[] data) {
        this.data = data;
        integerPart = 0;

        int u;

        for (int i = 0; i <= 3; i++) {
            integerPart = (256 * integerPart) + mp(data[i]);
        }

        fractionalPart = 0;

        for (int i = 4; i <= 7; i++) {
            fractionalPart = (256 * fractionalPart) + mp(data[i]);
        }

        long msSinceStartOfCentury = (integerPart * 1000) +
            ((fractionalPart * 1000) / 0x100000000L);
        date = new Date(msSinceStartOfCentury + startOfCentury.getTime());
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int mp(byte b) {
        int bb = b;

        return (bb < 0) ? (256 + bb) : bb;
    }

    /**
     * Checks for equality of two timestamps.
     *
     * @param ts DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(TimeStamp ts) {
        boolean value = true;
        byte[] tsData = ts.getData();

        for (int i = 0; i <= 7; i++) {
            if (data[i] != tsData[i]) {
                value = false;
            }
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "" + date + " + " + fractionalPart + "/" + 0x100000000L;
    }

    /**
     * Returns the eight byte array associated to a timestamp.
     *
     * @return DOCUMENT ME!
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the date associated to a timestamp.
     *
     * @return DOCUMENT ME!
     */
    public Date getTime() {
        return date;
    }
}
