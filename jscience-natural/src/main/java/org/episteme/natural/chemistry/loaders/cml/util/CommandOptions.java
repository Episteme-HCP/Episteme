/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Container and processor for a collection of command-line options.
 * Handles argument parsing and usage reporting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommandOptions {
    /** Logger for this class. */
    static Logger logger = Logger.getLogger(CommandOptions.class.getName());

    /** Convenience constant for Level.FINE. */
    static Level MYFINE = Level.FINE;

    /** Convenience constant for Level.FINEST. */
    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    /** A string of blanks used for formatting usage output. */
    static String BLANK = "                                                            ";

    /** Predefined option for enabling debug output. */
    static CommandOption DEBUG = new CommandOption("-DEBUG", Boolean.class,
            null, Boolean.FALSE, "debug");

    /** The manager that defines the options and logic. */
    CommandOptionManager commandOptionManager;

    /** The raw command-line arguments. */
    String[] args;

    /** The array of supported options. */
    CommandOption[] option = null;

/**
     * do not use.
     */
    public CommandOptions() {
        ;
    }

    /**
     * Creates a new CommandOptions object.
     *
     * @param args the command-line arguments to parse
     * @param com  the manager for these options
     */
    public CommandOptions(String[] args, CommandOptionManager com) {
        this.args = args;
        this.commandOptionManager = com;

        if (args.length == 0) {
            usage(System.out);
            System.exit(0);
        }

        getOptions();
        analyzeArgs();
    }

    /**
     * Returns the array of default options. Subclasses should override this to add more.
     *
     * @return the array of CommandOption objects
     */
    protected CommandOption[] extendOptions() {
        return new CommandOption[] { DEBUG, };
    }

    /**
     * Aggregates options from the class hierarchy.
     *
     * @return the complete array of options
     */
    protected CommandOption[] getOptions() {
        if (option == null) {
            Class<?> thisClass = this.getClass();

            if (thisClass.equals(CommandOptions.class)) {
                option = extendOptions();
            } else {
                Class<?> superx = thisClass.getSuperclass();
                CommandOptions superClass = null;

                try {
                    superClass = (CommandOptions) superx.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (superClass != null) {
                    CommandOption[] superOption = superClass.extendOptions();
                    CommandOption[] thisOption = this.extendOptions();
                    option = new CommandOption[superOption.length +
                        thisOption.length];
    
                    for (int i = 0; i < superOption.length; i++) {
                        option[i] = superOption[i];
                    }
    
                    for (int i = 0; i < thisOption.length; i++) {
                        option[i + superOption.length] = thisOption[i];
                    }
                } else {
                    option = this.extendOptions();
                }
            }
        }

        if (logger.getLevel().equals(MYFINE)) {
            for (int i = 0; i < option.length; i++) {
                System.out.println(option[i]);
            }
        }

        return option;
    }

    /**
     * Sets the command option manager.
     *
     * @param commandOptionManager the manager to set
     */
    public void setCommandOptionManager(
        CommandOptionManager commandOptionManager) {
        this.commandOptionManager = commandOptionManager;
    }

    /**
     * Returns the command option manager.
     *
     * @return the manager
     */
    public CommandOptionManager getCommandOptionManager() {
        return commandOptionManager;
    }

    /**
     * Analyzes and processes the command-line arguments.
     */
    void analyzeArgs() {
        int argCount = 0;

        while (argCount < args.length) {
            String argx = args[argCount];

            // scan through args
            int j = getOptionIndex(argx);

            if (j == CommandOptionManager.UNKNOWN) {
                System.err.println("unknown arg: " + argx);
                argCount++;
            } else if (j == CommandOptionManager.AMBIGUOUS) {
                System.err.println("ambiguous arg: " + argx);

                for (int i = 0; i < option.length; i++) {
                    if (option[i].name.toUpperCase().startsWith(argx)) {
                        System.err.println("......" + option[i].name);
                    }
                }

                argCount++;
            } else {
                // ok
                argCount = option[j].process(args, ++argCount);
            }
        }

        // debug input
        if (DEBUG.getValue().equals(Boolean.TRUE)) {
            debug();
        }
    }

    /**
     * Prints current options and their values to standard output for debugging.
     */
    void debug() {
        System.out.println("\nOptions and values:\n");

        for (int i = 0; i < option.length; i++) {
            System.out.println(option[i].toString());
        }
    }

    /**
     * Finds the index of the option matching the given argument prefix.
     *
     * @param arg the argument to match
     *
     * @return the index in the option array, or UNKNOWN/AMBIGUOUS
     */
    int getOptionIndex(String arg) {
        int found = CommandOptionManager.UNKNOWN;

        for (int i = 0; i < option.length; i++) {
            if (option[i].getName().startsWith(arg.toUpperCase())) {
                if (found != CommandOptionManager.UNKNOWN) {
                    found = CommandOptionManager.AMBIGUOUS;

                    break;
                }

                found = i;
            }
        }

        return found;
    }

    /**
     * Prints the usage information to the specified stream.
     *
     * @param out the print stream to output to
     */
    public void usage(java.io.PrintStream out) {
        getOptions();
        out.println("Usage: java " + commandOptionManager.getClass().getName() +
            " [options]");

        for (int i = 0; i < option.length; i++) {
            out.println(option[i].usageString());
        }
    }

    /**
     * Triggers the processing of the options via the manager.
     *
     * @throws Exception if processing fails
     */
    public void process() throws Exception {
        commandOptionManager.process(this);
    }
}
;

