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
 * $Id: VSOP87NaturalSatellite.java,v 1.2 2007-10-21 17:43:16 virtualcall Exp $
 *
 * $Log: not supported by cvs2svn $
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
 * Revision 1.3  2006/07/30 20:57:15  virtualcall
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
 * Revision 1.1  2006/07/13 22:20:04  virtualcall
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
 * Revision 1.7  2006/07/13 17:41:27  virtualcall
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
 * Revision 1.6  2006/07/12 20:35:33  virtualcall
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
 * Revision 1.5  2006/07/09 21:13:26  virtualcall
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
 * Revision 1.3  2006/07/05 21:04:55  virtualcall
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
 * Revision 1.2  2006/07/04 21:46:49  virtualcall
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
 * Revision 1.1  2006/06/27 21:35:40  virtualcall
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
 * Revision 2.5  2003/09/18 10:13:11  hme
 * Make the constructor protected so that only a subclass can instantiate it.
 *
 * Revision 2.4  2003/09/16 18:48:57  hme
 * *** empty log message ***
 *
 * Revision 2.3  2003/09/16 18:25:52  hme
 * Package review.
 *
 * Revision 2.2  2003/09/15 15:28:00  hme
 * Use Telescope instead of Station plus Sun in many interfaces.
 *
 * Revision 2.1  2003/09/14 09:51:29  hme
 * *** empty log message ***
 *
 * Revision 1.15  2003/06/14 15:27:17  hme
 * Add GetPhysics() method.
 *
 * Revision 1.14  2003/03/09 19:34:17  hme
 * *** empty log message ***
 *
 * Revision 1.13  2002/12/30 17:30:49  hme
 * *** empty log message ***
 *
 * Revision 1.12  2002/12/30 17:12:55  hme
 * The previous storage of the A VSOP87 coefficients did not work when
 * different sub-classes began to co-exist.  Now the coefficients are class
 * variables in the sub-classes (Sun and Mercury), and passed as arguments
 * to the GetHelio() method of the VSOP87 class.
 *
 * Revision 1.11  2002/07/13 18:25:56  hme
 * Consolidate documentation.
 *
 * Revision 1.9  2002/07/07 14:33:18  hme
 * Add physical ephemeris to class state.  Add speed of light as constant.
 * Override the superclass's showToFile() method so that physical ephemeris
 * will be written.
 *
 * Revision 1.7  2002/07/02 11:08:02  hme
 * Fix bug: normalise final angle itself, not the L1 coefficient.
 *
 * Revision 1.6  2002/06/23 17:39:09  hme
 * In compiling L1, normalise the angle to +-PI to avoid sin() or cos() of
 * large angles later on.
 *
 *-*/

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.MiscellaneousUtils;

import java.io.PrintStream;


/**
 * <p>The <code>VSOP87</code> class extends the {@link NamedObject
 * NamedObject} class such that it stores not only one position and a name,
 * but also holds information needed for the Sun or a major planet (excluding
 * Pluto).
 * <p/>
 * <p>The VSOP87 model of planetary motion
 * (Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, chapter 31 and appendix II;
 * P. Bretagnon, G. Francou, 1988, Planetary theories in rectangular and spherical variables, VSOP87 solutions, <em>Astronomy and Astrophysics</em>, <strong>202</strong>, p.309ff)
 * gives us polynomials in time for each planet Mercury to Uranus that are
 * independent of the other planets.  There are three polynomials, one each
 * for geometric heliocentric longitude, latitude and distance.  Presumably
 * they are heliocentric and not barycentric and the vectors point to the
 * planets' centres and not the centres of their planet-moon systems.
 * <p/>
 * <p>The VSOP87 model does not use osculating Kepler orbits, so we do not need
 * to update the orbital data as we have to for comets and asteroids (cf.
 * {@link Comet Comet}).
 * <p/>
 * <p>For each planet Mercury to Neptune there are three polynomials in time,
 * one for longitude (L), one for latitude (B) and one for distance (R).  The
 * polyomials are of different orders (j), up to 6th order.  Each coefficient
 * in each of the three polynomials is the sum of up to 91 terms (i).  Each
 * term is periodic in time and expressed by three numbers, a magnitude A, a
 * phase B and a frequency C.  for example, the full polynomial in distance
 * would be
 * <p/>
 * <p>r = &sum;<SUB>j</SUB>[t<SUP>j</SUP>
 * &sum;<SUB>i</SUB>{A<SUB>Rji</SUB>
 * cos(B<SUB>Rji</SUB> + C<SUB>Rji</SUB> t)}]
 * <p/>
 * <p>The precision of the result can be estimated from the smallest
 * A<SUB>kji</SUB> that is kept in the calculation.  The error is less than
 * 2&nbsp;i<SUP>0.5</SUP>&nbsp;A<SUB>kji</SUB>.  These errors propagate
 * through the sums over j to give the full error.
 * <p/>
 * <p>The results of the sums refer to the mean ecliptic and equinox of date,
 * but not exactly in the FK5 reference frame.  Conversion to J2000
 * equatorial rectangular coordinates can be performed.  But since the effect
 * is below 0.1" and the accuracy here is worse than that, this correction is
 * not calculated.
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
 * <p>$Id: VSOP87NaturalSatellite.java,v 1.2 2007-10-21 17:43:16 virtualcall Exp $
 * <p/>
 * <dl>
 * <dt><strong>2.5:</strong> 2003/09/18 hme
 * <dd>Make the constructor protected so that only a subclass can instantiate
 * it.
 * <dt><strong>2.3:</strong> 2003/09/16 hme
 * <dd>Package review.
 * <dt><strong>1.15:</strong> 2003/06/14 hme
 * <dd>Add GetPhysics() method.
 * <dt><strong>1.12:</strong> 2002/12/30 hme
 * <dd>The previous storage of the A VSOP87 coefficients did not work when
 * different sub-classes began to co-exist.  Now the coefficients are class
 * variables in the sub-classes (Sun and Mercury), and passed as arguments
 * to the GetHelio() method of the VSOP87 class.
 * <dt><strong>1.11:</strong> 2002/07/13 hme
 * <dd>Consolidate documentation.
 * <dt><strong>1.9:</strong> 2002/07/07 hme
 * <dd>Add physical ephemeris to class state.  Add speed of light as
 * constant.  Override the superclass's showToFile() method so that physical
 * ephemeris will be written.
 * <dt><strong>1.7:</strong> 2002/07/02 hme
 * <dd>Fix bug: normalise final angle itself, not the L1 coefficient.
 * <dt><strong>1.4:</strong> 2002/06/23 hme
 * <dd>New class.
 * </dl>
 *
 * @author Horst Meyerdierks, c/o Royal Observatory,
 *         Blackford Hill, Edinburgh, EH9 3HJ, Scotland;
 *         &lt; hme &#64; roe.ac.uk &gt;
 * @see Telescope
 * @see org.jscience.astronomy.solarsystem.sputnik
 */


public abstract class VSOP87NaturalSatellite extends SolarSystemNaturalSatellite {

    /**
     * Astronomical unit (au) in units of Gm.
     */
    private static final double AU = 149.597870;

    /**
     * Elongation from the Sun in rad.
     */
    private double itsElong;

    /**
     * Phase angle in rad.
     */
    private double itsPhase;

    /**
     * Illuminated fraction of disc.
     */
    private double itsIllum;

    /**
     * Magnitude.
     */
    private double itsMag;

    /**
     * Apparent radius in rad.
     */
    private double itsRho;

    /**
     * Inclination of the rotation axis in rad.
     */
    private double itsBeta;

    /**
     * Position angle of the rotation axis in rad.
     */
    private double itsPA;

    /**
     * Central meridian in rad.
     */
    private double itsCM;

    /**
     * Initialise the VSOP87 object.
     * <p/>
     * <p>This sets the object to be a point 100&nbsp;Gm from Earth in the
     * direction of the vernal equinox.
     */
    public VSOP87NaturalSatellite(String name) {
        super(name);
        final double t[] = {100., 0., 0.};
        getCoordinates().setJ2000(t);
    }

    /**
     * Return the physical ephemeris.
     * <p/>
     * <p>This gives access to the stored physical ephemeris.
     *
     * @param aOctet The eight returned numbers are
     *               the brightness in magnitudes,
     *               the apparent radius,
     *               the elongation from the Sun,
     *               the phase angle,
     *               the illuminated fraction of the disc,
     *               the inclination of the rotation axis,
     *               the position angle,
     *               the central meridian.
     *               All angles are in radian.
     */
    public final void getPhysics(double aOctet[]) {

        aOctet[0] = itsMag;
        aOctet[1] = itsRho;
        aOctet[2] = itsElong;
        aOctet[3] = itsPhase;
        aOctet[4] = itsIllum;
        aOctet[5] = itsBeta;
        aOctet[6] = itsPA;
        aOctet[7] = itsCM;

    }

    /**
     * Display the VSOP87.
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
     * UT: 2003-06-14-15:26:11.4 (JD  2452805.143188)
     * TT: 2003-06-14-15:27:17.3 (JDE 2452805.143950)
     * Ep: 2003.450086106
     * GST 08:56:15.6 = 134.065033 deg
     * LST 08:43:31.8 = 130.882533 deg
     * <p/>
     * Object: Sun
     * <p/>
     * coord. system      deg      deg     h  m  s     deg ' "      Gm
     * --------------  --------  -------  ----------  ---------  --------
     * gal.   lII,bII   182.946   -5.976
     * B1950  RA,Dec     81.761   23.223  05:27:02.7   23:13:21
     * J2000  RA,Dec     82.521   23.261  05:30:04.9   23:15:38
     * ecl.   lam,bet    83.180   -0.000
     * mean   RA,Dec     82.573   23.263  05:30:17.5   23:15:47   151.941
     * topo   HA,Dec     48.311   23.262  03:13:14.6   23:15:42   151.937
     * hori   A,h       247.452   42.026
     * <p/>
     * q    -47.662 deg   parallactic angle
     * vrot     -0.179 km/s  geocentric radial velocity of topocentre
     * vhel     -0.348 km/s  heliocentric radial velocity of topocentre
     * vLSR    -12.138 km/s  LSR radial velocity of topocentre
     * vGSR    -23.382 km/s  GSR radial velocity of topocentre
     * <p/>
     * mag    -26.7         V magnitude
     * rho    944.8"        apparent radius
     * El      0.0 deg     elongation from the Sun
     * phi    180.0 deg     phase angle
     * L      1.000       illuminated fraction of the disc
     * i      0.9 deg     inclination of rotation axis
     * PA    -10.2 deg     position angle of rotation axis
     * CM    290.0 deg     central meridian
     * </pre>
     * <p/>
     * <p>This method calls the superclass method to make the output for
     * NamedObject and only adds the physical ephemeris at the end.  These
     * will previously have been calculated by the subclass, e.g. the Sun
     * class.
     *
     * @param aFile      This is the output stream to which the information is to be written.
     * @param aTelescope Some of the coordinate transforms require the time or the location of
     *                   the observatory to be known.
     *                   The spatial velocity of the Sun is needed in order to reduce
     *                   the radial velocity of the observatory from geocentric to
     *                   heliocentric.
     */
    public final void showToFile(PrintStream aFile, EarthTelescope aTelescope) {

        super.showToFile(aFile, aTelescope);

        aFile.print("   mag   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, itsMag);
        aFile.print("         V magnitude");

        aFile.print("\n   rho   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, 3600. * MiscellaneousUtils.DEGREES_PER_RADIAN * itsRho);
        aFile.print("\"        apparent radius");

        aFile.print("\n    El   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, MiscellaneousUtils.DEGREES_PER_RADIAN * itsElong);
        aFile.print(" deg     elongation from the Sun");

        aFile.print("\n   phi   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, MiscellaneousUtils.DEGREES_PER_RADIAN * itsPhase);
        aFile.print(" deg     phase angle");

        aFile.print("\n     L   ");
        MiscellaneousUtils.writeFloat(aFile, 8, 3, itsIllum);
        aFile.print("       illuminated fraction of the disc");

        aFile.print("\n     i   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, MiscellaneousUtils.DEGREES_PER_RADIAN * itsBeta);
        aFile.print(" deg     inclination of rotation axis");

        aFile.print("\n    PA   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, MiscellaneousUtils.DEGREES_PER_RADIAN * itsPA);
        aFile.print(" deg     position angle of rotation axis");

        aFile.print("\n    CM   ");
        MiscellaneousUtils.writeFloat(aFile, 6, 1, MiscellaneousUtils.DEGREES_PER_RADIAN * itsCM);
        aFile.print(" deg     central meridian");

        aFile.print("\n\n");

    }

    /**
     * Calculate the heliocentric distance in Gm from the VSPO87 polynomial.
     * <p/>
     * <p>r = &sum;<SUB>j</SUB>[t<SUP>j</SUP>
     * &sum;<SUB>i</SUB>{A<SUB>Rji</SUB>
     * cos(B<SUB>Rji</SUB> + C<SUB>Rji</SUB> t)}]
     *
     * @param aTime The time for which to calculate the radius.
     * @param aR0   The R0 coefficients.
     * @param aR1   The R1 coefficients.
     * @param aR2   The R2 coefficients.
     * @param aR3   The R3 coefficients.
     * @param aR4   The R4 coefficients.
     * @param aR5   The R5 coefficients.
     */
    public final double getDistance(JulianTime aTime,
                                    double[] aR0, double[] aR1, double[] aR2,
                                    double[] aR3, double[] aR4, double[] aR5) {

        double theTau, theR0, theR1, theR2, theR3, theR4, theR5, theR;
        int i;

        theTau = (aTime.getJulianEphemerisDay() - 1545.) / 365250.;

        theR0 = 0.;
        for (i = 0; 0. < aR0[3 * i]; i++) {
            theR0 += aR0[3 * i] * Math.cos(aR0[3 * i + 1] + aR0[3 * i + 2] * theTau);
        }
        theR1 = 0.;
        for (i = 0; 0. < aR1[3 * i]; i++) {
            theR1 += aR1[3 * i] * Math.cos(aR1[3 * i + 1] + aR1[3 * i + 2] * theTau);
        }
        theR2 = 0.;
        for (i = 0; 0. < aR2[3 * i]; i++) {
            theR2 += aR2[3 * i] * Math.cos(aR2[3 * i + 1] + aR2[3 * i + 2] * theTau);
        }
        theR3 = 0.;
        for (i = 0; 0. < aR3[3 * i]; i++) {
            theR3 += aR3[3 * i] * Math.cos(aR3[3 * i + 1] + aR3[3 * i + 2] * theTau);
        }
        theR4 = 0.;
        for (i = 0; 0. < aR4[3 * i]; i++) {
            theR4 += aR4[3 * i] * Math.cos(aR4[3 * i + 1] + aR4[3 * i + 2] * theTau);
        }
        theR5 = 0.;
        for (i = 0; 0. < aR5[3 * i]; i++) {
            theR5 += aR5[3 * i] * Math.cos(aR5[3 * i + 1] + aR5[3 * i + 2] * theTau);
        }

        theR = theR5;
        theR *= theTau;
        theR += theR4;
        theR *= theTau;
        theR += theR3;
        theR *= theTau;
        theR += theR2;
        theR *= theTau;
        theR += theR1;
        theR *= theTau;
        theR += theR0;
        theR /= 1E8;
        theR *= AU;

        return theR;

    }

    /**
     * Return the mean J2000 heliocentric position for the given time.
     * <p/>
     * <p>This uses the VSOP87 polynomials to calculate the mean heliocentric
     * coordinates for the given time, then transforms these to J2000.
     * The position is rectangular in Gm.  The position is returned to the
     * caller and not stored in the object fields.  This is because it is a
     * heliocentric position and not a geometric - let alone an astrometric -
     * one.
     *
     * @param aTime The time for which to calculate the position.
     * @param aL0   The L0 coefficients.
     * @param aL1   The L1 coefficients.
     * @param aL2   The L2 coefficients.
     * @param aL3   The L3 coefficients.
     * @param aL4   The L4 coefficients.
     * @param aL5   The L5 coefficients.
     * @param aB0   The B0 coefficients.
     * @param aB1   The B1 coefficients.
     * @param aB2   The B2 coefficients.
     * @param aB3   The B3 coefficients.
     * @param aB4   The B4 coefficients.
     * @param aB5   The B5 coefficients.
     * @param aR0   The R0 coefficients.
     * @param aR1   The R1 coefficients.
     * @param aR2   The R2 coefficients.
     * @param aR3   The R3 coefficients.
     * @param aR4   The R4 coefficients.
     * @param aR5   The R5 coefficients.
     * @return aTriplet
     *         The returned J2000 heliocentric position in Gm.
     */
    public final double[] getHeliocentric(JulianTime aTime,
                                          double[] aL0, double[] aL1, double[] aL2,
                                          double[] aL3, double[] aL4, double[] aL5,
                                          double[] aB0, double[] aB1, double[] aB2,
                                          double[] aB3, double[] aB4, double[] aB5,
                                          double[] aR0, double[] aR1, double[] aR2,
                                          double[] aR3, double[] aR4, double[] aR5) {
        double t1[] = new double[3];
        double t2[] = new double[3];
        double theL, theB, theR;

        theL = getLongitude(aTime, aL0, aL1, aL2, aL3, aL4, aL5);
        theB = getLatitude(aTime, aB0, aB1, aB2, aB3, aB4, aB5);
        theR = getDistance(aTime, aR0, aR1, aR2, aR3, aR4, aR5);

        t1[0] = theR * Math.cos(theL) * Math.cos(theB);
        t1[1] = theR * Math.sin(theL) * Math.cos(theB);
        t1[2] = theR * Math.sin(theB);

        return new AstronomyCoordinates(t1).convertEclipticToMean(aTime).convertMeanToJ2000(aTime);

    }

    /**
     * Calculate the heliocentric latitude in rad from the VSPO87 polynomial.
     * <p/>
     * <p>The mean latitude for the ecliptic and equinox of date is
     * <p/>
     * <p>&beta; = &sum;<SUB>j</SUB>[t<SUP>j</SUP>
     * &sum;<SUB>i</SUB>{A<SUB>Bji</SUB>
     * cos(B<SUB>Bji</SUB> + C<SUB>Bji</SUB> t)}]
     *
     * @param aTime The time for which to calculate the latitude.
     * @param aB0   The B0 coefficients.
     * @param aB1   The B1 coefficients.
     * @param aB2   The B2 coefficients.
     * @param aB3   The B3 coefficients.
     * @param aB4   The B4 coefficients.
     * @param aB5   The B5 coefficients.
     */
    public final double getLatitude(JulianTime aTime,
                                    double[] aB0, double[] aB1, double[] aB2,
                                    double[] aB3, double[] aB4, double[] aB5) {

        double theTau, theB0, theB1, theB2, theB3, theB4, theB5, theB;
        int i;

        theTau = (aTime.getJulianEphemerisDay() - 1545.) / 365250.;

        theB0 = 0.;
        for (i = 0; 0. < aB0[3 * i]; i++) {
            theB0 += aB0[3 * i] * Math.cos(aB0[3 * i + 1] + aB0[3 * i + 2] * theTau);
        }
        theB1 = 0.;
        for (i = 0; 0. < aB1[3 * i]; i++) {
            theB1 += aB1[3 * i] * Math.cos(aB1[3 * i + 1] + aB1[3 * i + 2] * theTau);
        }
        theB2 = 0.;
        for (i = 0; 0. < aB2[3 * i]; i++) {
            theB2 += aB2[3 * i] * Math.cos(aB2[3 * i + 1] + aB2[3 * i + 2] * theTau);
        }
        theB3 = 0.;
        for (i = 0; 0. < aB3[3 * i]; i++) {
            theB3 += aB3[3 * i] * Math.cos(aB3[3 * i + 1] + aB3[3 * i + 2] * theTau);
        }
        theB4 = 0.;
        for (i = 0; 0. < aB4[3 * i]; i++) {
            theB4 += aB4[3 * i] * Math.cos(aB4[3 * i + 1] + aB4[3 * i + 2] * theTau);
        }
        theB5 = 0.;
        for (i = 0; 0. < aB5[3 * i]; i++) {
            theB5 += aB5[3 * i] * Math.cos(aB5[3 * i + 1] + aB5[3 * i + 2] * theTau);
        }

        theB = theB5;
        theB *= theTau;
        theB += theB4;
        theB *= theTau;
        theB += theB3;
        theB *= theTau;
        theB += theB2;
        theB *= theTau;
        theB += theB1;
        theB *= theTau;
        theB += theB0;
        theB /= 1E8;

        return theB;

    }

    /**
     * Calculate the heliocentric longitude in rad from the VSPO87 polynomial.
     * <p/>
     * <p>The mean longitude for the ecliptic and equinox of date is
     * <p/>
     * <p>&lambda; = &sum;<SUB>j</SUB>[t<SUP>j</SUP>
     * &sum;<SUB>i</SUB>{A<SUB>Lji</SUB>
     * cos(B<SUB>Lji</SUB> + C<SUB>Lji</SUB> t)}]
     *
     * @param aTime The time for which to calculate the longitude.
     * @param aL0   The L0 coefficients.
     * @param aL1   The L1 coefficients.
     * @param aL2   The L2 coefficients.
     * @param aL3   The L3 coefficients.
     * @param aL4   The L4 coefficients.
     * @param aL5   The L5 coefficients.
     */
    public final double getLongitude(JulianTime aTime,
                                     double[] aL0, double[] aL1, double[] aL2,
                                     double[] aL3, double[] aL4, double[] aL5) {

        double theTau, theL0, theL1, theL2, theL3, theL4, theL5, theL;
        int i;

        theTau = (aTime.getJulianEphemerisDay() - 1545.) / 365250.;

        theL0 = 0.;
        for (i = 0; 0. < aL0[3 * i]; i++) {
            theL0 += aL0[3 * i] * Math.cos(aL0[3 * i + 1] + aL0[3 * i + 2] * theTau);
        }
        theL1 = 0.;
        for (i = 0; 0. < aL1[3 * i]; i++) {
            theL1 += aL1[3 * i] * Math.cos(aL1[3 * i + 1] + aL1[3 * i + 2] * theTau);
        }
        theL2 = 0.;
        for (i = 0; 0. < aL2[3 * i]; i++) {
            theL2 += aL2[3 * i] * Math.cos(aL2[3 * i + 1] + aL2[3 * i + 2] * theTau);
        }
        theL3 = 0.;
        for (i = 0; 0. < aL3[3 * i]; i++) {
            theL3 += aL3[3 * i] * Math.cos(aL3[3 * i + 1] + aL3[3 * i + 2] * theTau);
        }
        theL4 = 0.;
        for (i = 0; 0. < aL4[3 * i]; i++) {
            theL4 += aL4[3 * i] * Math.cos(aL4[3 * i + 1] + aL4[3 * i + 2] * theTau);
        }
        theL5 = 0.;
        for (i = 0; 0. < aL5[3 * i]; i++) {
            theL5 += aL5[3 * i] * Math.cos(aL5[3 * i + 1] + aL5[3 * i + 2] * theTau);
        }

        theL = theL5;
        theL *= theTau;
        theL += theL4;
        theL *= theTau;
        theL += theL3;
        theL *= theTau;
        theL += theL2;
        theL *= theTau;
        theL += theL1;
        theL *= theTau;
        theL += theL0;
        theL /= 1E8;
        theL = MiscellaneousUtils.normalizeAngleAt180(theL);

        return theL;

    }

}
