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
 * ConsolaEIS.java
 *
 * Created on 2 of noviembre of 2001, 20:01
 */
package org.jscience.physics.fluids.dynamics;

/**
 * DOCUMENT ME!
 *
 * @author balrog
 */
public class ConsoleEIS implements ProtocoleEIS {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private Runtime runtime;

/**
     * Creates new ConsolaEIS
     *
     * @param kadfc DOCUMENT ME!
     */
    public ConsoleEIS(KernelADFC kadfc) {
        kernel = kadfc;
        runtime = Runtime.getRuntime();
    }

    /**
     * DOCUMENT ME!
     */
    public void initiate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setLoadProgress(int p) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void error(String text) {
        System.out.println(text);
        System.exit(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void outHTML(String html) {
        System.out.println(html);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void warning(String text) {
        System.out.println(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setStartupTime(String str) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setEndingTime(String str) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setCalculationProgress(int p) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param step DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    public void setActualStep(int step, int iter) {
        if (iter == 0) {
            long memTotal = (runtime.totalMemory() / 1024);
            long memFree = memTotal - (runtime.freeMemory() / 1024);
            System.out.println("//// " + memFree + "K/" + memTotal + "K");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean continueSerializedSimulation() {
        System.out.println(
            "Continue existing serialized simulation : Assumed True");

        return true;
    }
}
