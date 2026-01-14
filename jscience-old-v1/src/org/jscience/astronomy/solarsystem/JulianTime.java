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

/*+
 * $Id: JulianTime.java,v 1.3 2007-10-21 21:07:05 virtualcall Exp $
 *
 * $Log: not supported by cvs2svn $
 * Revision 1.2  2007/10/21 17:43:15  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 *
 * Revision 1.1  2006/09/07 21:14:31  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.6  2006/07/29 22:14:53  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.5  2006/07/13 17:41:23  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.4  2006/07/12 20:35:31  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.3  2006/07/09 21:13:24  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.2  2006/07/05 21:04:53  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.1  2006/07/04 21:46:52  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 1.1  2006/06/27 21:35:39  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS:   If this change addresses one or more issues,
 * CVS:   then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS:   If this change has been taken from another system,
 * CVS:   then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS:   If this code has been contributed to the project by someone else; i.e.,
 * CVS:   they sent us a patch or a set of diffs, then include their name/email
 * CVS:   address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS:   If we are doing pre-commit code reviews and someone else has
 * CVS:   reviewed your changes, include their name(s) here.
 * CVS:   If you have not had it reviewed then delete this line.
 *
 * Revision 4.1  2004/08/09 07:53:06  hme
 * Version 2.1.1.
 *
 * Revision 3.1  2004/07/28 10:47:43  hme
 * Version 2.1.
 *
 * Revision 2.10  2003/09/16 15:42:43  hme
 * Package review.
 *
 * Revision 2.9  2003/09/16 10:33:51  hme
 * *** empty log message ***
 *
 * Revision 2.8  2003/09/15 22:58:41  hme
 * *** empty log message ***
 *
 * Revision 2.7  2003/09/15 22:54:00  hme
 * Fix bug whereby in years -3 to -1 the JD to date conversion was wrong
 * by one day.
 *
 * Revision 2.6  2003/09/15 21:51:17  hme
 * Review, setUniversalTime may raise exception now.
 *
 * Revision 2.5  2003/09/15 14:03:39  hme
 * Make init() independent of SetUTSystem().
 *
 * Revision 2.4  2003/09/15 13:34:41  hme
 * *** empty log message ***
 *
 * Revision 2.3  2003/09/15 12:18:52  hme
 * Change copy() interface to make it overridable.
 *
 * Revision 2.2  2003/09/15 11:02:56  hme
 * Methods that change the time no longer final.
 *
 * Revision 2.1  2003/09/14 09:51:29  hme
 * *** empty log message ***
 *
 * Revision 1.18  2003/06/08 10:39:42  hme
 * *** empty log message ***
 *
 * Revision 1.17  2003/03/09 20:27:51  hme
 * *** empty log message ***
 *
 * Revision 1.16  2002/07/13 19:15:53  hme
 * *** empty log message ***
 *
 * Revision 1.15  2002/07/13 19:12:57  hme
 * Consolidate documentation.
 *
 * Revision 1.13  2002/06/23 15:27:32  hme
 * Move CommandTime() to Telescope subclass.
 *
 * Revision 1.9  2002/06/15 14:51:12  hme
 * Move CommandShow() to Station subclass.
 *
 * Revision 1.8  2002/06/14 21:24:08  hme
 * add() and subtract() methods.
 *
 * Revision 1.6  2002/06/09 20:44:34  hme
 * Translated from C++ after JulianTime class was finalised in the prospective
 * version 2 of Sputnik.
 *
 *-*/

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.MiscellaneousUtils;

import java.io.PrintStream;


/**
 * <p>The <code>JulianTime</code> class stores Julian Day (minus 2450000 d) and
 * allows setting and retrieval of time in various forms.  The stored time is
 * UT rather than TT, TD, ET or TAI.  See the {@link #DeltaT DeltaT} method
 * for the difference between these time scales.
 * <p/>
 * <p>While GST (Greenwich Sidereal Time) is a time proper, LST (local sidereal
 * time) is part of the parametrisation of your location.  It is not dealt
 * with by this class.
 * <p/>
 * <p>The storage of time is as JD minus 2450000 d.  This means UT
 * 2000-01-01-12:00:00 is stored as 1545.0.  The numeric precision should be
 * good enough for 1200 centuries either side of J2000 to represent time to
 * a millisecond.  While for internal consumption within the software we
 * always use JD-2450000, when writing the JD to terminal or file and when
 * reading it from the command line, we use JD itself.
 * <p/>
 * <p>Copyright: &copy; 2002-2003 Horst Meyerdierks.
 * <p/>
 * <p>This programme is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public Licence as
 * published by the Free Software Foundation; either version 2 of
 * the Licence, or (at your option) any later version.
 * <p/>
 * <p>This programme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public Licence for more details.
 * <p/>
 * <p>You should have received a copy of the GNU General Public Licence
 * along with this programme; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * <p/>
 * <p>$Id: JulianTime.java,v 1.3 2007-10-21 21:07:05 virtualcall Exp $
 * <p/>
 * <dl>
 * <dt><strong>2.10:</strong> 2002/09/15 hme
 * <dd>Package review.
 * <dt><strong>2.7:</strong> 2002/09/15 hme
 * <dd>Fix bug whereby in years -3 to -1 the JD to date conversion was wrong
 * by one day.
 * <dt><strong>2.6:</strong> 2002/09/15 hme
 * <dd>Review, setUniversalTime may raise exception now.
 * <dt><strong>2.4:</strong> 2002/09/15 hme
 * <dd>Make init() independent of SetUTSystem().
 * <dt><strong>2.3:</strong> 2002/09/15 hme
 * <dd>Change copy() interface to make it overridable.
 * <dt><strong>2.2:</strong> 2002/09/15 hme
 * <dd>Methods that change the time no longer final.
 * <dt><strong>1.15:</strong> 2002/07/13 hme
 * <dd>Consolidate documentation.
 * <dt><strong>1.14:</strong> 2002/07/02 hme
 * <dd>copy() method.
 * <dt><strong>1.13:</strong> 2002/06/23 hme
 * <dd>Move CommandTime() to Telescope subclass.
 * <dt><strong>1.10:</strong> 2002/06/15 hme
 * <dd>Move CommandShow() to Station subclass.
 * <dt><strong>1.8:</strong> 2002/06/14 hme
 * <dd>add() and subtract() methods.
 * <dt><strong>1.6:</strong> 2002/06/09 hme
 * <dd>Translated from C++ after Times class was finalised in the prospective
 * version 2 of Sputnik.
 * </dl>
 *
 * @author Horst Meyerdierks, c/o Royal Observatory,
 *         Blackford Hill, Edinburgh, EH9 3HJ, Scotland;
 *         &lt; hme &#64 roe.ac.uk &gt;
 * @see org.jscience.astronomy.solarsystem
 */

public class JulianTime {

    /**
     * The Julian Day (UT) minus 2.45 million days.
     * <p/>
     * <p>A zero here is equivalent to 1995-10-09.5.
     */
    private double itsJD;

    /**
     * Set the time from the system clock.
     * <p/>
     * <p>This obtains UTC from the system clock.  This is assumed to be UT and
     * converted to JD, which is the native storage form.
     */
    public JulianTime() {

        /* Get the milliseconds since UT 1970-01-01-00:00:00 from system clock.
 * Convert to days then to JD. */

        itsJD = (double) (System.currentTimeMillis()) / 86400000. + 587.5 - 10000.;

    }

    /**
     * Set the time to the given Julian Day (UT).
     * <p/>
     * <p>This takes the JD (minus 2450000 days) and stores it.  The given Julian
     * Day therefore must be UT and not TT.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for information about the relationship between
     * calendars and Julian Day.
     *
     * @param aJD The Julian Day minus 2.45 million days that is to be stored.
     */
    public JulianTime(double aJD) {

        itsJD = aJD;

    }

    public JulianTime(JulianTime aTime) {

        itsJD = aTime.getJulianDay();

    }

    /**
     * Add to the time.
     * <p/>
     * <p>Adds the given time difference (in days) to the time in this instance
     * of the class.
     *
     * @param aTimeStep The time increment to apply.
     */
    public void add(double aTimeStep) {

        itsJD += aTimeStep;

    }

    /**
     * Return difference between two times.
     * <p/>
     * <p>Returns the time difference (in days) between this instance of the
     * class and the given instance of the class (this minus given).
     *
     * @param aTime The other time, which is to be subtracted from the time stored in
     *              this instance of the class.
     */
    public double subtract(JulianTime aTime) {

        return (itsJD - aTime.getJulianDay());

    }

    /**
     * Return date (UT) as triplet.
     * <p/>
     * <p>This returns the calendar date (UT) as a triplet of year, month (1-12)
     * and day (1-31).  The calendar used is Julian before 1582-10-15, Gregorian
     * after.  All three values are integer, though their storage type is double
     * precision floating point.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for information about the two calendars and
     * their relationship with Julian Day.
     *
     * @return The returned triplet of year, month and day.
     */
    public double[] getDate() {

        double[] aTriplet;
        int len[] = {0, 31, 61, 92, 122, 153, 184, 214, 245, 275, 306, 337};
        /* Time difference from 1st March to the first of
    * the following months */
        int lm[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        /* Lengths of months (Jan ... Dec) */
        int yr, mn, dy;
        double ep, jd0, jd1;

        /* Get the Julian Date of the start of the day.  That is to say, we are
* looking for the previous occurrence of a fraction 0.5. */

        jd0 = Math.floor(itsJD + 0.5) - 0.5;

        /* First guess is midnight on a March 1st (more convenient because directly
* after any possible leap day). Using the Julian epoch as year may be
* wrong, that's why this is a guess. */

        ep = 2000. + (itsJD - 1545.) / 365.25;
        yr = (int) Math.floor(ep);
        mn = 3;
        dy = 1;

        /* Gregorian calendar. */

        if (-150839.5 < itsJD) {

            /* Find JD for guess day's midnight. */

            jd1 = 5643.5 - 10000. + (double) dy;
            jd1 += 365. * (double) (yr - 1985)
                    + Math.floor((double) yr / 4.)
                    - Math.floor((double) yr / 100.)
                    + Math.floor((double) yr / 400.)
                    + (double) len[mn - 3];

            /* Correct the day.  This probably gives a weird day number within March.
   * The date is then brought into conventional ranges. */

            dy += (int) (jd0 - jd1);

            /* Adjust for month uncommon. */

            while (mn < 1) {
                yr--;
                mn += 12;
            }
            while (mn > 12) {
                yr++;
                mn -= 12;
            }

            /* Adjust for day too small
    *   Month may be uncommon again, but not more than one month.
    *   Take care of leap years. */

            while (dy < 1) {
                mn--;
                if (mn < 1) {
                    yr--;
                    mn += 12;
                }
                dy += lm[mn - 1];
                if (mn == 2 && 0 == (yr % 4) && (0 != (yr % 100) || 0 == (yr % 400))) dy++;
            }

            /* Adjust for day too big.
  *   Month may be uncommon again, but not more than one month.
  *   Take care of leap years.
  *     If the result is to be 29th Feb. of a leap year, now we are
  *     pretty close at 0th March. */

            while (dy > lm[mn - 1]) {
                dy -= lm[mn - 1];
                mn++;
                if (mn > 12) {
                    yr++;
                    mn -= 12;
                }
                if (mn == 3) {
                    if (0 == (yr % 4) && (0 != (yr % 100) || 0 == (yr % 400))) dy--;
                    if (0 == dy) {
                        dy = 29;
                        mn = 2;
                        break;
                    }
                }
            }
        }

        /* Julian calendar. */

        else {

            /* Find JD for guess day's midnight. */

            jd1 = 5641.5 - 10000. + (double) dy;
            jd1 += 365. * (double) (yr - 1985)
                    + Math.floor((double) yr / 4.)
                    + (double) len[mn - 3];

            /* Correct the day.  This probably gives a weird day number within March.
   * The date is then brought into conventional ranges. */

            dy += (int) (jd0 - jd1);

            /* Adjust for month uncommon. */

            while (mn < 1) {
                yr--;
                mn += 12;
            }
            while (mn > 12) {
                yr++;
                mn -= 12;
            }

            /* Adjust for day too small
    *   Month may be uncommon again, but not more than one month.
    *   Take care of leap years. */

            while (dy < 1) {
                mn--;
                if (mn < 1) {
                    yr--;
                    mn += 12;
                }
                dy += lm[mn - 1];
                if (mn == 2 && 0 == (yr % 4)) dy++;
            }

            /* Adjust for day too big.
  *   Month may be uncommon again, but not more than one month.
  *   Take care of leap years.
  *     If the result is to be 29th Feb. of a leap year, now we are
  *     pretty close at 0th March. */

            while (dy > lm[mn - 1]) {
                dy -= lm[mn - 1];
                mn++;
                if (mn > 12) {
                    yr++;
                    mn -= 12;
                }
                if (mn == 3) {
                    if (0 == (yr % 4)) dy--;
                    if (0 == dy) {
                        dy = 29;
                        mn = 2;
                        break;
                    }
                }
            }
        }

        aTriplet = new double[3];
        aTriplet[0] = (double) yr;
        aTriplet[1] = (double) mn;
        aTriplet[2] = (double) dy;

        return aTriplet;
    }

    /**
     * Return Greenwich Sidereal Time.
     * <p/>
     * <p>This returns the Greenwich Sidereal Time (sidereal time since previous
     * culmination of the mean equinox at the longitude of Greenwich) in hours.
     * The value is calculated based on the expression given by
     * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.B6,
     * but where only the fraction of a full rotation is carried forward in
     * each step.
     * <p/>
     * <p>u = JD - JD(midnight)
     * <p>t = [JD(midnight) - 2451545] / 36525 d
     * <p>GST = 6.69737456 h + 100 d * t + 2.1390378E-3 d * t
     * <br>+ 1.07759E-6 d * t<sup>2</sup>
     * - 7.1759E-11 d * t<sup>3</sup>
     * + 1.00273791 * u
     */
    public double getGreenwichSideralTime() {

        double hr, ts;
        double t1, t2, t3, t4;

        /* t1 is the previous midnight in Julian centuries from J2000.
* t2 adds up the contributions to sidereal time in units of hours.
*    In doing the addition, only fractional days are taken. It does not
*    matter whether these fractions are positive or negative, since
*    the end result is normalised to 0 ... 24 hours anyway.
* t3 is a term for addition to t2, but in units of days.
* The fourth addend is 100 t1. That is to say per century there are
*    100 sideral days more than there are synodic days.
* The third addend is 2e-3 t1 and the first order term of the sideral
*    time as a Taylor series in t1.
* The second addend is 1e-6 t1 t1 and the second order term.
* The first addend is -7e-11 and the third order term.
* The fifth to seventh addends are the zero order term,
*    1.003 times the fraction of day and the geographic longitude. */

        hr = getUniversalTime();
        t1 = (Math.floor(itsJD + 0.5) - 0.5 - 1545.) / 36525.;
        t3 = -7.1759e-11 * t1 * t1 * t1;
        t2 = 24. * (t3 - Math.floor(t3));
        t3 = 1.07759e-6 * t1 * t1;
        t2 += 24. * (t3 - Math.floor(t3));
        t3 = 2.1390378e-3 * t1;
        t2 += 24. * (t3 - Math.floor(t3));
        t3 = 100. * t1;
        t2 += 24. * (t3 - Math.floor(t3));
        ts = 6.69737456 + t2 + 1.00273791 * hr;

        while (ts < 0.) ts += 24.;
        while (ts >= 24.) ts -= 24.;

        return ts;
    }

    /**
     * Return Greenwich Sidereal Time as triplet.
     * <p/>
     * <p>This returns the Greenwich Sidereal Time as a
     * triplet of hours (0-23), minutes (0-59) and seconds (0-60.0).
     * <p/>
     * <p>See {@link #getGreenwichSideralTime getGreenwichSiderealTime} for more information on GST.
     *
     * @return The returned triplet of hours, minutes and seconds.
     */
    public double[] getGreenwichSideralTimehms() {

        double theTime;
        double[] aTriplet;

        theTime = getGreenwichSideralTime();
        aTriplet = MiscellaneousUtils.degreesTodms(theTime);

        return aTriplet;
    }

    /**
     * Return the Julian Day (UT).
     * <p/>
     * <p>This returns the Julian Day (JD, UT) minus 2450000 days.  The value is
     * of course in days.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for information about the relationship between
     * the calendars and Julian Day.
     */
    public double getJulianDay() {

        return itsJD;

    }

    /**
     * Return the Julian Ephemeris Day (TT).
     * <p/>
     * <p>This returns the Julian Ephemeris Day (JDE, TT) minus 2450000 days.
     * The value is of course in days.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for information about the relationship between
     * the calendars and Julian Day.  See {@link #DeltaT DeltaT} for information
     * about the difference between Universal Time (UT) and Terrestrial Time
     * (TT), Temps Dynamique (TD) and Ephemeris Time (ET).
     */
    public double getJulianEphemerisDay() {

        return (itsJD + DeltaT());

    }

    /**
     * Return the Julian Epoch.
     * <p/>
     * <p>This returns the Julian Epoch in years.  This is always TT and not UT.
     * The unit is Julian years (365.25&nbsp;d) and the fix point is such that
     * J2000.0 is TT&nbsp;2000-01-01.5 or JDE&nbsp;2451545.0.
     * <p/>
     * <p>Ep = 2000 + (JDE - 2451545) / 365.25
     * <p/>
     * <p>Note that the difference between TT and UT is only on the order of
     * 0.0014&nbsp;d or 0.000004&nbsp;year.  But the epoch is normally used in
     * the context of the Solar System or stellar proper motion, not where we
     * care about the rotation state of the Earth.  So to base it on TT is more
     * natural.  Standard UT epochs are
     * (USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.B4):
     * <p/>
     * <p><table border>
     * <tr align="center"><td>1900.0</td>
     * <td>UT 1900-01-00.5</td><td>JD 2415020.000</td></tr>
     * <tr align="center"><td>B1950.0</td>
     * <td>UT 1950-01-00.923</td><td>JD 2433282.423</td></tr>
     * <tr align="center"><td>J2000.0</td>
     * <td>UT 2000-01-01.5</td><td>JD 2451545.0</td></tr>
     * <tr align="center"><td>&nbsp;</td>
     * <td>UT 1970-01-01.0</td><td>JD 2440587.5</td></tr>
     * </table>
     */
    public double getJulianEpoch() {

        return ((itsJD - 1545. + DeltaT()) / 365.25 + 2000.);

    }

    /**
     * Return Terrestrial Time.
     * <p/>
     * <p>This returns the Terrestrial Time (the time since previous midnight TT)
     * in hours.
     * <p/>
     * <p>See {@link #DeltaT DeltaT} for more information about Terrestrial Time
     * (TT), Temps Dynamique (TD) and Ephemeris Time (ET), and their difference
     * to Universal Time (UT).
     */
    public double getTerrestrialTime() {

        double theTime;

        theTime = itsJD + DeltaT() + 0.5;
        theTime = theTime - Math.floor(theTime);
        while (theTime < 0.) theTime++;
        while (theTime >= 1.) theTime--;
        theTime *= 24.;

        return theTime;
    }

    /**
     * Return date (TT) as triplet.
     * <p/>
     * <p>This returns the calendar date (TT) as a triplet of year, month (1-12)
     * and day (1-31).  The calendar used is Julian before 1582-10-15, Gregorian
     * after.  All three values are integer, though their storage type is double
     * precision floating point.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for information about the two calendars and
     * their relationship with Julian Day.
     *
     * @return The returned triplet of year, month and day.
     */
    public double[] getTerrestrialTimeDate() {

        double[] aTriplet;
        int len[] = {0, 31, 61, 92, 122, 153, 184, 214, 245, 275, 306, 337};
        /* Time difference from 1st March to the first of
    * the following months */
        int lm[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        /* Lengths of months (Jan ... Dec) */
        int yr, mn, dy;
        double ep, jd0, jd1;
        double theDelta;

        /* Get TT - UT. */

        theDelta = DeltaT();

        /* Get the Julian Date of the start of the day.  That is to say, we are
* looking for the previous occurrence of a fraction 0.5. */

        jd0 = Math.floor(itsJD + theDelta + 0.5) - 0.5;

        /* First guess is midnight on a March 1st (more convenient because directly
* after any possible leap day). Using the Julian epoch as year may be
* wrong, that's why this is a guess. */

        ep = 2000. + (itsJD + theDelta - 1545.) / 365.25;
        yr = (int) Math.floor(ep);
        mn = 3;
        dy = 1;

        /* Gregorian calendar. */

        if (-150839.5 < itsJD + theDelta) {

            /* Find JD for guess day's midnight. */

            jd1 = 5643.5 - 10000. + (double) dy;
            jd1 += 365. * (double) (yr - 1985)
                    + Math.floor((double) yr / 4.)
                    - Math.floor((double) yr / 100.)
                    + Math.floor((double) yr / 400.)
                    + (double) len[mn - 3];

            /* Correct the day.  This probably gives a weird day number within March.
   * The date is then brought into conventional ranges. */

            dy += (int) (jd0 - jd1);

            /* Adjust for month uncommon. */

            while (mn < 1) {
                yr--;
                mn += 12;
            }
            while (mn > 12) {
                yr++;
                mn -= 12;
            }

            /* Adjust for day too small
    *   Month may be uncommon again, but not more than one month.
    *   Take care of leap years. */

            while (dy < 1) {
                mn--;
                if (mn < 1) {
                    yr--;
                    mn += 12;
                }
                dy += lm[mn - 1];
                if (mn == 2 && 0 == (yr % 4) && (0 != (yr % 100) || 0 == (yr % 400))) dy++;
            }

            /* Adjust for day too big.
  *   Month may be uncommon again, but not more than one month.
  *   Take care of leap years.
  *     If the result is to be 29th Feb. of a leap year, now we are
  *     pretty close at 0th March. */

            while (dy > lm[mn - 1]) {
                dy -= lm[mn - 1];
                mn++;
                if (mn > 12) {
                    yr++;
                    mn -= 12;
                }
                if (mn == 3) {
                    if (0 == (yr % 4) && (0 != (yr % 100) || 0 == (yr % 400))) dy--;
                    if (0 == dy) {
                        dy = 29;
                        mn = 2;
                        break;
                    }
                }
            }
        }

        /* Julian calendar. */

        else {

            /* Find JD for guess day's midnight. */

            jd1 = 5641.5 - 10000. + (double) dy;
            jd1 += 365. * (double) (yr - 1985)
                    + Math.floor((double) yr / 4.)
                    + (double) len[mn - 3];

            /* Correct the day.  This probably gives a weird day number within March.
   * The date is then brought into conventional ranges. */

            dy += (int) (jd0 - jd1);

            /* Adjust for month uncommon. */

            while (mn < 1) {
                yr--;
                mn += 12;
            }
            while (mn > 12) {
                yr++;
                mn -= 12;
            }

            /* Adjust for day too small
    *   Month may be uncommon again, but not more than one month.
    *   Take care of leap years. */

            while (dy < 1) {
                mn--;
                if (mn < 1) {
                    yr--;
                    mn += 12;
                }
                dy += lm[mn - 1];
                if (mn == 2 && 0 == (yr % 4)) dy++;
            }

            /* Adjust for day too big.
  *   Month may be uncommon again, but not more than one month.
  *   Take care of leap years.
  *     If the result is to be 29th Feb. of a leap year, now we are
  *     pretty close at 0th March. */

            while (dy > lm[mn - 1]) {
                dy -= lm[mn - 1];
                mn++;
                if (mn > 12) {
                    yr++;
                    mn -= 12;
                }
                if (mn == 3) {
                    if (0 == (yr % 4)) dy--;
                    if (0 == dy) {
                        dy = 29;
                        mn = 2;
                        break;
                    }
                }
            }
        }

        aTriplet = new double[3];
        aTriplet[0] = (double) yr;
        aTriplet[1] = (double) mn;
        aTriplet[2] = (double) dy;

        return aTriplet;
    }

    /**
     * Return Terrestrial Time as triplet.
     * <p/>
     * <p>This returns the Terrestrial Time (the time since previous midnight TT)
     * as a triplet of hours (0-23), minutes (0-59) and seconds (0-60.0).
     * <p/>
     * <p>See {@link #DeltaT DeltaT} for more information about Terrestrial Time
     * (TT), Temps Dynamique (TD) and Ephemeris Time (ET), and their difference
     * to Universal Time (UT).
     *
     * @return The returned triplet of hours, minutes and seconds.
     */
    public double[] getTerrestrialTimehms() {

        double theTime;
        double[] aTriplet;
        theTime = getTerrestrialTime();
        aTriplet = MiscellaneousUtils.degreesTodms(theTime);

        return aTriplet;

    }

    /**
     * Return Universal Time.
     * <p/>
     * <p>This returns the Universal Time (the time since previous midnight UT)
     * in hours.
     */
    public double getUniversalTime() {

        double theTime;

        theTime = itsJD + 0.5;
        theTime = theTime - Math.floor(theTime);
        while (theTime < 0.) theTime++;
        while (theTime >= 1.) theTime--;
        theTime *= 24.;

        return theTime;
    }

    /**
     * Return Universal Time as triplet.
     * <p/>
     * <p>This returns the Universal Time (the time since previous midnight UT)
     * as a triplet of hours (0-23), minutes (0-59) and seconds (0-60.0).
     *
     * @return The returned triplet of hours, minutes and seconds.
     */
    public double[] getUniversalTimehms() {

        double theTime;
        double[] aTriplet;

        theTime = getUniversalTime();
        aTriplet = MiscellaneousUtils.degreesTodms(theTime);
        return aTriplet;

    }

    /**
     * Set the time to the given date and time (TT).
     * <p/>
     * <p>This takes a time (TT) in the form of year, month, day, hour, minute
     * and second, converts it to JDE and then to JD (UT), which is the native
     * storage form.  Since TT - UT is defined as a function of UT and not of TT,
     * this is not entirely precise.  A second order approximation is used in the
     * conversion.
     * <p/>
     * <p>This method uses {@link #setUniversalTime setUniversalTime}, which may run into trouble to
     * to with the transition between Julian and Gregorian calendars.
     * <p/>
     * <p>See {@link #setUniversalTime setUniversalTime} for the relationship between the calendars and
     * Julian Day.  See {@link #DeltaT DeltaT} for the difference between TT and
     * UT.
     *
     * @param aYear   The calendar year.
     * @param aMonth  The calendar month.
     * @param aDay    The calendar day.
     * @param anHour  The TT hour.
     * @param aMinute The TT minute.
     * @param aSecond The TT second.
     */
    public void setTerrestrialTime(int aYear, int aMonth, int aDay,
                                   double anHour, double aMinute, double aSecond)
            throws Exception {

        double theDelta;

        /* First assume the given UT is TT and calculate the DeltaT. */

        setUniversalTime(aYear, aMonth, aDay, anHour, aMinute, aSecond);
        theDelta = DeltaT();

        /* Using this, correct the time roughly to TT and calculate DeltaT
      * again. */

        itsJD -= theDelta;
        theDelta = DeltaT();

        /* Accept this as the correct DeltaT, and set the internal value
      * accordingly. */

        setUniversalTime(aYear, aMonth, aDay, anHour, aMinute, aSecond);
        itsJD -= theDelta;

    }

    /**
     * Set the time to the given date and time (UT).
     * <p/>
     * <p>This takes a time (UT) in the form of year, month, day, hour, minute
     * and second and converts it to JD, which is the native storage form.  There
     * is the possibility of failure, if the time is close to the Gregorian
     * calendar reform (1582-10-15) and if the day of the month is given out of
     * range.  It is then possible that the wrong calendar is assumed to be in
     * force.  The routine will spot this after the fact; it will throw an
     * exception in this event and will set the time to 1582-10-15-00:00:00.
     * <p/>
     * <p>A 1st of March in the Gregorian and Julian calendars convert to Julian
     * Day as
     * <p/>
     * <p>JD(G Y-03-01) = 2445643.5 + 365 (Y - 1985) + [Y/4] - [Y/100] + [Y/400]
     * <p>JD(J Y-03-01) = 2445641.5 + 365 (Y - 1985) + [Y/4]
     * <p/>
     * <p>The square brackets indicate the floor() function.
     * <p/>
     * <p>The calendar we use is Gregorian, which is an imprecise statement
     * (Jean Meeus, 1991, <I>Astronomical Algorithms</I>, Willmann-Bell, Richmond VA, p.59ff).
     * Before 1582-10-04 the calendar coincides roughly with the Julian calendar,
     * i.e. there is a leap day in every year that is divisible by 4, including
     * the year 4, 0, -4, -8 etc.  The Julian calendar was introduced only with
     * the year -44, was miscalculated until -7 and corrected only by the year +8
     * (Istv&aacute;n Hahn, 1989, <I>Sonnentage - Mondjahre, &Uuml;ber Kalendersysteme und Zeitrechnung</I>, Urania, Leipzig, Jena, Berlin, p. 55).
     * Here we ignore such details, and this seems to be confirmed by a test that
     * J&nbsp;-4712-01-01-12:00:00 is indeed calculated as JD&nbsp;0.0.
     * 1582-10-04-23:59:60 is followed immediately by 1582-10-15-00:00:00
     * (JD&nbsp;2299160.5) and from then on 1700, 1800 and 1900 have not been
     * leap years (1600 and 2000 have been leap years).  In fact, the change to
     * the new calendar was made at different times in different countries,
     * because it was instigated by the Catholic church.  The Protestant
     * L&auml;nder in Germany waited until 1700-02-19, England converted in
     * 1753, Russia in January 1918.  Red Hat GNU/Linux shows 1752-09-02 being
     * followed by 1752-09-14.  Apparently this is based on BSD Unix and we
     * have to assume that this is a US convention.  The difference between the
     * two calendars in 1582 was 10 days and in 1900 increased to 13 days.
     * <p/>
     * <p>Before Christ there is a difference of one between the historical and
     * the astronomical year count.  Historians have the year 1&nbsp;AD preceded
     * by the year 1&nbsp;BC, which to astronomers is the year 0.  So, for
     * example, the year -4 is also called 5&nbsp;BC.
     * <p/>
     * <p>Some important UT times are
     * (USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.B4):
     * <p/>
     * <p><table border>
     * <tr align="center"><td>1900.0</td>
     * <td>UT 1900-01-00.5</td><td>JD 2415020.000</td></tr>
     * <tr align="center"><td>B1950.0</td>
     * <td>UT 1950-01-00.923</td><td>JD 2433282.423</td></tr>
     * <tr align="center"><td>J2000.0</td>
     * <td>UT 2000-01-01.5</td><td>JD 2451545.0</td></tr>
     * <tr align="center"><td>&nbsp;</td>
     * <td>UT 1970-01-01.0</td><td>JD 2440587.5</td></tr>
     * <tr align="center"><td>&nbsp;</td>
     * <td>UT 1582-10-15.0</td><td>JD 2299160.5</td></tr>
     * <tr align="center"><td>&nbsp;</td>
     * <td>UT -4712-01-01.5</td><td>JD 0.0</td></tr>
     * </table>
     *
     * @param aYear   The calendar year.
     * @param aMonth  The calendar month.
     * @param aDay    The calendar day.
     * @param aHour   The UT hour.
     * @param aMinute The UT minute.
     * @param aSecond The UT second.
     */
    public void setUniversalTime(int aYear, int aMonth, int aDay,
                                 double aHour, double aMinute, double aSecond)
            throws Exception {

        int len[] = {0, 31, 61, 92, 122, 153, 184, 214, 245, 275, 306, 337};
        /* Time difference from 1st March to the first of
    * the following months */
        int theYear;
        int theMonth;
        boolean useGreg;

        theYear = aYear;
        theMonth = aMonth;

        /* Combine seconds, minutes and hours. */

        itsJD = aSecond;
        itsJD /= 60.;
        itsJD += aMinute;
        itsJD /= 60.;
        itsJD += aHour;
        itsJD /= 24.;

        /* Adjust date for month uncommon.
* Map Jan. and Feb. to month no. 13 and 14 resp. of previous year. */

        while (theMonth < 1) {
            theYear--;
            theMonth += 12;
        }
        while (theMonth > 12) {
            theYear++;
            theMonth -= 12;
        }
        if (theMonth < 3) {
            theYear--;
            theMonth += 12;
        }

        /* Decide which calendar.  If some joker gives us an out-of-range day,
* which is normally permitted, that can defeat this decision process.
* Once we have the JD we can check for this. */

        if (1582 > theYear) {
            useGreg = false;
        } else if (1582 < theYear) {
            useGreg = true;
        } else if (10 > theMonth) {
            useGreg = false;
        } else if (10 < theMonth) {
            useGreg = true;
        } else if (5 > aDay) {
            useGreg = false;
        } else if (14 < aDay) {
            useGreg = true;
        } else {
            useGreg = false;
        }

        /* Add the date's contribution to the JD. */

        if (useGreg) {
            itsJD += 5643.5 - 10000. + (double) aDay;
            itsJD += 365. * (double) (theYear - 1985)
                    + Math.floor((double) theYear / 4.)
                    - Math.floor((double) theYear / 100.)
                    + Math.floor((double) theYear / 400.)
                    + (double) len[theMonth - 3];
        } else {
            itsJD += 5641.5 - 10000. + (double) aDay;
            itsJD += 365. * (double) (theYear - 1985)
                    + Math.floor((double) theYear / 4.)
                    + (double) len[theMonth - 3];
        }

        /* Check that we used the right calendar.  The constant here corresponds
* to 1582-10-15.0, the first valid date of the Gregorian calendar. */

        if (useGreg && -150839.5 > itsJD) {
            itsJD = -150839.5;
            throw new Exception("ambiguous date");
        }
        if (!useGreg && -150839.5 < itsJD) {
            itsJD = -150839.5;
            throw new Exception("ambiguous date");
        }

    }

    /**
     * Display the time in various representations.
     * <p/>
     * <p>This writes information about the currently stored time to the given
     * open file.  The format is
     * <p/>
     * <pre>
     * UT: 1987-04-10-19:21:00.0 (JD  2446896.306250)
     * TT: 1987-04-10-19:22:27.1 (JDE 2446896.307258)
     * Ep: 1987.272572919
     * GST 08:34:57.1 = 128.737874 deg
     * </pre>
     *
     * @param aFile The output stream to which to write the information.
     */
    public void showToFile(PrintStream aFile) {

        double theDate[];
        double theTime[];
        double theGST[];
        double theJD, theEpoch;

        theDate = getDate();
        theTime = getUniversalTimehms();
        theJD = getJulianDay();
        aFile.print("  UT: ");
        MiscellaneousUtils.writeExtendedDate(aFile, theDate[0], theDate[1], theDate[2],
                theTime[0], theTime[1], theTime[2]);
        aFile.print(" (JD  ");
        MiscellaneousUtils.writeFloat(aFile, 14, 6, 2450000. + theJD);
        aFile.println(")");

        theDate = getTerrestrialTimeDate();
        theTime = getTerrestrialTimehms();
        theJD = getJulianEphemerisDay();
        theEpoch = getJulianEpoch();
        aFile.print("  TT: ");
        MiscellaneousUtils.writeExtendedDate(aFile, theDate[0], theDate[1], theDate[2],
                theTime[0], theTime[1], theTime[2]);
        aFile.print(" (JDE ");
        MiscellaneousUtils.writeFloat(aFile, 14, 6, 2450000. + theJD);
        aFile.println(")");
        aFile.print("  Ep: ");
        MiscellaneousUtils.writeFloat(aFile, 14, 9, theEpoch);
        aFile.println();

        theGST = getGreenwichSideralTimehms();
        aFile.print("             GST ");
        MiscellaneousUtils.writeExtendedTime(aFile, theGST[0], theGST[1], theGST[2]);
        aFile.print(" = ");
        MiscellaneousUtils.writeFloat(aFile, 10, 6,
                (theGST[0] + (theGST[1] + theGST[2] / 60.) / 60.) * 15.);
        aFile.println(" deg");

    }

    /**
     * Return TT minus UT.
     * <p/>
     * <p>Up to 1983 Ephemeris Time (ET) was used in place of TT, between
     * 1984 and 2000 Temps Dynamique Terrestrial (TDT) was used in place of TT.
     * The three time scales, while defined differently, form a continuous time
     * scale for most purposes.  TT has a fixed offset from TAI (Temps Atomique
     * International).
     * <p/>
     * <p>This method returns the difference TT - UT in days.  Usually this
     * would be looked up in a table published after the fact.  Here we use
     * polynomial fits for the distant past, for the future and also for the
     * time where the table exists.  Except for 1987 to 2015, the expressions
     * are taken from
     * Jean Meeus, 1991, <I>Astronomical Algorithms</I>, Willmann-Bell, Richmond VA, p.73f.
     * For the present (1987 to 2015 we use our own graphical linear fit to the
     * data 1987 to 2001 from
     * USNO/RAL, 2001, <I>Astronomical Almanach 2003</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.K9:
     * <p/>
     * <p>t = Ep - 2002
     * <p>DeltaT/s = 9.2 * t / 15 + 65
     * <p/>
     * <p>Close to the present (1900 to 1987) we use Schmadl and Zech:
     * <p/>
     * <p>t = (Ep - 1900) / 100
     * <p>DeltaT/d = -0.000020      + 0.000297 * t
     * + 0.025184 * t<sup>2</sup> - 0.181133 * t<sup>3</sup><BR>
     * + 0.553040 * t<sup>4</sup> - 0.861938 * t<sup>5</sup>
     * + 0.677066 * t<sup>6</sup> - 0.212591 * t<sup>7</sup>
     * <p/>
     * <p>This work dates from 1988 and the equation is supposed to be valid only
     * to 1987, but we extend its use into the near future.  For the 19th
     * century we use Schmadl and Zech:
     * <p/>
     * <p>t = (Ep - 1900) / 100
     * <p>DeltaT/d = -0.000009      +  0.003844 * t
     * +  0.083563 * t<sup>2</sup> +  0.865736 * t<sup>3</sup><BR>
     * +  4.867575 * t<sup>4</sup> + 15.845535 * t<sup>5</sup>
     * + 31.332267 * t<sup>6</sup> + 38.291999 * t<sup>7</sup><BR>
     * + 28.316289 * t<sup>8</sup> + 11.636204 * t<sup>9</sup>
     * +  2.043794 * t<sup>10</sup>
     * <p/>
     * <p>Stephenson and Houlden are credited with the equations for times before
     * 1600.  First for the period 948 to 1600:
     * <p/>
     * <p>t = (Ep - 1850) / 100
     * <p>DeltaT/s = 22.5 * t<sup>2</sup>
     * <p/>
     * <p>and before 948:
     * <p/>
     * <p>t = (Ep - 948) / 100
     * <p>DeltaT/s = 1830 - 405 * t + 46.5 * t<sup>2</sup>
     * <p/>
     * <p>This leaves no equation for times between 1600 and 1800 and beyond
     * 2015.  For such times we use the equation of Morrison and Stephenson:
     * <p/>
     * <p>t = Ep - 1810
     * <p>DeltaT/s = -15 + 0.00325 * t<sup>2</sup>
     */
    private final double DeltaT() {

        double theEpoch; /* Julian Epoch */
        double t; /* Time parameter used in the equations. */
        double D; /* The return value. */

        theEpoch = 2000. + (itsJD - 1545.) / 365.25;

        /* For 1987 to 2015 we use a graphical linear fit to the annual tabulation
* from USNO/RAL, 2001, Astronomical Almanach 2003, p.K9.  We use this up
* to 2015 about as far into the future as it is based on data in the past.
* The result is slightly higher than the predictions from that source. */

        if (1987 <= theEpoch && 2015 >= theEpoch) {
            t = (theEpoch - 2002.);
            D = 9.2 * t / 15. + 65.;
            D /= 86400.;
        }

        /* For 1900 to 1987 we use the equation from Schmadl and Zech as quoted in
     * Meeus, 1991, Astronomical Algorithms, p.74.  This is precise within
     * 1.0 second. */

        else if (1900 <= theEpoch && 1987 > theEpoch) {
            t = (theEpoch - 1900.) / 100.;
            D = -0.212591 * t * t * t * t * t * t * t
                    + 0.677066 * t * t * t * t * t * t
                    - 0.861938 * t * t * t * t * t
                    + 0.553040 * t * t * t * t
                    - 0.181133 * t * t * t
                    + 0.025184 * t * t
                    + 0.000297 * t
                    - 0.000020;
        }

        /* For 1800 to 1900 we use the equation from Schmadl and Zech as quoted in
      * Meeus, 1991, Astronomical Algorithms, p.74.  This is precise within 1.0
      * second. */

        else if (1800 <= theEpoch && 1900 > theEpoch) {
            t = (theEpoch - 1900.) / 100.;
            D = 2.043794 * t * t * t * t * t * t * t * t * t * t
                    + 11.636204 * t * t * t * t * t * t * t * t * t
                    + 28.316289 * t * t * t * t * t * t * t * t
                    + 38.291999 * t * t * t * t * t * t * t
                    + 31.332267 * t * t * t * t * t * t
                    + 15.845535 * t * t * t * t * t
                    + 4.867575 * t * t * t * t
                    + 0.865736 * t * t * t
                    + 0.083563 * t * t
                    + 0.003844 * t
                    - 0.000009;
        }

        /* For 948 to 1600 we use the equation from Stephenson and Houlden as
* quoted in Meeus, 1991, Astronomical Algorithms, p.73. */

        else if (948 <= theEpoch && 1600 >= theEpoch) {
            t = (theEpoch - 1850.) / 100.;
            D = 22.5 * t * t;
            D /= 86400.;
        }

        /* Before 948 we use the equation from Stephenson and Houlden as quoted
 * in Meeus, 1991, Astronomical Algorithms, p.73. */

        else if (948 > theEpoch) {
            t = (theEpoch - 948.) / 100.;
            D = 46.5 * t * t - 405. * t + 1830.;
            D /= 86400.;
        }

        /* Else (between 1600 and 1800 and after 2010) we use the equation from
    * Morrison and Stephenson, quoted as eqation 9.1 in Meeus, 1991,
    * Astronomical Algorithms, p.73. */

        else {
            t = theEpoch - 1810.;
            D = 0.00325 * t * t - 15.;
            D /= 86400.;
        }

        return D;
    }

}
