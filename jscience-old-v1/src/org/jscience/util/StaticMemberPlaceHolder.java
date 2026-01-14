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

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import java.lang.reflect.Field;


/**
 * DOCUMENT ME!
 *
 * @author Matthew Pocock
 */
public class StaticMemberPlaceHolder implements Serializable {
    /** DOCUMENT ME! */
    private String className;

    /** DOCUMENT ME! */
    private String fieldName;

/**
     * Creates a new StaticMemberPlaceHolder object.
     *
     * @param field DOCUMENT ME!
     */
    public StaticMemberPlaceHolder(Field field) {
        this.className = field.getDeclaringClass().getName();
        this.fieldName = field.getName();
    }

/**
     * Creates a new StaticMemberPlaceHolder object.
     */
    protected StaticMemberPlaceHolder() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ObjectStreamException DOCUMENT ME!
     * @throws InvalidObjectException DOCUMENT ME!
     */
    public Object readResolve() throws ObjectStreamException {
        try {
            Class c = Class.forName(className);
            Field f = c.getDeclaredField(fieldName);

            return f.get(null);
        } catch (Exception e) {
            throw new InvalidObjectException("Unable to retrieve static field " +
                fieldName + "for class " + className + " because:\n" +
                e.getMessage());
        }
    }
}
