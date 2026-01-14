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
 * Uses a gravitational force model to act as a circular "wall". Can be used to
 * construct circles which either attract or repel items.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class SphericalWallForce3D extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final float DEFAULT_GRAV_CONSTANT = -0.1f;
    public static final float DEFAULT_MIN_GRAV_CONSTANT = -1.0f;
    public static final float DEFAULT_MAX_GRAV_CONSTANT = 1.0f;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private float x, y, z, r;

    /**
     * Create a new CircularWallForce.
     * @param gravConst the gravitational constant to use
     * @param x the center x-coordinate of the circle
     * @param y the center y-coordinate of the circle
     * @param z the center z-coordinate of the circle
     * @param r the radius of the circle
     */
    public SphericalWallForce3D(float gravConst,
        float x, float y, float z, float r)
    {
        params = new float[] { gravConst };
        minValues = new float[] { DEFAULT_MIN_GRAV_CONSTANT };
        maxValues = new float[] { DEFAULT_MAX_GRAV_CONSTANT };
        this.x = x; this.y = y; this.z = z;
        this.r = r;
    }
    
    /**
     * Create a new CircularWallForce with default gravitational constant.
     * @param x the center x-coordinate of the circle
     * @param y the center y-coordinate of the circle
     * @param z the center z-coordinate of the circle
     * @param r the radius of the circle
     */
    public SphericalWallForce3D(float x, float y,  float z, float r) {
        this(DEFAULT_GRAV_CONSTANT,x,y,z,r);
    }
    
    /**
     * Returns true.
     * @see org.jscience.physics.kinematics.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return true;
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#getForce(org.jscience.physics.kinematics.force.ForceItem)
     */
    public void getForce(ForceItem3D item) {
        float[] n = item.location;
        float dx = x-n[0];
        float dy = y-n[1];
        float dz = z-n[2];
        float d = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
        float dr = r-d;
        float c = dr > 0 ? -1 : 1;
        float v = c*params[GRAVITATIONAL_CONST]*item.mass / (dr*dr);
        if ( d == 0.0 ) {
            dx = ((float)Math.random()-0.5f) / 50.0f;
            dy = ((float)Math.random()-0.5f) / 50.0f;
            dz = ((float)Math.random()-0.5f) / 50.0f;
            d  = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
        }
        item.force[0] += v*dx/d;
        item.force[1] += v*dy/d;
        item.force[2] += v*dz/d;
    }

} // end of class CircularWallForce
