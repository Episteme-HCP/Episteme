package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.MaterialResource;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;

import javax.media.j3d.Group;


/**
 * A class representing an artifact.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//artifacts are the result of human craftmanship
//think about every tool of the stone age... up to now whether a needle or a scyscrapper
//many artifacts are actually a complex set up of more simple artifacts.
//Take for example a light bulb. There is a filament, a glass bulb, a metal screw, an isolating part, just to mention a few.
//3d designers have much more flexibility than in real life as they can conceive the light bulb as a continuous mesh.
//we provide subclasses here mainly to help the developer tag its geometry and help him save some time providing actual expected behavior.
//we cannot expect him to define subparts for every geometry like in the light bulb example as it would be most of the time useless and also provide a rendering penality.
//we do not take into account decay or roads when they are only tracks in the dust
//see http://www.vterrain.org/Culture/culture_class.html for a discussion about classification
//artifacts are defined for there primary function, a specific purpose
//you can always decide something is something else: for example a knife can be used to cut one's food, as a surgeon tool, as a piece of art, etc
//but what you actually expect from a knife is that it cuts. See Object or Tool
//artifacts include worked bones, stone tools, processed liquids, etc
public class Artifact extends MaterialResource {
    /** DOCUMENT ME! */
    private Group group; //the geometry

    /** DOCUMENT ME! */
    private boolean isBroken;

/**
     * DOCUMENT ME!
     *
     * @param name
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Artifact(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
        this.isBroken = false;
    }

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

    //isBroken is a boolean indicating the functional state
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isBroken DOCUMENT ME!
     */
    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }
}
