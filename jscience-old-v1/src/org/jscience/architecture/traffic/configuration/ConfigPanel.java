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

package org.jscience.architecture.traffic.configuration;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class ConfigPanel extends Panel {
    /** DOCUMENT ME! */
    ConfigDialog confd;

/**
     * Creates a new ConfigPanel object.
     *
     * @param cd DOCUMENT ME!
     */
    public ConfigPanel(ConfigDialog cd) {
        confd = cd;
        setLayout(null);
    }

    /**
     * Returns the configuration dialog this panal is part of
     *
     * @return DOCUMENT ME!
     */
    public ConfigDialog getConfigDialog() {
        return confd;
    }

    /**
     * Sets the configuration dialog this panel is part of
     *
     * @param cd DOCUMENT ME!
     */
    public void setconfigDialog(ConfigDialog cd) {
        confd = cd;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
    }

    /**
     * DOCUMENT ME!
     */
    public void ok() {
    }
}
