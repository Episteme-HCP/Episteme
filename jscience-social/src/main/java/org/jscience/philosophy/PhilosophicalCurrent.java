package org.jscience.philosophy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a well defined group of ideas.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class PhilosophicalCurrent extends Belief {
    /** DOCUMENT ME! */
    private Set models;

/**
     * Creates a new PhilosophicalCurrent object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public PhilosophicalCurrent(String name, String comments) {
        super(name, comments);
        models = new HashSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getModels() {
        return models;
    }

    //all members of the Set should be models
    /**
     * DOCUMENT ME!
     *
     * @param models DOCUMENT ME!
     */
    public void setModels(Set models) {
        Iterator iterator;
        boolean valid;

        if (models != null) {
            iterator = models.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Model;
            }

            if (valid) {
                this.models = models;
            } else {
                throw new IllegalArgumentException(
                    "The Set of models should contain only Models.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of models shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void addModel(Model model) {
        models.add(model);
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void removeModel(Model model) {
        models.remove(model);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllConcepts() {
        Set result;
        Iterator iterator;

        result = new HashSet();
        iterator = models.iterator();

        while (iterator.hasNext()) {
            result.addAll(((Model) iterator.next()).getAllConcepts());
        }

        return result;
    }
}
