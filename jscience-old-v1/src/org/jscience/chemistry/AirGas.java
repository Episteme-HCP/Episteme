package org.jscience.chemistry;

import java.util.HashMap;


/**
 * The AirGas class defines the mixing gaz we all breath.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//the Gas is already filled with the appropriate air gaz at normal concentration
public final class AirGas extends Gas {
    //22,4 liters of air
    //actually one mole
    public AirGas() {
        HashMap contents;

        Element nitrogenElement;
        Element oxygenElement;
        Element argonElement;
        Element carbonElement;
        Element neonElement;
        Element heliumElement;
        Element kryptonElement;
        Element hydrogenElement;
        Element xenonElement;

        Atom nitrogenAtom1;
        Atom nitrogenAtom2;
        Atom nitrogenAtom3;
        Atom nitrogenAtom4;
        Atom oxygenAtom1;
        Atom oxygenAtom2;
        Atom oxygenAtom3;
        Atom argonAtom;
        Atom carbonAtom1;
        Atom oxygenAtom4;
        Atom oxygenAtom5;
        Atom neonAtom;
        Atom carbonAtom2;
        Atom hydrogenAtom1;
        Atom hydrogenAtom2;
        Atom hydrogenAtom3;
        Atom hydrogenAtom4;
        Atom heliumAtom;
        Atom kryptonAtom;
        Atom hydrogenAtom5;
        Atom hydrogenAtom6;
        Atom xenonAtom;
        Bond bond1;
        Bond bond2;
        Bond bond3;
        Bond bond4;
        Bond bond5;
        Bond bond6;
        Bond bond7;
        Bond bond8;
        Bond bond9;
        Bond bond10;
        Bond bond11;
        Bond bond12;

        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        argonElement = PeriodicTable.getElement("Argon");
        carbonElement = PeriodicTable.getElement("Carbon");
        neonElement = PeriodicTable.getElement("Neon");
        heliumElement = PeriodicTable.getElement("Helium");
        kryptonElement = PeriodicTable.getElement("Krypton");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        xenonElement = PeriodicTable.getElement("Xenon");

        nitrogenAtom1 = new Atom(nitrogenElement);
        nitrogenAtom2 = new Atom(nitrogenElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        argonAtom = new Atom(argonElement);
        carbonAtom1 = new Atom(carbonElement);
        oxygenAtom3 = new Atom(oxygenElement);
        oxygenAtom4 = new Atom(oxygenElement);
        neonAtom = new Atom(neonElement);
        carbonAtom2 = new Atom(carbonElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        heliumAtom = new Atom(heliumElement);
        kryptonAtom = new Atom(kryptonElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        hydrogenAtom6 = new Atom(hydrogenElement);
        xenonAtom = new Atom(xenonElement);
        nitrogenAtom3 = new Atom(nitrogenElement);
        nitrogenAtom4 = new Atom(nitrogenElement);
        oxygenAtom5 = new Atom(oxygenElement);

        bond1 = new Bond(nitrogenAtom1, nitrogenAtom2);
        bond2 = new Bond(oxygenAtom1, oxygenAtom2);
        bond3 = new Bond(carbonAtom1, oxygenAtom3);
        bond4 = new Bond(carbonAtom1, oxygenAtom4);
        bond5 = new Bond(carbonAtom2, hydrogenAtom1);
        bond6 = new Bond(carbonAtom2, hydrogenAtom2);
        bond7 = new Bond(carbonAtom2, hydrogenAtom3);
        bond8 = new Bond(carbonAtom2, hydrogenAtom4);
        bond9 = new Bond(hydrogenAtom5, hydrogenAtom6);
        bond10 = new Bond(nitrogenAtom3, nitrogenAtom4);
        bond11 = new Bond(nitrogenAtom3, oxygenAtom3);
        bond12 = new Bond(oxygenAtom3, nitrogenAtom4);

        contents = new HashMap();

        //data taken from http://lhs.lps.org/staff/sputnam/chem_notes/conversions.PDF
        //ok it is air at 15 degrees, not 25, sorry
        //percent by volume
        //Nitrogen  N2  78.084 %   28.013 molar mass
        //Oxygen O2 20.9476 %   31.998
        //Argon Ar 0.934 %   29.948
        //Carbon Dioxide CO2 0.0314 %   44.010
        //Neon Ne 0.001818 %   20.183
        //Helium He 0.000524 %   4.003
        //Methane CH4 0.0002 %   16.043
        //Krypton Kr 0.000114 %   83.80
        //Hydrogen H2 0.00005 %   2.016
        //N2O 0.00005 %   44.013
        //Xenon Xe 0.0000087 %   131.30
        contents.put(new Molecule(nitrogenAtom1), new Double(0.78084d * 28.013d));
        contents.put(new Molecule(oxygenAtom1), new Double(0.209476d * 31.998d));
        contents.put(new Molecule(argonAtom), new Double(0.00934d * 29.948d));
        contents.put(new Molecule(carbonAtom1), new Double(0.000314d * 44.010d));
        contents.put(new Molecule(neonAtom), new Double(0.00001818 * 20.183d));
        contents.put(new Molecule(heliumAtom), new Double(0.00000524d * 4.003d));
        contents.put(new Molecule(carbonAtom2), new Double(0.000002d * 16.043d));
        contents.put(new Molecule(kryptonAtom), new Double(0.00000114d * 83.80d));
        contents.put(new Molecule(hydrogenAtom5),
                new Double(0.0000005d * 2.016d));
        contents.put(new Molecule(nitrogenAtom3),
                new Double(0.0000005d * 44.013d));
        contents.put(new Molecule(xenonAtom), new Double(0.000000087d * 131.30d));

        super(contents, ChemicalConditions.getDefaultChemicalConditions());
    }
}
