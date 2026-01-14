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

package org.jscience.tests.computing.ai.expertsystem.monkeys;

/**
 * Useful constants for the monkey and bananas problem.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  19 Jul 2000
 */
public interface Constants {
    /** Goal status = satisfied. */
    public static final int SATISFIED = 0;

    /** Goal status = active. */
    public static final int ACTIVE = 1;

    /** Goal type: to be AT somewhere. */
    public static final int AT = 0;

    /** Goal type: to HOLD something. */
    public static final int HOLD = 1;

    /** Goal type: to be ON something. */
    public static final int ON = 2;

    /** Weight constants: light. */
    public static final int LIGHT = 0;

    /** Weight constants: heavy. */
    public static final int HEAVY = 1;

    /** Common objects: the floor. */
    public static final PhysicalObject FLOOR = new PhysicalObject("floor", HEAVY);

    /** Common objects: the ceiling. */
    public static final PhysicalObject CEILING = new PhysicalObject("ceiling",
            HEAVY);
}
