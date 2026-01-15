package org.jscience.astronomy;

import org.jscience.physics.kinematics.ClassicalParticle3D;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.media.j3d.Group;


/**
 * The Galaxy class provides support for clusters of star systems.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a collection of star systems and/or wandering objects (for example nebulas)
//there may be also other objects in space not covered here, this is dark matter and dark energy that account in fact for 99 percent of all mass
//these are invisible objects which real nature is not yet known

//A class representing a galaxy. This is an idealized galaxy that contains only astral bodies, no dark energy or dark matter.
//As real galaxies contain many star systems, we do not expect that a Galaxy contains all actual star systems.

//this class also accounts for globular star clusters
//as well as quasars

//extra dust mass is NOT directly taken into account
//but you can also set up a mass higher (or lower) than what the mass computation returns
//also you can add any number of wandering objects and especially nebullas

//the first galaxies appeared short (600 million years) after big bang
public class Galaxy extends ClassicalParticle3D implements Named {
    /** Constant for the well known Milky Way Galaxy. */
    public final static Galaxy MILKY_WAY = new Galaxy("Milky Way",
            Universe.CONVENTIONAL_UNIVERSE);

    /** Constant for the well known Andromeda Galaxy. */
    public final static Galaxy ANDROMEDA = new Galaxy("Andromeda",
            Universe.CONVENTIONAL_UNIVERSE);

    //unused but could be there to define kinds
    /**
     * DOCUMENT ME!
     */
    public final static String SPIRAL = "Spiral";

    /**
     * DOCUMENT ME!
     */
    public final static String LENTICULAR = "Lenticular";

    /**
     * DOCUMENT ME!
     */
    public final static String ELLIPTICAL = "Elliptical";

    /**
     * DOCUMENT ME!
     */
    public final static String IRREGULAR = "Irregular";

    /**
     * DOCUMENT ME!
     */
    private String name; //The name of this Galaxy.

    /**
     * DOCUMENT ME!
     */
    private Group shape; //shape (mostly never change)

    /**
     * DOCUMENT ME!
     */
    private double age; //age in seconds, defaults to 0

    /** The star systems of this Galaxy. */
    private Set starSystems; //all starsytems in a galaxy should be close to one other and for a group as a whole

    /**
     * DOCUMENT ME!
     */
    private Set wanderingObjects; //should not contain stars

    /**
     * DOCUMENT ME!
     */
    private Universe universe;

/**
     * Constructs a Galaxy.
     */
    public Galaxy(String name, Universe universe) {
        if ((name != null) && (name.length() > 0) && (universe != null)) {
            this.name = name;
            this.setMass(0);
            this.age = 0;
            this.starSystems = Collections.EMPTY_SET;
            this.wanderingObjects = Collections.EMPTY_SET;
            this.shape = null;
            this.universe = universe;
        } else {
            throw new IllegalArgumentException(
                "The Galaxy constructor doesn't accept null arguments (and name can't be empty).");
        }
    }

    /**
     * Gets the name for this object.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name can't be null or empty.");
        }
    }

    //there is no readon to get the center of mass as we don't expect to have all actual star systems
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeMass() {
        Iterator iterator;
        double result;

        iterator = starSystems.iterator();
        result = 0;

        while (iterator.hasNext()) {
            result += ((StarSystem) iterator.next()).getMass();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAge() {
        return age;
    }

    //tries to compute the age from all the contained star systems (which by the way should normally all have the same age)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeAge() {
        Iterator iterator;
        double result;

        iterator = starSystems.iterator();
        result = 0;

        while (iterator.hasNext()) {
            result = Math.max(result,
                    ((StarSystem) iterator.next()).computeAge());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param age DOCUMENT ME!
     */
    public void setAge(double age) {
        this.age = age;
    }

    /**
     * Gets the current star systems for this object.
     *
     * @return DOCUMENT ME!
     */
    public Set getStarSystems() {
        return starSystems;
    }

    //the enclosed stars
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumStars() {
        Iterator iterator;
        int result;

        iterator = starSystems.iterator();
        result = 0;

        while (iterator.hasNext()) {
            result += ((StarSystem) iterator.next()).getStars().size();
        }

        return result;
    }

    /**
     * Adds a star system for this object.
     *
     * @param starSystem DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addStarSystem(StarSystem starSystem) {
        if (starSystem != null) {
            if (starSystem.getGalaxy() == null) {
                starSystem.setGalaxy(this);
                starSystems.add(starSystem);
            } else {
                throw new IllegalArgumentException(
                    "Can't add StarSystem which is already in a Galaxy.");
            }
        } else {
            throw new IllegalArgumentException("Can't add a null StarSystem.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param starSystem DOCUMENT ME!
     */
    public void removeStar(StarSystem starSystem) {
        if (starSystem != null) {
            if (starSystems.size() > 1) {
                starSystem.setGalaxy(null);
                starSystems.remove(starSystem);
            } else {
                throw new IllegalArgumentException(
                    "Cannot remove a StarSystem which is not a child of this Galaxy.");
            }
        } else {
            throw new IllegalArgumentException(
                "Can't remove a null StarSystem.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param starSystems DOCUMENT ME!
     */
    public void setStarSystems(Set starSystems) {
        Iterator i;
        boolean valid;
        StarSystem currentStarSystem;

        if (starSystems != null) {
            if (starSystems.size() > 0) {
                i = starSystems.iterator();
                valid = true;

                while (i.hasNext() && valid) {
                    valid = i.next() instanceof StarSystem;
                }

                if (valid) {
                    i = starSystems.iterator();

                    while (i.hasNext()) {
                        ((StarSystem) i.next()).setGalaxy(this);
                    }

                    this.starSystems = starSystems;
                } else {
                    throw new IllegalArgumentException(
                        "The Set of starSystems should contain only StarSystems.");
                }
            } else {
                throw new IllegalArgumentException(
                    "Cannot set an empty StarSystem set to a Galaxy.");
            }
        } else {
            throw new IllegalArgumentException("Can't set null starSystems.");
        }
    }

    /**
     * Gets the current wandering objects for this object.
     *
     * @return DOCUMENT ME!
     */
    public Set getAstralBodies() {
        return wanderingObjects;
    }

    /**
     * Adds a wandering object for this object, by reassigning to this
     * galaxy the star system it belongs to.
     *
     * @param wanderingObject DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addAstralBody(AstralBody wanderingObject) {
        if (wanderingObject != null) {
            if (wanderingObject.getGalaxy() == null) {
                if (wanderingObject.getClass() != Star.class) {
                    wanderingObject.getStarSystem().setGalaxy(this);
                    this.wanderingObjects.add(wanderingObject);
                } else {
                    throw new IllegalArgumentException(
                        "Wandering objects set should contain only astral bodies (but not stars).");
                }
            } else {
                throw new IllegalArgumentException(
                    "Can't add a wandering object which is already in a Galaxy.");
            }
        } else {
            throw new IllegalArgumentException(
                "Can't add a null wandering object.");
        }
    }

    /**
     * Removes a wandering object for this object, by reassigning to
     * this galaxy the star system it belongs to.
     *
     * @param wanderingObject DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeAstralBody(AstralBody wanderingObject) {
        if (wanderingObject != null) {
            if (wanderingObject.getGalaxy().equals(this)) {
                wanderingObject.getStarSystem().setGalaxy(null);
                this.wanderingObjects.remove(wanderingObject);
            } else {
                throw new IllegalArgumentException(
                    "Cannot remove a wandering object which is not a child of this Galaxy.");
            }
        } else {
            throw new IllegalArgumentException(
                "Can't remove a null wandering object.");
        }
    }

    /**
     * Sets the current wandering objects for this object. All objects
     * in the set must be AstralBodies, of course.
     *
     * @param wanderingObjects DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAstralBodies(Set wanderingObjects) {
        Iterator i;
        boolean valid;
        Object value;

        if (wanderingObjects != null) {
            i = wanderingObjects.iterator();
            valid = true;

            while (i.hasNext() && valid) {
                value = i.next();
                valid = (value instanceof AstralBody) &&
                    (value.getClass() != Star.class);
            }

            if (valid) {
                i = wanderingObjects.iterator();

                while (i.hasNext()) {
                    ((AstralBody) i.next()).getStarSystem().setGalaxy(this);
                }

                this.wanderingObjects = wanderingObjects;
            } else {
                throw new IllegalArgumentException(
                    "Wandering objects set should contain only astral bodies (but not stars).");
            }
        } else {
            throw new IllegalArgumentException(
                "Can't set null wandering object.");
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return shape;
    }

    //the additional geometry for this galaxy, this is purely for rendering purposes
    /**
     * DOCUMENT ME!
     *
     * @param shape DOCUMENT ME!
     */
    public void setGroup(Group shape) {
        this.shape = shape;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Universe getUniverse() {
        return universe;
    }

    //use universe.addGalaxy(this)
    /**
     * DOCUMENT ME!
     *
     * @param universe DOCUMENT ME!
     */
    protected void setUniverse(Universe universe) {
        if (universe != null) {
            this.universe = universe;
        } else {
            throw new IllegalArgumentException("Can't set a null Universe.");
        }
    }

    //protected void updateShape() {
    //currently galaxies are invisible objects by themselves
    //you cannot fake a galaxy with a simple texture

    //}
}
