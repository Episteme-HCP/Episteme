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

package org.jscience.linguistics.braille.tests;

import org.jscience.linguistics.braille.utils.*;


/*
 * OutputTestTerminal
 * Shows the output of the BrailleTrans classes in the terminal.
 *
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 */
public class OutputTestTerminal extends Translator {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        String inFilename = "";
        String languageToUse = "";
        Language256 language256 = null;
        LanguageUnicode languageUnicode = null;
        LanguageInteger languageInteger = null;
        String result256 = null;
        String resultInteger = null;
        String resultUnicode = null;

        int state = 1;

        if (args.length != 3) {
            System.out.println("OutputTestOld");
            System.out.println(
                "USAGE  java OutputTestTerminal <file> <language> <state>");
            System.exit(SUCCESS);
        }

        inFilename = new String(args[0]);
        languageToUse = new String(args[1]);
        state = Integer.parseInt(args[2]);
        args = null;

        language256 = new Language256(languageToUse);
        language256.setState(state);
        languageUnicode = new LanguageUnicode(languageToUse);
        languageUnicode.setState(state);
        languageInteger = new LanguageInteger(languageToUse);
        languageInteger.setState(state);

        // Get the input to translate
        result256 = turnIntoString(language256.translate(readIntArrayFromDisk(
                        inFilename)));
        resultInteger = turnIntoString(languageInteger.translate(
                    readIntArrayFromDisk(inFilename)));
        resultUnicode = languageUnicode.translate(readStringFromDisk(inFilename));

        System.out.println("Translation Results" + "\n" + "Language256" + "\n" +
            result256 + "\n\n" + "LanguageInteger" + "\n" + resultInteger +
            "\n\n" + "LanguageUnicode" + "\n" + resultUnicode + "\n\n");
        System.exit(SUCCESS);
    }
}
