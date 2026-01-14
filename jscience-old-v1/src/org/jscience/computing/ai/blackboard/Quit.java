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

/* Quit.java */
package org.jscience.computing.ai.blackboard;

/**
 * Quit class.  This class implements a terminating rule action, following
 * the execution of an instantiation of this class, the KBS will abandon
 * operation.
 *
 * @author:   Paul Brown
 * @version:  1.2, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Quit implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the system controller. */
    private Controller controller;

    /** A reference to the containing knowledge source. */
    private KnowledgeSource ks;

/**
         * Constructs a quit action.
         * @param controller a reference to the system controller
         * @param ks a reference to the containing knowledge source
         */
    public Quit(Controller controller, KnowledgeSource ks) {
        this.controller = controller;
        this.ks = ks;
    }

    /**
     * Executes the quit action, terminating the KBS.
     *
     * @param arg DOCUMENT ME!
     */
    public void execute(Object arg) {
        controller.clear();
        ks.clearAgenda();
    }
}
