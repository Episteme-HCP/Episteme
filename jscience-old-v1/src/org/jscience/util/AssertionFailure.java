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

package org.jscience.util;

/**
 * An unchecked exception representing an Assertion failure.
 * <p/>
 * <p>Assertion failures should be raised when code finds itself in a state that
 * should be impossible. It should not be raised in response to any predictable
 * error condition. Assertion failures indicate that something has gone
 * badly wrong, and that the assumptions under which library code has been
 * developed are not holding.</p>
 * <p/>
 * <p>This extends {@link java.lang.AssertionError}, adding convenient
 * constructors with messages and causes.</p>
 *
 * @author Matthew Pocock
 * @for.user Your application may exit due to one of these being thrown. This usualy
 * indicates that something is badly wrong with library code. It should never
 * be raised in response to invalid arguments to methods, or incorrectly
 * formatted data. It is not your fault. Report the error to the mailing list,
 * or who ever else is responsible for the library code you are using.
 * @for.powerUser Under some rare circumstances, you may wish to catch assertion failures. For
 * example, when debugging library code, or when the success or failure of an
 * opperation is utterly inconsequential. Ignoring assertion failures
 * out-of-hand is a sure-fire way to make your code buggy.
 * @for.developer Raise AssertionFailure in your code when something that should be impossible
 * has happened. For example, if you have checked the alphabet of a symbol list
 * you are working with, and somewhere further down an IllegalSymbolException
 * is raised, then this is an assertion failure.
 * @since 1.4
 */
public class AssertionFailure extends AssertionError {
    /**
     * Creates a new AssertionFailure object.
     *
     * @param message DOCUMENT ME!
     */
    public AssertionFailure(String message) {
        super(message);
    }

    /**
     * Creates a new AssertionFailure object.
     *
     * @param cause DOCUMENT ME!
     */
    public AssertionFailure(Throwable cause) {
        initCause(cause);
    }

    /**
     * Creates a new AssertionFailure object.
     *
     * @param message DOCUMENT ME!
     * @param cause   DOCUMENT ME!
     */
    public AssertionFailure(String message, Throwable cause) {
        this(message);
        initCause(cause);
    }
}
