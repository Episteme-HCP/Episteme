/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * formats a LogEntry into a format that can be easily imported into
 * databases.
 *
 * @author Holger Antelmann
 */
public class DBLineLogFormatter implements LogEntryFormatter {
    /** DOCUMENT ME! */
    static String lineBreak = System.getProperty("line.separator", "\n");

    /** DOCUMENT ME! */
    protected SimpleDateFormat timeFormat = new SimpleDateFormat(
            "MM/dd/yy kk:mm:ss");

    /** DOCUMENT ME! */
    Date time = new Date();

    /** DOCUMENT ME! */
    StringBuilder buffer = new StringBuilder(500);

    /**
     * This method returns a one-line dif-like formattedd String of the
     * entry terminated by a line separator. The parameters are:<br>
     * time, level, message, sourceClass, thrown, threadName, param1, param2, etc.<br>
     * The time field is either written as milliseconds or as a String,
     * depending on the value of the boolean member timeAsString of this
     * class. At least one parameter field will be written, even if
     * entry.parameters is null. If any value is null, empty quotations will
     * be put instead of the String.
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized Object format(LogEntry entry) {
        buffer.delete(0, buffer.length());
        time.setTime(entry.time);
        buffer.append("\"" + timeFormat.format(time) + "\"");
        buffer.append(enclose(entry.level));
        buffer.append(enclose(entry.message));
        buffer.append(enclose(entry.sourceClassName));
        buffer.append(enclose(entry.sourceString));
        buffer.append(enclose(entry.thrown));
        buffer.append(enclose(entry.threadName));

        if (entry.parameters == null) {
            buffer.append(enclose(""));
        } else {
            for (int i = 0; i < entry.parameters.length; i++) {
                buffer.append(enclose(entry.parameters[i]));
            }
        }

        buffer.append(lineBreak);

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHeaderLine() {
        return "\"time\", \"level\", \"message\", \"sourceClass\", \"sourceString\", \"thrown\", \"thread\", \"param1\", \"param2\", \"param3\"" +
        lineBreak;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String enclose(Object obj) {
        return (", \"" + ((obj == null) ? "" : obj.toString()) + "\"");
    }
}
