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
 * Represents a point particle in a force simulation, maintaining values for
 * mass, forces, velocity, and position.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class ForceItem2D extends ForceItem implements Cloneable {
    
    /**
     * Create a new ForceItem.
     */
    public ForceItem2D() {
        mass = 1.0f;
        force = new float[] { 0.f, 0.f };
        velocity = new float[] { 0.f, 0.f };
        location = new float[] { 0.f, 0.f };
        plocation = new float[] { 0.f, 0.f };
        k = new float[4][2];
        l = new float[4][2];
    }
    
    /**
     * Clone a ForceItem.
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        ForceItem2D item = new ForceItem2D();
        item.mass = this.mass;
        System.arraycopy(force,0,item.force,0,2);
        System.arraycopy(velocity,0,item.velocity,0,2);
        System.arraycopy(location,0,item.location,0,2);
        System.arraycopy(plocation,0,item.plocation,0,2);
        for ( int i=0; i<k.length; ++i ) {
            System.arraycopy(k[i],0,item.k[i],0,2);
            System.arraycopy(l[i],0,item.l[i],0,2);
        }
        return item;
    }
    
    /**
     * Checks a ForceItem to make sure its values are all valid numbers
     * (i.e., not NaNs).
     * @param item the item to check
     * @return true if all the values are valid, false otherwise
     */
    public static final boolean isValid(ForceItem2D item) {
        return
          !( Float.isNaN(item.location[0])  || Float.isNaN(item.location[1])  || 
             Float.isNaN(item.plocation[0]) || Float.isNaN(item.plocation[1]) ||
             Float.isNaN(item.velocity[0])  || Float.isNaN(item.velocity[1])  ||
             Float.isNaN(item.force[0])     || Float.isNaN(item.force[1]) );
    }
    
} // end of class ForceItem
