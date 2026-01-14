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

/** An ecliptic coordinate system in a Julian frame.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class EclipticCoordinateSystem extends CoordinateSystem
  implements Named {
    
    private double epoch;
    private double elon;
    
    /** 
     * Get the name of this component.
     */
    public String getName() {
	return "E"+epoch;
    }
    
    /** 
     * Get a description of this component.
     */
    public String getDescription() {
	return "A coordinate system with the ecliptic as the equator at epoch of equinox"+epoch;
    }
	
	
    /** Get an Ecliptic Coordinate system at a given epoch.
     *  @epoch The epoch of the equinox of the coordinate system in calendar years.
     */
    public EclipticCoordinateSystem(double epoch) {
	this(epoch, 0);
    }
    
    /** Get an Ecliptic coordinate system where the 0 of longitude
     *  can be reset.
     *  @param epoch The epoch of the equinox.
     *  @param elon  The longitude in a standard coordinate system
     *               at which the prime meridian should be placed.
     */
    
    protected EclipticCoordinateSystem(double epoch, double elon) {
	
	this.epoch = epoch;
	this.elon  = elon;
    }
      
    public Rotater getRotater() {
        double DAS2R = 4.84813681109535993589914102e-6;

        //   Interval between basic epoch J2000.0 and current epoch (JC) */
        double t = ( epoch - 2000 ) / 100;
        //System.out.println("Ecliptic Coordinate System set, epoch=" + epoch);

 
        //   Mean obliquity 
        double eps0 = DAS2R * ( 84381.448 + ( -46.8150 + ( -0.00059 + 0.001813 * t ) * t ) * t );
 
        //   Get the matrix
	Rotater r1 = new JulianCoordinateSystem(epoch).getRotater();
	Rotater r2 = new Rotater("XZ", eps0, elon, 0.);
	if (r1 == null) {
	    return r2;
	} else {
	    return r1.add(r2);
	}
    }
}
