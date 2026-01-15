package org.jscience.ml.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CommandOptionValue {
    /** DOCUMENT ME! */
    static Logger logger = Logger.getLogger(CommandOptionValue.class.getName());

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
    String desc;

/**
     * Creates a new CommandOptionValue object.
     *
     * @param name DOCUMENT ME!
     * @param desc DOCUMENT ME!
     */
    public CommandOptionValue(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String usage() {
        return CommandOptions.BLANK.substring(0, 20) +
        (name + CommandOptions.BLANK).substring(0, 15) + "//" + desc;
    }
}
