package org.jscience.astronomy.solarsystem.coordinates;

/** This class is thrown when an error
 *  occurs relating to transformations among
 *  frames.
 */
//this class is rebundled after SkyView by NASA which is in public domain
public class TransformationException extends Exception {
    public TransformationException(){
    }
    public TransformationException(String msg) {
	super(msg);
    }
}
