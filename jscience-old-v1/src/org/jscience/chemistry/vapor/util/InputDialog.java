/*
 * Basic input dialog for value selection.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;


/**
 * Basic input dialog for value selection.
 */
public class InputDialog extends JDialog implements ActionListener,
    WindowListener {
    /**
     * DOCUMENT ME!
     */
    private Object[] returnVal = null;

    /**
     * DOCUMENT ME!
     */
    private JList list = null;

    /**
     * Creates a new InputDialog object.
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param selectValues DOCUMENT ME!
     * @param multiSelect DOCUMENT ME!
     */
    protected InputDialog(Frame owner, String title, String message,
        Object[] selectValues, boolean multiSelect) {
        super(owner, title, true);

        JPanel contentPane = (JPanel) getContentPane();
        JPanel bottomPanel = new JPanel();
        JLabel msgLabel = new JLabel(message);
        JScrollPane scrollPane = new JScrollPane();
        JButton okButton = new JButton(MessageHandler.getString("OK"));
        JButton cancelButton = new JButton(MessageHandler.getString("Cancel"));
        EmptyBorder emptyBorder = new EmptyBorder(0, 10, 5, 10);
        EtchedBorder etchedBorder = new EtchedBorder();

        list = new JList(selectValues);
        contentPane.setLayout(new BorderLayout());
        addWindowListener(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        if (!multiSelect) {
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        msgLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        contentPane.add(msgLabel, BorderLayout.NORTH);

        scrollPane.setViewportView(list);
        scrollPane.setBorder(new CompoundBorder(emptyBorder, etchedBorder));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        okButton.setPreferredSize(new Dimension(60, 27));
        cancelButton.setPreferredSize(new Dimension(90, 27));
        okButton.setActionCommand("OKAY");
        cancelButton.setActionCommand("CANCEL");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        pack();

        int x = owner.getX() + ((owner.getWidth() - getWidth()) / 2);
        int y = owner.getY() + ((owner.getHeight() - getHeight()) / 2);

        setLocation(x, y);
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param selectValues DOCUMENT ME!
     * @param multiSelect DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object[] show(Frame owner, String title, String message,
        Object[] selectValues, boolean multiSelect) {
        InputDialog dialog = new InputDialog(owner, title, message,
                selectValues, multiSelect);

        Object[] retVal = dialog.returnVal;
        dialog.dispose();

        return retVal;
    }

    /**
     * DOCUMENT ME!
     */
    private void okay() {
        returnVal = list.getSelectedValues();
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     */
    private void close() {
        returnVal = null;
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ev) {
        String action = ev.getActionCommand();

        if (action.equals("OKAY")) {
            okay();
        } else if (action.equals("CANCEL")) {
            close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowClosing(WindowEvent ev) {
        close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowActivated(WindowEvent ev) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowClosed(WindowEvent ev) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent ev) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent ev) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowIconified(WindowEvent ev) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void windowOpened(WindowEvent ev) {
    }
}
