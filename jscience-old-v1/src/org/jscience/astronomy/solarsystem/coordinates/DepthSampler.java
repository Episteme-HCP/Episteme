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

import skyview.survey.Image;

/** This class handles sampling in the depth dimension */
//this class is rebundled after SkyView by NASA which is in public domain
public class DepthSampler implements skyview.Component {
    
    /** The starting value for first output bin (first input bin goes from 0-1) */
    private double zero;
    
    /** The size of each output bin (in input bins) */
    private double delta;
    
    /** The number of output bins */
    private int n;

    public String getName() {
	return "DepthSampler";
    }
    
    public String getDescription() {
	return "Resample an image in the third (typically energy-like) dimension";
    }
    
    /** Generate an output image with the appropriate pixelization
     *  <p>
     *  Note that there is a conflict in the way SkyView originally presented
     *  3-D data.  It indicated that one could specify bands 1-10 to get 10 bands,
     *  but that is a range of only 9 bands.  This would be OK, except that it
     *  doesn't translate properly to physical units, i.e. 3000-4000 A.  We try
     *  to handle all of this consistently now, so a user needs to specify bands
     *  0-10 to get the same range.
     * 
     * @param zero:  The beginning of the range to be sampled (in depth pixels).
     * @param delta: The size of the output pixels in terms of the input pixels.
     * @param n:     The number of output bins.
     * 
     */
    public DepthSampler(double zero, double delta, int n) {
	this.zero  = zero;
	this.delta = delta;
	this.n     = n;
    }
	 
    /** Sample an image according the the input specification */    
    public Image sample(Image in) {
	
	// User just asked for the input.
	if (n == in.getDepth() && zero == 0 && delta == 1) {
	    return in;
	}
	
	int mx = in.getWidth();
	int my = in.getHeight();
	int mz = in.getDepth();
	
	double[] output = new double[mx*my*n];
	
	for (int tz = 0; tz < n; tz += 1) {
	    
	    double zmin = tz*delta     + zero;
	    double zmax = (tz+1)*delta + zero;
	    
	    // Check special case that output pixel comes from only one input pixel.
	    if (Math.floor(zmin) == Math.floor(zmax)  || (Math.floor(zmin) == zmin && delta == 1)) {
		
		int itz = (int)(Math.floor(zmin));
		
		int inOffset = itz*mx*my;
	        int outOffset = tz*mx*my;
		
		double rat = zmax-zmin;
		for (int ty= 0; ty < my; ty += 1) {
		    for (int tx = 0; tx < mx; tx += 1) {
			
			// Don't go outside the input range.
			if (itz >= 0 && itz < mz) {
			    output[outOffset] = rat*in.getData(inOffset);
			}
			outOffset += 1;
			inOffset  += 1;
		    }
		}
		
	    } else {
		
		// More than one input pixel contributes to the output pixel.
		int itzs = (int) Math.floor(zmin);
		int itze = (int) Math.floor(zmax);
		
		if (zmax == itze) {
		    itze -= 1;
		}
		
		// Loop over the contributing input pixels.
		for (int itz = itzs; itz <= itze; itz += 1) {
		    
		    if (itz < 0 || itz > mz) {
			continue;
		    }
		    // Need to keep within bounds here
		    
		    double rat = 1;
		    
		    // Check if the entire input pixel is not used...
		    if (itz == itzs) {
			rat = 1-(zmin-Math.floor(zmin));
		    } else if (itz == itze) {
			rat = zmax - Math.floor(zmax);
		    }
		    
		    int inOffset  = itz*mx*my;
		    int outOffset = tz*mx*my;
		    
		    for (int ty = 0; ty<my; ty += 1) {
			for (int tx = 0; tx<mx; tx += 1) {
			    
			    // Don't go outside the input range.
			    if (itz >= 0 && itz < mz) {
			        output[outOffset] += rat*in.getData(inOffset);
			    }
			    outOffset += 1;
			    inOffset  += 1;
			}
		    }
		}
	    }
	}
	
	Image out = new Image();
	try {
	    out.initialize(output, in.getWCS(), mx, my, n);
	} catch (Exception e) {
	    throw new Error("Unexpected exception in DepthSampler:"+e);
	}
	return out;
    }
}
