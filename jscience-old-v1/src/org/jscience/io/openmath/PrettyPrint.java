/*
 * $Id: PrettyPrint.java,v 1.3 2007-10-23 18:18:07 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
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
package org.jscience.io.openmath;

import org.jscience.ml.openmath.OMObject;

import org.xml.sax.InputSource;

import java.io.*;


/**
 * Pretty prints an OpenMath object.<p></p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Version$
 */
public class PrettyPrint {
    /**
     * Stores the input-stream.<p></p>
     */
    private InputStream inputStream = null;

    /**
     * Stores the output-stream.<p></p>
     */
    private PrintStream outputStream = null;

    /**
     * Sets the output-stream.<p></p>
     *
     * @param output set the output stream.
     */
    public final void setOutputStream(final OutputStream output) {
        this.outputStream = new PrintStream(output);
    }

    /**
     * Sets the input-stream.<p></p>
     *
     * @param input set the input stream.
     */
    public final void setInputStream(final InputStream input) {
        this.inputStream = input;
    }

    /**
     * Validates the document.<p></p>
     *
     * @throws Exception throw if we cannot pretty print the document for some
     *         obscure reason.
     */
    public void prettyPrint() throws Exception {
        OMXMLReader tReader = new OMXMLReader(new InputSource(inputStream));
        OMObject tObject = tReader.readObject();
        OutputStreamWriter tWriter = new OutputStreamWriter(outputStream);
        OMXMLWriter tXMLWriter = new OMXMLWriter(tWriter);

        tXMLWriter.writeObject(tObject);
        tXMLWriter.flush();
    }

    /**
     * Invokes the pretty-printer stand-alone.<p></p>
     *
     * @param fArguments the arguments passed to the program.
     */
    public static void main(String[] fArguments) {
        try {
            FileInputStream tInputStream = new FileInputStream(fArguments[0]);

            PrettyPrint tValidator = new PrettyPrint();
            PrintStream tOutputStream = new PrintStream(System.out);

            tValidator.setInputStream(tInputStream);
            tValidator.setOutputStream(tOutputStream);

            tValidator.prettyPrint();
        } catch (Exception tException) {
            tException.printStackTrace();
        }
    }
}
