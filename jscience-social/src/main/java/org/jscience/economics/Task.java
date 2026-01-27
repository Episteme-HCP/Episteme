/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;


import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import org.jscience.mathematics.numbers.real.Real;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a transformation process where materials and human resources are 
 * converted into finished products or services.
 * 
 * <p>A task can be subdivided into hierarchies of subtasks. Each task requires
 * a set of resources and produces a set of resulting resources (products).</p>
 * 
 * <p>Even the most basic work requires some resources (e.g., scissors for 
 * hair cutting). In many cases, these resources are "recycled" or remain
 * available after the task, in which case they should appear in both the
 * input resources and output products sets.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @version 6.0, July 21, 2014
 */
@Persistent
public class Task implements ComprehensiveIdentification {
    @Id
    private final Identification id;
    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();
    @Attribute
    private String name;
    @Attribute
    private String process;
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Resource> resources;
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Task> subTasks;
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Resource> products;
    @Attribute
    private Real duration;

    /**
     * Creates a new Task object.
     *
     * @param name      the name of the task
     * @param resources the required resources
     * @param products  the produced resources
     */
    public Task(String name, Set<Resource> resources, Set<Resource> products) {
        this(new SimpleIdentification(name), name, resources, products);
    }

    public Task(Identification id, String name, Set<Resource> resources, Set<Resource> products) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        
        this.resources = new HashSet<>(Objects.requireNonNull(resources, "Resources set cannot be null"));
        this.products = new HashSet<>(Objects.requireNonNull(products, "Products set cannot be null"));
        
        this.process = "";
        this.subTasks = new HashSet<>();
        this.duration = Real.ZERO;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    public String getName() {
        return name;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = Objects.requireNonNull(process, "Process cannot be null");
    }

    public Set<Task> getSubTasks() {
        return Collections.unmodifiableSet(subTasks);
    }

    public void addSubTask(Task task) {
        subTasks.add(Objects.requireNonNull(task, "Subtask cannot be null"));
    }

    public void removeSubTask(Task task) {
        subTasks.remove(task);
    }

    public void setSubTasks(Set<Task> tasks) {
        this.subTasks = new HashSet<>(Objects.requireNonNull(tasks, "Tasks set cannot be null"));
    }

    public Set<Resource> getResources() {
        return Collections.unmodifiableSet(resources);
    }

    public Real getDuration() {
        return duration;
    }

    public void setDuration(Real duration) {
        this.duration = duration;
    }

    public Set<Resource> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    public void addResource(Resource resource) {
        resources.add(Objects.requireNonNull(resource));
    }

    public void addProduct(Resource product) {
         products.add(Objects.requireNonNull(product));
    }
}
