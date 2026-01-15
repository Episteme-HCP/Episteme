package org.jscience.astronomy.catalogs.hipparcos;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
/**
 * For given alpha delta and d get all stars from the database in that
 * area. Optional vlim may be given and hipOnly.
 */
public class HipparcosRadiusStarFactory extends HipparcosStarFactory {
    /** DOCUMENT ME! */
    private double x;

    /** DOCUMENT ME! */
    private double y;

    /** DOCUMENT ME! */
    private double z;

    /** DOCUMENT ME! */
    private double rad;

/**
     * Creates a new StarFactory3D object.
     *
     * @param x   DOCUMENT ME!
     * @param y   DOCUMENT ME!
     * @param z   DOCUMENT ME!
     * @param rad DOCUMENT ME!
     */
    public HipparcosRadiusStarFactory(double x, double y, double z, double rad) {
        super(99, true);
        this.x = x;
        this.y = y;
        this.z = z;
        this.rad = rad;
        getCatprogs()[0] = "shipmainxyz";
        getCatprogs()[1] = null;
    }

/**
     * Creates a new StarFactory3D object.
     *
     * @param x    DOCUMENT ME!
     * @param y    DOCUMENT ME!
     * @param z    DOCUMENT ME!
     * @param rad  DOCUMENT ME!
     * @param vlim DOCUMENT ME!
     */
    public HipparcosRadiusStarFactory(double x, double y, double z, double rad,
        double vlim) {
        super(vlim, true);
        this.x = x;
        this.y = y;
        this.z = z;
        this.rad = rad;
        getCatprogs()[0] = "shipmainxyz";
        getCatprogs()[1] = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean loadFromURL() {
        try {
            String theUrl = HipparcosProperties.getProperty("hipurl");

            //if (Constants.verbose > 2) System.out.println("Got property hipurl"+theUrl+".");
            theUrl += "?noLinks=1&X=";
            theUrl += new Integer((int) x).toString();
            theUrl += "&Y=";
            theUrl += new Integer((int) y).toString();
            theUrl += "&Z=";
            theUrl += new Integer((int) z).toString();
            theUrl += "&radius=";
            theUrl += new Integer((int) rad).toString();
            theUrl += "&threshold=";
            theUrl += new Double(getVlim()).toString();

            URL url = new URL(theUrl);
            InputStreamReader istream = new InputStreamReader(url.openStream());
            setDstream(new BufferedReader(istream));

            //if (Constants.verbose > 1) System.out.println("opened "+theUrl+".");
        } catch (Exception e) {
            System.err.println("loadFromURL3d: " + e);
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param catprog DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean loadFromDisk(String catprog) {
        try {
            String bins = HipparcosProperties.getProperty("bins");
            String cmd = bins + "/" + catprog + " -tq ";
            cmd += (new Integer((int) x).toString() + " ");
            cmd += (new Integer((int) y).toString() + " ");
            cmd += (new Integer((int) z).toString() + " ");
            cmd += (new Integer((int) rad).toString() + " ");
            cmd += (new Double(getVlim()).toString() + " ");

            //if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStreamReader istream = new InputStreamReader(p.getInputStream());
            setDstream(new BufferedReader(istream));

            return true;
        } catch (Exception e) {
            System.err.println("Some problem starting cat progs");
            e.printStackTrace();

            return false;
        }
    }
}
