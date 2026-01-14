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

package org.jscience.philosophy.storytelling;

import org.jscience.util.BinaryRelation;
import org.jscience.util.NAry;
import org.jscience.util.Relations;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a continuous and logical flow of events that
 * corresponds to what could be described as the skeleton hidden in a story,
 * or the script. In other words, it is not meant to be as readble as a real
 * story but to store the useful information. Also a class representing an
 * event linked to some others in a causal or timed fashion.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is a VERY basic story system from which you can design your own and especially a solver able to understand certain kinds of facts (Event.getObject())
//for example you could do a Cluedo like solver
//example:
//Event1: A is here
//Event2: B passes by
//Event3: A and B usually fight (incomplete event)
//Event4: A and B fight this time (computed event from solver)
//Event1: Sunshines
//Event2: Humidity fades away
//Link (event1, event2)
public class Story extends java.lang.Object {
    /** DOCUMENT ME! */
    private Set events;

    /** DOCUMENT ME! */
    private BinaryRelation relations;

/**
     * Creates a new Story object.
     */
    public Story() {
        events = Collections.EMPTY_SET;
        relations = Relations.EMPTY_BINARYRELATION;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getEvents() {
        return events;
    }

    //refuse null
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void addEvent(Event event) {
        if (event != null) {
            events.add(event);
        } else {
            throw new IllegalArgumentException("Can't add a null Event.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }

    //you should add links only to events that are already stored as Events
    /**
     * DOCUMENT ME!
     *
     * @param cause DOCUMENT ME!
     * @param consequence DOCUMENT ME!
     */
    public void add(Event cause, Event consequence) {
        java.lang.Object[] value;

        if ((cause != null) && (events.contains(cause)) &&
                (consequence != null) && (events.contains(consequence))) {
            value = new Object[2];
            value[0] = cause;
            value[1] = consequence;
            relations.remove(new NAry(value));
        } else {
            throw new IllegalArgumentException(
                "Can't add a relation for unknown events.");
        }
    }

    //you should remove links only to events that are stored as Events
    /**
     * DOCUMENT ME!
     *
     * @param cause DOCUMENT ME!
     * @param consequence DOCUMENT ME!
     */
    public void remove(Event cause, Event consequence) {
        java.lang.Object[] value;

        if ((cause != null) && (events.contains(cause)) &&
                (consequence != null) && (events.contains(consequence))) {
            value = new Object[2];
            value[0] = cause;
            value[1] = consequence;
            relations.remove(new NAry(value));
        } else {
            throw new IllegalArgumentException(
                "Can't add a relation for unknown events.");
        }
    }

    //the resulting Set is a Set of events computed in the old fashioned Prolog style from the existing events
    /**
     * DOCUMENT ME!
     *
     * @param question DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set solve(Event question) {
        return null; //TODO
    }
}
