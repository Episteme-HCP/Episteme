package org.jscience.ml.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CommandOptions {
    /** DOCUMENT ME! */
    static Logger logger = Logger.getLogger(CommandOptions.class.getName());

    /** DOCUMENT ME! */
    static Level MYFINE = Level.FINE;

    /** DOCUMENT ME! */
    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    /** DOCUMENT ME! */
    static String BLANK = "                                                            ";

    /** DOCUMENT ME! */
    static CommandOption DEBUG = new CommandOption("-DEBUG", Boolean.class,
            null, Boolean.FALSE, "debug");

    /** DOCUMENT ME! */
    CommandOptionManager commandOptionManager;

    /** DOCUMENT ME! */
    String[] args;

    /** DOCUMENT ME! */
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
     * @param args DOCUMENT ME!
     * @param com  DOCUMENT ME!
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
     * subclass should override this
     *
     * @return DOCUMENT ME!
     */
    protected CommandOption[] extendOptions() {
        return new CommandOption[] { DEBUG, };
    }

    // combines options from this and CommandOptions
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected CommandOption[] getOptions() {
        if (option == null) {
            Class thisClass = this.getClass();

            if (thisClass.equals(CommandOptions.class)) {
                option = extendOptions();
            } else {
                Class superx = thisClass.getSuperclass();
                CommandOptions superClass = null;

                try {
                    superClass = (CommandOptions) superx.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
     * DOCUMENT ME!
     *
     * @param commandOptionManager DOCUMENT ME!
     */
    public void setCommandOptionManager(
        CommandOptionManager commandOptionManager) {
        this.commandOptionManager = commandOptionManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CommandOptionManager getCommandOptionManager() {
        return commandOptionManager;
    }

    /**
     * DOCUMENT ME!
     */
    void analyzeArgs() {
        int argCount = 0;

        while (argCount < args.length) {
            String argx = args[argCount];

            // scan through args
            int j = getOptionIndex(argx);

            if (j == commandOptionManager.UNKNOWN) {
                System.err.println("unknown arg: " + argx);
                argCount++;
            } else if (j == commandOptionManager.AMBIGUOUS) {
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
     * DOCUMENT ME!
     */
    void debug() {
        System.out.println("\nOptions and values:\n");

        for (int i = 0; i < option.length; i++) {
            System.out.println(option[i].toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getOptionIndex(String arg) {
        int found = commandOptionManager.UNKNOWN;

        for (int i = 0; i < option.length; i++) {
            if (option[i].getName().startsWith(arg.toUpperCase())) {
                if (found != commandOptionManager.UNKNOWN) {
                    found = commandOptionManager.AMBIGUOUS;

                    break;
                }

                found = i;
            }
        }

        return found;
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void process() throws Exception {
        commandOptionManager.process(this);
    }
}
;
