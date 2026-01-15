package org.jscience.chemistry;

import java.util.HashMap;

/**
 * The ChemicalSubstrate class is the superclass for substrates in which chemical reactions occur.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

public class ChemicalSubstrate extends Object {

    private HashMap contents;//what the substrate is made of, an ideal mix of chemicals
    //the molecule/mass for each of the components

    private ChemicalConditions chemicalConditions;

    public ChemicalSubstrate(Molecule molecule, double mass, ChemicalConditions chemicalConditions) {

        HashMap hashMap;

        hashMap = new HashMap();
        hashMap.put(Molecule
        molecule, new Double(mass));
        this(hashMap, chemicalConditions);

    }

    public ChemicalSubstrate(HashMap contents, ChemicalConditions chemicalConditions) {

        Iterator iterator;
        boolean valid;
        Object currentElement;

        if ((chemicalConditions != null) && (contents != null)) {
            if (contents.size() > 0) {
                iterator = contents.keySet().iterator();
                valid = true;
                while (iterator.hasNext() && valid) {
                    currentElement = iterator.next();
                    valid = (currentElement instanceof Molecule) && (contents.get(currentElement) != null) && (contents.get(currentElement) instanceof Double);
                }
                if (valid) {
                    this.contents = contents;
                    this.chemicalConditions = chemicalConditions;
                } else
                    throw new IllegalArgumentException("Chemical substrate contents must contain only Molecule/Double pairs.");
            } else
                throw new IllegalArgumentException("Chemical substrate contents can't be an empty HashMap.");
        } else
            throw new IllegalArgumentException("ChemicalSubstrate doesn't accept null arguments.");

    }

    public HashMap getContents() {

        return contents;

    }

    public ChemicalConditions getChemicalConditions() {

        return chemicalConditions;

    }

    //computes the sum mass from its components
    public double computeMass() {

        Iterator iterator;
        double sum;

        iterator = contents.keySet().iterator();
        sum = 0;
        while (iterator.hasNext()) {
            sum += ((Double) contents.get(iterator.next())).doubleValue();
        }

        return sum;

    }

    //gets from the mass the number of moles from the compound
    //returns 0 if the compound is not present
    //check is made against bopnding isomers, not stereoisomers (which are not yet supported)
    public double computeMoles(Molecule compound) {

        Iterator iterator;
        Molecule currentElement;
        boolean found;
        double result;

        iterator = contents.keySet().iterator();
        found = false;
        while (iterator.hasNext() && !found) {
            currentElement = (Molecule) iterator.next();
            found = currentElement.isBondingIsomer(compound);
        }
        if (found) {
            result = ((Double) contents.get(currentElement)).getDoubleValue() / compound.computeMolecularWeight();
        } else {
            result = 0;
        }

        return result;

    }

    //computes the total number of moles
    public double computeMoles() {

        Iterator iterator;
        Molecule currentElement;
        double result;

        iterator = contents.keySet().iterator();
        result = 0;
        while (iterator.hasNext() && !found) {
            currentElement = (Molecule) iterator.next();
            result += ((Double) contents.get(currentElement)).getDoubleValue() / currentElement.computeMolecularWeight();
        }

        return result;

    }

    //mixes this chemical substrate with chemicalSubstrate
    //chemicalSubstrate is set to null
    //and this is set to the mix of all compounds
    //volume is summed, temperature averaged (and modulated with mass), pressure also
    public void mix(ChemicalSustrate chemicalSubstrate) {

        ChemicalSustrate result;

        this.getContents().addAll()

        XXXXX
                chemicalSubstrate = null;

        return result;

    }

    //builds out a new chemical substrate out of the first two
    //chemical substrates are set to null
    //mix(A,B) always produces the same than mix(B,A)
    //results are produced in the same way than B.mix(A)
    public static ChemicalSustrate mix(ChemicalSustrate chemicalSubstrate1, ChemicalSustrate chemicalSubstrate2) {

        ChemicalSustrate result;

        result = chemicalSubstrate1.clone().mix(chemicalSubstrate2);
        chemicalSubstrate1 = null;
        chemicalSubstrate2 = null;

        return result;

    }

    //produces a chemical substrate that is the exact replica of this one
    //contents themselves are not cloned
    public ChemicalSustrate clone() {

        return new ChemicalSubstrate(getContents().clone(), new ChemicalConditions(getChemicalConditions().getTemperature(), getChemicalConditions().getVolume(), getChemicalConditions().getPressure(), getChemicalConditions().getPotential()));

    }

}
