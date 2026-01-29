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

package org.jscience.mathematics.loaders.mathml;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLContentToken;
import org.w3c.dom.mathml.MathMLNodeList;


/**
 * Implements a MathML content token.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLContentTokenImpl extends MathMLElementImpl
    implements MathMLContentToken {
    /**
     * Constructs a MathML content token.
     *
     * @param owner         the MathML document that owns this element
     * @param qualifiedName the qualified name of the element
     */
    public MathMLContentTokenImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * Returns the definition URL of the token.
     *
     * @return the definition URL
     */
    public String getDefinitionURL() {
        return getAttribute("definitionURL");
    }

    /**
     * Sets the definition URL of the token.
     *
     * @param definitionURL the definition URL to set
     */
    public void setDefinitionURL(String definitionURL) {
        setAttribute("definitionURL", definitionURL);
    }

    /**
     * Returns the encoding of the token.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return getAttribute("encoding");
    }

    /**
     * Sets the encoding of the token.
     *
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        setAttribute("encoding", encoding);
    }

    /**
     * Returns a list of arguments (excluding sep elements).
     *
     * @return the argument list
     */
    public MathMLNodeList getArguments() {
        return new MathMLNodeList() {
                public int getLength() {
                    return getArgumentsGetLength();
                }

                public Node item(int index) {
                    return getArgumentsItem(index);
                }
            };
    }

    /**
     * Returns the argument at the specified index.
     *
     * @param index the argument index (1-based)
     *
     * @return the argument node
     */
    public Node getArgument(int index) {
        Node arg = getArgumentsItem(index - 1);

        return arg;
    }

    /**
     * Sets the argument at the specified index.
     *
     * @param newArgument the new argument node
     * @param index       the argument index (1-based)
     *
     * @return the replaced node
     */
    public Node setArgument(Node newArgument, int index) {
        return replaceChild(newArgument, getArgumentsItem(index - 1));
    }

    /**
     * Inserts an argument at the specified index.
     *
     * @param newArgument the new argument to insert
     * @param index       the index where to insert (1-based)
     *
     * @return the inserted node
     */
    public Node insertArgument(Node newArgument, int index) {
        return insertBefore(newArgument, getArgumentsItem(index - 1));
    }

    /**
     * Removes the argument at the specified index.
     *
     * @param index the argument index (1-based)
     *
     * @return the removed node
     */
    public Node removeArgument(int index) {
        Node arg = getArgumentsItem(index - 1);

        return removeChild(arg);
    }

    /**
     * Deletes the argument at the specified index.
     *
     * @param index the argument index (1-based)
     */
    public void deleteArgument(int index) {
        removeArgument(index);
    }

    /**
     * Returns the number of arguments (excluding sep elements).
     *
     * @return the argument count
     */
    private int getArgumentsGetLength() {
        final int length = getLength();
        int numArgs = 0;

        for (int i = 0; i < length; i++) {
            String localName = item(i).getLocalName();

            if (!((localName != null) && localName.equals("sep"))) {
                numArgs++;
            }
        }

        return numArgs;
    }

    /**
     * Returns the argument at the specified 0-based index.
     *
     * @param index the 0-based index
     *
     * @return the argument node, or null if out of bounds
     */
    private Node getArgumentsItem(int index) {
        final int argsLength = getArgumentsGetLength();

        if ((index < 0) || (index >= argsLength)) {
            return null;
        }

        Node node = null;
        int n = -1;

        for (int i = 0; n < index; i++) {
            node = item(i);

            String localName = item(i).getLocalName();

            if (!((localName != null) && localName.equals("sep"))) {
                n++;
            }
        }

        return node;
    }
}
