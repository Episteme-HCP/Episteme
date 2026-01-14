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

package org.jscience.astronomy.solarsystem.coordinates;

import java.lang.reflect.*;

/** This class implements projection algorithms to/from a projection
  * plane and the unit sphere.  Data on the unit sphere is normally
  * represented as a unit-three vector.  Data in a projection
  * plane is normally represented as a two-ple.  Note that the projection
  * is usually broken into two pieces: a transformation to a convenient
  * location on the celestial sphere (e.g., for a TAN projection,
  * the unit vectors are rotated so that the reference pixel is at the pole),
  * and a functional transformation from the sphere to the plane.
  * The project and deproject functions address
  * this later element, while the rotation needed is encoded in
  * in the eulerRotationMatrix.
  */
//this class is rebundled after SkyView by NASA which is in public domain
public class Projection {
 
    /** Unit vector for standard reference point for this
      * projection.  I.e., we need to rotate to this
      * point to use the projection algorithms.
      * For most azimuthal projections this is the North pole,
      * but it can be the coordinate origin for other projections
      * and can in principle be anything...
      */
    private double[]		refProj= new double[]{0,Math.PI/2};
    
    private Rotater		rotation;
    private Projecter		proj;
    private Distorter           dist = null;
    
    /** Get the rotation that needs to be performed before the rotation. */
    public  Rotater getRotater() {
	return rotation;
    }
    
    /** Update the Rotater...*/
    public void setRotater(Rotater rot) {
	rotation = rot;
    }
    
    /** Get the projection algorithm associated with this rotation. */
    public Projecter getProjecter() {
	return proj;
    }
    
    /** Get any distortion in the plane associated with this projection. */
    public Distorter getDistorter() {
	return dist;
    }
    
    protected void setDistorter(Distorter dist) {
	this.dist = dist;
    }
    
    /* Is there a special location for the reference pixel? Other than the pole. */
    protected double[] specialReference() {
	return null;
    }
    
    /** Get the correct projection */
    public Projection(String type) throws TransformationException {
	
	if (!type.equals("Car")  && !type.equals("Ait")  &&!type.equals("Csc")) {
	    throw new TransformationException("Invalid non-parametrized projection:"+type);
	}
	String projClass   = "org.jscience.astronomy.solarsystem.coordinates.projecter."+type+"Projecter";
	
	try {
	    this.proj =  (Projecter) Class.forName(projClass).newInstance();
	} catch (Exception e) {
	    throw new TransformationException("Error creating non-parametrized projection:"+type);
	}
	this.rotation = null;
    }
    
    /** Create the specified projection.
     *  @param	type	   The three character string defining
     *                     the projection.
     *  @param  reference  The reference point for the projection (as a coordinate pair)
     *
     *  @throw  ProjectionException when the requested projection
     *          cannot be found or does not have an appropriate constructor.
     */
	
    public Projection (String type, double[] reference) 
      throws TransformationException {
	 
	String projClass    = "org.jscience.astronomy.solarsystem.coordinates.projecter."+type+"Projecter";
	
	try {
	    this.proj =  (Projecter) Class.forName(projClass).newInstance();
	} catch (Exception e) {
	    throw new TransformationException("Cannot create parametrized projection:"+type+"\n"+e);
	}
	  
	// We need to rotate the reference pixel to the pole.
//	rotation = new Rotater("ZYZ", Math.PI+reference[0], -(Math.PI/2 - reference[1]), Math.PI/2); 
	rotation = new Rotater("ZYZ", reference[0],  -reference[1]+Math.PI/2, Math.PI/2); 
	if (specialReference() != null) {
	    double[] spec = specialReference();
	    rotation = rotation.add(new Rotater("ZYZ", spec[0], spec[1], spec[2]));
	}
    }
}
