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
