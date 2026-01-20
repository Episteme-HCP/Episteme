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
