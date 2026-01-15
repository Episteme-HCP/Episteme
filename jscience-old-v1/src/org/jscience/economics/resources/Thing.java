package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.Resource;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import java.util.Date;

import javax.media.j3d.Group;


/**
 * A class representing a "thing" (especially targetted towards 3D and
 * robotics).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//every object that has a physical counterpart: not emotions for example
//it is meant to be something that can be physically seen. Electricity, protons are not things as they cannot be seen.
//several things are nevertheless hard to represent: for example rain, it extends Natural but should it extend Water (not provided) ?
//water, wind tides, solar radiation (light...)
//this package is not used by any other package to keep dependencies small as it is intended to be used in 3D
//several class enhancements could be expected looking at the relations between:
//org.jscience.history.archeology.Item and Artifact or Fossil
//org.jscience.biology.Individual and Creature
//org.jscience.arts.Artwork and Installation
//org.jscience.chemistry.Molecule org.jscience.chemistry.Crystal and Mineral
//org.jscience.geography.Home (and org.jscience.geography.BusinessPlace) and Building
//kind shouldn't be set to Resource.SECONDARY
public class Thing extends Resource {
    /** DOCUMENT ME! */
    private Group group; //the geometry

/**
     * Creates a new Thing object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Thing(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
    }

    //can be null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return group;
    }

    //you should store the Shape(s)3D below
    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}
