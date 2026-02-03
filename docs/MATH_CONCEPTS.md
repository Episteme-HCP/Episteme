# Mathematical Concepts in JScience

## 1. Automatic Differentiation (Autograd)
JScience implements **Reverse-Mode Automatic Differentiation**, commonly used in Deep Learning.

### The Chain Rule
Given a function $y = f(g(x))$, the derivative with respect to $x$ is:
$$ \frac{dy}{dx} = \frac{dy}{du} \cdot \frac{du}{dx} $$
where $u = g(x)$.

In our implementation (`org.jscience.core.mathematics.ml.neural.Variable`):
1. **Forward Pass**: We compute the value of the function graph from inputs to outputs.
2. **Backward Pass**: We start with a gradient of 1.0 at the output node and traverse the graph in reverse topological order.
   - For every operation $y = op(x)$, the Node accumulates $\frac{\partial L}{\partial x} += \frac{\partial L}{\partial y} \cdot op'(x)$.

This allows computing gradients for arbitrary computational graphs dynamically (Define-by-Run).

---

## 2. Swarm Intelligence

### Particle Swarm Optimization (PSO)
PSO finds the global minimum of a function by simulating a flock of birds.
Each particle $i$ has:
- Position $x_i$
- Velocity $v_i$
- Personal best position $pbest_i$

The update rule at time $t+1$ is:
$$ v_i(t+1) = w \cdot v_i(t) + c_1 r_1 (pbest_i - x_i(t)) + c_2 r_2 (gbest - x_i(t)) $$
$$ x_i(t+1) = x_i(t) + v_i(t+1) $$

Where:
- $w$: Inertia weight
- $gbest$: Global best position found by the entire swarm
- $r_1, r_2$: Random factors $[0, 1]$

### Ant Colony Optimization (ACO)
ACO solves graph traversal problems (like TSP) by simulating pheromone trails.
The probability of ant $k$ moving from node $i$ to $j$ is:

$$ P_{ij}^k = \frac{(\tau_{ij})^\alpha (\eta_{ij})^\beta}{\sum (\tau_{il})^\alpha (\eta_{il})^\beta} $$

Where:
- $\tau_{ij}$: Pheromone level on edge $i,j$
- $\eta_{ij}$: Heuristic desirability (usually $1/distance$)
- $\alpha, \beta$: Weights controlling pheromone vs heuristic influence

---

## 3. Evolutionary Algorithms

### Genetic Algorithms (GA)
Basic process used in `org.jscience.natural.computing.ai.evolutionary`:
1. **Selection**: Tournament selection designates parents based on fitness.
2. **Crossover**: Parents swap genetic information (bits/genes) to create offspring.
   - *Uniform Crossover*: Each gene is chosen from parent A or B with 50% probability.
3. **Mutation**: Random changes to genes to maintain diversity.
