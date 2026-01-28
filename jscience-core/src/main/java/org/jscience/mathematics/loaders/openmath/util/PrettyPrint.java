/*
 *  $Id: PrettyPrint.java,v 1.3 2007-10-23 18:21:28 virtualcall Exp $
 *
 *  Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 *  All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath.util;

import org.jscience.ml.openmath.OMObject;
import org.jscience.ml.openmath.io.OMXMLReader;
import org.jscience.ml.openmath.io.OMXMLWriter;

import org.xml.sax.InputSource;

import java.io.*;


/**
 * Pretty prints an OpenMath object.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class PrettyPrint {
    /**
     * Stores the input-stream.<p></p>
     */
    private InputStream inputStream = null;

    /**
     * Stores the output-stream.<p></p>
     */
    private OutputStream outputStream = null;

    /**
     * Sets the output-stream.<p></p>
     *
     * @param newOutputStream set the output stream.
     */
    public void setOutputStream(OutputStream newOutputStream) {
        outputStream = newOutputStream;
    }

    /**
     * Gets the output-stream.<p></p>
     *
     * @return the output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Sets the input-stream.<p></p>
     *
     * @param newInputStream set the input stream.
     */
    public void setInputStream(InputStream newInputStream) {
        inputStream = newInputStream;
    }

    /**
     * Gets the input-stream.<p></p>
     *
     * @return the input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Validates the document.<p></p>
     *
     * @throws Exception throw if we cannot pretty print the document for some
     *         obscure reason.
     */
    public void prettyPrint() throws Exception {
        OMXMLReader reader = new OMXMLReader(new InputSource(inputStream));
        OMObject object = reader.readObject();

        OutputStreamWriter writer = new OutputStreamWriter(new PrintStream(
                    outputStream));
        OMXMLWriter xmlWriter = new OMXMLWriter(writer);

        xmlWriter.writeObject(object);
        xmlWriter.flush();
    }

    /**
     * Invokes the pretty-printer stand-alone.<p></p>
     *
     * @param arguments the arguments passed to the program.
     */
    public static void main(String[] arguments) {
        try {
            FileInputStream inputStream = new FileInputStream(arguments[0]);
            PrettyPrint prettyPrint = new PrettyPrint();
            PrintStream outputStream = new PrintStream(System.out);

            prettyPrint.setInputStream(inputStream);
            prettyPrint.setOutputStream(outputStream);
            prettyPrint.prettyPrint();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
