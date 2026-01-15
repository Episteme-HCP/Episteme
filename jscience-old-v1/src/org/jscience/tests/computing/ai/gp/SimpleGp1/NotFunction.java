import org.jscience.computing.geneticprogramming.*;


/**
 * Boolean NOT Function
 */
public class NotFunction extends Function {
    /**
     * Creates a new NotFunction object.
     */
    public NotFunction() {
        super();
        super.arg = new Program[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "NOT";
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

        return new Boolean(!b1.booleanValue());
    }
}
