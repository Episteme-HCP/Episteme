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

package org.jscience.economics;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the transformation of some materials and some human
 * ressources into a finished something that can be sold. A product (whether
 * primary or secondary, that is, already transformed) is a material thing. A
 * service is a kind of immaterial product (like having a hair cut). Work is
 * also known as task. Each task can in turn be divided further on into
 * subtasks to further describe each process.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//even the most basic work require some resources
//for example cutting someone's hair require cisors, guarding a tower requires weapons (googles...)
//note that these resources should also be in the product list (as they are sort of recycled in the process)
//of course you can always subdivide any task in many more tasks, arrange them in hierarchies or whatever
//the way we define task does require a workforce but there is no direct links to Workers that could help you define such a work
//people in fact usually work on many projects at once and also you can assign them to a work, you can't really compute the workforce
//you therefore have to subclass this class and provide a Set holder of Workers should you want to design a project manager
//a task may be carried over in many different ways (although there is usually one more efficient than the others)
//tasks generally involve using specific process(es)
public class Task extends Object implements Named {
    //usually lower than sum of energy and human costs thanks to the fact that big groups work usually faster
    //you may also count in that cost the price of using the machines, renting the building, etc....
    /** DOCUMENT ME! */
    private String name; //the name of the task

    /** DOCUMENT ME! */
    private String process; //a step by step description of the task with commas, and numbering or an identification

    /** DOCUMENT ME! */
    private Set resources; //the needed stuff

    /** DOCUMENT ME! */
    private Set subTasks;

    /** DOCUMENT ME! */
    private Set products; //the produced stuff

    /** DOCUMENT ME! */
    private double duration; //the time for completion assuming that you have the machines, the humans, the energy, no delay

    //this is the work to produce one unit of the products (there is usually only a single resulting product)
    /**
     * Creates a new Task object.
     *
     * @param name DOCUMENT ME!
     * @param resources DOCUMENT ME!
     * @param products DOCUMENT ME!
     */
    public Task(String name, Set resources, Set products) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (resources != null) &&
                (resources.size() > 0) && (products != null) &&
                (products.size() > 0)) {
            iterator = resources.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            iterator = products.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                this.name = name;
                this.process = new String("");
                this.resources = resources;
                this.subTasks = Collections.EMPTY_SET;
                this.products = products;
                this.duration = 0;
            } else {
                throw new IllegalArgumentException(
                    "The Set of resources and products should contain only Resources.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Task constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getProcess() {
        return process;
    }

    /**
     * DOCUMENT ME!
     *
     * @param process DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setProcess(String process) {
        if (process != null) {
            this.process = process;
        } else {
            throw new IllegalArgumentException("You can't set a null process.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSubTasks() {
        return subTasks;
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addSubTasks(Task task) {
        if (task != null) {
            subTasks.add(task);
        } else {
            throw new IllegalArgumentException("You can't add a null task.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeSubTasks(Task task) {
        if (task != null) {
            subTasks.remove(task);
        } else {
            throw new IllegalArgumentException("You can't remove null Task.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tasks DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSubTasks(Set tasks) {
        Iterator iterator;
        boolean valid;

        if (tasks != null) {
            iterator = tasks.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Task;
            }

            if (valid) {
                this.subTasks = tasks;
            } else {
                throw new IllegalArgumentException(
                    "The task Set must contain only Tasks.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null tasks set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getResources() {
        return resources;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDuration() {
        return duration;
    }

    /**
     * DOCUMENT ME!
     *
     * @param duration DOCUMENT ME!
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getProducts() {
        return products;
    }
}
