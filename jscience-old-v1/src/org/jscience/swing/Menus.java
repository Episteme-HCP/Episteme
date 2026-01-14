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

import org.jscience.JScienceVersion;
import org.jscience.net.JDownloader;
import org.jscience.net.Spider;
import org.jscience.util.Debug;
import org.jscience.util.ResourceNotFoundException;
import org.jscience.util.Settings;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;


/**
 * Menus provides some convenient menus and dialogs that are potentially
 * useful for any Java Swing Application. If available, it takes advantage of
 * <dfn>com.incors.plaf.kunststoff.KunststoffLookAndFeel</dfn>.
 *
 * @author Holger Antelmann
 */
public final class Menus {
    /**
     * DOCUMENT ME!
     */
    public static final ImageIcon antelmannIcon = new ImageIcon(Settings.getResource(
            "org/jscience/jscienceicon.png"));

    /**
     * DOCUMENT ME!
     */
    public static final String aboutString = new String(
            "<html>(c) 2006 JScience Consortium<br>contact@jscience.org<br>http://www.jscience.org/");

    /**
     * DOCUMENT ME!
     */
    public static boolean verbose = false;

    /**
     * allows access to the localized language used in this package
     */
    public static final ResourceBundle language = ResourceBundle.getBundle(
            "org.jscience.swing.Language");

    static {
        try {
            // special feature class since J2SE 1.4.2
            String gtkClass = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            Class.forName(gtkClass);
            UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo("GTK",
                    gtkClass));
        } catch (LinkageError ignore) {
        } catch (ClassNotFoundException ignore) {
        }

        try {
            // custom class
            String kunststoffClass = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";
            Class.forName(kunststoffClass);
            UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(
                    "Kunststoff", kunststoffClass));
        } catch (LinkageError ignore) {
        } catch (ClassNotFoundException ignore) {
        }
    }

    /**
     * returns a convenient standard JMenuBar to be used with any Frame
     *
     * @param parent DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static JMenuBar createDefaultMenuBar(final Frame parent) {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item;
        JMenu fileMenu = new JMenu(language.getString("menu_file"));
        fileMenu.setMnemonic(((Integer) language.getObject("menu_file key")).intValue());
        menuBar.add(fileMenu);
        item = new JMenuItem(language.getString("menu_file_close"));
        item.setMnemonic(((Integer) language.getObject("menu_file_close key")).intValue());
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });
        fileMenu.add(item);
        item = new JMenuItem(language.getString("menu_file_exit"));
        item.setMnemonic(((Integer) language.getObject("menu_file_exit key")).intValue());
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(item);

        JMenu winMenu = new JMenu(language.getString("menu_window"));
        winMenu.setMnemonic(((Integer) language.getObject("menu_window key")).intValue());
        menuBar.add(winMenu);
        winMenu.add(createLookAndFeelMenu(parent));

        JMenu helpMenu = new JMenu(language.getString("menu_help"));
        helpMenu.setMnemonic(((Integer) language.getObject("menu_help key")).intValue());
        helpMenu.add(createAboutBoxMenuItem(parent));
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * createLookAndFeelMenu() generates a JMenu that lets the user
     * select from different look&feel options.
     *
     * @param rootComponent the top component of the GUI tree that needs to be
     *                      updated
     * @return DOCUMENT ME!
     * @throws Error DOCUMENT ME!
     */
    public static JMenu createLookAndFeelMenu(final Frame rootComponent) {
        JMenu looksMenu = new JMenu(language.getString("change_look"));

        // customized themes
        final JMenu themesMenu = new JMenu("Themes");
        final DefaultMetalTheme[] themes = CustomColorTheme.getThemes();
        final JRadioButtonMenuItem[] tItem = new JRadioButtonMenuItem[themes.length];

        for (int i = 0; i < themes.length; i++) {
            tItem[i] = new JRadioButtonMenuItem(themes[i].getName());
            themesMenu.add(tItem[i]);

            final DefaultMetalTheme t = themes[i];
            tItem[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    MetalLookAndFeel mlaf = (MetalLookAndFeel) UIManager.getLookAndFeel();
                    mlaf.setCurrentTheme(t);

                    try {
                        UIManager.setLookAndFeel(mlaf);
                    } catch (UnsupportedLookAndFeelException cannothappen) {
                        throw new Error();
                    }

                    SwingUtilities.updateComponentTreeUI(rootComponent);

                    //rootComponent.pack();
                    for (int n = 0; n < themes.length; n++) {
                        tItem[n].setSelected(t.getName()
                                .equals(tItem[n].getText()));
                    }

                    if (verbose) {
                        String s = "[org.jscience.utils.gui.Menus message] Look&Feel theme set to " +
                                t.getName();
                        System.out.println(s);
                    }
                }
            });
        }

        tItem[0].setSelected(true);

        // standard look&feels
        final UIManager.LookAndFeelInfo[] uis = UIManager.getInstalledLookAndFeels();
        final JRadioButtonMenuItem[] uiItem = new JRadioButtonMenuItem[uis.length];
        String current = UIManager.getLookAndFeel().getName();

        for (int i = 0; i < uis.length; i++) {
            uiItem[i] = new JRadioButtonMenuItem(uis[i].getName());

            final String uiclass = uis[i].getClassName();
            final String uiname = uis[i].getName();
            uiItem[i].setSelected(uiItem[i].getText().equals(current));
            uiItem[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(uiclass);
                        SwingUtilities.updateComponentTreeUI(rootComponent);

                        String current = UIManager.getLookAndFeel().getName();

                        for (int n = 0; n < uis.length; n++) {
                            uiItem[n].setSelected(uiItem[n].getText()
                                    .equals(current));
                        }

                        if (verbose) {
                            String s = "[org.jscience.utils.gui.Menus message] Look&Feel changed to " +
                                    uiname;
                            System.out.println(s);
                        }

                        themesMenu.setEnabled(UIManager.getLookAndFeel() instanceof MetalLookAndFeel);
                    } catch (Exception ex) {
                        String s = "Error in org.jscience.utils.gui.Menus: cannot change Look&Feel to " +
                                uiname;
                        System.err.println(s);
                        ex.printStackTrace();
                    }
                }
            });
            themesMenu.setEnabled(UIManager.getLookAndFeel() instanceof MetalLookAndFeel);
            looksMenu.add(uiItem[i]);
            looksMenu.add(themesMenu);
        }

        return looksMenu;
    }

    /**
     * returns an 'about' menu capable of displaying an Antelmann.com's
     * info box.
     *
     * @param parent the component over which the modal dialog is to be placed
     * @return DOCUMENT ME!
     */
    public static JMenuItem createAboutDialogMenuItem(final Frame parent) {
        ImageIcon icon = new ImageIcon(Settings.getResource(
                "org/jscience/awt/icons/general/About16.gif"));
        JMenuItem aboutMenu = new JMenuItem(language.getString("menu_about"),
                icon);
        aboutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (verbose) {
                    System.out.println(
                            "[Message from org.jscience.utils.gui.Menus] displaying 'About' box");
                }

                showAboutDialog(parent);
            }
        });

        return aboutMenu;
    }

    /**
     * returns an 'about' menu capable of displaying an Antelmann.com's
     * info box.
     *
     * @param parent the component over which the modal dialog is to be placed
     * @return DOCUMENT ME!
     */
    public static JMenuItem createAboutBoxMenuItem(final Frame parent) {
        ImageIcon icon = new ImageIcon(Settings.getResource(
                "org/jscience/awt/icons/general/About16.gif"));
        JMenuItem aboutMenu = new JMenuItem(language.getString("menu_about"),
                icon);
        aboutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (verbose) {
                    System.out.println(
                            "[Message from org.jscience.utils.gui.Menus] displaying 'About' box");
                }

                showAboutBox(parent);
            }
        });

        return aboutMenu;
    }

    /**
     * shows a small antelmann.com about dialog
     *
     * @param parent DOCUMENT ME!
     */
    public static void showAboutBox(Component parent) {
        JOptionPane.showMessageDialog(parent,
                aboutString + "<br>Version: " +
                        JScienceVersion.getCurrent().toString(),
                language.getString("about"), JOptionPane.INFORMATION_MESSAGE,
                antelmannIcon);
    }

    /**
     * shows the standard antelmann.com about dialog with license,
     * version and upgrade button
     *
     * @param parent DOCUMENT ME!
     */
    public static void showAboutDialog(final Component parent) {
        final JDialog dialog = createDialog(parent, "JScience", true);
        JPanel box = new JPanel();
        box.add(new JLabel(antelmannIcon));
        box.add(new JLabel(aboutString));

        JPanel stuff = new JPanel();
        JButton vbutton = new JButton(language.getString("version"));
        final Component component = parent;
        vbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showVersionDialog(component);
            }
        });
        stuff.add(vbutton);

        JButton lbutton = new JButton(language.getString("license"));
        lbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLicenseDialog(component);
            }
        });
        stuff.add(lbutton);
        stuff.add(vbutton);

        JButton ubutton = new JButton(language.getString("upgrade_check"));
        ubutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (JScienceVersion.getLatest()
                            .isLater(JScienceVersion.getCurrent())) {
                        int a = JOptionPane.showConfirmDialog(dialog,
                                language.getString(
                                        "upgrade_available_question"),
                                language.getString(
                                        "upgrade_available_title"),
                                JOptionPane.YES_NO_CANCEL_OPTION);

                        if (a == JOptionPane.CANCEL_OPTION) {
                            return;
                        }

                        if (a == JOptionPane.YES_OPTION) {
                            JFileChooser fc = new JFileChooser();
                            fc.setSelectedFile(new File(
                                    fc.getCurrentDirectory(), "jscience.jar"));

                            int af = fc.showSaveDialog(dialog);

                            if (af == JFileChooser.APPROVE_OPTION) {
                                File file = fc.getSelectedFile();

                                try {
                                    URL url = new URL(Settings.getProperty(
                                            "application.vendor.jarfile.url"));
                                    new JDownloader(parent, url, file).start();

                                    return;
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(dialog,
                                            ex.getMessage(), "URL problem",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }

                        JOptionPane.showMessageDialog(dialog,
                                language.getString(
                                        "upgrade_available_later_from") +
                                        Settings.getProperty("application.vendor.url"),
                                language.getString("upgrade_available_title"),
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                language.getString("upgrade_not"),
                                language.getString("upgrade_not_title"),
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            language.getString("connect_failure"),
                            language.getString("connect_failure_title"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        stuff.add(ubutton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(box, BorderLayout.NORTH);
        panel.add(stuff);
        panel.add(new CloseButton(dialog), BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public static void showVersionDialog(Component parent) {
        if (parent == null) {
            parent = new JMainFrame();
        }

        JTextArea version = new JTextArea(JScienceVersion.getCurrent().toString());
        version.setEditable(false);
        version.setCaretPosition(0);

        JDialog dialog = createDialog(parent, "JScience Version info", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(version));
        panel.add(new CloseButton(dialog), BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public static void showLicenseDialog(Component parent) {
        if (parent == null) {
            parent = new JMainFrame();
        }

        String content = null;

        try {
            content = new Spider(Settings.getResource(Settings.getProperty(
                    "application.vendor.license"))).getContentAsString();
        } catch (IOException e) {
            throw new Error();
        }

        JTextArea license = new JTextArea(content);
        license.setEditable(false);
        license.setCaretPosition(0);

        JDialog dialog = createDialog(parent, "JScience License", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 300));
        panel.add(new JScrollPane(license));
        panel.add(new CloseButton(dialog), BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * shows a JDialog containing a JTable to display the given
     * properties sorted
     *
     * @param title      DOCUMENT ME!
     * @param properties DOCUMENT ME!
     * @param parent     DOCUMENT ME!
     * @param modal      DOCUMENT ME!
     * @see #makePropertiesTable(Map,String,String)
     * @deprecated use <code>makePropertiesTable</code> with
     *             <code>JOptionPane</code> instead
     */
    @Deprecated
    public static void showPropertiesDialog(String title, Map properties,
                                            Component parent, boolean modal) {
        if (parent == null) {
            parent = new JMainFrame();
        }

        JDialog dialog = createDialog(parent, title, true);
        JTable table = makePropertiesTable(properties, "key", "header");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 300));
        panel.add(new JScrollPane(table));
        panel.add(new CloseButton(dialog), BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * calls <code>makePropertiesTable(properties, "key",
     * "value")</code>
     *
     * @param properties DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static JTable makePropertiesTable(Map properties) {
        return makePropertiesTable(properties, "key", "value");
    }

    /**
     * returns a table that orders the entries by the keys of the
     * properties
     *
     * @param properties  DOCUMENT ME!
     * @param keyHeader   DOCUMENT ME!
     * @param valueHeader DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static JTable makePropertiesTable(Map properties, String keyHeader,
                                             String valueHeader) {
        Vector<String> header = new Vector<String>();
        header.add(keyHeader);
        header.add(valueHeader);

        Vector<Vector> values = new Vector<Vector>();
        Iterator e = properties.keySet().iterator();

        while (e.hasNext()) {
            Vector<Object> v = new Vector<Object>(2);
            v.add(e.next());
            v.add(properties.get(v.get(0)));

            // ensure a sorted order
            int i = 0;

            while ((i < values.size()) &&
                    ((((Vector) values.get(i)).get(0)).toString()
                            .compareTo(v.get(0).toString()) < 0))
                i++;

            values.add(i, v);
        }

        DefaultTableModel model = new DefaultTableModel(values, header);

        return new JTable(model);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ex DOCUMENT ME!
     */
    public static void showExceptionDialog(Throwable ex) {
        showExceptionDialog(null, ex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param ex     DOCUMENT ME!
     */
    public static void showExceptionDialog(Component parent, Throwable ex) {
        showExceptionDialog(parent, ex, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param ex     DOCUMENT ME!
     * @param t      DOCUMENT ME!
     */
    public static void showExceptionDialog(final Component parent,
                                           final Throwable ex, final Thread t) {
        JPanel panel = new JPanel();
        JPanel tiny = new JPanel();
        tiny.setLayout(new BoxLayout(tiny, BoxLayout.Y_AXIS));
        tiny.add(new JLabel(ex.getClass().getName()));

        JTextField field = new JTextField(ex.getMessage(), 15);
        field.setEditable(false);
        tiny.add(field);
        panel.add(tiny);

        JButton button = new JButton("details");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JTextArea ta = new JTextArea(Debug.stackTraceAsString(ex, t));
                ta.setEditable(false);
                ta.setCaretPosition(0);

                JDialog dialog = createDialog(parent,
                        ex.getClass().getName(), true);
                JPanel panel = new JPanel(new BorderLayout());
                panel.setPreferredSize(new Dimension(400, 200));
                panel.add(new JScrollPane(ta));
                panel.add(new CloseButton(dialog), BorderLayout.SOUTH);
                dialog.getContentPane().add(panel);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        panel.add(button);
        //SoundPlayer.beep();
        JOptionPane.showMessageDialog(parent, panel, "Exception encountered",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static char[] enterPassword(Component parent) {
        return enterPassword(parent, language.getString("password required"),
                language.getString("enter password"), null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent       DOCUMENT ME!
     * @param title        DOCUMENT ME!
     * @param message      DOCUMENT ME!
     * @param initialValue DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static char[] enterPassword(Component parent, String title,
                                       Object message, String initialValue) {
        return enterPassword(parent, title, message, initialValue,
                new ImageIcon(Settings.getResource("org/jscience/util/gui/halt.gif")));
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent       DOCUMENT ME!
     * @param title        DOCUMENT ME!
     * @param message      DOCUMENT ME!
     * @param initialValue DOCUMENT ME!
     * @param icon         DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static char[] enterPassword(Component parent, String title,
                                       Object message, String initialValue, ImageIcon icon) {
        final JDialog dialog = createDialog(parent, title, true);
        dialog.getContentPane().setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        if (message != null) {
            JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            if (message instanceof Component) {
                msgPanel.add((Component) message);
            } else {
                msgPanel.add(new JLabel(message.toString()));
            }

            mainPanel.add(msgPanel);
        }

        final JPasswordField pwd = new JPasswordField(initialValue, 20);
        pwd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dialog.dispose();
            }
        });
        pwd.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ev) {
                if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pwd.setText(null);
                    dialog.dispose();
                }
            }
        });

        JPanel pwdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pwdPanel.add(pwd);
        mainPanel.add(pwdPanel);

        JPanel buttonPanel = new JPanel();
        JButton OkButton = new JButton(language.getString("button_ok"));
        OkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dialog.dispose();
            }
        });
        buttonPanel.add(OkButton);

        JButton CancelButton = new JButton(language.getString("button_cancel"));
        CancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dialog.dispose();
                pwd.setText(null);
            }
        });
        buttonPanel.add(CancelButton);
        mainPanel.add(buttonPanel);
        dialog.getContentPane().add(mainPanel, BorderLayout.CENTER);

        try {
            // works also if this particular icon is not found - not a big deal
            dialog.getContentPane().add(new JLabel(icon), BorderLayout.WEST);
        } catch (ResourceNotFoundException ignore) {
        }

        dialog.pack();
        dialog.setVisible(true);
        pwd.requestFocus();

        return pwd.getPassword();
    }

    /**
     * @see javax.swing.JOptionPane#createDialog(Component,String)
     */
    public static JDialog createDialog(Component parent, String title,
                                       boolean modal) {
        JDialog dialog = null;

        if (getDialogForComponent(parent) == null) {
            dialog = new JDialog(getFrameForComponent(parent), title, modal);
        } else {
            dialog = new JDialog(getDialogForComponent(parent), title, modal);
        }

        dialog.setLocationRelativeTo(parent);

        return dialog;
    }

    /* obsolete; see JOptionPane.getFrameForComponent(Component) */
    static Frame getFrameForComponent(Component component) {
        return (component instanceof Frame) ? (Frame) component
                : (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
                component);
    }

    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static Dialog getDialogForComponent(Component component) {
        return (component instanceof Dialog) ? (Dialog) component
                : (Dialog) SwingUtilities.getAncestorOfClass(Dialog.class,
                component);
    }

    /**
     * Aligns the first <code>rows</code><code>cols</code> components
     * of <code>parent</code> in a grid. Each component is as big as the
     * maximum preferred width and height of the components. The parent is
     * made just big enough to fit them all. The parent component must be a
     * SpringLayout. The code for this method is based on the turorial from
     * the Sun Java website.
     *
     * @param parent   DOCUMENT ME!
     * @param rows     number of rows
     * @param cols     number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad     x padding between cells
     * @param yPad     y padding between cells
     */
    public static void makeSpringGrid(Container parent, int rows, int cols,
                                      int initialX, int initialY, int xPad, int yPad) {
        SpringLayout layout;

        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println(
                    "The first argument to makeGrid must use SpringLayout.");

            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;

        //Calculate Springs that are the max of the width/height so that all
        //cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0))
                .getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0))
                .getWidth();

        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(
                    i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        //Apply the new width/height Spring. This forces all the
        //components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(
                    i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        //Then adjust the x/y constraints of all the cells so that they
        //are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;

        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(parent.getComponent(
                    i));

            if ((i % cols) == 0) { //start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                        xPadSpring));
            }

            if ((i / cols) == 0) { //first row
                cons.setY(initialYSpring);
            } else { //y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(
                        SpringLayout.SOUTH), yPadSpring));
            }

            lastCons = cons;
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                Spring.sum(Spring.constant(yPad),
                        lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                Spring.sum(Spring.constant(xPad),
                        lastCons.getConstraint(SpringLayout.EAST)));
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(int row,
                                                                  int col, Container parent, int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent((row * cols) + col);

        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code><code>cols</code> components
     * of <code>parent</code> in a grid. Each component in a column is as wide
     * as the maximum preferred width of the components in that column; height
     * is similarly determined for each row. The parent is made just big
     * enough to fit them all. The parent component must be a SpringLayout.
     * The code for this method is based on the turorial from the Sun Java
     * website.
     *
     * @param parent   DOCUMENT ME!
     * @param rows     number of rows
     * @param cols     number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad     x padding between cells
     * @param yPad     y padding between cells
     */
    public static void makeCompactSpringGrid(Container parent, int rows,
                                             int cols, int initialX, int initialY, int xPad, int yPad) {
        SpringLayout layout;

        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println(
                    "The first argument to makeCompactGrid must use SpringLayout.");

            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);

        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);

            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).getWidth());
            }

            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r,
                        c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }

            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);

        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);

            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).getHeight());
            }

            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r,
                        c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }

            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
