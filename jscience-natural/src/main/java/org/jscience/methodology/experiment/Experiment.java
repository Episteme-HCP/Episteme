/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.methodology.experiment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.jscience.biology.Individual;
import org.jscience.methodology.Hypothesis;
import org.jscience.methodology.ScientificExperiment;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A class representing a scientific experiment involving subjects, tasks, and variables.
 * <p>
 * This class specializes {@link ScientificExperiment} for experimental designs
 * common in psychology, biology, and social sciences.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Experiment extends ScientificExperiment<Void, Set<ValuedVariable>> {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Subject> subjects = new HashSet<>();

    public Experiment(String name) {
        super(name);
    }

    public Experiment(String name, String description) {
        super(name);
        setDescription(description);
    }

    /**
     * Gets all subjects involved in the experiment.
     */
    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    public int getNumSubjects() {
        return subjects.size();
    }

    /**
     * Adds a subject to the experiment.
     * @param individual The individual to add.
     * @param tasks The tasks assigned to this individual in this experiment.
     * @return The created Subject.
     */
    public Subject addSubject(Individual individual, List<Task> tasks) {
        Subject subject = new Subject(individual, tasks);
        subjects.add(subject);
        return subject;
    }

    public boolean isExperimentComplete() {
        if (subjects.isEmpty()) return false;
        for (Subject subject : subjects) {
            if (!subject.isValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets all tasks defined across all subjects.
     */
    public Set<Task> getTasks() {
        Set<Task> result = new HashSet<>();
        for (Subject subject : subjects) {
            result.addAll(subject.getTasks());
        }
        return result;
    }

    @Override
    public CompletableFuture<Set<ValuedVariable>> run(Void input) {
        // Implementation for running the experiment across all subjects and tasks
        return CompletableFuture.completedFuture(new HashSet<>());
    }

    @Override
    public Hypothesis<Set<ValuedVariable>> getHypothesis() {
        return null; // Can be set via an attribute if needed
    }
}
