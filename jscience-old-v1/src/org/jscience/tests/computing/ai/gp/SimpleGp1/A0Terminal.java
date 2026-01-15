import org.jscience.computing.geneticprogramming.*;


/**
 * This Boolean terminal corresponds to the A0 input of the 3-Multiplexer.
 */
public class A0Terminal extends Terminal {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toText() {
        return "A0";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "A0Terminal";
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

        return new Boolean(inp[0]);
    }
}
