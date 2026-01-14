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

package org.jscience.io.openmath;

import org.jscience.ml.openmath.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * A OpenMath DOM reader. <p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Revision: 1.2 $
 */
public class OMDOMReader {
    /**
     * Stores a static array with all the OpenMath element-names in use. <p>
     */
    protected static String[] sOMObjects = {
            "OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",
            "OMS", "OMSTR", "OMV", "OMBVAR", "OMATP"
    };

    /**
     * Stores the input-source if we use one. <p>
     */
    protected InputSource mInputSource;

    /**
     * Stores the node we want to read the OMObject from. <p>
     */
    protected Node mNode;

    /**
     * Constructor. <p>
     *
     * @param document the document to read using the reader.
     */
    public OMDOMReader(Document document) {
        mNode = (Node) document;
    }

    /**
     * Constructor. <p>
     *
     * @param fragment the document fragment to read using the reader.
     */
    public OMDOMReader(DocumentFragment fragment) {
        mNode = (Node) fragment;
    }

    /**
     * Constructor. <p>
     *
     * @param node the node to read using the reader.
     */
    public OMDOMReader(Node node) {
        mNode = node;
    }

    /**
     * Constructor. <p>
     *
     * @param inputSource the InputSource to read from using this reader.
     */
    public OMDOMReader(InputSource inputSource) {
        mInputSource = inputSource;
    }

    /**
     * Constructor. <p>
     *
     * @param string the string to read from using this reader.
     */
    public OMDOMReader(String string) {
        mInputSource = new InputSource(new StringReader(string));
    }

    /**
     * Read an OpenMath object. <p>
     * <p/>
     * <p/>
     * 1. If we are a document then get the nodelist that contains an
     * OMOBJ. Take the first of the list and read that object.
     * <p/>
     * 2. Or if we are a document fragment assume the first child is
     * the OMObject.
     * <p/>
     * 3. Or just assume the node contains the OMObject.
     * </p>
     *
     * @return the OpenMath object read.
     * @throws IOException when a problem arises while reading.
     */
    public OMObject readObject() throws IOException {
        if (mInputSource != null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                factory.setIgnoringElementContentWhitespace(true);
                factory.setNamespaceAware(true);
                factory.setValidating(false);

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(mInputSource);

                mNode = (Node) document;
            } catch (Exception tException) {
                throw new IOException("Unable to construct DOM");
            }
        }

        if (mNode instanceof Document) {
            Document tDocument = (Document) mNode;
            NodeList tNodeList = tDocument.getElementsByTagName("OMOBJ");

            if (tNodeList.getLength() >= 1) {
                return readNode(tNodeList.item(0));
            }
        } else if (mNode instanceof DocumentFragment) {
            DocumentFragment tFragment = (DocumentFragment) mNode;

            if (tFragment.getFirstChild() != null) {
                return readNode(tFragment.getFirstChild());
            }
        } else {
            return readNode(mNode);
        }

        throw new IOException("Unable to read OMObject from DOM");
    }

    /**
     * Utility method to get the index from
     * the element-array. <p>
     *
     * @param name the name to check for.
     * @return -1 when element was not found, the index if it was found.
     */
    private int getElementIndex(String name) {
        for (int i = 0; i < sOMObjects.length; i++) {
            if (sOMObjects[i].equals(name)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Read a DOM Node. <p>
     * <p/>
     * <p/>
     * Make sure we have a normalized Node. See spec for more details.
     * </p>
     *
     * @param fNode the node to read from
     * @return the OpenMath object read.
     * @throws IOException when a problem arises while reading.
     */
    private OMObject readNode(Node fNode) throws IOException {
        fNode.normalize();

        OMObject tObject = null;

        switch (getElementIndex(fNode.getNodeName())) {
            case 0: /* OMOBJ */ {
                Node tChild = fNode.getFirstChild();

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        return readNode(tChild);
                    }

                    tChild = tChild.getNextSibling();
                }
            }

            case 1: /* OMA  */ {
                OMApplication tApplication = new OMApplication();
                Node tChild = fNode.getFirstChild();

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        OMObject tElement = readNode(tChild);
                        tApplication.addElement(tElement);
                    }

                    tChild = tChild.getNextSibling();
                }

                tObject = tApplication;

                break;
            }

            case 2: /* OMATTR */ {
                OMAttribution tAttribution = new OMAttribution();
                Node tChild = fNode.getFirstChild();

                while (getElementIndex(tChild.getNodeName()) != 12) {
                    tChild = tChild.getNextSibling();
                }

                Node tChild2 = tChild.getFirstChild();

                while (tChild2 != null) {
                    OMObject tKey = null;
                    OMObject tValue = null;

                    while ((getElementIndex(tChild2.getNodeName()) < 0) ||
                            (getElementIndex(tChild2.getNodeName()) > 10)) {
                        tChild2 = tChild2.getNextSibling();

                        if (tChild2 == null) {
                            break;
                        }
                    }

                    if (tChild2 == null) {
                        break;
                    }

                    tKey = readNode(tChild2);

                    while ((getElementIndex(tChild2.getNodeName()) < 0) ||
                            (getElementIndex(tChild2.getNodeName()) > 10)) {
                        tChild2 = tChild2.getNextSibling();

                        if (tChild2 == null) {
                            break;
                        }
                    }

                    if (tChild2 == null) {
                        break;
                    }

                    tValue = readNode(tChild2);
                    tAttribution.put(tKey, tValue);

                    tChild2 = tChild2.getNextSibling();
                }

                tChild = fNode.getFirstChild();

                while (getElementIndex(tChild.getNodeName()) != 12) {
                    tChild = tChild.getNextSibling();
                }

                OMObject tConstructor = null;

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        tConstructor = readNode(tChild);

                        break;
                    }

                    tChild = tChild.getNextSibling();
                }

                tAttribution.setConstructor(tConstructor);
                tObject = tAttribution;

                break;
            }

            case 3: /* OMB    */ {
                OMByteArray tByteArray = new OMByteArray();
                Node tChild = fNode.getFirstChild();

                tByteArray.setByteArray(tChild.getNodeValue());
                tObject = tByteArray;

                break;
            }

            case 4: /* OMBIND */ {
                OMBinding tBinding = new OMBinding();
                Node tChild = fNode.getFirstChild();

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        tBinding.setBinder(readNode(tChild));

                        break;
                    }

                    tChild = tChild.getNextSibling();
                }

                tChild = tChild.getNextSibling();

                while (getElementIndex(tChild.getNodeName()) != 11) {
                    tChild = tChild.getNextSibling();
                }

                Node tChild2 = tChild.getFirstChild();

                while (tChild2 != null) {
                    OMObject tVariable = null;

                    while ((getElementIndex(tChild2.getNodeName()) < 0) ||
                            (getElementIndex(tChild2.getNodeName()) > 10)) {
                        tChild2 = tChild2.getNextSibling();

                        if (tChild2 == null) {
                            break;
                        }
                    }

                    if (tChild2 == null) {
                        break;
                    }

                    tVariable = readNode(tChild2);
                    tBinding.addVariable(tVariable);

                    tChild2 = tChild2.getNextSibling();
                }

                tChild = fNode.getFirstChild();

                while (getElementIndex(tChild.getNodeName()) != 11) {
                    tChild = tChild.getNextSibling();
                }

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        tBinding.setBody(readNode(tChild));

                        break;
                    }

                    tChild = tChild.getNextSibling();
                }

                tObject = tBinding;

                break;
            }

            case 5: /* OME    */ {
                OMError tError = new OMError();
                Node tChild = fNode.getFirstChild();

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        OMObject tElement = readNode(tChild);
                        tError.setSymbol((OMSymbol) tElement);

                        break;
                    }

                    tChild = tChild.getNextSibling();
                }

                tChild = tChild.getNextSibling();

                while (tChild != null) {
                    if ((getElementIndex(tChild.getNodeName()) >= 1) &&
                            (getElementIndex(tChild.getNodeName()) <= 10)) {
                        OMObject tElement = readNode(tChild);
                        tError.addElement(tElement);
                    }

                    tChild = tChild.getNextSibling();
                }

                tObject = tError;

                break;
            }

            case 6: /* OMF    */ {
                tObject = new OMFloat();

                break;
            }

            case 7: /* OMI    */ {
                OMInteger tInteger = new OMInteger();
                Node tChild = fNode.getFirstChild();

                tInteger.setInteger(tChild.getNodeValue());
                tObject = tInteger;

                break;
            }

            case 8: /* OMS    */ {
                tObject = new OMSymbol();

                break;
            }

            case 9: /* OMSTR  */ {
                OMString tString = new OMString();
                Node tChild = fNode.getFirstChild();

                tString.setString(tChild.getNodeValue());
                tObject = tString;

                break;
            }

            case 10: /* OMV    */ {
                tObject = new OMVariable();

                break;
            }

            default:
                throw new IOException("Unable to read OMObject from DOM: " +
                        fNode.getNodeName());
        }

        NamedNodeMap tAttributes = fNode.getAttributes();

        for (int i = 0; i < tAttributes.getLength(); i++) {
            Node tNode = tAttributes.item(i);

            if (tNode.getPrefix() == null) {
                tObject.setAttribute(tNode.getNodeName(), tNode.getNodeValue());
            } else {
                tObject.setAttribute(tNode.getPrefix() + ":" +
                        tNode.getNodeName(), tNode.getNodeValue());
            }
        }

        return tObject;
    }
}
