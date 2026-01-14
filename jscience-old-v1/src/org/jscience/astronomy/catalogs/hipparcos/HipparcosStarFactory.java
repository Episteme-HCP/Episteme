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

package org.jscience.astronomy.catalogs.hipparcos;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.NoSuchElementException;


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
public class HipparcosStarFactory {
    /** DOCUMENT ME! */
    private boolean hipOnly = false;

    /** DOCUMENT ME! */
    private double alpha = 0;

    /** DOCUMENT ME! */
    private double delta = 0;

    /** DOCUMENT ME! */
    private double vlim = 99;

    /** DOCUMENT ME! */
    private double d = 0;

    /** DOCUMENT ME! */
    private BufferedReader dstream = null;

    /** DOCUMENT ME! */
    private boolean finished = false;

    /** DOCUMENT ME! */
    private boolean hipDone = false;

    /** DOCUMENT ME! */
    private boolean opened = false;

    /** DOCUMENT ME! */
    private boolean disk = false;

    /** DOCUMENT ME! */
    private String[] catprogs = { "shipmainra", "stycmainra" };

/**
     * Creates a new StarFactory object.
     *
     * @param vlim    DOCUMENT ME!
     * @param hipOnly DOCUMENT ME!
     */
    protected HipparcosStarFactory(double vlim, boolean hipOnly) {
        this.vlim = vlim;
        this.hipOnly = hipOnly;
    }

/**
     * Creates a new StarFactory object.
     *
     * @param alpha DOCUMENT ME!
     * @param delta DOCUMENT ME!
     * @param d     DOCUMENT ME!
     */
    public HipparcosStarFactory(double alpha, double delta, double d) {
        this.alpha = alpha;
        this.delta = delta;
        this.d = d;
        finished = !openStream();
    }

/**
     * Creates a new StarFactory object.
     *
     * @param alpha   DOCUMENT ME!
     * @param delta   DOCUMENT ME!
     * @param d       DOCUMENT ME!
     * @param vlim    DOCUMENT ME!
     * @param hipOnly DOCUMENT ME!
     */
    public HipparcosStarFactory(double alpha, double delta, double d,
        double vlim, boolean hipOnly) {
        this.hipOnly = hipOnly;
        this.alpha = alpha;
        this.delta = delta;
        this.d = d;
        this.vlim = vlim;
        finished = !openStream();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean openStream() {
        opened = true;
        finished = false;
        disk = HipparcosProperties.getProperty("datasource", "www")
                                  .startsWith("disk");

        if (disk) {
            return loadFromDisk(catprogs[0]);
        } else {
            if (loadFromURL()) {
                try {
                    skipToData();

                    return true;
                } catch (Exception e) {
                    System.err.println("openStream: could not skip to data " +
                        e);
                    e.printStackTrace();
                }
            }
        }

        finished = true;

        return false;
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
            cmd += (new Double(alpha).toString() + " ");
            cmd += (new Double(delta).toString() + " ");
            cmd += new Double(d).toString();

            //if (Constants.verbose > 1) System.out.println("Running "+cmd+".");
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStreamReader istream = new InputStreamReader(p.getInputStream());
            dstream = new BufferedReader(istream);

            return true;
        } catch (Exception e) {
            System.err.println("Some problem starting cat progs");
            e.printStackTrace();

            return false;
        }
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
            theUrl += "?noLinks=1&raDecim=";
            theUrl += new Double(alpha).toString();
            theUrl += "&decDecim=";
            theUrl += new Double(delta).toString();
            theUrl += "&box=";
            theUrl += new Double(d).toString();

            URL url = new URL(theUrl);
            InputStreamReader istream = new InputStreamReader(url.openStream());
            dstream = new BufferedReader(istream);

            //if (Constants.verbose > 1) System.out.println("opened "+theUrl+".");
        } catch (Exception e) {
            System.err.println("loadFromURL: " + e);

            return false;
        }

        return true;
    }

    /**
     * Skip down to next PRE tag in html stream
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void skipToData() throws Exception {
        //if (Constants.verbose > 3) System.out.println("Skipping to data ..") ;
        String str;
        boolean found = false;

        while (!found) { // skip to pre
            str = dstream.readLine();
            found = (str == null) || str.startsWith("<pre>") ||
                str.startsWith("<PRE>");
        }

        str = dstream.readLine(); // skip header
    }

    /**
     * keep getting stars until none left
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public HipparcosCatalogEntry getNext() throws NoSuchElementException {
        if (!opened) {
            openStream();
        }

        if (finished) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        String str;
        HipparcosCatalogEntry star = null;

        while (!found) {
            str = null;

            try {
                str = dstream.readLine();

                //if (Constants.verbose > 5 ) System.out.println(str);
            } catch (Exception e) {
                //if (Constants.verbose > 5 ) e.printStackTrace();
            }

            if (str == null) {
                if (disk && (catprogs[1] != null) && !hipDone && !hipOnly) {
                    loadFromDisk(catprogs[1]);
                    hipDone = true;

                    try {
                        str = dstream.readLine();
                    } catch (Exception tyce) {
                        tyce.printStackTrace();
                        throw new NoSuchElementException();
                    }
                } else {
                    finished = true;

                    try {
                        dstream.close();
                    } catch (Exception e) {
                    }

                    throw new NoSuchElementException();
                }
            } else {
                if (str.startsWith("</pre>") || str.startsWith("</PRE>")) {
                    if (hipDone || hipOnly) {
                        finished = true;

                        try {
                            dstream.close();
                        } catch (Exception e) {
                        }

                        throw new NoSuchElementException();
                    } else {
                        try {
                            skipToData();
                            str = dstream.readLine();
                        } catch (Exception e) {
                            System.err.println("StarFactory:header " + e);
                            throw new NoSuchElementException();
                        }

                        hipDone = true;
                    }
                }
            }

            try {
                star = new HipparcosCatalogEntry(str);
                found = true;
            } catch (Exception e) {
                //if (Constants.verbose > 3) System.err.println("getNext: Unusable Star "+e);
            }
        }

        //if (Constants.verbose > 3) System.out.println("got: "+star);
        return star;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getCatprogs() {
        return catprogs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVlim() {
        return vlim;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected BufferedReader getDstream() {
        return dstream;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dstream DOCUMENT ME!
     */
    protected void setDstream(BufferedReader dstream) {
        this.dstream = dstream;
    }
}
