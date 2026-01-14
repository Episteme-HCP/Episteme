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
