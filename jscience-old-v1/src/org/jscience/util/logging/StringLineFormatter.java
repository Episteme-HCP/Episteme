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
