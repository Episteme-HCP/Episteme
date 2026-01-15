import JSHOP2.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CheckNull implements Calculate {
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Term call(List l) {
        if (l.getHead().isNil()) {
            return l.getRest().getHead();
        }

        return new TermNumber(0);
    }
}
