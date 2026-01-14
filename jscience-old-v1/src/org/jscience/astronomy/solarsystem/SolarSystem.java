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

import org.jscience.astronomy.Galaxy;
import org.jscience.astronomy.StarSystem;
import org.jscience.history.HistoryConstants;


/**
 * The SolarSystem class provides support for our star system as a whole.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//maps at http://gw.marketingden.com/planets/planets.html (with bump maps)
//http://www.snyderweb.com/planets/ (easy grabbing)
//http://maps.jpl.nasa.gov/ (accurate)
//http://www.mmedia.is/~bjj/ (accurate)
//general information http://solarviews.com/eng/homepage.htm (very complete with data and pictures)
//this data accounts for the current solar system
//for example moon distance from the earth is getting bigger and bigger http://www.lhs.berkeley.edu/pass/v7SlowingEarthRotation.html
//earth rotation on its own axis is slowing therefore there are less days per year http://www.cs.colorado.edu/~lindsay/creation/coral-clocks.txt
//magnetic poles are shifting http://es.ucsc.edu/~glatz/geodynamo.html
//solar light intensity, regularity is not constant
//position of the whole solar system in the galaxy (and therefore all stars positions) is changing
public class SolarSystem extends StarSystem {
    /**
     * Full human readable name for Sun.
     */
    public static final String SUN = "Sun";

    /**
     * Full human readable name for Mercury.
     */
    public static final String MERCURY = "Mercury";

    /**
     * Full human readable name for Venus.
     */
    public static final String VENUS = "Venus";

    /**
     * Full human readable name for Earth.
     */
    public static final String EARTH = "Earth";

    /**
     * Full human readable name for Mars.
     */
    public static final String MARS = "Mars";

    /**
     * Full human readable name for Jupiter.
     */
    public static final String JUPITER = "Jupiter";

    /**
     * Full human readable name for Saturn.
     */
    public static final String SATURN = "Saturn";

    /**
     * Full human readable name for Uranus.
     */
    public static final String URANUS = "Uranus";

    /**
     * Full human readable name for Neptun.
     */
    public static final String NEPTUN = "Neptun";

    /**
     * Full human readable name for Pluto.
     */
    public static final String PLUTO = "Pluto";

    /**
     * Constructs a SolarSystem.
     */
    public SolarSystem() {

        Sun sun;

        super("Solar System", HistoryConstants.SOLAR_SYSTEM_AGE, new Sun(), Galaxy.MILKY_WAY);

        KnownNaturalSatellitesFactory.getNaturalSatellites(sun);
        KnownCometsFactory.getComets(sun);
        KnownAsteroidsFactory.getAsteroids(sun);
        KnownArtificialSatellitesFactory.getArtificialSatellites(sun);
    }
}
