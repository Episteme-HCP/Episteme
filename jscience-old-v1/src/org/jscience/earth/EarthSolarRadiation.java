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
 * <p>This class represents a surface of specific area exposed to the sun.
 * The SOLorientation object required by the constructor specifies
 * the surface's orientation and location on Earth's surface. This object
 * defines the area and ground reflectance.
 * <p>It contains methods to calculate:
 * <ul><li> diffuse irradiance
 * <li>diffuse radiation
 * <li>diffuse irradiance simple
 * <li>diffuse radiation simple
 * <li>ground reflected irradiance
 * <li>ground reflected radiation
 * <li>direct irradiance
 * <li>direct radiation
 * <li>total radiation
 */

//this class is renamed and modified after SOLsurfaceBase
public class EarthSolarRadiation {

    // Instance variables
    private double area;
    // Ground Reflectance.
    /**
     * Ground reflectance associated with this surface. Between 0 and 1.
     */
    private double rho;
    //private EarthSurfacePosition location;
    private EarthSurfaceOrientation direction;

    // Following are coefficients for calculation of Diffuse
    // irradiance deviations from simple model (from Perez).
    private static final double[] F11 = {-0.008, 0.130, 0.330, 0.568, 0.873, 1.132, 1.060, 0.678};
    private static final double[] F12 = {0.588, 0.683, 0.487, 0.187, -0.392, -1.237, -1.6, -0.327};
    private static final double[] F13 = {-0.062, -0.151, -0.221, -0.295, -0.362, -0.412, -0.359, -0.25};
    private static final double[] F21 = {-0.060, -0.019, 0.055, 0.109, 0.226, 0.288, 0.264, 0.156};
    private static final double[] F22 = {0.072, 0.066, -0.064, -0.152, -0.462, -0.823, -1.127, -1.377};
    private static final double[] F23 = {-0.022, -0.029, -0.026, -0.014, 0.001, 0.056, 0.131, 0.251};

    /**
     * Create a EarthSolarRadiation object, given
     * <ul><li>A Direction object,
     * <li>The area of the surface (m<sup>2</sup>),
     * <li>The ground reflectance.</ul>
     */

    public EarthSolarRadiation(EarthSurfaceOrientation direction, double area, double rho) {
        this.direction = direction;
        // Add this surface to the list of surfaces at this orientation.
        //orientation.surfaces.addElement(this);
        this.area = area;
        //this.location = direction.getEarthSurfacePosition();
        this.rho = rho;
    }

    //-------------------------- private methods  --------------------------

    // clearness index

    private double getClearness(double Dh, double I, double Z) {
        double kZ3 = 1.041 * Z * Z * Z;
        double clearness = (((Dh + I) / Dh) + kZ3) / (1 + kZ3);
        //System.out.println("[getClearness]: clearness=" + clearness + " Dh=" + Dh + " I=" + I + " Z=" + Z);
        return (clearness);
    }

    // brightness index - Dh = Diffuse Horizontal, h = elevation (meters), alt = solar altitude (degrees)

    private double getBrightness(double Dh, double h, double alt) {

        //calculate relative optical air mass
        double pp0 = 1.0 - (h / 10000);
        double m = pp0 / (Math.sin(EarthSurfacePosition.toRadians(alt)) + 0.50572 * Math.pow(alt + 6.07995, -1.6364));

        double brightness = Dh * m / 1353;
        //System.out.println("[getBrightness]: Dh="+Dh+" m="+m+" brightness="+brightness);
        return (brightness);
    }

    private int findF1F2Index(double Dh, double I, int daynum, int solar_time, double Z) {
        int i = 1;
        double clearness = getClearness(Dh, I, Z);
        for (; i < 9; i++) {
            if (clearness < (i)) return (i - 1);
        }
        //    System.out.println("shouldn't be here");
        return (i - 2);
    }

    //------------------------ Public methods -----------------------------------

    //--------------------- diffuse, perez ---------------------------------------

    /**
     * Return diffuse irradiance on the surface, calculated according to
     * Perez (1990) (in Watts/m<sup>2</sup>), given
     * <ul><li>Diffuse Horizontal Irradiance (Dh),
     * <li>Direct Normal Irradiance (I),
     * <li>Day Number in the year (daynum),
     * <li>Elevation at the site. </ul>
     */

    public double getDiffuseIrradiance(double Dh, double I, int daynum, int solar_time) {
        if (Dh > 0.0) {
            double alt = direction.getEarthSurfacePosition().getSolarAltitude(daynum, solar_time);
            //keep zenith angle in radians
            double Z = EarthSurfacePosition.toRadians(90.0 - alt);
            double cos_theta = direction.getCosTheta(daynum, solar_time);
            double ab = Math.max(0.0, cos_theta) / Math.max(0.087, Math.cos(Z));
            double beta = EarthSurfacePosition.toRadians(direction.getSurfaceTilt());
            //System.out.println("-----------------------------------------");
            //System.out.println("[getDiffuseIrradiance]: Dh=" + Dh + " I=" + I + " daynum=" + daynum + " stime=" +
            //		 solar_time + " Z=" + Z + " cos theta=" + cos_theta);
            int i = findF1F2Index(Dh, I, daynum, solar_time, Z);
            double brightness = getBrightness(Dh, direction.getEarthSurfacePosition().getElevation(), alt);
            double F1 = F11[i] + (F12[i] * brightness) + (F13[i] * Z);
            double F2 = F21[i] + (F22[i] * brightness) + (F23[i] * Z);
            //System.out.println(F11[i]+" "+F12[i]+" "+F13[i]+" "+F21[i]+" "+F22[i]+" "+F23[i]);
            double costerm = Math.cos(beta / 2);
            // isotropic component
            double r1 = (1 - F1) * costerm * costerm;
            // circumsolar component
            double r2 = F1 * ab;
            // horizon and zenith component
            double r3 = F2 * Math.sin(beta);
            double ratio = r1 + r2 + r3;
            double Dhsurface = Dh * ratio;
            //System.out.println("ratio="+ratio+" r1="+r1+" r2="+r2+" r3="+r3+" cos_theta= "+cos_theta);
            //System.out.println("F1="+F1+" F2="+F2+" ab="+ab+" Dh="+Dh+
            //		 " Dhsurface="+Dhsurface+" tilt="+orientation.surface_tilt);
            return (Dhsurface);
        } else {
            return (0.0);
        }
    }

    /**
     * Return diffuse radiation on the surface (in Watts),
     * calculated according to Perez (1990),
     * given
     * <ul><li>Diffuse Horizontal Irradiance (Dh),
     * <li>Direct Normal Irradiance (I),
     * <li>Day Number in the year (daynum),
     * <li>Elevation at the site. </ul>
     */

    public double getDiffuseRadiation(double Dh, double I, int daynum, int solar_time) {
        return getDiffuseIrradiance(Dh, I, daynum, solar_time) * area;
    }
    //------------------------------------------------------------------------

    //------------------- simple diffuse ----------------------

    /**
     * Return diffuse irradiance (in Watts/m<sup>2</sup>) by the simpler formula,
     * Dh(cos<sup>2</sup>(tilt)), given
     * <ul>Diffuse horizontal irradiance.</ul>
     */

    public double getDiffuseIrradianceSimple(double Dh) {
        double costerm = Math.cos(EarthSurfacePosition.toRadians((direction.getSurfaceTilt()) / 2));
        return (Dh * costerm * costerm);
    }

    /**
     * Return diffuse Radiation on the surface (in Watts) by the simpler formula,
     * Dh(cos<sup>2</sup>(tilt)), given
     * <ul>Diffuse horizontal irradiance.</ul>
     */

    public double getDiffuseRadiationSimple(double Dh) {
        return getDiffuseIrradianceSimple(Dh) * area;
    }
    //------------------------------------------------------------

    //----------------- ground reflected -------------------------------

    /**
     * Return ground reflected irradiance on the surface (in Watts/m<sup>2</sup>), given
     * <ul><li>The global horizontal irradiance (W/m<sup>2</sup>)
     * <li>The ground reflectance.</ul>
     */

    public double getGroundReflectedIrradiance(double Ih) {
        double beta = EarthSurfacePosition.toRadians(direction.getSurfaceTilt());
        double sinterm = Math.sin(beta / 2);
        return (rho * Ih * sinterm * sinterm);
    }

    /**
     * Return ground reflected radiation on the surface, given
     * <ul><li>The global horizontal irradiance (in Watts)
     * <li>The ground reflectance.</ul>
     */
    public double getGroundReflectedRadiation(double Ih) {
        return getGroundReflectedIrradiance(Ih) * area;
    }

    //------------------------------------------------------------------

    //-------------------- direct ----------------------------------------

    /**
     * Return the direct irradiance on the surface (in W/m<sup>2</sup>), given
     * <ul><li>Direct normal irradiance,
     * <li>Day number in the year.</ul>
     */

    public double getDirectIrradiance(double I, int daynum, int solar_time) {
        if (I > 0.0) {
            return (I * direction.getCosTheta(daynum, solar_time));
        } else {
            return 0.0;
        }
    }

    /**
     * Return the direct radiation on the surface (in Watts), given
     * <ul><li>Direct normal irradiance,
     * <li>Day number in the year.</ul>
     */

    public double getDirectRadiation(double I, int daynum, int solar_time) {
        if (I > 0.0) {
            return getDirectIrradiance(I, daynum, solar_time) * area;
        } else {
            return 0.0;
        }
    }
    //---------------------------------------------------------------------

    //----------------------- total -----------------------

    /**
     * Return the total irradiance (Watts/m<sup>2</sup>) on the surface, given
     * <ul><li>Diffuse horizontal irradiance (w/m<sup>2</sup>),
     * <li>Direct normal irradiance (w/m<sup>2</sup>),
     * <li>Global horizontal irradiance (w/m<sup>2</sup>),
     * <li>Day number in year,<li>Elevation (meters).</ul>
     */

    public double getTotalIncidentRadiation(double Dh, double I,
                                            double Ih, int daynum,
                                            int solar_time) {

        return (getDirectRadiation(I, daynum, solar_time) +
                getGroundReflectedRadiation(Ih) +
                getDiffuseRadiation(Dh, I, daynum, solar_time));
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public EarthSurfacePosition getEarthSurfacePosition() {
        return direction.getEarthSurfacePosition();
    }

    public EarthSurfaceOrientation getEarthSurfaceOrientation() {
        return direction;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

} 





