/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.JDBC;
import org.jscience.util.JDBCTableModel;
import org.jscience.util.ResultSetMetaDataTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * displays information on a JDBC connection including table's row and meta
 * data. This info pane consists of a tabbed pane that contains a tab with
 * general information about the connection and another tab with the table
 * information. The table tab itself is split into a list of all accessible
 * tables and yet another tabbed pane which lets you either display the row
 * data or the meta data for each table.
 *
 * @author Holger Antelmann
 */
public class JDBCInfoPane extends JComponent {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -6967597618576103363L;

    /**
     * DOCUMENT ME!
     */
    Connection connection;

    /**
     * DOCUMENT ME!
     */
    DatabaseMetaData data;

    /**
     * DOCUMENT ME!
     */
    JList tableNames;

    /**
     * DOCUMENT ME!
     */
    Map<String, Container> metaData = new HashMap<String, Container>();

    /**
     * DOCUMENT ME!
     */
    Map<String, Container> rowData = new HashMap<String, Container>();

    /**
     * Creates a new JDBCInfoPane object.
     *
     * @param connection DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public JDBCInfoPane(Connection connection) throws SQLException {
        // data setup
        this.connection = connection;
        data = connection.getMetaData();

        String[] tn = JDBC.getTableNames(connection);
        tableNames = new JList(tn);

        for (int i = 0; i < tn.length; i++) {
            JDBCTableModel rowModel = new JDBCTableModel(connection, tn[i]);
            rowData.put(tn[i], new JScrollPane(new JTable(rowModel)));

            ResultSetMetaDataTableModel metaModel = new ResultSetMetaDataTableModel(rowModel.getResultSet()
                    .getMetaData());
            metaData.put(tn[i], new JScrollPane(new JTable(metaModel)));
        }

        // gui setup
        setName("JDBC Info on " + data.getConnection().getCatalog());
        setLayout(new BorderLayout());

        JTabbedPane tpane = new JTabbedPane();
        tpane.addTab("general", createGeneralPane());
        tpane.addTab("tables", createtableNamesPane());
        add(tpane);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Container createGeneralPane() {
        boolean useTable = true;

        try {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("Connection Catalog name", data.getConnection().getCatalog());
            map.put("Driver name", data.getDriverName());
            map.put("Driver version",
                    data.getDriverMajorVersion() + "." +
                            data.getDriverMinorVersion());
            map.put("Database product name", data.getDatabaseProductName());
            map.put("Database product version", data.getDatabaseProductVersion());
            map.put("Database URL", data.getURL());
            map.put("user name", data.getUserName());

            if (useTable) {
                JTable jtable = Menus.makePropertiesTable(map, "property",
                        "value");

                //jtable.doLayout();
                JScrollPane scrollPane = new JScrollPane(jtable);

                return scrollPane;
            } else {
                // optional code to have an alternative to the table
                String s = "<html><font color=\"#000000\">";
                s += "<table>";

                Iterator i = map.keySet().iterator();

                while (i.hasNext()) {
                    Object obj = i.next();
                    s += ("<tr><td>" + obj + "</td>");
                    s += ("<td>" + map.get(obj) + "</td></tr>");
                }

                s += "<table>";
                s += "</html>";

                return new JLabel(s);
            }
        } catch (SQLException ex) {
            return new JLabel("Error: " + ex.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Container createtableNamesPane() {
        JPanel main = new JPanel(new BorderLayout());
        main.add(new JScrollPane(tableNames), BorderLayout.WEST);

        final JPanel metaPanel = new JPanel(new BorderLayout());
        final JPanel rowPanel = new JPanel(new BorderLayout());
        final JTabbedPane tpane = new JTabbedPane();
        tpane.addTab("meta data", metaPanel);
        tpane.addTab("row data", rowPanel);
        main.add(tpane, BorderLayout.CENTER);
        tableNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNames.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {
                if (ev.getValueIsAdjusting()) {
                    return;
                }

                if (tableNames.getSelectedIndex() < 0) {
                    return;
                }

                String table = (String) tableNames.getSelectedValue();
                metaPanel.removeAll();
                metaPanel.add((Container) metaData.get(table));
                rowPanel.removeAll();
                rowPanel.add((Container) rowData.get(table));
                tpane.repaint();
            }
        });

        return main;
    }
}
