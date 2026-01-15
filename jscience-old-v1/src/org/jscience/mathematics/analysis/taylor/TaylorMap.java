package org.jscience.mathematics.analysis.taylor;

/**
 * Interface for maps that are evaluable as a <b>TaylorDouble</b> calculation
 * tree.
 * <p/>
 * <b>TaylorDouble</b>s are functions or variables that possess a known Taylor
 * expansion, which among other enables effective integration of ODE's.
 * <p/>
 * Notice, in connection with the <b>TaylorIntegrate</b> ODE solver class, that
 * if variation of a parameter during integration, e.g. doing continuation,
 * is desired, the <b>TaylorMap</b> should keep the parameter as a
 * <b>TaylorDouble</b> instance variable, which enables change of parameter
 * value in the calculation tree used in <b>TaylorIntegrate</b>.
 *
 * @author Tue Lehn-Schioeler
 * @author Laurits H&oslash;jgaard Olesen
 */
public interface TaylorMap {
    /**
     * Set <i>ODE</i> governing dependent variables <b>x</b>. That is, if
     * map is
     * <center>
     * <b>x</b>'(<i>t</i>) = <i>f</i> (<b>x</b>,<i>t</i>)
     * </center>
     * then evaluate <i>f</i> using <b>TaylorDouble</b> objects, and set the
     * components of <i>f</i> as right-hand-side for <i>x[]</i> using
     * <b>TaylorDependant</b> method <i>setOde</i>
     * <p/>
     * <i>x[i].setOde(f[i])</i>
     * <p/>
     * thereby making <i>x[]</i> self dependent, as suitable for the system
     * of <i>ODE</i>s.
     */
    public void evaluate(TaylorDependant[] x, TaylorIndependant t);
}
