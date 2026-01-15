package org.jscience.io.fits;

import java.util.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


/**
 * This abstract class represents the data in either a FITS ASCII table or
 * a FITS BINTABLE. The data implement the Swing TableModel interface, so they
 * can be accessed in a way natural to Java. In particular, an object of this
 * class may be displayed in a JTable component. Rows and columns are numbered
 * starting at zero, as is natural in Java. People used to the FORTRAN
 * convention of numbering from "1" used in the FITS files themselves may need
 * to be careful.
 */
public abstract class FitsTableData extends FitsData implements TableModel {
    /** the number of columns in the table */
    int ncolumns;

    /** the number of rows in the table */
    int nrows;

    /** the width of the table in bytes */
    int bytes_per_row;

    /** a list of all the columns names */
    List columns;

    /** the byte offset of each column within a row */
    Map offsets;

    /** DOCUMENT ME! */
    private Set listeners;

/**
     * Create a table object, reading the column information from the given
     * header
     *
     * @param header the HDU header used to extract column information
     * @throws FitsException if the FITS format is incorrect.
     */
    public FitsTableData(FitsHeader header) throws FitsException {
/**
         * allocate the data array
         */
        super(header.dataSize());

        bytes_per_row = header.card("NAXIS1").intValue();
        nrows = header.card("NAXIS2").intValue();
        ncolumns = header.card("TFIELDS").intValue();

        columns = new ArrayList(ncolumns);
        offsets = new HashMap(ncolumns);
        listeners = new HashSet();
    } // end of constructor

    /**
     * returns the column object for a given column number.
     *
     * @param col the column number - the first column is numbered "0", despite
     *        the fact that it is numbered "1" in the FITS file itself.
     *
     * @return the specified column.
     */
    public FitsColumn getColumn(int col) {
        return (FitsColumn) columns.get(col);
    }

    /**
     * returns the offset of the first byte of a column within a row
     *
     * @param column the column object representing the desired column
     *
     * @return the number of bytes into a row where the given column starts.
     */
    private int getOffset(FitsColumn column) {
        Integer offset = (Integer) offsets.get(column);

        return offset.intValue();
    } // end of getColumn

    /**
     * returns the offset of the first byte of a given table element
     *
     * @param row the desired row of the column, with the first row numbered
     *        "0".
     * @param column the column object corresponding to the desired column.
     *
     * @return DOCUMENT ME!
     */
    private long elementLocation(int row, FitsColumn column) {
        return (row * bytes_per_row) + getOffset(column);
    } // end of elementLocation method

    /**
     * reposition the reader to read the given table element
     *
     * @param row the desired row of the column, with the first row numbered
     *        "0".
     * @param column the column object corresponding to the desired column.
     */
    protected void goToElement(int row, FitsColumn column) {
        goToByte(elementLocation(row, column));
    } // end of goToElement method

    /**
     * add a listener to the table which will be alerted when the table
     * changes
     *
     * @param l the listener to add
     */
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    /**
     * remove a listener from the table.
     *
     * @param l the listener to remove
     */
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    /**
     * send a TableModelEvent to all the listeners. Note a listener
     * should not remove iteself in its tableChanged method.
     *
     * @param e DOCUMENT ME!
     */
    protected void fireTableModelEvent(TableModelEvent e) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            TableModelListener listener = (TableModelListener) it.next();

            listener.tableChanged(e);
        } // end of loop over listeners
    } // end of fireTableModelEvent method

    /**
     * send a TableModelEvent to all the listeners that a given cell
     * has changed.
     *
     * @param row - the row which has changed. The first row is "0".
     * @param col - the columns wich has changed. The first columns is "0".
     */
    protected void fireTableModelEvent(int row, int col) {
        fireTableModelEvent(new TableModelEvent(this, row, row, col));
    } // end of fireTableModelEvent method

    /**
     * returns the Java class of the data in a column
     *
     * @param col - the index of the desired column. The first column is
     *        numbered "0"
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int col) {
        FitsColumn column = getColumn(col);

        if (column.getCount() == 1) {
            return getColumn(col).scaledRepresentation();
        } else {
            return Object.class;
        }
    }

    /**
     * returns the number of columns in the table
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return ncolumns;
    }

    /**
     * returns the name of a given column. This is a name suitable for
     * display in a table header. It may have the units of the column appended
     * to the column nam egiven in the TTYPEn keyword. To get the raw column
     * name without decoration use {@link #getColumn(int)} to get the column
     * object and then {@link FitsColumn#getName()}.
     *
     * @param col the desired column. The first column is numbered "0".
     *
     * @return the decorated name of the column suitible for display
     */
    public String getColumnName(int col) {
        FitsColumn column = getColumn(col);

        String name = column.getName();
        String units = column.getUnits();

        if ((units == null) || units.equals(" ")) {
            return name;
        } else {
            return name + " (" + units + ")";
        }
    } // end of getColumnName method

    /**
     * returns the column number for a given column name, or -1 if
     * there is no such column in the table. The name is the raw undecorated
     * column name in the FITS TTYPEn keyword.
     *
     * @param name the undecorated name of the desired column
     *
     * @return the index of the desired columns, or -1 if there is no such
     *         column. The first column is numbered "0".
     */
    public int findColumn(String name) {
        for (int i = 0; i < ncolumns; ++i) {
            if (getColumn(i).getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * returns the number of rows in the table
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return nrows;
    }

    /**
     * always returns true. This could be modified in the future if we
     * wish to mark some tables as read-only.
     *
     * @param row the row number counting from zero.
     * @param col the column number counting from zero.
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int row, int col) {
        return true;
    }
} // end of FitsTableData class
