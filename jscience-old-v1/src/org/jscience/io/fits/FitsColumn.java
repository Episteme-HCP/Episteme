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

package org.jscience.io.fits;

/**
 * Holds the information about a column in an ASCII or binary table.
 */
public abstract class FitsColumn {
    /** DOCUMENT ME! */
    private int number;

    /** DOCUMENT ME! */
    private String name;

    /** the TFORM keyword value for this column */
    protected String form;

    /** the one character type code parsed from the TFORM  keyword */
    protected char type;

    /** the repeat count parsed from the TFORM keyword */
    protected int count;

    /** the offset for column scaling */
    private double zero;

    /* the scale factor for column scaling */
    /** DOCUMENT ME! */
    private double scale;

    /** DOCUMENT ME! */
    protected Class representation;

    /** DOCUMENT ME! */
    protected int bytes;

    /** the value of the TUNIT keyword for this column */
    String units;

/**
     * Create a column object from the information in an HDU header, for a
     * given column. This is the one case where the column number counts from
     * "1" as it does in the FITS keywords.
     *
     * @param header the FITS header from which to extract column information
     * @param col    the column to read from the FITS file. Note: this value
     *               counts from "1" unlike most other column indices in this
     *               package.
     * @throws FitsException if there is a problem with the FITS format.
     */
    public FitsColumn(FitsHeader header, int col) throws FitsException {
        this.number = number;

        name = header.card("TTYPE" + col).stringValue();
        form = header.card("TFORM" + col).stringValue();

        try {
            units = header.card("TUNIT" + col).stringValue();
        } catch (NoSuchFitsCardException e) {
            units = null;
        }

        try {
            scale = header.card("TSCALE" + col).doubleValue();
        } catch (NoSuchFitsCardException e) {
            scale = 1.0;
        }

        try {
            zero = header.card("TZERO" + col).doubleValue();
        } catch (NoSuchFitsCardException e) {
            zero = 0.0;
        }

        //     /************************
        //     * parse the form string *
        //     ************************/
        //     int i=1;
        //     for( ; i<=form.length(); ++i) {
        //         try { count = Integer.parseInt(form.substring(0,i));
        //     }
        //         catch(NumberFormatException e) {
        //             if(i==1) count=1;
        //             break;
        //         }
        //     }
        //
        //     --i;
        //
        //     /**************************
        //     * read the next character *
        //     **************************/
        //     try { type = form.charAt(i); }
        //     catch(IndexOutOfBoundsException e) {
        //         throw new FitsException("Can't parse TFORM"+col+" "+form);
        //     }
        //
        //     /******************************************************
        //     * special handling if this is a variable length array *
        //     ******************************************************/
        //     if(type=='P') {
        //         count=-1;
        //
        //         /***********************************************
        //         * read the actual type from the next character *
        //         ***********************************************/
        //         ++i;
        //         try { type = form.charAt(i); }
        //         catch(IndexOutOfBoundsException e) {
        //             throw new FitsException("Can't parse TFORM"+col+" "+form);
        //         }
        //
        //         /***********************************************
        //         * after this comes the (optional) max dimension
        //         * in parenthesis, but we won't worry about that
        //         * for now
        //         ************************************************/
        //     } // end if this is a variable length array column
        //
        //     /*****************************
        //     * treat bit columns as bytes *
        //     *****************************/
        //     if(type == 'X') count = (count+7)/8;
        //
        //     /******************************
        //     * treat complex columns as arrays *
        //     **********************************/
        //     if(type == 'C' || type == 'M') count *= 2;
        //
        //     /*************************************
        //     * determine the representation class *
        //     *************************************/
        //     try {
        //         if(     type == 'L') representation=Class.forName("java.lang.Boolean");
        //         else if(type == 'X') representation=Class.forName("java.lang.Byte");
        //         else if(type == 'A') representation=Class.forName("java.lang.String");
        //         else if(type == 'B') representation=Class.forName("java.lang.Integer");
        //         else if(type == 'I') representation=Class.forName("java.lang.Short");
        //         else if(type == 'J') representation=Class.forName("java.lang.Integer");
        //         else if(type == 'E') representation=Class.forName("java.lang.Float");
        //         else if(type == 'D') representation=Class.forName("java.lang.Double");
        //         else if(type == 'C') representation=Class.forName("java.lang.Float");
        //         else if(type == 'M') representation=Class.forName("java.lang.Double");
        //         else throw new FitsException("Unsupported data type "+type+" in "+this);
        //     } catch(ClassNotFoundException e) {
        //     System.err.println(e);
        //         throw new FitsException("Unknown class in "+this);
        //     }
        //
        //     if(     type == 'L') bytes=1;
        //     else if(type == 'X') bytes=1;
        //     else if(type == 'A') bytes=1;
        //     else if(type == 'B') bytes=1;
        //     else if(type == 'I') bytes=2;
        //     else if(type == 'J') bytes=4;
        //     else if(type == 'E') bytes=4;
        //     else if(type == 'D') bytes=8;
        //     else if(type == 'C') bytes=4;
        //     else if(type == 'M') bytes=8;
        //     else throw new FitsException("Unsupported data type "+type+" in "+this);
    } // end of constructor

    /**
     * returns the Java class used to represent raw unscaled values in
     * this column
     *
     * @return DOCUMENT ME!
     */
    public Class rawRepresentation() {
        return representation;
    }

    /**
     * returns the Java class used to represent values in this column
     * after scaling has been applied.
     *
     * @return DOCUMENT ME!
     */
    public Class scaledRepresentation() {
        if (!isScaled()) {
            return rawRepresentation();
        }

        if ((representation == Double.class) ||
                (representation == Float.class) ||
                ((double) ((int) scale) != scale) ||
                ((double) ((int) zero) != zero)) {
            return Double.class;
        }

        return Long.class;
    } // scaledRepresentation

    /**
     * returns a String representing the physical units for this column
     * or null if none are specified in the FITS file
     *
     * @return DOCUMENT ME!
     */
    public String getUnits() {
        return units;
    }

    /**
     * returns a String representing the raw undecorated column name.
     * or NULL if none are specified in the FITS file
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the repeat count for this column. This is the number of
     * vector elements or 1 for string columns.
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        if (type == 'A') {
            return 1;
        } else {
            return count;
        }
    }

    /**
     * returns the width of this column in bytes, in the main table.
     * This means it returns 128 for variable length columns because that is
     * the size of the data pointers.
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        if (count == -1) {
            return 8; // variable length column
        } else {
            return count * bytes;
        }
    }

    /**
     * returns the size in bytes of a single data element.
     *
     * @return DOCUMENT ME!
     */
    public int getElementSize() {
        return bytes;
    }

    /**
     * convert an object into one of the official representation for
     * this column. Returns null for an unknown column type. If converting to
     * a boolean, returns false if the integer representation of the number is
     * zero, and true otherwise.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(Number value) {
        if (value.getClass() == representation) {
            return value;
        }

        if (representation == Byte.class) {
            return new Byte(value.byteValue());
        } else if (representation == Short.class) {
            return new Short(value.shortValue());
        } else if (representation == Integer.class) {
            return new Integer(value.intValue());
        } else if (representation == Float.class) {
            return new Float(value.floatValue());
        } else if (representation == Double.class) {
            return new Double(value.doubleValue());
        } else if (representation == String.class) {
            return value.toString();
        } else if (representation == Boolean.class) {
            return new Boolean(value.doubleValue() != 0.0);
        }

        return null;

        // if(     type == 'X' ) return new Byte(    value.byteValue()   );
        // else if(type == 'B' ) return new Integer( value.intValue()    );
        // else if(type == 'I' ) return new Short(   value.shortValue()  );
        // else if(type == 'J' ) return new Integer( value.intValue()    );
        // else if(type == 'E' ) return new Float(   value.floatValue()  );
        // else if(type == 'D' ) return new Double(  value.doubleValue() );
        // else if(type == 'C' ) return new Float(   value.floatValue()  );
        // else if(type == 'M' ) return new Double(  value.doubleValue() );
        // else if(type == 'A' ) return value.toString();
        // else if(type == 'L' ) {
        //     if(value.intValue() == 0 ) return new Boolean(false);
        //     else                       return new Boolean(true );
        // }
        //
        // return null;
    } // end of represent number method

    /**
     * convert an object into one of the official representation for
     * this column. It returns null for an unknown column type
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(String value) {
        if (representation == String.class) {
            return value;
        }

        //  System.out.println("representing |"+value+"| as "+representation);
        if (value.length() == 0) {
            return null;
        }

/**
         * If we get here we are assuming that we are converting to a number
         * strip a leading plus sign, because Java is apparently not clever
         * enough to recognize it when parsing
         */
        if (value.charAt(0) == '+') {
            value = value.substring(1);
        }

        if (representation == Byte.class) {
            return new Byte(value);
        } else if (representation == Short.class) {
            return new Short(value);
        } else if (representation == Integer.class) {
            return new Integer(value);
        } else if (representation == Float.class) {
            return new Float(value);
        } else if (representation == Double.class) {
            return new Double(value);
        } else if (representation == String.class) {
            return value.toString();
        } else if (representation == Boolean.class) {
            return new Boolean(value);
        }

        return null;

        //     if(     type == 'X' ) return new Byte(    value );
        //     else if(type == 'B' ) return new Integer( value );
        //     else if(type == 'I' ) return new Short(   value );
        //     else if(type == 'J' ) return new Integer( value );
        //     else if(type == 'E' ) return new Float(   value );
        //     else if(type == 'D' ) return new Double(  value );
        //     else if(type == 'C' ) return new Float(   value );
        //     else if(type == 'M' ) return new Double(  value );
        //     else if(type == 'L' ) return new Boolean( value );
        //     else if(type == 'A' ) return value;
        //
        //     return null;
    } // end of represent String method

    /**
     * convert an object into one of the official representation for
     * this column. If the native type is a number, then false is represented
     * as zero and true is represented as one.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(Boolean value) {
        if (representation == Boolean.class) {
            return value;
        } else if (representation == String.class) {
            return value.toString();
        }

        if (value.booleanValue()) {
/**
             * true
             */
            if (representation == Byte.class) {
                return new Byte((byte) 1);
            } else if (representation == Short.class) {
                return new Short((short) 1);
            } else if (representation == Integer.class) {
                return new Integer(1);
            } else if (representation == Float.class) {
                return new Float(1.0);
            } else if (representation == Double.class) {
                return new Double(1.0);
            }
        } else {
/**
             * false
             */
            if (representation == Byte.class) {
                return new Byte((byte) 0);
            } else if (representation == Short.class) {
                return new Short((short) 0);
            } else if (representation == Integer.class) {
                return new Integer(0);
            } else if (representation == Float.class) {
                return new Float(0.0);
            } else if (representation == Double.class) {
                return new Double(0.0);
            }
        }

        return null;

        //     if(type == 'L' ) return value;
        //     else if(type == 'A' ) return value.toString();
        //     else {
        //         if(value.booleanValue()) {
        //             if(     type == 'X' ) return new Byte(     (byte)1  );
        //             else if(type == 'B' ) return new Integer(        1  );
        //             else if(type == 'I' ) return new Short(   (short)1  );
        //             else if(type == 'J' ) return new Integer(        1  );
        //             else if(type == 'E' ) return new Float(          1. );
        //             else if(type == 'D' ) return new Double(         1. );
        //         } else {
        //             if(     type == 'X' ) return new Byte(     (byte)0  );
        //             else if(type == 'B' ) return new Integer(        0  );
        //             else if(type == 'I' ) return new Short(   (short)0  );
        //             else if(type == 'J' ) return new Integer(        0  );
        //             else if(type == 'E' ) return new Float(          0. );
        //             else if(type == 'D' ) return new Double(         0. );
        //         }
        //     }
        //
        //     return null;
    } // end of represent Boolean method

    /**
     * convert an object into one of the official representation for
     * this column. If the object is a number or boolean the special represent
     * methods for thoses classes is used. Otherwise the string method is used
     * with the string representation of the value.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(Object value) {
        if (value instanceof Number) {
            return represent((Number) value);
        }

        if (value instanceof Boolean) {
            return represent((Boolean) value);
        }

        return represent(value.toString());
    } // end of represent Object method

    /**
     * convert a primitive type into the official representation for
     * this column
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(byte value) {
        return represent(new Byte(value));
    }

    /**
     * convert a primitive type into the official representation for
     * this column
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(int value) {
        return represent(new Integer(value));
    }

    /**
     * convert a primitive type into the official representation for
     * this column
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(long value) {
        return represent(new Long(value));
    }

    /**
     * convert a primitive type into the official representation for
     * this column
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(float value) {
        return represent(new Float(value));
    }

    /**
     * convert a primitive type into the official representation for
     * this column
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object represent(double value) {
        return represent(new Double(value));
    }

    /**
     * returns true if the column has a number value and there is an
     * adjustment specified by the TSCALEn or TZEROn keywords.
     *
     * @return DOCUMENT ME!
     */
    public boolean isScaled() {
        if ((scale == 1.0) && (zero == 0.0)) {
            return false;
        }

        if (!((Number.class).isAssignableFrom(representation))) {
            return false;
        }

        return true;
    }

    /**
     * Apply the column scaling to a value. String columns have
     * trailing blanks stripped off, but otherwise non-number values remain
     * unchanged.
     *
     * @param raw the unscaled value
     *
     * @return the scaled value
     */
    public Object scale(Object raw) {
        if (representation == String.class) {
            /** strip trailing blanks off of strings */
            String string = (String) raw;
            int end = string.length() - 1;

            while ((end >= 0) && (string.charAt(end) == ' '))
                --end;

            return string.substring(0, end + 1);
        }

        if (!isScaled()) {
            return raw;
        }

        if (!(raw instanceof Number)) {
            return raw;
        }

        Number num = (Number) raw;

        if (scaledRepresentation() == Double.class) {
/**
             * represent the result as a double
             */
            return new Double((num.doubleValue() * scale) + zero);
        } else {
/**
             * represent the result as a long
             */
            return new Long((long) ((num.longValue() * scale) + zero));
        }
    } // end of scale method

    /**
     * reverse the scaling process. Non-numbers are unchanged by this
     * method.
     *
     * @param scaled ascaled value.
     *
     * @return the unscaled value which would scale to the scaled value.
     */
    public Object unscale(Object scaled) {
        if (!isScaled()) {
            return scaled;
        }

        if (!(scaled instanceof Number)) {
            return scaled;
        }

        Number num = (Number) scaled;

        return represent((num.doubleValue() - zero) / scale);
    } // end of scale method
} // end of FitsColumn class
