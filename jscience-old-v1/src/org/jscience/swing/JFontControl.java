/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.Settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * provides a Font control component that can be used in a menu or toolbar.
 * Uses internationalization with resource bundle
 * <dfn>org.jscience.swing.Language</dfn>
 *
 * @author Holger Antelmann
 *
 * @see JFontChooser
 * @since 3/3/03
 */
public class JFontControl extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -948104583493181500L;

    /** DOCUMENT ME! */
    static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    /** DOCUMENT ME! */
    JComboBox familyControl = new JComboBox(ge.getAvailableFontFamilyNames());

    /** DOCUMENT ME! */
    JSpinner sizeControl = new JSpinner(new SpinnerNumberModel(1, 0, 200, 1));

    /** DOCUMENT ME! */
    JToggleButton boldControl = new JToggleButton(new ImageIcon(
                Settings.getResource("org/jscience/awt/icons/text/Bold16.gif")));

    /** DOCUMENT ME! */
    JToggleButton italicControl = new JToggleButton(new ImageIcon(
                Settings.getResource("org/jscience/awt/icons/text/Italic16.gif")));

/**
     * Creates a new JFontControl object.
     *
     * @param initialFont DOCUMENT ME!
     * @param listener    DOCUMENT ME!
     */
    public JFontControl(Font initialFont, final FontSelectionListener listener) {
        listenerList.add(FontSelectionListener.class, listener);

        // laying out components
        Insets insets = new Insets(1, 1, 1, 1);
        boldControl.setMargin(insets);
        italicControl.setMargin(insets);
        setLayout(new FlowLayout());
        add(familyControl);
        add(sizeControl);
        add(boldControl);
        add(italicControl);
        add(Box.createHorizontalGlue());

        // initializations
        sizeControl.setToolTipText(Menus.language.getString("size"));
        boldControl.setToolTipText(Menus.language.getString("bold"));
        italicControl.setToolTipText(Menus.language.getString("italic"));

        ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    listener.fontSelectionChanged(new FontSelectionEvent(this,
                            getSelectedFont()));
                }
            };

        familyControl.addActionListener(actionListener);
        boldControl.addActionListener(actionListener);
        italicControl.addActionListener(actionListener);
        sizeControl.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ev) {
                    listener.fontSelectionChanged(new FontSelectionEvent(this,
                            getSelectedFont()));
                }
            });
        setSelectedFont((initialFont == null) ? getFont() : initialFont);
    }

    /**
     * DOCUMENT ME!
     *
     * @param enabled DOCUMENT ME!
     */
    public void setEnabled(boolean enabled) {
        familyControl.setEnabled(enabled);
        sizeControl.setEnabled(enabled);
        boldControl.setEnabled(enabled);
        italicControl.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getStyle() {
        int style = Font.PLAIN;

        if (boldControl.isSelected()) {
            if (italicControl.isSelected()) {
                style = Font.BOLD | Font.ITALIC;
            } else {
                style = Font.BOLD;
            }
        } else {
            if (italicControl.isSelected()) {
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
        return new Font((String) familyControl.getSelectedItem(), getStyle(),
            ((Integer) sizeControl.getValue()).intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setSelectedFont(Font font) {
        if (font == null) {
            familyControl.setSelectedItem(null);
            sizeControl.getModel().setValue(new Integer(12));
            boldControl.setSelected(false);
            italicControl.setSelected(false);
        } else {
            familyControl.setSelectedItem(font.getFamily());
            sizeControl.getModel().setValue(new Integer(font.getSize()));
            boldControl.setSelected(font.isBold());
            italicControl.setSelected(font.isItalic());
        }
    }

    /**
     * demo application
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        final JTextPane textPane = new JTextPane();
        JMainFrame frame = new JMainFrame(new JScrollPane(textPane));
        FontSelectionListener l = new FontSelectionListener() {
                public void fontSelectionChanged(FontSelectionEvent ev) {
                    textPane.setFont(ev.getFont());
                }
            };

        JToolBar toolbar = new JToolBar();
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

        JFontControl control = new JFontControl(textPane.getFont(), l);
        toolbar.add(control);
        frame.pack();
        frame.setVisible(true);
    }
}
