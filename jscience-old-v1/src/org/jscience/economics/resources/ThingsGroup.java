/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
