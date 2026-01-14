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

package org.jscience.linguistics.kif;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A utility class that creates HTML-formatting Strings for various
 * purposes.
 */
public class HTMLformatter {
    /**
     * Create the HTML for a single step in a proof.
     *
     * @param query DOCUMENT ME!
     * @param step DOCUMENT ME!
     * @param kbName DOCUMENT ME!
     * @param language DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String proofTableFormat(String query, ProofStep step,
        String kbName, String language) {
        StringBuffer result = new StringBuffer();
        Formula f = new Formula();

        f.read(step.axiom);

        String axiom = new String(f.theFormula);
        f.theFormula = Formula.postProcess(f.theFormula);

        String hostname = KBmanager.getMgr().getPref("hostname");

        if (hostname == null) {
            hostname = "localhost";
        }

        String kbHref = "http://" + hostname + ":8080/sigma/Browse.jsp?lang=" +
            language + "&kb=" + kbName;

        if (f.theFormula.equalsIgnoreCase("FALSE")) { // Successful resolution theorem proving results in a contradiction.
            f.theFormula = "True"; // Change "FALSE" to "True" so it makes more sense to the user.
            result.append("<td valign=top width=50%>" + "True" + "</td>");
        } else {
            result.append("<td valign=top width=50%>" + f.htmlFormat(kbHref) +
                "</td>");
        }

        result.append("<td valign=top width=10%>");

        for (int i = 0; i < step.premises.size(); i++) {
            Integer stepNum = (Integer) step.premises.get(i);
            result.append(stepNum.toString() + " ");
        }

        if (step.premises.size() == 0) {
            if (Formula.isNegatedQuery(query, f.theFormula)) {
                result.append("[Negated Query]");
            } else {
                result.append("[KB]");
            }
        }

        result.append("</td><td width=40% valign=top>");

        if ((language != null) && (language.length() > 0)) {
            result.append(NLformatter.htmlParaphrase(kbHref, f.theFormula,
                    KBmanager.getMgr().getKB(kbName).getFormatMap(language),
                    KBmanager.getMgr().getKB(kbName).getTermFormatMap(language),
                    language));
        }

        result.append("</td>");

        return result.toString();
    }

    /**
     * Show a hyperlinked list of terms.
     *
     * @param terms DOCUMENT ME!
     * @param kbHref DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String termList(ArrayList terms, String kbHref) {
        StringBuffer show = new StringBuffer();

        for (int i = 0; i < terms.size(); i++) {
            String term = (String) terms.get(i);
            show.append("<a href=\"" + kbHref + "&term=" + term + "\">" + term +
                "</a>");

            if (i < (terms.size() - 1)) {
                show.append(", ");
            }
        }

        return show.toString();
    }

    /**
     * Create the HTML for a section of the Sigma term browser page.
     *
     * @param forms DOCUMENT ME!
     * @param header DOCUMENT ME!
     * @param htmlDivider DOCUMENT ME!
     * @param kbHref DOCUMENT ME!
     * @param kb DOCUMENT ME!
     * @param language DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String browserSectionFormat(ArrayList forms, String header,
        String htmlDivider, String kbHref, KB kb, String language) {
        StringBuffer show = new StringBuffer();

        if ((forms != null) && (forms.size() > 0)) {
            Collections.sort(forms);

            if (header != null) {
                show.append("<br><b>&nbsp;" + header + "</B>");
            }

            if (htmlDivider != null) {
                show.append(htmlDivider);
            }

            show.append("<TABLE width=95%%>");

            for (int i = 0; i < forms.size(); i++) {
                Formula f = (Formula) forms.get(i);
                show.append("<TR><TD width=50%% valign=top>");
                show.append(f.htmlFormat(kbHref) +
                    "</td>\n<TD width=10%% valign=top BGCOLOR=#B8CADF>");
                show.append(f.sourceFile.substring(f.sourceFile.lastIndexOf(
                            File.separator) + 1, f.sourceFile.length()) + " " +
                    (new Integer(f.startLine)).toString() + "-" +
                    (new Integer(f.endLine)).toString() +
                    "</TD>\n<TD width=40%% valign=top>");
                show.append(NLformatter.htmlParaphrase(kbHref, f.theFormula,
                        kb.getFormatMap(language),
                        kb.getTermFormatMap(language), language) +
                    "</TD></TR>\n");
            }

            show.append("</TABLE>\n");
        }

        return show.toString();
    }

    /**
     * Create an HTML menu, given an ArrayList of Strings.
     *
     * @param menuName DOCUMENT ME!
     * @param selectedOption DOCUMENT ME!
     * @param options DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String createMenu(String menuName, String selectedOption,
        ArrayList options) {
        StringBuffer result = new StringBuffer();

        result.append("<select name=" + menuName);
        result.append(">\n  ");

        for (int i = 0; i < options.size(); i++) {
            result.append("<option value='");

            String menuItem = (String) options.get(i);
            result.append(menuItem);

            if ((selectedOption != null) &&
                    selectedOption.equalsIgnoreCase(menuItem)) {
                result.append("' selected='yes'>");
            } else {
                result.append("'>");
            }

            result.append(menuItem);
            result.append("</option>");
        }

        result.append("\n</select>\n");

        return result.toString();
    }

    /**
     * Create an HTML formatted result of a query.
     *
     * @param result DOCUMENT ME!
     * @param stmt DOCUMENT ME!
     * @param processedStmt DOCUMENT ME!
     * @param lineHtml DOCUMENT ME!
     * @param kbName DOCUMENT ME!
     * @param language DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String formatProofResult(String result, String stmt,
        String processedStmt, String lineHtml, String kbName, String language) {
        StringBuffer html = new StringBuffer();

        if ((result != null) && (result.toString().length() > 0)) {
            BasicXMLparser res = new BasicXMLparser(result.toString());

            // System.out.print("INFO in AskTell.jsp: Number of XML elements: ");
            // System.out.println(res.elements.size());
            ProofProcessor pp = new ProofProcessor(res.elements);

            for (int i = 0; i < pp.numAnswers(); i++) {
                ArrayList proofSteps = pp.getProofSteps(i);
                proofSteps = new ArrayList(ProofStep.normalizeProofStepNumbers(
                            proofSteps));

                // System.out.print("Proof steps: ");
                // System.out.println(proofSteps.size());
                if (i != 0) {
                    html = html.append(lineHtml + "\n");
                }

                html = html.append("Answer " + "\n");
                html = html.append(i + 1);
                html = html.append(". " + pp.returnAnswer(i) + "\n");

                if (!pp.returnAnswer(i).equalsIgnoreCase("no")) {
                    html = html.append("<P><TABLE width=95%%>" + "\n");

                    for (int j = 0; j < proofSteps.size(); j++) {
                        //System.out.print("Printing proof step: ");
                        //System.out.println(j);
                        html = html.append("<TR>" + "\n");
                        html = html.append("<TD valign=top>" + "\n");
                        html = html.append(j + 1);
                        html = html.append(". </TD>" + "\n");
                        html = html.append(HTMLformatter.proofTableFormat(
                                    stmt, (ProofStep) proofSteps.get(j),
                                    kbName, language) + "\n");

                        // System.out.println(HTMLformatter.proofTableFormat(stmt,(ProofStep) proofSteps.get(j), kbName, language));
                        html = html.append("</TR>\n" + "\n");
                    }

                    html = html.append("</TABLE>" + "\n");
                }
            }
        }

        return html.toString();
    }
}
