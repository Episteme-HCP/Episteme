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

package org.jscience.computing.distributed;


/*
* InteractiveTask.java
*
* Created on 18 April 2001, 12:50
*/
/**
 * A task that can also perform I/O during the task opearation through get and
 * set methods.
 *
 * @author Michael Garvie
 */
public interface InteractiveTask extends Task {
    /**
     * Used to get output from the task
     *
     * @param params can be null if this task doesn't need to know WHAT it has
     *        to output
     *
     * @return Any amount of information can be provided by the task packaged
     *         in an Object.
     */
    public Object get(Object params);

    /**
     * Used to send input to the task
     *
     * @param paramsAndWhat this could be anything, either a single object if
     *        the task knows what to do with it, or a container with
     *        variable->value pairs..
     */
    public void set(Object paramsAndWhat);
}
