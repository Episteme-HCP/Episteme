/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
