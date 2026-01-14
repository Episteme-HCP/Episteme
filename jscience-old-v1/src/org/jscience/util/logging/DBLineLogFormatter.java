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
