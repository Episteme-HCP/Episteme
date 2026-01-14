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

package org.jscience.astronomy.solarsystem.planets;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * This class provides access to the solar system's planets and natural
 * satellites (our star system).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://pds.jpl.nasa.gov/public.html (for pictures)
//http://nssdc.gsfc.nasa.gov/planetary/factsheet/ (simple)
//http://atmos.nmsu.edu/jsdap/encyclopediawork.html (complex)
public final class SolarSystemPlanetaryBodiesFactory {
    /** DOCUMENT ME! */
    private static final WeakHashMap table = new WeakHashMap();

/**
     * Creates a new SolarSystemPlanetaryBodiesFactory object.
     */
    private SolarSystemPlanetaryBodiesFactory() {
    }

    /**
     * Returns an element.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SolarSystemPlanetaryBody getSolarSystemPlanetaryBody(
        String name) {
        name = name.toLowerCase();

        Object solarSystemPlanetaryBody = table.get(name);

        if (solarSystemPlanetaryBody == null) {
            solarSystemPlanetaryBody = loadSolarSystemPlanetaryBody(name +
                    ".xml");
            table.put(name, solarSystemPlanetaryBody);
        }

        return (SolarSystemPlanetaryBody) solarSystemPlanetaryBody;
    }

    /**
     * Loads an element from its XML resource.
     *
     * @param resname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static SolarSystemPlanetaryBody loadSolarSystemPlanetaryBody(
        String resname) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(SolarSystemPlanetaryBody.class.getResourceAsStream(
                        resname));
            Node root = doc.getDocumentElement();
            NodeList nl = root.getChildNodes();
            String name = findStringValue(nl, "name");
            SolarSystemPlanetaryBody astronomicalBody = new SolarSystemPlanetaryBody(name);
            astronomicalBody.setMass(findDoubleValue(nl, "mass"));
            astronomicalBody.setVolume(findDoubleValue(nl, "volume"));
            astronomicalBody.setEquatorialRadius(findDoubleValue(nl,
                    "equatorial-radius"));
            astronomicalBody.setPolarRadius(findDoubleValue(nl, "polar-radius"));
            astronomicalBody.setRadius(findDoubleValue(nl,
                    "volumetric-mean-radius"));
            astronomicalBody.setEllipticity(findDoubleValue(nl, "ellipticity"));
            astronomicalBody.setMeanDensity(findDoubleValue(nl, "mean-density"));
            astronomicalBody.setSurfaceGravity(findDoubleValue(nl,
                    "surface-gravity"));
            astronomicalBody.setSurfaceAcceleration(findDoubleValue(nl,
                    "surface-acceleration"));
            astronomicalBody.setEscapeVelocity(findDoubleValue(nl,
                    "escape-velocity"));
            astronomicalBody.setGM(findDoubleValue(nl, "GM"));
            astronomicalBody.setBondAlbedo(findDoubleValue(nl, "bond-albedo"));
            astronomicalBody.setVisualGeometricAlbedo(findDoubleValue(nl,
                    "visual-geometric-albedo"));
            astronomicalBody.setVisualMagnitude(findDoubleValue(nl,
                    "visual-magnitude"));
            astronomicalBody.setSolarIrradiance(findDoubleValue(nl,
                    "solar-irradiance"));
            astronomicalBody.setBlackBodyTemperature(findDoubleValue(nl,
                    "black-body-temperature"));
            astronomicalBody.setMomentOfInertia(findDoubleValue(nl,
                    "moment-of-inertia"));
            astronomicalBody.setJ2(findDoubleValue(nl, "J2"));
            astronomicalBody.setNumberOfNaturalSatellites(findIntegerValue(nl,
                    "number-of-natural-satellites"));
            astronomicalBody.setPlanetaryRingSystem(findBooleanValue(nl,
                    "planetary-ring-system"));
            astronomicalBody.setSemiMajorAxis(findDoubleValue(nl,
                    "semi-major-axis"));
            astronomicalBody.setSiderealOrbitPeriod(findDoubleValue(nl,
                    "sidereal-orbit-period"));
            astronomicalBody.setTropicalOrbitPeriod(findDoubleValue(nl,
                    "tropical-orbit-period"));
            astronomicalBody.setPerihelion(findDoubleValue(nl, "perihelion"));
            astronomicalBody.setAphelion(findDoubleValue(nl, "aphelion"));
            astronomicalBody.setSynodicPeriod(findDoubleValue(nl,
                    "synodic-period"));
            astronomicalBody.setMeanOrbitalVelocity(findDoubleValue(nl,
                    "mean-orbital-velocity"));
            astronomicalBody.setMaximumOrbitalVelocity(findDoubleValue(nl,
                    "maximum-orbital-velocity"));
            astronomicalBody.setMinimumOrbitalVelocity(findDoubleValue(nl,
                    "minimum-orbital-velocity"));
            astronomicalBody.setOrbitInclination(findDoubleValue(nl,
                    "orbit-inclination"));
            astronomicalBody.setOrbitEccentricity(findDoubleValue(nl,
                    "orbit-eccentricity"));
            astronomicalBody.setSiderealRotationPeriod(findDoubleValue(nl,
                    "sidereal-rotation-period"));
            astronomicalBody.setRotationPeriod(findDoubleValue(nl,
                    "length-of-day"));
            astronomicalBody.setTilt(findDoubleValue(nl, "obliquity-to-orbit"));
            astronomicalBody.setSurfacePressure(findDoubleValue(nl,
                    "surface-pressure"));
            astronomicalBody.setAverageTemperature(findDoubleValue(nl,
                    "average-temperature"));
            astronomicalBody.setAtmosphericomposition(findStringValue(nl,
                    "atmospheric-composition"));

            return astronomicalBody;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String findStringValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return item.getFirstChild().getNodeValue();
            }
        }

        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int findIntegerValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return Integer.parseInt(item.getFirstChild().getNodeValue());
            }
        }

        return 0; //Integer.NaN
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double findDoubleValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return Double.parseDouble(item.getFirstChild().getNodeValue());
            }
        }

        return Double.NaN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean findBooleanValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return (item.getFirstChild().getNodeValue().equals("Yes"));
            }
        }

        return false; //Boolean.NaN
    }
}
