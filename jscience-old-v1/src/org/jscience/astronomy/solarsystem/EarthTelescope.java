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
 * $Id: EarthTelescope.java,v 1.2 2007-10-21 17:43:15 virtualcall Exp $
 *
 * $Log: not supported by cvs2svn $
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
 * Revision 1.8  2006/07/30 20:57:14  virtualcall
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
 * Revision 1.7  2006/07/29 22:14:53  virtualcall
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
 * Revision 1.6  2006/07/26 21:23:12  virtualcall
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
 * Revision 1.5  2006/07/12 20:35:30  virtualcall
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
 * Revision 1.4  2006/07/09 21:13:24  virtualcall
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
 * Revision 1.3  2006/07/07 22:32:56  virtualcall
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
 * Revision 4.1  2004/08/09 08:26:38  hme
 * Version 2.1.1 and re-instate command string as parameter to Venus transit
 * method, it ignores the parameter.
 *
 * Revision 3.1  2004/07/28 11:20:06  hme
 * Version 2.1.
 *
 * Revision 2.26  2004/07/28 09:05:02  hme
 * Finalise VenusTransit method to serve planet/au/venus command.
 *
 * Revision 2.23  2004/03/14 20:11:58  hme
 * Change iridium/flare and h0004 to cope with simultaneous flares.
 *
 * Revision 2.21  2004/02/03 23:51:38  hme
 * Support h0003 and h0004 commands.
 *
 * Revision 2.18  2004/02/02 23:19:06  hme
 * Support h0002 command.
 *
 * Revision 2.17  2004/02/01 14:43:35  hme
 * Support satellite/pass command.
 * Also change iridium/flare to separate flares by blank lines.
 *
 * Revision 2.15  2003/09/19 18:58:10  hme
 * Support station/twilight command.
 *
 * Revision 2.12  2003/09/18 14:06:03  hme
 * Support moon/rise command.  Delegate the output for any rise/set
 * command to common protected method.  Report circumpolarity separately for
 * rise and set.
 *
 * Revision 2.11  2003/09/17 20:27:44  hme
 * Support object/rise command.
 *
 * Revision 2.9  2003/09/16 23:01:56  hme
 * Package review.
 *
 * Revision 2.6  2003/09/15 17:00:23  hme
 * Support satellite/all command.
 *
 * Revision 2.5  2003/09/15 15:27:06  hme
 * Use Telescope instead of Station plus Sun in many interfaces.
 *
 * Revision 2.4  2003/09/15 13:42:17  hme
 * Snapshot after review of JulianTime, Station, Telescope to do with interaction
 * between the time and the Sun in Telescope.
 *
 * Revision 2.3  2003/09/14 18:58:33  hme
 * Started work on iridium/flare command.
 *
 * Revision 2.2  2003/09/14 12:19:02  hme
 * Support satellite/all command.
 *
 * Revision 1.23  2003/06/14 15:20:18  hme
 * Support h0001 command.
 *
 * Revision 1.22  2003/06/08 10:53:37  hme
 * Support planet/show/pluto and asteroid/show commands.
 *
 * Revision 1.21  2003/06/07 16:04:38  hme
 * Support planet/show/venus, uranus and neptune commands.
 *
 * Revision 1.20  2003/06/01 17:26:32  hme
 * Support planet/show/saturn command.
 *
 * Revision 1.19  2003/06/01 13:02:59  hme
 * Support moon/show command.
 *
 * Revision 1.18  2003/04/21 19:05:53  hme
 * Support planet/show/mars command.
 *
 * Revision 1.16  2003/03/09 18:29:09  hme
 * Support satellite/show command.
 *
 * Revision 1.13  2003/03/02 14:27:57  hme
 * Support planet/show/mercury and /jupiter commands.
 *
 * Revision 1.12  2002/12/30 15:46:35  hme
 * Reference revision before change of VSOP87 class.
 *
 * Revision 1.11  2002/07/13 19:57:41  hme
 * Consolidate documentation.
 *
 * Revision 1.10  2002/07/13 12:12:28  hme
 * Support comet/show command.
 *
 * Revision 1.8  2002/07/02 17:50:58  hme
 * Pass itsSun to the NamedObject method that prints the object to a file
 * stream.  It uses this to calculate radial velocities.
 *
 * Revision 1.6  2002/06/23 15:26:25  hme
 * Add itsSun field.  Move CommandTime() from JulianTime to this class so it
 * serves station/{ut|tt|jd|sys} commands and updates itsSun after changing
 * the Station clock.
 *
 * Revision 1.5  2002/06/22 09:51:28  hme
 * Move CommandShow() from Station to this class so it serves station/show
 * and object/show.  Add CommandSetObject().
 *
 *-*/

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.MiscellaneousUtils;
import org.jscience.astronomy.Station;
import org.jscience.astronomy.Telescope;

import javax.vecmath.Vector3f;
import java.io.*;


/**
 * The <code>Telescope</code> class extends the {@link Station
 * Station} class which is an extension of the {@link JulianTime
 * JulianTime} class.  To the clock and the location on the Earth's surface we
 * here add a {@link NamedObject NamedObject}, i.e. the point on
 * the sky that the telescope is pointed at.  We also add a {@link
 * Sun Sun} to keep up-to-date information about the Sun.
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
 * <p>$Id: EarthTelescope.java,v 1.2 2007-10-21 17:43:15 virtualcall Exp $
 * <p/>
 * <dl>
 * <dt><strong>4.1:</strong> 2004/08/09 hme
 * <dd>Version 2.1.1 and re-instate command string as parameter to Venus
 * transit method, it ignores the parameter.
 * <dt><strong>2.26:</strong> 2004/07/28 hme
 * <dd>Finalise VenusTransit method to serve planet/au/venus command.
 * <dt><strong>2.25:</strong> 2004/06/11 hme
 * <dd>Add VenusTransit method to serve planet/au/venus command.
 * <dt><strong>2.23:</strong> 2004/03/14 hme
 * <dd>Change iridium/flare and h0004 to cope with simultaneous flares.
 * <dt><strong>2.20:</strong> 2004/02/03 hme
 * <dd>Support h0003 and h0004 commands.
 * <dt><strong>2.18:</strong> 2004/02/02 hme
 * <dd>Support h0002 command.
 * <dt><strong>2.17:</strong> 2004/02/01 hme
 * <dd>Support satellite/pass command.
 * Also change iridium/flare to separate flares by blank lines.
 * <dt><strong>2.16:</strong> 2003/11/10 hme
 * <dd>Fix bug in iridium/flare which did not copy the Station state from this
 * to theScope.
 * <dt><strong>2.15:</strong> 2003/09/19 hme
 * <dd>Support station/twilight command.
 * <dt><strong>2.12:</strong> 2003/09/18 hme
 * <dd>Support moon/rise command.  Delegate the output for any rise/set
 * command to common protected method.  Report circumpolarity separately for
 * rise and set.
 * <dt><strong>2.11:</strong> 2003/09/17 hme
 * <dd>Support object/rise command.
 * <dt><strong>2.9:</strong> 2003/09/16 hme
 * <dd>Package review.
 * <dt><strong>2.6:</strong> 2003/09/15 hme
 * <dd>Support satellite/all command.
 * <dt><strong>2.5:</strong> 2003/09/15 hme
 * <dd>Use Telescope instead of Station plus Sun in many interfaces.
 * <dt><strong>1.23:</strong> 2003/06/14 hme
 * <dd>Support h0001 command.
 * <dt><strong>1.21:</strong> 2003/06/08 hme
 * <dd>Support planet/show/pluto and asteroid/show commands.
 * <dt><strong>1.21:</strong> 2003/06/07 hme
 * <dd>Support planet/show/venus, uranus and neptune commands.
 * <dt><strong>1.20:</strong> 2003/06/01 hme
 * <dd>Support planet/show/saturn command.
 * <dt><strong>1.19:</strong> 2003/06/01 hme
 * <dd>Support moon/show command.
 * <dt><strong>1.18:</strong> 2003/04/21 hme
 * <dd>Support planet/show/mars command.
 * <dt><strong>1.16:</strong> 2003/03/09 hme
 * <dd>Support satellite/show command.
 * <dt><strong>1.13:</strong> 2003/03/02 hme
 * <dd>Support planet/show/mercury and /jupiter commands.
 * <dt><strong>1.11:</strong> 2002/07/13 hme
 * <dd>Consolidate documentation.
 * <dt><strong>1.10:</strong> 2002/07/13 hme
 * <dd>Support comet/show command.
 * <dt><strong>1.8:</strong> 2002/07/02 hme
 * <dd>Pass itsSun to the NamedObject method that prints the object to a file
 * stream.  It uses this to calculate radial velocities.
 * <dt><strong>1.6:</strong> 2002/06/23 hme
 * <dd>Add itsSun field.  Move CommandTime() from JulianTime to this class so it
 * serves station/{ut|tt|jd|sys} commands and updates itsSun after changing
 * the Station clock.
 * <dt><strong>1.5:</strong> 2002/06/22 hme
 * <dd>Move CommandShow() from Station to this class so it serves station/show
 * and object/show.  Add CommandSetObject().
 * <dt><strong>1.1:</strong> 2002/06/15 hme
 * <dd>New class.
 * </dl>
 *
 * @author Horst Meyerdierks, c/o Royal Observatory,
 *         Blackford Hill, Edinburgh, EH9 3HJ, Scotland;
 *         &lt; hme &#64; roe.ac.uk &gt;
 * @see AstralBody
 * @see Sun
 * @see org.jscience.astronomy.solarsystem.sputnik
 */

public final class EarthTelescope extends EarthStation {

    /**
     * The Sun.
     * <p/>
     * <p>This instance of the {@link Sun Sun} class keeps the
     * information about the Sun at the time of the station clock.
     */
    protected Sun itsSun;

    //the orientation they are pointing at
    private Vector3f orientation;

    public EarthTelescope(String name, double longitude, double latitude, double height, Vector3f orientation) {

        super(name, longitude, latitude, height);
        this.orientation = orientation;

    }

    public Vector3f getOrientation() {

        return orientation;

    }


    /**
     * Add to the time.
     *
     * @param aTimeStep The time increment to apply.
     */

    public void add(double aTimeStep) {

        super.add(aTimeStep);
        update();

    }

    /**
     * Serve the <code>satellite/pass</code> command.
     * <p/>
     * <p>The search step is given in seconds, 60&nbsp;s is probably short
     * enough in most cases.  The output format is:
     * <p/>
     * <pre>
     * Observatory: Edinburgh
     * East long.   -3.217000 deg
     * Latitude     55.950000 deg
     * Altitude            50 m
     * <p/>
     * UT: 2003-09-15-17:00:00.0 (JD  2452898.208333)
     * TT: 2003-09-15-17:01:06.0 (JDE 2452898.209098)
     * Ep: 2003.704884593
     * GST 16:36:59.2 = 249.246849 deg
     * LST 16:24:07.2 = 246.029849 deg
     * <p/>
     * Twilight passes of satellite ISS (ZARYA)
     * <p/>
     * UT               A      h       r
     * deg    deg      km
     * ---------------------  ------  -----  --------
     * 2004-01-26-18:16:00.0   228.7    2.7    1924.2
     * 2004-01-26-18:17:00.0   222.6    7.2    1544.4
     * 2004-01-26-18:18:00.0   212.2   12.9    1197.5
     * 2004-01-26-18:19:00.0   193.7   19.8     923.6
     * 2004-01-26-18:20:00.0   163.0   24.2     803.1
     * <p/>
     * 2004-01-26-19:51:00.0   255.9    2.4    1956.5
     * 2004-01-26-19:52:00.0   252.0    7.1    1554.0
     * </pre>
     *
     * @param aCommand The command line, including the parameters to be read.
     */

    public void commandSatPass(String aCommand)
            throws Exception, IOException {

        int theDate[] = new int[3];
        double theTime[] = new double[3];
        Telescope theScope = new Telescope();
        Satellite theSatellite;
        String theString, theFile, theName;
        double theStart, theStep, theEnd, theJD;
        int theNstep, inPass, wasInPass;
        int i;

        /* Initialise the Telescope for the time loop.  Then copy its state
* - in particular the station - from this. */

        theScope.init();
        theScope.copy(this);

        /* Read the parameters for the command: TLE file, satellite name,
    * interval, end time */

        theString = aCommand.substring(15);
        theFile = MiscellaneousUtils.deQuoteString(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        theName = MiscellaneousUtils.deQuoteString(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        theStep = MiscellaneousUtils.readFloat(theString);
        theString = MiscellaneousUtils.stringRemainder(theString).trim();
        MiscellaneousUtils.readDate(theString, theDate, theTime);
        theScope.setUniversalTime(theDate[0], theDate[1], theDate[2],
                theTime[0], theTime[1], theTime[2]);

        /* Work out the time stepping in JD. */

        theStart = getJulianDay();
        theEnd = theScope.getJulianDay();
        theStep /= 86400.;
        theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

        if (theEnd <= theStart)
            throw new Exception("end before start");

        /* Read the satellite from file. */

        theSatellite = new Satellite();
        theSatellite.init();
        theSatellite.readByName(theFile, theName);

        /* Write table header */

        showToFile(System.out);
        System.out.print("Twilight passes of satellite " + theName + "\n\n"
                + "         UT               A      h       r\n"
                + "                         deg    deg      km\n"
                + "---------------------  ------  -----  --------\n");

        /* Loop through time */

        wasInPass = 0;
        inPass = 0;
        for (i = 0; i < theNstep; i++) {

            /* Calculate time for next step. */

            theJD = theStart + (double) i * theStep;
            theScope.setJulianDay(theJD);

            /* Let the satellite update itself to the new time, decide whether
   * it is in pass, and write a line. */

            inPass = theSatellite.showPass(System.out, theScope);

            /* If we have changed from in pass to not in pass, write a blank
          * line. */

            if (0 != wasInPass && 0 == inPass) {
                System.out.print("\n");
            }

            wasInPass = inPass;
        }

        /* If at the end we are in pass, write an extra blank line. */

        if (0 != inPass) {
            System.out.print("\n");
        }

    }

    /**
     * Serve the <code>iridium/flare</code> command.
     * <p/>
     * <p>The search step is given in seconds, because when set to a single
     * minute flares will be missed.  An interval of 5&nbsp;s is probably short
     * enough.  The output format is:
     * <p/>
     * <pre>
     * Observatory: Edinburgh
     * East long.   -3.217000 deg
     * Latitude     55.950000 deg
     * Altitude            50 m
     * <p/>
     * UT: 2003-09-15-17:00:00.0 (JD  2452898.208333)
     * TT: 2003-09-15-17:01:06.0 (JDE 2452898.209098)
     * Ep: 2003.704884593
     * GST 16:36:59.2 = 249.246849 deg
     * LST 16:24:07.2 = 246.029849 deg
     * <p/>
     * Iridium flares from file: /home/hme/lib/sats/iridium.txt
     * <p/>
     * UT                A        h        r     I   Name
     * deg      deg      km    deg
     * ---------------------  --------  -------  ------  ---  ----------------------
     * 2003-09-15-20:33:35.0   358.331   21.681    1672  1.8  IRIDIUM 83
     * 2003-09-15-20:33:40.0   358.585   20.989    1702  1.4  IRIDIUM 83
     * 2003-09-15-20:33:45.0   358.829   20.316    1733  1.0  IRIDIUM 83
     * 2003-09-15-20:33:50.0   359.063   19.660    1763  0.9  IRIDIUM 83
     * 2003-09-15-20:33:55.0   359.288   19.022    1794  0.9  IRIDIUM 83
     * 2003-09-15-20:34:00.0   359.504   18.400    1825  1.2  IRIDIUM 83
     * 2003-09-15-20:34:05.0   359.713   17.793    1856  1.5  IRIDIUM 83
     * 2003-09-15-20:34:10.0   359.914   17.201    1887  1.8  IRIDIUM 83
     * <p/>
     * 2003-09-15-20:43:30.0   357.704   15.800    1964  2.0  IRIDIUM 16
     * 2003-09-15-20:43:35.0   357.971   15.264    1995  1.9  IRIDIUM 16
     * 2003-09-15-20:43:40.0   358.228   14.739    2026  1.9  IRIDIUM 16
     * 2003-09-15-20:43:45.0   358.478   14.225    2058  2.0  IRIDIUM 16
     * <p/>
     * 2003-09-15-21:30:05.0    32.998   16.608    1919  1.8  IRIDIUM 70
     * 2003-09-15-21:30:10.0    34.003   16.901    1903  1.7  IRIDIUM 70
     * 2003-09-15-21:30:15.0    35.028   17.189    1888  1.9  IRIDIUM 70
     * </pre>
     *
     * @param aCommand The command line, including the parameters to be read.
     */
    public void commandIridium(String aCommand)
            throws Exception, IOException {

        int theDate[] = new int[3];
        double theTime[] = new double[3];
        Telescope theScope = new Telescope();
        Satellite[] theSatellite;
        BufferedReader theFile;
        String theString, theFileName, theLine;
        double theStart, theStep, theEnd, theJD;
        int theNstep, theNsat;
        int inFlare[], wasInFlare[];
        int i, j;

        /* Initialise the Telescope for the time loop.  Then copy its state
* - in particular the station - from this. */

        theScope.init();
        theScope.copy(this);

        /* Read the parameters for the command: TLE file, interval, end time */

        theString = aCommand.substring(14);
        theFileName = MiscellaneousUtils.deQuoteString(theString);
        theString = MiscellaneousUtils.stringRemainder(theString);
        theStep = MiscellaneousUtils.readFloat(theString);
        theString = MiscellaneousUtils.stringRemainder(theString).trim();
        MiscellaneousUtils.readDate(theString, theDate, theTime);
        theScope.setUniversalTime(theDate[0], theDate[1], theDate[2],
                theTime[0], theTime[1], theTime[2]);

        /* Work out the time stepping in JD. */

        theStart = getJulianDay();
        theEnd = theScope.getJulianDay();
        theStep /= 86400.;
        theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

        if (theEnd <= theStart)
            throw new Exception("end before start");

        /* Open the TLE file, count the lines, close and reopen. */

        theFile = new BufferedReader(new FileReader(theFileName));
        for (theNsat = 0; ; theNsat++) {
            if ((theLine = theFile.readLine()) == null) break;
        }
        theNsat = (int) Math.floor((double) theNsat / 3.);
        theFile.close();
        if (1 > theNsat)
            throw new Exception("file has no satellites");

        /* Obtain an array of Satellite instances, initialise each.
* Read each Satellite TLE from file using NoradNext. */

        theSatellite = new Satellite[theNsat];
        wasInFlare = new int[theNsat];
        inFlare = new int[theNsat];
        theFile = new BufferedReader(new FileReader(theFileName));
        for (i = 0; i < theNsat; i++) {
            theSatellite[i] = new Satellite();
            theSatellite[i].init();
            theSatellite[i].readNext(theFile);
            wasInFlare[i] = 0;
            inFlare[i] = 0;
        }
        theFile.close();

        /* Write table header */

        showToFile(System.out);
        System.out.print("Iridium flares from file: " + theFileName + "\n\n"
                + "         UT                A        h        r     I   Name\n"
                + "                          deg      deg      km    deg\n"
                + "---------------------  --------  -------  ------  ---"
                + "  ----------------------\n");

        /* Loop through time */

        for (j = 0; j < theNstep; j++) {

            /* Calculate time for next step. */

            theJD = theStart + (double) j * theStep;
            theScope.setJulianDay(theJD);

            /* Let each satellite update itself to the new time, decide whether
   * it is in flare, and write a line. */

            for (i = 0; i < theNsat; i++) {
                inFlare[i] = theSatellite[i].showFlare(System.out, theScope);

                /* If we have changed from in flare to not in flare, write a blank
         * line. */

                if (0 != wasInFlare[i] && 0 == inFlare[i]) {
                    System.out.print("\n");
                }

                wasInFlare[i] = inFlare[i];
            }
        }

        /* At the end write an extra blank line. */

        System.out.print("\n");

    }

    /**
     * Serve the <code>station/twilight</code> command.
     * <p/>
     * <p>Serve the command <code>station/twilight</code>.  This writes to the
     * terminal the 8 times relevant to twilight.  The format is
     * <p/>
     * <pre>
     * Twilight:
     * <p/>
     * Start of astronomical dawn: 2003-05-04-00:39
     * Start of nautical dawn:     2003-05-04-02:35
     * Start of civil dawn:        2003-05-04-03:38
     * Rise  of the Sun:           2003-05-04-04:24
     * Set   of the Sun:           2003-05-04-19:57
     * End   of civil dawn:        2003-05-04-20:43
     * End   of nautical dawn:     2003-05-04-21:47
     * End   of astronomical dawn: none (circumpolar)
     * </pre>
     * <p/>
     * <p>The times are written in chronological order.  All times are in
     * the future (as referred to the time in this instance of Telescope), but
     * at most somewhat more than a day in the future.
     */
    public final void commandDawn() {

        JulianTime[] sixTimes = new JulianTime[8];
        double theDate[] = new double[3];
        double theTime[] = new double[3];
        boolean haveDawn[] = new boolean[8];
        int first, i, j;

        System.out.print("\nTwilight:\n");

        /* Obtain the eight times.
* If it were midnight now these would be in order.
* If we are somewhere during dawn, day or dusk some of the former times
* have been pushed to the next day.  We should print those last. */

        haveDawn[0] = true;
        try {
            sixTimes[0] = itsSun.nextRiseSet(this, NamedObject.RISEASTRO, true);
        }
        catch (Exception e) {
            haveDawn[0] = false;
        }
        haveDawn[1] = true;
        try {
            sixTimes[1] = itsSun.nextRiseSet(this, NamedObject.RISENAUTI, true);
        }
        catch (Exception e) {
            haveDawn[1] = false;
        }
        haveDawn[2] = true;
        try {
            sixTimes[2] = itsSun.nextRiseSet(this, NamedObject.RISECIVIL, true);
        }
        catch (Exception e) {
            haveDawn[2] = false;
        }
        haveDawn[3] = true;
        try {
            sixTimes[3] = itsSun.nextRiseSet(this, NamedObject.RISESUN, true);
        }
        catch (Exception e) {
            haveDawn[3] = false;
        }
        haveDawn[4] = true;
        try {
            sixTimes[4] = itsSun.nextRiseSet(this, NamedObject.RISESUN, false);
        }
        catch (Exception e) {
            haveDawn[4] = false;
        }
        haveDawn[5] = true;
        try {
            sixTimes[5] = itsSun.nextRiseSet(this, NamedObject.RISECIVIL, false);
        }
        catch (Exception e) {
            haveDawn[5] = false;
        }
        haveDawn[6] = true;
        try {
            sixTimes[6] = itsSun.nextRiseSet(this, NamedObject.RISENAUTI, false);
        }
        catch (Exception e) {
            haveDawn[6] = false;
        }
        haveDawn[7] = true;
        try {
            sixTimes[7] = itsSun.nextRiseSet(this, NamedObject.RISEASTRO, false);
        }
        catch (Exception e) {
            haveDawn[7] = false;
        }

        /* Figure out which of the 6 times is the first, i.e. is the smallest
   * of the existing ones. */

        /* Find the first time that exists. */

        for (first = 0; first < 7; first++) {
            if (haveDawn[first]) break;
        }

        /* If none of the times exist. */

        if (7 < first) {

            /* Write the eight entries, all as non-existent. */

            System.out.print("\n  Start of astronomical dawn: none (circumpolar)");
            System.out.print("\n  Start of nautical dawn:     none (circumpolar)");
            System.out.print("\n  Start of civil dawn:        none (circumpolar)");
            System.out.print("\n  Rise  of the Sun:           none (circumpolar)");
            System.out.print("\n  Set   of the Sun:           none (circumpolar)");
            System.out.print("\n  End   of civil dawn:        none (circumpolar)");
            System.out.print("\n  End   of nautical dawn:     none (circumpolar)");
            System.out.print("\n  End   of astronomical dawn: none (circumpolar)");

        }

        /* Else (one or more times exist). */

        else {

            /* Find the smallest of the existing times. */

            for (i = first + 1; i < 8; i++) {
                if (haveDawn[i])
                    if (0. > sixTimes[i].subtract(sixTimes[first])) first = i;
            }

            /* Write the six times, starting with the earliest. */

            j = first;
            for (i = 0; i < 8; i++) {

                switch (j) {
                    case 0:
                        System.out.print("\n  Start of astronomical dawn: ");
                        break;
                    case 1:
                        System.out.print("\n  Start of nautical dawn:     ");
                        break;
                    case 2:
                        System.out.print("\n  Start of civil dawn:        ");
                        break;
                    case 3:
                        System.out.print("\n  Rise  of the Sun:           ");
                        break;
                    case 4:
                        System.out.print("\n  Set   of the Sun:           ");
                        break;
                    case 5:
                        System.out.print("\n  End   of civil dawn:        ");
                        break;
                    case 6:
                        System.out.print("\n  End   of nautical dawn:     ");
                        break;
                    case 7:
                        System.out.print("\n  End   of astronomical dawn: ");
                        break;
                    default:
                        break;
                }

                if (haveDawn[j]) {
                    sixTimes[j].getDate(theDate);
                    sixTimes[j].getUniversalTimehms(theTime);
                    MiscellaneousUtils.writeRoundedDate(System.out, theDate[0], theDate[1], theDate[2],
                            theTime[0], theTime[1], theTime[2]);
                } else {
                    System.out.print("none (circumpolar)");
                }

                j++;
                if (7 < j) j -= 8;
            }
        }

        System.out.print("\n\n");

    }

    /**
     * Serve the <code>whatever/rise</code> commands.
     * <p/>
     * <p>Serve the commands
     * <code>object/rise</code>,
     * <code>planet/rise/sun</code>,
     * <code>planet/rise/&lt;planet&gt;</code>,
     * <code>moon/rise</code>,
     * <code>comet/rise</code>, <code>asteroid/rise</code>.
     * <p/>
     * <p>See {@link #ShowRiseSet ShowRiseSet} for more information.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void commandRise(String aCommand)
            throws Exception, IOException {
        String theString, theFile, theName;
        Comet theComet;
        Moon theMoon;
        Mercury theMercury;
        Venus theVenus;
        Mars theMars;
        Jupiter theJupiter;
        Saturn theSaturn;
        Uranus theUranus;
        Neptune theNeptune;
        Pluto thePluto;

        if (aCommand.startsWith("object/rise")) {
            showRiseSet(itsObject, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/sun")) {
            showRiseSet(itsSun, NamedObject.RISESUN, System.out);
        } else if (aCommand.startsWith("planet/rise/mercury")) {
            theMercury = new Mercury();
            theMercury.init();
            theMercury.update(this);
            showRiseSet(theMercury, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/venus")) {
            theVenus = new Venus();
            theVenus.init();
            theVenus.update(this);
            showRiseSet(theVenus, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/mars")) {
            theMars = new Mars();
            theMars.init();
            theMars.update(this);
            showRiseSet(theMars, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/jupiter")) {
            theJupiter = new Jupiter();
            theJupiter.init();
            theJupiter.update(this);
            showRiseSet(theJupiter, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/saturn")) {
            theSaturn = new Saturn();
            theSaturn.init();
            theSaturn.update(this);
            showRiseSet(theSaturn, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/uranus")) {
            theUranus = new Uranus();
            theUranus.init();
            theUranus.update(this);
            showRiseSet(theUranus, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/neptune")) {
            theNeptune = new Neptune();
            theNeptune.init();
            theNeptune.update(this);
            showRiseSet(theNeptune, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("planet/rise/pluto")) {
            thePluto = new Pluto();
            thePluto.init();
            thePluto.update(this);
            showRiseSet(thePluto, NamedObject.RISE, System.out);
        } else if (aCommand.startsWith("moon/rise")) {
            theMoon = new Moon();
            theMoon.init();
            theMoon.update(this);
            showRiseSet(theMoon, NamedObject.RISESUN, System.out);
        } else if (aCommand.startsWith("comet/rise") ||
                aCommand.startsWith("asteroid/rise")) {
            theString = aCommand.substring(11);
            theFile = MiscellaneousUtils.deQuoteString(theString);
            theString = MiscellaneousUtils.stringRemainder(theString);
            theName = MiscellaneousUtils.deQuoteString(theString);
            theComet = new Comet();
            theComet.init();
            theComet.readByName(theFile, theName);
            theComet.update(this);
            showRiseSet(theComet, NamedObject.RISE, System.out);
        }

    }

    /**
     * Serve the <code>object/*</code> commands that set the position.
     *
     * @param aCommand The command line, including the parameters to be read.
     */
    public final void commandSetObject(String aCommand)
            throws Exception {
        itsObject.commandSet(aCommand, this);

    }

    /**
     * Serve various <code>whatever/show</code> commands.
     * <p/>
     * <p>Serve the commands
     * <code>station/show</code>,
     * <code>object/show</code>,
     * <code>planet/show/sun</code>,
     * <code>planet/show/&lt;planet&gt;</code>,
     * <code>moon/show</code>,
     * <code>comet/show</code>, <code>asteroid/show</code>,
     * <code>satellite/show</code>,
     * <code>satellite/all</code>.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void commandShow(String aCommand)
            throws Exception, IOException {

        String theString, theFile, theName;
        Comet theComet;
        Satellite theSatellite;
        Moon theMoon;
        Mercury theMercury;
        Venus theVenus;
        Mars theMars;
        Jupiter theJupiter;
        Saturn theSaturn;
        Uranus theUranus;
        Neptune theNeptune;
        Pluto thePluto;

        if (aCommand.startsWith("station/show")) {
            showToFile(System.out);
        } else if (aCommand.startsWith("object/show")) {
            itsObject.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/sun")) {
            itsSun.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/mercury")) {
            theMercury = new Mercury();
            theMercury.init();
            theMercury.update(this);
            theMercury.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/venus")) {
            theVenus = new Venus();
            theVenus.init();
            theVenus.update(this);
            theVenus.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/mars")) {
            theMars = new Mars();
            theMars.init();
            theMars.update(this);
            theMars.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/jupiter")) {
            theJupiter = new Jupiter();
            theJupiter.init();
            theJupiter.update(this);
            theJupiter.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/saturn")) {
            theSaturn = new Saturn();
            theSaturn.init();
            theSaturn.update(this);
            theSaturn.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/uranus")) {
            theUranus = new Uranus();
            theUranus.init();
            theUranus.update(this);
            theUranus.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/neptune")) {
            theNeptune = new Neptune();
            theNeptune.init();
            theNeptune.update(this);
            theNeptune.showToFile(System.out, this);
        } else if (aCommand.startsWith("planet/show/pluto")) {
            thePluto = new Pluto();
            thePluto.init();
            thePluto.update(this);
            thePluto.showToFile(System.out, this);
        } else if (aCommand.startsWith("moon/show")) {
            theMoon = new Moon();
            theMoon.init();
            theMoon.update(this);
            theMoon.showToFile(System.out, this);
        } else if (aCommand.startsWith("comet/show") ||
                aCommand.startsWith("asteroid/show")) {
            theString = aCommand.substring(11);
            theFile = MiscellaneousUtils.deQuoteString(theString);
            theString = MiscellaneousUtils.stringRemainder(theString);
            theName = MiscellaneousUtils.deQuoteString(theString);
            theComet = new Comet();
            theComet.init();
            theComet.readByName(theFile, theName);
            theComet.update(this);
            theComet.showToFile(System.out, this);
        } else if (aCommand.startsWith("satellite/show")) {
            theString = aCommand.substring(15);
            theFile = MiscellaneousUtils.deQuoteString(theString);
            theString = MiscellaneousUtils.stringRemainder(theString);
            theName = MiscellaneousUtils.deQuoteString(theString);
            theSatellite = new Satellite();
            theSatellite.init();
            theSatellite.readByName(theFile, theName);
            theSatellite.update(this);
            theSatellite.showToFile(System.out, this);
        } else if (aCommand.startsWith("satellite/all")) {
            theString = aCommand.substring(14);
            theFile = MiscellaneousUtils.deQuoteString(theString);
            theSatellite = new Satellite();
            theSatellite.init();
            theSatellite.showAllToFile(System.out, theFile, this);
        }

    }

    /**
     * Serve the <code>station/{ut|tt|jd|sys}</code> commands.
     * <p/>
     * <p>This also updates the Telescope's instance of the Sun class to show the
     * position of the Sun for the new time.
     *
     * @param aCommand The command line, including the parameters to be read.
     */
    public void commandTime(String aCommand)
            throws Exception {

        int theDate[] = new int[3];
        double theTime[] = new double[3];

        if (aCommand.startsWith("station/ut ")) {
            MiscellaneousUtils.readDate(aCommand.substring(11), theDate, theTime);
            setUniversalTime(theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
        } else if (aCommand.startsWith("station/tt ")) {
            MiscellaneousUtils.readDate(aCommand.substring(11), theDate, theTime);
            setTerrestrialTime(theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
        } else if (aCommand.startsWith("station/jd ")) {
            setJulianDay(MiscellaneousUtils.readFloat(aCommand.substring(11)) - 2450000.);
        } else if (aCommand.startsWith("station/sys")) {
            setUTSystem();
        } else {
            throw new Exception("unrecognised command");
        }

    }

    /**
     * copy the state of a Telescope instance from another.
     * <p/>
     * <p>Invoke this method for the new instance of Telescope and pass as
     * argument an existing Telescope instance from which to copy the state.
     * The new instance must have been initialised with init() before making
     * this call.
     *
     * @param aTelescope The time, location, object and Sun to be copied into this instance.
     */
    public void copy(Telescope aTelescope) {

        super.copy(aTelescope);
        itsObject.copy(aTelescope.itsObject);
        update();

    }

    /**
     * Serve <code>h0004</code> command.
     * <p/>
     * <p>This writes to a file a fragment of XHTML stating when Iridium flares
     * occur during the next 24 hour period (from midday).  It uses the system
     * clock to determine the nearest midday, and it uses the file
     * <code>./data/stations.dat</code> to read the observatory
     * "Royal Observatory Edinburgh" from and the file
     * <code>./data/iridium.txt</code> to read the satellites from.
     * It writes the result to the file <code>./iriday.html</code>
     * <p/>
     * <p>This also writes an equivalent WML file (for WAP phones) to
     * <code>./iriday.wml</code>.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void h0004(String aCommand)
            throws Exception, IOException {

        BufferedReader theFile;
        String theLine;
        PrintStream theXhtml, theWml;
        Telescope theScope;
        Satellite[] theSatellite;
        double theStart, theStep, theEnd, theJD;
        double theDate[] = new double[3];
        double startPos[][], startTime[][];
        double peakPos[][], peakTime[][];
        double endPos[][], endTime[][];
        double peakAngle[];
        double theAngle;
        int inPass[], wasInPass[];
        int theNstep, theNsat;
        int i, j;

        /* Read the station from file. */

        readByName("data/stations.dat", "Royal Observatory Edinburgh");

        /* Set the time to the nearest midday UT, starting from system time. */

        setUTSystem();
        setJulianDay(Math.floor(0.5 + getJulianDay()));
        getDate(theDate);

        /* Initialise the Telescope for the time loop.  Then copy its state
* - in particular the station - from this. */

        theScope = new Telescope();
        theScope.init();
        theScope.copy(this);

        /* Work out the time stepping in JD. */

        theStart = getJulianDay();
        theEnd = theStart + 1.;
        theStep = 1. / 86400.;
        theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

        /* Open the TLE file, count the lines, close and reopen. */

        theFile = new BufferedReader(new FileReader("data/iridium.txt"));
        for (theNsat = 0; ; theNsat++) {
            if ((theLine = theFile.readLine()) == null) break;
        }
        theNsat = (int) Math.floor((double) theNsat / 3.);
        theFile.close();
        if (1 > theNsat)
            throw new Exception("file has no satellites");

        /* Obtain an array of Satellite instances, initialise each.
* Read each Satellite TLE from file using NoradNext. */

        theSatellite = new Satellite[theNsat];
        peakAngle = new double[theNsat];
        inPass = new int[theNsat];
        wasInPass = new int[theNsat];
        startTime = new double[theNsat][3];
        peakTime = new double[theNsat][3];
        endTime = new double[theNsat][3];
        startPos = new double[theNsat][3];
        peakPos = new double[theNsat][3];
        endPos = new double[theNsat][3];
        theFile = new BufferedReader(new FileReader("data/iridium.txt"));
        for (i = 0; i < theNsat; i++) {
            theSatellite[i] = new Satellite();
            theSatellite[i].init();
            theSatellite[i].readNext(theFile);
        }
        theFile.close();

        /* Open the output files. */

        theXhtml = new PrintStream(new FileOutputStream("iriday.html"));
        theWml = new PrintStream(new FileOutputStream("iriday.wml"));

        /* Some fixed XHTML, observatory and date, table header. */

        theXhtml.print("<h2>Iridium tonight</h2>\n\n"
                + "<p>\nThese are the flare times of Iridium satellites\n"
                + "over the location\n"
                + "\"" + itsName + "\"\n"
                + "during the 24 hour period following midday Universal Time of\n");
        MiscellaneousUtils.writeDate(theXhtml, theDate[0], theDate[1], theDate[2]);
        theXhtml.print(", irrespective of daylight.\n</p>\n");

        theWml.print("<?xml version=\"1.0\"?>\n"
                + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
                + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
                + "<wml>\n"
                + "<template>\n"
                + "  <do type='prev' label='Back'>\n"
                + "    <prev/>\n"
                + "  </do>\n"
                + "</template>\n"
                + "<card>\n"
                + "<p>\n"
                + "Iridium flares for " + itsName + ", night of ");
        MiscellaneousUtils.writeDate(theWml, theDate[0], theDate[1], theDate[2]);
        theWml.print(".\n</p>\n");

        theXhtml.print("<p></p>\n"
                + "<table border=\"1\" summary=\"Iridium flares\">\n"
                + "<col /><col /><col /><col /><col /><col />"
                + "<col /><col /><col /><col /><col />\n"
                + "<tr><th scope=\"col\">name</th>"
                + "<th scope=\"col\" colspan=\"3\">start</th>"
                + "<th scope=\"col\" colspan=\"4\">peak</th>"
                + "<th scope=\"col\" colspan=\"3\">end</th></tr>\n");
        theWml.print("<p>\nUT A h angle\n</p>\n");

        /* Loop through time. */

        for (j = 0; j < theNstep; j++) {

            /* Calculate JD. */

            theJD = theStart + (double) j * theStep;
            theScope.setJulianDay(theJD);

            /* Loop through the satellites. */

            for (i = 0; i < theNsat; i++) {

                /* Update the satellite, check it is up, sunlit and flaring. */

                theAngle = theSatellite[i].testFlare(theScope);
                if (2. >= theAngle) {
                    inPass[i] = 1;
                } else {
                    inPass[i] = 0;
                }

                /* If a flare is starting. */

                if (0 == wasInPass[i] && 0 != inPass[i]) {

                    /* Set the start time and position. */

                    theSatellite[i].getHorizontal(0, theScope, startPos[i]);
                    theScope.getUniversalTimehms(startTime[i]);

                    /* Set candiate end time and position */

                    theScope.getUniversalTimehms(endTime[i]);
                    endPos[i][0] = startPos[i][0];
                    endPos[i][1] = startPos[i][1];
                    endPos[i][2] = startPos[i][2];

                    /* Set candiate peak time, position and angle */

                    theScope.getUniversalTimehms(peakTime[i]);
                    peakPos[i][0] = startPos[i][0];
                    peakPos[i][1] = startPos[i][1];
                    peakPos[i][2] = startPos[i][2];
                    peakAngle[i] = theAngle;

                }

                /* Else if a flare has ended. */

                else if (0 != wasInPass[i] && 0 == inPass[i]) {

                    /* Write XHTML table row. */

                    theXhtml.print("<tr><td>"
                            + theSatellite[i].getName() + "</td><td>");
                    MiscellaneousUtils.writeTime(theXhtml, startTime[i][0],
                            startTime[i][1], startTime[i][2]);
                    theXhtml.print("</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 5, 1, startPos[i][0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 4, 1, startPos[i][1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td>");

                    theXhtml.print("<td>");
                    MiscellaneousUtils.writeTime(theXhtml, peakTime[i][0],
                            peakTime[i][1], peakTime[i][2]);
                    theXhtml.print("</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 5, 1, peakPos[i][0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 4, 1, peakPos[i][1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 3, 1, peakAngle[i]);
                    theXhtml.print("&deg;</td>");

                    theXhtml.print("<td>");
                    MiscellaneousUtils.writeTime(theXhtml, endTime[i][0], endTime[i][1], endTime[i][2]);
                    theXhtml.print("</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 5, 1, endPos[i][0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td><td align=\"right\">");
                    MiscellaneousUtils.writeFloat(theXhtml, 4, 1, endPos[i][1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theXhtml.print("&deg;</td></tr>\n");

                    /* Write WML table row. */

                    theWml.print("<p>\n");
                    MiscellaneousUtils.writeTime(theWml, peakTime[i][0],
                            peakTime[i][1], peakTime[i][2]);
                    theWml.print(" ");
                    MiscellaneousUtils.writeFloat(theWml, 3, 0, peakPos[i][0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theWml.print(" ");
                    MiscellaneousUtils.writeFloat(theWml, 2, 0, peakPos[i][1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                    theWml.print(" ");
                    MiscellaneousUtils.writeFloat(theWml, 3, 1, peakAngle[i]);
                    theWml.print("\n</p>\n");

                }

                /* Else if in flare. */

                else if (0 != inPass[i]) {

                    /* Update candiate end time and position. */

                    theSatellite[i].getHorizontal(0, theScope, endPos[i]);
                    theScope.getUniversalTimehms(endTime[i]);

                    /* If brighter than peak so far, update candidate peak. */

                    if (peakAngle[i] > theAngle) {
                        theScope.getUniversalTimehms(peakTime[i]);
                        peakPos[i][0] = endPos[i][0];
                        peakPos[i][1] = endPos[i][1];
                        peakPos[i][2] = endPos[i][2];
                        peakAngle[i] = theAngle;
                    }
                }

                wasInPass[i] = inPass[i];
            }
        }

        /* Table end and blurb. */

        theWml.print("</card>\n</wml>\n");
        theXhtml.print("</table>\n"
                + "<p>Listed are for each flare the name of the satellite, the start,\n"
                + "peak and end of the flare.  Start is here when the flare angle\n"
                + "goes below 2&deg;, end when it exceeds that value again.  The peak\n"
                + "is when the angle is at its smallest (to the nearest second).\n"
                + "Apart from the times are listed the azimuth\n"
                + "(0&deg; N, 90&deg; E, 180&deg; S, 270&deg; W) and elevation\n"
                + "(0&deg; horizon, 90&deg; zenith).  For the peak the flare angle\n"
                + "is also listed.</p>\n");

        /* Close the output files. */

        theWml.close();
        theXhtml.close();

    }

    /**
     * Serve <code>h0003</code> command.
     * <p/>
     * <p>This writes to a file a fragment of XHTML stating when ISS/Zarya
     * passes during the next 24 hour period (from midday).  It uses the system
     * clock to determine the nearest midday, and it uses the file
     * <code>./data/stations.dat</code> to read the observatory
     * "Royal Observatory Edinburgh" from and the file
     * <code>./data/stations.txt</code> to read the satellite "ISS (ZARYA)" from.
     * It writes the result to the file <code>./zarday.html</code>
     * <p/>
     * <p>This also writes an equivalent WML file (for WAP phones) to
     * <code>./zarday.wml</code>.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void h0003(String aCommand)
            throws Exception, IOException {

        PrintStream theXhtml, theWml;
        Telescope theScope;
        Satellite theSatellite;
        double theStart, theStep, theEnd, theJD;
        double theSun[] = new double[3];
        double theDate[] = new double[3];
        double theSatPos[] = new double[3];
        double peakTime[] = new double[3];
        double peakPos[] = new double[3];
        double lastTime[] = new double[3];
        double lastPos[] = new double[3];
        int theNstep, inPass, wasInPass;
        int i;

        /* Read the station from file. */

        readByName("data/stations.dat", "Royal Observatory Edinburgh");

        /* Set the time to the nearest midday UT, starting from system time. */

        setUTSystem();
        setJulianDay(Math.floor(0.5 + getJulianDay()));
        getDate(theDate);

        /* Initialise the Telescope for the time loop.  Then copy its state
* - in particular the station - from this. */

        theScope = new Telescope();
        theScope.init();
        theScope.copy(this);

        /* Work out the time stepping in JD. */

        theStart = getJulianDay();
        theEnd = theStart + 1.;
        theStep = 1. / 1440.;
        theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

        /* Read the satellite from file. */

        theSatellite = new Satellite();
        theSatellite.init();
        theSatellite.readByName("data/stations.txt", "ISS (ZARYA)");

        /* Open the output files. */

        theXhtml = new PrintStream(new FileOutputStream("zarday.html"));
        theWml = new PrintStream(new FileOutputStream("zarday.wml"));

        /* Some fixed XHTML, observatory and date, table header. */

        theXhtml.print("<h2>ISS/Zarya tonight</h2>\n\n"
                + "<p>\nThese are the passes of the International Space Station\n"
                + "(Zarya) over the location\n"
                + "\"" + itsName + "\"\n"
                + "during the 24 hour period following midday Universal Time of\n");
        MiscellaneousUtils.writeDate(theXhtml, theDate[0], theDate[1], theDate[2]);
        theXhtml.print(", subject to civil twilight.\n</p>\n");

        theWml.print("<?xml version=\"1.0\"?>\n"
                + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
                + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
                + "<wml>\n"
                + "<template>\n"
                + "  <do type='prev' label='Back'>\n"
                + "    <prev/>\n"
                + "  </do>\n"
                + "</template>\n"
                + "<card>\n"
                + "<p>\n"
                + "ISS/Zarya for " + itsName + ", night of ");
        MiscellaneousUtils.writeDate(theWml, theDate[0], theDate[1], theDate[2]);
        theWml.print(".\n</p>\n");

        theXhtml.print("<p></p>\n"
                + "<table border=\"1\" summary=\"ISS/Zarya passes\">\n"
                + "<col /><col /><col /><col /><col /><col /><col /><col /><col />\n"
                + "<tr><th scope=\"col\" colspan=\"3\">start</th>"
                + "<th scope=\"col\" colspan=\"3\">peak</th>"
                + "<th scope=\"col\" colspan=\"3\">end</th></tr>\n");
        theWml.print("<p>\nUT A h\n</p>\n");

        /* Loop through time. */

        wasInPass = 0;
        inPass = 0;
        for (i = 0; i < theNstep; i++) {

            /* Calculate time for next step. */

            theJD = theStart + (double) i * theStep;
            theScope.setJulianDay(theJD);

            /* Update the satellite, check it is up, sunlit and it is dark. */

            theSatellite.update(theScope);
            inPass = 0;
            if (0 != theSatellite.itsIsSunlit) {
                theSatellite.getHorizontal(0, theScope, theSatPos);
                if (0. < theSatPos[1]) {
                    theScope.itsSun.getHorizontal(0, theScope, theSun);
                    if (NamedObject.RISECIVIL > theSun[1]) {
                        inPass = 1;
                    }
                }
            }

            /* If we changed from no to yes. */

            if (0 == wasInPass && 0 != inPass) {

                /* Reset the peak tracker. */

                theScope.getUniversalTimehms(peakTime);
                peakPos[0] = theSatPos[0];
                peakPos[1] = theSatPos[1];
                peakPos[2] = theSatPos[2];

                /* Write time, A, h as start of a pass. */

                theXhtml.print("<tr><td>");
                MiscellaneousUtils.writeRoundedTime(theXhtml, peakTime[0], peakTime[1], peakTime[2]);
                theXhtml.print("</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 5, 1, peakPos[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 4, 1, peakPos[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td>");
            }

            /* Else if we changed from yes to no. */

            else if (0 != wasInPass && 0 == inPass) {

                /* Write the peak, then the previous entry as end of pass. */

                theXhtml.print("<td>");
                MiscellaneousUtils.writeRoundedTime(theXhtml, peakTime[0], peakTime[1], peakTime[2]);
                theXhtml.print("</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 5, 1, peakPos[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 4, 1, peakPos[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td>");

                theWml.print("<p>\n");
                MiscellaneousUtils.writeRoundedTime(theWml, peakTime[0], peakTime[1], peakTime[2]);
                theWml.print(" ");
                MiscellaneousUtils.writeFloat(theWml, 3, 0, peakPos[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theWml.print(" ");
                MiscellaneousUtils.writeFloat(theWml, 2, 0, peakPos[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theWml.print("</p>\n");

                theXhtml.print("<td>");
                MiscellaneousUtils.writeRoundedTime(theXhtml, lastTime[0], lastTime[1], lastTime[2]);
                theXhtml.print("</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 5, 1, lastPos[0] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td><td align=\"right\">");
                MiscellaneousUtils.writeFloat(theXhtml, 4, 1, lastPos[1] * MiscellaneousUtils.DEGREES_PER_RADIAN);
                theXhtml.print("&deg;</td></tr>\n");
            }

            /* Else if h higher than peak so far. */

            else if (peakPos[1] < theSatPos[1]) {

                /* Update peak tracker. */

                theScope.getUniversalTimehms(peakTime);
                peakPos[0] = theSatPos[0];
                peakPos[1] = theSatPos[1];
                peakPos[2] = theSatPos[2];
            }

            theScope.getUniversalTimehms(lastTime);
            lastPos[0] = theSatPos[0];
            lastPos[1] = theSatPos[1];
            lastPos[2] = theSatPos[2];
            wasInPass = inPass;
        }

        /* Table end and blurb. */

        theWml.print("</card>\n</wml>\n");
        theXhtml.print("</table>\n"
                + "<p>Listed are for each pass the start, highest elevation and end\n"
                + "of visibility.  The times are full minutes within the period of\n"
                + "visibility.  For these instances are listed the time, azimuth\n"
                + "(0&deg; N, 90&deg; E, 180&deg; S, 270&deg; W) and elevation\n"
                + "(0&deg; horizon, 90&deg; zenith).  Evening passes start with the\n"
                + "rise and end with ingress into the Earth's shadow.  Morning passes\n"
                + "start with egress and end with the set.  The peak elevation\n"
                + "(during visibility) may be the same as the ingress or egress.</p>\n");

        /* Close the output files. */

        theWml.close();
        theXhtml.close();

    }

    /**
     * Serve <code>h0002</code> command.
     * <p/>
     * <p>This writes to a file a fragment of XHTML stating when planets rise
     * and set during the next 24 hour period (from midday).  It uses the system
     * clock to determine the nearest midday, and it uses the file
     * <code>./data/stations.dat</code> to read the observatory
     * "Royal Observatory Edinburgh" from.  It writes the result to the file
     * <code>./planday.html</code>
     * <p/>
     * <p>This also writes an equivalent WML file (for WAP phones) to
     * <code>./planday.wml</code>.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void h0002(String aCommand)
            throws Exception, IOException {

        PrintStream theXhtml, theWml;
        Moon theMoon;
        Mercury theMercury;
        Venus theVenus;
        Mars theMars;
        Jupiter theJupiter;
        Saturn theSaturn;
        Uranus theUranus;
        Neptune theNeptune;
        Pluto thePluto;
        JulianTime theTime;
        double theDate[] = new double[3];
        double theTriplet[] = new double[3];

        /* Set up theTime in case we fail to get a rise/set time. */

        theTime = new JulianTime();
        theTime.init();

        /* Read the station from file. */

        readByName("data/stations.dat", "Royal Observatory Edinburgh");

        /* Set the time to the nearest midday UT, starting from system time. */

        setUTSystem();
        setJulianDay(Math.floor(0.5 + getJulianDay()));
        getDate(theDate);

        /* Open the output files. */

        theXhtml = new PrintStream(new FileOutputStream("planday.html"));
        theWml = new PrintStream(new FileOutputStream("planday.wml"));

        /* Some fixed XHTML, observatory and date. */

        theXhtml.print("<h2>The planets tonight</h2>\n\n"
                + "<p>\nThese are the times of twilight for the location\n"
                + "\"" + itsName + "\"\n"
                + "during the 24 hour period following midday Universal Time of\n");
        MiscellaneousUtils.writeDate(theXhtml, theDate[0], theDate[1], theDate[2]);
        theXhtml.print(":\n</p>\n");

        theWml.print("<?xml version=\"1.0\"?>\n"
                + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
                + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
                + "<wml>\n"
                + "<template>\n"
                + "  <do type='prev' label='Back'>\n"
                + "    <prev/>\n"
                + "  </do>\n"
                + "</template>\n"
                + "<card>\n"
                + "<p>\n"
                + "Planets' rise &amp; set (UT) for " + itsName + ",\n");
        MiscellaneousUtils.writeDate(theWml, theDate[0], theDate[1], theDate[2]);
        theWml.print(":\n</p>\n");

        /* Civil dusk and dawn, dto. nautical and astronomical.
 * (false for set, true for rise.) */

        theXhtml.print("<p></p>\n"
                + "<table border=\"1\" summary=\"twilight times\">\n"
                + "<col /><col /><col />\n<tr><td>&nbsp;</td>"
                + "<th scope=\"col\">dusk</th><th scope=\"col\">dawn</th></tr>\n");
        theWml.print("<p>\ndusk dawn\n</p>\n");

        theXhtml.print("<tr><th scope=\"row\">civil</td>");
        theWml.print("<p>\n");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISECIVIL, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISECIVIL, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" civil\n</p>\n");
        theXhtml.print("</tr>\n");

        theXhtml.print("<tr><th scope=\"row\">nautical</td>");
        theWml.print("<p>\n");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISENAUTI, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISENAUTI, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" nautical\n</p>\n");
        theXhtml.print("</tr>\n");

        theXhtml.print("<tr><th scope=\"row\">astronomical</td>");
        theWml.print("<p>\n");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISEASTRO, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISEASTRO, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" astronomical\n</p>\n");
        theXhtml.print("</tr>\n");

        theXhtml.print("</table>\n");

        /* Some fixed XHTML, observatory and date. */

        theXhtml.print(
                "<p>\nThese are the times of rise and set of the Sun, Moon\n"
                        + "and planets for the location\n"
                        + "\"" + itsName + "\"\n"
                        + "during the 24 hour period following midday Universal Time of\n");
        MiscellaneousUtils.writeDate(theXhtml, theDate[0], theDate[1], theDate[2]);
        theXhtml.print(":\n</p>\n");

        /* Rise and set of Sun, Moon and each planet. */

        theXhtml.print("<p></p>\n"
                + "<table border=\"1\" summary=\"twilight times\">\n"
                + "<col /><col /><col />\n<tr><td>&nbsp;</td>"
                + "<th scope=\"col\">rise</th><th scope=\"col\">set</th></tr>\n");
        theWml.print("<p>\nrise set\n</p>\n");

        theXhtml.print("<tr><th scope=\"row\">Sun</td>");
        theWml.print("<p>\n");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISESUN, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = itsSun.nextRiseSet(this, NamedObject.RISESUN, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Sun\n</p>\n");
        theXhtml.print("</tr>\n");

        theMoon = new Moon();
        theMoon.init();
        theMoon.update(this);
        theXhtml.print("<tr><th scope=\"row\">Moon</td>");
        theWml.print("<p>\n");
        try {
            theTime = theMoon.nextRiseSet(this, NamedObject.RISESUN, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theMoon.nextRiseSet(this, NamedObject.RISESUN, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Moon\n</p>\n");
        theXhtml.print("</tr>\n");

        theMercury = new Mercury();
        theMercury.init();
        theMercury.update(this);
        theXhtml.print("<tr><th scope=\"row\">Mercury</td>");
        theWml.print("<p>\n");
        try {
            theTime = theMercury.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theMercury.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Mercury\n</p>\n");
        theXhtml.print("</tr>\n");

        theVenus = new Venus();
        theVenus.init();
        theVenus.update(this);
        theXhtml.print("<tr><th scope=\"row\">Venus</td>");
        theWml.print("<p>\n");
        try {
            theTime = theVenus.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theVenus.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Venus\n</p>\n");
        theXhtml.print("</tr>\n");

        theMars = new Mars();
        theMars.init();
        theMars.update(this);
        theXhtml.print("<tr><th scope=\"row\">Mars</td>");
        theWml.print("<p>\n");
        try {
            theTime = theMars.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theMars.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Mars\n</p>\n");
        theXhtml.print("</tr>\n");

        theJupiter = new Jupiter();
        theJupiter.init();
        theJupiter.update(this);
        theXhtml.print("<tr><th scope=\"row\">Jupiter</td>");
        theWml.print("<p>\n");
        try {
            theTime = theJupiter.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theJupiter.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Jupiter\n</p>\n");
        theXhtml.print("</tr>\n");

        theSaturn = new Saturn();
        theSaturn.init();
        theSaturn.update(this);
        theXhtml.print("<tr><th scope=\"row\">Saturn</td>");
        theWml.print("<p>\n");
        try {
            theTime = theSaturn.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theSaturn.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Saturn\n</p>\n");
        theXhtml.print("</tr>\n");

        theUranus = new Uranus();
        theUranus.init();
        theUranus.update(this);
        theXhtml.print("<tr><th scope=\"row\">Uranus</td>");
        theWml.print("<p>\n");
        try {
            theTime = theUranus.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theUranus.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Uranus\n</p>\n");
        theXhtml.print("</tr>\n");

        theNeptune = new Neptune();
        theNeptune.init();
        theNeptune.Update(this);
        theXhtml.print("<tr><th scope=\"row\">Neptune</td>");
        theWml.print("<p>\n");
        try {
            theTime = theNeptune.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = theNeptune.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Neptune\n</p>\n");
        theXhtml.print("</tr>\n");

        thePluto = new Pluto();
        thePluto.init();
        thePluto.update(this);
        theXhtml.print("<tr><th scope=\"row\">Pluto</td>");
        theWml.print("<p>\n");
        try {
            theTime = thePluto.nextRiseSet(this, NamedObject.RISE, true);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" ");
        try {
            theTime = thePluto.nextRiseSet(this, NamedObject.RISE, false);
        }
        catch (Exception e) {
            theTime.copy(this);
            theTime.add(2.);
        }
        if (1. > theTime.subtract(this)) {
            theTime.getUniversalTimehms(theTriplet);
            theXhtml.print("<td>");
            MiscellaneousUtils.writeRoundedTime(theXhtml, theTriplet[0], theTriplet[1], theTriplet[2]);
            theXhtml.print("</td>");
            MiscellaneousUtils.writeRoundedTime(theWml, theTriplet[0], theTriplet[1], theTriplet[2]);
        } else {
            theXhtml.print("<td>&nbsp;</td>");
            theWml.print("--:--");
        }
        theWml.print(" Pluto\n</p>\n");
        theXhtml.print("</tr>\n");

        theWml.print("</card>\n</wml>\n");
        theXhtml.print("</table>\n<p></p>\n");

        theWml.close();
        theXhtml.close();

    }

    /**
     * Serve <code>h0001</code> command.
     * <p/>
     * <p>This returns the current information about Mars in a prosaic form
     * and in HTML language.
     *
     * @param aCommand The command string including any parameters to know what to show.
     */
    public final void h0001(String aCommand) {

        double theOctet[] = new double[8];
        Mars theMars;

        /* Create Mars and update it to the time currently set. */

        theMars = new Mars();
        theMars.init();
        theMars.update(this);

        /* The text we want looks like this:
   *
   * Today is 2003/06/13, the time is 06:02:25 Univeral Time.
   * Mars is currently 100.3 million km from Earth.
   * To an observer in Edinburgh it is 17 degrees above the South horizon.
   * The apparent diameter is 14 arcseconds, the brightness -1.0 mag.
   * 88 per cent of the planet appears illuminated.
   * The Martian South Pole is inclined 21 degrees toward the observer and
   * the rotation axis appears rotated 9 degrees clockwise
   * from celestial North.
   * The centre of the planetary disc as seen from Earth has
   * Martian longitude 37.3 degrees. */

        getDate(theOctet);
        System.out.print("<p>Today is " + (int) theOctet[0] + "/");
        if (10 > (int) theOctet[1]) System.out.print("0");
        System.out.print((int) theOctet[1] + "/");
        if (10 > (int) theOctet[2]) System.out.print("0");
        System.out.print((int) theOctet[2] + ", the time is ");
        getUniversalTimehms(theOctet);
        if (10 > (int) theOctet[0]) System.out.print("0");
        System.out.print((int) theOctet[0] + ":");
        if (10 > (int) theOctet[1]) System.out.print("0");
        System.out.print((int) theOctet[1] + ":");
        if (10 > (int) (theOctet[2] + 0.5)) System.out.print("0");
        System.out.print((int) (theOctet[2] + 0.5) + " Universal Time.\n");

        theMars.getXYZ(theOctet);
        System.out.print("<p>" + theMars.getName() + " is currently ");
        MiscellaneousUtils.writeFloat(System.out, 1, 1, Math.sqrt(theOctet[0] * theOctet[0]
                + theOctet[1] * theOctet[1] + theOctet[2] * theOctet[2]));
        System.out.print(" million km from Earth.\n");

        theMars.getHorizontal(0, this, theOctet);
        theOctet[0] *= MiscellaneousUtils.DEGREES_PER_RADIAN;
        theOctet[1] *= MiscellaneousUtils.DEGREES_PER_RADIAN;
        System.out.print("<p>As seen from the location \""
                + itsName + "\"\nit is ");
        if (0. > theOctet[1]) {
            MiscellaneousUtils.writeFloat(System.out, 1, 0, -theOctet[1]);
            System.out.print(" degrees below the ");
        } else {
            MiscellaneousUtils.writeFloat(System.out, 1, 0, theOctet[1]);
            System.out.print(" degrees above the ");
        }
        if (22.5 > theOctet[0] || 360. - 22.5 < theOctet[0]) {
            System.out.print("North horizon.\n");
        } else if (67.5 > theOctet[0]) {
            System.out.print("Northeast horizon.\n");
        } else if (112.5 > theOctet[0]) {
            System.out.print("East horizon.\n");
        } else if (157.5 > theOctet[0]) {
            System.out.print("Southeast horizon.\n");
        } else if (202.5 > theOctet[0]) {
            System.out.print("South horizon.\n");
        } else if (247.5 > theOctet[0]) {
            System.out.print("Southwest horizon.\n");
        } else if (292.5 > theOctet[0]) {
            System.out.print("West horizon.\n");
        } else {
            System.out.print("Northwest horizon.\n");
        }

        theMars.getPhysics(theOctet);
        System.out.print("<p>The apparent diameter is ");
        MiscellaneousUtils.writeFloat(System.out, 1, 0,
                2. * theOctet[1] * 3600. * MiscellaneousUtils.DEGREES_PER_RADIAN);
        System.out.print(" arcseconds, the brightness ");
        MiscellaneousUtils.writeFloat(System.out, 1, 1, theOctet[0]);
        System.out.print(" mag.\n");
        MiscellaneousUtils.writeFloat(System.out, 1, 0, 100. * theOctet[4]);
        System.out.print(" per cent of the planet appears illuminated.\n");

        if (0. > theOctet[5]) {
            System.out.print("<p>The Martian South pole is inclined ");
            MiscellaneousUtils.writeFloat(System.out, 1, 0, -theOctet[5] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        } else {
            System.out.print("<p>The Martian North pole is inclined ");
            MiscellaneousUtils.writeFloat(System.out, 1, 0, theOctet[5] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        }
        System.out.print(" degrees toward the observer and the \n"
                + "rotation axis appears rotated ");
        if (0. > theOctet[6]) {
            MiscellaneousUtils.writeFloat(System.out, 1, 0, -theOctet[6] * MiscellaneousUtils.DEGREES_PER_RADIAN);
            System.out.print(" degrees clockwise from celestial North.\n");
        } else {
            MiscellaneousUtils.writeFloat(System.out, 1, 0, theOctet[6] * MiscellaneousUtils.DEGREES_PER_RADIAN);
            System.out.print(" degrees counter-clockwise from celestial North.\n");
        }

        System.out.print("The centre of the planetary disc "
                + "as seen from Earth\nhas Martian longitude ");
        MiscellaneousUtils.writeFloat(System.out, 3, 1, theOctet[7] * MiscellaneousUtils.DEGREES_PER_RADIAN);
        System.out.print(" degrees.\n");
    }

    /**
     * Initialise the object.
     * <p/>
     * <p>This initialises the Station part, then obtains itsObject and itsSun
     * and initialises them.  itsSun is also updated to the current time.
     */
    public final void init() {

        super.init();
        itsObject = new NamedObject();
        itsObject.init();
        itsSun = new Sun();
        itsSun.init();
        update();

    }

    /**
     * Set the time to the given Julian Day (UT).
     *
     * @param aJD The Julian Day minus 2.45 million days that is to be stored.
     */

    public void setJulianDay(double aJD) {

        super.setJulianDay(aJD);
        update();

    }

    /**
     * Set the time to the given date and time (TT).
     *
     * @param aYear   The calendar year.
     * @param aMonth  The calendar month.
     * @param aDay    The calendar day.
     * @param aHour   The TT hour.
     * @param aMinute The TT minute.
     * @param aSecond The TT second.
     */
    public void setTerrestrialTime(int aYear, int aMonth, int aDay,
                                   double aHour, double aMinute, double aSecond)
            throws Exception {
        super.setTerrestrialTime(aYear, aMonth, aDay, aHour, aMinute, aSecond);
        update();

    }

    /**
     * Set the time to the given date and time (UT).
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

        super.setUniversalTime(aYear, aMonth, aDay, aHour, aMinute, aSecond);
        update();

    }

    /**
     * Set the time from the system clock.
     */
    public void setUTSystem() {

        super.setUTSystem();
        update();

    }

    /**
     * Calculate and print rise and set.
     * <p/>
     * <p>This asks the given object for its rise and set times (in fact the
     * times it crosses the given elevation upwards and downwards).  It then
     * writes out the result to the given open output file.  The format is
     * <p/>
     * <pre>
     * Object: Galactic Centre
     * <p/>
     * Set:  2003-09-17-20:42
     * Rise: 2003-09-18-15:42
     * </pre>
     * <p/>
     * <p>Rise and set are written in chronological order.  Both times are in
     * the future (as referred to the time in this instance of Telescope), but
     * at most somewhat more than a day in the future.
     *
     * @param aObject The object whose rise and set is requested.
     * @param aElev   The elevation defining the event.  See
     *                {@link NamedObject#NextRiseSet NamedObject.NextRiseSet} for details.
     * @param aFile   The open output file where to write the information to.
     */
    protected final void showRiseSet(NamedObject aObject, double aElev,
                                     PrintStream aFile)
            throws Exception {

        JulianTime[] twoTimes = new JulianTime[2];
        double theDate[] = new double[3];
        double theTime[] = new double[3];
        boolean haveRise, haveSet;

        /* Print out the object name. */

        aFile.print("\nObject: " + aObject.getName() + "\n");

        /* Calculate the rise, then the set.
     * If either inidicates circumpolarity, catch, report this for both and
     * bail out. */

        haveRise = true;
        try {
            twoTimes[0] = aObject.nextRiseSet(this, aElev, true);
        }
        catch (Exception e) {
            haveRise = false;
        }
        haveSet = true;
        try {
            twoTimes[1] = aObject.nextRiseSet(this, aElev, false);
        }
        catch (Exception e) {
            haveSet = false;
        }

        if (!haveRise && !haveSet) {
            aFile.print("\n  Rise: none (circumpolar)");
            aFile.print("\n  Set:  none (circumpolar)");
        } else if (!haveRise) {
            twoTimes[1].getDate(theDate);
            twoTimes[1].getUniversalTimehms(theTime);
            aFile.print("\n  Set:  ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
            aFile.print("\n  Rise: none (circumpolar)");
        } else if (!haveSet) {
            twoTimes[0].getDate(theDate);
            twoTimes[0].getUniversalTimehms(theTime);
            aFile.print("\n  Rise: ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
            aFile.print("\n  Set:  none (circumpolar)");
        } else if (0. < twoTimes[1].subtract(twoTimes[0])) {
            twoTimes[0].getDate(theDate);
            twoTimes[0].getUniversalTimehms(theTime);
            aFile.print("\n  Rise: ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
            twoTimes[1].getDate(theDate);
            twoTimes[1].getUniversalTimehms(theTime);
            aFile.print("\n  Set:  ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
        } else {
            twoTimes[1].getDate(theDate);
            twoTimes[1].getUniversalTimehms(theTime);
            aFile.print("\n  Set:  ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
            twoTimes[0].getDate(theDate);
            twoTimes[0].getUniversalTimehms(theTime);
            aFile.print("\n  Rise: ");
            MiscellaneousUtils.writeRoundedDate(aFile, theDate[0], theDate[1], theDate[2],
                    theTime[0], theTime[1], theTime[2]);
        }

        aFile.print("\n\n");

    }

    /**
     * Update after time change.
     * <p/>
     * <p>This updates the whole state according to the time currently set.
     */
    protected final void update() {
        itsSun.update(this);
    }

    /**
     * Serve <code>planet/au/venus</code> command.
     * <p/>
     * <p>This calculates assumes 11 different values of the astronomical unit
     * and prints out the consequent topocentric apparent separation between
     * Venus and the Sun.  While this can be done for any time, it makes most
     * sense during a Venus transit like 2004-06-08 or 2012-06-06.  The format
     * of the output is
     * <pre>
     * Observatory: Edinburgh
     * East long.   -3.217000 deg
     * Latitude     55.950000 deg
     * Altitude            50 m
     * <p/>
     * UT: 2004-06-08-08:00:00.0 (JD  2453164.833333)
     * TT: 2004-06-08-08:01:06.5 (JDE 2453164.834103)
     * Ep: 2004.434864074
     * GST 01:08:10.8 =  17.045078 deg
     * LST 00:55:18.7 =  13.828078 deg
     * <p/>
     * Venus-Sun separation for various AU values
     * <p/>
     * AU      d       d       d       d       d
     * Gm      "       "       "       "       "
     * -----  --+0--  --+2--  --+4--  --+6--  --+8--
     * 100.0   657.4   656.9   656.4   655.9   655.4
     * 110.0   655.0   654.6   654.2   653.8   653.4
     * 120.0   653.0   652.7   652.3   652.0   651.7
     * 130.0   651.4   651.1   650.8   650.5   650.2
     * 140.0   649.9   649.7   649.4   649.2   648.9
     * 150.0   648.7   648.5   648.2   648.0   647.8
     * 160.0   647.6   647.4   647.2   647.0   646.8
     * 170.0   646.6   646.5   646.3   646.1   646.0
     * 180.0   645.8   645.6   645.5   645.3   645.2
     * 190.0   645.0   644.9   644.8   644.6   644.5
     * 200.0   644.4   644.2   644.1   644.0   643.9
     * </pre>
     * <p/>
     * <p>The AU values are 100.0, 102.0, ... 110.0, ... 208.0 Gm.  The left
     * column shows an AU value, the second column (+0) the Venus-Sun separation
     * for that value.  The further columns show the separation for increasing
     * AU values (+2, +4, +6, +8 Gm).
     *
     * @param aCommand The command string, ignored.
     */

    public final void venusTransit(String aCommand)
            throws Exception {
        double thea[] = new double[3];     /* topocentre */
        double theA[] = new double[3];     /* topocentre */
        double thex[] = new double[3];     /* Sun */
        double theX[] = new double[3];     /* Venus */
        double thext[] = new double[3];
        double theXt[] = new double[3];
        NamedObject theTopo = new NamedObject();
        Telescope theScope = new Telescope();
        Venus theVenus = new Venus();
        double theAU, theDist;
        int i, j, k;

        /* copy this instance of Telescope to a new instance. */

        theScope.init();
        theScope.copy(this);

        /* Get an instance of Venus and update it for this time. */

        theVenus.init();
        theVenus.update(theScope);

        /* Get an instance of NamedObject for the topocentre.
* Set it to (0,0,0) topocentric.  The method converts to geocentric. */

        theTopo.init();
        thea[0] = 0.;
        thea[1] = 0.;
        thea[2] = 0.;
        theTopo.setTopocentric(0, theScope, thea);

        /* Extract the geocentric position of the topocentre (a) with GetXYZ.
* Extract the geocentric position of the Sun (x) with GetPos or GetXYZ.
* Extract the geocentric position of Venus (X) with GetXYZ.
* Re-convert Sun and Venus from Gm to AU. */

        theTopo.getXYZ(thea);
        theScope.itsSun.GetXYZ(thex);
        theVenus.getXYZ(theX);
        for (i = 0; i < 3; i++) {
            thex[i] /= NamedObject.AU;
            theX[i] /= NamedObject.AU;
        }

        /* Write header. */

        showToFile(System.out);
        System.out.print("Venus-Sun separation for various AU values\n\n"
                + "    AU      d       d       d       d       d\n"
                + "    Gm      \"       \"       \"       \"       \"\n"
                + "  -----  --+0--  --+2--  --+4--  --+6--  --+8--\n");

        /* Loop through hypothetical AU values. */

        for (j = 0; j < 11; j++) {
            for (k = 0; k < 5; k++) {

                /* Set the AU value. */

                theAU = 100. + 10. * j + 2. * k;

                /* Topocentric positions. */

                for (i = 0; i < 3; i++) {
                    thext[i] = thex[i] - thea[i] / theAU;
                    theXt[i] = theX[i] - thea[i] / theAU;
                }

                /* Topocentric apparent distance. */

                theDist = MiscellaneousUtils.sphericalDistance(thext, theXt);
                theDist *= MiscellaneousUtils.DEGREES_PER_RADIAN;
                theDist *= 3600.;

                /* Print the lead AU value. */

                if (0 == k) {
                    System.out.print("  ");
                    MiscellaneousUtils.writeFloat(System.out, 5, 1, theAU);
                }

                /* Print the result. */

                if (10000. > theDist) {
                    System.out.print("  ");
                    MiscellaneousUtils.writeFloat(System.out, 6, 1, theDist);
                } else {
                    System.out.print("  ******");
                }

                /* Print the line feed. */

                if (4 == k) {
                    System.out.print("\n");
                }
            }
        }

        /* Trailing blank line. */

        System.out.print("\n");

    }

}
