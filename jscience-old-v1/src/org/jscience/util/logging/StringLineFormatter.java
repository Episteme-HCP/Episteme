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
 * formats a LogEntry into a simple one-line String terminated by a line break.
 *
 * @author Holger Antelmann
 */
public class StringLineFormatter implements LogEntryFormatter {
    static String lineBreak = System.getProperty("line.separator", "\n");

    protected SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yy kk:mm:ss");
    Date time = new Date();
    StringBuffer buffer = new StringBuffer(256);

    /**
     * This method returns a one-line String of the entry terminated by a line separator.
     * The format is: "[<i>time<i> <i>level<i>] <i>message</i> (<i>param1</i>, <i>param2</i>, ..) (<i>thrown</i>)"<br>
     * The other parameters are ignored.
     */
    public synchronized Object format(LogEntry entry) {
        buffer.delete(0, buffer.length());
        time.setTime(entry.time);
        buffer.append("[" + timeFormat.format(time) + " " + entry.level + "] ");
        buffer.append((entry.message == null) ? "n/a" : entry.message);
        if ((entry.parameters != null) && (entry.parameters.length > 0)) {
            buffer.append(" (");
            for (int i = 0; i < entry.parameters.length; i++) {
                buffer.append(entry.parameters[i].toString());
                if (i < (entry.parameters.length - 1)) buffer.append(", ");
            }
            buffer.append(")");
        }
        if (entry.thrown != null) {
            buffer.append(" (" + entry.thrown.getClass().getName() + ": ");
            buffer.append(entry.thrown.getMessage() + ")");
        }
        buffer.append(lineBreak);
        return buffer.toString();
    }
}
