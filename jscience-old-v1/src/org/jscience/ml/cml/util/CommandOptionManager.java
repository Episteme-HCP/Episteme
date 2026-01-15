package org.jscience.ml.cml.util;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public interface CommandOptionManager {
    /** unknown argument. */
    int UNKNOWN = -1;

    /** ambiguous argument. */
    int AMBIGUOUS = -2;

    /**
     * after all params are set process them. includes input and output
     *
     * @param options
     *
     * @throws Exception could be anything, including IO
     */
    void process(CommandOptions options) throws Exception;

    /**
     * turn debug on or off.
     *
     * @param d on/off
     */
    void setDebug(boolean d);

    /**
     * get debugging.
     *
     * @return on/off
     */
    boolean getDebug();
}
