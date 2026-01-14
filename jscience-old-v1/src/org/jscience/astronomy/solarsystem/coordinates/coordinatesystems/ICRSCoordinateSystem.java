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

/** A class defining the ICRS coordinate system.
 *  This should probably be the reference coordinate system, but it
 *  differs only very slightly from the J2000 coordinates.
 *  We are assuming the the J2000 coordinate system is FK5 based.
 *  This may not be true for at the 50 mas level.
 * 
 *  According to Feissel and Mignard (1998) the rotation angles for
 *  the three axes are between the ICRS coordinates and FK5
 *     (-19.9, -9.1, -22.9)
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class ICRSCoordinateSystem extends CoordinateSystem
  implements Named {
      
      
    private static double[] angles = {
	Math.toRadians(-.0199 / 3600),
	Math.toRadians(-.0091 / 3600),
	Math.toRadians(+.0229 / 3600)
    };
      
    /**
     * Get the name of this object.
     */
    public String getName() {
	return "ICRS";
    }
    
    /**
     * Get a description of the object.
     */
    public String getDescription() {
	return "Non-precessing equatorial coordinate system";
    }
    
    /**
     * Return the rotation associated with the coordinate system.
     */
      
    public Rotater getRotater() {
	return new Rotater("XYZ", angles[0], angles[1], angles[2]);
    }
}
