package org.jscience.astronomy.solarsystem.naturalsatellites;

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
public final class SolarSystemNaturalSatellitesFactory {
    /** DOCUMENT ME! */
    private static final WeakHashMap table = new WeakHashMap();

/**
     * Creates a new SolarSystemPlanetaryBodiesFactory object.
     */
    private SolarSystemNaturalSatellitesFactory() {
    }

    /**
     * Returns an element.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SolarSystemNaturalSatellite getSolarSystemNaturalSatellite(
        String name) {
        name = name.toLowerCase();

        Object solarSystemNaturalSatellite = table.get(name);

        if (solarSystemNaturalSatellite == null) {
            solarSystemNaturalSatellite = loadSolarSystemNaturalSatellite(name +
                    ".xml");
            table.put(name, solarSystemNaturalSatellite);
        }

        return (SolarSystemNaturalSatellite) solarSystemNaturalSatellite;
    }

    /**
     * Loads an element from its XML resource.
     *
     * @param resname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static SolarSystemNaturalSatellite loadSolarSystemNaturalSatellite(
        String resname) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(SolarSystemNaturalSatellite.class.getResourceAsStream(
                        resname));
            Node root = doc.getDocumentElement();
            NodeList nl = root.getChildNodes();
            String name = findStringValue(nl, "name");
            SolarSystemNaturalSatellite astronomicalBody = new SolarSystemNaturalSatellite(name);
            astronomicalBody.setRadius(findDoubleValue(nl, "diameter") / 2);
            astronomicalBody.setRotationPeriod(findDoubleValue(nl,
                    "rotationperiod"));
            astronomicalBody.setOrbitPeriod(findDoubleValue(nl, "orbitperiod"));
            astronomicalBody.setMeanOrbitVelocity(findDoubleValue(nl,
                    "meanorbitvelocity"));
            astronomicalBody.setOrbitEccentricity(findDoubleValue(nl,
                    "orbiteccentricity"));
            astronomicalBody.setOrbitInclinationToEcliptic(findDoubleValue(nl,
                    "orbitinclinationtoecliptic"));
            astronomicalBody.setInclinationOfEquatorToOrbit(findDoubleValue(
                    nl, "inclinationofequatortoorbit"));
            astronomicalBody.setVolume(findDoubleValue(nl, "volume"));
            astronomicalBody.setDistanceToParent(findDoubleValue(nl, "distance"));
            astronomicalBody.setMass(findDoubleValue(nl, "mass"));
            astronomicalBody.setSurfaceGravity(findDoubleValue(nl,
                    "surfacegravity"));
            astronomicalBody.setEscapeVelocity(findDoubleValue(nl,
                    "escapevelocity"));
            astronomicalBody.setTemperature(findDoubleValue(nl,
                    "meantempatsolidsurface"));
            astronomicalBody.setAtmosphericConstitutents(findDoubleValue(nl,
                    "majoratmosphericconstitutents"));

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
}
