/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.io.ExtendedFile;

import org.jscience.util.Settings;
import org.jscience.util.license.Licensed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Properties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;


/**
 * a convenient graphical editor for Properties
 *
 * @author Holger Antelmann
 */
public class PropertiesEditor extends JMainFrame implements Licensed {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2702088927729584352L;

    /** DOCUMENT ME! */
    ExtendedFile file;

    /** DOCUMENT ME! */
    PropertiesTableModel model;

    /** DOCUMENT ME! */
    JTable table;

/**
     * Creates a new PropertiesEditor object.
     *
     * @param propsFile DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public PropertiesEditor(File propsFile) throws IOException {
        this(new ExtendedFile(propsFile).loadProperties());
        file = new ExtendedFile(propsFile);
        updateStatusText(file.toString());
    }

/**
     * Creates a new PropertiesEditor object.
     *
     * @param props DOCUMENT ME!
     */
    public PropertiesEditor(Properties props) {
        super("PropertiesEditor", true, true);
        Settings.checkLicense(this);
        model = new PropertiesTableModel(props);
        table = new JTable(model);
        table.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    if (ke.getKeyCode() == KeyEvent.VK_INSERT) {
                        addProperty();
                    } else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                        deleteProperty();
                    }
                }
            });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel cmodel = table.getColumnModel();
        cmodel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ev) {
                    if (ev.getValueIsAdjusting()) {
                        return;
                    }

                    table.getColumnModel().getSelectionModel()
                         .setSelectionInterval(1, 1);
                }
            });
        cmodel.getSelectionModel().setSelectionInterval(1, 1);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        //getContentPane().add(new JScrollPane(Menus.makePropertiesTable(props)), BorderLayout.CENTER);
        getContentPane().add(createToolBar(), BorderLayout.NORTH);
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
                    "org/jscience/awt/icons/general/New16.gif"));
        JButton button = new JButton(icon);
        button.setToolTipText("new properties");
        button.setMnemonic(KeyEvent.VK_N);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    int a = JOptionPane.showConfirmDialog(PropertiesEditor.this,
                            "use new properties?");

                    if (a != JOptionPane.OK_OPTION) {
                        return;
                    }

                    model.setProperties(new Properties());

                    //file == null;
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Open16.gif"));
        button = new JButton(icon);
        button.setToolTipText("load file");
        button.setMnemonic(KeyEvent.VK_L);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    JFileChooser jfc = new JFileChooser();

                    if (file != null) {
                        jfc.setSelectedFile(file);
                    }

                    int a = jfc.showOpenDialog(PropertiesEditor.this);

                    if (a != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    file = new ExtendedFile(jfc.getSelectedFile());
                    updateStatusText(file.toString());

                    try {
                        model.setProperties(file.loadProperties());

                        //JOptionPane.showMessageDialog(PropertiesEditor.this, "loaded");
                    } catch (IOException ex) {
                        Menus.showExceptionDialog(PropertiesEditor.this, ex);
                    }
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Save16.gif"));
        button = new JButton(icon);
        button.setToolTipText("save");
        button.setMnemonic(KeyEvent.VK_S);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (file == null) {
                        JFileChooser jfc = new JFileChooser();
                        int a = jfc.showSaveDialog(PropertiesEditor.this);

                        if (a != JFileChooser.APPROVE_OPTION) {
                            return;
                        }

                        file = new ExtendedFile(jfc.getSelectedFile());
                        updateStatusText(file.toString());
                    } else {
                        JOptionPane.showConfirmDialog(PropertiesEditor.this,
                            "save to " + file);
                    }

                    try {
                        model.getProperties()
                             .store(new FileOutputStream(file),
                            "saved by PropertiesEditor");

                        //JOptionPane.showMessageDialog(PropertiesEditor.this, "saved");
                    } catch (IOException ex) {
                        Menus.showExceptionDialog(PropertiesEditor.this, ex);
                    }
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/SaveAs16.gif"));
        button = new JButton(icon);
        button.setToolTipText("save as");
        button.setMnemonic(KeyEvent.VK_A);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    JFileChooser jfc = new JFileChooser();
                    int a = jfc.showSaveDialog(PropertiesEditor.this);

                    if (a != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    file = new ExtendedFile(jfc.getSelectedFile());
                    updateStatusText(file.toString());

                    try {
                        model.getProperties()
                             .store(new FileOutputStream(file),
                            "PropertiesEditor");

                        //JOptionPane.showMessageDialog(PropertiesEditor.this, "saved");
                    } catch (IOException ex) {
                        Menus.showExceptionDialog(PropertiesEditor.this, ex);
                    }
                }
            });
        tools.add(button);
        tools.addSeparator();
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Add16.gif"));
        button = new JButton(icon);
        button.setToolTipText("add property");
        button.setMnemonic(KeyEvent.VK_INSERT);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    addProperty();
                }
            });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource(
                    "org/jscience/awt/icons/general/Delete16.gif"));
        button = new JButton(icon);
        button.setToolTipText("delete property");
        button.setMnemonic(KeyEvent.VK_DELETE);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    deleteProperty();
                }
            });
        tools.add(button);

        return tools;
    }

    /**
     * DOCUMENT ME!
     */
    private void addProperty() {
        String key = JOptionPane.showInputDialog(this, "new property key:");

        if (key == null) {
            return;
        }

        if (model.getProperties().getProperty(key) != null) {
            JOptionPane.showMessageDialog(this, "key already exists: " + key,
                "cannot add key", JOptionPane.WARNING_MESSAGE);

            return;
        }

        model.getProperties().setProperty(key, "");
        model.setProperties(model.getProperties());

        int row = model.getRow(key);
        table.setRowSelectionInterval(row, row);
        table.requestFocus();
    }

    /**
     * DOCUMENT ME!
     */
    private void deleteProperty() {
        int index = table.getSelectedRow();

        if (index < 0) {
            return;
        }

        String key = model.getValueAt(index, 0).toString();
        int a = JOptionPane.showConfirmDialog(this, "delete key: " + key +
                " ?");

        if (a != JOptionPane.YES_OPTION) {
            return;
        }

        model.getProperties().remove(key);
        model.setProperties(model.getProperties());
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            new PropertiesEditor(new File(args[0])).setVisible(true);
        } else {
            new PropertiesEditor(new Properties()).setVisible(true);
        }
    }
}
