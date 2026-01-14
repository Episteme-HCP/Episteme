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

package org.jscience.astronomy.solarsystem;

import org.jscience.astronomy.Rings;
import org.jscience.astronomy.solarsystem.naturalsatellites.SolarSystemNaturalSatellite;
import org.jscience.astronomy.solarsystem.naturalsatellites.SolarSystemNaturalSatellitesFactory;
import org.jscience.astronomy.solarsystem.planets.SolarSystemPlanetaryBodiesFactory;
import org.jscience.astronomy.solarsystem.planets.SolarSystemPlanetaryBody;


/**
 * The KnownNaturalSatellitesFactory class provides support for the natural
 * satellites from our star sytstem.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//find more information at http://www.vectorsite.net/taxpl.html
//data
//http://ssd.jpl.nasa.gov/eph_info.html
//ftp://ssd.jpl.nasa.gov/pub/eph/export/
public class KnownNaturalSatellitesFactory extends Object {
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody MERCURY = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "mercury");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody VENUS = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "venus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody EARTH = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "earth");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody MARS = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "mars");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody JUPITER = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "jupiter");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody SATURN = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "saturn");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody URANUS = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "uranus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody NEPTUN = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "neptun");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemPlanetaryBody PLUTO = SolarSystemPlanetaryBodiesFactory.getSolarSystemPlanetaryBody(
            "pluto");

    //earth
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite MOON = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "moon");

    //mars
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PHOBOS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "phobos");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite DEIMOS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "deimos");

    //jupiter
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite METIS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "metis");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ADRASTEA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "adrastea");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite AMALTHEA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "amalthea");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite THEBE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "thebe");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite IO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "io");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite EUROPA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "europa");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite GANYMEDE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "ganymede");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CALLISTO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "callisto");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite LEDA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "leda");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite HIMALIA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "himalia");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite LYSITHEA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "lysithea");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ELARA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "elara");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ANANKE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "ananke");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CARME = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "carme");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PASIPHAE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "pasiphae");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite SINOPE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "sinope");

    //saturn
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PAN = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "pan");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ATLAS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "atlas");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PROMETHEUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "prometheus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PANDORA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "pandora");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite EPIMETHEUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "epimetheus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite JANUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "janus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite MIMAS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "mimas");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ENCELADUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "enceladus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite TETHYS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "tethys");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite TELESTO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "telesto");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CALYPSO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "calypso");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite DIONE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "dione");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite HELENE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "helene");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite RHEA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "rhea");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite TITAN = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "titan");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite HYPERION = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "hyperion");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite IAPETUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "iapetus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PHOEBE = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "phoebe");

    //uranus
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CORDELIA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "cordelia");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite OPHELIA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "ophelia");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite BIANCA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "bianca");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CRESSIDA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "cressida");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite DESDEMONA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "desdemona");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite JULIET = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "juliet");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PORTIA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "portia");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ROSALIND = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "rosalind");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite BELINDA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "belinda");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PUCK = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "puck");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite MIRANDA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "miranda");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite ARIEL = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "ariel");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite UMBRIEL = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "umbriel");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite TITANIA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "titania");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite OBERON = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "oberon");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CALIBAN = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "caliban");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite SETEBOS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "setebos");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite SYCORAX = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "sycorax");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite STEPHANO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "stephano");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PROSPERO = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "prospero");

    //neptune
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite NAIAD = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "naiad");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite THALASSA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "thalassa");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite DESPINA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "despina");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite GALATEA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "galatea");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite LARISSA = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "larissa");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite PROTEUS = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "proteus");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite TRITON = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "triton");

    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite NEREID = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "nereid");

    //pluto
    /**
     * DOCUMENT ME!
     */
    public final static SolarSystemNaturalSatellite CHARON = SolarSystemNaturalSatellitesFactory.getSolarSystemNaturalSatellite(
            "charon");

    static {
        EARTH.addChild(MOON);

        MARS.addChild(PHOBOS);
        MARS.addChild(DEIMOS);

        JUPITER.addChild(METIS);
        JUPITER.addChild(ADRASTEA);
        JUPITER.addChild(AMALTHEA);
        JUPITER.addChild(THEBE);
        JUPITER.addChild(IO);
        JUPITER.addChild(EUROPA);
        JUPITER.addChild(GANYMEDE);
        JUPITER.addChild(CALLISTO);
        JUPITER.addChild(LEDA);
        JUPITER.addChild(HIMALIA);
        JUPITER.addChild(LYSITHEA);
        JUPITER.addChild(ELARA);
        JUPITER.addChild(ANANKE);
        JUPITER.addChild(CARME);
        JUPITER.addChild(PASIPHAE);
        JUPITER.addChild(SINOPE);

        //http://en.wikipedia.org/wiki/Rings_of_Saturn
        SATURN.addChild(new Rings("Saturn rings", 66900000, 136775000));

        SATURN.addChild(PAN);
        SATURN.addChild(ATLAS);
        SATURN.addChild(PROMETHEUS);
        SATURN.addChild(PANDORA);
        SATURN.addChild(EPIMETHEUS);
        SATURN.addChild(JANUS);
        SATURN.addChild(MIMAS);
        SATURN.addChild(ENCELADUS);
        SATURN.addChild(TETHYS);
        SATURN.addChild(TELESTO);
        SATURN.addChild(CALYPSO);
        SATURN.addChild(DIONE);
        SATURN.addChild(HELENE);
        SATURN.addChild(RHEA);
        SATURN.addChild(TITAN);
        SATURN.addChild(HYPERION);
        SATURN.addChild(IAPETUS);
        SATURN.addChild(PHOEBE);

        URANUS.addChild(CORDELIA);
        URANUS.addChild(OPHELIA);
        URANUS.addChild(BIANCA);
        URANUS.addChild(CRESSIDA);
        URANUS.addChild(DESDEMONA);
        URANUS.addChild(JULIET);
        URANUS.addChild(PORTIA);
        URANUS.addChild(ROSALIND);
        URANUS.addChild(BELINDA);
        URANUS.addChild(PUCK);
        URANUS.addChild(MIRANDA);
        URANUS.addChild(ARIEL);
        URANUS.addChild(UMBRIEL);
        URANUS.addChild(TITANIA);
        URANUS.addChild(OBERON);
        URANUS.addChild(CALIBAN);
        URANUS.addChild(SETEBOS);
        URANUS.addChild(SYCORAX);
        URANUS.addChild(STEPHANO);
        URANUS.addChild(PROSPERO);

        NEPTUN.addChild(NAIAD);
        NEPTUN.addChild(THALASSA);
        NEPTUN.addChild(DESPINA);
        NEPTUN.addChild(GALATEA);
        NEPTUN.addChild(LARISSA);
        NEPTUN.addChild(PROTEUS);
        NEPTUN.addChild(TRITON);
        NEPTUN.addChild(NEREID);

        PLUTO.addChild(CHARON);
    }

    /**
     * Creates a new KnownNaturalSatellitesFactory object.
     */
    private KnownNaturalSatellitesFactory() {
    }
}
