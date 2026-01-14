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
