package org.jscience.io.fits;

/**
 * Represents a column in a BINTABLE extension
 */
public class FitsBinaryColumn extends FitsColumn {
/**
     * Create a new column object which will represent the given column in the
     * given FITS header.
     *
     * @param header A FITS header describing the column
     * @param col    the column to read from the FITS file. Note: this value
     *               counts from "1" unlike most other column indices in this
     *               package.
     * @throws FitsException DOCUMENT ME!
     */
    public FitsBinaryColumn(FitsHeader header, int col)
        throws FitsException {
        super(header, col);

        /** parse the form string */
        int i = 1;

        for (; i <= form.length(); ++i) {
            try {
                count = Integer.parseInt(form.substring(0, i));
            } catch (NumberFormatException e) {
                if (i == 1) {
                    count = 1;
                }

                break;
            }
        }

        --i;

/**
         * read the next character
         */
        try {
            type = form.charAt(i);
        } catch (IndexOutOfBoundsException e) {
            throw new FitsException("Can't parse TFORM" + col + " " + form);
        }

/**
         * special handling if this is a variable length array
         */
        if (type == 'P') {
            count = -1;

/**
             * read the actual type from the next character
             */
            ++i;

            try {
                type = form.charAt(i);
            } catch (IndexOutOfBoundsException e) {
                throw new FitsException("Can't parse TFORM" + col + " " + form);
            }

/**
             * after this comes the (optional) max dimension in parenthesis,
             * but we won't worry about that for now
             */
        } // end if this is a variable length array column

/**
         * treat bit columns as bytes
         */
        if (type == 'X') {
            count = (count + 7) / 8;
        }

/**
         * treat complex columns as arrays
         */
        if ((type == 'C') || (type == 'M')) {
            count *= 2;
        }

/**
         * determine the representation class
         */
        try {
            if (type == 'L') {
                representation = Class.forName("java.lang.Boolean");
            } else if (type == 'X') {
                representation = Class.forName("java.lang.Byte");
            } else if (type == 'A') {
                representation = Class.forName("java.lang.String");
            } else if (type == 'B') {
                representation = Class.forName("java.lang.Integer");
            } else if (type == 'I') {
                representation = Class.forName("java.lang.Short");
            } else if (type == 'J') {
                representation = Class.forName("java.lang.Integer");
            } else if (type == 'E') {
                representation = Class.forName("java.lang.Float");
            } else if (type == 'D') {
                representation = Class.forName("java.lang.Double");
            } else if (type == 'C') {
                representation = Class.forName("java.lang.Float");
            } else if (type == 'M') {
                representation = Class.forName("java.lang.Double");
            } else {
                throw new FitsException("Unsupported data type " + type +
                    " in " + this);
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            throw new FitsException("Unknown class in " + this);
        }

        if (type == 'L') {
            bytes = 1;
        } else if (type == 'X') {
            bytes = 1;
        } else if (type == 'A') {
            bytes = 1;
        } else if (type == 'B') {
            bytes = 1;
        } else if (type == 'I') {
            bytes = 2;
        } else if (type == 'J') {
            bytes = 4;
        } else if (type == 'E') {
            bytes = 4;
        } else if (type == 'D') {
            bytes = 8;
        } else if (type == 'C') {
            bytes = 4;
        } else if (type == 'M') {
            bytes = 8;
        } else {
            throw new FitsException("Unsupported data type " + type + " in " +
                this);
        }
    } // end of constructor
} // end of FitsBinaryColumn class
