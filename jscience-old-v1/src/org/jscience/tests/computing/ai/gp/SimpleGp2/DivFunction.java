import org.jscience.computing.geneticprogramming.*;


/**
 * The function which divides two doubles (its parameters) [protected
 * divison]
 */
public class DivFunction extends Function {
    /**
     * Creates a new DivFunction object.
     */
    public DivFunction() {
        super.arg = new Program[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Div";
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

        if (d2.doubleValue() == 0) {
            return new Double(0);
        }

        return new Double(d1.doubleValue() / d2.doubleValue());
    }
}
