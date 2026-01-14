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

package org.jscience.linguistics.kif;

import java.io.IOException;

/** This code is copyright Articulate Software (c) 2003.  Some portions
 copyright Teknowledge (c) 2003 and reused under the terms of the GNU license.
 This software is released under the GNU Public License <http://www.gnu.org/copyleft/gpl.html>.
 Users of this code also consent, by use of this code, to credit Articulate Software
 and Teknowledge in any writings, briefings, publications, presentations, or
 other representations of any software which incorporates, builds on, or uses this
 code.  Please cite the following article in any publication with references:

 Pease, A., (2003). The Sigma Ontology Development Environment,
 in Working Notes of the IJCAI-2003 Workshop on Ontology and Distributed Systems,
 August 9, Acapulco, Mexico.
 */

/**
 * Class for invoking a client side text editor.
 */
public class Edit {
    /**
     * DOCUMENT ME!
     *
     * @param filename   DOCUMENT ME!
     * @param lineNumber DOCUMENT ME!
     */
    public static void editFile(String filename, int lineNumber) {
        System.out.println("INFO in Edit.editFile(): Editing file " + filename);

        StringBuffer execString = new StringBuffer();
        String editorCommand = KBmanager.getMgr().getPref("editorCommand");
        String lineNumberCommand = KBmanager.getMgr().getPref("lineNumberCommand");

        if ((editorCommand != null) && (editorCommand != "")) {
            execString.append(editorCommand + " " + filename);

            if ((lineNumberCommand != null) && (lineNumberCommand != "")) {
                execString.append(" " + lineNumberCommand +
                        (new Integer(lineNumber)).toString());
            }

            execString.append("')\">");
        }

        System.out.println("INFO in Edit.editFile(): Exec string " +
                execString.toString());

        try {
            Process _edit = Runtime.getRuntime().exec(execString.toString());
        } catch (IOException ioe) {
            System.out.println("Error in Edit.editFile(): Unable to start editor: " +
                    editorCommand);
            System.out.println(ioe.getMessage());
        }
    }
}
