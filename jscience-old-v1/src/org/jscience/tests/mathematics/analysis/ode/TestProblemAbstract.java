package org.jscience.tests.mathematics.analysis.ode;

/**
 * This class is used as the base class of the problems that are integrated
 * during the junit tests for the ODE integrators.
 */
abstract class TestProblemAbstract implements FirstOrderDifferentialEquations,
    Cloneable {
    /** Dimension of the problem. */
    protected int n;

    /** Number of functions calls. */
    protected int calls;

    /** Initial time */
    protected double t0;

    /** Initial state */
    protected double[] y0;

    /** Final time */
    protected double t1;

    /** Error scale */
    protected double[] errorScale;

/**
     * Simple constructor.
     */
    protected TestProblemAbstract() {
        n = 0;
        calls = 0;
        t0 = 0;
        y0 = null;
        t1 = 0;
        errorScale = null;
    }

/**
     * Copy constructor.
     *
     * @param problem problem to copy
     */
    protected TestProblemAbstract(TestProblemAbstract problem) {
        n = problem.n;
        calls = problem.calls;
        t0 = problem.t0;

        if (problem.y0 == null) {
            y0 = null;
        } else {
            y0 = new double[problem.y0.length];
            System.arraycopy(problem.y0, 0, y0, 0, problem.y0.length);
        }

        if (problem.errorScale == null) {
            errorScale = null;
        } else {
            errorScale = new double[problem.errorScale.length];
            System.arraycopy(problem.errorScale, 0, errorScale, 0,
                problem.errorScale.length);
        }

        t1 = problem.t1;
    }

    /**
     * Clone operation.
     *
     * @return a copy of the instance
     */
    public abstract Object clone();

    /**
     * Set the initial conditions
     *
     * @param t0 initial time
     * @param y0 initial state vector
     */
    protected void setInitialConditions(double t0, double[] y0) {
        calls = 0;
        n = y0.length;
        this.t0 = t0;
        this.y0 = new double[y0.length];
        System.arraycopy(y0, 0, this.y0, 0, y0.length);
    }

    /**
     * Set the final conditions.
     *
     * @param t1 final time
     */
    protected void setFinalConditions(double t1) {
        this.t1 = t1;
    }

    /**
     * Set the error scale
     *
     * @param errorScale error scale
     */
    protected void setErrorScale(double[] errorScale) {
        this.errorScale = new double[errorScale.length];
        System.arraycopy(errorScale, 0, this.errorScale, 0, errorScale.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return n;
    }

    /**
     * Get the initial time.
     *
     * @return initial time
     */
    public double getInitialTime() {
        return t0;
    }

    /**
     * Get the initial state vector.
     *
     * @return initial state vector
     */
    public double[] getInitialState() {
        return y0;
    }

    /**
     * Get the final time.
     *
     * @return final time
     */
    public double getFinalTime() {
        return t1;
    }

    /**
     * Get the error scale.
     *
     * @return error scale
     */
    public double[] getErrorScale() {
        return errorScale;
    }

    /**
     * Get the switching functions.
     *
     * @return switching functions
     */
    public SwitchingFunction[] getSwitchingFunctions() {
        return null;
    }

    /**
     * Get the number of calls.
     *
     * @return nuber of calls
     */
    public int getCalls() {
        return calls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param yDot DOCUMENT ME!
     */
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        ++calls;
        doComputeDerivatives(t, y, yDot);
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param yDot DOCUMENT ME!
     */
    abstract public void doComputeDerivatives(double t, double[] y,
        double[] yDot);

    /**
     * Compute the theoretical state at the specified time.
     *
     * @param t time at which the state is required
     *
     * @return state vector at time t
     */
    abstract public double[] computeTheoreticalState(double t);
}
