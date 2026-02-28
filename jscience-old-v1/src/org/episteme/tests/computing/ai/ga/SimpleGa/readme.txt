SimpleGa
---------

This example contains a genetic algorithm to find x form the range [-1..2] 
which maximizes f(x)=x*sin(10*PI*x)+1.0 function.

The example is taken from the book written below:

- Michalewicz Z. Genetic Algorithms + Data Structures = Evolution Programs. 
  3rd ed. New York: Springer-Verlag, 1996, pp. 18-22.

The following section is taken from the book with little modification to explain
the solution:

    "We use  binary vector as a chromosome to represent real values of the 
variable x. The length of the vector depends on the reqired precision, which, 
in this example, is six places after the decimal point.
    
    The domain of the variable x has length 3; the precision requirement 
implies that the range [-1..2] should be divided into at least 3*1000000 equal
size ranges. This means that 22 bits are required as a binary vector 
(chromosome):

    2097152 = 2**21 < 3000000 <= 2**22 = 4194304

    The mapping from a binary string into a real number x from the range [-1..2]
is straightforward and is completedin two steps:

    1. convert the binary string from the base 2 to base 10, lets call the 
       converted integer as x'
    
    2. find a corresponding real number 
        
       x=-1.0+x'*(3/(2**22-1)) 
    
    where -1.0 is the left boundary of the domain and 3 is the length of the 
    domain.

    For example, a chromosome (1000101110110101000111) represents the number 
0.637197, since

    x'=2288967 and
    x=-1.0+2288967*(3/4194303)=0.637197

    Of course, the chromosomes (0000000000000000000000) and 
(1111111111111111111111) represent the boundaries of the domain, -1.0 and 2.0,
respectively.


