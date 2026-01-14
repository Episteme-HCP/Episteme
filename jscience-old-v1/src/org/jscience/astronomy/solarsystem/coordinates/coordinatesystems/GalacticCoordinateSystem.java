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

package org.jscience.astronomy.solarsystem.coordinates.coordinatesystems;

import org.jscience.astronomy.solarsystem.coordinates.CoordinateSystem;
import org.jscience.astronomy.solarsystem.coordinates.Rotater;
import org.jscience.util.Named;

/** The class defining Galactic coordinate system.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class GalacticCoordinateSystem extends CoordinateSystem
  implements Named {
    
    /**
     * Get the name of this object.
     */
    public String getName() {
	return "Galactic";
    }
    
    /**
     * Get a description of the object.
     */
    public String getDescription() {
	return "Coordinate system based upon the orientation and center of the Galaxy";
    }
    
    /**
     * Return the rotation associated with the coordinate system.
     */
      
    public Rotater getRotater() {
	double[] poles = new double[] {122.931918, 27.128251, 192.859481};
	return new Rotater("ZYZ", Math.toRadians(poles[2]),
			          Math.toRadians(90-poles[1]),
			          Math.toRadians(180-poles[0]));
    }
}
