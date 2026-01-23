package org.jscience.economics.money.providers;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL
/**
 * A small class representing a search result.
 */
public class SearchHit {
    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private String result;

/**
     * Creates a new SearchHit object.
     *
     * @param psymbol DOCUMENT ME!
     * @param presult DOCUMENT ME!
     */
    public SearchHit(String psymbol, String presult) {
        symbol = psymbol;

        // Massage the result data
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < presult.length(); i++) {
            if ((Character.isJavaIdentifierStart(presult.charAt(i))) ||
                    (Character.isWhitespace(presult.charAt(i)))) {
                sb.append(presult.charAt(i));
            }
        }

        result = sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getResult() {
        return result;
    }
}
