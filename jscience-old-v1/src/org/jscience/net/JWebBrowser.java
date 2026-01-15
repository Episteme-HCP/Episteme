/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.swing.CloseButton;
import org.jscience.swing.ImageViewer;
import org.jscience.swing.JMainFrame;
import org.jscience.swing.Menus;
import org.jscience.util.Settings;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * A very plain Web Browser with minimal functionality; but still cool.
 * <p/>
 * Unfortunately, the caching doesn't work for the main display, yet. <p>
 * Check out the multiple right-mouse-click options. :-) <p>
 * At this point, the implementation is completely transient, i.e.
 * nothing is written to disk other than what you explicitly save;
 * so your homepage settings, cache and favorites are reset when you
 * restart the browser. <p>
 * No support for cookies (that may be the good news ;-), JavaScript,
 * forms, etc. at this point. <p>
 * There are several other limitations as this implementation is just based
 * on the javax.swing.JEditorPane at this point.
 * However, there are also some features that your average browser does NOT have,
 * such as the display of the server information in the view menu.
 * <p/>
 * To use a customized HTTP Agent when connecting to a web-server, you need to
 * set the system property <dfn>http.agent</dfn> accordingly before using this
 * class.
 *
 * @author Holger Antelmann
 * @see JSpider
 * @see javax.swing.JEditorPane
 */
public class JWebBrowser extends JMainFrame {
    static final long serialVersionUID = 8894203216788969688L;

    URL home;
    URLCache current;
    HashMap<URL, URLCache> cache;
    Stack<URLCache> history;
    Stack<URLCache> forwards;
    Stack<URL> favorites;
    JMenu favMenu;
    JButton stopButton;
    URLCache nowLoading;
    JURLCachePane textPane;
    JTextField textUrlField;
    JFileChooser fchooser;
    URL hyperlinkHoover;
    volatile boolean busy;
    /**
     * needed for the @#$*@#$ JEditorPane to reload the page in refresh()
     */
    static URLCache blankPage = new URLCache(Settings.getResource("org/jscience/blank_page.htm"));
    String[] favoriteDefaults = new String[]{
            Settings.getProperty("application.vendor.url"),
            Settings.getProperty("application.documentation.url"),
            "http://www.gnu.org/",
            "http://java.sun.com/",
            "http://localhost/"
    };

    /**
     * initializes with a blank page
     */
    public JWebBrowser() {
        this(blankPage);
    }

    public JWebBrowser(String urlHomeSpec) throws IOException {
        this(new URL(urlHomeSpec));
    }

    public JWebBrowser(URL homeURL) {
        this(new URLCache(homeURL));
    }

    public JWebBrowser(URLCache homeURLCache) {
        super("Holger's Java Browser", false, true);
        home = homeURLCache.getURL();
        textUrlField = new JTextField(home.toString());
        fchooser = new JFileChooser();
        history = new Stack<URLCache>();
        forwards = new Stack<URLCache>();
        favorites = new Stack<URL>();
        cache = new HashMap<URL, URLCache>();
        textPane = new JURLCachePane(cache);
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (!busy) setPage(e.getURL());
                } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                    hyperlinkHoover = e.getURL();
                    if (!busy) updateStatusText(e.getURL().toString());
                } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
                    hyperlinkHoover = null;
                    if (!busy) updateStatusText(" ");
                }
            }
        });
        textPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup(e);
            }

            public void mouseReleased(MouseEvent e) {
                popup(e);
            }

            void popup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu menu = getPopupMenu();
                    if (hyperlinkHoover != null) {
                        JMenuItem item = new JMenuItem("open in new window");
                        item.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                URL url = hyperlinkHoover;
                                if (url == null) return;
                                JWebBrowser jwb = new JWebBrowser(url);
                                jwb.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                                jwb.setVisible(true);
                            }
                        });
                        menu.insert(item, 0);
                        item = new JMenuItem("save link");
                        item.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                download(hyperlinkHoover);
                            }
                        });
                        menu.insert(item, 0);
                    }
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(textPane);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.setSize((int) screen.getWidth() / 2, (int) screen.getHeight() / 2);
        scrollPane.setPreferredSize(screen);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(getToolBar(), BorderLayout.NORTH);
        setJMenuBar(getMenu());
        current = homeURLCache;
        cache.put(current.getURL(), current);
        setPage(current.getURL());
        pack();
    }

    JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item;
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        {
            item = new JMenuItem("Save page as ..");
            item.setMnemonic(KeyEvent.VK_S);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    download(current);
                }
            });
            fileMenu.add(item);

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
        }
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
        {
            item = new JMenuItem("show header info");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final JDialog dialog = new JDialog(JWebBrowser.this, "header info", true);
                    dialog.getContentPane().setLayout(new BorderLayout());
                    String s = "cannot access header";
                    try {
                        s = new Spider(current.getURL()).fullHeaderAsString();
                    } catch (IOException ex) {
                    }
                    JTextArea pane = new JTextArea(s);
                    pane.setEditable(false);
                    dialog.getContentPane().add(pane, BorderLayout.CENTER);
                    dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
                    dialog.pack();
                    dialog.setLocationRelativeTo(JWebBrowser.this);
                    dialog.setVisible(true);
                }
            });
            viewMenu.add(item);
            item = new JMenuItem("show Whois registration info");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final JDialog dialog = new JDialog(JWebBrowser.this, "Whois registration info", true);
                    dialog.getContentPane().setLayout(new BorderLayout());
                    String s = null;
                    try {
                        s = new Spider(current.getURL()).whois();
                    } catch (IOException ex) {
                    }
                    if (s == null) s = "cannot access Whois registration info";
                    JTextArea pane = new JTextArea(s);
                    pane.setEditable(false);
                    dialog.getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);
                    dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
                    dialog.pack();
                    dialog.setLocationRelativeTo(JWebBrowser.this);
                    dialog.setVisible(true);
                }
            });
            viewMenu.add(item);
            MenuElement[] elements = getPopupMenu().getSubElements();
            for (int i = 0; i < elements.length; i++) {
                viewMenu.add((Component) elements[i]);
            }
        }
        favMenu = new JMenu("Favorites");
        favMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(favMenu);
        {
            item = new JMenuItem("add to favorites");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final URLCache uc = current;
                    favorites.add(uc.getURL());
                    JMenuItem nf = new JMenuItem(uc.getURL().toString());
                    nf.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            setPage(uc.getURL());
                        }
                    });
                    favMenu.add(nf);
                }
            });
            item.setMnemonic(KeyEvent.VK_A);
            favMenu.add(item);
            JMenu defaults = new JMenu("defaults");
            {
                for (int i = 0; i < favoriteDefaults.length; i++) {
                    item = new JMenuItem(favoriteDefaults[i]);
                    final int n = i;
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            try {
                                setPage(new URL(favoriteDefaults[n]));
                            } catch (IOException e) {
                            }
                        }
                    });
                    defaults.add(item);
                }
            }
            favMenu.add(defaults);
            favMenu.addSeparator();
        }
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);
        menuBar.add(toolsMenu);
        {
            item = new JMenuItem("Messenger");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JMessenger js = new JMessenger();
                        js.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        js.setVisible(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(JWebBrowser.this,
                                ex.getMessage(), "Messenger failed to start",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            item.setMnemonic(KeyEvent.VK_U);
            toolsMenu.add(item);

            item = new JMenuItem("Chat client");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JChat js = new JChat();
                    js.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    js.setVisible(true);
                }
            });
            item.setMnemonic(KeyEvent.VK_U);
            toolsMenu.add(item);

            item = new JMenuItem("URL tree");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JSpider js = new JSpider(current.getURL());
                    js.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    js.setVisible(true);
                }
            });
            item.setMnemonic(KeyEvent.VK_U);
            toolsMenu.add(item);
        }
        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(optionMenu);
        {
            item = new JMenuItem("set current page as home");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHome(current.getURL());
                }
            });
            item.setMnemonic(KeyEvent.VK_S);
            optionMenu.add(item);

            item = new JMenuItem("set HTTP agent");
            item.setToolTipText("this determines how the browser identifies itself");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String p = JOptionPane.showInputDialog(JWebBrowser.this,
                            "enter HTTP agent identification:",
                            System.getProperty("http.agent", "Java1.4.0"));
                    if (p != null) System.setProperty("http.agent", p);
                }
            });
            item.setMnemonic(KeyEvent.VK_S);
            optionMenu.add(item);

            item = new JMenuItem("validate links in page");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    validateLinks(current);
                }
            });
            item.setMnemonic(KeyEvent.VK_V);
            optionMenu.add(item);

            item = new JMenuItem("clear cache");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    synchronized (cache) {
                        Iterator i = cache.keySet().iterator();
                        while (i.hasNext()) {
                            ((URLCache) cache.get((URL) i.next())).clearCache();
                        }
                    }
                }
            });
            item.setMnemonic(KeyEvent.VK_V);
            optionMenu.add(item);
        }
        JMenu winMenu = new JMenu("Window");
        winMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(winMenu);
        {
            winMenu.add(Menus.createLookAndFeelMenu(this));
        }
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(Menus.createAboutDialogMenuItem(this));
        menuBar.add(helpMenu);
        return menuBar;
    }

    JPopupMenu getPopupMenu() {
        JPopupMenu miniMenu = new JPopupMenu();
        JMenuItem item;

        item = new JMenuItem("HTML code");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewHTML();
            }
        });
        item.setMnemonic(KeyEvent.VK_H);
        miniMenu.add(item);

        item = new JMenuItem("plain text");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTextOnly();
            }
        });
        item.setMnemonic(KeyEvent.VK_T);
        miniMenu.add(item);

        item = new JMenuItem("links");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewLinkList();
            }
        });
        item.setMnemonic(KeyEvent.VK_L);
        miniMenu.add(item);

        item = new JMenuItem("embedded images");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewImageList();
            }
        });
        item.setMnemonic(KeyEvent.VK_I);
        miniMenu.add(item);

        return miniMenu;
    }

    JToolBar getToolBar() {
        JToolBar tools = new JToolBar();
        tools.setRollover(true);
        //tools.setMargin(new Insets(1, 1, 1, 1));
        ImageIcon icon = new ImageIcon(Settings.getResource("org/jscience/awt/icons/navigation/Back16.gif"));
        JButton button = new JButton(icon);
        button.setToolTipText("Back");
        button.setMnemonic(KeyEvent.VK_B);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource("org/jscience/awt/icons/navigation/Forward16.gif"));
        button = new JButton(icon);
        button.setToolTipText("Forward");
        button.setMnemonic(KeyEvent.VK_R);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forward();
            }
        });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource("org/jscience/awt/icons/navigation/Home16.gif"));
        button = new JButton(icon);
        button.setToolTipText("Home");
        button.setMnemonic(KeyEvent.VK_M);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setPage(home);
            }
        });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource("org/jscience/awt/icons/general/Refresh16.gif"));
        button = new JButton(icon);
        button.setToolTipText("Refresh");
        button.setMnemonic(KeyEvent.VK_R);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        tools.add(button);
        icon = new ImageIcon(Settings.getResource("org/jscience/awt/icons/general/Stop16.gif"));
        stopButton = new JButton(icon);
        stopButton.setToolTipText("Stop");
        stopButton.setMnemonic(KeyEvent.VK_S);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopLoading();
            }
        });
        stopButton.setEnabled(false);
        tools.add(stopButton);
        tools.addSeparator();
        textUrlField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setPage(new URL(textUrlField.getText()));
                } catch (MalformedURLException ex) {
                    try {
                        setPage(new URL("http://" + textUrlField.getText()));
                    } catch (MalformedURLException exc) {
                        updateStatusText("entered URL invalid");
                    }
                }
            }
        });
        tools.add(textUrlField);
        return tools;
    }

    /**
     * shows a window with only the text stripped from the HTML
     */
    protected void viewTextOnly() {
        try {
            JTextArea text = new JTextArea(current.stripText());
            final JDialog dialog = new JDialog(this, "plain text", true);
            dialog.getContentPane().setLayout(new BorderLayout());
            JScrollPane pane = new JScrollPane(text);
            Dimension d = new Dimension((int) getSize().getWidth(),
                    (int) (getSize().getHeight() * 0.7));
            pane.setPreferredSize(d);
            dialog.getContentPane().add(pane, BorderLayout.CENTER);
            dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (IOException e) {
            updateStatusText("could not load text");
            return;
        }
    }

    /**
     * shows a window with the HTML code
     */
    protected void viewHTML() {
        try {
            JTextArea text = new JTextArea(current.getContentAsString());
            final JDialog dialog = new JDialog(this, "HTML code", true);
            dialog.getContentPane().setLayout(new BorderLayout());
            JScrollPane pane = new JScrollPane(text);
            Dimension d = new Dimension((int) getSize().getWidth(),
                    (int) (getSize().getHeight() * 0.7));
            pane.setPreferredSize(d);
            dialog.getContentPane().add(pane, BorderLayout.CENTER);
            dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (IOException e) {
            updateStatusText("could not HTML code");
            return;
        }
    }

    /**
     * shows a window with the embedded images from the current page
     */
    protected void viewImageList() {
        final JDialog dialog = new JDialog(this, "embedded images", false);
        dialog.getContentPane().setLayout(new BorderLayout());
        final Stack<ImageViewer> viewers = new Stack<ImageViewer>();
        final JList list;
        try {
            list = new JList(URLSorter.sort(current.getImages()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "cannot retrieve images", JOptionPane.ERROR_MESSAGE);
            //updateStatusText(" ");
            return;
        }
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    URL url = (URL) list.getSelectedValue();
                    ImageIcon icon = new ImageIcon(url);
                    ImageViewer v = new ImageViewer(icon, url.toString());
                    viewers.add(v);
                    v.setVisible(true);
                }
            }

            public void mousePressed(MouseEvent e) {
                popup(e);
            }

            public void mouseReleased(MouseEvent e) {
                popup(e);
            }

            void popup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("save image");
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            URL url = (URL) list.getSelectedValue();
                            if (url == null) {
                                JOptionPane.showMessageDialog(dialog, "no image selected");
                            } else {
                                download(url);
                            }
                        }
                    });
                    menu.add(item);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        dialog.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        JButton button = new JButton("Close");
        button.setMnemonic(KeyEvent.VK_C);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < viewers.size(); i++) {
                    ((ImageViewer) viewers.get(i)).setVisible(false);
                    ((ImageViewer) viewers.get(i)).dispose();
                }
                dialog.setVisible(false);
            }
        });
        dialog.getContentPane().add(button, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        //updateStatusText(" ");
    }

    /**
     * shows a window with the links from the given page
     * (as probably not all links are accessible via hyperlink from the
     * JEditorPane)
     */
    protected void viewLinkList() {
        final JDialog dialog = new JDialog(this, "link list", true);
        dialog.getContentPane().setLayout(new BorderLayout());
        final JList list;
        try {
            list = new JList(URLSorter.sort(current.getLinks()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "cannot retrieve links", JOptionPane.ERROR_MESSAGE);
            //updateStatusText(" ");
            return;
        }
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setPage((URL) list.getSelectedValue());
                }
            }

            public void mousePressed(MouseEvent e) {
                popup(e);
            }

            public void mouseReleased(MouseEvent e) {
                popup(e);
            }

            void popup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("save link");
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            URL url = (URL) list.getSelectedValue();
                            if (url == null) {
                                JOptionPane.showMessageDialog(dialog, "no link selected");
                            } else {
                                download(url);
                            }
                        }
                    });
                    menu.add(item);
                    item = new JMenuItem("open in new window");
                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            URL url = (URL) list.getSelectedValue();
                            if (url == null) return;
                            JWebBrowser jwb = new JWebBrowser(url);
                            jwb.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            jwb.setVisible(true);
                        }
                    });
                    menu.add(item);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        dialog.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        //updateStatusText(" ");
    }

    /**
     * shows a window with the broken links in this page
     */
    protected void validateLinks(final URLCache uc) {
        if (busy) return;
        busy = true;
        new Thread() {
            public void run() {
                validateLinksInThread(uc);
            }
        }.start();
        updateStatusText("validating links ..");
    }

    /**
     * executes outside the event handler thread
     */
    private void validateLinksInThread(URLCache uc) {
        final URL[] broken;
        try {
            broken = URLSorter.sort(new Spider(uc.getURL()).getBrokenLinks());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "cannot verify links", JOptionPane.ERROR_MESSAGE);
            updateStatusText(" ");
            return;
        }
        if (broken.length == 0) {
            JOptionPane.showMessageDialog(this, "no broken links in current page",
                    "links validated", JOptionPane.INFORMATION_MESSAGE);
            updateStatusText(" ");
            return;
        }
        Runnable showDialog = new Runnable() {
            public void run() {
                // this is going to be performed in the event handler thread again
                JDialog dialog = new JDialog(JWebBrowser.this, "broken links", true);
                dialog.getContentPane().setLayout(new BorderLayout());
                JList list = new JList(broken);
                dialog.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
                dialog.getContentPane().add(new CloseButton(dialog), BorderLayout.SOUTH);
                dialog.pack();
                dialog.setLocationRelativeTo(JWebBrowser.this);
                dialog.setVisible(true);
                updateStatusText(" ");
                busy = false;
            }
        };
        SwingUtilities.invokeLater(showDialog);
    }

    /**
     * implements the action for the browser's refresh button
     */
    public void refresh() {
        URLCache tmp = current;
        current.clearCache();
        setPage(blankPage);
        busy = false;
        setPage(tmp);
    }

    /**
     * sets the current page for the browser's main window;
     * the call may be ignored if the browser is currently
     * busy loading another page (preventing multiple loads)
     */
    public void setPage(URL url) {
        URLCache uc;
        synchronized (cache) {
            if (cache.containsKey(url)) {
                uc = (URLCache) cache.get(url);
            } else {
                uc = new URLCache(url);
                cache.put(url, uc);
            }
        }
        history.push(current);
        forwards.clear();
        setPage(uc);
    }

    /**
     * only to be called by back(), forward() and refresh(),
     * as it doesn't maintain history; use setPage(URL) otherwise
     */
    private void setPage(final URLCache uc) {
        if (busy) return;
        busy = true;
        new Thread() {
            public void run() {
                loadPageInThread(uc);
            }
        }.start();
        nowLoading = uc;
        stopButton.setEnabled(true);
        updateStatusText("loading page ..");
    }

    /**
     * executes outside the event handler thread and is only called by setPage(URLCache)
     */
    private void loadPageInThread(final URLCache uc) {
        // loading URL into cache
        uc.waitForRefresh();
        stopButton.setEnabled(false);
        // put the remainder back in the event handler thread
        Runnable showstuff = new Runnable() {
            public void run() {
                try {
                    String type = uc.getContentType();
                    if ((type != null) && (type.indexOf("mage/") > 0)) {
                        ImageViewer iv = new ImageViewer(new ImageIcon(uc.getURL()), uc.getURL().toString());
                        iv.setVisible(true);
                        updateStatusText("displaying image in external viewer");
                        busy = false;
                        return;
                    }
                    // textPane.setPage(uc);
                    textPane.setPage(uc.getURL());
                    current = uc;
                    textUrlField.setText(current.getURL().toString());
                    updateStatusText("page loaded: " + ((current.getTitle().length() == 0) ?
                            "(no title)" : current.getTitle()));
                } catch (IOException e) {
                    // todo
                    //textPane.setPage((URL)null);
                    current = uc;
                    textUrlField.setText(current.getURL().toString());
                    updateStatusText("page loaded with error: " + current.getURL().toString());
                    JOptionPane.showMessageDialog(JWebBrowser.this, e.getMessage(),
                            "page load error", JOptionPane.ERROR_MESSAGE);
                    updateStatusText(" ");
                }
                busy = false;
            }
        };
        SwingUtilities.invokeLater(showstuff);
    }

    /**
     * implements the action for the browser's stop button
     */
    public void stopLoading() {
        if (!stopButton.isEnabled()) return;
        stopButton.setEnabled(false);
        nowLoading.stopCurrentRefresh();
    }

    /**
     * downloads the given URL with a JDownloader after the GUI
     * asked for the file name
     *
     * @see JDownloader
     */
    protected void download(URL url) {
        if (url == null) return;
        URLCache uc;
        synchronized (cache) {
            if (cache.containsKey(url)) {
                uc = (URLCache) cache.get(url);
            } else {
                uc = new URLCache(url);
                cache.put(url, uc);
            }
        }
        download(uc);
    }

    void download(final URLCache uc) {
        if (uc == null) return;
        if (busy) return;
        fchooser.setDialogTitle("download URL");
        if (uc.getURL().getProtocol().equals("mailto")) {
            JOptionPane.showMessageDialog(this, "cannot download email links");
            updateStatusText(" ");
            return;
        }
        String path = uc.getURL().toExternalForm();
        path = path.substring(path.lastIndexOf("/"));
        if (path.length() < 2) path = "default.html";
        fchooser.setSelectedFile(new File(fchooser.getCurrentDirectory(), path));
        int option = fchooser.showSaveDialog(this);
        if (option == fchooser.APPROVE_OPTION) {
            if (!uc.isCached()) uc.refresh();
            File file = fchooser.getSelectedFile();
            new JDownloader(this, uc, file).start();
        }
    }

    /**
     * implements the action for the browser's back button
     */
    public void back() {
        if (busy) return;
        if (history.size() > 0) {
            setPage((URLCache) history.peek());
            forwards.push(current);
            current = (URLCache) history.pop();
            textUrlField.setText(current.getURL().toString());
        }
    }

    /**
     * implements the action for the browser's forward button
     */
    public void forward() {
        if (busy) return;
        if (forwards.size() > 0) {
            setPage((URLCache) forwards.peek());
            history.push(current);
            current = (URLCache) forwards.pop();
            textUrlField.setText(current.getURL().toString());
        }
    }

    /**
     * returns the page currently set as the home page
     */
    public URL getHome() {
        return home;
    }

    /**
     * returns the list of favorites
     */
    public URL[] getFavorites() {
        return (URL[]) favorites.toArray(new URL[favorites.size()]);
    }

    /* sets the home page that can be accessed using the browser's home button */
    public void setHome(URL homeURL) {
        if (homeURL == null) throw new IllegalArgumentException();
        home = homeURL;
    }

    /**
     * returns the page currently displayed
     */
    public URL getCurrentURL() {
        return current.getURL();
    }

    /**
     * returns a map of URL objects to URLCache objects
     */
    public Map getCache() {
        return cache;
    }

    /**
     * starts the JWebBrowser and makes it visible;
     * you can optionally specify a URL as parameter
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            new JWebBrowser(new URL(args[0])).setVisible(true);
        } else {
            new JWebBrowser().setVisible(true);
        }
    }
}
