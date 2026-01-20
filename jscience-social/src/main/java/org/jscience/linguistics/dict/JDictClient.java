package org.jscience.linguistics.dict;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Zakladni GUI klient pro DICT dle RFC2229 implementovano SHOW DB, SHOW
 * STRAT, DEFINE, MATCH, QUIT
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1.5
 */
public class JDictClient {
    /* START: konstanty */
    private final static String TITLE_ERROR = "JDictClient error"; // titulek chybove hlasky

    /**
     * DOCUMENT ME!
     */
    private final static String TITLE_INFO = "JDictClient information"; // titulek chybove hlasky

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_FILE = "jdictclient.conf"; // konfiguracni soubor

    /**
     * DOCUMENT ME!
     */
    private final static int DIST = 3; // mezera mezi komponentami

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_SERVER = "server."; // prefix informce z konfiguracniho souboru

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_NAME = ".name"; // klic - jmeno serveru

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_PORT = ".port"; // klic - cislo portu

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_LABEL = "label."; // klic - text popisku

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_MENU = "menu."; // klic - text nabidek

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_LANG = "language"; // klic - jazyk

    /**
     * DOCUMENT ME!
     */
    private final static String CONF_COLUMN = "column."; // klic - nazvy sloupecku

    /* END: konstanty */
    /* START: nabidky */
    private JMenuBar mnuMain = new JMenuBar();

    /**
     * DOCUMENT ME!
     */
    private JMenu mnuFile = new JMenu("File");

    /**
     * DOCUMENT ME!
     */
    private JMenuItem miEnd = new JMenuItem("End");

    /**
     * DOCUMENT ME!
     */
    private JMenuItem miSave = new JMenuItem("Save");

    /**
     * DOCUMENT ME!
     */
    private JMenu mnuHelp = new JMenu("Help");

    /**
     * DOCUMENT ME!
     */
    private JMenuItem miAbout = new JMenuItem("About");

    /* END: nabidky */
    /* START: GUI */
    private JFrame frmMain = new JFrame("JDictClient"); // hlavni okno

    /**
     * DOCUMENT ME!
     */
    private JLabel lblServerName = new JLabel("Server:");

    /**
     * DOCUMENT ME!
     */
    private JComboBox cboServerName = new JComboBox();

    /**
     * DOCUMENT ME!
     */
    private JLabel lblDatabaseName = new JLabel("Database:");

    /**
     * DOCUMENT ME!
     */
    private JComboBox cboDatabaseName = new JComboBox();

    /**
     * DOCUMENT ME!
     */
    private JLabel lblSearchStyle = new JLabel("Search style:");

    /**
     * DOCUMENT ME!
     */
    private JComboBox cboSearchStyle = new JComboBox();

    /**
     * DOCUMENT ME!
     */
    private JLabel lblSearch = new JLabel("Search:");

    /**
     * DOCUMENT ME!
     */
    private JTextField txtSearch = new JTextField();

    /**
     * DOCUMENT ME!
     */
    private JTextPane txtResult = new JTextPane(); // definice nalezenho vyrazu

    /**
     * DOCUMENT ME!
     */
    private JButton cmdSearch = new JButton("Search");

    /**
     * DOCUMENT ME!
     */
    private JTable tblResult = new JTable(); // seznam nalezenych vyrazu

    /* END: GUI */
    private Properties prop = new Properties(); // konfigurace

    /**
     * DOCUMENT ME!
     */
    private String[] headers = { "Word", "Database" }; // zahlavi tabulky

    /**
     * DOCUMENT ME!
     */
    private String lastDb = new String();

    /* START: zpracovani udalosti */
    ActionListener act = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(cboServerName)) {
                    fillComboBoxes();
                } else if (e.getSource().equals(cmdSearch)) {
                    fillResultList();
                } else if (e.getSource().equals(miEnd)) {
                    System.exit(0);
                } else if (e.getSource().equals(miSave)) {
                    save();
                } else if (e.getSource().equals(miAbout)) {
                    about();
                }
            }
        };

    /**
     * DOCUMENT ME!
     */
    ListSelectionListener lsl = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (!lsm.isSelectionEmpty()) {
                    fillResultText(lsm.getMinSelectionIndex());
                }
            }
        };

    /**
     * DOCUMENT ME!
     */
    HyperlinkListener hyp = new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    fillResultText(lastDb, e.getURL().toString().substring(7));
                }
            }
        };

/**
     * Creates a new JDictClient object.
     */
    public JDictClient() {
        /* START: konfigurace */
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(
                        CONF_FILE), "UTF-8");
            StringBuffer conf = new StringBuffer();
            int c;

            while ((c = in.read()) != -1)
                conf.append((char) c);

            in.close();

            prop.load(new ByteArrayInputStream(conf.toString().getBytes()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can not read config file.",
                TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            System.out.println(e.toString());
            System.exit(1);
        }

        int c = 1;

        while (prop.containsKey(CONF_SERVER + c + CONF_NAME)) {
            try {
                int p = Integer.parseInt((String) prop.get(CONF_SERVER + c +
                            CONF_PORT));
                String s = (String) prop.get(CONF_SERVER + c + CONF_NAME);
                cboServerName.addItem(new DictServerInfo(s, p));
                c++;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                    "Bad port number in conf file.", TITLE_ERROR,
                    JOptionPane.WARNING_MESSAGE);
                System.out.println(e.toString());
            }
        }

        if (cboServerName.getItemCount() < 1) {
            JOptionPane.showMessageDialog(null,
                "No servers found in " + CONF_FILE, TITLE_ERROR,
                JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

        /* END: konfigurace */
        initComponents();
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setSize(400, 350);
        frmMain.show();
    } // END: JDict()

    /* END: zpracovani udalosti */
    public static void main(String[] args) {
        JDictClient jd = new JDictClient();
    } // END: main(String[] args)

    /**
     * Inicializace GUI
     */
    private void initComponents() {
        /* START: nastaveni popisku */
        if (prop.containsKey(CONF_LANG)) {
            String lang = (String) prop.get(CONF_LANG);

            if (prop.containsKey(CONF_LABEL + lang + ".1")) {
                lblServerName.setText((String) prop.get(CONF_LABEL + lang +
                        ".1"));
            }

            if (prop.containsKey(CONF_LABEL + lang + ".2")) {
                lblDatabaseName.setText((String) prop.get(CONF_LABEL + lang +
                        ".2"));
            }

            if (prop.containsKey(CONF_LABEL + lang + ".3")) {
                lblSearchStyle.setText((String) prop.get(CONF_LABEL + lang +
                        ".3"));
            }

            if (prop.containsKey(CONF_LABEL + lang + ".4")) {
                lblSearch.setText((String) prop.get(CONF_LABEL + lang + ".4"));
            }

            if (prop.containsKey(CONF_LABEL + lang + ".5")) {
                cmdSearch.setText((String) prop.get(CONF_LABEL + lang + ".5"));
            }

            if (prop.containsKey(CONF_MENU + lang + ".1")) {
                mnuFile.setText((String) prop.get(CONF_MENU + lang + ".1"));
            }

            if (prop.containsKey(CONF_MENU + lang + ".2")) {
                miSave.setText((String) prop.get(CONF_MENU + lang + ".2"));
            }

            if (prop.containsKey(CONF_MENU + lang + ".3")) {
                miEnd.setText((String) prop.get(CONF_MENU + lang + ".3"));
            }

            if (prop.containsKey(CONF_MENU + lang + ".4")) {
                mnuHelp.setText((String) prop.get(CONF_MENU + lang + ".4"));
            }

            if (prop.containsKey(CONF_MENU + lang + ".5")) {
                miAbout.setText((String) prop.get(CONF_MENU + lang + ".5"));
            }

            if (prop.containsKey(CONF_COLUMN + lang + ".1")) {
                headers[0] = (String) prop.get(CONF_COLUMN + lang + ".1");
            }

            if (prop.containsKey(CONF_COLUMN + lang + ".2")) {
                headers[1] = (String) prop.get(CONF_COLUMN + lang + ".2");
            }
        } // END: if

        /* END: nastaveni popisku */
        cboServerName.addActionListener(act);
        cboServerName.setSelectedIndex(0);
        cmdSearch.addActionListener(act);

        txtResult.setContentType("text/html");
        txtResult.setEditable(false);
        txtResult.addHyperlinkListener(hyp);

        tblResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblResult.getSelectionModel().addListSelectionListener(lsl);

        /* START: nabidky */
        miEnd.addActionListener(act);
        miSave.addActionListener(act);
        miAbout.addActionListener(act);

        mnuFile.add(miSave);
        mnuFile.addSeparator();
        mnuFile.add(miEnd);

        mnuHelp.add(miAbout);

        mnuMain.add(mnuFile);
        mnuMain.add(mnuHelp);
        frmMain.setJMenuBar(mnuMain);

        /* END: nabidky */
        /* START: popisky */
        JPanel pan1 = new JPanel(new GridLayout(4, 1, DIST, DIST));
        pan1.add(lblServerName);
        pan1.add(lblDatabaseName);
        pan1.add(lblSearchStyle);
        pan1.add(lblSearch);

        /* END: popisky */
        /* START: pole pro data */
        JPanel pan2 = new JPanel(new GridLayout(4, 1, DIST, DIST));
        pan2.add(cboServerName);
        pan2.add(cboDatabaseName);
        pan2.add(cboSearchStyle);
        pan2.add(txtSearch);

        /* END: pole pro data */
        /* START: horni cast */
        JPanel pan3 = new JPanel(new BorderLayout(DIST, DIST));
        pan3.add(BorderLayout.WEST, pan1);
        pan3.add(BorderLayout.CENTER, pan2);
        pan3.add(BorderLayout.SOUTH, cmdSearch);

        /* END: horni cast */
        /* START: dolni cast */
        JSplitPane pan4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblResult), 
            //new JScrollPane(lstResult),
            new JScrollPane(txtResult));

        /* END: dolni cast */
        Container con = frmMain.getContentPane();
        con.setLayout(new BorderLayout(DIST, DIST));
        con.add(BorderLayout.NORTH, pan3);
        con.add(BorderLayout.CENTER, pan4);
    } // END: initComponents()

    /**
     * DOCUMENT ME!
     */
    private void fillComboBoxes() {
        DictServerInfo dsi = (DictServerInfo) cboServerName.getSelectedItem();

        try {
            fillComboBox(cboDatabaseName, dsi.getDatabase());
            fillComboBox(cboSearchStyle, dsi.getStrategy());
        } catch (DictException e) {
            JOptionPane.showMessageDialog(null, e.toString(), TITLE_ERROR,
                JOptionPane.WARNING_MESSAGE);
            System.out.println(e.toString());
        }
    } // END: fillComboBoxes()

    /**
     * DOCUMENT ME!
     *
     * @param cbo DOCUMENT ME!
     * @param data DOCUMENT ME!
     */
    private void fillComboBox(JComboBox cbo, Vector data) {
        cbo.removeAllItems();

        int itemsCount = data.size();

        for (int i = 0; i < itemsCount; i++)
            cbo.addItem(data.elementAt(i));
    } // END: fillComboBox(JComboBox cbo, Vector data)

    /**
     * DOCUMENT ME!
     */
    private void fillResultList() {
        try {
            String db = ((DSItem) cboDatabaseName.getSelectedItem()).getName();
            String strat = ((DSItem) cboSearchStyle.getSelectedItem()).getName();
            Vector data = ((DictServerInfo) cboServerName.getSelectedItem()).getWords(db,
                    strat, txtSearch.getText());
            TableResult tr = new TableResult(data);
            tr.setColumnNames(headers);
            tblResult.setModel(tr);
        } catch (DictException e) {
            if (e.getErrorNumber() < 400) {
                JOptionPane.showMessageDialog(frmMain, e.toString(),
                    TITLE_INFO, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frmMain, e.toString(),
                    TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
            }

            System.out.println(e.toString());
        }
    } // END: fillResultList()

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    private void fillResultText(int row) {
        TableResult tr = (TableResult) tblResult.getModel();
        DSItem dci = (DSItem) tr.getValueAt(row, 0);
        String db = dci.getName();
        lastDb = db;

        String word = dci.toString();
        fillResultText(db, word);
    } // END: fillResultText()

    /**
     * DOCUMENT ME!
     *
     * @param db DOCUMENT ME!
     * @param word DOCUMENT ME!
     */
    private void fillResultText(String db, String word) {
        try {
            String definition = ((DictServerInfo) cboServerName.getSelectedItem()).getDefinition(db,
                    word);
            definition = addBR(definition, "\r\n");
            definition = addBR(definition, "\n");
            definition = addLinks(definition);
            txtResult.setText(
                "<html><div style=\"FONT-FAMILY: Verdana, Tahoma; FONT-SIZE: 12pt;\">" +
                definition + "</div></html>");
        } catch (DictException e) {
            JOptionPane.showMessageDialog(frmMain, e.toString(), TITLE_ERROR,
                JOptionPane.WARNING_MESSAGE);
            System.out.println(e.toString());
        }
    } // END: fillResultText(String db, String word)

    /**
     * Ulozeni nalezene definice
     */
    private void save() {
        JFileChooser dlgSave = new JFileChooser();
        dlgSave.setDialogTitle("Save");
        dlgSave.setCurrentDirectory(new File(
                (String) System.getProperties().get("user.dir")));

        int result = dlgSave.showSaveDialog(frmMain);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            File f = dlgSave.getSelectedFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                        new FileWriter(f)));
            out.println(txtResult.getText());
            out.flush();
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frmMain, e.toString(), TITLE_ERROR,
                JOptionPane.WARNING_MESSAGE);
            System.out.println(e.toString());
        }
    } // END: save()

    /**
     * DOCUMENT ME!
     */
    private void about() {
        StringBuffer msg = new StringBuffer();
        msg.append(
            "<html><div style=\"FONT-FAMILY: Verdana, Tahoma; FONT-SIZE: 12pt;\">");
        msg.append(
            "<strong>Application:</strong> JDictClient - dict server client<br>");
        msg.append("<strong>Version:</strong> 0.1.5 (2001-11-21)<br>");
        msg.append("<strong>Author:</strong> �t�p�n Bechynsk�<br>");
        msg.append("<strong>E-mail:</strong> bechynsk@seznam.cz<br>");
        msg.append("<strong>Licence:</strong> GNU GPL<br>");
        msg.append("</div></html>");
        JOptionPane.showMessageDialog(frmMain, msg, "About",
            JOptionPane.INFORMATION_MESSAGE);
    } // END: about()

    /**
     * Seznam souvicejicich slov
     *
     * @param text vyklad slova
     *
     * @return DOCUMENT ME!
     */
    private String addLinks(String text) {
        int left = 0; // index {
        int right = 0; // index }
        int last = 0;

        StringBuffer newText = new StringBuffer();

        while ((left = text.indexOf("{", right)) > -1) {
            right = text.indexOf("}", ++left);

            if (right < left) {
                break;
            }

            String link = text.substring(left, right);
            newText.append(text.substring(last, left));
            newText.append("<a href=\"http://");
            newText.append(link);
            newText.append("\">");
            newText.append(link);
            newText.append("</a>");
            last = right;
        }

        newText.append(text.substring(last));

        return newText.toString();
    } // END: getLinks(String text)

    /**
     * Zameni znak konce radku za <br>
     *
     * @param text
     * @param eol DOCUMENT ME!
     *
     * @return String
     */
    private String addBR(String text, String eol) {
        final String BR = "<br>";
        int indexOfEOL = 0;
        int last = 0;
        StringBuffer newText = new StringBuffer();

        while ((indexOfEOL = text.indexOf(eol, last)) > -1) {
            newText.append(text.substring(last, indexOfEOL));
            newText.append(BR);
            last = indexOfEOL + eol.length();
        }

        newText.append(text.substring(last));

        return newText.toString();
    } // END: addBR(String text)
}
