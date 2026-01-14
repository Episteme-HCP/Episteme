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

/** This class implements a nearest neighbor sampling */

//this class is rebundled after SkyView by NASA which is in public domain
public class NNSampler extends org.jscience.astronomy.solarsystem.coordinates.Sampler {
    
//    private int testCounter = 0;
    
    public String getName() {
	return "NNSampler";
    }
    
    public String getDescription() {
	return "Sample using the nearest input pixel value";
    }
    
    
    private double[] out = new double[2];
    
    /** Sample at a specified pixel */
    public void sample(int pix) {
	
	double[] in = outImage.getCenter(pix);
	trans.transform(in, out);
	
//	System.out.println("NNSampler("+testCounter+") "+in[0]+","+in[1]+"   -->  "+out[0]+","+out[1]);
//	testCounter += 1;
	
	// Remember that the pixel value is assumed
	// to be at the center of the pixel not the corner.
	int x = (int) out[0];
	int y = (int) out[1];
	
	if (x < 0 || x >= inWidth || y < 0 || y >= inHeight) {
//	    System.out.println("NNSampler: No data");
	    return;
	} else {
	    for (int k=0; k < inDepth; k += 1) {
		double val = inImage.getData(x+inWidth*y+k*inWidth*inHeight);
		outImage.setData(pix+k*outWidth*outHeight, val);
//	        System.out.println("NNSampler("+k+")=  "+val);
	    }
	}
    }
    
}
