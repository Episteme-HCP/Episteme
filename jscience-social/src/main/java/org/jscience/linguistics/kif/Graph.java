package org.jscience.linguistics.kif;

import java.util.ArrayList;


/**
 * Handle operations for creating a graphical representation of partial
 * ordering relations.  Supports Graph.jsp.
 */
public class Graph {
    /**
     * Create a ArrayList with a set of terms comprising a hierarchy
     * Each term String will be prefixed with an appropriate number of
     * indentChars.
     *
     * @param kb the knowledge base being graphed
     * @param term the term in the KB being graphed
     * @param relation the binary relation that is used to forms the arcs in
     *        the graph.
     * @param above the number of levels above the given term in the graph
     * @param below the number of levels below the given term in the graph
     * @param indentChars a String of characters to be used for indenting the
     *        terms
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList createGraph(KB kb, String term, String relation,
        int above, int below, String indentChars) {
        ArrayList result = new ArrayList();
        result = createGraphBody(kb, term, relation, above, 0, indentChars,
                above, true);
        result.addAll(createGraphBody(kb, term, relation, 0, below,
                indentChars, above, false));

        return result;
    }

    /**
     * The main body for createGraph().
     *
     * @param kb DOCUMENT ME!
     * @param term DOCUMENT ME!
     * @param relation DOCUMENT ME!
     * @param above DOCUMENT ME!
     * @param below DOCUMENT ME!
     * @param indentChars DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param show DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static ArrayList createGraphBody(KB kb, String term,
        String relation, int above, int below, String indentChars, int level,
        boolean show) {
        ArrayList result = new ArrayList();
        ArrayList parents = new ArrayList();
        ArrayList children = new ArrayList();

        if (above > 0) {
            ArrayList stmtAbove = kb.askWithRestriction(0, relation, 1, term);

            for (int i = 0; i < stmtAbove.size(); i++) {
                Formula f = (Formula) stmtAbove.get(i);
                String newTerm = f.getArgument(2);
                result.addAll(createGraphBody(kb, newTerm, relation, above - 1,
                        0, indentChars, level - 1, true));
            }
        }

        StringBuffer prefix = new StringBuffer();

        for (int i = 0; i < level; i++)
            prefix = prefix.append(indentChars);

        String hostname = KBmanager.getMgr().getPref("hostname");

        if (hostname == null) {
            hostname = "localhost";
        }

        String kbHref = "http://" + hostname + ":8080/sigma/Browse.jsp?lang=" +
            kb.language + "&kb=" + kb.name;
        String formattedTerm = "<a href=\"" + kbHref + "&term=" + term + "\">" +
            term + "</a>";

        if (show) {
            result.add(prefix + formattedTerm);
        }

        if (below > 0) {
            ArrayList stmtBelow = kb.askWithRestriction(0, relation, 2, term);

            for (int i = 0; i < stmtBelow.size(); i++) {
                Formula f = (Formula) stmtBelow.get(i);
                String newTerm = f.getArgument(1);
                result.addAll(createGraphBody(kb, newTerm, relation, 0,
                        below - 1, indentChars, level + 1, true));
            }
        }

        return result;
    }
}
