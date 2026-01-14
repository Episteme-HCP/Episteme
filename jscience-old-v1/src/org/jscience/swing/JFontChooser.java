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

import org.jscience.util.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * provides a nice GUI mechanism to choose a System Font (with preview and
 * compare option and several other features). In the sample text area,
 * right-mouse-click can be used to invoke a context menu allowing to reset
 * the sample text to the default. JFontChooser class uses
 * internationalization with resource bundle
 * <dfn>org.jscience.swing.Language</dfn>.
 *
 * @author Holger Antelmann
 *
 * @see JFontFileChooser
 * @see FontViewer
 * @see JFontControl
 * @see FontSelectionEvent
 * @see FontSelectionListener
 * @since 3/2/03
 */
public class JFontChooser extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 735227717250207090L;

    /** DOCUMENT ME! */
    public static String DEFAULT_SAMPLE_TEXT = "The quick brown fox jumps over a lazy dog\n" +
        "abcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ\n1234567890 +-*/= " +
        "[]{}()<>\n!@#$%^&*:?\\|/ ������������������\n������" +
        StringUtils.EURO;

    /** DOCUMENT ME! */
    static ResourceBundle lang = Menus.language;

    /** DOCUMENT ME! */
    static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    /** DOCUMENT ME! */
    JList familyList = new JList(ge.getAvailableFontFamilyNames());

    /** DOCUMENT ME! */
    JCheckBox boldBox = new JCheckBox(lang.getString("bold"));

    /** DOCUMENT ME! */
    JCheckBox italicBox = new JCheckBox(lang.getString("italic"));

    /** DOCUMENT ME! */
    JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 200, 1));

    /** DOCUMENT ME! */
    JButton compareButton = new JButton(lang.getString("compare"));

    //JButton    propertyButton = new JButton(lang.getString("properties"));
    /** DOCUMENT ME! */
    JTextArea sampleArea = new JTextArea();

    /** DOCUMENT ME! */
    private boolean okButtonPressed = false;

/**
     * Creates a new JFontChooser object.
     */
    public JFontChooser() {
        this(null);
    }

/**
     * Creates a new JFontChooser object.
     *
     * @param defaultFont DOCUMENT ME!
     */
    public JFontChooser(Font defaultFont) {
        setName(lang.getString("selectFont"));

        // laying out components
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setLayout(new BorderLayout());
        add(new JScrollPane(familyList), BorderLayout.WEST);

        JPanel other = new JPanel(new BorderLayout());
        JPanel props = new JPanel();
        props.setLayout(new BoxLayout(props, BoxLayout.X_AXIS));
        props.add(compareButton);
        props.add(Box.createHorizontalGlue());

        JPanel stylePanel = new JPanel();
        stylePanel.setLayout(new BoxLayout(stylePanel, BoxLayout.Y_AXIS));
        stylePanel.add(boldBox);
        stylePanel.add(italicBox);
        props.add(stylePanel);

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.add(new JLabel(lang.getString("size")));
        sizePanel.add(sizeSpinner);
        sizePanel.add(Box.createVerticalGlue());
        props.add(sizePanel);
        props.add(Box.createHorizontalGlue());

        //props.add(propertyButton);
        other.add(props, BorderLayout.NORTH);
        other.add(new JScrollPane(sampleArea), BorderLayout.CENTER);
        add(other, BorderLayout.CENTER);

        // final initializations

        /*
        propertyButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ev) {
                Menus.showPropertiesDialog(lang.getString("properties"),
                    getSelectedFont().getAttributes(), JFontChooser.this, true);
            }
        });
        */
        compareButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    Object[] selected = familyList.getSelectedValues();

                    if ((selected == null) || (selected.length < 1)) {
                        return;
                    }

                    JDialog dialog = Menus.createDialog(JFontChooser.this,
                            lang.getString("compare"), true);
                    dialog.getContentPane().setLayout(new BorderLayout());
                    dialog.getContentPane()
                          .add(new CloseButton(dialog), BorderLayout.SOUTH);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    int style = getStyle();
                    int s = ((Integer) sizeSpinner.getValue()).intValue();

                    for (int i = 0; i < selected.length; i++) {
                        JLabel label = new JLabel(getSampleText());
                        label.setFont(new Font(selected[i].toString(), style, s));
                        label.setToolTipText(selected[i].toString());
                        panel.add(label);
                    }

                    dialog.getContentPane().add(new JScrollPane(panel));
                    dialog.pack();
                    dialog.setLocationRelativeTo(JFontChooser.this);
                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
        familyList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ev) {
                    Font font = getSelectedFont();
                    sampleArea.setFont(font);
                    notifyListeners(font);
                }
            });
        sampleArea.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    popup(e);
                }

                public void mouseReleased(MouseEvent e) {
                    popup(e);
                }

                void popup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem item = new JMenuItem(lang.getString("reset"));
                        item.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    setSampleText(DEFAULT_SAMPLE_TEXT);
                                }
                            });
                        menu.add(item);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

        ChangeListener changeListener = new ChangeListener() {
                public void stateChanged(ChangeEvent ev) {
                    Font font = getSelectedFont();
                    sampleArea.setFont(font);
                    notifyListeners(font);
                }
            };

        boldBox.addChangeListener(changeListener);
        italicBox.addChangeListener(changeListener);
        sizeSpinner.addChangeListener(changeListener);
        setSampleText(DEFAULT_SAMPLE_TEXT);
        setSelectedFont((defaultFont == null) ? getFont() : defaultFont);

        //familyList.getSelectionModel().setSelectionMode(
        //    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addFontSelectionListener(FontSelectionListener listener) {
        listenerList.add(FontSelectionListener.class, listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void removeFontSelectionListener(FontSelectionListener listener) {
        listenerList.remove(FontSelectionListener.class, listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FontSelectionListener[] getFontSelectionListeners() {
        return (FontSelectionListener[]) listenerList.getListeners(FontSelectionListener.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    void notifyListeners(Font font) {
        FontSelectionListener[] listener = getFontSelectionListeners();

        for (int i = 0; i < listener.length; i++) {
            listener[i].fontSelectionChanged(new FontSelectionEvent(this, font));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setSampleText(String text) {
        sampleArea.setText(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSampleText() {
        return sampleArea.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getStyle() {
        int style = Font.PLAIN;

        if (boldBox.isSelected()) {
            if (italicBox.isSelected()) {
                style = Font.BOLD | Font.ITALIC;
            } else {
                style = Font.BOLD;
            }
        } else {
            if (italicBox.isSelected()) {
                style = Font.ITALIC;
            } else {
                style = Font.PLAIN;
            }
        }

        return style;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getSelectedFont() {
        return new Font((String) familyList.getSelectedValue(), getStyle(),
            ((Integer) sizeSpinner.getValue()).intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setSelectedFont(Font font) {
        if (font == null) {
            familyList.setSelectedValue(null, true);
            sizeSpinner.getModel().setValue(new Integer(12));
            boldBox.setSelected(false);
            italicBox.setSelected(false);
        } else {
            familyList.setSelectedValue(font.getFamily(), true);
            sizeSpinner.getModel().setValue(new Integer(font.getSize()));
            boldBox.setSelected(font.isBold());
            italicBox.setSelected(font.isItalic());
        }

        sampleArea.setFont(font);
    }

    /**
     * pops up a font chooser dialog and returns the selected font or
     * null. If the defaultFont is null, the font of the parent is used; ff
     * then the parent is null, too, no default font is used.
     *
     * @param parent DOCUMENT ME!
     * @param defaultFont DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Font showDialog(Component parent, Font defaultFont) {
        final Font returnValue = null;
        final JFontChooser fc = new JFontChooser((defaultFont == null)
                ? ((parent == null) ? null : parent.getFont()) : defaultFont);
        final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(
                    parent), lang.getString("selectFont"), true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fc, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton okButton = new JButton(lang.getString("button_ok"));
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    fc.okButtonPressed = true;
                    dialog.dispose();
                }
            });
        bottom.add(okButton);

        JButton cancelButton = new JButton(lang.getString("button_cancel"));
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    dialog.dispose();
                }
            });
        bottom.add(cancelButton);
        panel.add(bottom, BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        dialog.dispose();

        return fc.okButtonPressed ? fc.getSelectedFont() : null;
    }
}
