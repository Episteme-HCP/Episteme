package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.PotentialResource;
import org.jscience.economics.Resource;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * A class representing a tool.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an object used to build up or fix some other object
//many objects can be seen as tools when used for an unsual purpose
//but they are not really tools as their use is "final": a tin is to be eaten, a chair to sit on
//in a virtual world you will probably prefer to build up ready to use tools
//for example you will not design a hammer but a wireless automatic nailer (that contains nails) and can be applied directly on Objets
//you will therefore never have to locate nails first, put nails under hammer and then nail, but directly nail.
public abstract class Tool extends org.jscience.economics.resources.Object {
    /** DOCUMENT ME! */
    private String purpose; //the human readable purpose of this tool, for example: a hammer nails

    /** DOCUMENT ME! */
    private PotentialResource[] targets; //the target classes of Things the Tool is meant to be applied on

    /** DOCUMENT ME! */
    private int acts; //the number of different actions that can be applied on this tool

/**
     * Creates a new Tool object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Tool(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
        this.purpose = null;
        this.targets = new PotentialResource[0];
        this.acts = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //may return null
    public String getPurpose() {
        return purpose;
    }

    /**
     * DOCUMENT ME!
     *
     * @param purpose DOCUMENT ME!
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PotentialResource[] getTargets() {
        return targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @param targets DOCUMENT ME!
     */
    public void setTargets(Resource[] targets) {
        this.targets = targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumActions() {
        return acts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param acts DOCUMENT ME!
     */
    public void setNumActions(int acts) {
        this.acts = acts;
    }

    //return the corresponding uhman readable action name in english for action i
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getActionName(int i);

    //launches the predefined action on the predefined objects (which classes should be in the target list) provided a target is in range
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param objects DOCUMENT ME!
     */
    public abstract void act(int i, Object[] objects);
}
