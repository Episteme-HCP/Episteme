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

package org.jscience.ml.openmath.io;

import org.jscience.ml.openmath.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Constructs an OpenMath Object (OMObject) from the XML-input. <p>
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public class OMXMLReader extends DefaultHandler {
    /**
     * Stores a static array with all the OpenMath element-names in use. <p>
     */
    protected static String[] sOMObjects = {
            "OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI", "OMS",
            "OMSTR", "OMV", "OMBVAR", "OMATP", "OMR", "OMFOREIGN"
    };

    /**
     * Stores the OMObject for this reader. <p>
     */
    protected OMObject mOMObject = null;

    /**
     * Stores an vector with the last active element as the last in the
     * vector. This vector grows/shrinks during parsing. It is used to
     * easily keep track of the current parent object in the tree. <p>
     */
    protected Vector mElementStack = new Vector();

    /**
     * Stores the input-source. <p>
     */
    protected InputSource mInputSource = null;

    /**
     * Stores if we should keep the outer OMOBJ. <p>
     */
    protected boolean keep = false;

    /**
     * Stores if we are inside a foreign object section. <p>
     * <p/>
     * <p/>
     * Note: if we find another foreign inside the foreign we add to this
     * number to make sure we can handle the endElements correctly.
     * </p>
     */
    protected int foreign = 0;

    /**
     * Constructor. <p>
     */
    public OMXMLReader() {
        super();
    }

    /**
     * Constructor. <p>
     *
     * @param fInputSource the InputSource to read from.
     */
    public OMXMLReader(InputSource fInputSource) {
        super();

        mInputSource = fInputSource;
    }

    /**
     * Constructor taking a String as the InputSource. <p>
     *
     * @param fString the string to read from.
     */
    public OMXMLReader(String fString) {
        super();

        mInputSource = new InputSource(new StringReader(fString));
    }

    /**
     * Constructor taking a String as the InputSource and a boolean indicating
     * if we want to keep the outer OMOBJ. <p>
     *
     * @param newString the input source
     * @param newKeep   the boolean indicating if we keep the OMOBJ
     */
    public OMXMLReader(String newString, boolean newKeep) {
        super();

        mInputSource = new InputSource(new StringReader(newString));
        keep = newKeep;
    }

    /**
     * We've encountered a PCDATA section. <p>
     * <p/>
     * <p/>
     * Check if we have an enclosing OMInteger or OMString
     * and set the data-members accordingly.
     * </p>
     *
     * @param fCharacters the characters to handle.
     * @param fStart      the start index into the characters array.
     * @param fLength     the number of characters to read from the start.
     * @throws SAXException when something is seriously wrong.
     */
    public void characters(char[] fCharacters, int fStart, int fLength)
            throws SAXException {
        if (mElementStack.size() != 0) {
            if (mElementStack.lastElement() instanceof OMInteger) {
                OMInteger tInteger = (OMInteger) mElementStack.lastElement();
                if (tInteger.getInteger() == null) {
                    StringBuffer tStringBuffer = new StringBuffer();
                    for (int i = fStart; i < (fStart + fLength); i++) {
                        tStringBuffer.append(fCharacters[i]);
                    }
                    tInteger.setInteger(tStringBuffer.toString().trim());
                }
            }

            if (mElementStack.lastElement() instanceof OMString) {
                OMString tString = (OMString) mElementStack.lastElement();
                if (tString.getString() == null) {
                    StringBuffer tStringBuffer = new StringBuffer();
                    for (int i = fStart; i < (fStart + fLength); i++) {
                        tStringBuffer.append(fCharacters[i]);
                    }
                    tString.setString(tStringBuffer.toString());
                }
            }

            if (mElementStack.lastElement() instanceof OMByteArray) {
                OMByteArray tByteArray = (OMByteArray) mElementStack.lastElement();
                if (tByteArray.getByteArrayAsString() == null) {
                    StringBuffer tStringBuffer = new StringBuffer();
                    for (int i = fStart; i < (fStart + fLength); i++) {
                        tStringBuffer.append(fCharacters[i]);
                    }
                    tByteArray.setByteArray(tStringBuffer.toString());
                }
            }
        }
    }

    /**
     * End the document. <p>
     */
    public void endDocument() {
        if (mElementStack.size() > 0) {
            /*
            * This reader is a bit more relaxing than the specification.
            * If you don't put <OMOBJ>...</OMOBJ> around your object it
            * will still be read. This is to facilitate easy reading.
            */
            mOMObject = (OMObject) mElementStack.lastElement();
        }
    }

    /**
     * End a XML element. <p>
     *
     * @param fNamespaceURI the URI of the namespace.
     * @param fLocalName    the locale name of the element.
     * @param fRawName      the raw name of the element.
     * @throws SAXException when something is seriously wrong.
     */
    public void endElement(String fNamespaceURI, String fLocalName, String fRawName) throws SAXException {
        if (foreign == 0) {
            if (fLocalName.equals("OMOBJ")) {
                mOMObject = (OMObject) mElementStack.lastElement();
                mElementStack.removeElement(mElementStack.lastElement());
            } else {
                if (mElementStack.size() > 1) {
                    OMObject tObject = (OMObject) mElementStack.elementAt(mElementStack.size() - 2);
                    int tIndex = getElementIndex(tObject.getType());

                    switch (tIndex) {
                        case 1: /* OMA */ {
                            OMApplication tApplication = (OMApplication) tObject;
                            OMObject tElement = (OMObject) mElementStack.lastElement();

                            tApplication.addElement(tElement);
                            mElementStack.removeElement(mElementStack.lastElement());
                        }
                        break;

                        case 2: /* OMATTR */ {
                            OMAttribution tAttribution = (OMAttribution) tObject;
                            OMObject tElement = (OMObject) mElementStack.lastElement();

                            if (mElementStack.lastElement() instanceof OMVector) {
                                OMVector tVector = (OMVector) mElementStack.lastElement();
                                Enumeration tEnum = tVector.getElements().elements();

                                for (; tEnum.hasMoreElements();) {
                                    OMObject tKey = (OMObject) tEnum.nextElement();
                                    OMObject tValue = (OMObject) tEnum.nextElement();

                                    tAttribution.put(tKey, tValue);
                                }
                            } else {
                                tAttribution.setConstructor(tElement);
                            }
                            mElementStack.removeElement(mElementStack.lastElement());
                        }
                        break;

                        case 4: /* OMBIND */ {
                            OMBinding tBinding = (OMBinding) tObject;
                            OMObject tElement = (OMObject) mElementStack.lastElement();

                            if (mElementStack.lastElement() instanceof OMVector) {
                                OMVector tVector = (OMVector) mElementStack.lastElement();
                                Enumeration tEnum = tVector.getElements().elements();

                                for (; tEnum.hasMoreElements();) {
                                    OMObject tVariable = (OMObject) tEnum.nextElement();

                                    tBinding.addVariable(tVariable);
                                }
                            } else {
                                if (tBinding.getBinder() == null) {
                                    tBinding.setBinder(tElement);
                                } else {
                                    tBinding.setBody(tElement);
                                }
                            }
                            mElementStack.removeElement(mElementStack.lastElement());
                        }
                        break;

                        case 5: /* OME */ {
                            OMError tError = (OMError) tObject;
                            OMObject tElement = (OMObject) mElementStack.lastElement();

                            if (tError.getSymbol() == null) {
                                tError.setSymbol((OMSymbol) tElement);
                            } else {
                                tError.addElement(tElement);
                            }
                            mElementStack.removeElement(mElementStack.lastElement());
                        }
                        break;

                        default: {
                            if (tObject instanceof OMVector) {
                                OMVector tVector = (OMVector) tObject;
                                tVector.addElement((OMObject) mElementStack.lastElement());
                            }

                            if (tObject instanceof OMRoot) {
                                OMRoot root = (OMRoot) tObject;
                                root.setObject((OMObject) mElementStack.lastElement());
                            }

                            mElementStack.removeElement(mElementStack.lastElement());
                        }
                        break;
                    }
                }
            }
        } else {
            if (fLocalName.equals("OMFOREIGN"))
                foreign--;
        }
    }

    /**
     * Utility method to get the index from
     * the element-array. <p>
     *
     * @param fName the name of the OpenMath type to look for.
     * @return -1 when element was not found, the index if it was found.
     */
    private int getElementIndex(String fName) {
        for (int i = 0; i < sOMObjects.length; i++) {
            if (sOMObjects[i].equals(fName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Process ignorable whitespace. <p>
     *
     * @param tCharacter the characters that can be ignored.
     * @param tStart     the start index into the characters array.
     * @param tLength    the number of characters to ignore.
     */
    public void ignorableWhitespace(char[] tCharacter, int tStart, int tLength) {
    }

    /**
     * Process the processing instruction. <p>
     *
     * @param fTarget the target of the processing instruction.
     * @param fData   the data of the processing instruction.
     */
    public void processingInstruction(String fTarget, String fData) {
    }

    /**
     * Read an OpenMath object. <p>
     *
     * @return the OpenMath object to was read.
     * @throws IOException when reading failed.
     */
    public OMObject readObject() throws IOException {
        SAXParserFactory tSAXParserFactory = SAXParserFactory.newInstance();
        tSAXParserFactory.setNamespaceAware(true);
        tSAXParserFactory.setValidating(true);
        try {
            SAXParser tSAXParser = tSAXParserFactory.newSAXParser();
            tSAXParser.parse(mInputSource, this);
            return mOMObject;
        } catch (Exception tException) {
            tException.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Get the OpenMath object. <p>
     * <p/>
     * <p/>
     * <i>Note</i>: if you didn't set this reader up from an input-source
     * nor from a String, you will need this method to get the actual
     * object at the end of reading. This method will only garantuee
     * the proper result if you call it after 'endDocument' has be
     * recognized.
     * </p>
     *
     * @return the parsed OMObject or <b>null</b>
     */
    public OMObject getObject() {
        return mOMObject;
    }

    /**
     * Set the document-locator. <p>
     *
     * @param fLocator the document locator to use.
     */
    public void setDocumentLocator(Locator fLocator) {
    }

    /**
     * Signals the start of the document. <p>
     * <p/>
     * <p/>
     * We reinitiliaze all the data-structures used during parsing, note that
     * this parser is not reentrant and should only be used sequentially.
     * </p>
     */
    public void startDocument() {
        mOMObject = null;
        mElementStack = new Vector();
    }

    /**
     * Start a XML element. <p>
     *
     * @param fNamespaceURI the namespace URI.
     * @param fLocalName    the local name of the element.
     * @param fRawName      the raw name of the element.
     * @param fAttributes   the attributes of the element.
     * @throws SAXException when something is seriously wrong.
     */
    public void startElement(String fNamespaceURI, String fLocalName, String fRawName, Attributes fAttributes) throws SAXException {
        if (foreign == 0) {
            int tIndex = getElementIndex(fLocalName);
            OMObject tElement = null;

            switch (tIndex) {
                case 0: /* OMOBJ  */
                    if (keep) {
                        tElement = new OMRoot();
                    }
                    break;

                case 1: /* OMA    */
                    tElement = new OMApplication();
                    break;

                case 2: /* OMATTR */
                    tElement = new OMAttribution();
                    break;

                case 3: /* OMB    */
                    tElement = new OMByteArray();
                    break;

                case 4: /* OMBIND */
                    tElement = new OMBinding();
                    break;

                case 5: /* OME    */
                    tElement = new OMError();
                    break;

                case 6: /* OMF    */ {
                    OMFloat tFloat = new OMFloat();
                    if (fAttributes.getIndex("dec") != -1) {
                        tFloat.setFloat(fAttributes.getValue("dec"));
                    }
                    if (fAttributes.getIndex("hex") != -1) {
                        tFloat.setFloat(fAttributes.getValue("hex"), "hex");
                    }
                    tElement = tFloat;
                    break;
                }

                case 7: /* OMI    */
                    tElement = new OMInteger();
                    break;

                case 8: /* OMS    */ {
                    OMSymbol tSymbol = new OMSymbol();
                    if (fAttributes.getIndex("cd") != -1) {
                        tSymbol.setCD(fAttributes.getValue("cd"));
                    }
                    if (fAttributes.getIndex("name") != -1) {
                        tSymbol.setName(fAttributes.getValue("name"));
                    }
                    tElement = tSymbol;
                    break;
                }

                case 9: /* OMSTR  */
                    tElement = new OMString();
                    break;

                case 10: /* OMV    */ {
                    OMVariable tVariable = new OMVariable();
                    if (fAttributes.getIndex("name") != -1) {
                        tVariable.setName(fAttributes.getValue("name"));
                    }
                    tElement = tVariable;
                    break;
                }

                case 11: /* OMBVAR */
                case 12: /* OMATP  */
                    tElement = new OMVector();
                    break;

                case 13: /* OMR */ {
                    tElement = new OMReference();
                    break;
                }

                case 14: /* OMFOREIGN */ {
                    tElement = new OMForeign();
                    foreign++;
                    break;
                }

                default:
                    throw new SAXException("Element <" + fLocalName +
                            "> not recognized");
            }

            /*
            * Copy the attributes from the Attributes structure to the
            * attributes Hashtable.
            *
            * Note: the 'cd', 'name', 'dec' and 'hex' attributes will
            *       also be copied into the Hashtable containing the
            *       attributes.
            */
            if (tElement != null) {
                for (int i = 0; i < fAttributes.getLength(); i++) {
                    String tName = fAttributes.getLocalName(i);
                    String tValue = fAttributes.getValue(i);
                    tElement.setAttribute(tName, tValue);
                }
            }

            if (tElement != null) {
                mElementStack.addElement(tElement);
            }
        } else {
            if (fLocalName.equals("OMFOREIGN"))
                foreign++;
        }
    }

    /**
     * Inner class used while parsing OMATP and OMBVAR sections. <p>
     */
    private class OMVector extends OMObject {
        /**
         * Stores the vector elements. <p>
         */
        protected Vector mElements = new Vector();

        /**
         * Constructor. <p>
         */
        public OMVector() {
            super();
        }

        /**
         * Adds an element.
         *
         * @param fOMObject the element to add.
         */
        public void addElement(OMObject fOMObject) {
            mElements.addElement(fOMObject);
        }

        /**
         * Gets the elements.
         *
         * @return the vector of elements.
         */
        public Vector getElements() {
            return mElements;
        }

        /**
         * Is this a composite object?
         *
         * @return <b>true</b> if this is a composite object,
         *         <b>false</b> if it is not.
         */
        public boolean isComposite() {
            return true;
        }

        /**
         * Is this an atom object?
         *
         * @return <b>true</b> if this is atom object,
         *         <b>false</b> if it is not.
         */
        public boolean isAtom() {
            return false;
        }

        /**
         * Returns the type. <p>
         *
         * @return the type of the object.
         */
        public String getType() {
            return "OMVCT";
        }

        /**
         * toString. <p>
         *
         * @return a string representation.
         */
        public String toString() {
            return "<OMVCT/>";
        }

        /**
         * Clones this object (shallow copy). <p>
         *
         * @return a shallow copy.
         */
        public Object clone() {
            OMVector tVector = new OMVector();
            tVector.mElements = this.mElements;
            return tVector;
        }

        /**
         * Copies the object (deep copy). <p>
         *
         * @return a deep copy.
         */
        public Object copy() {
            return null;
        }

        /**
         * Determins if this is the same object. <p>
         *
         * @param object the object to compare against.
         * @return <b>true</b> if this is the same object,
         *         <b>false</b> if it is not.
         */
        public boolean isSame(OMObject object) {
            return false;
        }

        /**
         * Determines if this is a valid object. <p>
         *
         * @return <b>true</b> if this is a valid object,
         *         <b>false</b> if it is not.
         */
        public boolean isValid() {
            return false;
        }
    }
}
