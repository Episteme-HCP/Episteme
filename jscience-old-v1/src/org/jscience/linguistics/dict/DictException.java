package org.jscience.linguistics.dict;

/**
 * Vyjimky protokolu dict RFC2229
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1
 */
public class DictException extends Exception {
    /* START: navratove kody */
    private final int CODE_221 = 221;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_250 = 250;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_420 = 420;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_421 = 421;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_500 = 500;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_501 = 501;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_502 = 502;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_503 = 503;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_530 = 530;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_531 = 531;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_532 = 532;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_550 = 550;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_551 = 551;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_552 = 552;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_554 = 554;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_555 = 555;

    /* END: navratove kody */
    /* START: chybove hlaseni */
    private final String ERROR_221 = "Problems while losing connection";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_250 = "Problems with command ";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_420 = "Server temporarily unavailable";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_421 = "Server shutting down at operator request";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_500 = "Syntax error, command not recognized";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_501 = "Syntax error, illegal parameters";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_502 = "Command not implemented";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_503 = "Command parameter not implemented";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_530 = "Access denied";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_531 = "Access denied, use \"SHOW INFO\" for server information";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_532 = "Access denied, unknown mechanism";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_550 = "Invalid database, use \"SHOW DB\" for list of databases";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_551 = "Invalid strategy, use \"SHOW STRAT\" for a list of strategies";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_552 = "No match";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_554 = "No databases present";

    /**
     * DOCUMENT ME!
     */
    private final String ERROR_555 = "No strategies available";

    /* END: chybove hlaseni */
    private String errorMsg = new String("Unknown error");

    /**
     * DOCUMENT ME!
     */
    private int errorNum = 0;

/**
     * Creates a new DictException object.
     */
    public DictException() {
    }

/**
     * Konstruktor
     *
     * @param num cislo chyby
     */
    public DictException(int num) {
        errorNum = num;

        switch (errorNum) {
        case CODE_221:
            errorMsg = ERROR_221;

            break;

        case CODE_250:
            errorMsg = ERROR_250;

            break;

        case CODE_420:
            errorMsg = ERROR_420;

            break;

        case CODE_421:
            errorMsg = ERROR_421;

            break;

        case CODE_500:
            errorMsg = ERROR_500;

            break;

        case CODE_501:
            errorMsg = ERROR_501;

            break;

        case CODE_502:
            errorMsg = ERROR_502;

            break;

        case CODE_503:
            errorMsg = ERROR_503;

            break;

        case CODE_530:
            errorMsg = ERROR_530;

            break;

        case CODE_531:
            errorMsg = ERROR_531;

            break;

        case CODE_532:
            errorMsg = ERROR_532;

            break;

        case CODE_550:
            errorMsg = ERROR_550;

            break;

        case CODE_551:
            errorMsg = ERROR_551;

            break;

        case CODE_552:
            errorMsg = ERROR_552;

            break;

        case CODE_554:
            errorMsg = ERROR_554;

            break;

        case CODE_555:
            errorMsg = ERROR_555;

            break;
        }
    } // END: DictException(int num)

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "DictException: " + errorMsg;
    } // END: toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getErrorNumber() {
        return errorNum;
    } // END: getErrorNumber()
}
