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

import java.util.ArrayList;

/**
 * Represents a spring in a force simulation.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class Spring3D extends Spring {
    private static SpringFactory s_factory = new SpringFactory();
    
    /**
     * Retrieve the SpringFactory instance, which serves as an object pool
     * for Spring instances.
     * @return the Spring Factory
     */
    public static SpringFactory getFactory() {
        return s_factory;
    }
    
    /**
     * Create a new Spring instance
     * @param fi1 the first ForceItem endpoint
     * @param fi2 the second ForceItem endpoint
     * @param k the spring tension co-efficient
     * @param len the spring's resting length
     */
    public Spring3D(ForceItem3D fi1, ForceItem3D fi2, float k, float len) {
        item1 = fi1;
        item2 = fi2;
        coeff = k;
        length = len;
    }
    
    /** The first ForceItem endpoint */
    public ForceItem3D item1;
    /** The second ForceItem endpoint */
    public ForceItem3D item2;

    /**
     * The SpringFactory is responsible for generating Spring instances
     * and maintaining an object pool of Springs to reduce garbage collection
     * overheads while force simulations are running.
     */
    public static final class SpringFactory {
        private int maxSprings = 10000;
        private ArrayList springs = new ArrayList();
        
        /**
         * Get a Spring instance and set it to the given parameters.
         */
        public Spring3D getSpring(ForceItem3D f1, ForceItem3D f2, float k, float length) {
            if ( springs.size() > 0 ) {
                Spring3D s = (Spring3D)springs.remove(springs.size()-1);
                s.item1 = f1;
                s.item2 = f2;
                s.coeff = k;
                s.length = length;
                return s;
            } else {
                return new Spring3D(f1,f2,k,length);
            }
        }
        /**
         * Reclaim a Spring into the object pool.
         */
        public void reclaim(Spring3D s) {
            s.item1 = null;
            s.item2 = null;
            if ( springs.size() < maxSprings )
                springs.add(s);
        }
    } // end of inner class SpringFactory

} // end of class Spring
