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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Implements an outputstream that can write OpenMath objects as XML.<p></p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Revision: 1.3 $
 */
public class OMXMLWriter extends Writer {
    /**
     * Stores a static array with all the OpenMath element-names in
     * use.<p></p>
     */
    protected static String[] sOMObjects = {
            "OMOBJ", "OMA", "OMATTR", "OMB", "OMBIND", "OME", "OMF", "OMI",
            "OMS", "OMSTR", "OMV", "OMBVAR", "OMATP"
        };

    /**
     * Stores if we should pretty-print.<p></p>
     */
    protected boolean mPrettyPrint = true;

    /**
     * Stores if we should drop the outer OMOBJ.<p></p>
     */
    protected boolean printOuterOMOBJ = true;

    /**
     * Stores the output-stream.<p></p>
     */
    protected PrintWriter mPrintWriter = null;

/**
     * Constructor. <p>
     *
     * @param fWriter the writer we actually use to write out.
     */
    public OMXMLWriter(Writer fWriter) {
        super();

        mPrintWriter = new PrintWriter(fWriter);
        mPrettyPrint = true;
    }

/**
     * Constructor. <p>
     *
     * @param fWriter      the writer we actually use to write out.
     * @param fPrettyPrint sets if we should pretty print the output.
     */
    public OMXMLWriter(Writer fWriter, boolean fPrettyPrint) {
        super();

        mPrintWriter = new PrintWriter(fWriter);
        mPrettyPrint = fPrettyPrint;
    }

/**
     * Constructor. <p>
     *
     * @param writer          the writer we actually use to write out.
     * @param printPretty     sets if we should pretty print the output.
     * @param printOuterOMOBJ sets if we should print the outer OMOBJ.
     */
    public OMXMLWriter(Writer writer, boolean printPretty,
        boolean printOuterOMOBJ) {
        super();

        mPrintWriter = new PrintWriter(writer);
        mPrettyPrint = printPretty;
        this.printOuterOMOBJ = printOuterOMOBJ;
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for write is that one byte is written to the output stream.
     * The byte to be written is the eight low-order bits of the argument
     * fInt. The 24 high-order bits of fInt are ignored.<p></p>
     *
     * @param fInt the byte to write.
     *
     * @throws IOException when an I/O exception occurs.
     */
    public void write(int fInt) throws IOException {
        mPrintWriter.write(fInt);
    }

    /**
     * Utility method to get the index from the element-array.<p></p>
     *
     * @param fObject the object to get the element index for.
     *
     * @return -1 when element was not found, the index if it was found.
     */
    private int getElementIndex(OMObject fObject) {
        if (fObject != null) {
            for (int i = 0; i < sOMObjects.length; i++) {
                if (sOMObjects[i].equals(fObject.getType())) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Close the stream, flushing it first. Once a stream has been
     * closed, further write() or flush() invocations will cause an
     * IOException to be thrown. Closing a previously-closed stream, however,
     * has no effect.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void close() throws IOException {
        mPrintWriter.flush();
        mPrintWriter.close();
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param fCharacterBuffer the buffer of characters to write.
     * @param fOffset the offset in the character buffer.
     * @param fLength the number of characters to write.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void write(char[] fCharacterBuffer, int fOffset, int fLength)
        throws IOException {
        mPrintWriter.write(fCharacterBuffer, fOffset, fLength);
    }

    /**
     * Flush the stream. If the stream has saved any characters from
     * the various write() methods in a buffer, write them immediately to
     * their intended destination. Then, if that destination is another
     * character or byte stream, flush it. Thus one flush() invocation will
     * flush all the buffers in a chain of Writers and OutputStreams.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void flush() throws IOException {
        mPrintWriter.flush();
    }

    /**
     * Writes the attributes.<p></p>
     *
     * @param fAttributes the table with the attributes.
     */
    private void writeAttributes(Hashtable fAttributes) {
        if (fAttributes != null) {
            Enumeration tKeys = fAttributes.keys();

            for (; tKeys.hasMoreElements();) {
                Object tKey = tKeys.nextElement();

                mPrintWriter.print(" " + tKey.toString() + "=\"");
                mPrintWriter.print(fAttributes.get(tKey).toString() + "\"");
            }
        }
    }

    /**
     * Writes an OMApplication.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fApplication the OMApplication to write out.
     */
    private void writeApplication(int fIndent, OMApplication fApplication) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMA");
        writeAttributes(fApplication.getAttributes());
        mPrintWriter.println(">");

        Enumeration tEnum = fApplication.getElements().elements();

        for (; tEnum.hasMoreElements();) {
            OMObject tKey = (OMObject) tEnum.nextElement();
            writeElement(fIndent + 1, tKey);
        }

        writeIndent(fIndent);
        mPrintWriter.print("</OMA>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Writes an OMAttribution.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fAttribution the OMAttribution to write out.
     */
    private void writeAttribution(int fIndent, OMAttribution fAttribution) {
        writeIndent(fIndent);
        mPrintWriter.print("<OMATTR");
        writeAttributes(fAttribution.getAttributes());
        mPrintWriter.print(">");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        Hashtable tHashtable = fAttribution.getAttributions();

        writeIndent(fIndent + 1);
        mPrintWriter.print("<OMATP>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        Enumeration tEnum = tHashtable.keys();

        for (; tEnum.hasMoreElements();) {
            OMObject tKey = (OMObject) tEnum.nextElement();
            writeElement(fIndent + 2, tKey);

            OMObject tValue = (OMObject) tHashtable.get(tKey);
            writeElement(fIndent + 2, tValue);
        }

        writeIndent(fIndent + 1);
        mPrintWriter.print("</OMATP>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        OMObject tObject = (OMObject) fAttribution.getConstructor();
        writeElement(fIndent + 1, tObject);

        writeIndent(fIndent);
        mPrintWriter.print("</OMATTR>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an OMBinding.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fBinding the OMBinding to write out.
     */
    private void writeBinding(int fIndent, OMBinding fBinding) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMBIND");
        writeAttributes(fBinding.getAttributes());
        mPrintWriter.print(">");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        writeElement(fIndent + 1, fBinding.getBinder());

        writeIndent(fIndent + 1);
        mPrintWriter.print("<OMBVAR>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        Enumeration tEnum = fBinding.getVariables().elements();

        for (; tEnum.hasMoreElements();) {
            OMObject tObject = (OMObject) tEnum.nextElement();

            writeElement(fIndent + 2, tObject);
        }

        writeIndent(fIndent + 1);
        mPrintWriter.print("</OMBVAR>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        writeElement(fIndent + 1, fBinding.getBody());
        writeIndent(fIndent);

        mPrintWriter.print("</OMBIND>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an OMByteArray.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fByteArray the OpenMath byte array to write out.
     */
    private void writeByteArray(int fIndent, OMByteArray fByteArray) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMB");
        writeAttributes(fByteArray.getAttributes());
        mPrintWriter.print(">");
        mPrintWriter.print(fByteArray.getByteArrayAsString());
        mPrintWriter.print("</OMB>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an element.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fObject the OpenMath object to write out.
     */
    private void writeElement(int fIndent, OMObject fObject) {
        int tIndex = getElementIndex(fObject);

        switch (tIndex) {
        case 1:
            writeApplication(fIndent, (OMApplication) fObject);

            break;

        case 2:
            writeAttribution(fIndent, (OMAttribution) fObject);

            break;

        case 3:
            writeByteArray(fIndent, (OMByteArray) fObject);

            break;

        case 4:
            writeBinding(fIndent, (OMBinding) fObject);

            break;

        case 5:
            writeError(fIndent, (OMError) fObject);

            break;

        case 6:
            writeFloat(fIndent, (OMFloat) fObject);

            break;

        case 7:
            writeInteger(fIndent, (OMInteger) fObject);

            break;

        case 8:
            writeSymbol(fIndent, (OMSymbol) fObject);

            break;

        case 9:
            writeString(fIndent, (OMString) fObject);

            break;

        case 10:
            writeVariable(fIndent, (OMVariable) fObject);

            break;

        default:
            break;
        }
    }

    /**
     * Write an OMError.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fError the OMError to write out.
     */
    private void writeError(int fIndent, OMError fError) {
        writeIndent(fIndent);

        mPrintWriter.print("<OME");
        writeAttributes(fError.getAttributes());
        mPrintWriter.print(">");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }

        OMObject tObject = (OMObject) fError.getSymbol();
        writeElement(fIndent + 1, tObject);

        Enumeration tEnum = fError.getElements().elements();

        for (; tEnum.hasMoreElements();) {
            OMObject tKey = (OMObject) tEnum.nextElement();
            writeElement(fIndent + 1, tKey);
        }

        writeIndent(fIndent);
        mPrintWriter.print("</OME>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an OMFloat.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fFloat the OpenMath float to write out.
     */
    private void writeFloat(int fIndent, OMFloat fFloat) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMF");
        writeAttributes(fFloat.getAttributes());
        mPrintWriter.print("/>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Writes a specific indent.<p></p>
     *
     * @param fIndent the level of indentation.
     */
    private void writeIndent(int fIndent) {
        if (mPrettyPrint) {
            for (int i = 0; i < fIndent; i++) {
                mPrintWriter.print("  ");
            }
        }
    }

    /**
     * Write an OMInteger.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fInteger the OMInteger to write out.
     */
    private void writeInteger(int fIndent, OMInteger fInteger) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMI");
        writeAttributes(fInteger.getAttributes());
        mPrintWriter.print(">");

        mPrintWriter.print(fInteger.getInteger() + "</OMI>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an OMObject.<p></p>
     *
     * @param fObject the OMObject to write out.
     */
    public void writeObject(OMObject fObject) {
        if (printOuterOMOBJ) {
            mPrintWriter.print("<OMOBJ>");

            if (mPrettyPrint) {
                mPrintWriter.println();
            }
        }

        writeElement(1, fObject);

        if (printOuterOMOBJ) {
            mPrintWriter.print("</OMOBJ>");

            if (mPrettyPrint) {
                mPrintWriter.println();
            }
        }

        mPrintWriter.flush();
    }

    /**
     * Write an OMString.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fString the OpenMath string to write out.
     */
    private void writeString(int fIndent, OMString fString) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMSTR");
        writeAttributes(fString.getAttributes());
        mPrintWriter.print(">");

        mPrintWriter.print(fString.getString() + "</OMSTR>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Writes an OMSymbol.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fSymbol the OpenMath symbol to write out.
     */
    private void writeSymbol(int fIndent, OMSymbol fSymbol) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMS");
        writeAttributes(fSymbol.getAttributes());
        mPrintWriter.print("/>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }

    /**
     * Write an OMVariable.<p></p>
     *
     * @param fIndent the level of indentation.
     * @param fVariable the OMVariable to write out.
     */
    private void writeVariable(int fIndent, OMVariable fVariable) {
        writeIndent(fIndent);

        mPrintWriter.print("<OMV");
        writeAttributes(fVariable.getAttributes());
        mPrintWriter.print("/>");

        if (mPrettyPrint) {
            mPrintWriter.println();
        }
    }
}
