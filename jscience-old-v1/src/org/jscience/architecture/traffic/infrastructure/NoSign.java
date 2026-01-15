/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.xml.XMLCannotSaveException;
import org.jscience.architecture.traffic.xml.XMLElement;


/**
 * Sign implementing basic trafic rules.
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class NoSign extends Sign {
    /** DOCUMENT ME! */
    protected final static int type = Sign.NO_SIGN;

/**
     * Creates a new NoSign object.
     *
     * @param _node DOCUMENT ME!
     * @param _lane DOCUMENT ME!
     */
    public NoSign(Node _node, Drivelane _lane) {
        super(_node, _lane);
    }

/**
     * Creates a new NoSign object.
     */
    public NoSign() {
    }

    /**
     * Returns the type of this Sign
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean needsExternalAlgorithm() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean mayDrive() {
        return true;
    }

    // Specific XMLSerializable implementation 
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".sign-no";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = super.saveSelf();
        result.setName("sign-no");

        return result;
    }
}
