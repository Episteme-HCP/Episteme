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

package org.jscience.physics.kinematics.force;

import java.util.Iterator;

/**
 * Updates velocity and position data using Euler's Method. This is the
 * simplest and fastest method, but is somewhat inaccurate and less smooth
 * than more costly approaches.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @see RungeKuttaIntegrator2D
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class EulerIntegrator2D implements ForceIntegrator {
    
    /**
     * @see org.jscience.physics.kinematics.force.ForceIntegrator#integrate(org.jscience.physics.kinematics.force.ForceSimulator, long)
     */
    public void integrate(ForceSimulator sim, long timestep) {
        float speedLimit = sim.getSpeedLimit();
        Iterator iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem2D item = (ForceItem2D)iter.next();
            item.location[0] += timestep * item.velocity[0];
            item.location[1] += timestep * item.velocity[1];
            float coeff = timestep / item.mass;
            item.velocity[0] += coeff * item.force[0];
            item.velocity[1] += coeff * item.force[1];
            float vx = item.velocity[0];
            float vy = item.velocity[1];
            float v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                item.velocity[0] = speedLimit * vx / v;
                item.velocity[1] = speedLimit * vy / v;
            }
        }
    }

} // end of class EulerIntegrator
