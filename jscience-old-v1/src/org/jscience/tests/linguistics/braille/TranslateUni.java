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

import org.jscience.linguistics.braille.tests.*;

import javax.swing.*;


/*
 * TranslateUni
 * Uses the LanguageUnicode class to perform translation.
 *
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 */
public class TranslateUni extends Translator {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String inFilename = "";
        String languageToUse = "";
        String toConvert = null;
        LanguageUnicode language = null;
        int state = 1;
        Stopwatch toLoad = new Stopwatch();
        Stopwatch toRun = new Stopwatch();

        if (args.length != 3) {
            System.out.println("TranslateUni");
            System.out.println(
                "USAGE  java TranslateUni <file> <language> <state>");
            System.exit(SUCCESS);
        }

        inFilename = new String(args[0]);
        languageToUse = new String(args[1]);
        state = Integer.parseInt(args[2]);
        args = null;

        try {
            toLoad.start();
            language = new LanguageUnicode(languageToUse);
            toLoad.stop();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(DISK_ERROR);
        }

        language.setState(state);

        // Get the input to translate
        try {
            toConvert = Translator.readStringFromDisk(inFilename);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(DISK_ERROR);
        }

        toRun.start();

        String output = language.translate(toConvert);
        toRun.stop();

        System.out.println("Result: " + output);
        JOptionPane.showMessageDialog(null, output, "Translation Output",
            JOptionPane.NO_OPTION);

        System.out.println("Time to load: " + toLoad.getTime());
        System.out.println("Time to run: " + toRun.getTime());
        System.exit(SUCCESS);
    }
}
