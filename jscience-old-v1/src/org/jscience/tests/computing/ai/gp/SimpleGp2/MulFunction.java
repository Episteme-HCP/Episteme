import org.jscience.computing.geneticprogramming.*;


/**
 * The function which multiplies two doubles (its parameters)
 */
public class MulFunction extends Function {
    /**
     * Creates a new MulFunction object.
     */
    public MulFunction() {
        super.arg = new Program[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Mul";
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

        return new Double(d1.doubleValue() * d2.doubleValue());
    }
}
