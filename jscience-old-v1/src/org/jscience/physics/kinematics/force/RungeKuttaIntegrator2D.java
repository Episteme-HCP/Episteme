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
 * Updates velocity and position data using the 4th-Order Runge-Kutta method.
 * It is slower but more accurate than other techniques such as Euler's Method.
 * The technique requires re-evaluating forces 4 times for a given timestep.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class RungeKuttaIntegrator2D implements ForceIntegrator {
    
    /**
     * @see org.jscience.physics.kinematics.force.ForceIntegrator#integrate(org.jscience.physics.kinematics.force.ForceSimulator, long)
     */
    public void integrate(ForceSimulator sim, long timestep) {
        float speedLimit = sim.getSpeedLimit();
        float vx, vy, v, coeff;
        float[][] k, l;
        
        Iterator iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem2D item = (ForceItem2D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            item.plocation[0] = item.location[0];
            item.plocation[1] = item.location[1];
            k[0][0] = timestep*item.velocity[0];
            k[0][1] = timestep*item.velocity[1];
            l[0][0] = coeff*item.force[0];
            l[0][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] += 0.5f*k[0][0];
            item.location[1] += 0.5f*k[0][1];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem2D item = (ForceItem2D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5f*l[0][0];
            vy = item.velocity[1] + .5f*l[0][1];
            v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[1][0] = timestep*vx;
            k[1][1] = timestep*vy;
            l[1][0] = coeff*item.force[0];
            l[1][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5f*k[1][0];
            item.location[1] = item.plocation[1] + 0.5f*k[1][1];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem2D item = (ForceItem2D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5f*l[1][0];
            vy = item.velocity[1] + .5f*l[1][1];
            v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[2][0] = timestep*vx;
            k[2][1] = timestep*vy;
            l[2][0] = coeff*item.force[0];
            l[2][1] = coeff*item.force[1];
        
            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5f*k[2][0];
            item.location[1] = item.plocation[1] + 0.5f*k[2][1];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem2D item = (ForceItem2D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            float[] p = item.plocation;
            vx = item.velocity[0] + l[2][0];
            vy = item.velocity[1] + l[2][1];
            v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            k[3][0] = timestep*vx;
            k[3][1] = timestep*vy;
            l[3][0] = coeff*item.force[0];
            l[3][1] = coeff*item.force[1];
            item.location[0] = p[0] + (k[0][0]+k[3][0])/6.0f + (k[1][0]+k[2][0])/3.0f;
            item.location[1] = p[1] + (k[0][1]+k[3][1])/6.0f + (k[1][1]+k[2][1])/3.0f;
            
            vx = (l[0][0]+l[3][0])/6.0f + (l[1][0]+l[2][0])/3.0f;
            vy = (l[0][1]+l[3][1])/6.0f + (l[1][1]+l[2][1])/3.0f;
            v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
            }
            item.velocity[0] += vx;
            item.velocity[1] += vy;
        }
    }

} // end of class RungeKuttaIntegrator
