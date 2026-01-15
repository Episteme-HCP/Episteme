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
package org.jscience.architecture.traffic.util;

import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Stack;
import java.util.Vector;


// A very simple number dispenser for ID's. It sucks. It also works.
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class NumberDispenser implements XMLSerializable {
    /** DOCUMENT ME! */
    Stack stack;

    /** DOCUMENT ME! */
    int counter;

    /** DOCUMENT ME! */
    String parentName = "model";

/**
     * Creates a new NumberDispenser object.
     */
    public NumberDispenser() {
        stack = new Stack();
        counter = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int get() {
        if (stack.isEmpty()) {
            return counter++;
        } else {
            return ((Integer) (stack.pop())).intValue();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     */
    public void giveBack(int number) {
        if (number == (counter - 1)) {
            counter--;
        } else if ((number < counter) && !stack.contains(new Integer(number))) {
            stack.push(new Integer(number));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        stack = new Stack();
        stack.addAll((Vector) (XMLArray.loadArray(this, loader)));
        counter = myElement.getAttribute("counter").getIntValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("dispenser");
        result.addAttribute(new XMLAttribute("counter", counter));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException {
        XMLArray.saveArray(stack, this, saver, "stack");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".dispenser";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
