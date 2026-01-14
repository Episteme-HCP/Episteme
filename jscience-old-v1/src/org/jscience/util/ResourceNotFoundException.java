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
 * Thrown to indicate that a specific resource (an image, a sound, etc.)
 * needed to perform the requested operation was not found (while the resource
 * was expected to be present with the distribution of this Antelmann.com
 * framework).
 *
 * @author Holger Antelmann
 *
 * @see Settings#getResource(String)
 * @since 4/6/2002
 */
public class ResourceNotFoundException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -4741573872221219506L;

    /** DOCUMENT ME! */
    Object resource;

/**
     * Creates a new ResourceNotFoundException object.
     */
    public ResourceNotFoundException() {
        super();
    }

/**
     * Creates a new ResourceNotFoundException object.
     *
     * @param message DOCUMENT ME!
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

/**
     * Creates a new ResourceNotFoundException object.
     *
     * @param message  DOCUMENT ME!
     * @param resource DOCUMENT ME!
     */
    public ResourceNotFoundException(String message, Object resource) {
        super(message);
        this.resource = resource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getResourceNotFound() {
        return resource;
    }
}
