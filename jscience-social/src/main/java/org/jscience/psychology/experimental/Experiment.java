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
    public Experiment(String name, String comments) {
        super(name, comments);
    }

    public Set<Subject> getSubjects() {
        Set<Subject> result = new HashSet<>();
        for (Role role : getRoles()) {
            if (role instanceof Subject subject) {
                result.add(subject);
            }
        }
        return result;
    }

    public int getNumSubjects() {
        return getSubjects().size();
    }

    public void addSubject(Individual individual, String identifier, Vector<Task> tasks) {
        addRole(new Subject(individual, this, identifier, tasks));
    }

    public boolean isExperimentComplete() {
        for (Subject subject : getSubjects()) {
            if (!subject.isValid()) {
                return false;
            }
        }
        return true;
    }

    public Set<Task> getTasks() {
        Set<Task> result = new HashSet<>();
        for (Subject subject : getSubjects()) {
            result.addAll(subject.getTasks());
        }
        return result;
    }
}
}
