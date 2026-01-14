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

/**

 *

 */
public class ItemManager {
    private static final String FIELD_LINE_INDICATOR = "f";
    private static final String DATA_LINE_INDICATOR = "d";
    private static final String DEFAULT_FILE_NAME = "data.txt";

    //--- Static for performance. i don't want to have each instance
    //---   of this load its own data from the data source
    private static TraitDescriptors traitDescriptors = new TraitDescriptors();
    private static Items items = new Items(traitDescriptors);

    ItemManager() {
        init();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Items getItems() {
        return items;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitDescriptors getTraitDescriptors() {
        return traitDescriptors;
    }

    private void init() {
        loadItems();
    }

    /**
     * *********************************************************************
     * <p/>
     * <p/>
     * <p/>
     * **********************************************************************
     */
    protected void loadItems() {
        //--- If we've already loaded everything, don't bother doing so again
        if (items.size() != 0) {
            return;
        }

        try {
            String fileName = DEFAULT_FILE_NAME;

            BufferedReader in = new BufferedReader(new FileReader(fileName));

            String line;

            //--- Read all the lines in
            while ((line = in.readLine()) != null) {
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

                //--- We're only going to work with data and field definition lines
                if (!(lineType.equalsIgnoreCase(FIELD_LINE_INDICATOR)) &&
                        !(lineType.equalsIgnoreCase(DATA_LINE_INDICATOR))) {
                    continue;
                }

                //--- OK, we got a string in a format similar to
                //---   D=val1|val2|val3 or
                //---   F=s:val1|i:val2|f:val3|b:val4
                //--- Let's get rid of the tag and = sign
                int equalSignPosition = line.indexOf('=');

                line = line.substring(equalSignPosition + 1);

                //--- We want to take the data and put it into the fields
                //---   we have set up
                //--- So if we haven't found any fields yet, we can't do anything
                //---   with the data lines
                if (traitDescriptors.isEmpty()) {
                    if (!(lineType.equalsIgnoreCase(FIELD_LINE_INDICATOR))) {
                        continue;
                    } //--- field_line_indicator
                } //--- traitDescriptors.isEmpty

                //--- We have a field definition line
                if (lineType.equalsIgnoreCase(FIELD_LINE_INDICATOR)) {
                    //--- Make sure we haven't already processed a field definition line
                    if (!(traitDescriptors.isEmpty())) {
                        //--- Oops, someone's trying to sneak an extra definition row in here
                        //--- Let's just skip this row
                        continue;
                    } //--- field_line_indicator

                    traitDescriptors.loadFromDelimitedString(line);
                } //--- lineType = field

                //--- We got a line of data
                if (lineType.equalsIgnoreCase(DATA_LINE_INDICATOR)) {
                    items.add(getTraitDescriptors(), line);
                } //--- lineType = data
            } //--- while in.readLine

            in.close();
        } //--- try
        catch (Exception e) {
            String methodName = "ItemManager::loadItems";

            System.out.println(methodName + " error: " + e);
        } //--- catch
    } //--- loadItems
}
