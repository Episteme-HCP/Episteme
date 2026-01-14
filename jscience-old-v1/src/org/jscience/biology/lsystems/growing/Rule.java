/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.biology.lsystems.growing;

/**
 * To represent a rule of an LSystem containing a predecessor a successor
 * and a probability this class may be used.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Rule {
    /** The prdecessor of the rule. */
    private String m_strPred;

    /** The successor of the rule. */
    private String m_strSucc;

    /** The char id of the predecessor. */
    private char m_chPredID = ' ';

    /**
     * The age of the predecesor. This is the time which indicates when
     * to use this rule
     */
    private int m_iPredAge = -1;

/**
     * Constructor by passing the string of hte rule
     *
     * @param strRule The string of this rule.
     * @throws Exception Is thrown if the rule is not in the right form or
     *                   contains invalid signs.
     */
    public Rule(String strRule) throws Exception {
        int iSignPos;

        //store the position of the rulesign '->' in the string
        if ((iSignPos = strRule.indexOf(GrowingPlantsDefinitions.STR_RULESIGN)) == -1) { //there is no sign wich separates the predecessor from the successor

            //Log.log(Def.STR_RULESIGN, Log.NO_RULESIGN, true);
        }

        //get predecessor
        m_strPred = strRule.substring(0, iSignPos).trim();

        //get the string right of the rulesign which is the successor
        m_strSucc = strRule.substring(iSignPos +
                GrowingPlantsDefinitions.STR_RULESIGN.length()).trim();

        validateSuccessor(m_strSucc);

        //Log.debug("Rule created: Pred=" + m_strPred + " Succ=" + m_strSucc);
    }

/**
     * Constructor allowing to initialize the predecesor and successor
     *
     * @param strPredecessor The predecessor of this rule.
     * @param strSuccessor   The successor of the rule.
     */
    public Rule(String strPredecessor, String strSuccessor) {
        m_strPred = strPredecessor;
        m_strSucc = strSuccessor;

        //Log.debug("Rule created: Pred=" + m_strPred + " Succ=" + m_strSucc);
    }

    /**
     * Checks if the axiom or the successor contain invalid signs. If
     * so an exception is thrown. Else the method returns normally. All valid
     * signs must be listed in the Def class.
     *
     * @param strSucc The successor resp. axiom to check.
     *
     * @throws Exception If the string contains invalid signs.
     *
     * @see GrowingPlantsDefinitions
     */
    private void validateSuccessor(String strSucc) throws Exception {
        //Log.debug("String to validate: "+strSucc);
        for (int i = 0; i < strSucc.length(); i++) //search the whole string
         {
            //check if the actual sign is a letter
            if (Character.isLetter(strSucc.charAt(i)) == false) {
                //Log.debug(Def.STR_SIGNS);
                //the actual sign is not a letter thus it must be a valid sign
                //check if the actual sign is in the string with all valid signs
                if (GrowingPlantsDefinitions.STR_SIGNS.indexOf(strSucc.charAt(i)) == -1) {
                    //the char was not found in the valid sign list, output error
                    //Log.log(String.valueOf(strSucc.charAt(i)), Log.INVALID_SIGN, true);
                }
            }
        }
    }

    /**
     * Extracts the ID char of the first occurence of an element in the
     * given string. The element is identified by the C_BEGIN_ELEMENT constant
     * in the Def class.
     *
     * @param strLine The string containing the element.
     *
     * @return ID char
     *
     * @see GrowingPlantsDefinitions
     */
    public static char getElementID(String strLine) {
        int iStartPos = strLine.indexOf(GrowingPlantsDefinitions.C_BEGIN_ELEMENT);

        return strLine.charAt(iStartPos + 1);
    }

    /**
     * Extracts the age of the first occurence of an element in the
     * given string. The separator from an element ID to the age is the
     * C_ELEMENT_SEPARATOR constant and the end is the C_END_ELEMENT constant
     * both from the Def class.
     *
     * @param strLine The string containing the element.
     *
     * @return Age
     *
     * @see GrowingPlantsDefinitions
     */
    public static int getElementAge(String strLine) {
        int iStartPos = strLine.indexOf(GrowingPlantsDefinitions.C_ELEMENT_SEPARATOR);
        int iEndPos = strLine.indexOf(GrowingPlantsDefinitions.C_END_ELEMENT);

        return Converter.readInt(strLine.substring(iStartPos + 1, iEndPos));
    }

    /**
     * Retrieves the predecessor ID
     *
     * @return The predecessor id char.
     */
    public char getPredecessorID() {
        if (m_chPredID == ' ') {
            m_chPredID = getElementID(m_strPred);
        }

        return m_chPredID;
    }

    /**
     * Retrieves the predecessor age.
     *
     * @return the predecessor age.
     */
    public int getPredecessorAge() {
        if (m_iPredAge == -1) {
            m_iPredAge = getElementAge(m_strPred);
        }

        return m_iPredAge;
    }

    /**
     * Stores a new predecessor in the member variable.
     *
     * @param strPredecessor The new predecessor
     */
    public void setPredecessor(String strPredecessor) {
        m_strPred = strPredecessor;
    }

    /**
     * Stores a new sucessor in the member variable.
     *
     * @param strSuccessor The new sucessor
     */
    public void setSuccessor(String strSuccessor) {
        m_strSucc = strSuccessor;
    }

    /**
     * Returns the predecessor value of this rule.
     *
     * @return The predecessor.
     */
    public String getPredecessor() {
        return m_strPred;
    }

    /**
     * Returns the successor value of this rule
     *
     * @return The successor value.
     */
    public String getSuccessor() {
        return m_strSucc;
    }

    /**
     * Converts the rule to a string version and returns it. The output
     * of the string is in the form predecessor, rulesign, successor.
     *
     * @return The rule as a string.
     */
    public String toString() {
        return m_strPred + " " + GrowingPlantsDefinitions.STR_RULESIGN + " " +
        m_strSucc;
    }
} //rule
