package org.jscience.measure.random;

/**
 * This interface represent a random generator for scalars.
 * Additionally, normalized generator should provide null mean and unit standard deviation
 * scalars.
 *
 * @author L. Maisonobe
 * @version $Id: RandomGenerator.java,v 1.2 2007-10-21 17:46:13 virtualcall Exp $
 */
public interface RandomGenerator {

    /**
     * Generate a random scalar with null mean and unit standard deviation.
     * <p/>
     * <p/>
     * This method does <strong>not</strong> specify the shape of the
     * distribution, it is the implementing class that provides it. The only
     * contract here is to generate numbers with null mean and unit standard
     * deviation.
     * </p>
     *
     * @return a random scalar
     */
    public double nextDouble();

}
