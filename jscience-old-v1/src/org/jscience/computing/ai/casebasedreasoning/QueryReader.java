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

package org.jscience.computing.ai.casebasedreasoning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/**

 *

 */
public class QueryReader {
    private static final String FIELD_DELIMITER = "|";
    private static final String CONSTRAINT_LINE_INDICATOR = "c";
    private static final String WEIGHT_LINE_INDICATOR = "w";
    private static final String DEFAULT_FILE_NAME = "query.txt";

    //--- Static for performance. i don't want to have each instance
    //---   of this load its own data from the data source
    private static FilterCriteria filterCriteria = new FilterCriteria();
    private static SimilarityCriteria similarityCriteria = new SimilarityCriteria();
    private static SimilarityWeights similarityWeights = new SimilarityWeights();

    /**

     *

     */
    public QueryReader() {
        init();
    } //--- constructor

    /**

     *

     */
    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    } //--- getFilterCriteria

    /**

     *

     */
    public SimilarityCriteria getSimilarityCriteria() {
        return similarityCriteria;
    } //--- getFilterCriteria

    /**

     *

     */
    public SimilarityWeights getSimilarityWeights() {
        return similarityWeights;
    } //--- getFilterCriteria

    private void init() {
        loadData();
    } //--- init

    /**
     * *********************************************************************
     * <p/>
     * What can i say, this method is a fucking mess
     * <p/>
     * i hate methods longer than ~10 lines, and this one just looks like
     * <p/>
     * some huge, amateur script
     * <p/>
     * Oh well, i just wanted it to work. i'll come back and make it good
     * <p/>
     * after i get the basic functionality working
     * <p/>
     * **********************************************************************
     */
    protected void loadData() {
        final int FIELD_INDEX = 1;

        final int OPERATOR_INDEX = 2;

        final int WEIGHT_INDEX = 2;

        final int VALUE_INDEX = 3;

        //--- If we've already loaded everything, don't bother doing so again
        if (filterCriteria.size() != 0) {
            return;
        }

        try {
            String fileName = DEFAULT_FILE_NAME;

            BufferedReader in = new BufferedReader(new FileReader(fileName));

            String line;

            //--- Read all the lines in
            while ((line = in.readLine()) != null) {
                //--- Just in case, get rid of any starting spaces
                line = line.toLowerCase().trim();

                //--- What type of string is this?
                //--- We can tell from the first character
                String lineType = null;

                if (line.length() != 0) {
                    lineType = line.substring(0, 1);
                } else {
                    //--- Gotta make this something so that we don't get a null
                    //---   object error later on
                    lineType = "";
                }

                //--- We have a couple different types of lines:
                //---   - blank
                //---   - comment (#)
                //---   - constraint/filter (c)
                //---	- weight (w)
                //--- We're only going to work with data and field definition lines
                if (!(lineType.equalsIgnoreCase(CONSTRAINT_LINE_INDICATOR)) &&
                        !(lineType.equalsIgnoreCase(WEIGHT_LINE_INDICATOR))) {
                    continue;
                }

                //----------------------------------------------------
                //--- OK, we got a string in a format similar to
                //---   type | field | operator | value  or
                //---   type | field | weight
                //--- Examples:
                //---   c | maker   | !% | hp
                //---   c | maker   | != | dell
                //---   c | dvd     | %  | y
                //---   w | maker   | 2
                //----------------------------------------------------
                //--- We have a constraint definition line
                if (lineType.equalsIgnoreCase(CONSTRAINT_LINE_INDICATOR)) {
                    //--- Break the line apart
                    //--- Should end up with four tokens
                    //---   1. type (constraint)
                    //---   2. field
                    //---   3. operator
                    //---   4. value
                    StringTokenizer tokenizer = new StringTokenizer(line,
                            FIELD_DELIMITER);

                    //--- This probably isn't necessary, but i'm going to
                    //---   move these tokens to an array for direct access
                    String[] tokens = new String[tokenizer.countTokens()];

                    int i = 0;

                    while (tokenizer.hasMoreElements()) {
                        String token = (String) tokenizer.nextElement();

                        token = token.toLowerCase().trim();

                        tokens[i++] = token;
                    }

                    String fieldName = tokens[FIELD_INDEX];

                    String operator = tokens[OPERATOR_INDEX];

                    String value = tokens[VALUE_INDEX];

                    //--- Is this a hard filter constraint
                    //---   or a similarity criteria?
                    boolean isAHardConstraint = (!((operator.equals("~")) ||
                            (operator.equals("%")) || (operator.equals("!%"))));

                    //--- Hard constraints (filters) go to the
                    //---   FilterCriteria collection
                    if (isAHardConstraint) {
                        filterCriteria.add(fieldName, operator, value);
                    } else {
                        similarityCriteria.add(fieldName, operator, value);
                    }
                } //--- lineType = constraint

                //--- We have a weight definition line
                if (lineType.equalsIgnoreCase(WEIGHT_LINE_INDICATOR)) {
                    //--- Break the line apart
                    //--- Should end up with three tokens
                    //---   1. type (constraint)
                    //---   2. field
                    //---   3. weight
                    StringTokenizer tokenizer = new StringTokenizer(line,
                            FIELD_DELIMITER);

                    //--- This probably isn't necessary, but i'm going to
                    //---   move these tokens to an array for direct access
                    String[] tokens = new String[tokenizer.countTokens()];

                    int i = 0;

                    while (tokenizer.hasMoreElements()) {
                        String token = (String) tokenizer.nextElement();

                        token = token.toLowerCase().trim();

                        tokens[i++] = token;
                    }

                    String fieldName = tokens[FIELD_INDEX];

                    Integer weight = new Integer(tokens[WEIGHT_INDEX]);

                    similarityWeights.add(fieldName, weight.intValue());
                } //--- lineType = weight
            } //--- while in.readLine

            in.close();
        } //--- try
        catch (Exception e) {
            String methodName = "ItemManager::loadItems";

            System.out.println(methodName + " error: " + e);
        } //--- catch
    } //--- loadItems
} //--- QueryReader
