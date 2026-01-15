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
