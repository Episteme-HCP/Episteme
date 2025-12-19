package org.jscience.computing.ai.gp;

import java.util.Random;
import java.util.function.DoubleBinaryOperator;

/**
 * Basic genetic programming framework.
 * Evolves expression trees to solve symbolic regression problems.
 */
public class GeneticProgramming {

    private final Random random = new Random();
    private int maxDepth = 4;
    private double mutationRate = 0.1;
    private double crossoverRate = 0.7;

    public void setMaxDepth(int depth) {
        this.maxDepth = depth;
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    public void setCrossoverRate(double rate) {
        this.crossoverRate = rate;
    }

    /**
     * Represents an expression tree node.
     */
    public abstract static class Node {
        public abstract double evaluate(double x);

        public abstract Node copy();

        public abstract int depth();
    }

    /**
     * Constant node.
     */
    public static class ConstNode extends Node {
        private final double value;

        public ConstNode(double value) {
            this.value = value;
        }

        @Override
        public double evaluate(double x) {
            return value;
        }

        @Override
        public Node copy() {
            return new ConstNode(value);
        }

        @Override
        public int depth() {
            return 1;
        }

        @Override
        public String toString() {
            return String.format("%.2f", value);
        }
    }

    /**
     * Variable node (x).
     */
    public static class VarNode extends Node {
        @Override
        public double evaluate(double x) {
            return x;
        }

        @Override
        public Node copy() {
            return new VarNode();
        }

        @Override
        public int depth() {
            return 1;
        }

        @Override
        public String toString() {
            return "x";
        }
    }

    /**
     * Binary operator node.
     */
    public static class OpNode extends Node {
        private final String symbol;
        private final DoubleBinaryOperator op;
        private Node left, right;

        public OpNode(String symbol, DoubleBinaryOperator op, Node left, Node right) {
            this.symbol = symbol;
            this.op = op;
            this.left = left;
            this.right = right;
        }

        @Override
        public double evaluate(double x) {
            return op.applyAsDouble(left.evaluate(x), right.evaluate(x));
        }

        @Override
        public Node copy() {
            return new OpNode(symbol, op, left.copy(), right.copy());
        }

        @Override
        public int depth() {
            return 1 + Math.max(left.depth(), right.depth());
        }

        @Override
        public String toString() {
            return "(" + left + " " + symbol + " " + right + ")";
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node n) {
            this.left = n;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node n) {
            this.right = n;
        }
    }

    /**
     * Generates a random expression tree.
     */
    public Node randomTree(int depth) {
        if (depth <= 1 || random.nextDouble() < 0.3) {
            return random.nextBoolean() ? new VarNode() : new ConstNode(random.nextDouble() * 10 - 5);
        }

        String[] ops = { "+", "-", "*", "/" };
        String op = ops[random.nextInt(ops.length)];
        DoubleBinaryOperator func = switch (op) {
            case "+" -> Double::sum;
            case "-" -> (a, b) -> a - b;
            case "*" -> (a, b) -> a * b;
            case "/" -> (a, b) -> b == 0 ? 1 : a / b;
            default -> Double::sum;
        };
        return new OpNode(op, func, randomTree(depth - 1), randomTree(depth - 1));
    }

    /**
     * Evaluates fitness as negative mean squared error.
     */
    public double fitness(Node tree, double[] inputs, double[] outputs) {
        double mse = 0;
        for (int i = 0; i < inputs.length; i++) {
            double predicted = tree.evaluate(inputs[i]);
            if (Double.isNaN(predicted) || Double.isInfinite(predicted)) {
                return -1e10;
            }
            mse += Math.pow(predicted - outputs[i], 2);
        }
        return -mse / inputs.length;
    }

    /**
     * Simple evolutionary run.
     */
    public Node evolve(double[] inputs, double[] outputs, int populationSize, int generations) {
        Node[] population = new Node[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = randomTree(maxDepth);
        }

        for (int gen = 0; gen < generations; gen++) {
            // Sort by fitness (descending)
            java.util.Arrays.sort(population,
                    (a, b) -> Double.compare(fitness(b, inputs, outputs), fitness(a, inputs, outputs)));

            // Elitism: keep top 10%
            int elite = populationSize / 10;
            Node[] newPop = new Node[populationSize];
            for (int i = 0; i < elite; i++) {
                newPop[i] = population[i].copy();
            }

            // Fill rest with mutations of top performers
            for (int i = elite; i < populationSize; i++) {
                if (random.nextDouble() < crossoverRate && i + 1 < populationSize) {
                    // Crossover: combine two parents (simplified: alternate between them)
                    int p1 = random.nextInt(elite);
                    int p2 = random.nextInt(elite);
                    newPop[i] = (i % 2 == 0) ? population[p1].copy() : population[p2].copy();
                } else if (random.nextDouble() < mutationRate) {
                    // Mutation: generate new random tree
                    newPop[i] = randomTree(maxDepth);
                } else {
                    // Copy from elite
                    newPop[i] = population[random.nextInt(elite)].copy();
                }
            }
            population = newPop;
        }

        return population[0];
    }
}
