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

package org.jscience.computing.loaders.pmml;

import java.util.*;

/** Data field in PMML data dictionary.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DataField {
    private String name;
    private String optype;
    private String dataType;
    private final List<String> values = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    
    public String getOptype() { return optype; }
    public void setOptype(String o) { this.optype = o; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String t) { this.dataType = t; }
    
    public void addValue(String v) { if (v != null) values.add(v); }
    public List<String> getValues() { return Collections.unmodifiableList(values); }
    
    public boolean isCategorical() { return "categorical".equals(optype); }
    public boolean isContinuous() { return "continuous".equals(optype); }
    
    @Override
    public String toString() { return name + " (" + dataType + ", " + optype + ")"; }
}
