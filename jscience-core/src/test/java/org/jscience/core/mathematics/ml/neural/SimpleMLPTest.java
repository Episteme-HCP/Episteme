package org.jscience.core.mathematics.ml.neural;

import org.jscience.core.mathematics.ml.neural.autograd.GraphNode;
import org.jscience.core.mathematics.ml.neural.layers.Linear;
import org.jscience.core.mathematics.ml.neural.layers.ReLU;
import org.jscience.core.mathematics.ml.neural.layers.Sequential;
import org.jscience.core.mathematics.ml.neural.optimizers.SGD;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorFactory;
import org.jscience.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleMLPTest {

    @Test
    public void testForwardBackwardPass() {
        // Model: 2 -> 4 -> 1
        Sequential<Real> model = new Sequential<>();
        model.add(new Linear<>(2, 4));
        model.add(new ReLU<>());
        model.add(new Linear<>(4, 1));
        
        SGD<Real> optimizer = new SGD<>(model.getParameters(), 0.1);
        
        // Input [1, 2]
        Real[] xData = {Real.of(0.5), Real.of(-0.5)};
        GraphNode<Real> X = new GraphNode<>(TensorFactory.of(xData, 1, 2));
        
        // Target [1, 1]
        Real[] yData = {Real.of(1.0)};
        GraphNode<Real> Y = new GraphNode<>(TensorFactory.of(yData, 1, 1));
        
        Real initialLoss = null;
        for (int i = 0; i < 20; i++) {
            optimizer.zeroGrad();
            
            GraphNode<Real> pred = model.forward(X);
            // MSE Loss: (pred - Y)^2
            GraphNode<Real> diff = pred.subtract(Y);
            GraphNode<Real> loss = diff.multiply(diff).mean();
            
            Real lossVal = loss.getData().get(0);
            if (i == 0) initialLoss = lossVal;
            
            System.out.println("Iteration " + i + ", Loss: " + lossVal);
            
            loss.backward();
            optimizer.step();
            
            if (i == 19) {
                assertTrue(lossVal.compareTo(initialLoss) < 0, "Loss should decrease");
            }
        }
    }
}
