/*
---------------------------------------------------------------------
VIRTUAL PLANTS
==============

University of applied sciences Biel

hosted by Claude Schwab

programmed by Rene Gressly

March - July 1999

---------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.common;

/**
 * This class handles all the error messages, warnings and infos. It throws
 * exceptions if the error makes it impossible to proceed the program. All the
 * error messages are stored in this class. Debugging info can easily be
 * turned off in the member funtction debug.
 */
public class Log {
    /**
     * All definitions are used as error code. Use this one if no file
     * can be found
     */
    public static final int NO_FILE = 1;

    /** An invalid sign is found in the axiom or rule */
    public static final int INVALID_SIGN = 2;

    /** The predecessor is invalid. Too long or not a letter */
    public static final int INVALID_PRED = 3;

    /** No sign for the rule has been found */
    public static final int NO_RULESIGN = 4;

    /**
     * Prints the line to the standard out.
     *
     * @param strOut The string to print.
     */
    public static void log(String strOut) {
        System.out.println(strOut);
    }

    /**
     * Use this function to log an error and then throw an exception.
     * This is use if the program can not be continued due to the error.
     *
     * @param strInfo Supplemental information of the error.
     * @param iErrorCode The type of the error matching the defined value.
     * @param bThrow Specify if an exception shall be thrown or not. If not the
     *        calling method must handle the exception anyways.
     *
     * @throws Exception An exception containing the error information.
     */
    public static void log(String strInfo, int iErrorCode, boolean bThrow)
        throws Exception {
        strInfo = getMsg(iErrorCode) + strInfo;
        log(strInfo);

        if (bThrow == true) {
            throw new Exception(strInfo);
        }
    }

    /**
     * Use this function to log an error.
     *
     * @param strInfo Supplemental information of the error.
     * @param iErrorCode The type of the error matching the defined value.
     */
    public static void log(String strInfo, int iErrorCode) {
        log(getMsg(iErrorCode) + strInfo);
    }

    /**
     * This is used to output debugging information. It can be easily
     * removed in a final version of the program.
     *
     * @param strDebug The string to output.
     */
    public static void debug(String strDebug) {
        //log(strDebug);		//eliminate this line to avoid debug info.
    }

    /**
     * Evaluates the string representing the error code. All error
     * messages are listed here. If the specified value does not mach to a
     * defined error code a standart message will be returned.
     *
     * @param iErrorCode The defined value representing the error.
     *
     * @return DOCUMENT ME!
     */
    private static String getMsg(int iErrorCode) {
        switch (iErrorCode) {
        case NO_FILE:
            return "File does not exist or can not be read: ";

        case INVALID_SIGN:
            return "Invalid sign found in axiom or successor: ";

        case INVALID_PRED:
            return "Predecessor is too long or not a letter.";

        case NO_RULESIGN:
            return "No sign to separate the rule has been found.";

        default:
            return "Not defined error. Info: ";
        }
    }
}
