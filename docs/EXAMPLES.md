# Episteme Examples

## 1. Deep Learning (Autograd)
Training a simple Multi-Layer Perceptron (MLP) to learn XOR.

```java
import org.episteme.core.mathematics.ml.neural.*;

// 1. Define inputs and targets
Variable input = new Variable(new double[][]{{0,0}, {0,1}, {1,0}, {1,1}});
Variable target = new Variable(new double[][]{{0}, {1}, {1}, {0}});

// 2. Define Model (Linear -> ReLU -> Linear -> Sigmoid)
Sequential model = new Sequential(
    new Linear(2, 4),
    new ReLU(),
    new Linear(4, 1),
    new Sigmoid()
);

// 3. Optimizer
Optimizer optimizer = new SGD(model.parameters(), 0.1);

// 4. Training Loop
for (int i = 0; i < 1000; i++) {
    optimizer.zeroGrad();
    
    // Forward
    Variable output = model.forward(input);
    
    // Loss (MSE)
    Variable loss = output.sub(target).pow(2).mean();
    
    // Backward
    loss.backward();
    
    // Update
    optimizer.step();
    
    if (i % 100 == 0) System.out.println("Loss: " + loss.getData().get(0));
}
```

## 2. Swarm Optimization (PSO)
Finding the minimum of the sphere function $f(x) = \sum x_i^2$.

```java
import org.episteme.core.mathematics.optimization.swarm.*;

// Define objective function
ObjectiveFunction sphere = args -> {
    double sum = 0;
    for (double x : args) sum += x * x;
    return sum;
};

// Configure Optimizer: 30 particles, 5 dimensions, 100 iterations
ParticleSwarmOptimization pso = new ParticleSwarmOptimization(30, 5, 100);

// Run
double[] bestPosition = pso.optimize(sphere);
System.out.println("Best Solution: " + Arrays.toString(bestPosition));
```

## 3. Running Demos
Episteme includes a JavaFX demo suite showcasing these features visually.

```bash
mvn exec:java -pl episteme-core -Dexec.mainClass="org.episteme.core.ui.EpistemeDemosApp"
```
