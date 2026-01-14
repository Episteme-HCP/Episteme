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

import org.jscience.util.Settings;
import org.jscience.util.license.Licensed;

import java.awt.*;

import java.io.*;

import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 *
 * @see JMessenger
 * @see javax.swing.ProgressMonitorInputStream
 */
class JFileTransmitter extends Thread implements Licensed {
    /** DOCUMENT ME! */
    static int RECEIVER = 1;

    /** DOCUMENT ME! */
    static int SENDER = 2;

    /** DOCUMENT ME! */
    Component parent;

    /** DOCUMENT ME! */
    File file;

    /** DOCUMENT ME! */
    InetAddress inetaddr;

    /** DOCUMENT ME! */
    Socket socket;

    /** DOCUMENT ME! */
    int port;

    /** DOCUMENT ME! */
    long length;

    /** DOCUMENT ME! */
    int type;

    /** DOCUMENT ME! */
    NetConnection con;

/**
     * Creates a new JFileTransmitter object.
     *
     * @param parent DOCUMENT ME!
     * @param con    DOCUMENT ME!
     * @param socket DOCUMENT ME!
     * @param file   DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public JFileTransmitter(Component parent, NetConnection con, Socket socket,
        File file, long length) {
        super("JFileTransmitter receiver");
        this.parent = parent;
        this.con = con;
        this.socket = socket;
        this.file = file;
        this.length = length;
        type = RECEIVER;
    }

/**
     * Creates a new JFileTransmitter object.
     *
     * @param parent   DOCUMENT ME!
     * @param file     DOCUMENT ME!
     * @param inetaddr DOCUMENT ME!
     * @param port     DOCUMENT ME!
     */
    public JFileTransmitter(Component parent, File file, InetAddress inetaddr,
        int port) {
        super("JFileTransmitter sender");
        this.parent = parent;
        this.file = file;
        this.inetaddr = inetaddr;
        this.port = port;
        type = SENDER;
    }

    /**
     * receives/sends a file through a connection
     */
    public void run() {
        Settings.checkLicense(this);

        if (type == SENDER) {
            ProgressMonitorInputStream in = null;
            OutputStream out = null;

            try {
                String note = "transmitting " + file.getName() + " (bytes: " +
                    length + ")";
                in = new ProgressMonitorInputStream(parent, note,
                        new FileInputStream(file));

                Socket s = new Socket(inetaddr, port);
                out = s.getOutputStream();

                ProgressMonitor mon = in.getProgressMonitor();
                mon.setMaximum((int) length);
                mon.setMillisToDecideToPopup(0);
                mon.setMillisToPopup(0);

                byte[] buffer = new byte[16384];
                int n = 0;

                while (n > -1) {
                    n = in.read(buffer);

                    if (n < 0) {
                        break;
                    }

                    out.write(buffer, 0, n);
                }

                out.flush();
                JOptionPane.showMessageDialog(parent,
                    "file transfer complete (" + file.getName() + ")");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, e.getMessage(),
                    "file transfer failed", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                } catch (NullPointerException e) {
                }
            }
        } else {
            assert type == RECEIVER;

            FileOutputStream out = null;
            ProgressMonitorInputStream in = null;

            try {
                out = new FileOutputStream(file);

                String note = "saving " + file.getName() + " (bytes: " +
                    length + ")";
                in = new ProgressMonitorInputStream(parent, note,
                        socket.getInputStream());

                ProgressMonitor mon = in.getProgressMonitor();
                mon.setMaximum((int) length);
                mon.setMillisToDecideToPopup(0);
                mon.setMillisToPopup(0);

                byte[] buffer = new byte[16384];
                int n = 0;
                int read = 0;

                while ((n > -1) && (read < length)) {
                    n = in.read(buffer);
                    read += n;

                    if (n < 0) {
                        break;
                    }

                    out.write(buffer, 0, n);
                }

                out.flush();
                JOptionPane.showMessageDialog(parent,
                    "file received: " + file.getName());
            } catch (IOException e) {
                try {
                    con.sendMessage(new JMessenger.FileSendAbort());
                } catch (IOException ex) {
                }

                JOptionPane.showMessageDialog(parent, e.getMessage(),
                    "file receive failed", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    out.close();
                    in.close();
                } catch (IOException e) {
                } catch (NullPointerException e) {
                }
            }
        }
    }
}
