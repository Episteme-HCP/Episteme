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
