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
 * Task.java
 *
 * Created on 18 April 2001, 12:49
 */
package org.jscience.computing.distributed;

import java.io.Serializable;


/**
 * A Task represents a single task that takes a paramater when it starts and
 * returns a value when it terminates, both can be vectors..
 *
 * @author Michael Garvie
 */
public interface Task extends Serializable {
    /**
     * Totally customizable method with code to be executed by the the
     * interactive task clients.
     *
     * @param params Generic parameters to start the run method with. The
     *        implemented InteractiveTaskClient will always start the run
     *        method with a null parameter so any configuring of the task must
     *        be done previous to the run method being executed.
     *
     * @return Final result of the run method.  This could be the solution to a
     *         problem for example.
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public Object run(Object params) throws InterruptedException;
}
