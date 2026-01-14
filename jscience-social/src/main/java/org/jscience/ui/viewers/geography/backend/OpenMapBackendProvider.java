/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.geography.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for OpenMap library.
 * Java toolkit for viewing and manipulating geospatial data.
 * 
 * @see <a href="http://openmap-java.org/">OpenMap</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenMapBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "map";
    }

    @Override
    public String getId() {
        return "openmap";
    }

    @Override
    public String getName() {
        return "OpenMap";
    }

    @Override
    public String getDescription() {
        return "Java toolkit for viewing and manipulating geospatial data.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("com.bbn.openmap.MapBean");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 40; // Good priority for GIS applications
    }

    @Override
    public Object createBackend() {
        // Placeholder - actual implementation would create OpenMap wrapper
        return null;
    }
}
