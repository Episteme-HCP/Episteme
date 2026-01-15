import org.jscience.computing.geneticprogramming.*;


/**
 * The function which adds two doubles (its parameters)
 */
public class AddFunction extends Function {
    /**
     * Creates a new AddFunction object.
     */
    public AddFunction() {
        super.arg = new Program[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Add";
    }

    /**
     * DOCUMENT ME!
     *
     * @param fitnessCase DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(Object fitnessCase) {
        Double d1 = (Double) arg[0].eval(fitnessCase);
        Double d2 = (Double) arg[1].eval(fitnessCase);

        return new Double(d1.doubleValue() + d2.doubleValue());
    }
}
