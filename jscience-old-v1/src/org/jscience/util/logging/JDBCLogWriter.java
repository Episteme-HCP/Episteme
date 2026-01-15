/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.JDBC;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * JDBCLogWriter provides a LogWriter that writes to a java.sql.Connection.
 * It requires a LogEntryFormatter that maps the fields from the LogEntry
 * object to the fields of a table. The required LogEntryFormatter's
 * <code>format(LogEntry)</code> would return an Object[] that contains the
 * values that are to be inserted into the table. This implementation is only
 * suitable for those implementations where it is sufficient to store the
 * LogEntry data into a single row of a single table. Below is some sample
 * code that demonstrates how a LogEntryFormatter could be implemented to be
 * used with this class.<pre>public Object format (LogEntry entry) {
 *     Object[] value = new Object[10];
 *     value[0] = new Timestamp(entry.getTime());
 *     value[1] = entry.getLevel().toString();
 *     value[2] = entry.getMessage();    value[3] = entry.getSourceClassName();
 *     value[4] = entry.getThreadName();
 *     value[5] = (entry.getThrown() == null)? null : entry.getThrown().getMessage();
 *     value[6] = (entry.getThrown() == null)? null : entry.getThrown().getClass().getName();
 *     if (entry.getParameters() != null) {
 *         value[7] = (entry.getParameters()[0] == null)? null : entry.getParameters()[0].toString();
 *         value[8] = (entry.getParameters()[0] == null)? null : entry.getParameters()[0].toString();
 *         value[9] = (entry.getParameters()[0] == null)? null : entry.getParameters()[0].toString();
 *     }    for (int i = 0; i < value.length; i++) {
 *         if (value[i] == null) value[i] = "";    }    return value;}</pre>The
 * above code example would work with a table that contains 10 data fields
 * that would take the above returned array accordingly.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.util.JDBC#insert(Connection,String,Object[])
 */
public class JDBCLogWriter extends AbstractLogWriter {
    /** DOCUMENT ME! */
    Connection con;

    /** DOCUMENT ME! */
    String tableName;

/**
     * requires a formatter that will split log entries in an object array
     * which corresponds to the fields in the given table.
     *
     * @see org.jscience.util.JDBC#insert(Connection,String,Object[])
     */
    public JDBCLogWriter(Connection con, String tableName,
        LogEntryFormatter formatter) {
        super(formatter);
        this.con = con;
        this.tableName = tableName;
    }

    /**
     * the entry is split into an object array by the formatter which
     * is then inserted into the table. For more sophisticated storage that
     * requires more than one table (if e.g. a variable number of parameters
     * are to be stored in a separate table) a different implementation would
     * be required.
     *
     * @see org.jscience.util.JDBC#insert(Connection,String,Object[])
     */
    public synchronized void writeLogEntry(Object pattern)
        throws LogException {
        try {
            Object[] values = (Object[]) pattern;
            JDBC.insert(con, tableName, values);
        } catch (SQLException ex) {
            throw new LogException(ex);
        } catch (ClassCastException ex) {
            throw new LogException("no appropriate LogEntryFormatter used", ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Connection getConnection() {
        return con;
    }
}
