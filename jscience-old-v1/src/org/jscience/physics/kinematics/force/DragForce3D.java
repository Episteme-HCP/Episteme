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
 * Implements a viscosity/drag force to help stabilize items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class DragForce3D extends AbstractForce {

    private static String[] pnames = new String[] { "DragCoefficient" };
    
    public static final float DEFAULT_DRAG_COEFF = 0.01f;
    public static final float DEFAULT_MIN_DRAG_COEFF = 0.0f;
    public static final float DEFAULT_MAX_DRAG_COEFF = 0.1f;
    public static final int DRAG_COEFF = 0;

    /**
     * Create a new DragForce.
     * @param dragCoeff the drag co-efficient
     */
    public DragForce3D(float dragCoeff) {
        params = new float[] { dragCoeff };
        minValues = new float[] { DEFAULT_MIN_DRAG_COEFF };
        maxValues = new float[] { DEFAULT_MAX_DRAG_COEFF };
    }

    /**
     * Create a new DragForce with default drag co-efficient.
     */
    public DragForce3D() {
        this(DEFAULT_DRAG_COEFF);
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
    public void getForce(ForceItem item) {
        item.force[0] -= params[DRAG_COEFF]*item.velocity[0];
        item.force[1] -= params[DRAG_COEFF]*item.velocity[1];
        item.force[2] -= params[DRAG_COEFF]*item.velocity[2];
      }

} // end of class DragForce
