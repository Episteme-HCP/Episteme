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
