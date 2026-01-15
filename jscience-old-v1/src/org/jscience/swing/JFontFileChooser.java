/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.io.ExtensionFileFilter;
import org.jscience.io.FileTreeNode;

import org.jscience.util.Settings;
import org.jscience.util.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


/**
 * provides a nice GUI mechanism to choose a Font from the file system.
 * (with preview and compare option and several other features). In the sample
 * text area, right-mouse-click can be used to invoke a context menu allowing
 * to reset the sample text to the default. JFontFileChooser class uses
 * internationalization with resource bundle
 * <dfn>org.jscience.swing.Language</dfn>.
 *
 * @author Holger Antelmann
 *
 * @see JFontChooser
 * @see FontViewer
 * @see JFontControl
 * @see FontSelectionEvent
 * @see FontSelectionListener
 * @since 3/2/03
 */
public class JFontFileChooser extends JSplitPane {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5187952952730872918L;

    /** DOCUMENT ME! */
    static ExtensionFileFilter fontFilter = new ExtensionFileFilter("ttf",
            "font files");

    /** DOCUMENT ME! */
    public static String DEFAULT_SAMPLE_TEXT = "The quick brown fox jumps over a lazy dog\n" +
        "abcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ\n1234567890 +-*/= " +
        "[]{}()<>\n!@#$%^&*:?\\|/ ������������������\n������" +
        StringUtils.EURO;

    /** DOCUMENT ME! */
    File rootDirectory;

    /** DOCUMENT ME! */
    JTree tree;

    /** DOCUMENT ME! */
    JCheckBox boldBox = new JCheckBox(Menus.language.getString("bold"));

    /** DOCUMENT ME! */
    JCheckBox italicBox = new JCheckBox(Menus.language.getString("italic"));

    /** DOCUMENT ME! */
    JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 200, 1));

    /** DOCUMENT ME! */
    JButton compareButton = new JButton(Menus.language.getString("compare"));

    /** DOCUMENT ME! */
    JTextArea sampleArea = new JTextArea();

    /** DOCUMENT ME! */
    JFileChooser fc = new JFileChooser();

    /** DOCUMENT ME! */
    private boolean okButtonPressed = false;

/**
     * Creates a new JFontFileChooser object.
     *
     * @param rootDirectory DOCUMENT ME!
     */
    public JFontFileChooser(File rootDirectory) {
        this(rootDirectory, null);
    }

/**
     * Creates a new JFontFileChooser object.
     *
     * @param rootDirectory DOCUMENT ME!
     * @param defaultFont   DOCUMENT ME!
     */
    public JFontFileChooser(File rootDirectory, Font defaultFont) {
        super();
        this.rootDirectory = rootDirectory;
        setName(Menus.language.getString("selectFont"));
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(rootDirectory);

        if (rootDirectory == null) {
            this.rootDirectory = chooseDir();
        }

        // laying out components
        JPanel leftPanel = new JPanel(new BorderLayout());
        tree = new JTree(new FileTreeNode(rootDirectory, fontFilter));
        leftPanel.add(tree, BorderLayout.CENTER);

        JPanel toolPanel = new JPanel();
        JButton button = new JButton(new ImageIcon(Settings.getResource(
                        "org/jscience/awt/icons/general/Open16.gif")));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    File dir = chooseDir();

                    if (dir == null) {
                        return;
                    }

                    JFontFileChooser.this.rootDirectory = dir;
                    tree.setModel(new DefaultTreeModel(
                            new FileTreeNode(
                                JFontFileChooser.this.rootDirectory, fontFilter)));
                }
            });
        toolPanel.add(button);
        leftPanel.add(toolPanel, BorderLayout.NORTH);
        setLeftComponent(new JScrollPane(leftPanel));

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
        sizePanel.add(new JLabel(Menus.language.getString("size")));
        sizePanel.add(sizeSpinner);
        sizePanel.add(Box.createVerticalGlue());
        props.add(sizePanel);
        props.add(Box.createHorizontalGlue());
        other.add(props, BorderLayout.NORTH);
        other.add(new JScrollPane(sampleArea), BorderLayout.CENTER);
        setRightComponent(other);

        // final initializations
        compareButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    TreePath[] selected = tree.getSelectionPaths();

                    if ((selected == null) || (selected.length < 1)) {
                        return;
                    }

                    JDialog dialog = Menus.createDialog(JFontFileChooser.this,
                            Menus.language.getString("compare"), true);
                    dialog.getContentPane().setLayout(new BorderLayout());
                    dialog.getContentPane()
                          .add(new CloseButton(dialog), BorderLayout.SOUTH);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    int style = getStyle();
                    int s = ((Integer) sizeSpinner.getValue()).intValue();

                    for (int i = 0; i < selected.length; i++) {
                        JLabel label = new JLabel(getSampleText());
                        Font f = getFontFromFile(((FileTreeNode) selected[i].getLastPathComponent()).getFile());
                        label.setFont(f);
                        label.setToolTipText(f.getFamily() + " (" +
                            selected[i].toString() + ")");
                        panel.add(label);
                    }

                    dialog.getContentPane().add(new JScrollPane(panel));
                    dialog.pack();
                    dialog.setLocationRelativeTo(JFontFileChooser.this);
                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
        tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent ev) {
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
                        JMenuItem item = new JMenuItem(Menus.language.getString(
                                    "reset"));
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
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Font getFontFromFile(File file) {
        return getFontFromFile(file, getStyle(),
            ((Integer) sizeSpinner.getValue()).intValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param style DOCUMENT ME!
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Font getFontFromFile(File file, int style, float size) {
        try {
            return deriveFontFromFile(file, style, size);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);

            return null;
        } catch (FontFormatException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param style DOCUMENT ME!
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws FontFormatException DOCUMENT ME!
     */
    public static Font deriveFontFromFile(File file, int style, float size)
        throws IOException, FontFormatException {
        FileInputStream stream = new FileInputStream(file);
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
        font = font.deriveFont(style, size);
        stream.close();

        return font;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getSelectedFont() {
        TreePath path = tree.getSelectionPath();

        if (path == null) {
            return null;
        }

        return getFontFromFile(((FileTreeNode) path.getLastPathComponent()).getFile());
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setSelectedFont(Font font) {
        if (font == null) {
            tree.clearSelection();
            sizeSpinner.getModel().setValue(new Integer(12));
            boldBox.setSelected(false);
            italicBox.setSelected(false);
        } else {
            tree.clearSelection();
            sizeSpinner.getModel().setValue(new Integer(font.getSize()));
            boldBox.setSelected(font.isBold());
            italicBox.setSelected(font.isItalic());
        }

        sampleArea.setFont(font);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    File chooseDir() {
        int n = fc.showOpenDialog(this);

        if (n == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }

        return null;
    }

    /**
     * pops up a font chooser dialog and returns the selected font or
     * null
     *
     * @param rootDirectory DOCUMENT ME!
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Font showDialog(File rootDirectory, Component parent) {
        final Font returnValue = null;
        final JFontFileChooser fc = new JFontFileChooser(rootDirectory,
                (parent == null) ? null : parent.getFont());
        final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(
                    parent), Menus.language.getString("selectFont"), true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fc, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton okButton = new JButton(Menus.language.getString("button_ok"));
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    fc.okButtonPressed = true;
                    dialog.dispose();
                }
            });
        bottom.add(okButton);

        JButton cancelButton = new JButton(Menus.language.getString(
                    "button_cancel"));
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
