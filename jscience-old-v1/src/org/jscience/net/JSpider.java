/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.swing.CloseButton;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.Menus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


/**
 * a Swing based GUI that uses functionality of the Spider class and uses a
 * default SampleCrawlerSetting instance if no other CrawlerSetting is
 * specified.
 *
 * @author Holger Antelmann
 *
 * @see JWebBrowser
 * @see Spider
 * @see SampleCrawlerSetting
 */
public class JSpider extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5812231818142923789L;

    /** DOCUMENT ME! */
    JTree jtree;

    /** DOCUMENT ME! */
    JTextArea textField;

    /** DOCUMENT ME! */
    JMenu crawlerMenu = null;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem includeHTMLCode;

    /** DOCUMENT ME! */
    JCheckBoxMenuItem singlePath;

    /** DOCUMENT ME! */
    JProgressBar progressBar;

/**
     * Creates a new JSpider object.
     *
     * @param root DOCUMENT ME!
     */
    public JSpider(URL root) {
        this(new URLTree(root));
        setCrawlerMenu(getSampleCrawlerMenu(
                (SampleCrawlerSetting) ((URLTree) jtree.getModel()).getCrawler()));
    }

/**
     * Creates a new JSpider object.
     *
     * @param urlTree DOCUMENT ME!
     */
    public JSpider(URLTree urlTree) {
        super("JSpider", false, true);
        jtree = new JTree(urlTree);
        includeHTMLCode = new JCheckBoxMenuItem("include HTML code");
        singlePath = new JCheckBoxMenuItem("single occurrence of URL");
        singlePath.setSelected(urlTree.getSinglePath());

        // set up the GUI
        setJMenuBar(getMenu());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JScrollPane leftPane = new JScrollPane(jtree);
        textField = new JTextArea();
        textField.setEditable(false);

        //textField.setLineWrap(true);
        //textField.setWrapStyleWord(true);
        JScrollPane rightPane = new JScrollPane(textField);
        JSplitPane centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPane, rightPane);
        mainPanel.add(centerPanel);
        getContentPane().add(mainPanel);

        // add JTree listener
        jtree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(final TreeSelectionEvent e) {
                    Thread t = new Thread() {
                            public void run() {
                                updateStatusText("loading");
                                statusBar.add(progressBar);
                                statusBar.repaint();

                                URLNode node = (URLNode) e.getPath()
                                                          .getLastPathComponent();
                                String s;

                                if (includeHTMLCode.isSelected()) {
                                    try {
                                        s = node.getURLCache()
                                                .getContentAsString();
                                    } catch (IOException e) {
                                        s = "I/O Error while loading page";
                                    }
                                } else {
                                    try {
                                        s = node.getURLCache().stripText();
                                    } catch (IOException e) {
                                        s = "I/O Error while loading page";
                                    }
                                }

                                textField.setText(s);
                                textField.setCaretPosition(0);

                                // expand the node beyond the crawler definition of the
                                // URL tree when the node is selected
                                if (node.isLeaf()) {
                                    node.expand();
                                }

                                statusBar.remove(progressBar);
                                updateStatusText("page loaded");
                                repaint();
                            }
                        };

                    t.start();
                }
            });
        centerPanel.setDividerLocation(200);
        centerPanel.setPreferredSize(new Dimension(600, 400));
        setLocation(100, 100);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        pack();

        //System.out.println("JSpider instanciated");
    }

    /**
     * DOCUMENT ME!
     */
    public void repaint() {
        super.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();

        //menuBar.setBackground(Color.blue);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem item;
        item = new JMenuItem("Close");
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        item.setMnemonic(KeyEvent.VK_C);
        fileMenu.add(item);
        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        item.setMnemonic(KeyEvent.VK_X);
        fileMenu.add(item);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
        includeHTMLCode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (includeHTMLCode.isSelected()) {
                        updateStatusText("changed setting to include HTML code");
                    } else {
                        updateStatusText("changed setting to exclude HTML code");
                    }
                }
            });
        viewMenu.add(includeHTMLCode);
        item = new JMenuItem("view JWebBrowser");
        item.setMnemonic(KeyEvent.VK_B);
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    URLNode node = (URLNode) jtree.getLastSelectedPathComponent();

                    if (node == null) {
                        updateStatusText("no node selected - action cancelled");

                        return;
                    }

                    JWebBrowser mb = new JWebBrowser(node.getURLCache());
                    mb.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    mb.setVisible(true);
                }
            });
        viewMenu.add(item);

        item = new JMenuItem("view embedded images list");
        item.setMnemonic(KeyEvent.VK_I);
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    URLNode node = (URLNode) jtree.getLastSelectedPathComponent();

                    if (node == null) {
                        updateStatusText("no node selected - action cancelled");

                        return;
                    }

                    JTextArea text = null;

                    try {
                        URL[] images = node.getURLCache().getImages();
                        String s = "";

                        for (int i = 0; i < images.length; i++) {
                            s += images[i];

                            if (i < (images.length - 1)) {
                                s += "\n";
                            }
                        }

                        text = new JTextArea(s);

                        JDialog dialog = new JDialog(JSpider.this,
                                "Image list", false);
                        dialog.getContentPane().setLayout(new BorderLayout());
                        dialog.getContentPane()
                              .add(new JScrollPane(text), BorderLayout.CENTER);
                        dialog.getContentPane()
                              .add(new CloseButton(dialog), BorderLayout.SOUTH);
                        dialog.pack();
                        dialog.setLocationRelativeTo(JSpider.this);
                        dialog.setVisible(true);
                    } catch (IOException ex) {
                        updateStatusText("no images found in the given URL");
                    }
                }
            });
        viewMenu.add(item);

        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(optionMenu);
        item = new JMenuItem("set new root URL");
        item.setMnemonic(KeyEvent.VK_R);
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread t = new Thread() {
                            public void run() {
                                String s = JOptionPane.showInputDialog(JSpider.this,
                                        "enter new Root URL",
                                        jtree.getModel().getRoot());

                                if (s == null) {
                                    updateStatusText("selection cancelled");

                                    return;
                                }

                                updateStatusText(
                                    "loading tree from new root ..");
                                statusBar.add(progressBar);
                                statusBar.repaint();

                                try {
                                    URL nroot = new URL(s);
                                    ((URLTree) jtree.getModel()).setRoot(nroot);
                                    updateStatusText("new root set");
                                    jtree.repaint();
                                } catch (MalformedURLException ex) {
                                    updateStatusText(
                                        "URL entered not valid; old root is maintained");
                                }

                                statusBar.remove(progressBar);
                                repaint();
                            }
                        };

                    t.start();
                }
            });
        optionMenu.add(item);
        optionMenu.add(singlePath);
        singlePath.setMnemonic(KeyEvent.VK_S);
        singlePath.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread t = new Thread() {
                            public void run() {
                                if (singlePath.isSelected()) {
                                    updateStatusText(
                                        "reloading with single URL occurrence ..");
                                } else {
                                    updateStatusText(
                                        "reloading with multiple URL occurrence ..");
                                }

                                statusBar.add(progressBar);
                                statusBar.repaint();
                                ((URLTree) jtree.getModel()).setSinglePath(singlePath.isSelected());
                                jtree.repaint();
                                updateStatusText("tree loaded");
                                statusBar.remove(progressBar);
                                repaint();
                            }
                        };

                    t.start();
                }
            });

        if (crawlerMenu != null) {
            menuBar.add(crawlerMenu);
        }

        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        winMenu.add(Menus.createLookAndFeelMenu(this));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(Menus.createAboutDialogMenuItem(this));
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * replaces the current URLTree with the given one; the crawlerMenu
     * (if present) will be set to null and will need to be re-initialized if
     * appropriate
     *
     * @param utree DOCUMENT ME!
     */
    public void setURLTree(final URLTree utree) {
        Thread t = new Thread() {
                public void run() {
                    statusBar.add(progressBar);
                    statusBar.repaint();
                    updateStatusText("reloading tree with new root ..");
                    jtree.setModel(utree);
                    setCrawlerMenu(null);
                    updateStatusText("tree loaded");
                    statusBar.remove(progressBar);
                    repaint();
                }
            };

        t.start();
    }

    /**
     * This method places a given menu between the 'Options' and
     * 'Window' menu of the GUI; the menu is intended to provide special
     * controls for the given CrawlerSetting in use with this JSpider. If the
     * CrawlerSetting in use is a SampleCrawlerSetting, getSampleCrawlerMenu()
     * provides a sample menu that can be placed here.
     *
     * @see #getSampleCrawlerMenu(SampleCrawlerSetting)
     */
    public void setCrawlerMenu(JMenu menu) {
        crawlerMenu = menu;
        setJMenuBar(getMenu());
        repaint();
    }

    /**
     * provides a sample JMenu that provides controls to change
     * settings on a SampleCrawlerSetting; the return value can the be used
     * for setCrawlerMenu()
     *
     * @see #setCrawlerMenu(JMenumenu)
     */
    public JMenu getSampleCrawlerMenu(final SampleCrawlerSetting crawler) {
        JMenu smenu = new JMenu("Crawler settings");
        JMenuItem item = new JMenuItem("view settings");
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = new JDialog(JSpider.this,
                            "Crawler settings", true);
                    JTextArea text = new JTextArea(((URLTree) jtree.getModel()).getCrawler()
                                                    .toString());
                    text.setLineWrap(true);
                    text.setWrapStyleWord(true);
                    text.setPreferredSize(new Dimension(200, 100));
                    dialog.getContentPane().setLayout(new BorderLayout());
                    dialog.getContentPane()
                          .add(new JScrollPane(text), BorderLayout.CENTER);
                    dialog.getContentPane()
                          .add(new CloseButton(dialog), BorderLayout.SOUTH);
                    dialog.pack();
                    dialog.setLocationRelativeTo(JSpider.this);
                    dialog.setVisible(true);
                }
            });
        smenu.add(item);
        item = new JMenuItem("set tree depth");
        item.setMnemonic(KeyEvent.VK_D);
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s = JOptionPane.showInputDialog(JSpider.this,
                            "enter default tree depth",
                            new Integer(((SampleCrawlerSetting) ((URLTree) jtree.getModel()).getCrawler()).depth));

                    try {
                        ((SampleCrawlerSetting) ((URLTree) jtree.getModel()).getCrawler()).depth = Integer.parseInt(s);
                    } catch (NumberFormatException ex) {
                    }
                }
            });
        smenu.add(item);

        final JCheckBoxMenuItem restrictToSite = new JCheckBoxMenuItem(
                "restrict to current site");
        restrictToSite.setSelected(crawler.currentSiteOnly);
        smenu.setMnemonic(KeyEvent.VK_C);
        restrictToSite.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread t = new Thread() {
                            public void run() {
                                updateStatusText("validating URL tree ..");
                                statusBar.add(progressBar);
                                statusBar.repaint();
                                crawler.currentSiteOnly = restrictToSite.isSelected();
                                ((URLTree) jtree.getModel()).revalidate(false);
                                updateStatusText(
                                    "new crawler settings in effect");
                                statusBar.remove(progressBar);
                                repaint();
                            }
                        };

                    t.start();
                }
            });
        smenu.add(restrictToSite);

        return smenu;
    }

    /**
     * starts a JSpider with the local host as root and makes the
     * JSpider visible
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        //URL url = new URL("file:///C:/Documents%20and%20Settings/holger/My%20Documents/My%20Webs/index.html");
        URL url = new URL("http://localhost/");
        URLTree tree = new URLTree(url);
        JSpider jspider = new JSpider(tree);
        jspider.setCrawlerMenu(jspider.getSampleCrawlerMenu(
                (SampleCrawlerSetting) tree.getCrawler()));
        jspider.setVisible(true);
    }
}
