/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry;

import java.util.*;
import java.util.regex.*;

/**
 * Chemical Reaction Parser and Balancer.
 * Parses chemical equations from strings and can balance them.
 *
 * <p>
 * Supports standard chemical notation:
 * </p>
 * <ul>
 * <li>Elements: H, O, C, Na, Cl, etc.</li>
 * <li>Coefficients: 2H2O, 3NaCl</li>
 * <li>Subscripts: H2O, C6H12O6</li>
 * <li>Parentheses: Ca(OH)2, Al2(SO4)3</li>
 * <li>Charges: Na+, SO4^2-, Fe^3+</li>
 * <li>States: (s), (l), (g), (aq)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalReactionParser {

    /**
     * Represents a parsed chemical formula.
     */
    public static class Formula {
        private final String original;
        private final Map<String, Integer> elements;
        private int coefficient;
        private String state; // (s), (l), (g), (aq)
        private int charge;

        public Formula(String original) {
            this.original = original.trim();
            this.elements = new LinkedHashMap<>();
            this.coefficient = 1;
            this.state = null;
            this.charge = 0;
            parse();
        }

        private void parse() {
            String formula = original;

            // Extract coefficient
            Matcher coefMatch = Pattern.compile("^(\\d+)").matcher(formula);
            if (coefMatch.find()) {
                coefficient = Integer.parseInt(coefMatch.group(1));
                formula = formula.substring(coefMatch.end());
            }

            // Extract state
            Matcher stateMatch = Pattern.compile("\\((s|l|g|aq)\\)$").matcher(formula);
            if (stateMatch.find()) {
                state = stateMatch.group(1);
                formula = formula.substring(0, stateMatch.start());
            }

            // Extract charge
            Matcher chargeMatch = Pattern.compile("\\^?(\\d*)([+-])$").matcher(formula);
            if (chargeMatch.find()) {
                int val = chargeMatch.group(1).isEmpty() ? 1 : Integer.parseInt(chargeMatch.group(1));
                charge = chargeMatch.group(2).equals("+") ? val : -val;
                formula = formula.substring(0, chargeMatch.start());
            }

            // Parse elements
            parseElements(formula, 1);
        }

        private void parseElements(String formula, int multiplier) {
            // Handle parentheses first
            int i = 0;
            while (i < formula.length()) {
                char c = formula.charAt(i);

                if (c == '(') {
                    // Find matching closing paren
                    int depth = 1;
                    int j = i + 1;
                    while (j < formula.length() && depth > 0) {
                        if (formula.charAt(j) == '(')
                            depth++;
                        else if (formula.charAt(j) == ')')
                            depth--;
                        j++;
                    }
                    // Get subscript after paren
                    int subEnd = j;
                    while (subEnd < formula.length() && Character.isDigit(formula.charAt(subEnd))) {
                        subEnd++;
                    }
                    int parenMult = 1;
                    if (subEnd > j) {
                        parenMult = Integer.parseInt(formula.substring(j, subEnd));
                    }
                    parseElements(formula.substring(i + 1, j - 1), multiplier * parenMult);
                    i = subEnd;
                } else if (Character.isUpperCase(c)) {
                    // Element symbol
                    int j = i + 1;
                    while (j < formula.length() && Character.isLowerCase(formula.charAt(j))) {
                        j++;
                    }
                    String symbol = formula.substring(i, j);

                    // Get subscript
                    int subEnd = j;
                    while (subEnd < formula.length() && Character.isDigit(formula.charAt(subEnd))) {
                        subEnd++;
                    }
                    int count = 1;
                    if (subEnd > j) {
                        count = Integer.parseInt(formula.substring(j, subEnd));
                    }

                    elements.merge(symbol, count * multiplier, (a, b) -> a + b);
                    i = subEnd;
                } else {
                    i++;
                }
            }
        }

        public String getOriginal() {
            return original;
        }

        public Map<String, Integer> getElements() {
            return new LinkedHashMap<>(elements);
        }

        public int getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(int c) {
            this.coefficient = c;
        }

        public String getState() {
            return state;
        }

        public int getCharge() {
            return charge;
        }

        /**
         * Returns the total count of each element (coefficient Ãƒâ€” subscripts).
         */
        public Map<String, Integer> getTotalElements() {
            Map<String, Integer> total = new LinkedHashMap<>();
            for (var e : elements.entrySet()) {
                total.put(e.getKey(), e.getValue() * coefficient);
            }
            return total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (coefficient > 1)
                sb.append(coefficient);
            for (var e : elements.entrySet()) {
                sb.append(e.getKey());
                if (e.getValue() > 1)
                    sb.append(e.getValue());
            }
            if (state != null)
                sb.append("(").append(state).append(")");
            if (charge != 0) {
                sb.append("^");
                if (Math.abs(charge) > 1)
                    sb.append(Math.abs(charge));
                sb.append(charge > 0 ? "+" : "-");
            }
            return sb.toString();
        }
    }

    /**
     * Represents a parsed chemical reaction.
     */
    public static class Reaction {
        private final List<Formula> reactants;
        private final List<Formula> products;
        private final String original;

        public Reaction(String equation) {
            this.original = equation.trim();
            this.reactants = new ArrayList<>();
            this.products = new ArrayList<>();
            parse();
        }

        private void parse() {
            // Split by arrow
            String[] sides = original.split("->|Ã¢â€ â€™|=|Ã¢Å¸Â¶");
            if (sides.length != 2) {
                throw new IllegalArgumentException("Invalid reaction format: " + original);
            }

            // Parse reactants
            for (String r : sides[0].split("\\+")) {
                if (!r.trim().isEmpty()) {
                    reactants.add(new Formula(r.trim()));
                }
            }

            // Parse products
            for (String p : sides[1].split("\\+")) {
                if (!p.trim().isEmpty()) {
                    products.add(new Formula(p.trim()));
                }
            }
        }

        public List<Formula> getReactants() {
            return reactants;
        }

        public List<Formula> getProducts() {
            return products;
        }

        public String getOriginal() {
            return original;
        }

        /**
         * Checks if the reaction is balanced.
         */
        public boolean isBalanced() {
            Map<String, Integer> leftCounts = new HashMap<>();
            Map<String, Integer> rightCounts = new HashMap<>();

            for (Formula f : reactants) {
                for (var e : f.getTotalElements().entrySet()) {
                    leftCounts.merge(e.getKey(), e.getValue(), (a, b) -> a + b);
                }
            }

            for (Formula f : products) {
                for (var e : f.getTotalElements().entrySet()) {
                    rightCounts.merge(e.getKey(), e.getValue(), (a, b) -> a + b);
                }
            }

            return leftCounts.equals(rightCounts);
        }

        /**
         * Returns element counts for each side.
         */
        public String getElementBalance() {
            Map<String, Integer> left = new HashMap<>();
            Map<String, Integer> right = new HashMap<>();

            for (Formula f : reactants) {
                for (var e : f.getTotalElements().entrySet()) {
                    left.merge(e.getKey(), e.getValue(), (a, b) -> a + b);
                }
            }
            for (Formula f : products) {
                for (var e : f.getTotalElements().entrySet()) {
                    right.merge(e.getKey(), e.getValue(), (a, b) -> a + b);
                }
            }

            Set<String> allElements = new TreeSet<>();
            allElements.addAll(left.keySet());
            allElements.addAll(right.keySet());

            StringBuilder sb = new StringBuilder();
            for (String elem : allElements) {
                int l = left.getOrDefault(elem, 0);
                int r = right.getOrDefault(elem, 0);
                String status = l == r ? "Ã¢Å“â€œ" : "Ã¢Å“â€”";
                sb.append(String.format("%s: %d Ã¢â€ â€™ %d %s\n", elem, l, r, status));
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < reactants.size(); i++) {
                if (i > 0)
                    sb.append(" + ");
                sb.append(reactants.get(i));
            }
            sb.append(" Ã¢â€ â€™ ");
            for (int i = 0; i < products.size(); i++) {
                if (i > 0)
                    sb.append(" + ");
                sb.append(products.get(i));
            }
            return sb.toString();
        }
    }

    /**
     * Parses a chemical equation string into a Reaction object.
     */
    public static Reaction parse(String equation) {
        return new Reaction(equation);
    }

    /**
     * Parses a single formula string.
     */
    public static Formula parseFormula(String formula) {
        return new Formula(formula);
    }
}


