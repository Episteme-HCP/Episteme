/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import java.awt.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;


/**
 * JDBCTableModel provides a simple way to display a table from a JDBC
 * connection. An additional feature is that this model is also editable.
 *
 * @author Holger Antelmann
 *
 * @see javax.swing.JTable
 * @see org.jscience.util.JDBC
 * @since 10/24/2002
 */
public class JDBCTableModel extends AbstractTableModel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2487661888487685951L;

    /** DOCUMENT ME! */
    Connection con;

    /** DOCUMENT ME! */
    String tableName;

    /** DOCUMENT ME! */
    ResultSet set;

    /** DOCUMENT ME! */
    int size;

    /** DOCUMENT ME! */
    boolean updateable;

    /** DOCUMENT ME! */
    Component parent;

    /** DOCUMENT ME! */
    Statement statement;

/**
     * Creates a new JDBCTableModel object.
     *
     * @param con       DOCUMENT ME!
     * @param tableName DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public JDBCTableModel(Connection con, String tableName)
        throws SQLException {
        this(con, tableName, false);
    }

/**
     * Creates a new JDBCTableModel object.
     *
     * @param con        DOCUMENT ME!
     * @param tableName  DOCUMENT ME!
     * @param updateable DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public JDBCTableModel(Connection con, String tableName, boolean updateable)
        throws SQLException {
        this.con = con;
        this.tableName = tableName;
        this.updateable = updateable;
        refresh();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResultSet getResultSet() {
        return set;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public void refresh() throws SQLException {
        Statement tmpst = con.createStatement();
        ResultSet sizeSet = tmpst.executeQuery("select count(*) from " +
                tableName);
        sizeSet.next();
        size = sizeSet.getInt(1);
        tmpst.close();

        if (statement != null) {
            statement.close();
        }

        statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                (updateable ? ResultSet.CONCUR_UPDATABLE
                            : ResultSet.CONCUR_READ_ONLY));
        set = statement.executeQuery("select * from " + tableName);
        fireTableDataChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditable() {
        return updateable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setEditable(boolean flag) {
        updateable = flag;
        fireTableDataChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParentComponent(Component parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getParentComponent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return updateable ? (size + 1) : size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public int getColumnCount() {
        try {
            return set.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        if (row == size) {
            return null;
        }

        try {
            set.absolute(row + 1);

            return set.getObject(column + 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int column) {
        try {
            return set.getMetaData().getColumnLabel(column + 1);
        } catch (SQLException ex) {
            return super.getColumnName(column);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Class getColumnClass(int column) {
        try {
            return Class.forName(set.getMetaData().getColumnClassName(column +
                    1));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int row, int column) {
        return updateable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void setValueAt(Object value, int row, int column) {
        if (updateable) {
            try {
                if (row == size) {
                    set.moveToInsertRow();
                    set.updateObject(column + 1, value);
                    set.insertRow();
                    set.moveToCurrentRow();
                    size += 1;

                    //fireTableRowsInserted(row, row + 1);
                } else {
                    set.absolute(row + 1);
                    set.updateObject(column + 1, value);
                    set.updateRow();

                    //fireTableRowsUpdated(row, row);
                }

                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, ex.getMessage(),
                    ex.getClass().getName(), JOptionPane.ERROR_MESSAGE, null);
            } catch (Exception ex) {
                System.err.println("debug: " + value);
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, ex.getMessage(),
                    ex.getClass().getName(), JOptionPane.ERROR_MESSAGE, null);
            } finally {
                try {
                    refresh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public void close() throws SQLException {
        statement.close();
        con.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void finalize() throws Exception {
        close();
    }
}
