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

package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.modules.VectorSpace;


/**
 * This interface defines an algebra.
 *
 * @author Mark Hale
 * @version 1.0
 */

//this is only linear algebra (or field algebra) here.
//we should have a parent interface with empty contents
//see http://en.wikipedia.org/wiki/Algebra_over_a_field
//http://en.wikipedia.org/wiki/Abstract_algebra
//http://en.wikipedia.org/wiki/Algebra
//http://en.wikipedia.org/wiki/Linear_algebra
public interface Algebra {
/**
     * This interface defines a member of an algebra.
     */
    interface Member extends VectorSpace.Member, Ring.Member {
    }
}
