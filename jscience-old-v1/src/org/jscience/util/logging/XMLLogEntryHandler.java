/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.StringUtils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;


/**
 * not quite done yet; still experimental
 *
 * @author Holger Antelmann
 */
class XMLLogEntryHandler extends DefaultHandler {
    /** DOCUMENT ME! */
    Logger logger;

    /** DOCUMENT ME! */
    LogEntry entry;

    /** DOCUMENT ME! */
    StringBuilder data = new StringBuilder();

    /** DOCUMENT ME! */
    Attributes attr;

    /** DOCUMENT ME! */
    boolean inside = false;

    /** DOCUMENT ME! */
    String thrownMessage = null;

    /** DOCUMENT ME! */
    String stringValue = null;

/**
     * Creates a new XMLLogEntryHandler object.
     *
     * @param logger DOCUMENT ME!
     */
    XMLLogEntryHandler(Logger logger) {
        this.logger = logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     */
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) {
        data.delete(0, data.length());
        attr = attributes;

        if (qName.equals("org.jscience.util.logging.LogEntry")) {
            entry = new LogEntry();
            inside = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void characters(char[] ch, int start, int length) {
        if (!inside) {
            return;
        }

        data.append(new String(ch, start, length).trim());
    }

    /**
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("org.jscience.util.logging.LogEntry")) {
            logger.log(entry);
            inside = false;
        }

        if (!inside) {
            return;
        }

        if (qName.equals("level")) {
            entry.setLevel(Level.forName(data.toString()));
        } else if (qName.equals("time")) {
            try {
                entry.setTime(XMLLogFormatter.timeFormat.parse(data.toString())
                                                        .getTime());
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        } else if (qName.equals("message")) {
            entry.setMessage(StringUtils.decodeUTF(data.toString().trim()));
        } else if (qName.equals("sourceClassName")) {
            entry.setSourceClassName(data.toString().trim());
        } else if (qName.equals("sourceString")) {
            entry.setSourceString(StringUtils.decodeUTF(data.toString().trim()));
        } else if (qName.equals("threadName")) {
            entry.setThreadName(StringUtils.decodeUTF(data.toString().trim()));
        } else if (qName.equals("thrownMessage")) {
            thrownMessage = StringUtils.decodeUTF(data.toString().trim());
        } else if (qName.equals("thrown")) {
            entry.setThrown(new ProxyThrowable(thrownMessage,
                    attr.getValue("className")));
        } else if (qName.equals("stringValue")) {
            stringValue = StringUtils.decodeUTF(data.toString().trim());
        } else if (qName.equals("parameter")) {
            new ProxyParameter(stringValue, attr.getValue("className"));
        } else if (qName.equals("StackTraceElement")) {
            StackTraceElement element = new StackTraceElement(attr.getValue(
                        "className"), attr.getValue("methodName"),
                    attr.getValue("fileName"),
                    Integer.parseInt(attr.getValue("lineNumber")));
        }
    }
}
