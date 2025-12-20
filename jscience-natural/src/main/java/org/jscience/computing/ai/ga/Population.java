package org.jscience.computing.ai.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a generation of chromosomes.
 * 
 * @param <T> gene type
 */
public class Population<T> implements Iterable<Chromosome<T>> {

    private final List<Chromosome<T>> chromosomes;
    private final int generation;

    public Population(List<Chromosome<T>> chromosomes, int generation) {
        this.chromosomes = new ArrayList<>(chromosomes);
        this.generation = generation;
    }

    public Population(List<Chromosome<T>> chromosomes) {
        this(chromosomes, 0);
    }

    public Chromosome<T> getFittest() {
        return Collections.max(chromosomes);
    }

    public int size() {
        return chromosomes.size();
    }

    public Chromosome<T> get(int index) {
        return chromosomes.get(index);
    }

    public int getGeneration() {
        return generation;
    }

    public Stream<Chromosome<T>> stream() {
        return chromosomes.stream();
    }

    @Override
    public Iterator<Chromosome<T>> iterator() {
        return chromosomes.iterator();
    }

    /**
     * Sorts the population by fitness descending (best first).
     */
    public void sort() {
        chromosomes.sort(Collections.reverseOrder());
    }
}
