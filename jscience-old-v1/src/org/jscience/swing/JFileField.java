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
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import java.io.File;
import java.io.IOException;

import java.util.List;

import javax.swing.*;


/**
 * A text component that denotes a file path; supports mouse drops from
 * file system and provides a convenient JFileChooser.
 *
 * @author Holger Antelmann
 *
 * @see javax.swing.JFileChooser
 */
public class JFileField extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -3765985417186188911L;

    /** DOCUMENT ME! */
    JFileChooser chooser;

    /** DOCUMENT ME! */
    JTextField textField;

    /** DOCUMENT ME! */
    JButton button;

    /** DOCUMENT ME! */
    boolean dirty = true;

/**
     * Creates a new JFileField object.
     */
    public JFileField() {
        this(null, 0);
    }

/**
     * Creates a new JFileField object.
     *
     * @param columns DOCUMENT ME!
     */
    public JFileField(int columns) {
        this(null, columns);
    }

/**
     * Creates a new JFileField object.
     *
     * @param initialFile DOCUMENT ME!
     */
    public JFileField(File initialFile) {
        this(initialFile, 0);
    }

/**
     * Creates a new JFileField object.
     *
     * @param initialFile DOCUMENT ME!
     * @param columns     DOCUMENT ME!
     */
    public JFileField(File initialFile, int columns) {
        chooser = new JFileChooser(initialFile);
        chooser.setMultiSelectionEnabled(false);
        textField = new JTextField(columns);
        textField.setTransferHandler(new MyTransferHandler(
                textField.getTransferHandler()));

        /*
        textField.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ev) { check(); }
        });
        textField.addFocusListener(new FocusListener() {
            public void focusGained (FocusEvent ev) {}
            public void focusLost (FocusEvent ev) {
                check();
            }
        });
        */

        /*
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate (DocumentEvent ev) { check(); }
            public void insertUpdate (DocumentEvent ev) { check(); }
            public void removeUpdate (DocumentEvent ev) { check(); }
        });
        */
        ImageIcon icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Open16.gif"));
        button = new JButton(icon);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    check();

                    int a = chooser.showOpenDialog(JFileField.this);

                    if (a == JFileChooser.APPROVE_OPTION) {
                        setFile(chooser.getSelectedFile());
                    }
                }
            });
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        setFile(initialFile);
    }

    /**
     * DOCUMENT ME!
     */
    private void check() {
        //System.out.println("*** debug line");
        if ((textField.getText() == null) ||
                (textField.getText().length() < 1)) {
            setFile(null);
        } else {
            setFile(new File(textField.getText()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setEnabled(boolean flag) {
        textField.setEnabled(flag);
        button.setEnabled(flag);
        super.setEnabled(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mode the type of files to be chosen:
     *        <ul><li>JFileChooser.FILES_ONLY
     *        <li>JFileChooser.DIRECTORIES_ONLY
     *        <li>JFileChooser.FILES_AND_DIRECTORIES    </ul>
     *
     * @see JFileChooser
     */
    public void setFileSelectionMode(int mode) {
        chooser.setFileSelectionMode(mode);
    }

    /**
     * DOCUMENT ME!
     *
     * @return the type of files to be chosen: <ul><li>JFileChooser.FILES_ONLY
     *         <li>JFileChooser.DIRECTORIES_ONLY
     *         <li>JFileChooser.FILES_AND_DIRECTORIES    </ul>
     *
     * @see JFileChooser
     */
    public int getFileSelectionMode() {
        return chooser.getFileSelectionMode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void addChoosableFileFilter(
        javax.swing.filechooser.FileFilter filter) {
        chooser.addChoosableFileFilter(filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void setFileFilter(javax.swing.filechooser.FileFilter filter) {
        chooser.setFileFilter(filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return chooser.getFileFilter();
    }

    /**
     * returns the file or null if the field is null or empty
     *
     * @return DOCUMENT ME!
     */
    public File getFile() {
        check();

        return chooser.getSelectedFile();
    }

    /**
     * returns true only if the file was accepted by the filter and
     * hence the file was actually set
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean setFile(File file) {
        if (!chooser.getFileFilter().accept(file)) {
            return false;
        }

        chooser.setSelectedFile(file);

        if (file == null) {
            textField.setText(null);
        } else {
            textField.setText(file.getPath());
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class MyTransferHandler extends TransferHandler {
        /** DOCUMENT ME! */
        static final long serialVersionUID = 6082855520319956846L;

        /** DOCUMENT ME! */
        TransferHandler handler;

/**
         * Creates a new MyTransferHandler object.
         *
         * @param handler DOCUMENT ME!
         */
        public MyTransferHandler(TransferHandler handler) {
            this.handler = handler;
        }

        /**
         * DOCUMENT ME!
         *
         * @param comp DOCUMENT ME!
         * @param df DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean canImport(JComponent comp, DataFlavor[] df) {
            if (!textField.isEnabled()) {
                return false;
            }

            for (int i = 0; i < df.length; i++) {
                if (df[i].equals(DataFlavor.javaFileListFlavor)) {
                    return true;
                }
            }

            return handler.canImport(comp, df);
        }

        //protected Transferable createTransferable (JComponent c) { return handler.createTransferable(c); }
        /**
         * DOCUMENT ME!
         *
         * @param comp DOCUMENT ME!
         * @param e DOCUMENT ME!
         * @param action DOCUMENT ME!
         */
        public void exportAsDrag(JComponent comp, InputEvent e, int action) {
            handler.exportAsDrag(comp, e, action);
        }

        //protected void exportDone (JComponent source, Transferable data, int action) { handler.exportDone(source, data, action); }
        /**
         * DOCUMENT ME!
         *
         * @param comp DOCUMENT ME!
         * @param clip DOCUMENT ME!
         * @param action DOCUMENT ME!
         */
        public void exportToClipboard(JComponent comp, Clipboard clip,
            int action) {
            handler.exportToClipboard(comp, clip, action);
        }

        /**
         * DOCUMENT ME!
         *
         * @param c DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getSourceActions(JComponent c) {
            return handler.getSourceActions(c);
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Icon getVisualRepresentation(Transferable t) {
            return handler.getVisualRepresentation(t);
        }

        /**
         * DOCUMENT ME!
         *
         * @param comp DOCUMENT ME!
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean importData(JComponent comp, Transferable t) {
            if (!textField.isEnabled()) {
                return false;
            }

            DataFlavor[] df = t.getTransferDataFlavors();

            try {
                for (int i = 0; i < df.length; i++) {
                    if (df[i].equals(DataFlavor.javaFileListFlavor)) {
                        List l = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

                        if (l.size() > 0) {
                            setFile((File) l.get(0));
                        }

                        return true;
                    }

                    boolean flag = handler.importData(comp, t);

                    if (flag) {
                        check();
                    }

                    return flag;
                }
            } catch (UnsupportedFlavorException ex) {
            } catch (IOException ex) {
            }

            return false;
        }
    }
}
