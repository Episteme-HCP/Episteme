package org.jscience.mathematics.analysis.optimization;

/**
 * This interface represents a cost function to be minimized.
 *
 * @author Luc Maisonobe
 * @version $Id: CostFunction.java,v 1.3 2007-10-23 18:19:20 virtualcall Exp $
 */
public interface CostFunction {
    /**
     * Compute the cost associated to the given parameters array.
     *
     * @param x parameters array
     *
     * @return cost associated to the parameters array
     *
     * @throws CostException if no cost can be computed for the parameters
     *
     * @see PointCostPair
     */
    public double cost(double[] x) throws CostException;
}
