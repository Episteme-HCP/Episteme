package org.jscience.mathematics.analysis.estimation;

/**
 * This interface represents solvers for estimation problems.
 * <p/>
 * <p>The classes which are devoted to solve estimation problems
 * should implement this interface. The problems which can be handled
 * should implement the {@link EstimationProblem} interface which
 * gather all the information needed by the solver.</p>
 * <p/>
 * <p>The interface is composed only of the {@link #estimate estimate}
 * method.</p>
 *
 * @author L. Maisonobe
 * @version $Id: Estimator.java,v 1.2 2007-10-21 17:38:12 virtualcall Exp $
 * @see EstimationProblem
 */

public interface Estimator {

    /**
     * Solve an estimation problem.
     * <p/>
     * <p>The method should set the parameters of the problem to several
     * trial values until it reaches convergence. If this method returns
     * normally (i.e. without throwing an exception), then the best
     * estimate of the parameters can be retrieved from the problem
     * itself, through the {@link EstimationProblem#getAllParameters
     * EstimationProblem.getAllParameters} method.</p>
     *
     * @param problem estimation problem to solve
     * @throws EstimationException if the problem cannot be solved
     */
    public void estimate(EstimationProblem problem)
            throws EstimationException;

}
