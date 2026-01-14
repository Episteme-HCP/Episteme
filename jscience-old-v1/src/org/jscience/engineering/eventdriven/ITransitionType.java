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

package org.jscience.engineering.eventdriven;

/**
 * <p/>
 * Contains a set of constants used to specify transition types.
 * </p>
 *
 * @author Pete Ford, May 29, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 */

//**********************************************
//
//This package is rebundled after the code from JSpasm
//
// Project Homepage : http://jspasm.sourceforge.net/
// Original Developer : Pete Ford
// Official Domain : CodeXombie.com
//
//**********************************************

public interface ITransitionType {
    /**
     * <p/>
     * NORMAL transition. The entity state changes to the new state and the
     * state code is executed.
     * </p>
     */
    public final static int NORMAL = 0;

    /**
     * <p/>
     * DO NOT EXECUTE transition. The entity state changes to the new state but
     * the state code is not executed.
     * </p>
     */
    public final static int DO_NOT_EXECUTE = 1;

    /**
     * <p/>
     * EXCURSION transition. The entity state changes to the new state and the
     * state code is executed, then the entity state is restored to its
     * starting value.
     * </p>
     */
    public final static int EXCURSION = 2;

    /**
     * <p/>
     * IGNORE transition. The entity state is not changed and no state code is
     * executed.
     * </p>
     */
    public final static int IGNORE = 3;

    /**
     * <p/>
     * Used for bounds checking.
     * </p>
     */
    public final static int MIN_VALUE = 0;

    /**
     * <p/>
     * Used for bounds checking.
     * </p>
     */
    public final static int MAX_VALUE = 3;
}
