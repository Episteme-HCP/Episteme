package org.jscience.measure.random;

/**
 * This interface represent a random generator for whole vectors.
 *
 * @author L. Maisonobe
 * @version $Id: RandomVectorGenerator.java,v 1.1 2006-09-07 21:42:30 virtualcall Exp $
 */
public interface RandomVectorGenerator {
    /**
     * Generate a random vector.
     *
     * @return a random vector as an array of double. The generator
     *         <em>will</em> reuse the same array for each call, in order to
     *         save the allocation time, so the user should keep a copy by
     *         himself if he needs so.
     */
    public double[] nextVector();
}
