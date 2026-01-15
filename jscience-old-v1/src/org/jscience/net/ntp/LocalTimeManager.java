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
 * This class implements TimeManager.setTime(Date) through a native method
 * under Windows 95 (this will presumably also work under NT, if you have the
 * correct permissions).
 *
 * @author Michel Van den Bergh
 * @version 1.0
 */
public class LocalTimeManager extends TimeManager {
    /** DOCUMENT ME! */
    static private TimeZone UTC = new SimpleTimeZone(0, "UTC");

    static {
        System.loadLibrary("settime");
    }

    /**
     * Sets the local time to a given date. For this method to work
     * settime.dll should be in the library loadpath (for example in
     * "c:\windows")
     *
     * @param d DOCUMENT ME!
     */
    public void setTime(Date d) {
        Calendar c = new GregorianCalendar(UTC);
        c.setTime(d);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int millis = c.get(Calendar.MILLISECOND);
        nativeSetTime(year, month, day, hour, minute, second, millis);
    }

    /**
     * DOCUMENT ME!
     *
     * @param year DOCUMENT ME!
     * @param month DOCUMENT ME!
     * @param day DOCUMENT ME!
     * @param hour DOCUMENT ME!
     * @param minute DOCUMENT ME!
     * @param second DOCUMENT ME!
     * @param millis DOCUMENT ME!
     */
    private native void nativeSetTime(int year, int month, int day, int hour,
        int minute, int second, int millis);
}
