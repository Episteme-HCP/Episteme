package org.jscience.ml.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CommandOption {
    /** DOCUMENT ME! */
    static Logger logger = Logger.getLogger(CommandOption.class.getName());

    /** DOCUMENT ME! */
    static Level MYFINE = Level.FINE;

    /** DOCUMENT ME! */
    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    /** DOCUMENT ME! */
    String name;

    /** DOCUMENT ME! */
    Class classx;

    /** DOCUMENT ME! */
    CommandOptionValue[] optionValue;

    /** DOCUMENT ME! */
    Object value;

    /** DOCUMENT ME! */
    String desc;

/**
     * Creates a new CommandOption object.
     *
     * @param name        DOCUMENT ME!
     * @param classx      DOCUMENT ME!
     * @param optionValue DOCUMENT ME!
     * @param value       DOCUMENT ME!
     * @param desc        DOCUMENT ME!
     */
    public CommandOption(String name, Class classx,
        CommandOptionValue[] optionValue, Object value, String desc) {
        this.name = name.toUpperCase();
        this.classx = classx;
        this.optionValue = optionValue;
        this.value = value;
        this.desc = desc;

        if (classx.equals(Boolean.class)) {
            this.value = Boolean.FALSE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return this.name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getClassx() {
        return this.classx;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return this.desc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String usageString() {
        String s = "";

        // name
        s += (" " + name + CommandOptions.BLANK).substring(0, 10);

        String className = classx.getName();
        int idx = ("." + className).lastIndexOf(".");

        // class (type)
        className = className.substring(idx);

        if (className.equals("Boolean")) {
            s += CommandOptions.BLANK.substring(0, 9);
        } else {
            s += (" (" + (className.substring(0, 6)) + ")");
        }

        // default value
        String vv = " []";

        if (value != null) {
            vv = " [" + value + "]";
        }

        s += (vv + CommandOptions.BLANK).substring(0, 12);

        // description
        s += (" //" + desc);

        // list optionValues if present
        if (optionValue != null) {
            for (int i = 0; i < optionValue.length; i++) {
                s += "\n";
                s += optionValue[i].usage();
            }
        }

        return s;
    }

    /**
     * interprets arg.
     *
     * @param args DOCUMENT ME!
     * @param argCount DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int process(String[] args, int argCount) {
        int count = argCount;

        // boolean, if present set to true
        if (classx.equals(Boolean.class)) {
            value = Boolean.TRUE;
        } else {
            String value1 = args[count];

            // check value following
            if ((value1 == null) || value1.startsWith("-")) {
                System.err.println("Missing arg after: " + name);
            } else {
                if (optionValue != null) {
                    boolean found = false;

                    for (int i = 0; i < optionValue.length; i++) {
                        if (optionValue[i].name.equals(value1)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        System.err.println("Unknown option value: " + name +
                            "/" + value1);

                        for (int i = 0; i < optionValue.length; i++) {
                            System.err.println("   ..." + optionValue[i].name);
                        }
                    }
                }

                value = args[count++];

                if (classx.equals(Double.class)) {
                    value = new Double((String) value);
                    System.out.println("ZZ " + ((Double) value).doubleValue());
                }
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "";
        s += (" " + name + CommandOptions.BLANK).substring(0, 10);
        s += (" = " + value);

        return s;
    }
}
