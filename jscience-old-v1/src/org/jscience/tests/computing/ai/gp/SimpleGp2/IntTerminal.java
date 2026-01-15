import org.jscience.computing.geneticprogramming.*;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IntTerminal extends Terminal {
    /**
     * DOCUMENT ME!
     */
    private static Random random = new Random();

    /**
     * DOCUMENT ME!
     */
    private int value = -1;

    /**
     * Creates a new IntTerminal object.
     */
    public IntTerminal() {
        value = random.nextInt(10) - 5;
    }

    /**
     * Creates a new IntTerminal object.
     *
     * @param val DOCUMENT ME!
     */
    public IntTerminal(int val) {
        value = val;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toText() {
        return (new Double(value)).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "IntTerminal";
    }

    /**
     * DOCUMENT ME!
     *
     * @param fitnessCase DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(Object fitnessCase) {
        return (new Double(value));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new IntTerminal(value);
    }
}
