/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ConsoleLog prints abbreviated log messages to the console
 * using <code>System.out.println()</code>.
 * ConsoleLog logs synchronously.
 *
 * @author Holger Antelmann
 * @see LogException
 * @see Logger
 * @see LogEntry
 */
public class ConsoleLog extends AbstractLogWriter {
    static SimpleDateFormat dformat = new SimpleDateFormat("k:mm:ss");
    static Date currentTime = new Date();

    public ConsoleLog() {
    }

    /**
     * This method writes a short version of the entry to the console.
     * <br>The format is: "[<i>time-level<i>] <i>message</i> (<i>param1</i>)"<br>
     * If an exception was included, it will be printed in the next line.
     * The other parameters are ignored.
     */
    public synchronized void writeLogEntry(Object pattern) {
        LogEntry entry = (LogEntry) pattern;
        currentTime.setTime(entry.time);
        String s = "[" + dformat.format(currentTime) + "-" + entry.level + "] ";
        s += ((entry.message == null) ? "n/a" : entry.message);
        if ((entry.parameters != null) && (entry.parameters.length > 0) && (entry.parameters[0] != null))
            s += " (" + entry.parameters[0].toString() + ")";
        System.out.println(s);
        if (entry.thrown != null) {
            System.out.println("[!exception logged:] " + entry.thrown.getClass().getName()
                    + ": " + entry.thrown.getMessage());
            //entry.thrown.printStackTrace();
        }
    }
}
