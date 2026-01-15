package org.jscience.astronomy;


//perhaps we should provide a system which allows to add elements after creation time
public class Catalog {

    /**
     * The name of the catalog
     */
    private String name;

    /**
     * The names.
     * <p/>
     * These is the corresponding name to each position entry of the catalog
     */
    private String[] names;

    /**
     * The positions.
     * <p/>
     * <p>These are itsNpos triplets of numbers, each triplet being x,y,z in Gm.
     * The coordinate system is mean equinox of J2000.0.
     */
    private double[] itsR;

    /**
     * Initialise the object.
     * <p/>
     * <p>This creates the array itsR[], but does not initialise the array
     * values.
     *
     * @param aNpos Obtain space for so many xyz triplets of coordinates.
     */

    public void Catalog(String name, int aNpos) {

        this.name = name;
        names = new String[aNpos];
        itsR = new double[3 * aNpos];

    }

    /**
     * The name of the catalogue.
     */
    public String getName() {

        return name;

    }

    /**
     * The number of positions in the catalogue.
     */
    public int getNumEntries() {

        return names.length;

    }

    public String getEntryName(int pos) {

        return names[pos];

    }

    public double[] getEntry(int pos) {

        double[] triplet;

        triplet = new double[3];
        triplet[0] = itsR[3 * pos];
        triplet[0] = itsR[3 * pos + 1];
        triplet[0] = itsR[3 * pos + 2];

        return triplet;

    }

    public void setEntry(int pos, String name, double x, double y, double z) {

        names[pos] = name;
        itsR[3 * pos] = x;
        itsR[3 * pos + 1] = y;
        itsR[3 * pos + 2] = z;

    }

}
