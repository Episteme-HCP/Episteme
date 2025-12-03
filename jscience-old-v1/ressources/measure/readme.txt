Useful sources for units dictionaries and conversion:
http://www.sedris.org/sdk_4.0/src/lib/edcs/docs/dictionary/units.htm
http://www.unc.edu/~rowlett/units/
http://www.gordonengland.co.uk/conversion/

Although in a first place it was considered that every class in every other package would use and
be based on the Units and Quantities, it appears this is 1. a big move for people, 2. a big effort
on the developper side (more code and more complex) and 3. a performance hit on raw calculus.
Usage should therefore be the following way: use this package for user interface. Grab data input
from the user as Quantities objects and convert to a unit in the SI (Sytème International d'Unités),
get the double value and use them in the internal JScience system which is (apart from a couple cases)
entirely based on SI, therefore not requireing any further conversion. When computation is done,
put the values back into Quantities objects and display to user.

Perhaps this package should be moved under org.jscience.engineering.measure