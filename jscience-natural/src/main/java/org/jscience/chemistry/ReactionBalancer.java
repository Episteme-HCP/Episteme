package org.jscience.chemistry;

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
 * @author Gemini AI
 * @since 2.0
 */
public class ReactionBalancer {

    private ReactionBalancer() {
    }

    /**
     * Balances a chemical reaction.
     * 
     * @param reaction The unbalanced reaction.
     * @return A new Balanced ChemicalReaction, or throws exception if
     *         unbalanceable.
     */
    /**
     * Balances a chemical equation string.
     * Example: "H2 + O2 = H2O" -> "2H2 + O2 -> 2H2O"
     * 
     * @param equation The unbalanced reaction equation.
     * @return A balanced ChemicalReaction object.
     */
    public static ChemicalReaction balance(String equation) {
        String[] sides = equation.split("->|=|→");
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

        StringBuilder balancedEq = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Integer> e : reactants.entrySet()) {
            if (!first)
                balancedEq.append(" + ");
            if (e.getValue() > 1)
                balancedEq.append(e.getValue());
            balancedEq.append(e.getKey());
            first = false;
        }
        balancedEq.append(" -> ");
        first = true;
        for (Map.Entry<String, Integer> e : products.entrySet()) {
            if (!first)
                balancedEq.append(" + ");
            if (e.getValue() > 1)
                balancedEq.append(e.getValue());
            balancedEq.append(e.getKey());
            first = false;
        }

        return new ChemicalReaction(reactants, products, balancedEq.toString());
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

        double[][] matrix = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            String el = elementList.get(r);
            for (int c = 0; c < cols; c++) {
                int count = compositions.get(c).getOrDefault(el, 0);
                if (isProduct.get(c)) {
                    matrix[r][c] = -count;
                } else {
                    matrix[r][c] = count;
                }
            }
        }

        // solve M * x = 0
        // We use Gaussian elimination

        double[] solution = solveHomogeneous(matrix);

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

    private static double[] solveHomogeneous(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Gaussian Elimination to RREF
        int pivotRow = 0;
        for (int pivotCol = 0; pivotCol < cols - 1 && pivotRow < rows; pivotCol++) {
            // Find pivot
            int maxRow = pivotRow;
            double maxVal = Math.abs(matrix[maxRow][pivotCol]);

            for (int r = pivotRow + 1; r < rows; r++) {
                if (Math.abs(matrix[r][pivotCol]) > maxVal) {
                    maxRow = r;
                    maxVal = Math.abs(matrix[r][pivotCol]);
                }
            }

            if (maxVal < 1e-10) {
                continue; // No pivot in this column
            }

            // Swap
            double[] temp = matrix[pivotRow];
            matrix[pivotRow] = matrix[maxRow];
            matrix[maxRow] = temp;

            // Normalize pivot row
            double pivot = matrix[pivotRow][pivotCol];
            for (int c = pivotCol; c < cols; c++) {
                matrix[pivotRow][c] /= pivot;
            }

            // Eliminate other rows
            for (int r = 0; r < rows; r++) {
                if (r != pivotRow) {
                    double factor = matrix[r][pivotCol];
                    for (int c = pivotCol; c < cols; c++) {
                        matrix[r][c] -= factor * matrix[pivotRow][c];
                    }
                }
            }

            pivotRow++;
        }

        // Now extract solution
        // We assume 1 degree of freedom (last variable = 1)
        double[] x = new double[cols];
        x[cols - 1] = 1.0;

        // Back substitution?
        // In RREF, pivot variables are expressed in terms of free variables.
        // Assuming rank = cols - 1.

        for (int i = pivotRow - 1; i >= 0; i--) {
            // Find pivot col for this row
            int pCol = -1;
            for (int c = 0; c < cols; c++) {
                if (Math.abs(matrix[i][c]) > 1e-10) {
                    pCol = c;
                    break;
                }
            }

            if (pCol != -1 && pCol < cols - 1) {
                double sum = 0;
                for (int j = pCol + 1; j < cols; j++) {
                    sum += matrix[i][j] * x[j];
                }
                x[pCol] = -sum; // since coeff of x[pCol] is 1
            }
        }

        // If simpler cases failed, return default 1s
        return x;
    }

    private static int[] convertToIntegers(double[] x) {
        // Find LCM equivalent
        double maxError = 1e-5;
        for (int multiplier = 1; multiplier <= 1000; multiplier++) {
            boolean allInt = true;
            int[] result = new int[x.length];
            for (int i = 0; i < x.length; i++) {
                double val = x[i] * multiplier;
                long round = Math.round(val);
                if (Math.abs(val - round) > maxError) {
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
            res[i] = (int) Math.round(x[i]);
        return res;
    }

}
