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
 * Interface for force functions in a force simulation.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public interface Force {

    /**
     * Initialize this force function.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim);

    /**
     * Returns the number of parameters (e.g., gravitational constant or
     * spring force coefficient) affecting this force function. 
     * @return the number of parameters
     */
    public int getParameterCount();

    /**
     * Returns the specified, numbered parameter.
     * @param i the index of the parameter to return
     * @return the parameter value
     */
    public float getParameter(int i);
    
    /**
     * Get the suggested minimum value for a parameter. This value is not
     * strictly enforced, but is used by interface components that allow force
     * parameters to be varied.
     * @param param the parameter index
     * @return the suggested minimum value.
     */
    public float getMinValue(int param);
    
    /**
     * Get the suggested maximum value for a parameter. This value is not
     * strictly enforced, but is used by interface components that allow force
     * parameters to be varied.
     * @param param the parameter index
     * @return the suggested maximum value.
     */
    public float getMaxValue(int param);
    
    /**
     * Gets the text name of the requested parameter.
     * @param i the index of the parameter
     * @return a String containing the name of this parameter
     */
    public String getParameterName(int i);

    /**
     * Sets the specified parameter value.
     * @param i the index of the parameter
     * @param val the new value of the parameter
     */
    public void setParameter(int i, float val);
    
    /**
     * Set the suggested minimum value for a parameter. This value is not
     * strictly enforced, but is used by interface components that allow force
     * parameters to be varied.
     * @param i the parameter index
     * @param val the suggested minimum value to use
     */
    public void setMinValue(int i, float val);
    
    /**
     * Set the suggested maximum value for a parameter. This value is not
     * strictly enforced, but is used by interface components that allow force
     * parameters to be varied.
     * @param i the parameter index
     * @return the suggested maximum value to use
     */
    public void setMaxValue(int i, float val);
    
    /**
     * Indicates if this force function will compute forces
     * on Spring instances.
     * @return true if this force function processes Spring instances 
     */
    public boolean isSpringForce();
    
    /**
     * Indicates if this force function will compute forces
     * on ForceItem instances
     * @return true if this force function processes Force instances 
     */
    public boolean isItemForce();
    
    /**
     * Updates the force calculation on the given ForceItem
     * @param item the ForceItem on which to compute updated forces
     */
    public void getForce(ForceItem item);
    
    /**
     * Updates the force calculation on the given Spring. The ForceItems
     * attached to Spring will have their force values updated appropriately.
     * @param spring the Spring on which to compute updated forces
     */
    public void getForce(Spring spring);
    
} // end of interface Force
