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

package org.jscience.astronomy.solarsystem.coordinates.distorters;

import org.jscience.astronomy.solarsystem.coordinates.Transformer;
import org.jscience.astronomy.solarsystem.coordinates.Distorter;

/** This class implements the NEAT radial distortion.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class NeatDistorter extends Distorter {
    
    private  double x0, y0, scale;
    
    public String getName() {
	return "NeatDistorter";
    }
    
    public String  getDescription() {
	return "Invert a radial cubic distortion (find x from y where y=x+d x^3)";
    }
    
    public Distorter inverse() {
	return new NeatInvDistorter();
    }
    
    public boolean isInverse(Transformer test) {
	try {
	    return test.inverse().inverse() == this;
	} catch (Exception e) {
	    throw new Error("Unexpected exception in NeatDistorter.isInverse:"+e);
	}
    }
    
    public NeatDistorter(double x0, double y0, double scale) {
	this.x0     = x0;
	this.y0     = y0;
	this.scale  = scale;
    }
    
    public void transform(double[] in, double[] out) {
	
	double x = in[0];
	double y = in[1];
	    
	double dx = (x-x0);
	double dy = (y-y0);
	    
	double r = Math.sqrt(dx*dx + dy*dy);
	    
	if (r > 0) {
	    
	    // Inverse of the cubic transformation per Mathematica
            double athird = 1./3.;
            double t = 9*Math.sqrt(scale)*r + Math.sqrt(12 + 81*scale*r*r);
      
            double rp = (-Math.pow(2/(3*t), athird) + Math.pow(t/18, athird)) /
                         Math.sqrt(scale);
	}
	out[0] = dx + x0;
	out[1] = dy + y0;
	
    }
    
    public class NeatInvDistorter extends Distorter {
	
	public String getName() {
	    return "NeatInvDistorter";
	}
	public String getDescrition() {
	    return "Perform radial distortion y = x + d x^3";
	}
	
        public boolean isInverse(Transformer test) {
	    return test == NeatDistorter.this;
	}
	
	public Distorter inverse() {
	    return NeatDistorter.this;
	}
	
	public void transform(double[] in, double[] out) {
	    
	    double x = in[0];
	    double y = in[1];
	    
	    double dx = (x-x0);
	    double dy = (y-y0);
	    
	    double r = Math.sqrt(dx*dx + dy*dy);
	    
	    if (r > 0) {
	    
	        double rp = r + scale * r*r*r;
	    
	        dx = dx*rp/r;
		dy = dy*rp/r;
	    }
	    out[0] = dx + x0;
	    out[1] = dy + y0;
	}
    }
   
}
	
