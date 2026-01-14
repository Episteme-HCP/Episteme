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

import org.jscience.util.StringUtils;

import java.text.DateFormat;

import java.util.Date;
import java.util.Locale;


/**
 * formats LogEntry objects as XML.
 *
 * @author Holger Antelmann
 */
public class XMLLogFormatter implements LogEntryFormatter {
    /** DOCUMENT ME! */
    final static String lb = StringUtils.lb;

    /** DOCUMENT ME! */
    final static DateFormat timeFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
            DateFormat.LONG, Locale.US);

    /** DOCUMENT ME! */
    StringBuilder buffer = new StringBuilder(256);

    /**
     * This method returns a String with an XML version of the entry.
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized String format(LogEntry entry) {
        buffer.delete(0, buffer.length());
        buffer.append("<org.jscience.util.logging.LogEntry>" + lb);
        buffer.append("  <level>" + entry.getLevel() + "</level>" + lb);
        buffer.append("  <time>" + timeFormat.format(new Date(entry.time)) +
            "</time>" + lb);

        if (entry.getMessage() != null) {
            buffer.append("  <message>" + lb);
            buffer.append("    " + StringUtils.encodeUTF(entry.getMessage()) +
                lb);
            buffer.append("  </message>" + lb);
        }

        if (entry.getSourceClassName() != null) {
            buffer.append("  <sourceClassName>" + entry.getSourceClassName() +
                "</sourceClassName>" + lb);
        }

        if (entry.getSourceString() != null) {
            buffer.append("  <sourceString>" +
                StringUtils.encodeUTF(entry.getSourceString()) +
                "</sourceString>" + lb);
        }

        if (entry.getThreadName() != null) {
            buffer.append("  <threadName>" +
                StringUtils.encodeUTF(entry.getThreadName()) + "</threadName>" +
                lb);
        }

        if (entry.getThrown() != null) {
            buffer.append("  <thrown className=\"" +
                entry.getThrown().getClass().getName() + "\">" + lb);

            if (entry.getThrown().getMessage() != null) {
                buffer.append("    <thrownMessage>" + lb);
                buffer.append("      " +
                    StringUtils.encodeUTF(entry.getThrown().getMessage()) + lb);
                buffer.append("    </thrownMessage>" + lb);
            }

            buffer.append("  </thrown>" + lb);
        }

        if (entry.getParameters() != null) {
            buffer.append("  <parameters>" + lb);

            for (int i = 0; i < entry.getParameters().length; i++) {
                Object param = entry.getParameters()[i];
                buffer.append("    <parameter className=\"" +
                    param.getClass().getName() + "\">" + lb);
                buffer.append("      <stringValue>" + lb);
                buffer.append("        " +
                    StringUtils.encodeUTF(param.toString()) + lb);
                buffer.append("      </stringValue>" + lb);
                buffer.append("    </parameter>" + lb);
            }

            buffer.append("  </parameters>" + lb);
        }

        if (entry.getStackTrace() != null) {
            buffer.append("  <stackTrace>" + lb);

            for (int i = 0; i < entry.getStackTrace().length; i++) {
                StackTraceElement se = entry.getStackTrace()[i];
                buffer.append("    <StackTraceElement");
                buffer.append(" fileName=\"" + se.getFileName() + "\"");
                buffer.append(" className=\"" + se.getClassName() + "\"");
                buffer.append(" methodName=\"" + se.getMethodName() + "\"");
                buffer.append(" lineNumber=\"" + se.getLineNumber() + "\"");
                buffer.append(" \\>" + lb);
            }

            buffer.append("  </stackTrace>" + lb);
        }

        buffer.append("</org.jscience.util.logging.LogEntry>" + lb);

        return buffer.toString();
    }
}
