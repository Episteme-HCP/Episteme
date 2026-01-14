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

import skyview.survey.Image;
import org.jscience.astronomy.solarsystem.coordinates.Transformer;
import org.jscience.astronomy.solarsystem.coordinates.Sampler;
import skyview.executive.Settings;

/** This class uses two samplers.  The default sampler is
 *  used but whenever a NaN is returned, the backup sampler is tried.
 *  Normally the default sampler will a high order sampler
 *  and the backup sampler will be simpler.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class ComboSampler extends Sampler {
    
    protected Sampler primary;
    protected Sampler backup;
    protected String  combo;
    
    public ComboSampler() {
	combo = Settings.get("ComboSamplers");
	String[] samplers = combo.split(",");
	primary = Sampler.factory(samplers[0]);
	backup  = Sampler.factory(samplers[1]);
    }

    
    public String getName() {
	return "ComboSampler";
    }
    
    public String getDescription() {
	return "A combination sampler with a primary and backup:"+combo;
    }

    
    
    /** Use the primary unless we get a NaN */
    public void sample(int index) {
	primary.sample(index);
	if (Double.isNaN(outImage.getData(index))) {
	    backup.sample(index);
	}
    }
    
    /** Set the input image for the sampling
      */
    public void setInput(Image inImage) {
	primary.setInput(inImage);
	backup.setInput(inImage);
    }
    
    /** Set the bounds of the output image that may be asked for. */
    public void setBounds(int[] bounds) {
	primary.setBounds(bounds);
	backup.setBounds(bounds);
    }
	
        
    /** Set the output image for the sampling
      */
    public void setOutput(Image outImage) {
	this.outImage = outImage;
	primary.setOutput(outImage);
	backup.setOutput(outImage);
    }
    
    /** Set the transformation information.
     * @param transform  The transformer object.
     * @param pixels     The pixel array of the data.
     */
    public void setTransform(Transformer transform) {
	primary.setTransform(transform);
	backup.setTransform(transform);
    }
    
}
