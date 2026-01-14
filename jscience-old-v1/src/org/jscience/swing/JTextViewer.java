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

import org.jscience.io.ExtendedFile;

import org.jscience.util.PrintUtils;
import org.jscience.util.Settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.IOException;

import java.util.ResourceBundle;

import javax.print.PrintException;
import javax.print.PrintService;

import javax.swing.*;


/**
 * a very simple text viewer w/ copy/print/save capabilities
 *
 * @author Holger Antelmann
 *
 * @since 04/01/2004
 */
public class JTextViewer extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 1884551457870544634L;

    /** DOCUMENT ME! */
    static ResourceBundle lang = Menus.language;

    /** DOCUMENT ME! */
    static int defaultWidth = 400;

    /** DOCUMENT ME! */
    static int defaultHeigth = 400;

    /** DOCUMENT ME! */
    JTextArea ta;

    /** DOCUMENT ME! */
    Component parent;

    /** DOCUMENT ME! */
    String searchString = null;

/**
     * Creates a new JTextViewer object.
     *
     * @param text DOCUMENT ME!
     */
    public JTextViewer(String text) {
        this(lang.getString("textViewer"), text);
    }

/**
     * Creates a new JTextViewer object.
     *
     * @param title DOCUMENT ME!
     * @param text  DOCUMENT ME!
     */
    public JTextViewer(String title, String text) {
        this(title, null, text, defaultWidth, defaultHeigth);
    }

/**
     * Creates a new JTextViewer object.
     *
     * @param parent DOCUMENT ME!
     * @param text   DOCUMENT ME!
     */
    public JTextViewer(Component parent, String text) {
        this(lang.getString("textViewer"), parent, text);
    }

/**
     * Creates a new JTextViewer object.
     *
     * @param title  DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param text   DOCUMENT ME!
     */
    public JTextViewer(String title, Component parent, String text) {
        this(title, parent, text, defaultWidth, defaultHeigth);
    }

/**
     * Creates a new JTextViewer object.
     *
     * @param text   DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public JTextViewer(String text, int width, int height) {
        this(lang.getString("textViewer"), null, text, width, height);
    }

/**
     * Creates a new JTextViewer object.
     *
     * @param title  DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param text   DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public JTextViewer(String title, Component parent, String text, int width,
        int height) {
        super(title);
        ta = new JTextArea(text);
        ta.setEditable(false);
        getContentPane().setLayout(new BorderLayout());

        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(width, height));
        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(createToolBar(), BorderLayout.NORTH);
        this.parent = parent;

        if (parent == null) {
            setLocationRelativeTo(getOwner());
        } else {
            setLocationRelativeTo(parent);
        }

        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private JToolBar createToolBar() {
        JToolBar tools = new JToolBar();
        ImageIcon icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Save16.gif"));
        AbstractButton button = new JButton(icon);
        button.setToolTipText(lang.getString("save to file"));
        button.setMnemonic(((Integer) lang.getObject("keySave")).intValue());
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        JFileChooser fc = new JFileChooser();
                        int a = fc.showSaveDialog(JTextViewer.this);

                        if (a != JFileChooser.APPROVE_OPTION) {
                            return;
                        }

                        if (fc.getSelectedFile().exists()) {
                            a = JOptionPane.showConfirmDialog(JTextViewer.this,
                                    fc.getSelectedFile() + "\n" +
                                    lang.getString("file exists; overwrite?"),
                                    lang.getString("overwrite warning"),
                                    JOptionPane.YES_NO_CANCEL_OPTION);

                            if (a != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }

                        new ExtendedFile(fc.getSelectedFile()).writeText(ta.getText(),
                            false);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(JTextViewer.this,
                            ex.getMessage(), ex.getClass().getName(),
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Copy16.gif"));
        button = new JButton(icon);
        button.setToolTipText(lang.getString("copyAll"));
        button.setMnemonic(((Integer) lang.getObject("keyCopy")).intValue());
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    ta.setSelectionStart(0);
                    ta.setSelectionEnd(ta.getText().length());
                    ta.copy();
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Print16.gif"));
        button = new JButton(icon);
        button.setToolTipText(lang.getString("print"));
        button.setMnemonic(((Integer) lang.getObject("keyPrint")).intValue());
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        PrintService ps = JPrinterSelect.showDialog(JTextViewer.this,
                                false);

                        if (ps == null) {
                            return;
                        }

                        PrintUtils.print(ps, ta.getText(), true);
                    } catch (PrintException ex) {
                        JOptionPane.showMessageDialog(JTextViewer.this,
                            ex.getMessage(), ex.getClass().getName(),
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        tools.add(button);
        tools.addSeparator();
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Search16.gif"));
        button = new JButton(icon);
        button.setToolTipText(lang.getString("search"));
        button.setMnemonic(((Integer) lang.getObject("keySearch")).intValue());
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    String pattern = JOptionPane.showInputDialog(JTextViewer.this,
                            lang.getString("search"), searchString);

                    if (pattern == null) {
                        return;
                    }

                    searchString = pattern;

                    int index = ta.getCaretPosition();
                    int found = ta.getText().indexOf(searchString, index);

                    if (found < 0) {
                        JOptionPane.showMessageDialog(JTextViewer.this,
                            "not found", searchString,
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        ta.getCaret().setDot(found);
                        ta.getCaret().moveDot(found + searchString.length());
                        ta.getCaret().setSelectionVisible(true);
                    }
                }
            });
        tools.add(button);
        tools.addSeparator();
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Edit16.gif"));

        final JToggleButton toggleButton = new JToggleButton(icon, false);
        toggleButton.setToolTipText(lang.getString("editable"));
        toggleButton.setMnemonic(((Integer) lang.getObject("keyEditable")).intValue());
        toggleButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ev) {
                    ta.setEditable(toggleButton.isSelected());
                }
            });
        tools.add(toggleButton);
        tools.addSeparator();
        tools.add(new CloseButton(this));

        return tools;
    }
}
