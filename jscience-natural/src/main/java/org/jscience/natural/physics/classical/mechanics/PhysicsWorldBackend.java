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

package org.jscience.natural.physics.classical.mechanics;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Time;

/**
 * Represents a physics simulation environment (Scene/World).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface PhysicsWorldBackend {

    /**
     * Adds a rigid body to the simulation.
     * 
     * @param body the body to add
     */
    void addRigidBody(RigidBody body);

    /**
     * Removes a rigid body from the simulation.
     * 
     * @param body the body to remove
     */
    void removeRigidBody(RigidBody body);

    /**
     * Steps the simulation forward in time.
     * 
     * @param timeStep the amount of time to simulate
     */
    void stepSimulation(Quantity<Time> timeStep);

    /**
     * Steps the simulation with fixed time steps and max substeps.
     * 
     * @param timeStep the amount of time to simulate
     * @param maxSubSteps maximum number of internal substeps
     * @param fixedTimeStep the internal fixed time step
     */
    void stepSimulation(Quantity<Time> timeStep, int maxSubSteps, Quantity<Time> fixedTimeStep);

    /**
     * Sets the gravity vector for the world.
     * 
     * @param gravityX gravity in X
     * @param gravityY gravity in Y
     * @param gravityZ gravity in Z
     */
    void setGravity(double gravityX, double gravityY, double gravityZ);
}
