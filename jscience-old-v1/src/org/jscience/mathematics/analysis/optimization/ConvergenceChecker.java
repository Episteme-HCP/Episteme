package org.jscience.mathematics.analysis.optimization;

/**
 * This interface specifies how to check if a {@link
 * DirectSearchOptimizer direct search method} has converged.
 * <p/>
 * <p>Deciding if convergence has been reached is a problem-dependent
 * issue. The user should provide a class implementing this interface
 * to allow the optimization algorithm to stop its search according to
 * the problem at hand.</p>
 *
 * @author L. Maisonobe
 * @version $Id: ConvergenceChecker.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 */
public interface ConvergenceChecker {
    /**
     * Check if the optimization algorithm has converged on the
     * simplex.
     *
     * @param simplex ordered simplex (all points in the simplex have been
     *        eavluated and are sorted from lowest to largest cost)
     *
     * @return true if the algorithm is considered to have converged
     */
    public boolean converged(PointCostPair[] simplex);
}
