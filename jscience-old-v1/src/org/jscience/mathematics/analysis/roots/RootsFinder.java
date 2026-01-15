package org.jscience.mathematics.analysis.roots;

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * This interface specifies root-finding methods for scalar functions.
 *
 * @author L. Maisonobe
 * @version $Id: RootsFinder.java,v 1.3 2007-10-23 18:19:26 virtualcall Exp $
 */
public interface RootsFinder {
    /**
     * Solve a function in a given interval known to contain a root.
     *
     * @param function function for which a root should be found
     * @param checker checker for the convergence of the function
     * @param maxIter maximal number of iteration allowed
     * @param x0 abscissa of the lower bound of the interval
     * @param f0 value of the function the lower bound of the interval
     * @param x1 abscissa of the higher bound of the interval
     * @param f1 value of the function the higher bound of the interval
     *
     * @return true if a root has been found in the given interval
     *
     * @throws MappingException DOCUMENT ME!
     */
    public boolean findRoot(PrimitiveMapping function,
        ConvergenceChecker checker, int maxIter, double x0, double f0,
        double x1, double f1) throws MappingException;

    /**
     * Get the abscissa of the root.
     *
     * @return abscissa of the root
     */
    public double getRoot();
}
