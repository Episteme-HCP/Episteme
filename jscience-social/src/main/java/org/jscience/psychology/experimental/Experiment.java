package org.jscience.psychology.experimental;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;
import org.jscience.sociology.Situation;

import java.util.*;


/**
 * A class representing a psychology experiment (whether for cognitive
 * psychology or social psychology).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//groups of subjects are defined by the tasks that they have to do
//then they are added to the experiment
//when all subjects have finished the experiments you can run a set of tests
//like for example ANOVA
public class Experiment extends Situation {
/**
     * Creates a new Experiment object.
     *
     * @param name     DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public Experiment(String name, String comments) {
        super(name, comments);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSubjects() {
        Iterator iterator;
        Set result;
        Role role;

        iterator = getRoles().iterator();
        result = Collections.EMPTY_SET;

        while (iterator.hasNext()) {
            role = (Role) iterator.next();

            if (role instanceof Subject) {
                result.add(role);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumSubjects() {
        return getSubjects().size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     * @param tasks DOCUMENT ME!
     */
    public void addSubject(Individual individual, Vector tasks) {
        addRole(new Subject(individual, this, tasks));
    }

    //if every subject has completed the experiment (VALID) or is tagged as invalid (INVALID)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExperimentComplete() {
        Iterator iterator;
        boolean result;

        iterator = getSubjects().iterator();
        result = true;

        while (iterator.hasNext() && result) {
            result = ((Subject) iterator.next()).isValid();
        }

        return result;
    }

    //returns all the tasks in no particular order that appear at least once for a subject
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTasks() {
        Iterator iterator;
        HashSet result;

        iterator = getSubjects().iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((Subject) iterator.next()).getTasks());
        }

        return result;
    }
}
