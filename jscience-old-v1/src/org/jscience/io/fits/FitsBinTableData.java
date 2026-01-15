package org.jscience.io.fits;

import java.io.EOFException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Represents BINTABLE format data. The superclass handles basic column
 * information common between ASCII and binary tables and casts the data as a
 * Java TableModel. This class handles the actual reading of the data. You use
 * the TableModel getValueAt and setValueAt methods to interact with the
 * table. These are geared toward use in a JTable, so they discard exceptions,
 * and do not give alot of support for vector columns. I should probably add a
 * few methods to compensate for this. The number of elements in a variable
 * length vector column can't be changed That could be fixed, but there are
 * some tricky issues to shuffling the heap around efficiently.
 */
public class FitsBinTableData extends FitsTableData {
/**
     * Create a new table from the given header.
     *
     * @param header HDU header whose keywords are used to set up the table
     *               structure. Note that the header is not saved internally, so
     *               modifications to the header after this constructor is called do
     *               not affect objects of this class.
     * @throws FitsException DOCUMENT ME!
     */
    public FitsBinTableData(FitsHeader header) throws FitsException {
        super(header);

/**
         * read the column information
         */
        for (int i = 1; i <= ncolumns; ++i) {
            columns.add(new FitsBinaryColumn(header, i));
        }

        /** determine the start byte for each column */
        int offset = 0;

        for (Iterator it = columns.iterator(); it.hasNext();) {
            FitsColumn col = (FitsColumn) it.next();

            offsets.put(col, new Integer(offset));
            offset += col.getWidth();
        }
    } // end of constructor

    /**
     * read an element of a given data type from the table, starting at
     * the current position in the data.
     *
     * @param col the column object corresponding to the desired column.
     *
     * @return the value stored in this column. This can be a Number, Boolean,
     *         or a String,
     *
     * @throws FitsException is there is an error in the FITS format.
     */
    private Object readNextElement(FitsColumn col) throws FitsException {
        char type = col.type;

        try {
            if (type == 'L') {
                /** logicals are encoded as T/F */
                byte code = interpreter.readByte();

                if (code == (byte) 'T') {
                    return Boolean.TRUE;
                } else if (code == (byte) 'F') {
                    return Boolean.FALSE;
                } else {
                    return null;
                }
            } else if (type == 'X') {
                return new Byte(interpreter.readByte());
            } else if (type == 'A') {
                /** read the string */
                byte[] buffer = new byte[col.getWidth()];
                interpreter.readFully(buffer);

                String string = new String(buffer);

                /** trim trailing ASCII nulls */
                int index = string.indexOf('\0');

                if (index >= 0) {
                    string = string.substring(0, index);
                }

                return string;
            } else if (type == 'B') {
                return new Integer(interpreter.readUnsignedByte());
            } else if (type == 'I') {
                return new Short(interpreter.readShort());
            } else if (type == 'J') {
                return new Integer(interpreter.readInt());
            } else if (type == 'E') {
                return new Float(interpreter.readFloat());
            } else if (type == 'D') {
                return new Double(interpreter.readDouble());
            } else if (type == 'C') {
                return new Float(interpreter.readFloat());
            } else if (type == 'M') {
                return new Double(interpreter.readDouble());
            } else {
                throw new FitsException("Can't read data type " + type +
                    " from " + this);
            }
        } catch (EOFException e) {
            throw new FitsException("No such element in " + this);
        } catch (FitsException e) {
            throw e;
        } catch (IOException e) {
            throw new FitsException("Couldn't get element in " + this);
        }
    } // end of readNextElement method

    /**
     * set an element of a given data type from the table at the
     * current position in the data. This method assumes the value is
     * represented with a class which is compatible with the official
     * representation of the column.
     *
     * @param col DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws FitsException if this method is given an incompatible data
     *         class.
     */
    private void setNextElement(FitsColumn col, Object value)
        throws FitsException {
        char type = col.type;

        try {
            if (type == 'L') {
/**
                 * logicals are encoded as T/F
                 */
                if (value == null) {
                    setter.writeByte(0);
                } else {
                    Boolean boo = (Boolean) value;

                    if (boo.booleanValue()) {
                        setter.writeByte((byte) 'T');
                    } else {
                        setter.writeByte((byte) 'F');
                    }
                }
            } else if (type == 'X') {
                setter.writeByte(((Number) value).intValue());
            } else if (type == 'A') {
                /** trim to fit and pad with blanks */
                String s = (String) value;

                if (s.length() > col.count) {
                    s = s.substring(0, col.count);
                }

                if (s.length() < col.count) {
                    /** need to pad with ASCII nulls */
                    int npad = col.count - s.length();
                    StringBuffer buf = new StringBuffer(npad);

                    for (int i = 0; i < npad; ++i) {
                        buf.append('\0');
                    }

                    s = s + buf;
                }

                setter.writeBytes(s);
            } else if (type == 'B') {
                setter.writeByte(((Number) value).intValue());
            } else if (type == 'I') {
                setter.writeShort(((Number) value).intValue());
            } else if (type == 'J') {
                setter.writeInt(((Number) value).intValue());
            } else if (type == 'E') {
                setter.writeFloat(((Number) value).floatValue());
            } else if (type == 'D') {
                setter.writeDouble(((Number) value).doubleValue());
            } else if (type == 'C') {
                setter.writeFloat(((Number) value).floatValue());
            } else if (type == 'M') {
                setter.writeDouble(((Number) value).doubleValue());
            } else {
                throw new FitsException("Can't write data type " + type +
                    " from " + this);
            }
        } catch (ClassCastException e) {
            throw new FitsException("Incompatible data type " +
                value.getClass().getName() + " for column " + col + " (" +
                col.rawRepresentation().getName() + ") " + "in " + this);
        } catch (IOException e) {
            throw new FitsException("Couldn't set element in " + this);
        }
    } // end of setNextElement method

    /**
     * Return the value for a given row and column. This method is to
     * satisfy the contract for the TableModel interface It catches
     * FitsExceptions and returns a null pointer if there is an error.
     *
     * @param row the desired row counting from zero
     * @param col the desired column counting from zero.
     *
     * @return the tabel value. This can be a Number, Boolean, String, List or
     *         null. Lists are used to return vector elements and null is
     *         returned if there is an error in the FITS format of the table.
     */
    public Object getValueAt(int row, int col) {
        /** locate the start of the data */
        FitsColumn column = getColumn(col);
        goToElement(row, column);

        try {
            /** if the column is a scalar, just return the element */
            int count = column.getCount();

            if (count == 1) {
                return column.scale(readNextElement(column));
            }

/**
             * special handling for variable length arrays
             */
            if (count < 0) {
                count = interpreter.readInt();

                long offset = interpreter.readInt();

                offset += (nrows * bytes_per_row);

                goToByte(offset);
            }

            /**
             * if we get here, the column is a vector, so
             * return all the elements in a List
             */

            //System.out.println("column "+col+" row "+row+" is a vector count="+count);
            List list = new ArrayList();

            for (int i = 0; i < count; ++i) {
                list.add(column.scale(readNextElement(column)));

                //System.out.println("    element "+i+" = "+list.get(list.size()-1) );
            }

            return list;
        } catch (IOException e) {
            return null;
        }
    } // end of getValueAt method

    /**
     * Set a value in the table. This method is to satisfy the contract
     * for the TableModel interface. It makes its best attempt to interpret
     * the value object in a format appropriate to the column. In particular,
     * strings are parsed as numbers or booleans if need be, and vector
     * columns can be specified as a bracketed, comma-separated list of
     * strings (i.e. [value1, value2, value2, ...] ). In general, this method
     * should accept the data representations from a JTable displaying this
     * table. This method should probably be modified to accept a List or
     * array for vector columns, but that has yet to be necessary. If the
     * value cannot be properly formatted, or if there is trouble with the
     * FITS format, this method silently ignores the requested update.
     *
     * @param value This is the value to write to the table.
     * @param row the row index counting from zero.
     * @param col the column index counting from zero.
     */
    public void setValueAt(Object value, int row, int col) {
        /** locate the position in the data where we should start writing */
        FitsColumn column = getColumn(col);
        goToElement(row, column);

        try {
            long count = column.getCount();

            if (count != 1) {
                /** this is a vector so write all the elements */
                String string = value.toString();

/**
                 * special handling for variable length arrays
                 */
                if (count < 0) {
                    count = interpreter.readInt();

                    int offset = interpreter.readInt();

                    offset += (nrows * bytes_per_row);
                    goToByte(offset);
                }

                /** find the first character which is not a space or a bracket */
                int first = 0;

                while (first < string.length()) {
                    char c = string.charAt(first);

                    if ((c != ' ') && (c != '[')) {
                        break;
                    }

                    ++first;
                }

                /** find the last character which is not a space or a bracket */
                int last = string.length() - 1;

                while (last >= 0) {
                    char c = string.charAt(last);

                    if ((c != ' ') && (c != ']')) {
                        break;
                    }

                    last--;
                }

/**
                 * trim the string
                 */
                string = string.substring(first, last + 1);

                /** break the trimmed string into tokens */
                StringTokenizer tokens = new StringTokenizer(string, ",");

                for (int i = 0; (i < count) && tokens.hasMoreTokens(); ++i) {
                    String token = tokens.nextToken();
                    setNextElement(column,
                        column.represent(column.unscale(token)));
                }
            } else {
/**
                 * this is a scalar, so we can just write it
                 */
                setNextElement(column, column.represent(column.unscale(value)));
            }

            fireTableModelEvent(row, col);
        } catch (IOException e) {
        } catch (NumberFormatException e) {
        }
    } // end of getValueAt method
} // end of FitsBinTableData class
