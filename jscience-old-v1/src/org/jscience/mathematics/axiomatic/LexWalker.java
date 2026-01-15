package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LexWalker {
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public static void getMostImportant(String[] p) {
        ProofSystem system = new ProofSystem(p);
        system.analyze();
    }
}
