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

package org.jscience.ml.cml.logger;

import org.jscience.ml.cml.AbstractBase;
import org.jscience.ml.cml.CMLException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.*;

/**
 * logger for JUMBO/CML.
 * allows for XML-absed logging
 */
public class JumboLogger extends Logger {

    static JumboLogger theLogger;

    static {
        setHandlers();
    }

    static void setHandlers() {
        if (theLogger == null) {
            theLogger = (JumboLogger) JumboLogger.getLogger("");
            theLogger.setLevel(Level.FINE);
            theLogger.addHandler(new JumboConsoleHandler());
            try {
                theLogger.addHandler(new JumboXmlHandler("jumbolog.xml"));
            } catch (IOException ioe) {
                theLogger.getHandlers()[0].getErrorManager().error("Bad log file", ioe, ErrorManager.OPEN_FAILURE);
            }
//            theLogger.info("created logger");
        }
    }

    protected JumboLogger(String s) {
        super(s, null);
    }

    /**
     * returns a singleton JumboLogger.
     *
     * @param s see Logger for use
     * @return the logger (should be cast to JumboLogger)
     */
    public static Logger getLogger(String s) {
        if (theLogger == null) {
            theLogger = new JumboLogger(s);
        }
        return theLogger;
    }

    /**
     * intercept log.
     *
     * @param record as in Logger
     */
    public void log(LogRecord record) {
// route to other handlers as normal
        super.log(record);
// only for testing
        Object[] params = record.getParameters();
        String s = record.getSourceClassName();
        s = s.substring(s.lastIndexOf(".") + 1);
        s += "." + record.getSourceMethodName() + "(" +
                record.getMessage();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                s += ", " + params[i];
            }
        }
        s += ")";
        System.out.println(record.getLevel() + " " + s);
    }

    /**
     * closes handlers.
     * Seems to be required explicitly.
     */
    public void close() {
        Handler[] handlers = this.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].close();
        }
    }

    /**
     * Write XML to a Logger.
     * <p/>
     * This method writes a Node as an XML string to a logger
     *
     * @param level The Level for the logger to log at
     * @param base  The Object to log in XML
     */
    public void logXML(Level level, final AbstractBase base) {
        StringWriter sw = new StringWriter();
        try {
            base.writeXML(sw);
        } catch (IOException ioe) {
            log(Level.SEVERE, "IOException: Couldn't log  base " + ioe);
        } catch (CMLException cmle) {
            log(Level.SEVERE, "CMLException: Couldn't log  base " + cmle);
        }

        log(level, sw.toString());
    }

    /**
     * Write Exception to a Logger.
     * <p/>
     * This method writes an Execption  to a logger
     *
     * @param level The Level for the logger to log at
     * @param t     The Throwable Object to log
     */
    public void logThrowable(Level level, final Throwable t) {
        Throwable th = t.fillInStackTrace();

        if (th != null) {
            StackTraceElement[] stackTrace = th.getStackTrace();

            if (stackTrace != null) {
                for (int i = 0; i < stackTrace.length; ++i) {
                    log(level, stackTrace[i].toString());
                }
            }
        }
    }
}

