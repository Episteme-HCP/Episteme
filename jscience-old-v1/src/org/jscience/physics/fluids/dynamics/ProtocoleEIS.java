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

/*
 * ProtocoleEIS.java
 *
 * Created on 2 of noviembre of 2001, 19:15
 */
package org.jscience.physics.fluids.dynamics;

/**
 * This interface defines methods to be implemented by an EIS (Entorno
 * Integrado of Simulacion / Integrated Simulation Environment) to show
 * information of the ADFC 2.1.
 *
 * @author balrog
 */
public interface ProtocoleEIS {
    /**
     * DOCUMENT ME!
     */
    public void initiate();

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void warning(String texto);

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void error(String texto);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean continueSerializedSimulation();

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void outHTML(String html);

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setEndingTime(String str);

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setStartupTime(String str);

    /**
     * DOCUMENT ME!
     *
     * @param paso DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    public void setActualStep(int paso, int iter);

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setCalculationProgress(int p);

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setLoadProgress(int p);
}
