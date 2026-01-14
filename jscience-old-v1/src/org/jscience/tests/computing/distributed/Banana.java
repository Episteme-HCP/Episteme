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
 * Banana.java
 *
 * Created on 21 July 2003, 16:56
 */
package org.jscience.tests.distributed;

import org.jscience.computing.distributed.InteractiveTask;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class Banana implements InteractiveTask {
    /** DOCUMENT ME! */
    String runLabel = "I";

    /** DOCUMENT ME! */
    String showLabel = "I";

    /** DOCUMENT ME! */
    Chocolate choco;

/**
     * Creates a new Banana object.
     */
    public Banana() {
        choco = new Chocolate();
    }

/**
     * Creates a new instance of Banana
     *
     * @param rl DOCUMENT ME!
     * @param sl DOCUMENT ME!
     */
    public Banana(String rl, String sl) {
        this();
        runLabel = rl;
        showLabel = sl;
    }

    /**
     * Used to get output from the task
     *
     * @param params can be null if this task doesn't need to know WHAT it has
     *        to output
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object params) {
        return "my banana " + this;
    }

    /**
     * Totally customizable method say throws InterruptedException?
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object params) throws InterruptedException {
        System.out.println("Babanana " + runLabel);

        return "out of bananas";
    }

    /**
     * Used to send input to the task
     *
     * @param paramsAndWhat this could be anything, either a single object if
     *        the task knows what to do with it, or a packet with
     *        variable->value pairs..
     */
    public void set(Object paramsAndWhat) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Banana " + showLabel + " and " + choco;
    }
}
