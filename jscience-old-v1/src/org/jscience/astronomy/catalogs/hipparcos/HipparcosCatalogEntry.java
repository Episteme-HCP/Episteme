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

import java.net.URL;

import java.text.DecimalFormat;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
/**
 * A class to represent one entry in the hipparcos catalog not all fields
 * are put in here just the ones used elsewhere could easily add them all but
 * then chewing up memory
 */
public class HipparcosCatalogEntry {
    /** DOCUMENT ME! */
    private static DecimalFormat lform = new DecimalFormat("000.#########");

    /** DOCUMENT ME! */
    private static DecimalFormat decform = new DecimalFormat(
            " 00.#########;-00.########");

    /** DOCUMENT ME! */
    private static DecimalFormat sform = new DecimalFormat("00.###;-0.###");

    /** DOCUMENT ME! */
    public static DecimalFormat mform = new DecimalFormat(" 000.###;-000.###");

    /** DOCUMENT ME! */
    private String type; // H0 or T0 contains H or T

    /** DOCUMENT ME! */
    private String id; // H1 ot T1

    /** DOCUMENT ME! */
    private double mag; // H5

    /** DOCUMENT ME! */
    private double alpha; // H8

    /** DOCUMENT ME! */
    private double delta; /// H9

    /** DOCUMENT ME! */
    private double paralax; // H11

    /** DOCUMENT ME! */
    private double muAlpha; //H12

    /** DOCUMENT ME! */
    private double muDelta; //H13

    /** DOCUMENT ME! */
    private double b_v; //H37

    /** DOCUMENT ME! */
    private boolean inHIPnTYC = false;

    /** DOCUMENT ME! */
    private boolean inHIP = false;

/**
     * Creates a new Star object.
     */
    public HipparcosCatalogEntry() {
        id = null;
    }

    /* copy constructor */
    public HipparcosCatalogEntry(HipparcosCatalogEntry copy) {
        type = copy.type;
        id = copy.id;
        mag = copy.mag;
        alpha = copy.alpha;
        delta = copy.delta;
        paralax = copy.paralax;
        muAlpha = copy.muAlpha;
        muDelta = copy.muDelta;
        inHIPnTYC = copy.inHIPnTYC;
        inHIP = copy.inHIP;
        b_v = copy.b_v;
    }

/**
     * COntruct the star from the delimetd line as it appears in the catalog
     *
     * @param str DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public HipparcosCatalogEntry(String str) throws Exception {
        DelimitedLine line = new DelimitedLine(str, '|');

        //System.out.println("Got - "+str);
        String skip;
        type = line.getNextString();
        id = line.getNextString();
        skip = line.getNextString(); //H2
        skip = line.getNextString(); //H3
        skip = line.getNextString(); //H4
        mag = line.getNextDouble(); //H5
        skip = line.getNextString(); //H6
        skip = line.getNextString(); //H7
        alpha = line.getNextDouble();
        delta = line.getNextDouble();
        skip = line.getNextString(); //H10
        paralax = line.getNextDouble(); //H11

        try {
            muAlpha = line.getNextDouble();
        } catch (Exception e) {
        }

        try {
            muDelta = line.getNextDouble(); //h13
        } catch (Exception e) {
        }

        int i = 13;

        if (type.startsWith("T")) { // get T31

            try {
                for (i = 13; i < 30; i++) {
                    try {
                        skip = line.getNextString();
                    } catch (Exception e) {
                        System.out.println("Skip Failed " + e);
                    }
                }

                i++;

                String tmpStr = line.getNextString(); //extractHipNo from T31
                int tmp = new Integer(tmpStr).intValue();
                inHIPnTYC = (tmp >= 1);
            } catch (Exception e) {
                //System.out.println ("Convert Failed "+ e);
                inHIPnTYC = false;
            }
        } else {
            inHIP = true;
        }

        // skip to H37  b-v
        while (i < 36) {
            try {
                skip = line.getNextString();
                i++;
            } catch (Exception e) {
                System.out.println("Skip Failed " + e);
            }
        }

        b_v = line.getNextDouble(); // H37
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String shortInfo() {
        String ret = null;

        if (type.startsWith("T")) {
            ret = new String("TYC" + id);
        } else {
            ret = new String("HIP" + id);
        }

        ret += " V=";
        ret += mag;

        return (ret);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String header() {
        return "HIP/TYC        |Vmag |alpha       |delta    |parallax|mu_alpha|mu_delta|B-V";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMag() {
        return mag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDelta() {
        return delta;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getParalax() {
        return paralax;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMuAlpha() {
        return muAlpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMuDelta() {
        return muDelta;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getB_V() {
        return b_v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inHIP() {
        return inHIP;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = null;

        if (type.startsWith("T")) {
            ret = new String("TYC" + id);
        } else {
            ret = new String("HIP" + id);
        }

        ret = buf(ret, 15);
        ret += "|";
        ret += buf(sform.format(mag), 5);
        ret += "|";
        ret += buf(lform.format(alpha), 12);
        ret += "|";
        ret += buf(decform.format(delta), 12);
        ret += "|";
        ret += buf(mform.format(paralax), 7);
        ret += "|";
        ret += buf(mform.format(muAlpha), 7);
        ret += "|";
        ret += buf(mform.format(muDelta), 7);
        ret += "|";
        ret += sform.format(b_v);

        return (ret);
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String buf(String in, int len) {
        String ret = in;

        while (ret.length() < len)
            ret += " ";

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public URL makeUrl() {
        String theUrl = HipparcosProperties.getProperty("hipurl") + "?";

        try {
            if (getType().startsWith("T")) {
                DelimitedLine tycnos = new DelimitedLine(id, ' ');
                theUrl += ("tyc1=" + tycnos.getNextInt());
                theUrl += ("&tyc2=" + tycnos.getNextInt());
                theUrl += ("&tyc3=" + tycnos.getNextInt());
            } else {
                theUrl += ("hipId=" + getId());
            }

            URL url = new URL(theUrl);

            return url;
        } catch (Exception e) {
            System.err.println(e);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param years DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMuAlpha(int years) {
        return (years * ((muAlpha) / 3600000));
    }

    /**
     * DOCUMENT ME!
     *
     * @param years DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMuDelta(int years) {
        return (years * ((muDelta) / 3600000));
    }
}
