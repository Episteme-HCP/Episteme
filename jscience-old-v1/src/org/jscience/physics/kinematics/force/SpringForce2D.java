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

/**
 * Force function that computes the force acting on ForceItems due to a
 * given Spring.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class SpringForce2D extends AbstractForce {

    private static String[] pnames 
        = new String[] { "SpringCoefficient", "DefaultSpringLength" };
    
    public static final float DEFAULT_SPRING_COEFF = 1E-4f;
    public static final float DEFAULT_MAX_SPRING_COEFF = 1E-3f;
    public static final float DEFAULT_MIN_SPRING_COEFF = 1E-5f;
    public static final float DEFAULT_SPRING_LENGTH = 50;
    public static final float DEFAULT_MIN_SPRING_LENGTH = 0;
    public static final float DEFAULT_MAX_SPRING_LENGTH = 200;
    public static final int SPRING_COEFF = 0;
    public static final int SPRING_LENGTH = 1;
    
    /**
     * Create a new SpringForce.
     * @param springCoeff the default spring co-efficient to use. This will
     * be used if the spring's own co-efficient is less than zero.
     * @param defaultLength the default spring length to use. This will
     * be used if the spring's own length is less than zero.
     */
    public SpringForce2D(float springCoeff, float defaultLength) {
        params = new float[] { springCoeff, defaultLength };
        minValues = new float[] 
            { DEFAULT_MIN_SPRING_COEFF, DEFAULT_MIN_SPRING_LENGTH };
        maxValues = new float[] 
            { DEFAULT_MAX_SPRING_COEFF, DEFAULT_MAX_SPRING_LENGTH };
    }
    
    /**
     * Constructs a new SpringForce instance with default parameters.
     */
    public SpringForce2D() {
        this(DEFAULT_SPRING_COEFF, DEFAULT_SPRING_LENGTH);
    }

    /**
     * Returns true.
     * @see org.jscience.physics.kinematics.force.Force#isSpringForce()
     */
    public boolean isSpringForce() {
        return true;
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    } 
    
    /**
     * Calculates the force vector acting on the items due to the given spring.
     * @param s the Spring for which to compute the force
     * @see org.jscience.physics.kinematics.force.Force#getForce(org.jscience.physics.kinematics.force.Spring)
     */
    public void getForce(Spring2D s) {
        ForceItem item1 = s.item1;
        ForceItem item2 = s.item2;
        float length = (s.length < 0 ? params[SPRING_LENGTH] : s.length);
        float x1 = item1.location[0], y1 = item1.location[1];
        float x2 = item2.location[0], y2 = item2.location[1];
        float dx = x2-x1, dy = y2-y1;
        float r  = (float)Math.sqrt(dx*dx+dy*dy);
        if ( r == 0.0 ) {
            dx = ((float)Math.random()-0.5f) / 50.0f;
            dy = ((float)Math.random()-0.5f) / 50.0f;
            r  = (float)Math.sqrt(dx*dx+dy*dy);
        }
        float d  = r-length;
        float coeff = (s.coeff < 0 ? params[SPRING_COEFF] : s.coeff)*d/r;
        item1.force[0] += coeff*dx;
        item1.force[1] += coeff*dy;
        item2.force[0] += -coeff*dx;
        item2.force[1] += -coeff*dy;
    }
    
} // end of class SpringForce
