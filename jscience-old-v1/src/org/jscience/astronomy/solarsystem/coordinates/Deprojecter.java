package org.jscience.astronomy.solarsystem.coordinates;

/** This class deprojects a point from a projection plane
 *  onto thecelestial sphere.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public abstract class Deprojecter extends Transformer {
    
    
    /** What is the output dimensionality of a deprojecter? */
    protected int getOutputDimension() {
	return 3;
    }
    
    /** What is the input dimensionality of a deprojecter? */
    protected int getInputDimension() {
	return 2;
    }
}
