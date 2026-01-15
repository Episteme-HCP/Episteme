package org.jscience.mathematics.analysis.optimization;

/**
 * This class holds a point and its associated cost.
 * <p/>
 * <p/>
 * A cost/point pair is not evaluated at build time. Its associated cost set to
 * <code>Double.NaN</code> until it is evaluated.
 * </p>
 *
 * @author Luc Maisonobe
 * @version $Id: PointCostPair.java,v 1.2 2007-10-21 17:45:47 virtualcall Exp $
 * @see CostFunction
 */
public class PointCostPair {
    /**
     * Point coordinates.
     */
    private double[] point;

    /**
     * Cost associated to the point.
     */
    private double cost;

    /**
     * Build a point/cost pair with non-evaluated cost.
     *
     * @param point point coordinates
     */
    public PointCostPair(double[] point) {
        this.point = point;
        cost = Double.NaN;
    }

    /**
     * Reset the point coordinates.
     * <p/>
     * <p/>
     * Resetting the points coordinates automatically reset the cost to
     * non-evaluated
     * </p>
     *
     * @param point new point coordinates
     * @return old point coordinates (this can be re-used to put the
     *         coordinates of another point without re-allocating an array)
     */
    public double[] setPoint(double[] point) {
        double[] oldPoint = this.point;
        this.point = point;
        cost = Double.NaN;

        return oldPoint;
    }

    /**
     * Get the point coordinates.
     *
     * @return point coordinates
     */
    public double[] getPoint() {
        return point;
    }

    /**
     * Set the cost.
     *
     * @param cost cost to store in the instance (can be
     *             <code>Double.NaN</code> to reset the instance to non-evaluated)
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Get the cost.
     *
     * @return cost associated to the point (or <code>Double.NaN</code> if the
     *         instance is not evaluated)
     */
    public double getCost() {
        return cost;
    }

    /**
     * Check if the cost has been evaluated.
     * <p/>
     * <p/>
     * The cost is considered to be non-evaluated if it is
     * <code>Double.isNaN(pair.getCost())</code> would return true
     * </p>
     *
     * @return true if the cost has been evaluated
     */
    public boolean isEvaluated() {
        return !Double.isNaN(cost);
    }
}
