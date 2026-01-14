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

/* ====================================================================
 * /SchemaElement.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

/**
 * The SchemaElement represents the root class for all schema element
 * classes. It provides a simple implemantation of the ISchemaInterface, so
 * that any subclass inherits a unique ID.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public abstract class SchemaElement implements ISchemaElement {
    // ------------------
    /**
     * DOCUMENT ME!
     */
    private String ID = new String();

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a Schema Element.<br>
     * Any instance of a Schema Element has a unique ID
     * which identifies the element, and which allows to
     * link serveral elements.
     */
    public SchemaElement() {
        ID = new org.jscience.ml.om.util.UIDGenerator().generateUID();
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a Schema Element with a given ID.<br>
     * Any instance of a Schema Element has a unique ID
     * which identifies the element, and which allows to
     * link serveral elements.<br>
     *
     * @param ID This elements unique ID
     * @throws IllegalArgumentException if ID is <code>null</code> or contains
     *                                  empty string.
     */
    SchemaElement(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty! ");
        }

        ID = id;
    }

    // --------------
    // ISchemaElement ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------
    /**
     * Returns a unique ID of this schema element.<br>
     * The ID is used to link this element with other XML elements in the
     * schema.
     *
     * @return Returns a String representing a unique ID of this schema
     *         element.
     */
    public String getID() {
        return ID;
    }

    // -------------------------------------------------------------------
    /**
     * Sets a unique ID of this schema element.<br>
     * The ID is used to link this element with other XML elements in the schema.<br>
     * Call this method only, if your know what you're doing.
     *
     * @param newID The new unique ID for this object.
     */
    public void setID(String newID) {
        this.ID = newID;
    }

    // -------------------------------------------------------------------
    /**
     * Returns a display name for this element.<br>
     * The method differs from the toString() method as toString() shows more
     * technical information about the element. Also the formating of
     * toString() can spread over several lines.<br>
     * This method returns a string (in one line) that can be used as
     * displayname in e.g. a UI dropdown box.
     *
     * @return Returns a String with a one line display name
     *
     * @see java.lang.Object.toString();
     */
    public abstract String getDisplayName();
}
