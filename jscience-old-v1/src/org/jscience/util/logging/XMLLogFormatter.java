/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
