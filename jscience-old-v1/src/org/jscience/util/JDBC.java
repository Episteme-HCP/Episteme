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

package org.jscience.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;


/**
 * The class JDBC provides some simplified access to existing ODBC and
 * MySQL drivers through static methods. Although the class doesn't require
 * any additional driver software for compilation, certain methods may through
 * SQLException if the specified driver is not present at the time.
 *
 * @author Holger Antelmann
 *
 * @see JDBCTableModel
 */
public final class JDBC implements ThirdParty {
    // MM.MySQL 2.0.8 driver
    /**
     * DOCUMENT ME!
     */
    static String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    /**
     * Creates a new JDBC object.
     */
    private JDBC() {
    }

    /**
     * inserts the given values as a single row into the given table of
     * the given connection. <br>
     * It is assumed that the given vaules correspond to the columns found in
     * the table specified by the connection and the table name.
     *
     * @param con DOCUMENT ME!
     * @param tableName DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return the row count for the insert statement
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static int insert(Connection con, String tableName, Object[] values)
        throws SQLException {
        String s = "insert into " + tableName + " values (";

        for (int i = 0; i < values.length; i++) {
            s += "?, ";
        }

        s = s.substring(0, s.length() - 2) + ")";

        PreparedStatement insert = con.prepareStatement(s);

        for (int i = 0; i < values.length; i++) {
            insert.setObject(i + 1, values[i]);
        }

        int rcount = insert.executeUpdate();
        insert.close();

        //con.commit();
        return rcount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static String[] getTableNames(Connection con)
        throws SQLException {
        ResultSet set = con.getMetaData()
                           .getTables(null, null, "%", new String[] { "TABLE" });
        ArrayList<String> list = new ArrayList<String>();

        while (set.next()) {
            //list.add(set.getString("TABLE_NAME"));
            list.add(set.getString(3));
        }

        set.close();

        return list.toArray(new String[list.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static String[] getViews(Connection con) throws SQLException {
        ResultSet set = con.getMetaData()
                           .getTables(null, null, "%", new String[] { "VIEW" });
        ArrayList<String> list = new ArrayList<String>();

        while (set.next()) {
            //list.add(set.getString("TABLE_NAME"));
            list.add(set.getString(3));
        }

        set.close();

        return list.toArray(new String[list.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     * @param tableName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static ResultSet getResultSet(Connection con, String tableName)
        throws SQLException {
        String query = "select * from " + tableName;
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery(query);

        return set;
    }

    /**
     * retrieves the entire table and returns it as a Vector of rows
     * containing Vectors with the column values per row
     *
     * @param con DOCUMENT ME!
     * @param tableName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static Vector<Vector<Object>> getTable(Connection con,
        String tableName) throws SQLException {
        String query = "select * from " + tableName;
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery(query);
        Vector<Vector<Object>> result = new Vector<Vector<Object>>();
        int columns = set.getMetaData().getColumnCount();

        while (set.next()) {
            Vector<Object> row = new Vector<Object>(columns);

            for (int i = 1; i <= columns; i++) {
                row.add(set.getObject(i));
            }

            result.add(row);
        }

        st.close();

        return result;
    }

    /**
     * returns <code>getMySQLConnection("localhost", dbName)</code>
     *
     * @see #getMySQLConnection(String, String)
     */
    public static Connection getMySQLConnection(String dbName)
        throws SQLException {
        return getMySQLConnection("localhost", dbName);
    }

    /**
     * provides simplified access to a MySQL database using the MySQL
     * Connector/J 3.1 driver. If the MySQL driver is not present, this method
     * will throw a RuntimeException.
     *
     * @param host hostname; unless a port is specified, 3306 is used
     * @param dbName name of the database
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Connection getMySQLConnection(String host, String dbName)
        throws SQLException {
        try {
            Class.forName(MYSQL_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver Class not found: " +
                MYSQL_DRIVER_CLASS);
        }

        String url = "jdbc:mysql://" + host;

        if (host.indexOf(":") > 1) {
            url = url + "/" + dbName;
        } else {
            url = url + ":3306/" + dbName;
        }

        Connection connection = DriverManager.getConnection(url);

        return connection;
    }

    /**
     * provides simplified access to a MySQL database using MySQL
     * Connector/J 3.1
     *
     * @param host DOCUMENT ME!
     * @param dbName DOCUMENT ME!
     * @param login DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Connection getMySQLConnection(String host, String dbName,
        String login, String password) throws SQLException {
        try {
            Class.forName(MYSQL_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver Class not found: " +
                MYSQL_DRIVER_CLASS);
        }

        String url = "jdbc:mysql://" + host + ":3306/" + dbName;

        //Connection connection = DriverManager.getConnection(url + "?user=" + login + "&password=" + password);
        Connection connection = DriverManager.getConnection(url, login, password);

        return connection;
    }

    /**
     * provides some simplified access to existing ODBC connections
     * through Sun's JDBC/ODBC bridge
     *
     * @param dbName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Connection getODBCConnection(String dbName)
        throws SQLException {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "ODBC/JDBC Bridge Driver Class not found");
        }

        String url = "jdbc:odbc:" + dbName;
        Connection connection = DriverManager.getConnection(url);

        return connection;
    }

    /**
     * provides some simplified access to existing ODBC connections
     * through Sun's JDBC/ODBC bridge
     *
     * @param dbName DOCUMENT ME!
     * @param login DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Connection getODBCConnection(String dbName, String login,
        String password) throws SQLException {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "ODBC/JDBC Bridge Driver Class not found");
        }

        String url = "jdbc:odbc:" + dbName;
        Connection connection = DriverManager.getConnection(url, login, password);

        return connection;
    }

    /**
     * getSQLServerConnection() provides access to a Microsoft
     * SQL-Server database directly through the JDBC driver provided by
     * Microsoft
     *
     * @param host DOCUMENT ME!
     * @param dbName DOCUMENT ME!
     * @param login DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     * @throws RuntimeException DOCUMENT ME!
     */
    public static Connection getSQLServerConnection(String host, String dbName,
        String login, String password) throws SQLException {
        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "ODBC/JDBC Bridge Driver Class not found");
        }

        String url = "jdbc:microsoft:sqlserver://" + host + ":1433";
        Connection connection = DriverManager.getConnection(url + ";user=" +
                login + ";password=" + password);

        return connection;
    }

    /**
     * creates a new table in Connection <code>to</code> corresponding
     * to the given table in Connection <code>from</code> and transfers all
     * data
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param table DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static void copyTable(Connection from, Connection to, String table)
        throws SQLException {
        Statement st1 = from.createStatement();
        ResultSet set = st1.executeQuery("select * from " + table);
        Statement st2 = to.createStatement();
        String ddl = JDBC.createTableString(set.getMetaData(), table);
        //System.out.println("debug: " + ddl);
        st2.executeUpdate(ddl);

        ResultSetMetaData meta = set.getMetaData();
        String insertTemplate = "insert into " + table + " values (";
        int ccount = meta.getColumnCount();

        for (int i = 0; i < ccount; i++) {
            insertTemplate += ("?" + ((i < (ccount - 1)) ? ", " : ")"));
        }

        PreparedStatement insert = to.prepareStatement(insertTemplate);
        int n = 0;

        while (set.next()) {
            for (int i = 1; i <= ccount; i++) {
                insert.setObject(i, set.getObject(i));
            }

            insert.executeUpdate();
        }

        st1.close();
        st2.close();
    }

    /**
     * copies all tables from one connection into the other connection
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static void copyConnection(Connection from, Connection to)
        throws SQLException {
        for (String tableName : getTableNames(from)) {
            copyTable(from, to, tableName);
        }
    }

    /**
     * creates the DDL statement that would create a table identical to
     * the one denoted by the given meta data
     *
     * @param data DOCUMENT ME!
     * @param tableName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static String createTableString(ResultSetMetaData data,
        String tableName) throws SQLException {
        String ddl = "CREATE TABLE `" + tableName + "` (";

        for (int column = 1; column <= data.getColumnCount(); column++) {
            ddl += ("`" + data.getColumnName(column) + "`");
            ddl += (" " + data.getColumnTypeName(column));

            switch (data.getColumnType(column)) {
            case Types.VARCHAR:
                ddl += ("(" + data.getColumnDisplaySize(column) + ")");

                break;

            default:
            }

            //int size = data.getColumnDisplaySize(column);
            if (column < data.getColumnCount()) {
                ddl = ddl + ", ";
            }
        }

        ddl += ")";

        return ddl;
    }
}
