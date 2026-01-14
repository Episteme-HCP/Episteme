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

package org.jscience.chemistry.gui.extended.molecule;

import java.util.Enumeration;
import java.util.Properties;


/**
 * This is a base class which the Atom and Bond classes include. It
 * provides the common functionality of property pairs which can be added to
 * the end of either an Atom or a Bond
 *
 * @author Scott Schwab
 * @version 1.0 6/27/97
 */
public class PropAttributes {
    /** The property to be maninpulate */
    public Properties prop;

/**
     * Default constuctor
     */
    public PropAttributes() {
        prop = new Properties();
    }

/**
     * Constructor if a property exist, create a copy with (clone)
     *
     * @param prop DOCUMENT ME!
     */
    public PropAttributes(Properties prop) {
        if (prop == null) {
            this.prop = new Properties();
        } else {
            this.prop = (Properties) prop.clone();
        }
    }

/**
     * Copy Constructor, using clone on property
     *
     * @param propSource DOCUMENT ME!
     */
    public PropAttributes(PropAttributes propSource) {
        if (propSource != null) {
            this.prop = (Properties) (propSource.prop.clone());
        } else {
            this.prop = new Properties();
        }
    }

    /**
     * Overridden to String
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String propValue = " ";
        String key;
        String val;
        String keyOut;
        String valOut;
        Enumeration keys = prop.keys();

        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            val = (String) prop.get(key);

            if (key.indexOf(" ") > -1) {
                keyOut = "\"" + key + "\"";
            } else {
                keyOut = key;
            }

            if (val.indexOf(" ") > -1) {
                valOut = "\"" + val + "\"";
            } else {
                valOut = val;
            }

            propValue = propValue + " " + keyOut + "=" + valOut;
        }

        return (propValue);
    }

    /**
     * access the atom's properties
     *
     * @return DOCUMENT ME!
     */
    public Properties getProperties() {
        return prop;
    }

    /**
     * Check to see if a key is in the atom's property list
     *
     * @param key value to look for
     *
     * @return true if it exist
     */
    public boolean checkIfKeyExist(String key) {
        return (prop.containsKey(key));
    }

    /**
     * Get a value from the Atoms property
     *
     * @param key value to search for
     *
     * @return string value for the parameter passed in, null if not found
     */
    public String getPropertyValue(String key) {
        return (prop.getProperty(key));
    }

    /**
     * Get a value from the Atoms property
     *
     * @param key value to search for
     * @param defValue value to return if search failes
     *
     * @return string value for the parameter passed in, default if not found
     */
    public String getPropertyValue(String key, String defValue) {
        return (prop.getProperty(key, defValue));
    }

    /**
     * Get a value from the Atoms property
     *
     * @param key key to set
     * @param value value to set
     *
     * @return true if it worked, false on failure
     */
    public boolean setPropertyValue(String key, String value) {
        try {
            prop = new Properties();
            prop.put(key, value);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
