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

package org.jscience.linguistics;

import org.jscience.biology.Individual;

import org.jscience.sociology.Situation;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing the interaction of people communicating.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChatSituation extends Situation {
    /** DOCUMENT ME! */
    private Vector communications;

/**
     * Creates a new ChatSituation object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public ChatSituation(String name, String comments) {
        super(name, comments);
        communications = new Vector();
    }

    //locutors are automatically added if not already by calling addVerbalCommunication()
    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addLocutor(Individual individual) {
        super.addRole(new Locutor(individual, this));
    }

    //locutors are automatically added if not already by calling addVerbalCommunication()
    /**
     * DOCUMENT ME!
     *
     * @param locutor DOCUMENT ME!
     */
    public void addLocutor(Locutor locutor) {
        super.addRole(locutor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getLocutors() {
        Iterator iterator;
        Object value;
        Set result;

        iterator = getRoles().iterator();
        result = Collections.EMPTY_SET;

        while (iterator.hasNext()) {
            value = iterator.next();

            if (value instanceof Locutor) {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getVerbalCommunications() {
        return communications;
    }

    /**
     * DOCUMENT ME!
     *
     * @param communication DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addVerbalCommunication(VerbalCommunication communication) {
        if (communication != null) {
            if (getLocutors().contains(communication.getLocutor())) {
                communications.add(communication);

                //may be we should store the people who heard the communication
                //but this would mean we would have to track people as they come and as they leave
                //(we would then also remove the requirement for the locutor to be part of the roles, or would we ?)
            } else {
                addLocutor(communication.getLocutor());
                communications.add(communication);
            }
        } else {
            throw new IllegalArgumentException(
                "You can't add a null VerbalCommunication.");
        }
    }
}
