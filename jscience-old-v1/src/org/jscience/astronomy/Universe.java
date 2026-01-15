package org.jscience.astronomy;

import org.jscience.util.Named;

import javax.media.j3d.BranchGroup;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * The Universe class provides support for clusters of galaxies.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a collection of galaxies, no wandering body or anything between them (use a galaxy)
//there may be also other objects in space not covered here, this is dark matter and dark energy that account in fact for 99 percent of all mass
//these are invisible objects which real nature is not yet known

//you should always declare a universe even for a single star system as it is used to schedule the proper motion of objects
public class Universe extends BranchGroup implements Named {

    /**
     * Constant for the well known conventional universe.
     */
    public final static Universe CONVENTIONAL_UNIVERSE = new Universe("Conventional universe");

    private String name;
    private Set galaxies;
    private double age;//defaults to 1.38e10 years for our universe,
    //set age older than 3e6 years as you should expect it to be cool enough to hold galaxies
    private double mass;
    //current size is about 1e27 meters


    /**
     * Constructs a Universe.
     */
    public Universe(String name) {

        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.galaxies = Collections.EMPTY_SET;
            this.age = 1.38e10 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY;
            this.mass = 1e50;
        } else
            throw new IllegalArgumentException("The Universe constructor doesn't accept null or empty arguments.");

    }

    /**
     * Gets the name for this object.
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

    /**
     * Gets the current galaxies for this object.
     */
    public Set getGalaxies() {

        return galaxies;

    }

    /**
     * Adds a galaxy for this object.
     */
    public void addGalaxy(Galaxy galaxy) {

        if (galaxy != null) {
            if (galaxy.getUniverse() == null) {
                galaxy.setUniverse(this);
                this.galaxies.add(galaxy);
            } else
                throw new IllegalArgumentException("Can't add Galaxy which is already in a Universe.");
        } else
            throw new IllegalArgumentException("Can't add a null Galaxy.");

    }

    /**
     * Removes a galaxy for this object.
     */
    public void removeGalaxy(Galaxy galaxy) {

        if (galaxy != null) {
            if (galaxy.getUniverse().equals(this)) {
                galaxy.setUniverse(null);
                this.galaxies.remove(galaxy);
            } else
                throw new IllegalArgumentException("Cannot remove a Galaxy which is not a child of this Universe.");
        } else
            throw new IllegalArgumentException("Can't remove a null Galaxy.");

    }

    /**
     * Sets the current galaxies for this object. All objects in the set must be Galaxy, of course.
     */
    public void setGalaxies(Set galaxies) {

        Iterator i;
        boolean valid;

        if (galaxies != null) {
            i = galaxies.iterator();
            valid = true;
            while (i.hasNext() && valid) {
                valid = i.next() instanceof Galaxy;
            }
            if (valid) {
                i = galaxies.iterator();
                while (i.hasNext()) {
                    ((Galaxy) i.next()).setUniverse(this);
                }
                this.galaxies = galaxies;
            } else
                throw new IllegalArgumentException("A set of galaxies should contain only galaxies.");
        } else
            throw new IllegalArgumentException("Can't set null galaxies.");

    }

    public double getMass() {

        return mass;

    }

    //tries to compute the mass from all the contained star systems (counting only stars)
    //does not change mass
    public double computeMass() {

        Iterator iterator;
        double result;

        iterator = galaxies.iterator();
        result = 0;
        while (iterator.hasNext()) {
            result += ((Galaxy) iterator.next()).getMass();
        }

        return result;

    }

    public void setMass(double mass) {

        this.mass = mass;

    }

    public double getAge() {

        return age;

    }

    public void setAge(double age) {

        this.age = age;

    }

    //although we account for AstralBodies motion we do not check specific situations like for example:
    //colliding starsystems where planets are captured by another star
    //AstralBodies collisions
    //colliding galaxies where star systems are captured by another galaxy
    //no universe dynamic like big bang or expansion in general
    //star system fate like for example planets capture by dying star
    //black holes gathering at the end of universe
    //
    //the main reason for all that is that we intend to use this system for real time universes
    //and simulation of the solar system or alike star systems
    //
    //you can nevertheless write your own scheduler for different purposes
    public void compile() {

        XXXX
        //add all galaxies and everything they contain
        //shedule behaviors for astralBodies, rings, galaxies
        //use a physic simulator

        //and updates shapes

        //a note for multiple star systems
        //you should check that each children of the star can actually orbit such a star
        //setting imporper parameters may lead to unstable orbits
    }

}
