package org.jscience.core.mathematics.ml.neural.autograd;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Represents a node in the computation graph for Automatic Differentiation.
 * <p>
 * Nodes wrap Tensors and track the operation history to allow for 
 * reverse-mode automatic differentiation (backpropagation).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class GraphNode<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Tensor<T> data;
    private Tensor<T> grad;
    private final transient List<GraphNode<T>> parents;
    private final transient BackwardFunc<T> backwardFunc;
    private final boolean requiresGrad;
    private final String operation;

    public GraphNode(Tensor<T> data) {
        this(data, true, null, Collections.emptyList(), "Const");
    }

    public GraphNode(Tensor<T> data, boolean requiresGrad) {
        this(data, requiresGrad, null, Collections.emptyList(), "Const");
    }

    protected GraphNode(Tensor<T> data, boolean requiresGrad, BackwardFunc<T> backwardFunc, List<GraphNode<T>> parents, String op) {
        this.data = data;
        this.requiresGrad = requiresGrad;
        this.backwardFunc = backwardFunc;
        this.parents = parents;
        this.operation = op;
    }

    /**
     * Triggers reverse-mode automatic differentiation.
     * Computes the gradient of this node with respect to all its ancestors.
     */
    public void backward() {
        if (!requiresGrad) return;

        // Initialize seed gradient if null (usually 1.0 for scalars)
        if (grad == null) {
            // For now, assume scalar output or use ones
            // Placeholder: Tensor.ones matching shape
            this.grad = createOnesGrad();
        }

        // Topological Sort
        List<GraphNode<T>> sorted = new ArrayList<>();
        Set<GraphNode<T>> visited = new HashSet<>();
        buildTopologicalSort(this, sorted, visited);
        Collections.reverse(sorted);

        // Propagate gradients
        for (GraphNode<T> v : sorted) {
            if (v.backwardFunc != null && v.grad != null) {
                v.backwardFunc.backward(v.grad);
            }
        }
    }

    private void buildTopologicalSort(GraphNode<T> v, List<GraphNode<T>> sorted, Set<GraphNode<T>> visited) {
        if (visited.contains(v)) return;
        visited.add(v);
        for (GraphNode<T> parent : v.parents) {
            buildTopologicalSort(parent, sorted, visited);
        }
        sorted.add(v);
    }

    @SuppressWarnings("unchecked")
    private Tensor<T> createOnesGrad() {
        return data.map(val -> {
            if (val instanceof Real) return (T) Real.ONE;
            return val;
        });
    }

    public Tensor<T> getData() { return data; }
    public Tensor<T> getGrad() { return grad; }
    public void setGrad(Tensor<T> grad) { 
        if (grad == null) {
            this.grad = null;
            return;
        }
        if (this.grad == null) {
            this.grad = grad;
        } else {
            this.grad = this.grad.add(grad); // Accumulate gradients
        }
    }

    public boolean requiresGrad() { return requiresGrad; }

    // --- Operations ---

    /**
     * Element-wise addition.
     */
    public GraphNode<T> add(GraphNode<T> other) {
        Tensor<T> result = this.data.add(other.data);
        boolean reqGrad = this.requiresGrad || other.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) this.setGrad(outputGrad);
            if (other.requiresGrad) other.setGrad(outputGrad);
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this, other), "Add");
    }

    /**
     * Element-wise multiplication (Hadamard product).
     */
    public GraphNode<T> multiply(GraphNode<T> other) {
        Tensor<T> result = this.data.multiply(other.data);
        boolean reqGrad = this.requiresGrad || other.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) this.setGrad(outputGrad.multiply(other.data));
            if (other.requiresGrad) other.setGrad(outputGrad.multiply(this.data));
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this, other), "Mul");
    }

    /**
     * Matrix multiplication (supports multidimensional tensors via einsum).
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> matmul(GraphNode<T> other) {
        // Simple matrix multiply logic using einsum: "ij,jk->ik"
        // For higher ranks, this could be generalized.
        Tensor<T> result = this.data.einsum("ij,jk->ik", other.data);
        boolean reqGrad = this.requiresGrad || other.requiresGrad;
        
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                // d(AB)/dA = G B^T
                this.setGrad(outputGrad.einsum("ik,jk->ij", other.data));
            }
            if (other.requiresGrad) {
                // d(AB)/dB = A^T G
                other.setGrad(this.data.einsum("ji,jk->ik", outputGrad));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this, other), "MatMul");
    }

    /**
     * Element-wise negation.
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> negate() {
        Tensor<T> result = this.data.map(val -> {
            if (val instanceof Real) return (T) ((Real) val).negate();
            return val;
        });
        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                this.setGrad(outputGrad.map(v -> {
                    if (v instanceof Real) return (T) ((Real) v).negate();
                    return v;
                }));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Neg");
    }

    /**
     * Element-wise subtraction.
     */
    public GraphNode<T> subtract(GraphNode<T> other) {
        return this.add(other.negate());
    }

    /**
     * Rectified Linear Unit (ReLU).
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> relu() {
        Tensor<T> result = this.data.map(val -> {
            if (val instanceof Real) {
                return (T) ((Real) val).max(Real.ZERO);
            }
            return val;
        });

        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                Tensor<T> mask = this.data.map(val -> {
                    if (val instanceof Real) {
                        return (T) (((Real) val).compareTo(Real.ZERO) > 0 ? Real.ONE : Real.ZERO);
                    }
                    return (T) Real.ONE;
                });
                this.setGrad(outputGrad.multiply(mask));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "ReLU");
    }

    /**
     * Sigmoid activation function.
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> sigmoid() {
        Tensor<T> result = this.data.map(val -> {
            if (val instanceof Real) {
                Real x = (Real) val;
                return (T) Real.ONE.divide(Real.ONE.add(x.negate().exp()));
            }
            return val;
        });

        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                // dSigmoid = Sigmoid * (1 - Sigmoid)
                Tensor<T> oneMinusSig = result.map(val -> {
                    if (val instanceof Real) return (T) Real.ONE.subtract((Real) val);
                    return val;
                });
                this.setGrad(outputGrad.multiply(result).multiply(oneMinusSig));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Sigmoid");
    }

    /**
     * Natural logarithm.
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> log() {
        Tensor<T> result = this.data.map(val -> {
            if (val instanceof Real) return (T) ((Real) val).log();
            return val;
        });
        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                // d(log x) = 1/x
                Tensor<T> inv = this.data.map(val -> {
                    if (val instanceof Real) return (T) ((Real) val).inverse();
                    return val;
                });
                this.setGrad(outputGrad.multiply(inv));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Log");
    }

    /**
     * Sum of all elements in the tensor.
     */
    public GraphNode<T> sum() {
        T sumVal = this.data.sum();
        // Pack in a rank-1 tensor of size 1 for consistency
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(sumVal.getClass(), 1);
        arr[0] = sumVal;
        Tensor<T> result = new org.jscience.core.mathematics.linearalgebra.tensors.DenseTensor<>(arr, 1);
        
        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                // Gradient is broadcasted ones
                T gradScale = outputGrad.get(0);
                this.setGrad(this.data.map(v -> gradScale));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Sum");
    }

    /**
     * Scale by a scalar factor.
     */
    @SuppressWarnings("unchecked")
    public GraphNode<T> scale(double factor) {
        Tensor<T> result = this.data.map(val -> {
            if (val instanceof Real) return (T) ((Real) val).multiply(Real.of(factor));
            return val;
        });
        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                this.setGrad(outputGrad.map(v -> {
                    if (v instanceof Real) return (T) ((Real) v).multiply(Real.of(factor));
                    return v;
                }));
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Scale");
    }

    /**
     * Arithmetic mean of all elements.
     */
    public GraphNode<T> mean() {
        int n = this.data.size();
        return this.sum().scale(1.0 / n);
    }

    public void setData(Tensor<T> data) {
        this.data = data;
    }

    /**
     * Broadcasts the node to a new shape.
     */
    public GraphNode<T> broadcast(int... shape) {
        Tensor<T> result = this.data.broadcast(shape);
        boolean reqGrad = this.requiresGrad;
        BackwardFunc<T> gradFunc = outputGrad -> {
            if (this.requiresGrad) {
                // Gradient for broadcast is the sum along broadcasted dimensions.
                // For simplicity, handle common case: [out] -> [batch, out]
                if (outputGrad.rank() > this.data.rank()) {
                    // Sum along axis 0 (batch)
                    this.setGrad(outputGrad.sum(0));
                } else {
                    this.setGrad(outputGrad);
                }
            }
        };
        return new GraphNode<>(result, reqGrad, gradFunc, List.of(this), "Broadcast");
    }

    @Override
    public String toString() {
        return "GraphNode{op='" + operation + "', shape=" + Arrays.toString(data.shape()) + "}";
    }

    /**
     * Functional interface for the backward pass of an operation.
     */
    @FunctionalInterface
    public interface BackwardFunc<T> {
        void backward(Tensor<T> outputGrad);
    }
}
