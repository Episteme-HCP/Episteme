import org.jscience.computing.geneticprogramming.*;


/**
 * X varible in the mathematical equation we try to find
 */
public class XTerminal extends Terminal {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toText() {
        return "X";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "XTerminal";
    }

    /**
     * DOCUMENT ME!
     *
     * @param fitnessCase DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(Object fitnessCase) {
        return (Double) fitnessCase;
    }
}
