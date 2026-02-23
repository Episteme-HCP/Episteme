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

/**
 * Backend representation of a RigidBody.
 * <p>
 * This interface bridges the high-level JScience {@code RigidBody} state 
 * to the low-level physics engine representation (e.g. {@code btRigidBody}).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface RigidBodyBridge {
    
    /**
     * Syncs the backend state from the JScience object.
     * Called before simulation step.
     */
    void pushState();

    /**
     * Syncs the JScience object from the backend state.
     * Called after simulation step.
     */
    void pullState();
    
    /**
     * Apply a central force to the body.
     * 
     * @param x force x
     * @param y force y
     * @param z force z
     */
    void applyCentralForce(double x, double y, double z);
    
    /**
     * Apply a central impulse to the body.
     * 
     * @param x impulse x
     * @param y impulse y
     * @param z impulse z
     */
    void applyCentralImpulse(double x, double y, double z);

    /**
     * Destroys native resources associated with this body.
     */
    void destroy();
}
