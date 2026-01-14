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

import java.awt.geom.Line2D;

/**
 * Uses a gravitational force model to act as a "wall". Can be used to
 * construct line segments which either attract or repel items.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class WallForce2D extends AbstractForce {

    private static String[] pnames = new String[] { "GravitationalConstant" };
    
    public static final float DEFAULT_GRAV_CONSTANT = -0.1f;
    public static final float DEFAULT_MIN_GRAV_CONSTANT = -1.0f;
    public static final float DEFAULT_MAX_GRAV_CONSTANT = 1.0f;
    public static final int GRAVITATIONAL_CONST = 0;
    
    private float x1, y1, x2, y2;
    private float dx, dy;
    
    /**
     * Create a new WallForce.
     * @param gravConst the gravitational constant of the wall
     * @param x1 the first x-coordinate of the wall
     * @param y1 the first y-coordinate of the wall
     * @param x2 the second x-coordinate of the wall
     * @param y2 the second y-coordinate of the wall
     */
    public WallForce2D(float gravConst,
        float x1, float y1, float x2, float y2) 
    {
        params = new float[] { gravConst };
        minValues = new float[] { DEFAULT_MIN_GRAV_CONSTANT };
        maxValues = new float[] { DEFAULT_MAX_GRAV_CONSTANT };
        
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        dx = x2-x1;
        dy = y2-y1;
        float r = (float)Math.sqrt(dx*dx+dy*dy);
        if ( dx != 0.0 ) dx /= r;
        if ( dy != 0.0 ) dy /= r;
    }
    
    /**
     * Create a new WallForce with default gravitational constant.
     * @param x1 the first x-coordinate of the wall
     * @param y1 the first y-coordinate of the wall
     * @param x2 the second x-coordinate of the wall
     * @param y2 the second y-coordinate of the wall
     */
    public WallForce2D(float x1, float y1, float x2, float y2) {
        this(DEFAULT_GRAV_CONSTANT,x1,y1,x2,y2);
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
    public void getForce(ForceItem2D item) {
        float[] n = item.location;
        int ccw = Line2D.relativeCCW(x1,y1,x2,y2,n[0],n[1]);
        float r = (float)Line2D.ptSegDist(x1,y1,x2,y2,n[0],n[1]);
        if ( r == 0.0 ) r = (float)Math.random() / 100.0f;
        float v = params[GRAVITATIONAL_CONST]*item.mass / (r*r*r);
        if ( n[0] >= Math.min(x1,x2) && n[0] <= Math.max(x1,x2) )
            item.force[1] += ccw*v*dx;
        if ( n[1] >= Math.min(y1,y2) && n[1] <= Math.max(y1,y2) )
            item.force[0] += -1*ccw*v*dy;
    }

} // end of class WallForce
