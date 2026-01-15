import org.jscience.computing.geneticprogramming.*;


/**
 * Boolean OR Function
 */
public class OrFunction extends Function {
    /**
     * Creates a new OrFunction object.
     */
    public OrFunction() {
        super();
        super.arg = new Program[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "OR";
    }

    /**
     * DOCUMENT ME!
     *
     * @param fitnessCase DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(Object fitnessCase) {
        Boolean b1 = (Boolean) arg[0].eval(fitnessCase);
        Boolean b2 = (Boolean) arg[1].eval(fitnessCase);

        return new Boolean(b1.booleanValue() || b2.booleanValue());
    }
}
