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

import org.jscience.architecture.traffic.util.StringUtils;


/**
 * This class is used by the XMLLoader and the XMLSaver to remember where
 * the parser is saving/loading in the XML tree. XMLStack is an extension of
 * the normal java.util.Stack.
 */
public class XMLStack extends java.util.Stack {
    /** DOCUMENT ME! */
    String branchName;

/**
     * Make a new XMLStack
     */
    public XMLStack() {
        super();
        updateBranchName();
    }

    /**
     * Standard stack push method
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object push(Object x) {
        Object result = super.push(x);
        updateBranchName();

        return result;
    }

    /**
     * Standard stack pop method
     *
     * @return DOCUMENT ME!
     */
    public Object pop() {
        Object result = super.pop();
        updateBranchName();

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the complete dot separated branchname
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * Make sure that a certain XMLSerializable is at the top of the
     * XML stack
     *
     * @param x The XMLSerializable
     *
     * @throws XMLTreeException If the XMLSerializable is not at the top of the
     *         stack
     */
    public void assertIsCurrentParent(XMLSerializable x)
        throws XMLTreeException {
        if (x == null) {
            assertIsCurrentParent("");
        } else {
            assertIsCurrentParent(x.getXMLName());
        }
    }

    /**
     * Make sure that the branch name equals a certain XML tag name
     *
     * @param s The full XML tag name
     *
     * @throws XMLTreeException If the XMLSerializable is not at the top of the
     *         stack
     */
    public void assertIsCurrentParent(String s) throws XMLTreeException {
        if (isCurrentParent(s)) {
            return; // OK
        }

        if (("").equals(s)) {
            throw new XMLTreeException(
                "Main parser tried to access the root of the XML tree when the " +
                "XML stack was not empty. XML Stack dump :" + getBranchName());
        } else {
            throw new XMLTreeException("The XMLSerializable :" + s +
                ": tried to access the " +
                "XML Tree when it was not in control. XML Stack dump:" +
                getBranchName() + ":");
        }
    }

    /**
     * Check if the branch name equals a certain XML tag name
     *
     * @param s The full XML name
     *
     * @return A boolean indicating if the branch name equals the tag name
     */
    public boolean isCurrentParent(String s) {
        return (("").equals(s) && super.isEmpty()) || branchName.equals(s) ||
        XMLUtils.getGenericName(branchName).equals(XMLUtils.getGenericName(s));
    }

    /**
     * Check if a certain branch name is currenly at control in the
     * stack
     *
     * @param x The XMLSerializable
     *
     * @return A boolean indicating if the XMLSerializable is at the top of the
     *         stack.
     */
    public boolean isCurrentParent(XMLSerializable x) {
        return (x == null) ? isCurrentParent("") : isCurrentParent(x.getXMLName());
    }

    /**
     * Check if the branch name begins with a certain XML name
     *
     * @param s The full XML name
     *
     * @return A boolean indicating if the branch name begins with the tag name
     */
    public boolean isARootParent(String s) {
        return (("").equals(s) && super.isEmpty()) || (s.length() == 0) ||
        branchName.startsWith(s) ||
        XMLUtils.getGenericName(branchName, false)
                .startsWith(XMLUtils.getGenericName(s, false));
    }

    /**
     * Check if a certain branch name is at the root of the stack
     *
     * @param x The XMLSerializable
     *
     * @return A boolean indicating if the XMLSerializable is at the root of
     *         the stack.
     */
    public boolean isARootParent(XMLSerializable x) {
        return (x == null) ? isARootParent("") : isARootParent(x.getXMLName());
    }

    /**
     * Internal method to update the branch name if the stack has
     * changed
     */
    protected void updateBranchName() {
        branchName = super.toString();
        branchName = branchName.substring(1, branchName.length() - 1);
        branchName = StringUtils.replace(branchName, ", ", ".");
    }
}
