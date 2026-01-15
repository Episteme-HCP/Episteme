/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
class PropertiesTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 2712267251032296467L;

    /**
     * DOCUMENT ME!
     */
    Properties props;

    /**
     * Creates a new PropertiesTableModel object.
     *
     * @param props DOCUMENT ME!
     */
    public PropertiesTableModel(Properties props) {
        super();
        addColumn("key");
        addColumn("value");
        setProperties(props);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @param props DOCUMENT ME!
     */
    public synchronized void setProperties(Properties props) {
        this.props = props;

        while (getRowCount() > 0)
            removeRow(0);

        ArrayList<String> list = new ArrayList<String>();
        Enumeration<Object> e = props.keys();

        while (e.hasMoreElements())
            list.add(e.nextElement().toString());

        Collections.sort(list);

        for (String key : list) {
            addRow(new Object[]{key, props.get(key)});
        }

        //fireTableDataChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row    DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int row, int column) {
        return column == 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public int getRow(String key) {
        for (int i = 0; i < getRowCount(); i++) {
            if (key.equals(getValueAt(i, 0))) {
                return i;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value  DOCUMENT ME!
     * @param row    DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    public void setValueAt(Object value, int row, int column) {
        assert column == 1;

        Object key = getValueAt(row, 0);
        props.put(key, value);
        super.setValueAt(value, row, column);
    }
}
