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

import org.jscience.util.Named;


/** This class defines a non-linear distortion in the image plane.
    Normally the forward distortion converts from a fiducial
    projection plane to some distorted coordinates.  The reverse
    distortion transforms from the distorted coordinates back
    to the fiducial coordinates.
  */
//this class is rebundled after SkyView by NASA which is in public domain
public abstract class Distorter extends Transformer implements Named {
    
    /** A name for this object */
    public String getName() {
	return "Generic Distorter";
    } 
    
    /** What does this object do? */
    public String getDescription() {
	return "Placeholder for distortions in projection plane";
    }
    
    public abstract Distorter inverse();
    
    /** What is the output dimensionality of a Distorter? */
    protected int getOutputDimension() {
	return 2;
    }
    
    /** What is the input dimensionality of a Distorter? */
    protected int getInputDimension() {
	return 2;
    }
    
}
