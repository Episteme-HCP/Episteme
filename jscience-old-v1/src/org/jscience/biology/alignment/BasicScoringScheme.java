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

package org.jscience.biology.alignment;

/**
 * This class implements a basic scoring scheme. At least three parameters must be
 * provided to the constructor: the reward for a match (a substitution of equal
 * characters), the penalty for a mismatch (a substitution of different characters) and
 * the cost of a gap (an insertion or deletion of a character). Note that it only supports
 * an additive gap cost function.
 * <p/>
 * <P>Although the match reward is expected to be a positive value, and the mismatch
 * penalty and the gap cost are expected to be negative, no attempt is made to enforce
 * these behaviour.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 */
public class BasicScoringScheme extends ScoringScheme {
    /**
     * The reward for a match (a substitution of equal characters).
     */
    protected int match_reward;

    /**
     * The penalty for a mismatch (a substitution of different characters).
     */
    protected int mismatch_penalty;

    /**
     * The cost of a gap (an insertion or deletion of a character).
     */
    protected int gap_cost;

    /**
     * The maximum absolute score that this scoring scheme can return, which is the
     * maximum absolute value among <CODE>match_reward</CODE>,
     * <CODE>mismatch_penalty</CODE> and <CODE>gap_cost</CODE>.
     */
    protected int max_absolute_score;

    /**
     * Creates a new instance of a basic scoring scheme with the specified values of
     * match reward, mismatch penalty and gap cost. The case of characters is significant
     * when subsequently computing their score.
     *
     * @param match_reward     reward for a substitution of equal characters
     * @param mismatch_penalty penalty for a substitution of different characters
     * @param gap_cost         cost of an insertion or deletion of any character
     */
    public BasicScoringScheme(int match_reward, int mismatch_penalty,
                              int gap_cost) {
        this(match_reward, mismatch_penalty, gap_cost, true);
    }

    /**
     * Creates a new instance of basic scoring scheme with the specified values of
     * match reward, mismatch penalty and gap cost. If <CODE>case_sensitive</CODE> is
     * <CODE>true</CODE>, the case of characters is significant when subsequently
     * computing their score; otherwise the case is ignored.
     *
     * @param match_reward     reward for a substitution of equal characters
     * @param mismatch_penalty penalty for a substitution of different characters
     * @param gap_cost         cost of an insertion or deletion of any character
     * @param case_sensitive   <CODE>true</CODE> if the case of characters must be
     *                         significant, <CODE>false</CODE> otherwise
     */
    public BasicScoringScheme(int match_reward, int mismatch_penalty,
                              int gap_cost, boolean case_sensitive) {
        super(case_sensitive);

        this.match_reward = match_reward;
        this.mismatch_penalty = mismatch_penalty;
        this.gap_cost = gap_cost;

        // store the maximum absolute score that this scoring scheme can return,
        // which is the maximum absolute value among match_reward, mismatch_penalty
        // and gap_cost
        if (Math.abs(match_reward) >= Math.abs(mismatch_penalty)) {
            if (Math.abs(match_reward) >= Math.abs(gap_cost)) {
                this.max_absolute_score = Math.abs(match_reward);
            } else {
                this.max_absolute_score = Math.abs(gap_cost);
            }
        } else if (Math.abs(mismatch_penalty) >= Math.abs(gap_cost)) {
            this.max_absolute_score = Math.abs(mismatch_penalty);
        } else {
            this.max_absolute_score = Math.abs(gap_cost);
        }
    }

    /**
     * Returns the score of a substitution of character <CODE>a</CODE> for character
     * <CODE>b</CODE> according to this scoring scheme. It is <CODE>match_reward</CODE>
     * if <CODE>a</CODE> equals <CODE>b</CODE>, <CODE>mismatch_penalty</CODE> otherwise.
     *
     * @param a first character
     * @param b second character
     * @return <CODE>match_reward</CODE> if <CODE>a</CODE> equals <CODE>b</CODE>,
     *         <CODE>mismatch_penalty</CODE> otherwise.
     */
    public int scoreSubstitution(char a, char b) {
        if (isCaseSensitive()) {
            if (a == b) {
                return match_reward;
            } else {
                return mismatch_penalty;
            }
        } else if (Character.toLowerCase(a) == Character.toLowerCase(b)) {
            return match_reward;
        } else {
            return mismatch_penalty;
        }
    }

    /**
     * Always returns <CODE>gap_cost</CODE> for the insertion of any character.
     *
     * @param a the character to be inserted
     * @return <CODE>gap_cost</CODE>
     */
    public int scoreInsertion(char a) {
        return gap_cost;
    }

    /**
     * Always returns <CODE>gap_cost</CODE> for the deletion of any character.
     *
     * @param a the character to be deleted
     * @return <CODE>gap_cost</CODE>
     */
    public int scoreDeletion(char a) {
        return gap_cost;
    }

    /**
     * Returns the maximum absolute score that this scoring scheme can return for any
     * substitution, deletion or insertion, which is the maximum absolute value among
     * <CODE>match_reward</CODE>, <CODE>mismatch_penalty</CODE> and
     * <CODE>gap_cost</CODE>.
     *
     * @return the maximum absolute value among <CODE>match_reward</CODE>,
     *         <CODE>mismatch_penalty</CODE> and <CODE>gap_cost</CODE>.
     */
    public int maxAbsoluteScore() {
        return max_absolute_score;
    }

    /**
     * Tells whether this scoring scheme supports partial matches, which it does not.
     *
     * @return always return <CODE>false</CODE>
     */
    public boolean isPartialMatchSupported() {
        return false;
    }

    /**
     * Returns a String representation of this scoring scheme.
     *
     * @return a String representation of this scoring scheme
     */
    public String toString() {
        return "Basic scoring scheme: match reward = " + match_reward +
                ", mismatch penalty = " + mismatch_penalty + ", gap cost = " +
                gap_cost;
    }
}
