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

package org.jscience.net;

import org.jscience.util.logging.Level;
import org.jscience.util.logging.LogWriter;
import org.jscience.util.logging.Logger;

import java.io.IOException;


/**
 * MessageDelegator listens for incoming messages on the given connection
 * and delegates the message to the handler - once its <code>run()</code>
 * method is called. The MessageDelegator will listen continuously until
 * <code>stopListening()</code> is called or an IOException is caught. The
 * MessageDelegator can be used as a Runnable for the
 * <code>createHandlerThread()</code> in ConnectionDispatcher.
 *
 * @author Holger Antelmann
 *
 * @see ConnectionDispatcher#createHandlerThread(NetConnectioncon)
 * @see NetConnection
 * @see NetConnectionHandler
 */
public class MessageDelegator extends Thread {
    /**
     * DOCUMENT ME!
     */
    NetConnectionHandler handler;

    /**
     * DOCUMENT ME!
     */
    NetConnection con;

    /**
     * DOCUMENT ME!
     */
    int count = 0;

    /**
     * DOCUMENT ME!
     */
    int missed = 0;

    /**
     * DOCUMENT ME!
     */
    boolean enabled = true;

    /**
     * DOCUMENT ME!
     */
    boolean continuousMode;

    /**
     * DOCUMENT ME!
     */
    Logger logger;

    /**
     * Creates a new MessageDelegator object.
     *
     * @param con DOCUMENT ME!
     * @param handler DOCUMENT ME!
     */
    public MessageDelegator(NetConnection con, NetConnectionHandler handler) {
        this(con, handler, true, null);
    }

/**
     * if logger is not null, it will be used to log established and closed connections
     */
    public MessageDelegator(NetConnection con, NetConnectionHandler handler,
        Logger logger) {
        this(con, handler, true, logger);
    }

/**
     * if continuousMode false, the connection handling thread will exit after the first message received;
     * if logger is not null, it will be used to log established and closed connections
     *
     * @deprecated
     */
    @Deprecated
    private MessageDelegator(NetConnection con, NetConnectionHandler handler,
        boolean continuousMode, Logger logger) {
        if ((con == null) || (handler == null)) {
            throw new NullPointerException();
        }

        this.handler = handler;
        this.con = con;
        this.continuousMode = continuousMode;
        this.logger = logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NetConnection getConnection() {
        return con;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NetConnectionHandler getHandler() {
        return handler;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * DOCUMENT ME!
     *
     * @param logger DOCUMENT ME!
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * disables the listener and causes run() to exit after either the
     * next message is read or an IOException is thrown; it does <b>not</b>
     * close the connection
     */
    public void stopListening() {
        enabled = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMessageCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetMessageCount() {
        count = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param on DOCUMENT ME!
     */
    @Deprecated
    private void setContinuousMode(boolean on) {
        continuousMode = on;
    }

    /**
     * if false, the run() method will exit after a single message was
     * received
     *
     * @return DOCUMENT ME!
     */
    public boolean getContinuousMode() {
        return continuousMode;
    }

    /**
     * run() listens for incoming messages and delegates to the
     * handler. If a Logger is present, it will log the following entries:
     *  <ul>
     *      <li>every message processed with Level.FINE</li>
     *      <li>ClassNotFoundExceptions with Level.WARNING
     *      (exeption will be included)</li>
     *      <li>IOExceptions with Level.WARNING (exeption will be
     *      included)</li>
     *  </ul>
     *
     * @see Logger
     * @see LogWriter
     * @see Level
     */
    public void run() {
        Object message;

        while (enabled) {
            try {
                message = con.readMessage();

                if (logger != null) {
                    String s = "message processed from: ";
                    s += con.getSocket().getInetAddress().getHostName();
                    s += (" port " + con.getSocket().getPort());
                    s += (", message class: " + message.getClass().getName());
                    logger.log(this, Level.FINE, s);
                }

                count++;

                try {
                    if (handler != null) {
                        handler.handleMessage(message, con);
                    }
                } catch (Exception ex) {
                    if (logger != null) {
                        logger.log(ex);
                    } else {
                        ex.printStackTrace();
                    }
                }

                if (!continuousMode) {
                    break;
                }
            } catch (ClassNotFoundException e) {
                missed++;

                if (logger != null) {
                    String s = "the received message could not be interpreted; con details: ";
                    s += con.getSocket().getInetAddress().getHostName();
                    s += (" port " + con.getSocket().getPort());
                    logger.log(this, Level.WARNING, s, e);
                }

                continue;
            } catch (IOException e) {
                //e.printStackTrace();
                if (logger != null) {
                    String s = "IOException (probably a closed connection)";
                    s += "; connection attached";
                    logger.log(this, Level.CONFIG, s, e, new Object[] { con });
                }

                handler.connectionLost(con);

                break;
            }
        }
    }

    /**
     * returns the number of messages that were missed due to a
     * ClassNotFoundException (unknown messages)
     *
     * @return DOCUMENT ME!
     */
    public int getMissedMessages() {
        return missed;
    }
}
