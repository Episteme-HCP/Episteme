/**
 * Author Nagy Elemer Karoly<br>
 * This file is licensed under the GNU Public Licens (GPL). <br>
 * Changelog:<br>
 * 2005 March 13 - Working, tested, no further improvements foresighted.<br>
 * This class is designed to enable even simpler formatting than SimpleFormatter. This means that
 * log messages are only one line long, and they have a precise timestamp. Also, parameters are logged into
 * the same line.
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
