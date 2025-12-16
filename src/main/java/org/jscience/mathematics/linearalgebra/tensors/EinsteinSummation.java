package org.jscience.mathematics.linearalgebra.tensors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility for Einstein summation convention (einsum).
 * <p>
 * Evaluates tensor contractions based on a subscript string.
 * Example: "ij,jk->ik" corresponds to matrix multiplication.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class EinsteinSummation {

    private EinsteinSummation() {
    }

    /**
     * Performs Einstein summation.
     * 
     * @param equation the equation string (e.g. "ij,jk->ik")
     * @param operands the tensor operands
     * @return the resulting tensor
     * @param <T> the field element type
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> Tensor<T> einsum(String equation, Tensor<T>... operands) {
        String[] parts = equation.replaceAll("\\s+", "").split("->");
        String inputs = parts[0];
        String outputIndices = parts.length > 1 ? parts[1] : null;

        String[] inputTerms = inputs.split(",");
        if (inputTerms.length != operands.length) {
            throw new IllegalArgumentException(
                    "Number of operands does not match equation terms: " + inputTerms.length + " vs "
                            + operands.length);
        }

        // Map characters to dimension sizes
        Map<Character, Integer> dimSizes = new HashMap<>();

        for (int i = 0; i < operands.length; i++) {
            String term = inputTerms[i];
            Tensor<T> tensor = operands[i];
            int[] shape = tensor.shape();

            if (term.length() != shape.length) {
                throw new IllegalArgumentException(
                        "Operand " + i + " indices " + term + " do not match rank " + shape.length);
            }

            for (int k = 0; k < term.length(); k++) {
                char c = term.charAt(k);
                int size = shape[k];
                if (dimSizes.containsKey(c)) {
                    if (dimSizes.get(c) != size) {
                        throw new IllegalArgumentException(
                                "Dimension mismatch for index '" + c + "': " + dimSizes.get(c) + " vs " + size);
                    }
                } else {
                    dimSizes.put(c, size);
                }
            }
        }

        // Determine output indices if not provided (implicit mode)
        if (outputIndices == null) {
            Map<Character, Integer> counts = new HashMap<>();
            for (char c : inputs.toCharArray()) {
                if (c == ',')
                    continue;
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }
            StringBuilder sb = new StringBuilder();
            List<Character> sortedChars = new ArrayList<>(counts.keySet());
            java.util.Collections.sort(sortedChars);

            for (char c : sortedChars) {
                if (counts.get(c) == 1) {
                    sb.append(c);
                }
            }
            outputIndices = sb.toString();
        }

        // Determine summation indices (in inputs but not in output)
        Set<Character> outputSet = new HashSet<>();
        for (char c : outputIndices.toCharArray())
            outputSet.add(c);

        List<Character> summationIndices = new ArrayList<>();
        for (Character c : dimSizes.keySet()) {
            if (!outputSet.contains(c)) {
                summationIndices.add(c);
            }
        }

        // Calculate output shape
        int[] outputShape = new int[outputIndices.length()];
        for (int i = 0; i < outputIndices.length(); i++) {
            outputShape[i] = dimSizes.get(outputIndices.charAt(i));
        }

        return compute(operands, inputTerms, outputIndices, summationIndices, dimSizes, outputShape);
    }

    @SuppressWarnings("unchecked")
    private static <T> Tensor<T> compute(Tensor<T>[] operands, String[] inputTerms,
            String outputIndices, List<Character> sumIndices, Map<Character, Integer> dimSizes, int[] outputShape) {

        int outputSize = 1;
        for (int s : outputShape) {
            outputSize *= s;
        }

        if (operands[0].size() == 0) {
            throw new IllegalArgumentException("Cannot compute einsum on empty tensors");
        }

        // Get zero element and class type from first operand
        // We use int[] indices of correct rank (zeros)
        T sample = operands[0].get(new int[operands[0].rank()]);
        T zero = ((org.jscience.mathematics.structures.rings.Ring<T>) sample).zero();
        Class<?> type = sample.getClass();

        Object resultDataObj = java.lang.reflect.Array.newInstance(type, outputSize);
        T[] resultData = (T[]) resultDataObj;
        for (int i = 0; i < outputSize; i++) {
            resultData[i] = zero;
        }

        // Setup iteration variables
        List<Character> allVars = new ArrayList<>(dimSizes.keySet());
        int[] limits = new int[allVars.size()];
        for (int i = 0; i < allVars.size(); i++) {
            limits[i] = dimSizes.get(allVars.get(i));
        }

        int[] counters = new int[allVars.size()];

        Map<Character, Integer> varMap = new HashMap<>();
        for (int i = 0; i < allVars.size(); i++) {
            varMap.put(allVars.get(i), i);
        }

        // Cache mapping from operand index -> list of indices in 'counters'
        int[][] opVarIndices = new int[operands.length][];
        for (int i = 0; i < operands.length; i++) {
            String term = inputTerms[i];
            opVarIndices[i] = new int[term.length()];
            for (int j = 0; j < term.length(); j++) {
                opVarIndices[i][j] = varMap.get(term.charAt(j));
            }
        }

        // Cache output indices mapping
        int[] outVarIndices = new int[outputIndices.length()];
        for (int i = 0; i < outputIndices.length(); i++) {
            outVarIndices[i] = varMap.get(outputIndices.charAt(i));
        }

        // Pre-calculate output strides
        int[] outputStrides = new int[outputShape.length];
        int stride = 1;
        for (int i = outputShape.length - 1; i >= 0; i--) {
            outputStrides[i] = stride;
            stride *= outputShape[i];
        }

        boolean done = false;
        long totalIterations = 1;
        for (int l : limits)
            totalIterations *= l;
        if (totalIterations == 0)
            done = true;

        while (!done) {
            // 1. Compute product
            T product = null;
            for (int i = 0; i < operands.length; i++) {
                int[] indices = new int[opVarIndices[i].length];
                for (int k = 0; k < indices.length; k++) {
                    indices[k] = counters[opVarIndices[i][k]];
                }

                T val = operands[i].get(indices);

                if (product == null) {
                    product = val;
                } else {
                    product = ((org.jscience.mathematics.structures.rings.Ring<T>) product).multiply(product, val);
                }
            }

            // 2. Add to output
            int outIdx = 0;
            for (int k = 0; k < outVarIndices.length; k++) {
                outIdx += counters[outVarIndices[k]] * outputStrides[k];
            }

            resultData[outIdx] = ((org.jscience.mathematics.structures.rings.Ring<T>) resultData[outIdx])
                    .add(resultData[outIdx], product);

            // 3. Increment counters
            if (allVars.isEmpty()) {
                done = true;
            } else {
                for (int i = allVars.size() - 1; i >= 0; i--) {
                    counters[i]++;
                    if (counters[i] < limits[i]) {
                        break;
                    } else {
                        counters[i] = 0;
                        if (i == 0) {
                            done = true;
                        }
                    }
                }
            }
        }

        return new DenseTensor<>(resultData, outputShape);
    }
}
