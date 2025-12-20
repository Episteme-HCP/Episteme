package org.jscience.astronomy;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.physics.kinematics.ClassicalParticle3D;
import org.jscience.util.Named;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The StarSystem class provides support for star systems.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//describes complete information (objects and their position at any given time) about the StarSystem in which the current Virtual Planet is

//imagine a star system in which the main star is like Betelgeuse (500 times the radius of the sun) around which at a certain distance star systems like ours rotate around.

//a note for multiple star systems
//you should check that each children of the star can actually orbit such a star
//setting imporper parameters may lead to unstable orbits

//there should be at least a Star within a StarSystem at all time
//you can always tweak the system to bypass this condition but results are not guaranteed

//starsystem don't have a geometry by themselves but you can nevertheless extend this class or put a wandering object at the same position

//extra dust mass is NOT taken into account (as it should be fairly low compared to the mass of other objects)
public class StarSystem extends ClassicalParticle3D implements Named {

    private String name;

    private Galaxy galaxy;
    private Set stars;

    private StarBackground background;//defaults to null (no background)

    /**
     * Constructs a StarSystem.
     */
    public StarSystem(String name, Star star, Galaxy galaxy) {

        if ((name != null) && (name.length() > 0) && (stars != null) && (stars.size() > 0) && (galaxy != null)) {
            this.name = name;
            this.stars = Collections.EMPTY_SET;
            this.stars.add(star);
            this.galaxy = galaxy;
        } else
            throw new IllegalArgumentException("The StarSystem constructor can't have null arguments (and name and stars shouldn't be empty).");

    }

    public StarSystem(String name, Set stars, Galaxy galaxy) {

        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (stars != null) && (stars.size() > 0) && (galaxy != null)) {
            iterator = stars.iterator();
            valid = true;
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Star;
            }
            if (valid) {
                this.name = name;
                this.stars = stars;
                this.galaxy = galaxy;
            } else
                throw new IllegalArgumentException("The Set of stars should contain only Stars.");
        } else
            throw new IllegalArgumentException("The StarSystem constructor can't have null arguments (and name and stars shouldn't be empty).");

    }

    //compute center of mass of all stars and returns a Double3Vector that you should use as the origin for all children AstralBodies.
    public Double3Vector getCenterOfMasses() {

        //either use CenterOfMasses from JSCI.physics or
        Iterator iterator;
        Double3Vector position;
        Star currentStar;
        double[] result;

        if (stars.size() > 0) {
            iterator = stars.iterator();
            result = new double[3];
            while (iterator.hasNext()) {
                currentStar = (Star) iterator.next();
                position = (Double3Vector) currentStar.getPosition();
                result[0] += position.getPrimitiveElement(0) * currentStar.getMass();
                result[1] += position.getPrimitiveElement(1) * currentStar.getMass();
                result[2] += position.getPrimitiveElement(2) * currentStar.getMass();
            }
            result[0] = result[0] / stars.size();
            result[1] = result[1] / stars.size();
            result[2] = result[2] / stars.size();
            return new Double3Vector(result);
        } else {
            return null;
        }

    }

    //computes the sum mass of all stars
    //does not add mass from planets, asteroids... which should be very small compared to the Stars mass
    //does not actually set the mass
    public double computeMass() {

        Iterator iterator;
        double result;

        iterator = stars.iterator();
        result = 0;
        while (iterator.hasNext()) {
            result += ((Star) iterator.next()).getMass();
        }

        return result;

    }

    //compute the sum mass of all bodies
    //much slower than the previous one
    //does not actually set the mass
    public double computeExactMass() {

        Iterator iterator;
        HashSet hashSet;
        double result;

        iterator = stars.iterator();
        hashSet = new HashSet();
        result = 0;
        while (iterator.hasNext()) {
            hashSet.addAll(((Star) iterator.next()).getAllChildren());
            result += ((Star) iterator.next()).getMass();
        }
        iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            result += ((AstralBody) iterator.next()).getMass();
        }

        return result;

    }

    /**
     * returns the full human readable name for that star system.
     */
    public String getName() {

        return name;

    }

    public void setName(String name) {

        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else
            throw new IllegalArgumentException("Name can't be null or empty.");

    }

    //returns age of the oldest star
    public double computeAge() {

        Iterator iterator;
        double result;

        iterator = stars.iterator();
        result = 0;
        while (iterator.hasNext()) {
            result = Math.max(result, ((Star) iterator.next()).getAge());
        }

        return result;

    }

    public Galaxy getGalaxy() {

        return galaxy;

    }

    protected void setGalaxy(Galaxy galaxy) {

        if (galaxy != null) {
            this.galaxy = galaxy;
        } else
            throw new IllegalArgumentException("Can't set a null Galaxy.");

    }

    /**
     * Gets the current stars for this object.
     */
    public Set getStars() {

        return stars;

    }

    /**
     * Adds a star system for this object.
     */
    public void addStar(Star star) {

        if (star != null) {
            if (star.getStarSystem() == null) {
                star.setStarSystem(this);
                this.stars.add(star);
            } else
                throw new IllegalArgumentException("Can't add Star which is already in a StarSystem.");
        } else
            throw new IllegalArgumentException("Can't add a null Star.");

    }

    //refuse to remove last star
    public void removeStar(Star star) {

        if (star != null) {
            if (stars.size() > 1) {
                if (star.getStarSystem().equals(this)) {
                    star.setStarSystem(null);
                    this.stars.remove(star);
                } else
                    throw new IllegalArgumentException("Cannot remove a Star which is not a child of this StarSystem.");
            } else
                throw new IllegalArgumentException("Cannot remove last Star of a StarSystem.");
        } else
            throw new IllegalArgumentException("Can't remove a null Star.");

    }

    public void setStars(Set stars) {

        Iterator i;
        boolean valid;
        Star currentStar;

        if (stars != null) {
            if (stars.size() > 0) {
                i = stars.iterator();
                valid = true;
                while (i.hasNext() && valid) {
                    valid = i.next() instanceof Star;
                }
                if (valid) {
                    i = stars.iterator();
                    while (i.hasNext()) {
                        ((Star) i.next()).setStarSystem(this);
                    }
                    this.stars = stars;
                } else
                    throw new IllegalArgumentException("The Set of stars should contain only Stars.");
            } else
                throw new IllegalArgumentException("Cannot set an empty Star set to a StarSystem.");
        } else
            throw new IllegalArgumentException("Can't set null stars.");

    }

    public StarBackground getStarBackground() {

        return background;

    }

    public void setStarBackground(StarBackground background) {

        this.background = background;

    }

    public boolean hasPlanets() {

        return getPlanets().size() > 0;

    }

    /**
     * Gets the current planets for this star system, defined as direct NaturalSatellites children of all stars
     */
    public Set getPlanets() {

        Iterator iterator;
        HashSet subResult;
        AstralBody astralBody;
        HashSet result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getChildren());
        }
        iterator = subResult.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            astralBody = (AstralBody) iterator.next();
            if (astralBody instanceof NaturalSatellite) {
                result.add(astralBody);
            }
        }

        return result;

    }

    public Set getAllChildren() {

        Iterator iterator;
        HashSet result;
        Star currentElement;

        iterator = stars.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            currentElement = (Star) iterator.next();
            result.addAll(currentElement.getAllChildren());
            result.add(currentElement);
        }

        return result;

    }

    /**
     * Gets the current natural satellites for this star system.
     */
    public Set getAllNaturalSatellites() {

        Iterator iterator;
        Set subResult;
        AstralBody astralBody;
        Set result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getAllChildren());
        }
        iterator = subResult.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            astralBody = (AstralBody) iterator.next();
            if (astralBody instanceof NaturalSatellite) {
                result.add(astralBody);
            }
        }

        return result;

    }

    /**
     * Gets the current asteroids in this star system
     */
    public Set getAllAsteroids() {

        Iterator iterator;
        Set subResult;
        AstralBody astralBody;
        Set result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getAllChildren());
        }
        iterator = subResult.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            astralBody = (AstralBody) iterator.next();
            if (astralBody instanceof Asteroid) {
                result.add(astralBody);
            }
        }

        return result;

    }

    /**
     * Gets the current comets in this star system
     */
    public Set getAllComets() {

        Iterator iterator;
        Set subResult;
        AstralBody astralBody;
        Set result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getAllChildren());
        }
        iterator = subResult.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            astralBody = (AstralBody) iterator.next();
            if (astralBody instanceof Comet) {
                result.add(astralBody);
            }
        }

        return result;

    }

    /**
     * Gets the current artificial satellites in this star system
     */
    public Set getAllArtificialSatellites() {

        Iterator iterator;
        Set subResult;
        AstralBody astralBody;
        Set result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getAllChildren());
        }
        iterator = subResult.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            astralBody = (AstralBody) iterator.next();
            if (astralBody instanceof ArtificialSatellite) {
                result.add(astralBody);
            }
        }

        return result;

    }

    //gets through all children to check if one displays ellipses
    //there must be no circular relationship or otherwise this is an endless loop
    public boolean hasEllipses() {

        Iterator iterator;
        Set subResult;
        boolean result;

        //we probably could get the result faster
        iterator = stars.iterator();
        subResult = new HashSet();
        while (iterator.hasNext()) {
            subResult.addAll(((Star) iterator.next()).getAllChildren());
        }
        iterator = subResult.iterator();
        result = false;
        while (iterator.hasNext() && !result) {
            result = ((AstralBody) iterator.next()).hasEllipse();
        }

        return result;

    }

    /**
     * //former code
     * public void enableAllEllipses(boolean enable) {
     * <p/>
     * Iterator iterator;
     * Set subResult;
     * Ellipse ellipse;
     * <p/>
     * //we probably could get the result faster
     * iterator = stars.iterator();
     * subResult = new HashSet();
     * while (iterator.hasNext()) {
     * subResult.addAll(((Star) iterator.next()).getAllChildren());
     * }
     * iterator = subResult.iterator();
     * while (iterator.hasNext()) {
     * ellipse = ((AstralBody) iterator.next()).getEllipse();
     * if (ellipse != null) {
     * ellipse.setVisible(enable);
     * }
     * }
     * <p/>
     * }
     */

    //true if Asteroids, Comets and Artificial Satellites don't have any NaturalSatellite as children
    public boolean isStandardStarSystem() {

        Iterator iterator1;
        Iterator iterator2;
        boolean result;
        AstralBody currentAstralBody;

        iterator1 = getAllChildren().iterator();
        result = true;
        while (iterator1.hasNext() && result) {
            currentAstralBody = (AstralBody) iterator1.next();
            if ((currentAstralBody instanceof Asteroid) || (currentAstralBody instanceof Comet) || (currentAstralBody instanceof ArtificialSatellite)) {
                iterator2 = currentAstralBody.getChildren().iterator();
                while (iterator2.hasNext() && result) {
                    result = !(iterator2.next() instanceof NaturalSatellite);
                }
            }
        }

        return result;

    }

    public double computeDistance(StarSystem starSystem) {

        if (starSystem != null) {
            return Math.abs(getPosition().subtract(starSystem.getPosition()).norm());
        } else
            return 0;

    }

    public static double computeDistance(StarSystem starSystem1, StarSystem starSystem2) {

        if ((starSystem1 != null) && (starSystem2 != null)) {
            return Math.abs(starSystem1.getPosition().subtract(starSystem2.getPosition()).norm());
        } else
            return 0;

    }

//titius bode

    //a = 0.4 + 0.3�k
    //mean distance a of the planet from the Sun for planets numbered from 0 to 8

    public double computeTiTiusBode(int n) {

        int value;
        if (n < 0) {
            if (n == 0) {
                value = 0;
            } else {
                value = 1;
                for (int i = 1; i < n; i++) {
                    value = value * 2;
                }
            }
            return 0.4 + 0.3 * value;
        }
        throw new IllegalArgumentException("n must be equal to 0 or greater.");

    }

    //protected void updateShape() {

    // }

}
