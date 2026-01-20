package org.jscience.economics.resources;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a group of things that are bond together or act as
 * a group.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a useful class to define and manipulate grouped things (for example parts of a motor)
//the group has to be understood as a physical binding
//for example a light bulb should be in a group with a lamp and perhaps a remote switch, a painting with a frame or a window with a building
//it is up to you to maintain lists of ThingsGroup
//it is up to the developer to arrange things so that a thing has some knowledge
//about its thingGroup and to change the internal state of each object accordingly
public class ThingsGroup extends java.lang.Object {
    /** DOCUMENT ME! */
    private Set things; //the things in the group, they all have to share the same GeosphereBranchGroup

    //grouped things usually move together, behave as one, are close to one others
    /**
     * Creates a new ThingsGroup object.
     */
    public ThingsGroup() {
        things = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thing DOCUMENT ME!
     */
    public void addThing(Thing thing) {
        things.add(thing);
    }

    /**
     * DOCUMENT ME!
     *
     * @param thing DOCUMENT ME!
     */
    public void removeThing(Thing thing) {
        things.remove(thing);
    }

    //we should provide a method to
    //move the things as a group updating their position with this offset
    //but I haven't found any convenient method
}
