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

package org.jscience.astronomy.solarsystem.coordinates.projecters;


/** This class implements the Zenithal Equal Area (ZEA)
 *  projection.  Note that the tangent point
 *  is assumed to be at the north pole.
 *  This class assumes preallocated arrays for
 *  maximum efficiency.
 */

import org.jscience.astronomy.solarsystem.coordinates.Projecter;
import org.jscience.astronomy.solarsystem.coordinates.Deprojecter;
import org.jscience.astronomy.solarsystem.coordinates.Transformer;

//this class is rebundled after SkyView by NASA which is in public domain
public class ZeaProjecter extends Projecter {

    /** Get a name for the component */
    public String getName() {
	return "ZeaProjecter";
    }
    
    /** Get a description for the component */
    public String getDescription() {
	return "Zenithal Equal Area projecter";
    }
    
    /** Get this inverse of the transformation */
    public Deprojecter inverse() {
	return new ZeaProjecter.ZeaDeprojecter();
    }

    /** Is this an inverse of some other transformation? */
    public boolean isInverse(Transformer t) {
	return t.getName().equals("ZeaDeprojecter");
    }
    
    /** Project a point from the sphere to the plane.
     *  @param sphere a double[3] unit vector
     *  @param plane  a double[2] preallocated vector.
     */
    public final void transform(double[] sphere, double[] plane) {
	
	if (Double.isNaN(sphere[2])) {
	    plane[0] = Double.NaN;
	    plane[1] = Double.NaN;
	} else {
	    double num = 2*(1-sphere[2]);
	    if (num < 0) {
		num = 0;
	    }
	    double denom = sphere[0]*sphere[0] + sphere[1]*sphere[1];
	    if (denom == 0) {
		plane[0] = 0;
		plane[1] = 0;
	    } else {
	        double ratio = Math.sqrt(num) / 
	                   Math.sqrt(sphere[0]*sphere[0] + sphere[1]*sphere[1]);
	        plane[0] = ratio * sphere[0];
	        plane[1] = ratio * sphere[1];
	    }
	    
	}
    }
    
    
    public class ZeaDeprojecter extends Deprojecter {
	
	/** Get the name of the component */
	public String getName() {
	    return "ZeaDeprojecter";
	}
	
	/** Get the description of the compontent */
	public String getDescription() {
	    return "Zenithal equal area deprojecter";
	}
	
	/** Get the inverse transformation */
	public Projecter inverse() {
	    return ZeaProjecter.this;
	}
	 
        /** Is this an inverse of some other transformation? */
        public boolean isInverse(Transformer t) {
	    return t.getName().equals("ZeaProjecter");
        }
	
	
        /** Deproject a point from the plane to the sphere.
         *  @param plane a double[2] vector in the tangent plane.
         *  @param sphere a preallocated double[3] vector.
         */
        public final void transform(double[] plane, double[] sphere) {
	
	    double r = Math.sqrt(plane[0]*plane[0] + plane[1]*plane[1]);
	
	    if (r > 2 || Double.isNaN(plane[0])) {
	        sphere[0] = Double.NaN;
	        sphere[1] = Double.NaN;
	        sphere[2] = Double.NaN;
	    
	    } else {
	        sphere[2]  = 1 - r*r/2;
	        double ratio = (1-sphere[2]*sphere[2]);
		if (ratio > 0) {
		    ratio = Math.sqrt(ratio)/r;
		} else {
		    ratio = 0;
		}
	        sphere[0] = ratio * plane[0];
	        sphere[1] = ratio * plane[1];
	    }
	}
    }
}
