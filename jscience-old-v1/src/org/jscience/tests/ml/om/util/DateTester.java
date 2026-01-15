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
