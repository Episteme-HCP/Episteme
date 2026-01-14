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

import org.jscience.linguistics.braille.tests.Stopwatch;

import java.io.IOException;


/*
 * TranslateInteger
 * Uses the LanguageInteger class to perform translation.
 *
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 */
public class TranslateInt extends Translator {
    /** DOCUMENT ME! */
    static final int LANGUAGE_DATA_ERROR = 3;

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String inFilename = "";
        String languageToUse = "";
        int[] toConvert = null;
        LanguageInteger language = null;
        int state = 1;
        Stopwatch toLoad = new Stopwatch();
        Stopwatch toRun = new Stopwatch();

        if (args.length != 3) {
            System.out.println("TranslateInteger");
            System.out.println(
                "USAGE  java TranslateInt <file> <language> <state>");
            System.exit(SUCCESS);
        }

        inFilename = new String(args[0]);
        languageToUse = new String(args[1]);
        state = Integer.parseInt(args[2]);
        args = null;

        try {
            toLoad.start();
            language = new LanguageInteger(languageToUse);
            toLoad.stop();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(DISK_ERROR);
        } catch (LanguageLegacyDatafileFormatException e) {
            System.err.println(e);
            System.exit(LANGUAGE_DATA_ERROR);
        }

        language.setState(state);

        // Get the input to translate
        try {
            toConvert = Translator.readIntArrayFromDisk(inFilename);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(DISK_ERROR);
        }

        toRun.start();

        int[] output = language.translate(toConvert);
        toRun.stop();

        System.out.println("Result:" + Translator.turnIntoString(output));
        System.out.println("Time to load: " + toLoad.getTime());
        System.out.println("Time to run: " + toRun.getTime());
    }
}
