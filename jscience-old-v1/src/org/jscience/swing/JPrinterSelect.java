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

package org.jscience.swing;

import org.jscience.util.PrintUtils;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
public class JPrinterSelect extends JComponent {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 6016518754683820149L;

    /**
     * DOCUMENT ME!
     */
    JList list;

    /**
     * Creates a new JPrinterSelect object.
     */
    public JPrinterSelect() {
        this(false);
    }

    /**
     * Creates a new JPrinterSelect object.
     *
     * @param showInfo DOCUMENT ME!
     */
    public JPrinterSelect(boolean showInfo) {
        setLayout(new BorderLayout());
        setName("Printer Selection");

        Vector<PrintWrapper> p = new Vector<PrintWrapper>();
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);

        for (int i = 0; i < ps.length; i++) {
            p.add(new PrintWrapper(ps[i]));
        }

        list = new JList(p);

        PrintService dps = PrintServiceLookup.lookupDefaultPrintService();

        if (dps != null) {
            list.setSelectedValue(new PrintWrapper(dps), true);
        }

        if (showInfo) {
            add(new JScrollPane(list), BorderLayout.WEST);

            final JTextArea area = new JTextArea();
            area.setEditable(false);
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ev) {
                    PrintService service = ((PrintWrapper) list.getSelectedValue()).ps;

                    if (service == null) {
                        area.setText(null);
                    } else {
                        area.setText(PrintUtils.getInfoOn(service));
                        area.setCaretPosition(0);
                    }
                }
            });

            JScrollPane sp = new JScrollPane(area);
            sp.setPreferredSize(new Dimension(300, 200));
            add(sp, BorderLayout.CENTER);
        } else {
            add(new JScrollPane(list));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintService getSelectedPrinter() {
        return ((PrintWrapper) list.getSelectedValue()).ps;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static PrintService showDialog(Component parent) {
        return showDialog(parent, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent   DOCUMENT ME!
     * @param showInfo DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static PrintService showDialog(Component parent, boolean showInfo) {
        JPrinterSelect jps = new JPrinterSelect(showInfo);
        int a = JOptionPane.showConfirmDialog(parent, jps,
                Menus.language.getString("selectPrinter"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (a == JOptionPane.OK_OPTION) {
            return jps.getSelectedPrinter();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.2 $
     */
    static class PrintWrapper {
        /**
         * DOCUMENT ME!
         */
        PrintService ps;

        /**
         * Creates a new PrintWrapper object.
         *
         * @param ps DOCUMENT ME!
         */
        PrintWrapper(PrintService ps) {
            if (ps == null) {
                throw new NullPointerException("PrintService must not be null");
            }

            this.ps = ps;
        }

        /**
         * DOCUMENT ME!
         *
         * @param obj DOCUMENT ME!
         * @return DOCUMENT ME!
         */
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            return ps.equals(((PrintWrapper) obj).ps);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return ps.getName();
        }
    }
}
