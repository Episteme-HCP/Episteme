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

import org.jscience.astronomy.AstronomyConstants;
import org.jscience.astronomy.Star;

import javax.media.j3d.TransformGroup;

/**
 * The Sun class provides several constants for the Sun star.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//reprinted from the code of http://www.physci.org/
public class Sun extends Star {

    /**
     * </i>Core temperature</i> in degrees celsius.
     */
    public final double CORE_TEMPERATURE = 15000000;
    /**
     * <i>Surface temperature</i> in degrees celsius.
     */
    public final double SURFACE_TEMPERATURE = 5700;

    /**
     * Described as "<i>..12 times lead</i>", where lead has a density of
     * 11.4 Gm/cc.  Expressed as Gm/cc.
     */
    public final double CORE_DENSITY = 136.8;
    /**
     * Described as "<i>..about equal to water</i>".  Expressed as Gm/cc.
     */
    public final double MEDIAN_DENSITY = 1.410;
    /**
     * Described as "<i>..1/10000 of that of air</i>".  Expressed as Gm/cc.
     */
    public final double SURFACE_DENSITY = 0;

    /**
     * The number of years for the <i>Sun Spot Cycle</i> to complete.
     */
    public final int SUN_SPOT_CYCLE = 11;

    /**
     * The <i>rotation rate</i> of the sun, in days.
     */
    public final double ROTATION_EQUATOR = 25;
    public final double ROTATION_POLES = 29;

    /**
     * <i>Mean angular diameter</i> of the Sun as seen from Earth,
     * (degrees, minutes, seconds: 31<sup>o</sup>59'3'').
     */
    public final int[] MEAN_ANGULAR_DIAMETER = {31, 59, 3};
    /**
     * The Sun is an oblate speroid due to it's rotation, this records the
     * <i>angular oblateness</i> of the sun in seconds.
     */
    public final double ANGULAR_OBLATENESS = 8e-3;

    /**
     * The <i>escape velocity to infinity</i>, or the raw velocity a body or
     * craft would need to attain in order to be able to leave the Solar
     * System.
     */
    public final double ESCAPE_VELOCITY = 6.18e+5;

    /**
     * <i>Energy output</i> in joules/sec.
     */
    public final double ENERGY_OUTPUT = 3.9e26;
    /**
     * The Suns <i>absolute magnitude</i>.
     */
    public final double ABSOLUTE_MAGNITUDE = 4.79;

    /**
     * The Sun's <i>lifespan</i> in years.
     */
    public final double LIFESPAN_IN_YEARS = 1.0e+10;

    /**
     * Minimum and maximum remaining <i>livability period on Earth</i>.
     * The estimated period between now and when the Sun matures to
     * a point that would boil away Earth's oceans.
     */
    public final double[] EARTH_LIVABILITY = {1e+8, 8e+8};

    public Sun() {

        super("Sun", new TransformGroup(), 1.990e+30, 1.392e+9 / 2, 27 * AstronomyConstants.EARTH_DAY, 0, 5.0e+9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.DAY, "Hydrogen (95%), Helium (5%)", Star.MAIN_SEQUENCE, 5780);
        createPlanet(1.392e+9, 27 * AstronomyConstants.EARTH_DAY, "sun.gif", getGroup(), true);

    }

}
