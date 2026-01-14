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

import java.io.FileInputStream;

import java.util.Properties;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
/**
 * Class to load properties from file and then be a singleton for thoose
 * properties. Has smart defaults though just in case the file is not found
 */
public class HipparcosProperties {
    /** DOCUMENT ME! */
    static Properties props = null;

    /**
     * Static initializer ...
     */
    static void init() {
        props = new Properties();

        String toolProps = System.getProperty("toolProps",
                "hipparcos.properties");

        try {
            FileInputStream in = new FileInputStream(toolProps);
            props.load(in);
            in.close();
        } catch (Exception e) {
            System.err.println(" Can not find " + toolProps +
                " use -DtoolProps=path to specify location if not in current dir");
            props.put("hipurl",
                "http://astro.estec.esa.nl/hipparcos_scripts/HIPcatalogueSearch.pl");
            props.put("browser", "netscape");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String prop) {
        if (props == null) {
            init();
        }

        return props.getProperty(prop);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String prop, String def) {
        if (props == null) {
            init();
        }

        return props.getProperty(prop, def);
    }
}
