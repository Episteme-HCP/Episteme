package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Unifier {
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean unify(WFF a, WFF b) {
        b.increaseVariables(a.getMaxVariable());

        int wffA = WFF.A;

        for (int i = 0; i < a.getLength(); i++) {
            if (i >= b.getLength()) {
                return false;
            }

            byte ach = a.getToken(i);
            byte bch = b.getToken(i);

            if (ach != bch) {
                if (bch >= wffA) {
                    WFF sub = a.getSubWFF(i);

                    if (sub.contains(bch) || !a.substitute(bch, sub) ||
                            !b.substitute(bch, sub)) {
                        return false;
                    }
                } else if (ach >= wffA) {
                    WFF sub = b.getSubWFF(i);

                    if (sub.contains(ach) || !a.substitute(ach, sub) ||
                            !b.substitute(ach, sub)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        a.normalize();
        b.normalize();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        //		WFF a = new WFF(":>>a>~b~c>a>cb");
        //		WFF b = new WFF(":>~a>ab");
        WFF a = new WFF(":>a>~b~c");
        WFF b = new WFF(":>~a>ab");
        System.out.println(Unifier.unify(a, b));
        System.out.println("a" + a);
        System.out.println("b" + b);
    }
}
