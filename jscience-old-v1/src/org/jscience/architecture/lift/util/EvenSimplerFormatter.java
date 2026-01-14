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

package org.jscience.architecture.lift.util;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


/**
 * 
DOCUMENT ME!
 *
 * @author Nagy Elemér Károly
 */
public class EvenSimplerFormatter extends SimpleFormatter {
/**
     * Constructor for the EvenSimplerFormatter object
     */
    public EvenSimplerFormatter() {
        super();
    }

    /**
     * We have to override/hide/shadow the format() of SimpleFormatter.
     * As a matter of fact, we only have to override this one. Nice design.
     *
     * @param record The LogRecord to format.
     *
     * @return The formatted, localized message.
     */
    public String format(LogRecord record) {
        // Faster & cleaner to build up strings than a some new String() + new String() + new String()
        StringBuffer SB = new StringBuffer(record.getMessage().length() + 32);

        if (record == null) {
            return ("NULL");
        }

        //This is a fairly basic, human-comaptible, c-compatible, universal format.
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS#");

        SB.append(SDF.format(new Date()));

        //We also log the level
        if (record.getLevel() != null) {
            SB.append(record.getLevel().getLocalizedName());
            SB.append("#");
        }

        SB.append(tryToLocalize(record.getMessage(), record));

        // And we need to log the parameters
        Object[] Params = record.getParameters();

        if (Params != null) {
            for (int i = 0; i < Params.length; i++) {
                SB.append("#");
                SB.append(Params[i].toString());
            }
        }

        SB.append("\n");

        return (SB.toString());
    }

    /**
     * The main (test) program for the EvenSimplerFormatter.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        System.out.println("EvenSimpleFormatter started.");

        java.util.logging.Logger MyLog = java.util.logging.Logger.getAnonymousLogger();
        MyLog.setUseParentHandlers(false);

        try {
            java.util.logging.Handler MyFileHandler = new java.util.logging.FileHandler(
                    "ESFtesz.log");

            MyFileHandler.setFormatter(new EvenSimplerFormatter());
            MyLog.addHandler(MyFileHandler);
            MyLog.setLevel(Level.INFO);
        } catch (java.io.IOException IOE) {
            System.out.println(IOE.toString());
        }

        MyLog.log(MyLog.getLevel(), "TESZT1", new Integer(1));
        MyLog.log(MyLog.getLevel(), "TESZT2", new Integer(2));
        MyLog.log(MyLog.getLevel(), "TESZT3", new Integer(3));
        MyLog.log(MyLog.getLevel(), "TESZT4", new Integer(4));

        System.out.println("EvenSimplerFormatter stopped.");
    }

    /**
     * This Method is create to serve a simple goal: it must try to
     * localize a string if it is possible. If localization is not possible,
     * it will return the original string. In c it would be surely a macro or
     * an inliner.
     *
     * @param Message The message to localize.
     * @param MyRB A ResourceBundle that contains localization.
     *
     * @return The localized string on succes, the originial Message at
     *         failure.
     */
    public String tryToLocalize(String Message, ResourceBundle MyRB) {
        if (MyRB == null) {
            return (Message);
        }

        try {
            String TmpString = MyRB.getString(Message);

            if (TmpString == null) {
                return (Message);
            } else {
                return (TmpString);
            }
        } catch (MissingResourceException MRE) {
            return (Message);
        }
    }

    /**
     * This Method is created to serve a simple goal: it must try to
     * localize a string if it is possible. If localization is not possible,
     * it will return the original string. In c it would be surely a macro or
     * an inliner.
     *
     * @param Message The message to localize.
     * @param record The LogRecord used to get the ResourceBundle.
     *
     * @return The localized string on succes, NULL (LogRecord) on
     *         null==record, or the originial Message at failure.
     */
    public String tryToLocalize(String Message, LogRecord record) {
        if (record == null) {
            return ("NULL (LogRecord)");
        }

        return (tryToLocalize(Message, record.getResourceBundle()));
    }
}
