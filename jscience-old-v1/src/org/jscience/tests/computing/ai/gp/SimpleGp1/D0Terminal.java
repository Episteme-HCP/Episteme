import org.jscience.computing.geneticprogramming.*;


/**
 * This Boolean terminal corresponds to the D0 input of the 3-Multiplexer.
 */
public class D0Terminal extends Terminal {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toText() {
        return "D0";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "D0Terminal";
    }

    /**
     * DOCUMENT ME!
     *
     * @param fitnessCase DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(Object fitnessCase) {
        boolean[] inp = (boolean[]) fitnessCase;

        return new Boolean(inp[1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new D0Terminal();
    }
}
