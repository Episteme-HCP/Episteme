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

/** This class implements a nearest neighbor sampling
  * scheme.
  */
//this class is rebundled after SkyView by NASA which is in public domain
public class LanczosSampler extends org.jscience.astronomy.solarsystem.coordinates.Sampler {
    
    
    public String getName() {
	return "Lanczos"+nLobe+" Sampler";
    }
    
    public String getDescription() {
	return "Sample using smoothly truncated sinc kernel";
    }
    
    /** The number of lobes in the window */
    private int nLobe;
    private double[] out = new double[2];
    
    private double coef, coef2;
    
    /** Weights used internally */
    private double[] xw;
    private double[] yw;
    
    /** Create a Lanczos sample of specified width sampler 
     *  The data will be set later.
     *  
     */
    public void setOrder(int n) {
	init(n);
    }
    
    /** Create a three lobe lanczos sampler as a default width.
     */
    public LanczosSampler() {
	init(3);
    }
    
    
    
    private void init (int n) {
	this.nLobe = n;
	this.coef  = Math.PI/n;
	this.coef2 = coef*Math.PI;
	xw = new double[2*n];
	yw = new double[2*n];
    }
    
    /** Sample a single pixel
      * @param coords  The x,y coordinates of the center of the pixel.
      * @return        The sample value.
      */
    public void sample(int pix) {
	
	double output = 0;
        double[] in = outImage.getCenter(pix);
	trans.transform(in, out);
	
	double x = out[0]-0.5;
	double y = out[1]-0.5;

        int ix = (int) Math.floor(x);
	int iy = (int) Math.floor(y);
	
	double dx = ix - x - (nLobe-1);
	double dy = iy - y - (nLobe-1);
	
	
	
	if (ix <nLobe-1 || y < nLobe-1 || ix >= inWidth-nLobe || iy >= inHeight-nLobe) {
	    return;
	    
	} else {
	    for (int xc=0; xc < 2*nLobe; xc += 1) {
		if (Math.abs(dx) < 1.e-10) {
		    xw[xc] = 1;
		} else {
		    xw[xc] = Math.sin(coef*dx)*Math.sin(Math.PI*dx)/(coef2*dx*dx);
		}
		dx += 1;
	    }
	    
	    for (int yc=0; yc < 2*nLobe; yc += 1) {
		if (Math.abs(dy) < 1.e-10) {
		    yw[yc] = 1;
		} else {
		    yw[yc] = Math.sin(coef*dy)*Math.sin(Math.PI*dy)/(coef2*dy*dy);
		}
		dy += 1;
	    }
	    int p=0,pstart=0;
	    
	    for (int k=0; k<inDepth; k += 1) {
		
		
		p  = (iy-(nLobe-1))*inWidth + ix-(nLobe-1) + k*inWidth*inHeight;
		
		for (int yc=0; yc<2*nLobe; yc += 1) {
		   
		    for (int xc=0; xc<2*nLobe; xc += 1) {
			
			output += inImage.getData(p)*xw[xc]*yw[yc];
			p += 1;
		    }
		    p += inWidth - 2*nLobe;
		}
	    }
	}
	outImage.setData(pix, output);
    }
}

