package org.jscience.economics;

import org.jscience.util.Named;


//a rather abstract way by which you are able to modify your surroundings
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
//http://en.wikipedia.org/wiki/Taxonomy_of_manufacturing_processes
//or see processes.pdf in this directory
public class Process implements Named {
    /** DOCUMENT ME! */
    public final static Process FERMENTATION = new Process("Fermentation");

    /** DOCUMENT ME! */
    public final static Process HEAT = new Process("Heat"); //and melt and burn

    /** DOCUMENT ME! */
    public final static Process SPLIT = new Process("Split"); //cut, separate, filter, dig

    /** DOCUMENT ME! */
    public final static Process HIT = new Process("Hit"); //push firmly, press, nail, pedal...

    /** DOCUMENT ME! */
    public final static Process MIX = new Process("Mix"); //salt, etc

    /** DOCUMENT ME! */
    public final static Process DRY = new Process("Dry");

    /** DOCUMENT ME! */
    public final static Process HUMIDIFY = new Process("Humidify");

    /** DOCUMENT ME! */
    public final static Process GROUP = new Process("Group"); //also centralize, the opposite of separate (split)

    /** DOCUMENT ME! */
    public final static Process CLEAN = new Process("Clean");

    /** DOCUMENT ME! */
    public final static Process STORE = new Process("Store"); //and keep for later use or wait

    /** DOCUMENT ME! */
    public final static Process AGGITATE = new Process("Aggitate");

    /** DOCUMENT ME! */
    public final static Process TRANSPORT = new Process("Transport"); //moving, includes lifting, pushing, turning

    /** DOCUMENT ME! */
    public final static Process MOULD = new Process("Mould");

    /** DOCUMENT ME! */
    public final static Process SPRAY = new Process("Spray"); //and paint, and spill

    /** DOCUMENT ME! */
    public final static Process PARALLELIZE = new Process("Parallelize"); // TAYLORIZE = DIVIDE and PARALLELIZE and GROUP

    /** DOCUMENT ME! */
    public final static Process FREEZE = new Process("Freeze"); //includes freshen

    /** DOCUMENT ME! */
    public final static Process POUR = new Process("Pour"); //and fill

    /** DOCUMENT ME! */
    public final static Process SELECT = new Process("Select"); //grab, etc.

    /** DOCUMENT ME! */
    public final static Process ACTIVATE = new Process("Activate"); //power on, etc

    /** DOCUMENT ME! */
    public final static Process VENTILATE = new Process("Ventilate");

    /** DOCUMENT ME! */
    public final static Process INFLATE = new Process("Inflate");

    /** DOCUMENT ME! */
    public final static Process ASSEMBLE = new Process("Assemble"); //includes glue or screw

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String comments;

    //sew, dig....
/**
     * Creates a new Process object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Process(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.comments = new String();
        } else {
            throw new IllegalArgumentException(
                "The Process constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setComments(String comments) {
        if (comments != null) {
            this.comments = comments;
        } else {
            throw new IllegalArgumentException("You can't set a null comment.");
        }
    }
}
