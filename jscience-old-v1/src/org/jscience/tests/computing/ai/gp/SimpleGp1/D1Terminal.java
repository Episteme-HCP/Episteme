import org.jscience.computing.geneticprogramming.*;


/**
 * This Boolean terminal corresponds to the D1 input of the 3-Multiplexer.
 */
public class D1Terminal extends Terminal {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toText() {
        return "D1";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "D1Terminal";
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

        return new Boolean(inp[2]);
    }
}
