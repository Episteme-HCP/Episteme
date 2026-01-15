import JSHOP2.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MyFunc implements Calculate {
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Term call(List l) {
        double sum = 0;

        while (l != null) {
            sum += ((TermNumber) l.getHead()).getNumber();
            l = l.getRest();
        }

        return new TermNumber(sum);
    }
}
