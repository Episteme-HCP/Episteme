package org.jscience.io.fits;

import java.io.UnsupportedEncodingException;


/**
 * Represents a single header record. It's called a "card" because when
 * FITS was created, FORTRAN was the standard programming language for science
 * and the designers had punchcards in mind. A card consists of 80 ASCII
 * characters containing a key/value pair and a comment. The internal
 * structure of a card is not parsed until needed. You may construct a new
 * card by specifying its raw bytes or by specifying it's component fields. In
 * the former case the bytes are taken without question, and in the latter
 * case, the constructor forces correct FITS format.
 */
public class FitsCard {
    /** the number of bytes in a card = 80 */
    public static final int LENGTH = 80;

    /** the maximum number of bytes in a keyword = 8 */
    public static final int KEY_LENGTH = 8;

    /** the number of cards in a FITS block = 2880/80 */
    public static final int CARDS_PER_BLOCK = FitsFile.BLOCK_SIZE / LENGTH;

    /** the value to use for padding out an incomplete block (80 ASCII spaces) */
    public static final byte[] PADDING = {
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32
        };

    /** DOCUMENT ME! */
    private byte[] data;

    /** DOCUMENT ME! */
    private String key;

    /** DOCUMENT ME! */
    private boolean parsed;

    /** DOCUMENT ME! */
    private Object value;

    /** DOCUMENT ME! */
    private String comment;

/**
     * Create a new card initialized with the given data. The values are copied
     * from the array, so the caller may reuse the array.
     *
     * @param data an array of at least 80 bytes
     */
    public FitsCard(byte[] data) {
        this(data, 0);
    }

/**
     * Create a new card from the bytes in an array, starting at the given
     * offset into the array.
     *
     * @param data   an array of bytes to use for the card data.
     * @param offset the offset  into the array at which to start reading data.
     *               an offset of "0" means read from the beginning of the array
     */
    public FitsCard(byte[] data, int offset) {
/**
         * store the raw data
         */
        initializeStorage();

        System.arraycopy(data, offset, this.data, 0, LENGTH);

/**
         * read the keyword
         */
        try {
            key = new String(this.data, 0, KEY_LENGTH, "ASCII");
        } catch (UnsupportedEncodingException e) {
            System.err.println("unsupported encoding exception");
        }

        key = key.trim();
/**
         * mark that we have not yet parsed the value and comment
         */
        parsed = false;
    } // end of constructor

/**
     * Create a card by specifying only its keyword. The card will have no
     * value and a blank comment field.
     *
     * @param key the keyword for this card.
     */
    public FitsCard(String key) {
        this(key, "");
    }

/**
     * create a new card which has no value field. Use this constructor for
     * things like HISTORY and COMMENT cards.
     *
     * @param key     the keyword for the card
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, String comment) {
        initializeStorage();

        this.key = writeKeywordToData(key);
        this.comment = writeCommentToData(comment, KEY_LENGTH);
        parsed = true;
    } // end of no value constructor

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for string-valued cards. This method ensures the resulting
     * card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, String value, String comment) {
/**
         * allocate the data bytes
         */
        initializeStorage();
/**
         * keyword
         */
        this.key = writeKeywordToData(key);

        /** value */
        int index = KEY_LENGTH;
        data[index++] = (byte) '=';
        data[index++] = (byte) ' ';
        data[index++] = (byte) '\''; // open quote

        /** truncate the value if we need to */
        int max_length = LENGTH - index - 1; // minus 1 for the quote at the end

        if (value.length() > max_length) {
            value = value.substring(0, max_length);
        }

        try {
            /** copy bytes */
            byte[] buffer = value.getBytes("ASCII");

            for (int i = 0; i < buffer.length; ++i) {
                data[index++] = buffer[i];
            }
        } catch (UnsupportedEncodingException e) {
        }

        data[index++] = (byte) '\''; // close quote

        this.value = value;
/**
         * comment
         */
        this.comment = writeCommentToData(comment, index);

        parsed = true;
    } // end of string value constructor

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for boolean-valued cards. This method ensures the resulting
     * card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, boolean value, String comment) {
        this(key, new Boolean(value), comment);
    }

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for boolean-valued cards. This method ensures the resulting
     * card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, Boolean value, String comment) {
/**
         * allocate the data bytes
         */
        initializeStorage();
/**
         * keyword
         */
        this.key = writeKeywordToData(key);

        /** value */
        int index = KEY_LENGTH;
        data[index++] = (byte) '=';

        while (index < 29)
            data[index++] = (byte) ' ';

        if (value.booleanValue()) {
            data[index++] = (byte) 'T';
        } else {
            data[index++] = (byte) 'F';
        }

        this.value = value;
/**
         * comment
         */
        this.comment = writeCommentToData(comment, index);

        parsed = true;
    } // end of string value constructor

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for numberical-valued cards. This method ensures the
     * resulting card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, int value, String comment) {
        this(key, new Long(value), comment);
    }

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for numberical-valued cards. This method ensures the
     * resulting card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, long value, String comment) {
        this(key, new Long(value), comment);
    }

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for numberical-valued cards. This method ensures the
     * resulting card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, float value, String comment) {
        this(key, new Double(value), comment);
    }

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for numberical-valued cards. This method ensures the
     * resulting card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, double value, String comment) {
        this(key, new Double(value), comment);
    }

/**
     * construct a card by specifying it's keyword, value and comment. Use this
     * constructor for numberical-valued cards. This method ensures the
     * resulting card will have proper format, possibly truncating values.
     *
     * @param key     the keyword for the card
     * @param value   the value of the keyword
     * @param comment the comment field for this card.
     */
    public FitsCard(String key, Number value, String comment) {
/**
         * allocate the data bytes
         */
        initializeStorage();
/**
         * keyword
         */
        this.key = writeKeywordToData(key);

        /** value */
        int index = KEY_LENGTH;
        data[index++] = (byte) '=';

        String string = value.toString();
        int first_index = 29 - string.length() + 1;

        while (index < first_index)
            data[index++] = (byte) ' ';

        try {
            /** copy bytes */
            byte[] buffer = string.getBytes("ASCII");

            for (int i = 0; i < buffer.length; ++i) {
                data[index++] = buffer[i];
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println(e);
        }

        this.value = value;
/**
         * comment
         */
        this.comment = writeCommentToData(comment, index);

        parsed = true;
    } // end of string value constructor

    /**
     * Initialize the basic array, etc. storage for the card this
     * should be called by all the constructors
     */
    private void initializeStorage() {
        data = new byte[LENGTH];
    } // end of initializeStorage method

    /**
     * write a given keyword into the data array. The key is first
     * formatted to conform to the FITS standard. The formated keyword is
     * returned.
     *
     * @param key the string intended to be used as a keyword
     *
     * @return the keyword forced into proper FITS format.
     */
    private String writeKeywordToData(String key) {
        String formatted_key = formatKeyword(key);

        int index = 0;

        try {
            /** copy bytes */
            byte[] buffer = formatted_key.getBytes("ASCII");

            for (int i = 0; (i < buffer.length) && (index < KEY_LENGTH); ++i) {
                data[index++] = buffer[i];
            }
        } catch (UnsupportedEncodingException e) {
        }

/**
         * padding
         */
        for (int i = index; i < KEY_LENGTH; ++i) {
            data[i] = (byte) ' ';
        }

        return formatted_key;
    } // end of writeKeywordToData method

    /**
     * write a given comment into the data array, starting at the given
     * index the index should point to the first. Truncates the comment if it
     * will not fit in the card. Returns the truncated comment.
     *
     * @param comment the comment to write into the data array
     * @param index the offset into the data array at which to start the
     *        comment.
     *
     * @return DOCUMENT ME!
     */
    private String writeCommentToData(String comment, int index) {
        if (index == KEY_LENGTH) {
/**
             * there is no value for this card
             */
            data[index++] = (byte) ' ';
            data[index++] = (byte) ' ';
        } else {
/**
             * add a slash to separate the value from the comment but first
             * check if we have enogh room for at least one character of the
             * comment
             */
            if (index >= (LENGTH - 4)) {
                return "";
            }

/**
             * add a space between the value and the slash
             */
            data[index++] = (byte) ' ';

/**
             * Check if the comment is short enough that we can start it in the
             * same column as a number or boolean valued card. We don't have
             * to do this, but it makes the files look prettier
             */
            if ((comment.length() + 2) < (LENGTH - 31)) {
                while (index < 31)
                    data[index++] = (byte) ' ';
            }

/**
             * now a slash and a space to separate value from comment
             */
            data[index++] = (byte) '/';
            data[index++] = (byte) ' ';
        }

        /** truncate the comment if we need to */
        int max_length = LENGTH - index;

        if (comment.length() > max_length) {
            comment = comment.substring(0, max_length);
        }

        try {
            /** copy bytes */
            byte[] buffer = comment.getBytes("ASCII");

            for (int i = 0; i < buffer.length; ++i) {
                data[index++] = buffer[i];
            }
        } catch (UnsupportedEncodingException e) {
        }

/**
         * fill the rest of the card with blanks
         */
        while (index < LENGTH)
            data[index++] = (byte) ' ';

/**
         * return the truncated comment
         */
        return comment;
    } // end of writeCommentToData method

    /**
     * returns the raw 80 bytes of data for this card. Note, this
     * returns the actual internal array and not a copy, so it is usually a
     * bad idea to modify the contents of the returned array.
     *
     * @return the 80 byte array representing this card.
     */
    public byte[] data() {
        return data;
    }

    /**
     * returns a string containing the keyword for this card
     *
     * @return the keyword for this card.
     */
    public String key() {
        return key;
    }

    /**
     * returns an object representing the value field. This object may
     * be Boolean, String, Integer, Double, or null if the keyword has no
     * value. Caling this method forces the card to parse its internal
     * structure.
     *
     * @return the value field of the card or null if the card has no value.
     *
     * @throws FitsCardException if the raw data are not properly formatted
     */
    public Object value() throws FitsCardException {
        parse();

        return value;
    } // end of value method

    /**
     * returns an integer value, if appropriate. If the card contains a
     * floating point number it will be clipped to an integer.
     *
     * @return the card value clipped to an integer.
     *
     * @throws FitsCardException if the card does not have a numerical value
     */
    public int intValue() throws FitsCardException {
        return ((Number) value()).intValue();
    }

    /**
     * returns a double value, if appropriate
     *
     * @return the value field of the card
     *
     * @throws FitsCardException if the card does not have a numerical value
     */
    public double doubleValue() throws FitsCardException {
        return ((Number) value()).doubleValue();
    }

    /**
     * returns the value of the card as a string. If the value is not
     * actually a string, then the toString() method of the value will be
     * returned. Note that this may be different from the literal value in the
     * raw data - particularly for boolean values.
     *
     * @return DOCUMENT ME!
     *
     * @throws FitsCardException if the raw data are not properly formatted
     */
    public String stringValue() throws FitsCardException {
        return value().toString();
    }

    /**
     * returns the commment field. Calling this method forces the card
     * to parse its internal structure.
     *
     * @return the comment field of the card.
     *
     * @throws FitsCardException if the raw data are not properly formatted.
     */
    public String comment() throws FitsCardException {
        parse();

        return comment;
    } // end of comment method

    /**
     * parse the value and comment fields This method accepts both
     * fixed and free format cards. It does nothing if the card has already
     * been parsed.
     *
     * @throws FitsCardException if the raw data are not properly formatted.
     */
    private void parse() throws FitsCardException {
        if (parsed) {
            return;
        }

/**
         * make sure the card has a value
         */
        if (data[8] != '=') {
/**
             * this card has no value
             */
            value = null;
            comment = new String(data, 8, 71);
        } else {
            /**
             * The card has a value so let's parse it. Start by
             * skipping any leading white space.
             */
            int i = 10;

            while ((i < 80) && (data[i] == ' '))
                ++i;

            if (i == 80) {
/**
                 * blank line - the value is null and there is no comment
                 */
                value = null;
                comment = "";
            } else {
                if (data[i] == '/') {
/**
                     * The value is null, but there is a comment
                     */
                    value = null;
                } else if (data[i] == '\'') {
                    /**
                     * this is a string we need to
                     * unescape pairs of single quotes and fiond the end of
                     * the string. To do this we copy the data to a temporary
                     * buffer
                     */
                    int nbytes = 0;
                    byte[] bytes = new byte[68];
                    ++i;

                    while (true) {
                        if (i == 80) {
                            throw new FitsCardException("No closing quote for " +
                                this);
                        }

                        if (data[i] == '\'') {
                            if ((i < 79) && (data[i + 1] != '\'')) {
/**
                                 * we found the end quote
                                 */
                                ++i; // move past the end quote

                                break;
                            } else {
/**
                                 * this is an escaped quote
                                 */
                                bytes[nbytes++] = data[i++];
                                ++i;
                            }
                        } else {
/**
                             * just a regular character, copy it to the
                             * unescaped buffer
                             */
                            bytes[nbytes++] = data[i++];
                        }
                    } // end of loop over string bytes

/**
                     * leading blanks are significant, but trailing ones are
                     * not. All blanks becomes a single blank, since the first
                     * blank is considered to be a leading blank but the rest
                     * are considered to be trailing
                     */
                    while ((nbytes > 1) && (bytes[nbytes - 1] == ' '))
                        --nbytes;

/**
                     * now we are ready to parse the bytes into an ASCII string
                     */
                    try {
                        value = new String(bytes, 0, nbytes, "ASCII");
                    } catch (UnsupportedEncodingException e) {
                        throw new FitsCardException(
                            "Java doesn't understand ASCII encoding");
                    }
                } else {
                    /** this wasn't a string, so we assume it is a number. */
                    int start = i;

                    while ((i < 80) && (data[i] != ' ') && (data[i] != '/'))
                        ++i;

                    try {
                        String string = new String(data, start, i - start,
                                "ASCII");
                        value = new Double(string);
                    } catch (Exception e) {
                        throw new FitsCardException("Ill formed number in " +
                            this);
                    }

/**
                     * check if this is a complex number. We don't currently
                     * support these
                     */
                    while ((i < 80) && (data[i] == ' '))
                        ++i;

                    if ((i < 80) &&
                            (Character.isDigit((char) data[i]) ||
                            (data[i] == '+') || (data[i] == '-'))) {
                        throw new FitsCardException(
                            "Complex numbers not supported " + this);
                    }
                } // end if the value was a number

/**
                 * OK, now we have to parse the comment The standard now
                 * requires a slash between the value and the comment, but it
                 * didn't use to. So we will be lenient and not require it,
                 * though we still have to handle it if it is there
                 */
                while ((i < 80) && (data[i] == ' '))
                    ++i;

                if ((i < 80) && (data[i] == '/')) {
                    ++i;
                }

                if (i < 80) {
                    try {
                        comment = new String(data, i, 80 - i, "ASCII").trim();
                    } catch (UnsupportedEncodingException e) {
                        throw new FitsCardException(
                            "Java doesn't understand ASCII");
                    }
                } else {
                    comment = "";
                }
            } // end if there was a non-null value
        } // end if there was a value
    } // end of parse method

    /**
     * returns the raw bytes of the card converted to a string and with
     * trailing white space removed.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String(data).trim();
    } // end of toString method

    /**
     * returns a string which is guaranteed to be a legal FITS keyword.
     * Lower case is converted to upper case, white space is trimmed, illegal
     * characters are removed and the string length is truncated to 8
     * characters or less.
     *
     * @param template the intended keyword
     *
     * @return a properly formatted FITS keyword as close as possible to the
     *         template.
     */
    public static String formatKeyword(String template) {
        template = template.trim().toUpperCase();

        int index = 0;
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; (i < template.length()) && (index < KEY_LENGTH); ++i) {
            char c = template.charAt(i);

/**
             * check if this is a valid character
             */
            if (Character.isLetter(c) || Character.isDigit(c) || (c == '-') ||
                    (c == '_')) {
/**
                 * this is a valid keyword character
                 */
                buffer.append(template.charAt(i));
                ++index;
            }
        } /* end of loop over characters */
        return buffer.toString();
    } // end of formatKeyword method
} // end of FitsCard class
