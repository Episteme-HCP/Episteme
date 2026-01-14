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

package org.jscience.architecture.traffic.xml;

/**
 * The GLD user documentation defines InstantiationAssistant as  "Your friendly
 * class creator who gives you a warm and fuzzy feeling inside". The technical
 * documentation says that it is just another  interface which classes can
 * implement if they can create classes that the XML Parser cannot
 * instantiate. For instance non-static inner classes. The parser has to
 * figure out which InstantiationAssistant to use : for an inner class it will
 * normally use the parent class.
 */
public interface InstantiationAssistant {
    /**
     * Tell our parser if we can create an instance of a certain class
     *
     * @param request The class of the object that the parser wants
     *
     * @return Can we create that?
     */
    public boolean canCreateInstance(Class request);

    /**
     * Create an instance of a certain class for our parser
     *
     * @param request The class of the object that the parser wants
     *
     * @return An instance of that object
     *
     * @throws ClassNotFoundException If we don't know that class
     * @throws InstantiationException In case something goes wrong
     * @throws IllegalAccessException DOCUMENT ME!
     */
    public Object createInstance(Class request)
        throws ClassNotFoundException, InstantiationException,
            IllegalAccessException;
}
