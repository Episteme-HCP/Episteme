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

package org.jscience.astronomy.solarsystem.coordinates.samplers;

/** This class implements a linear interpolation sampling
  * scheme.
  */
//this class is rebundled after SkyView by NASA which is in public domain
public class LISampler extends org.jscience.astronomy.solarsystem.coordinates.Sampler {
    
    public String getName() {
	return "LISampler";
    }
    
    public String getDescription() {
	return "Sample using a bi-linear interpolation";
    }
    
    private double[] out =  new double[2];
 
    /** Sample at a specified pixel */
    public void sample(int pix) {
	
	
	double[] in = outImage.getCenter(pix);
	trans.transform(in, out);
	
	// The values of the pixels are assumed to be
	// at the center of the pixel.  Thus we cannot
	// interpolate past outermost half-pixel edge of the
	// map.
	double x = out[0] - 0.5;
	double y = out[1] - 0.5;
	
        if (x < 0 || x > inWidth-1 || y < 0 || y > inHeight-1) {
	    return;
	} else {
	    int ix = (int) Math.floor(x);
	    int iy = (int) Math.floor(y);
	    double dx = x-ix;
	    double dy = y-iy;
	
	    for (int k=0; k < inDepth; k += 1) {
	        int inOffset = k*inWidth*inHeight;
		int outOffset = k*outWidth*outHeight;
	
	        outImage.setData(pix+outOffset,  
		              (1-dx)*(1-dy)* inImage.getData(ix   +  inWidth*iy     + inOffset) + 
	                        dx  *(1-dy)* inImage.getData(ix+1 +  inWidth*iy     + inOffset) +
	                      (1-dx)*  dy  * inImage.getData(ix   +  inWidth*(iy+1) + inOffset) +
	                        dx  *  dy  * inImage.getData(ix+1 +  inWidth*(iy+1) + inOffset));
	    }
	}
    }
    
}
