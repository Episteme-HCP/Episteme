package org.jscience.io.fits;

import java.io.IOException;
import java.util.Iterator;

/**
 * ******************************************************************************
 * <p/>
 * *******************************************************************************
 */
public class FitsASCIITableData extends FitsTableData {
    /**
     * ******************************************************************************
     * <p/>
     * *******************************************************************************
     */
    public FitsASCIITableData(FitsHeader header) throws FitsException {
        super(header);

        /******************************
         * read the column information *
         ******************************/
        for (int i = 1; i <= ncolumns; ++i) {
            columns.add(new FitsASCIIColumn(header, i));
        }

        int offset = 0;

        for (Iterator it = columns.iterator(); it.hasNext();) {
            FitsASCIIColumn col = (FitsASCIIColumn) it.next();

            offsets.put(col, new Integer(col.getStartByte()));
        }
    } // end of constructor

    /**
     * ***********************************************************************
     * Return the value for a given row and column.
     * This method is to satisfy the contract for the TableModel interface
     * It catches FitsExceptions and returns a null pointer if there is an error.
     *
     * @param row the desired row counting from zero
     * @param col the desired column counting from zero.
     * @return the tabel value. This can be a Number, Boolean, String, List or null.
     *         Lists are used to return vector elements and null is returned
     *         if there is an error in the FITS format of the table.
     *         ************************************************************************
     */
    public Object getValueAt(int row, int col) {
        try {
            /*******************************
             * locate the start of the data *
             *******************************/
            FitsColumn column = getColumn(col);
            goToElement(row, column);

            byte[] buffer = new byte[column.getWidth()];
            interpreter.readFully(buffer);

            String string = new String(buffer);

            /****************************
             * trim trailing ASCII nulls *
             ****************************/
            int index = string.indexOf('\0');

            if (index >= 0) {
                string = string.substring(0, index);
            }

            return column.scale(column.represent(string.trim()));
        } catch (IOException e) {
            return null;
        }
    } // end of getValueAt method

    /**
     * ******************************************************************************
     * <p/>
     * *******************************************************************************
     */
    public void setValueAt(Object value, int row, int col) {
    } // end of setValueAt method
} // end of FitsASCIITableData class
