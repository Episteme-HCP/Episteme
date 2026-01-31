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

package org.jscience.natural.chemistry;

import org.jscience.core.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility for balancing chemical equations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ReactionBalancer {

    private ReactionBalancer() {
    }

    /**
     * Balances a chemical equation string.
     * Example: "H2 + O2 = H2O" -> "2H2 + O2 -> 2H2O"
     * 
     * @param equation The unbalanced reaction equation.
     * @return A balanced ChemicalReaction object.
     */
    public static ChemicalReaction balance(String equation) {
        String[] sides = equation.split("->|=|â†’");
        if (sides.length != 2) {
            throw new IllegalArgumentException("Invalid equation format: " + equation);
        }

        List<String> compounds = new ArrayList<>();
        List<Boolean> isProduct = new ArrayList<>();

        // Parse left side
        for (String term : sides[0].split("\\+")) {
            String species = parseSpecies(term.trim());
            compounds.add(species);
            isProduct.add(false);
        }

        // Parse right side
        for (String term : sides[1].split("\\+")) {
            String species = parseSpecies(term.trim());
            compounds.add(species);
            isProduct.add(true);
        }

        Map<String, Integer> coeffs = balance(compounds, isProduct);

        Map<String, Integer> reactants = new HashMap<>();
        Map<String, Integer> products = new HashMap<>();

        for (int i = 0; i < compounds.size(); i++) {
            String species = compounds.get(i);
            int coeff = coeffs.get(species);
            if (isProduct.get(i)) {
                products.put(species, coeff);
            } else {
                reactants.put(species, coeff);
            }
        }

        return new ChemicalReaction(reactants, products, formatBalanced(reactants, products));
    }

    private static String formatBalanced(Map<String, Integer> reactants, Map<String, Integer> products) {
        StringBuilder sb = new StringBuilder();
        appendCompoundMap(sb, reactants);
        sb.append(" -> ");
        appendCompoundMap(sb, products);
        return sb.toString();
    }

    private static void appendCompoundMap(StringBuilder sb, Map<String, Integer> map) {
        boolean first = true;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (!first)
                sb.append(" + ");
            if (e.getValue() > 1)
                sb.append(e.getValue());
            sb.append(e.getKey());
            first = false;
        }
    }

    private static String parseSpecies(String term) {
        // Remove existing coefficients if any
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\d*\\s*([A-Z][a-zA-Z0-9()]*)$");
        java.util.regex.Matcher matcher = pattern.matcher(term);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return term;
    }

    /**
     * Balances coefficients for a given set of reactants and products.
     * 
     * @param compounds List of all compound formulas involved.
     *                  Reactants first, then Products.
     * @param isProduct Boolean array corresponding to compounds.
     * @return Map of Compound -> Coefficient
     */
    public static Map<String, Integer> balance(List<String> compounds, List<Boolean> isProduct) {
        // 1. Identify Elements
        Set<String> elements = new HashSet<>();
        List<Map<String, Integer>> compositions = new ArrayList<>();

        for (String comp : compounds) {
            Map<String, Integer> atoms = parseFormula(comp);
            compositions.add(atoms);
            elements.addAll(atoms.keySet());
        }

        List<String> elementList = new ArrayList<>(elements);
        int rows = elementList.size(); // Number of elements
        int cols = compounds.size(); // Number of variables

        // Matrix M of size (rows) x (cols)
        // Entry M[i][j] = count of element i in compound j
        // If compound j is product, count is negative (to sum to 0)

        Real[][] matrix = new Real[rows][cols];

        for (int r = 0; r < rows; r++) {
            String el = elementList.get(r);
            for (int c = 0; c < cols; c++) {
                int count = compositions.get(c).getOrDefault(el, 0);
                if (isProduct.get(c)) {
                    matrix[r][c] = Real.of(-count);
                } else {
                    matrix[r][c] = Real.of(count);
                }
            }
        }

        // solve M * x = 0
        // We use Gaussian elimination
        Real[] solution = solveHomogeneous(matrix);

        // Convert to integers
        int[] integerSolution = convertToIntegers(solution);

        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < cols; i++) {
            result.put(compounds.get(i), integerSolution[i]);
        }

        return result;
    }

    private static Map<String, Integer> parseFormula(String formula) {
        Map<String, Integer> atoms = new HashMap<>();
        parseFormulaRecursive(formula, 1, atoms);
        return atoms;
    }

    private static void parseFormulaRecursive(String part, int multiplier, Map<String, Integer> atomCounts) {
        int i = 0;
        int n = part.length();

        while (i < n) {
            char c = part.charAt(i);

            if (c == '(') {
                // Find matching closing parenthesis
                int depth = 1;
                int j = i + 1;
                while (j < n && depth > 0) {
                    if (part.charAt(j) == '(')
                        depth++;
                    else if (part.charAt(j) == ')')
                        depth--;
                    j++;
                }

                if (depth != 0)
                    throw new IllegalArgumentException("Unmatched parentheses in: " + part);

                String inner = part.substring(i + 1, j - 1);

                // Check if followed by number
                int count = 1;
                int k = j;
                while (k < n && Character.isDigit(part.charAt(k))) {
                    k++;
                }
                if (k > j) {
                    count = Integer.parseInt(part.substring(j, k));
                }

                parseFormulaRecursive(inner, multiplier * count, atomCounts);
                i = k;
            } else if (Character.isUpperCase(c)) {
                // Element
                int j = i + 1;
                while (j < n && Character.isLowerCase(part.charAt(j))) {
                    j++;
                }
                String element = part.substring(i, j);

                // Number
                int count = 1;
                int k = j;
                while (k < n && Character.isDigit(part.charAt(k))) {
                    k++;
                }
                if (k > j) {
                    count = Integer.parseInt(part.substring(j, k));
                }

                atomCounts.merge(element, count * multiplier, (a, b) -> a + b);
                i = k;
            } else {
                // Skip spaces or unknown (shouldn't happen in clean formula)
                i++;
            }
        }
    }

    private static Real[] solveHomogeneous(Real[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Gaussian Elimination to RREF
        int pivotRow = 0;
        for (int pivotCol = 0; pivotCol < cols - 1 && pivotRow < rows; pivotCol++) {
            // Find pivot
            int maxRow = pivotRow;
            double maxVal = Math.abs(matrix[maxRow][pivotCol].doubleValue());

            for (int r = pivotRow + 1; r < rows; r++) {
                if (Math.abs(matrix[r][pivotCol].doubleValue()) > maxVal) {
                    maxRow = r;
                    maxVal = Math.abs(matrix[r][pivotCol].doubleValue());
                }
            }

            if (maxVal < 1e-12) {
                continue; // No pivot in this column
            }

            // Swap
            Real[] temp = matrix[pivotRow];
            matrix[pivotRow] = matrix[maxRow];
            matrix[maxRow] = temp;

            // Normalize pivot row
            Real pivot = matrix[pivotRow][pivotCol];
            for (int c = pivotCol; c < cols; c++) {
                matrix[pivotRow][c] = matrix[pivotRow][c].divide(pivot);
            }

            // Eliminate other rows
            for (int r = 0; r < rows; r++) {
                if (r != pivotRow) {
                    Real factor = matrix[r][pivotCol];
                    for (int c = pivotCol; c < cols; c++) {
                        matrix[r][c] = matrix[r][c].subtract(factor.multiply(matrix[pivotRow][c]));
                    }
                }
            }

            pivotRow++;
        }

        // Now extract solution
        // We assume 1 degree of freedom (last variable = 1)
        Real[] x = new Real[cols];
        java.util.Arrays.fill(x, Real.ZERO);
        x[cols - 1] = Real.ONE;

        for (int i = pivotRow - 1; i >= 0; i--) {
            // Find pivot col for this row
            int pCol = -1;
            for (int c = 0; c < cols; c++) {
                if (Math.abs(matrix[i][c].doubleValue()) > 1e-12) {
                    pCol = c;
                    break;
                }
            }

            if (pCol != -1 && pCol < cols - 1) {
                Real sum = Real.ZERO;
                for (int j = pCol + 1; j < cols; j++) {
                    sum = sum.add(matrix[i][j].multiply(x[j]));
                }
                x[pCol] = sum.negate(); // since coeff of x[pCol] is 1
            }
        }

        return x;
    }

    private static int[] convertToIntegers(Real[] x) {
        double maxError = 1e-6;
        for (int multiplier = 1; multiplier <= 5000; multiplier++) {
            boolean allInt = true;
            int[] result = new int[x.length];
            for (int i = 0; i < x.length; i++) {
                double val = Math.abs(x[i].doubleValue() * multiplier);
                long round = Math.round(val);
                if (Math.abs(val - round) > maxError || round == 0) {
                    allInt = false;
                    break;
                }
                result[i] = (int) round;
            }
            if (allInt)
                return result;
        }
        // Fallback
        int[] res = new int[x.length];
        for (int i = 0; i < x.length; i++)
            res[i] = (int) Math.round(Math.abs(x[i].doubleValue()));
        return res;
    }

}

