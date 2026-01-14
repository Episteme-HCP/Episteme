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

/*
 * Created on 02.07.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.jscience.tests.ml.om.util;

import org.jscience.ml.om.util.DateConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author dirk
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateTester {

    public static void main(String[] args) {

        Calendar cal = GregorianCalendar.getInstance();
        System.out.println("Date: " + cal + "\t(Timezone = " + cal.getTimeZone() + ")");
        double julian = DateConverter.toJulianDate(cal);
        System.out.println("Julian Date = " + julian);
        Calendar convertedJulianDate = DateConverter.toGregorianDate(julian, TimeZone.getDefault());
        System.out.println("Converted Julian Date: " + convertedJulianDate + "\t(Timezone = " + convertedJulianDate.getTimeZone() + ")");
        String iso8601 = DateConverter.toISO8601(convertedJulianDate);
        System.out.println("ISO8601 Date = " + iso8601);
        Calendar converted8601Date = DateConverter.toDate(iso8601);
        System.out.println("Converted ISO8601 Date: " + converted8601Date + "\t(Timezone = " + converted8601Date.getTimeZone() + ")");

    }
}
