package org.episteme.economics.resources;

import org.episteme.economics.Community;
import org.episteme.economics.Resource;

import org.episteme.earth.Place;

import org.episteme.measure.Amount;

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
//org.episteme.history.archeology.Item and Artifact or Fossil
//org.episteme.biology.Individual and Creature
//org.episteme.arts.Artwork and Installation
//org.episteme.chemistry.Molecule org.episteme.chemistry.Crystal and Mineral
//org.episteme.geography.Home (and org.episteme.geography.BusinessPlace) and Building
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
