/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
