/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.numerical.providers;

import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numerical.ReduceProvider;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * Multicore implementation of ReduceProvider using Java Streams and Vector API for parallel processing.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreReduceProvider implements ReduceProvider {

    private static final VectorSpecies<Float> FLOAT_SPECIES = FloatVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Double> DOUBLE_SPECIES = DoubleVector.SPECIES_PREFERRED;

    @Override
    public float reduce(String operation, float[] input) {
        if (input.length < 1024) { // Heuristic threshold for Vector API
            return fallbackFloat(operation, input);
        }

        return switch (operation.toLowerCase()) {
            case "sum" -> {
                FloatVector acc = FloatVector.zero(FLOAT_SPECIES);
                int i = 0;
                int upperBound = FLOAT_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromArray(FLOAT_SPECIES, input, i);
                    acc = acc.add(v);
                }
                float res = acc.reduceLanes(VectorOperators.ADD);
                for (; i < input.length; i++) res += input[i];
                yield res;
            }
            case "max" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, -Float.MAX_VALUE);
                int i = 0;
                int upperBound = FLOAT_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromArray(FLOAT_SPECIES, input, i);
                    acc = acc.max(v);
                }
                float res = acc.reduceLanes(VectorOperators.MAX);
                for (; i < input.length; i++) res = Math.max(res, input[i]);
                yield res;
            }
            case "min" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, Float.MAX_VALUE);
                int i = 0;
                int upperBound = FLOAT_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromArray(FLOAT_SPECIES, input, i);
                    acc = acc.min(v);
                }
                float res = acc.reduceLanes(VectorOperators.MIN);
                for (; i < input.length; i++) res = Math.min(res, input[i]);
                yield res;
            }
            case "prod" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, 1.0f);
                int i = 0;
                int upperBound = FLOAT_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromArray(FLOAT_SPECIES, input, i);
                    acc = acc.mul(v);
                }
                float res = acc.reduceLanes(VectorOperators.MUL);
                for (; i < input.length; i++) res *= input[i];
                yield res;
            }
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    private float fallbackFloat(String operation, float[] input) {
        return switch (operation.toLowerCase()) {
            case "sum" -> {
                double sum = 0;
                for (float v : input) sum += v;
                yield (float) sum;
            }
            case "max" -> {
                float max = -Float.MAX_VALUE;
                for (float v : input) if (v > max) max = v;
                yield max;
            }
            case "min" -> {
                float min = Float.MAX_VALUE;
                for (float v : input) if (v < min) min = v;
                yield min;
            }
            case "prod" -> {
                float prod = 1.0f;
                for (float v : input) prod *= v;
                yield prod;
            }
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    @Override
    public double reduce(String operation, double[] input) {
        if (input.length < 512) {
            return switch (operation.toLowerCase()) {
                case "sum" -> DoubleStream.of(input).sum();
                case "max" -> DoubleStream.of(input).max().orElse(Double.NaN);
                case "min" -> DoubleStream.of(input).min().orElse(Double.NaN);
                case "prod" -> DoubleStream.of(input).reduce(1.0, (a, b) -> a * b);
                default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
            };
        }

        return switch (operation.toLowerCase()) {
            case "sum" -> {
                DoubleVector acc = DoubleVector.zero(DOUBLE_SPECIES);
                int i = 0;
                int upperBound = DOUBLE_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromArray(DOUBLE_SPECIES, input, i);
                    acc = acc.add(v);
                }
                double res = acc.reduceLanes(VectorOperators.ADD);
                for (; i < input.length; i++) res += input[i];
                yield res;
            }
            case "max" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, -Double.MAX_VALUE);
                int i = 0;
                int upperBound = DOUBLE_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromArray(DOUBLE_SPECIES, input, i);
                    acc = acc.max(v);
                }
                double res = acc.reduceLanes(VectorOperators.MAX);
                for (; i < input.length; i++) res = Math.max(res, input[i]);
                yield res;
            }
            case "min" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, Double.MAX_VALUE);
                int i = 0;
                int upperBound = DOUBLE_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromArray(DOUBLE_SPECIES, input, i);
                    acc = acc.min(v);
                }
                double res = acc.reduceLanes(VectorOperators.MIN);
                for (; i < input.length; i++) res = Math.min(res, input[i]);
                yield res;
            }
            case "prod" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, 1.0);
                int i = 0;
                int upperBound = DOUBLE_SPECIES.loopBound(input.length);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromArray(DOUBLE_SPECIES, input, i);
                    acc = acc.mul(v);
                }
                double res = acc.reduceLanes(VectorOperators.MUL);
                for (; i < input.length; i++) res *= input[i];
                yield res;
            }
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    @Override
    public Real reduce(String operation, Real[] input) {
        return switch (operation.toLowerCase()) {
            case "sum" -> Arrays.stream(input).parallel().reduce(Real.ZERO, Real::add);
            case "max" -> Arrays.stream(input).parallel().reduce(input[0], (a, b) -> a.compareTo(b) > 0 ? a : b);
            case "min" -> Arrays.stream(input).parallel().reduce(input[0], (a, b) -> a.compareTo(b) < 0 ? a : b);
            case "prod" -> Arrays.stream(input).parallel().reduce(Real.ONE, Real::multiply);
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    @Override
    public float reduce(String operation, MemorySegment input, ValueLayout.OfFloat layout, long count) {
        return switch (operation.toLowerCase()) {
            case "sum" -> {
                FloatVector acc = FloatVector.zero(FLOAT_SPECIES);
                long i = 0;
                long upperBound = FLOAT_SPECIES.loopBound(count);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromMemorySegment(FLOAT_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.add(v);
                }
                float res = acc.reduceLanes(VectorOperators.ADD);
                for (; i < count; i++) res += input.getAtIndex(layout, i);
                yield res;
            }
            case "max" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, -Float.MAX_VALUE);
                long i = 0;
                long upperBound = FLOAT_SPECIES.loopBound(count);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromMemorySegment(FLOAT_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.max(v);
                }
                float res = acc.reduceLanes(VectorOperators.MAX);
                for (; i < count; i++) res = Math.max(res, input.getAtIndex(layout, i));
                yield res;
            }
            case "min" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, Float.MAX_VALUE);
                long i = 0;
                long upperBound = FLOAT_SPECIES.loopBound(count);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromMemorySegment(FLOAT_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.min(v);
                }
                float res = acc.reduceLanes(VectorOperators.MIN);
                for (; i < count; i++) res = Math.min(res, input.getAtIndex(layout, i));
                yield res;
            }
            case "prod" -> {
                FloatVector acc = FloatVector.broadcast(FLOAT_SPECIES, 1.0f);
                long i = 0;
                long upperBound = FLOAT_SPECIES.loopBound(count);
                for (; i < upperBound; i += FLOAT_SPECIES.length()) {
                    FloatVector v = FloatVector.fromMemorySegment(FLOAT_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.mul(v);
                }
                float res = acc.reduceLanes(VectorOperators.MUL);
                for (; i < count; i++) res *= input.getAtIndex(layout, i);
                yield res;
            }
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    @Override
    public double reduce(String operation, MemorySegment input, ValueLayout.OfDouble layout, long count) {
        return switch (operation.toLowerCase()) {
            case "sum" -> {
                DoubleVector acc = DoubleVector.zero(DOUBLE_SPECIES);
                long i = 0;
                long upperBound = DOUBLE_SPECIES.loopBound(count);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromMemorySegment(DOUBLE_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.add(v);
                }
                double res = acc.reduceLanes(VectorOperators.ADD);
                for (; i < count; i++) res += input.getAtIndex(layout, i);
                yield res;
            }
            case "max" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, -Double.MAX_VALUE);
                long i = 0;
                long upperBound = DOUBLE_SPECIES.loopBound(count);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromMemorySegment(DOUBLE_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.max(v);
                }
                double res = acc.reduceLanes(VectorOperators.MAX);
                for (; i < count; i++) res = Math.max(res, input.getAtIndex(layout, i));
                yield res;
            }
            case "min" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, Double.MAX_VALUE);
                long i = 0;
                long upperBound = DOUBLE_SPECIES.loopBound(count);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromMemorySegment(DOUBLE_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.min(v);
                }
                double res = acc.reduceLanes(VectorOperators.MIN);
                for (; i < count; i++) res = Math.min(res, input.getAtIndex(layout, i));
                yield res;
            }
            case "prod" -> {
                DoubleVector acc = DoubleVector.broadcast(DOUBLE_SPECIES, 1.0);
                long i = 0;
                long upperBound = DOUBLE_SPECIES.loopBound(count);
                for (; i < upperBound; i += DOUBLE_SPECIES.length()) {
                    DoubleVector v = DoubleVector.fromMemorySegment(DOUBLE_SPECIES, input, i * layout.byteSize(), java.nio.ByteOrder.nativeOrder());
                    acc = acc.mul(v);
                }
                double res = acc.reduceLanes(VectorOperators.MUL);
                for (; i < count; i++) res *= input.getAtIndex(layout, i);
                yield res;
            }
            default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public double reduce(String operation, DoubleBuffer input, int size) {
        double[] array = new double[size];
        input.get(array);
        return reduce(operation, array);
    }

    @Override
    public String getName() {
        return "Multicore Reduce (CPU + SIMD)";
    }

    @Override
    public int getPriority() {
        return 40;
    }
}
