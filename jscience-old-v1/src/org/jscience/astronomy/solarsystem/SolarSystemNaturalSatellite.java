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
 * $Id: SolarSystemNaturalSatellite.java,v 1.3 2007-10-21 21:07:05 virtualcall Exp $
 *
 * $Log: not supported by cvs2svn $
 * Revision 1.2  2007/10/21 17:43:16  virtualcall
 * Issue number:
 * Obtained from:
 * Submitted by:
 * Reviewed by:
 *
 * Revision 1.1  2006/09/07 21:14:33  virtualcall
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
 * Revision 1.4  2006/08/13 17:14:00  virtualcall
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
 * Revision 1.3  2006/07/30 20:57:14  virtualcall
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
 * Revision 1.2  2006/07/29 22:14:53  virtualcall
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
 * Revision 1.1  2006/07/13 22:20:03  virtualcall
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
 * Revision 1.6  2006/07/12 20:35:32  virtualcall
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
 * Revision 1.5  2006/07/09 21:13:25  virtualcall
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
 * Revision 1.4  2006/07/07 22:32:57  virtualcall
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
 * Revision 1.3  2006/07/05 21:04:54  virtualcall
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
 * Revision 1.2  2006/07/04 21:46:48  virtualcall
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
 * Revision 1.1  2006/06/27 21:35:38  virtualcall
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
 * Revision 3.2  2004/08/03 07:55:52  hme
 * Fix bug in showToFile whereby the parallactic angle was calculated
 * with the NCP not above the N horizon but above the S horizon.  This bug
 * probably propagated from Sputnik 1.9.
 *
 * Revision 3.1  2004/07/28 10:47:43  hme
 * Version 2.1.
 *
 * Revision 2.18  2003/09/18 14:07:56  hme
 * Add protected NaiveRiseSet() method to which the semi day arc
 * calculation is delegated.  While NextRiseSet is overriden by subclasses,
 * NaiveRiseSet is inherited by them.
 *
 * Revision 2.11  2003/09/17 16:00:36  hme
 * Add NextRiseSet() method.
 *
 * Revision 2.7  2003/09/16 18:01:11  hme
 * Package review.
 *
 * Revision 2.6  2003/09/15 23:08:01  hme
 * Avoid the use of JulianTime.setTerrestrialTime for the 1900 epoch, as it would imply
 * propagating an exception upwards that will never occur.
 *
 * Revision 2.4  2003/09/15 17:10:08  hme
 * Changed Station method name GetX0Z.
 *
 * Revision 2.3  2003/09/15 15:27:25  hme
 * Use Telescope instead of Station plus Sun in many interfaces.
 *
 * Revision 2.2  2003/09/15 13:36:15  hme
 * Add copy() method.
 *
 * Revision 1.21  2003/06/14 09:07:42  hme
 * Show the station before the object.
 *
 * Revision 1.20  2003/04/06 21:38:05  hme
 * Change distance output to km for small distances.
 *
 * Revision 1.19  2003/04/06 18:22:52  hme
 * Change output of azimuth to range 0 to 360 deg.
 *
 * Revision 1.15  2002/07/13 16:01:10  hme
 * Consolidate documentation.
 *
 * Revision 1.10  2002/07/02 17:46:36  hme
 * Change showToFile() to use a Sun as well as a Station, and to calculate
 * and display heliocentric, LSR and GSR radial velocities.
 *
 * Revision 1.9  2002/06/23 17:40:45  hme
 * Change showToFile to display A and HA as +-180 deg.
 *
 * Revision 1.7  2002/06/22 15:00:24  hme
 * showToFile now writes q and vrot.
 *
 * Revision 1.6  2002/06/22 09:49:44  hme
 * Add CommandSet().
 *
 * Revision 1.5  2002/06/21 20:58:30  hme
 * Change showToFile() to use a Station instead of Time, add topo and hori
 * output to it.
 *
 * Revision 1.3  2002/06/17 22:06:26  hme
 * Add showToFile().
 *
 * Revision 1.1  2002/06/15 19:47:41  hme
 * Initial revision
 *
 *-*/

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.MiscellaneousUtils;
import org.jscience.astronomy.NaturalSatellite;

import java.io.PrintStream;


/**
 * The <code>SolarSystemNaturalSatellite</code>
 * provides some astronomical constants to its subclasses.
 * <p/>
 * <p>Copyright: &copy; 2002-2004 Horst Meyerdierks.
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
 * $Id: SolarSystemNaturalSatellite.java,v 1.3 2007-10-21 21:07:05 virtualcall Exp $
 * <p/>
 * <dl>
 * <dt><strong>3.2:</strong> 2004-08-03 hme</dt>
 * <dd>Fix bug in showToFile whereby the parallactic angle was calculated
 * with the NCP not above the N horizon but above the S horizon.  This bug
 * probably propagated from Sputnik 1.9.</dd>
 * <dt><strong>2.18:</strong> 2003/09/18 hme</dt>
 * <dd>Add protected NaiveRiseSet() method to which the semi day arc
 * calculation is delegated.  While NextRiseSet is overriden by subclasses,
 * NaiveRiseSet is inherited by them.</dd>
 * <dt><strong>2.11:</strong> 2003/09/17 hme</dt>
 * <dd>Add NextRiseSet() method.</dd>
 * <dt><strong>2.7:</strong> 2003/09/16 hme</dt>
 * <dd>Package review.</dd>
 * <dt><strong>2.6:</strong> 2003/09/15 hme</dt>
 * <dd>Avoid the use of JulianTime.setTerrestrialTime for the 1900 epoch, as it would imply
 * propagating an exception upwards that will never occur.</dd>
 * <dt><strong>2.2:</strong> 2003/09/15 hme</dt>
 * <dd>Add copy() method.</dd>
 * <dt><strong>1.22:</strong> 2003/06/14 hme</dt>
 * <dd>Add GetName() and GetXYZ() methods.</dd>
 * <dt><strong>1.21:</strong> 2003/06/14 hme</dt>
 * <dd>Show the station before the object.</dd>
 * <dt><strong>1.20:</strong> 2003/04/06 hme</dt>
 * <dd>Change distance output to km for small distances.</dd>
 * <dt><strong>1.19:</strong> 2003/04/06 hme</dt>
 * <dd>Change output of azimuth to range 0 to 360 deg.</dd>
 * <dt><strong>1.15:</strong> 2002/07/13 hme</dt>
 * <dd>Consolidate documentation.</dd>
 * <dt><strong>1.10:</strong> 2002/07/02 hme</dt>
 * <dd>Change showToFile() to use a Sun as well as a Station, and to
 * calculate and display heliocentric, LSR and GSR radial velocities.</dd>
 * <dt><strong>1.6:</strong> 2002/06/22 hme</dt>
 * <dd>Add CommandSet().</dd>
 * <dt><strong>1.5:</strong> 2002/06/21 hme</dt>
 * <dd>Change showToFile() to use a Station instead of Time, add topo and
 * hori output to it.</dd>
 * <dt><strong>1.3:</strong> 2002/06/17 hme</dt>
 * <dd>Add showToFile().</dd>
 * <dt><strong>1.1:</strong> 2002/06/15 hme</dt>
 * <dd>New class.</dd>
 * </dl>
 *
 * @author Horst Meyerdierks, c/o Royal Observatory,
 *         Blackford Hill, Edinburgh, EH9 3HJ, Scotland;
 *         &lt; hme &#64; roe.ac.uk &gt;
 * @see org.jscience.astronomy.Telescope
 * @see org.jscience.astronomy.solarsystem.sputnik
 */

public class SolarSystemNaturalSatellite extends NaturalSatellite {

    /**
     * Speed of light in Gm/d.
     */
    private static final double LIGHTGMD = 25902.068371;

    /**
     * Gauss gravitational constant.
     */
    private static final double GAUSSK = 0.01720209895;

    /**
     * Factor to convert sideral and solar time.
     */
    private static final double SIDSOL = 366.2422 / 365.2422;

    /**
     * Elevation of ordinary rise and set (refraction).
     * <p/>
     * <p>An object rises or sets when its elevation is approximately -34'.
     */
    public static final double RISE = -34. * Math.PI / 60. / 180.;

    /**
     * Elevation of solar/lunar rise and set (refraction).
     * <p/>
     * <p>The Sun or Moon (their top limb) rises or sets when its elevation is
     * approximately -50'.
     */
    public static final double RISE_SUN = -50. * Math.PI / 60. / 180.;

    /**
     * Solar elevation at civil dawn and dusk.
     * <p/>
     * <p>Civil twilight is defined as the Sun being less than 6&deg; below
     * the mathematical horizon.
     */
    public static final double RISE_CIVIL = -6. * Math.PI / 180.;

    /**
     * Solar elevation at nautical dawn and dusk.
     * <p/>
     * <p>Nautical twilight is defined as the Sun being less than 12&deg; below
     * the mathematical horizon.
     */
    public static final double RISE_NAUTIC = -12. * Math.PI / 180.;

    /**
     * Solar elevation at astronomical dawn and dusk.
     * <p/>
     * <p>Astronomical twilight is defined as the Sun being less than 18&deg;
     * below the mathematical horizon.
     */
    public static final double RISE_ASTRONOMICAL = -18. * Math.PI / 180.;

    private AstronomyCoordinates coords;

    /**
     * Initialise the object.
     * <p/>
     * This initialises the object to be the Galactic Centre, which is
     * 8.5&nbsp;kpc away.
     */
    public SolarSystemNaturalSatellite(String name) {
        super(name);
        /* The constant is 8.5 kpc. */
        final double t[] = {2.623E11, 0., 0.};
        coords.setGalactic(t);
    }

    /**
     * Serve the <code>object/whatever</code> commands that set the position.
     *
     * @param aCommand The full command string, from which the parameters will be read.
     * @param aStation Some of the commands served here require either the time (for the
     *                 obliquity of the ecliptic or in case of equinox of date coordiantes) or
     *                 the location of the observatory (if horizontal of topocentric
     *                 coordinates are involved).  This given parameter will contain that
     *                 information.
     */
    public final void commandSet(String aCommand, EarthStation aStation)
            throws Exception {

        double t1[] = new double[3];
        double t2[] = new double[3];
        String theString;

        /* Strip the first word (the command) off the command string to be left
* with the parameters.  Then read each and skip each.  At the end of
* this block theString contains the name of the object. */

        theString = MiscellaneousUtils.stringRemainder(aCommand);
        t1[0] = MiscellaneousUtils.readFloat(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        t1[1] = MiscellaneousUtils.readFloat(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        t1[2] = MiscellaneousUtils.readFloat(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        theString = MiscellaneousUtils.deQuoteString(theString);

        /* Convert to rectangular. */

        t1[0] /= MiscellaneousUtils.DEGREES_PER_RADIAN;
        t1[1] /= MiscellaneousUtils.DEGREES_PER_RADIAN;
        t2 = MiscellaneousUtils.convertSphericalToOrthogonal(t1);

        /* Set the fields. */

        setName(theString);
        if (aCommand.startsWith("object/gal")) {
            coords.setGalactic(t2);
        } else if (aCommand.startsWith("object/B1950")) {
            coords.setB1950(t2);
        } else if (aCommand.startsWith("object/J2000")) {
            coords.setJ2000(t2);
        } else if (aCommand.startsWith("object/mean")) {
            coords.setMean(aStation, t2);
        } else if (aCommand.startsWith("object/ecl")) {
            coords.setEcliptic(aStation, t2);
        } else if (aCommand.startsWith("object/topo")) {
            coords.setTopocentric(aStation, t2);
        } else if (aCommand.startsWith("object/hori")) {
            coords.setHorizontal(aStation, t2);
        }

    }

    /**
     * Return the object position.
     * <p/>
     * <p>This gives access to the stored position.  The numbers are in Gm and
     * the axis orientation is equatorial for J2000.
     * <p/>
     * The geocentric rectangular coordinates in the J2000 coordinate
     * system.
     */
    public AstronomyCoordinates getCoordinates() {
        return coords;
    }

    public void setCoordinates(AstronomyCoordinates coords) {
        this.coords = coords;
    }

    /**
     * Return the JulianTime of the next rise and set.
     * <p/>
     * <p>This takes the given station and its time to scan forward for the next
     * rise and set JulianTime.  The set may occur before the rise, of course.  The
     * given Telescope instance is left alone.
     * <p/>
     * <p>See {@link #naiveRiseSet NaiveRiseSet} for details, but note that this
     * method makes sure that the time is in the future (greater than the time
     * in the given Telescope).
     *
     * @param aTelescope The rise and set time are calculated for this given location on the
     *                   Earth.  Also, the rise and set JulianTime are the next occurences after the
     *                   time given in this instance of Telescope.  So for this method only
     *                   a Station would be required, but subclasses need to override this
     *                   method and do require a Telescope.
     * @param aElev      The elevation in radian that defines the event.
     * @param findRise   Give true for rise, false for set.
     */
    public JulianTime nextRiseSet(Telescope aTelescope, double aElev,
                                  boolean findRise)
            throws Exception {
        JulianTime theRise;

        /* Calculate a nearby rise (set). */

        theRise = naiveRiseSet(aTelescope, aElev, findRise);

        /* If that is in the past, go forward one sidereal day. */

        while (0. > theRise.subtract(aTelescope)) {
            theRise.add(1. / SIDSOL);
        }

        return theRise;

    }

    /**
     * Display the NamedObject.
     * <p/>
     * <p>This writes information about the currently stored object to the given
     * open file.  The format is
     * <p/>
     * <pre>
     * Observatory: Royal Observatory Edinburgh
     * East long.   -3.182500 deg
     * Latitude     55.925000 deg
     * Altitude           146 m
     * <p/>
     * UT: 2003-06-14-15:24:14.4 (JD  2452805.141834)
     * TT: 2003-06-14-15:25:20.3 (JDE 2452805.142596)
     * Ep: 2003.450082399
     * GST 08:54:18.3 = 133.576169 deg
     * LST 08:41:34.5 = 130.393669 deg
     * <p/>
     * Object: Galactic Centre
     * <p/>
     * coord. system      deg      deg     h  m  s     deg ' "      Gm
     * --------------  --------  -------  ----------  ---------  --------
     * gal.   lII,bII   360.000   -0.000
     * B1950  RA,Dec    265.611  -28.917  17:42:26.6  -28:55:00
     * J2000  RA,Dec    266.405  -28.936  17:45:37.2  -28:56:10
     * ecl.   lam,bet   266.888   -5.537
     * mean   RA,Dec    266.460  -28.937  17:45:50.4  -28:56:15  2.623E11
     * topo   HA,Dec   -136.066  -28.937  14:55:44.1  -28:56:15  2.623E11
     * hori   A,h        67.547  -48.928
     * <p/>
     * q     53.520 deg   parallactic angle
     * vrot      0.159 km/s  geocentric radial velocity of topocentre
     * vhel      2.213 km/s  heliocentric radial velocity of topocentre
     * vLSR     12.484 km/s  LSR radial velocity of topocentre
     * vGSR     12.484 km/s  GSR radial velocity of topocentre
     * </pre>
     * <p/>
     * <p>The rotation of the Earth according to
     * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.79f.
     * is 0.00007292115018 rad/s.  Reduction to heliocentric velocities involves
     * the spatial velocity of the Sun, which the Sun class can deliver from
     * the difference in position over a 1000&nbsp;s interval.  Reduction to
     * the local standard of rest requires us to precess the Sun's LSR velocity
     * from equinox 1900 to J2000.  The velocity in question is 20&nbsp;km/s
     * toward RA 270&deg;, Dec +30&deg; for equinox 1900.  Reduction to the
     * galactic standard of rest involves the rotation of the Galaxy of
     * 220&nbsp;km/s at the galactocentric distance of the Sun.
     *
     * @param aFile      This is the output stream to which the information is to be written.
     * @param aTelescope Some of the coordinate transforms require the time or the location of
     *                   the observatory to be known.
     *                   The spatial velocity of the Sun is needed in order to reduce
     *                   the radial velocity of the observatory from geocentric to
     *                   heliocentric.
     */
    public void showToFile(PrintStream aFile, Telescope aTelescope) {

        JulianTime the1900 = new JulianTime();
        double theGal[] = new double[3];
        double theB50[] = new double[3];
        double theDat[] = new double[3];
        double theEcl[] = new double[3];
        double theTop[] = new double[3];
        double theHor[] = new double[3];
        double theSpher[] = new double[3];
        double theRect[] = new double[3];
        double theHMS[] = new double[3];
        double theV3[] = new double[3];
        double Triplets[] = new double[9];
        double theParAng, theVRot, theVHel, theVLSR, theVGSR;
        double distMag;

        /* distMag tells whether to use normal Gm format, exponential Gm format
    * or km format. */

        distMag = Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1]
                + itsR[2] * itsR[2]);
        if (1. > distMag) {
            distMag = -1.;
        } else if (9999. < distMag) {
            distMag = +1.;
        } else {
            distMag = 0.;
        }

        /* Transform away from J2000 to all systems. */

        convertJ2000ToB1950(1, itsR, theB50);
        convertB1950ToGalactic(1, theB50, theGal);
        convertJ2000ToMean(1, aTelescope, itsR, theDat);
        convertMeanToEcliptic(1, aTelescope, theDat, theEcl);
        convertMeanToTopocentric(1, aTelescope, theDat, theTop);
        convertTopocentricToHorizontal(1, aTelescope, theTop, theHor);

        /* Parallactic angle. */

        Triplets[0] = 0.;
        Triplets[1] = 0.;
        Triplets[2] = 1.;
        Triplets[3] = theHor[0];
        Triplets[4] = theHor[1];
        Triplets[5] = theHor[2];
        Triplets[6] = Math.cos(aTelescope.getLatitude());
        Triplets[7] = 0.;
        Triplets[8] = Math.sin(aTelescope.getLatitude());
        theParAng = MiscellaneousUtils.getSphericalAngle(Triplets);
        if (Math.PI < theParAng) theParAng -= 2. * Math.PI;

        /* Rotation of the Earth. */

        aTelescope.getX0Z(Triplets);
        theVRot = -72.92115018 * Triplets[0] * theTop[1]
                / Math.sqrt(theTop[0] * theTop[0]
                + theTop[1] * theTop[1] + theTop[2] * theTop[2]);

        /* Revolution of the Earth. */

        aTelescope.itsSun.GetVel(theV3);
        theVHel = theVRot
                - (itsR[0] * theV3[0] + itsR[1] * theV3[1] + itsR[2] * theV3[2])
                / Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1] + itsR[2] * itsR[2]);

        /* Peculiar motion of the Sun.
* This is in 1900 coordinates 20 km/s towards RA 270 deg, Dec 30 deg. */

        theSpher[0] = 270. / MiscellaneousUtils.DEGREES_PER_RADIAN;
        theSpher[1] = 30. / MiscellaneousUtils.DEGREES_PER_RADIAN;
        theSpher[2] = 20.;
        MiscellaneousUtils.convertSphericalToOrthogonal(theSpher, theRect);
        the1900.setJulianDay(-34979.99998); /* TT 1900-01-00-12:00:00 */
        convertMeanToJ2000(1, the1900, theRect, theV3);
        theVLSR = theVHel
                + (itsR[0] * theV3[0] + itsR[1] * theV3[1] + itsR[2] * theV3[2])
                / Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1] + itsR[2] * itsR[2]);

        /* Rotation of the Galaxy. */

        MiscellaneousUtils.convertOrthogonalToSpherical(theGal, theSpher);
        theVGSR = theVLSR + 220. * Math.sin(theSpher[0]) * Math.cos(theSpher[1]);

        /* Print it all out.  theSpher currently holds lII, bII. */

        aTelescope.showToFile(aFile);

        aFile.print("Object: " + itsName);

        if (-0.5 > distMag) {
            aFile.print(
                    "\n\n  coord. system      deg      deg     h  m  s     deg ' \"      km\n" +
                            "  --------------  --------  -------  ----------  ---------  --------");
        } else {
            aFile.print(
                    "\n\n  coord. system      deg      deg     h  m  s     deg ' \"      Gm\n" +
                            "  --------------  --------  -------  ----------  ---------  --------");
        }

        aFile.print("\n  gal.   lII,bII  ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);

        MiscellaneousUtils.convertOrthogonalToSpherical(theB50, theSpher);
        aFile.print("\n  B1950  RA,Dec   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.degreesTodms(theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN / 15., theHMS);
        aFile.print("  ");
        MiscellaneousUtils.writeExtendedTime(aFile, theHMS[0], theHMS[1], theHMS[2]);
        aFile.print("  ");
        MiscellaneousUtils.writeAngle(aFile, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);

        MiscellaneousUtils.convertOrthogonalToSpherical(itsR, theSpher);
        aFile.print("\n  J2000  RA,Dec   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.degreesTodms(theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN / 15., theHMS);
        aFile.print("  ");
        MiscellaneousUtils.writeExtendedTime(aFile, theHMS[0], theHMS[1], theHMS[2]);
        aFile.print("  ");
        MiscellaneousUtils.writeAngle(aFile, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);

        MiscellaneousUtils.convertOrthogonalToSpherical(theEcl, theSpher);
        aFile.print("\n  ecl.   lam,bet  ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);

        MiscellaneousUtils.convertOrthogonalToSpherical(theDat, theSpher);
        aFile.print("\n  mean   RA,Dec   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.degreesTodms(theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN / 15., theHMS);
        aFile.print("  ");
        MiscellaneousUtils.writeExtendedTime(aFile, theHMS[0], theHMS[1], theHMS[2]);
        aFile.print("  ");
        MiscellaneousUtils.writeAngle(aFile, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        aFile.print("  ");
        if (-0.5 > distMag) {
            MiscellaneousUtils.writeFloat(aFile, 8, 0, theSpher[2] * 1E6);
        } else if (0.5 < distMag) {
            MiscellaneousUtils.writeFloat(aFile, theSpher[2]);
        } else {
            MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[2]);
        }

        MiscellaneousUtils.convertOrthogonalToSpherical(theTop, theSpher);
        MiscellaneousUtils.degreesTodms(theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN / 15., theHMS);
        theSpher[0] = MiscellaneousUtils.normalizeAngleAt0(theSpher[0]);
        aFile.print("\n  topo   HA,Dec   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        aFile.print("  ");
        MiscellaneousUtils.writeExtendedTime(aFile, theHMS[0], theHMS[1], theHMS[2]);
        aFile.print("  ");
        MiscellaneousUtils.writeAngle(aFile, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        aFile.print("  ");
        if (-0.5 > distMag) {
            MiscellaneousUtils.writeFloat(aFile, 8, 0, theSpher[2] * 1E6);
        } else if (0.5 < distMag) {
            MiscellaneousUtils.writeFloat(aFile, theSpher[2]);
        } else {
            MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[2]);
        }

        MiscellaneousUtils.convertOrthogonalToSpherical(theHor, theSpher);
        theSpher[0] = MiscellaneousUtils.normalizeAngleAt180(theSpher[0]);
        aFile.print("\n  hori   A,h      ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theSpher[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        MiscellaneousUtils.writeFloat(aFile, 9, 3, theSpher[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);

        aFile.print("\n\n     q   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theParAng * MiscellaneousUtils.DEGREES_PER_RADIAN);
        aFile.print(" deg   parallactic angle");

        aFile.print("\n  vrot   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theVRot);
        aFile.print(" km/s  geocentric radial velocity of topocentre");

        aFile.print("\n  vhel   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theVHel);
        aFile.print(" km/s  heliocentric radial velocity of topocentre");

        aFile.print("\n  vLSR   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theVLSR);
        aFile.print(" km/s  LSR radial velocity of topocentre");

        aFile.print("\n  vGSR   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, theVGSR);
        aFile.print(" km/s  GSR radial velocity of topocentre");

        aFile.print("\n\n");
    }

    /**
     * Return the JulianTime of the next rise and set.
     * <p/>
     * <p>This takes the given station and its time to scan for a nearby rise
     * or set time.  The set may occur before the rise, of course.  The
     * given Station instance is left alone.
     * <p/>
     * <p>We generalise the concept of rise and set to mean the rising and
     * setting passage through a given elevation.  The caller has to specify the
     * elevation required:
     * <p/>
     * <p><table border>
     * <col><col>
     * <tr><th>h</th><th>object, event etc.</th></tr>
     * <tr valign="top">
     * <td align="right">0</td>
     * <td>rise and set of any object (centre), ignoring refraction</td>
     * </tr>
     * <tr valign="top">
     * <td align="right">-50'</td>
     * <td>rise and set of Sun or Moon (upper limb), taking account of
     * average refraction and average apparent diameter</td>
     * </tr>
     * <tr valign="top">
     * <td align="right">-34'</td>
     * <td>rise and set of an object other than Sun or Moon,
     * taking account of average refraction</td>
     * </tr>
     * <tr valign="top">
     * <td align="right">-6&deg;</td>
     * <td>civil dawn and dusk when applied to the Sun</td>
     * </tr>
     * <tr valign="top">
     * <td align="right">-12&deg;</td>
     * <td>nautical dawn and dusk when applied to the Sun</td>
     * </tr>
     * <tr valign="top">
     * <td align="right">-18&deg;</td>
     * <td>astronomical dawn and dusk when applied to the Sun</td>
     * </tr>
     * </table>
     * <p/>
     * <p>(Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA).
     * All these values are available as public constants of this class.
     * <p/>
     * <p>Since the calculation depends merely on the elevation below the
     * mathematical horizon, the height above sea level is irrelevant.
     * <p/>
     * <p>For objects fixed on the celestial sphere the hour angles of rise and
     * set are a function of geodetic latitude and object declination according
     * to
     * <p/>
     * <p>HA = &plusmn;[(sin h - sin &phi;) sin &delta;]
     * / (cos &phi; cos &delta;)
     *
     * @param aStation The rise and set time are calculated for this given location on the
     *                 Earth.  Also, the rise and set JulianTime are the next occurences near the
     *                 time given in this instance of Station.
     * @param aElev    The elevation in radian that defines the event.
     * @param findRise Give true for rise, false for set.
     */
    protected JulianTime naiveRiseSet(EarthStation aStation, double aElev,
                                      boolean findRise)
            throws Exception {
        JulianTime theJulianTime;
        double theTopo[] = new double[3];
        double theSDA, theRise;

        /* Calculate the topocentric HA and Dec. */

        coords.getTopocentric(aStation, theTopo);

        /* If the declination is circumpolar, throw an exception. */
        /* Calculate the semi daily arc in the process. */

        theSDA = Math.cos(aStation.getLatitude()) * Math.cos(theTopo[1]);
        if (0. == theSDA)
            throw new Exception("object circumpolar");
        theSDA = (Math.sin(aElev) - Math.sin(aStation.getLatitude())
                * Math.sin(theTopo[1])) / theSDA;
        if (1. <= Math.abs(theSDA))
            throw new Exception("object circumpolar");
        theSDA = Math.acos(theSDA);

        /* A rise occurs (-SDA - HA) from now in sidereal time.
* We do nothing here to make sure the result is in the future. */

        /* If rise requested. */

        if (findRise) {
            theRise = MiscellaneousUtils.normalizeAngleAt0(-theSDA - theTopo[0]);
        }

        /* Else (set requested). */

        else {
            theRise = MiscellaneousUtils.normalizeAngleAt0(+theSDA - theTopo[0]);
        }

        /* copy given time to the returned JulianTime.
    * Then apply the above increment.
    * We need to scale the increment from sidereal to solar and from
    * radian to days. */

        theRise *= MiscellaneousUtils.DEGREES_PER_RADIAN / SIDSOL / 360.;
        theJulianTime = new JulianTime();
        theJulianTime.copy(aStation);
        theJulianTime.add(theRise);

        return theJulianTime;
    }

}
