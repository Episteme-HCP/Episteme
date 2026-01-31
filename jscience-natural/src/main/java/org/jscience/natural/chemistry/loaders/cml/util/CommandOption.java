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

package org.jscience.natural.chemistry.loaders.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Represents a single command-line option for JVM-based tools.
 * Supports various types including Boolean, String, Double, etc.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommandOption {
    /** The logger instance for this class. */
    static Logger logger = Logger.getLogger(CommandOption.class.getName());

    /** Convenience constant for the FINE logging level. */
    static Level MYFINE = Level.FINE;

    /** Convenience constant for the FINEST logging level. */
    static Level MYFINEST = Level.FINEST;
    
    /** The option name (stored in uppercase). */
    String name;

    /** The class representing the type of this option's value. */
    Class<?> classx;

    /** An array of allowed values for this option. */
    CommandOptionValue[] optionValue;

    /** The current value of this option. */
    Object value;

    /** A brief description of this option for usage reporting. */
    String desc;

    /**
     * Creates a new CommandOption object.
     *
     * @param name        the name of the option
     * @param classx      the expected value type
     * @param optionValue allowed value descriptors (can be null)
     * @param value       the default value
     * @param desc        the option description
     */
    public CommandOption(String name, Class<?> classx,
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
     * Sets the value of this option.
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns the current value of this option.
     *
     * @return the value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the name of this option.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the class type of this option.
     *
     * @return the class type
     */
    public Class<?> getClassx() {
        return this.classx;
    }

    /**
     * Returns the description of this option.
     *
     * @return the description
     */
    public String getDescription() {
        return this.desc;
    }

    /**
     * Returns a string representing the usage documentation for this option.
     *
     * @return the usage string
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
     * Interprets command line arguments to set this option's value.
     *
     * @param args     the command line arguments
     * @param argCount the current position in the arguments array
     *
     * @return the updated argument count position
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
                    value = Double.valueOf((String) value);
                    System.out.println("ZZ " + ((Double) value).doubleValue());
                }
            }
        }

        return count;
    }

    /**
     * Returns a string representation of this option and its current value.
     *
     * @return the string representation
     */
    public String toString() {
        String s = "";
        s += (" " + name + CommandOptions.BLANK).substring(0, 10);
        s += (" = " + value);

        return s;
    }
}

