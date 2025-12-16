package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Free Group over a set of generators.
 * <p>
 * Elements are "words" consisting of generators and their inverses.
 * The group operation is concatenation followed by reduction (removing pairs of
 * x and x^-1).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FreeGroup implements Group<FreeGroup.Word> {

    private final int numGenerators;

    public FreeGroup(int numGenerators) {
        this.numGenerators = numGenerators;
    }

    @Override
    public Word operate(Word left, Word right) {
        return left.concatenate(right);
    }

    @Override
    public Word identity() {
        return Word.IDENTITY;
    }

    @Override
    public Word inverse(Word element) {
        return element.inverse();
    }

    @Override
    public boolean isCommutative() {
        return numGenerators <= 0; // Free group is non-abelian for n >= 2 (and n=1 is Z, abelian)
    }

    @Override
    public String description() {
        return "Free Group F_" + numGenerators;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Word element) {
        // Any valid word is in the group
        return element != null;
    }

    /**
     * A word in the free group.
     * Represented as a list of integers.
     * Positive k means generator k.
     * Negative -k means inverse of generator k.
     * 0 is invalid (or identity if list is empty).
     */
    public static class Word {
        private final List<Integer> symbols;

        public static final Word IDENTITY = new Word(Collections.emptyList());

        public Word(List<Integer> symbols) {
            this.symbols = reduce(symbols);
        }

        private static List<Integer> reduce(List<Integer> input) {
            List<Integer> result = new ArrayList<>(input);
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int i = 0; i < result.size() - 1; i++) {
                    int a = result.get(i);
                    int b = result.get(i + 1);
                    if (a == -b) {
                        result.remove(i + 1);
                        result.remove(i);
                        changed = true;
                        break;
                    }
                }
            }
            return Collections.unmodifiableList(result);
        }

        public Word concatenate(Word other) {
            List<Integer> newSymbols = new ArrayList<>(this.symbols);
            newSymbols.addAll(other.symbols);
            return new Word(newSymbols);
        }

        public Word inverse() {
            List<Integer> invSymbols = new ArrayList<>();
            for (int i = symbols.size() - 1; i >= 0; i--) {
                invSymbols.add(-symbols.get(i));
            }
            return new Word(invSymbols);
        }

        @Override
        public String toString() {
            if (symbols.isEmpty())
                return "1";
            StringBuilder sb = new StringBuilder();
            for (int s : symbols) {
                if (s > 0)
                    sb.append("x").append(s);
                else
                    sb.append("x").append(-s).append("^-1");
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Word))
                return false;
            Word word = (Word) o;
            return Objects.equals(symbols, word.symbols);
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbols);
        }
    }
}
