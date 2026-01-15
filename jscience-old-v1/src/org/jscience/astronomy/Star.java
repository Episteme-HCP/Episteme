package org.jscience.astronomy;

import org.jscience.physics.PhysicsConstants;
import org.jscience.physics.waves.BlackBody;
import org.jscience.physics.waves.ColorConverter;

import java.awt.*;

import javax.media.j3d.Group;


/**
 * The Star class provides support for the category of AstralBody of the
 * same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a big NaturalSatellite around which others revolves, usually lighten up by nuclear fusion

//you should match the temperature of the star with the color you use to render it as there is a direct relationship

//this class also accounts for planetary nebulaes (as dead stars)

//you may want to use information from org.jscience.physics.BlackBody

//see http://en.wikipedia.org/wiki/Stellar_evolution

//much, much information on stars to be found at http://www-star.st-and.ac.uk/~kw25/teaching/stars/struc11.ps.prn.pdf
//replace "11" by any number from 1 to 11
public abstract class Star extends NaturalSatellite {
    // a lot of microbodies
    /**
     * DOCUMENT ME!
     */
    public final static int FORMATION = -1;

    //our star system
    /**
     * DOCUMENT ME!
     */
    public final static int MAIN_SEQUENCE = 0;

    //stars below 0.08 solar mass never lit
    /**
     * DOCUMENT ME!
     */
    public final static int RED_GIANT = 1;

    /**
     * DOCUMENT ME!
     */
    public final static int WHITE_DWARF = 2;

    //there should be no more planets past this stage
    /**
     * DOCUMENT ME!
     */
    public final static int SUPERNOVAE = 1;

    /**
     * DOCUMENT ME!
     */
    public final static int BLACK_HOLE = 2;

    /**
     * DOCUMENT ME!
     */
    public final static int NEUTRON_STAR = 2;

    //don't forget to set the Shape color accordingly
    /**
     * DOCUMENT ME!
     */
    public final static int UNKNOWN = 0;

    /**
     * DOCUMENT ME!
     */
    public final static int M = 3000; //red, titanium oxide

    /**
     * DOCUMENT ME!
     */
    public final static int K = 4500; //orange, titanium oxide

    /**
     * DOCUMENT ME!
     */
    public final static int G = 6000; //yellow, calcium

    /**
     * DOCUMENT ME!
     */
    public final static int F = 7000; //creamy, ionized calcium

    /**
     * DOCUMENT ME!
     */
    public final static int A = 10000; //white, hydrogen

    /**
     * DOCUMENT ME!
     */
    public final static int B = 21000; //blue-white, helium

    /**
     * DOCUMENT ME!
     */
    public final static int O = 35000; //blue-white, ionized helium

    /**
     * DOCUMENT ME!
     */
    private int stage; //defaults to MAIN_SEQUENCE

    //star temperature from around 3000K to 50,000K , blue stars are hotter and red stars are cooler, defaults to 5000K
    /**
     * DOCUMENT ME!
     */
    private StarSystem starSystem;

    /**
     * Creates a new Star object.
     *
     * @param name DOCUMENT ME!
     */
    public Star(String name) {
        super(name);
        this.stage = MAIN_SEQUENCE;
        this.setTemperature(5000);
        this.starSystem = null;
    }

    /**
     * Creates a new Star object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     * @param radius DOCUMENT ME!
     * @param composition DOCUMENT ME!
     * @param stage DOCUMENT ME!
     * @param temperature DOCUMENT ME!
     */
    public Star(String name, double mass, double radius, String composition,
        int stage, double temperature) {
        super(name, mass, radius, 3600 * 24, 0, 0, composition);
        this.setRadius(radius);
        this.setComposition(composition);
        this.stage = stage;
        this.setTemperature(temperature);
        this.starSystem = null;
    }

    /**
     * Creates a new Star object.
     *
     * @param name DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass DOCUMENT ME!
     * @param radius DOCUMENT ME!
     * @param rotationPeriod DOCUMENT ME!
     * @param tilt DOCUMENT ME!
     * @param age DOCUMENT ME!
     * @param composition DOCUMENT ME!
     * @param stage DOCUMENT ME!
     * @param temperature DOCUMENT ME!
     */
    public Star(String name, Group shape, double mass, double radius,
        double rotationPeriod, double tilt, double age, String composition,
        int stage, double temperature) {
        super(name, shape, mass, radius, rotationPeriod, tilt, age, composition);
        this.stage = stage;
        this.setTemperature(temperature);
        this.starSystem = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStar() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStage() {
        return stage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stage DOCUMENT ME!
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StarSystem getStarSystem() {
        return starSystem;
    }

    //use starSystem.addStar(this)
    /**
     * DOCUMENT ME!
     *
     * @param starSystem DOCUMENT ME!
     */
    protected void setStarSystem(StarSystem starSystem) {
        if (starSystem != null) {
            this.starSystem = starSystem;
        } else {
            throw new IllegalArgumentException("Can't set a null StarSystem.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int computeSpectralClass() {
        if ((getTemperature() >= M) && (getTemperature() < K)) {
            return M;
        }

        if ((getTemperature() >= K) && (getTemperature() < G)) {
            return K;
        }

        if ((getTemperature() >= G) && (getTemperature() < F)) {
            return G;
        }

        if ((getTemperature() >= F) && (getTemperature() < A)) {
            return F;
        }

        if ((getTemperature() >= A) && (getTemperature() < B)) {
            return A;
        }

        if ((getTemperature() >= B) && (getTemperature() < O)) {
            return B;
        }

        if (getTemperature() >= O) {
            return O;
        }

        return UNKNOWN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color computeColor() {
        BlackBody blackBody;

        blackBody = new BlackBody(getTemperature());

        //http://csep10.phys.utk.edu/astr162/lect/stars/cindex.html
        return ColorConverter.getColor(blackBody.getLambdaMax() / 1e9, 1.0);
    }

    //in seconds
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeMainSequenceDuration() {
        //this is really empirical
        //http://www.physics.gmu.edu/~jevans/astr113/CourseNotes/Lec05_pt4_stellarMSEvol.htm
        //http://www.geocities.com/stellar_clusters/HRDiagrams.htm
        return (AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY * 1e9) / Math.pow(getMass() / 1.990e+30,
            2.548);
    }

    //http://en.wikipedia.org/wiki/Schwarzschild_radius
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeSchwarzschildRadius() {
        return (2 * PhysicsConstants.G * getMass()) / (PhysicsConstants.SPEED_OF_LIGHT * PhysicsConstants.SPEED_OF_LIGHT);
    }
}
