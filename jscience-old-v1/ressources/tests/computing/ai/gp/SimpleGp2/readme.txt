SimpleGP2
---------

This example contains a genetic program to find a function which will
behave as the f(x)=(x**2)/2 function.

The Koza Tableau information for the genetic program is written below:


Objective                             - Find a function fitting the values of 
                                        the fitness case table.
Terminal set                          - x in range [0..1], 
                                        integers from -5 to +5
Function set                          - Add, Sub, Div, Mul
Population size                       - 600
Crossover probability                 - 0.8
Mutation probability                  - 0.2
Selection method                      - Fitness Proportionate
Termination criterion                 - none
Maximum number of generations         - 50
Maximum depth of tree after crossover - 20
Maximum mutant depth                  - 4
Initialization method                 - Full

Fitness cases to evaluate evolved functions:

                Input   Output
Fitness Case1:  0.000   0.000
Fitness Case1:  0.100   0.005
Fitness Case1:  0.200   0.020
Fitness Case1:  0.300   0.045
Fitness Case1:  0.400   0.080
Fitness Case1:  0.500   0.125
Fitness Case1:  0.600   0.180
Fitness Case1:  0.700   0.245
Fitness Case1:  0.800   0.320
Fitness Case1:  0.900   0.405


The example is taken from the book written below:

Banzhaf W., Nordin P., Keller R.E., and Francone F.D. Genetic Programming: An 
Introduction: On the Automatic Evolution of Computer Programs and Its 
Applications. San Francisco: Morgan Kaufmann, 1998, pp. 135-141.

