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

package org.jscience.earth;

/**
 * <p/>
 * This class represents the orientation of a surface exposed to sunlight. Its
 * class variables define the surface's position on Earth (a
 * EarthSurfacePosition object) and orientation. Its methods calculate the
 * angle of incidence of sunlight falling on it.
 * </p>
 */

//you should plug in data from org.jscience.geography.coordinates.Direction  subclass
public class EarthSurfaceOrientation {
    /**
     * The EarthSurfacePosition object that defines the location of this
     * orientation.
     */
    private EarthSurfacePosition location;

    // surface

    /**
     * Azimuth of the surface, in degrees, measured from true south (East is
     * negative, West is positive).
     */
    private double surface_azimuth;

    /**
     * Tilt angle of the surface, in degrees. Vertical surface is 90.
     */
    private double surface_tilt;

    /**
     * Linked List of surfaces with this orientation.
     *
     * @param location DOCUMENT ME!
     * @param surface_azimuth DOCUMENT ME!
     * @param surface_tilt DOCUMENT ME!
     */

    //private Vector surfaces;
    //--------------------  Constructors  -----------------------------

    /**
     * <p/>
     * Create an EarthSurfaceOrientation object, which represents the
     * orientation of a surface exposed to sunlight. The parameters defining
     * the orientation are:
     * <p/>
     * <ul>
     * <li>
     * a EarthSurfacePosition object, specifying the surface's location on
     * Earth,
     * </li>
     * <li>
     * surface azimuth(in degrees) -  measured from South, with Eastward
     * deviation negative and Westward deviation positive,
     * </li>
     * <li>
     * and its surface tilt from the horizontal (in degrees).
     * </li>
     * </ul>
     * </p>
     *
     * @param location        DOCUMENT ME!
     * @param surface_azimuth DOCUMENT ME!
     * @param surface_tilt    DOCUMENT ME!
     */
    public EarthSurfaceOrientation(EarthSurfacePosition location,
                                   double surface_azimuth, double surface_tilt) {
        this.location = location;

        //    location.orientations.add(this);
        this.surface_azimuth = surface_azimuth;
        this.surface_tilt = surface_tilt;

        //this.name = name;
        //surfaces = new Vector();
        //    System.out.println("[SOLorientation]: latitude = " + latitude + " longitude = " + longitude +
        //		       " time zone = " + time_zone + " surface_azimuth = " + surface_azimuth +
        //	       " surface_tilt = " + surface_tilt);
    }

    //----------------------------- Cos of Angle of Incidence -------------

    /**
     * Returns cosine of angle between solar radiation and normal to surface,
     * given solar time, in seconds, and day number.
     *
     * @param daynum     DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getCosTheta(int daynum, int solar_time) {
        double decl = location.getDeclination(daynum);
        double alt = location.getSolarAltitude(decl, solar_time);
        double azi = location.getSolarAzimuth(decl, solar_time);
        double rel_azi = azi - surface_azimuth;

        double cosIncidentAngle = (Math.cos(EarthSurfacePosition.toRadians(alt)) * Math.cos(EarthSurfacePosition.toRadians(
                rel_azi)) * Math.sin(EarthSurfacePosition.toRadians(
                surface_tilt))) +
                (Math.sin(EarthSurfacePosition.toRadians(alt)) * Math.cos(EarthSurfacePosition.toRadians(
                        surface_tilt)));

        //System.out.println("[getCosTheta]:decl:"+decl+" alt:"+alt+" azi:"+
        //azi+" rel_azi:"+rel_azi+" surface_tilt"+surface_tilt+"cos theta="+cosIncidentAngle);
        return (Math.max(0.0, cosIncidentAngle));
    }

    //----------------------------- Angle of Incidence ----------------------

    /**
     * Returns angle between solar radiation and normal to surface, given solar
     * time, in seconds, and day number.
     *
     * @param daynum     DOCUMENT ME!
     * @param solar_time DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public double getTheta(int daynum, int solar_time) {
        return (EarthSurfacePosition.toDegrees(Math.acos(getCosTheta(daynum,
                solar_time))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EarthSurfacePosition getEarthSurfacePosition() {
        return location;
    }

    //public void setEarthSurfacePosition (EarthSurfacePosition location){
    //   this.location= location;
    //}
    public double getSurfaceAzimuth() {
        return surface_azimuth;
    }

    // public void setSurfaceAzimuth(double surface_azimuth){
    //   this.surface_azimuth= surface_azimuth;
    //}
    public double getSurfaceTilt() {
        return surface_tilt;
    }

    //public void setSurfaceTilt(double surface_tilt){
    //   this.surface_tilt= surface_tilt;
    // }
}
