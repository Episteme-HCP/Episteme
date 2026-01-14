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

package org.jscience.tests.net.ntp.gui;

import org.jscience.net.ntp.NtpConnection;
import org.jscience.net.ntp.NtpInfo;

import java.awt.*;

import java.net.InetAddress;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
class TraceDialog extends JDialog {
    /** DOCUMENT ME! */
    private static final int rowCount = 6;

    /** DOCUMENT ME! */
    private Vector trace = new Vector();

    /** DOCUMENT ME! */
    private AbstractTableModel traceTableModel;

    /** DOCUMENT ME! */
    private Properties ntpProperties;

    /** DOCUMENT ME! */
    private NumberFormat f = new DecimalFormat();

    /** DOCUMENT ME! */
    private TraceDialog thisReference = this;

/**
     * Creates a new TraceDialog object.
     *
     * @param propertiesListener DOCUMENT ME!
     */
    protected TraceDialog(PropertiesListener propertiesListener) {
        f.setMaximumFractionDigits(2);
        ntpProperties = propertiesListener.getProperties();

        Container pane = getContentPane();
        traceTableModel = new AbstractTableModel() {
                    public String getColumnName(int column) {
                        if (column == 0) {
                            return "Server";
                        } else if (column == 1) {
                            return "Stratum";
                        } else if (column == 2) {
                            return "Version";
                        } else if (column == 3) {
                            return "Dispersion";
                        } else if (column == 4) {
                            return "Offset";
                        } else {
                            return "Code";
                        }
                    }

                    public int getRowCount() {
                        return rowCount;
                    }

                    public int getColumnCount() {
                        return 6;
                    }

                    public Object getValueAt(int row, int column) {
                        int size = trace.size();

                        if (row > (size - 1)) {
                            return null;
                        } else {
                            if (column == 0) {
                                InetAddress temp = ((NtpInfo) trace.elementAt(row)).serverAddress;

                                return (String) (temp.getHostName());
                            } else if (column == 1) {
                                return new Integer(((NtpInfo) trace.elementAt(
                                        row)).stratum);
                            } else if (column == 2) {
                                return new Integer(((NtpInfo) trace.elementAt(
                                        row)).versionNumber);
                            } else if (column == 3) {
                                return f.format(((NtpInfo) trace.elementAt(row)).rootDispersion);
                            } else if (column == 4) {
                                long temp = ((NtpInfo) trace.elementAt(row)).offset;
                                long offset = Long.parseLong(ntpProperties.getProperty(
                                            "offset"));

                                return new Long(temp - offset);
                            } else {
                                Object temp = ((NtpInfo) trace.elementAt(row)).referenceIdentifier;

                                if (temp instanceof String) {
                                    return (String) temp;
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                };

        JTable traceTable = new JTable(traceTableModel);
        TableColumnModel c = traceTable.getColumnModel();
        c.getColumn(0).setPreferredWidth(100);
        c.getColumn(1).setPreferredWidth(30);
        c.getColumn(2).setPreferredWidth(30);
        c.getColumn(3).setPreferredWidth(30);
        c.getColumn(4).setPreferredWidth(30);
        c.getColumn(5).setPreferredWidth(30);
        traceTable.setPreferredScrollableViewportSize(new Dimension(600, 70));

        JScrollPane scrollPane = new JScrollPane(traceTable);
        setContentPane(scrollPane);

        //    pane.add("North",traceTable.getTableHeader());
        //    Dimension d=getPreferredSize();
        //    setSize(600,d.height);
        setResizable(false);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ntpServer DOCUMENT ME!
     * @param button DOCUMENT ME!
     */
    protected void showTrace(String ntpServer, JButton button) {
        button.setEnabled(false);
        thisReference.setVisible(true);
        new TraceWorker(ntpServer, button);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    private class TraceWorker extends SwingWorker {
        /** DOCUMENT ME! */
        private String ntpServer;

        /** DOCUMENT ME! */
        private JButton button;

        /** DOCUMENT ME! */
        private Vector temporaryTrace;

        /** DOCUMENT ME! */
        private boolean errorOccurred = false;

/**
         * Creates a new TraceWorker object.
         *
         * @param ntpServer DOCUMENT ME!
         * @param button    DOCUMENT ME!
         */
        TraceWorker(String ntpServer, JButton button) {
            this.ntpServer = ntpServer;
            this.button = button;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object construct() {
            try {
                NtpConnection ntpConnection = new NtpConnection(InetAddress.getByName(
                            ntpServer));
                temporaryTrace = ntpConnection.getTrace();
                ntpConnection.close();
                errorOccurred = false;
            } catch (Exception e) {
                errorOccurred = true;
            } finally {
                button.setEnabled(true);

                return null;
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void finished() {
            if (errorOccurred) {
                JOptionPane.showMessageDialog(thisReference,
                    "There is a server problem", "", JOptionPane.ERROR_MESSAGE);
            } else {
                thisReference.setVisible(true);
                trace = temporaryTrace;
                traceTableModel.fireTableDataChanged();
            }
        }
    }
}
