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

package org.jscience.physics.kinematics;

import org.jscience.util.IllegalDimensionException;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The PhysicsBehavior class provides a way to build a complete physics simulation system ready to be used with Java3D. This is a simulator for 3D particles only.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//user should stop the sheduler before changing any field or particle.
//fields and particles should all be of dimension 3.

//although this is a complete scheduler, actual geometry for particles is left to the user which should set n transformGroup accordingly to the n particles positions and orientation after each stimulus is processed.

//let us know if you enhance this class

//this algorithm is sub optimal but there are ways to have something better although it is quite much work:

//from sverre@ast.cam.ac.uk:
//I am afraid the algorithm
//is too difficult to describe in an article. I have written a book
//but even that is not enough? We are dealing with some kind of
//complicated artificial intelligence and not a simple system, even
//with only 4 interacting bodies.

public class PhysicsBehavior extends Behavior implements KinematicsSimulation {

    private final static double DEFAULT_SIMULATION_STEP = 10; //timestep in milliseconds

    private Set fields;
    private Set particles;
    private WakeupCondition wakeupCondition;

    private double interval;
    private double startTime;//used by force or fields that are dependant on time BUT NOT by particles for which you have to save up position and velocity at any given time if you want to reuse it later
    private double currentTime;

    public PhysicsBehavior() {

        fields = Collections.EMPTY_SET;
        particles = Collections.EMPTY_SET;
        interval = DEFAULT_SIMULATION_STEP;
        startTime = 0;
        currentTime = startTime;

    }

    public double getInterval() {

        return interval;

    }

    //defines the time step in milliseconds
    public void setInterval(double dt) {

        interval = dt;

    }

    public double getStartTime() {

        return startTime;

    }

    public void setStartTime(double time) {

        startTime = time;

    }

    public double getCurrentTime() {

        return currentTime;

    }

    public void initialize() {

        wakeupCondition = new WakeupOnElapsedTime(interval);
        this.setEnable(false);

    }

    //calls start or stop
    public void setEnable(boolean value) {

        super.setEnable(value);
        if (value) {
            start();
        } else {
            stop();
        }

    }

    //starts the scheduler (modifies the setEnable() field)
    public void start() {

        Iterator iterator;
        Field currentField;
        AbstractClassicalParticle currentParticle;

        iterator = fields.iterator();
        while (iterator.hasNext()) {
            currentField = (Field) iterator.next();
            currentParticle = currentField.getParticle();
            currentParticle.addForce(currentField.createForce());
        }

        this.setEnable(true);

    }

    //stops the scheduler first if not stopped
    //then restarts the scheduler at startTime
    public void restart() {

        stop();
        currentTime = startTime;
        start();

    }

    public boolean isRunning() {

        return this.getEnable();

    }

    //stops the scheduler if running (modifies the setEnable() field)
    public void stop() {

        if (!isRunning()) {
            this.setEnable(false);
        }

    }

    public void processStimulus(java.util.Enumeration enumeration) {

        Iterator iterator1;
        Iterator iterator2;

        iterator1 = particles.iterator();
        while (iterator1.hasNext()) {
            iterator2 = ((AbstractClassicalParticle) iterator1.next()).getForces().iterator();
            while (iterator2.hasNext()) {
                XXXXXXXXXXXXXXXX//must be done on a copy of all particles
//may be we should provide a different way for large simulations (tridiagonal 2D matrix of forces for the n particles)
                currentParticle.applyForce((Force) iterator2.next(), currentTime, interval);
            }
        }

        currentTime += interval;

        this.wakeupOn(wakeupCondition);

    }

    public int getDimension() {

        return 3;

    }

    public Set getFields() {

        return fields;

    }

    //field must be of the same number of dimension than the system
    //fields can't be added while the scheduler is running
    public void addField(Field f) {

        if (!isRunning()) {
            if (f != null) {
                if (f.getDimension() == getDimension()) {
                    fields.add(f);
                } else
                    throw new IllegalDimensionException("Dimension of the Field must be 3.");
            } else
                throw new IllegalArgumentException("You can't add a null Field.");
        } else
            throw new IllegalArgumentException("You can't add Fields while the scheduler is running.");

    }

    //fields can't be removed while the scheduler is running
    public void removeField(Field f) {

        if (!isRunning()) {
            fields.remove(f);
        } else
            throw new IllegalArgumentException("You can't remove Fields while the scheduler is running.");

    }

    //each element of the set must be an field and with the same number of dimension than the system
    //fields can't be added while the scheduler is running
    public void setFields(Set fields) {

        Iterator iterator;
        boolean valid;

        if (!isRunning()) {
            if (fields != null) {
                iterator = fields.iterator();
                valid = true;
                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Field;
                }
                if (valid) {
                    iterator = particles.iterator();
                    valid = true;
                    while (iterator.hasNext() && valid) {
                        valid = (((AbstractClassicalParticle) iterator.next()).getDimension() == getDimension());
                    }
                    if (valid) {
                        this.particles = new HashSet();
                        this.particles.addAll(particles);
                    } else
                        throw new IllegalDimensionException("Dimension of the Fields must be 3.");
                } else
                    throw new IllegalArgumentException("All elements in the Set must be Fields.");
            } else
                throw new IllegalArgumentException("You can't add a null Field.");
        } else
            throw new IllegalArgumentException("You can't add Fields while the scheduler is running.");

    }

    //fields can't be removed while the scheduler is running
    public void removeFields(Set fields) {

        if (!isRunning()) {
            this.fields.removeAll(fields);
        } else
            throw new IllegalArgumentException("You can't remove Fields while the scheduler is running.");

    }

    public Set getParticles() {

        return particles;

    }

    //particle must be of the same number of dimension than the system
    //particles can't be added while the scheduler is running
    public void addParticle(AbstractClassicalParticle p) {

        if (!isRunning()) {
            if (p != null) {
                if (p.getDimension() == getDimension()) {
                    particles.add(p);
                } else
                    throw new IllegalDimensionException("Dimension of the particle must be 3.");
            } else
                throw new IllegalArgumentException("You can't add a null particle.");
        } else
            throw new IllegalArgumentException("You can't add particles while the scheduler is running.");

    }

    //particles can't be removed while the scheduler is running
    public void removeParticle(AbstractClassicalParticle p) {

        if (!isRunning()) {
            particles.remove(p);
        } else
            throw new IllegalArgumentException("You can't remove particles while the scheduler is running.");

    }

    //each element of the set must be a particle and with the same number of dimension than the system
    //particles can't be added while the scheduler is running
    public void setParticles(Set particles) {

        Iterator iterator;
        boolean valid;

        if (!isRunning()) {
            if (particles != null) {
                iterator = particles.iterator();
                valid = true;
                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof AbstractClassicalParticle;
                }
                if (valid) {
                    iterator = particles.iterator();
                    valid = true;
                    while (iterator.hasNext() && valid) {
                        valid = (((AbstractClassicalParticle) iterator.next()).getDimension() == getDimension());
                    }
                    if (valid) {
                        this.particles = new HashSet();
                        this.particles.addAll(particles);
                    } else
                        throw new IllegalDimensionException("Dimension of the particles must be 3.");
                } else
                    throw new IllegalArgumentException("All particles in the Set must be AbstractClassicleParticles.");
            } else
                throw new IllegalArgumentException("You can't add a null particle.");
        } else
            throw new IllegalArgumentException("You can't add particles while the scheduler is running.");

    }

    //particles can't be removed while the scheduler is running
    public void removeParticles(Set particles) {

        if (!isRunning()) {
            this.particles.remove(particles);
        } else
            throw new IllegalArgumentException("You can't remove particles while the scheduler is running.");

    }

}
