package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.InOutput;
import org.jscience.architecture.lift.World;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is  simple, static zoning {@link CA} that uses a {@link SimpleALLCA}
 * for each {@link Car}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:53 $
 */
public class SimpleCA implements CA {
    /**
     * DOCUMENT ME!
     */
    private int[][] FromTos;

    /**
     * DOCUMENT ME!
     */
    private int NoF;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] DownCalls;

    /**
     * DOCUMENT ME!
     */
    private final boolean[] UpCalls;

    /**
     * DOCUMENT ME!
     */
    private Car[] Cars = null;

    /**
     * DOCUMENT ME!
     */
    private SimpleALLCA[] ALLCAs = null;

/**
     * Constructor
     */
    public SimpleCA() {
        int NoC;

        Cars = World.getCars();
        NoC = Cars.length;
        NoF = World.getNoF();
        UpCalls = new boolean[NoF];
        DownCalls = new boolean[NoF];
        ALLCAs = new SimpleALLCA[NoC];
        FromTos = new int[NoC][2];

        for (int i = 0; i < NoC; i++) {
            double Share = (double) NoF / (double) NoC;

            FromTos[i][0] = (int) Math.floor(((double) i) * Share);
            FromTos[i][1] = (int) Math.floor((((double) (i + 1)) * Share) - 1);
            //			ALLCAs[i] = new SimplexALLCA(Cars[i], Cars[i].getCrtF(), 20);
            //			System.err.println("FTS " + i +" is (" + FromTos[i][0] + "," + FromTos[i][1] +
            //			"), parking at " + Cars[i].getCrtF());
            ALLCAs[i] = new SimpleALLCA(Cars[i],
                    ((FromTos[i][0] + FromTos[i][1]) / 2), 20, 5);

            //			System.err.println("FTS " + i + " is (" + FromTos[i][0] + "," + FromTos[i][1] +
            //					"), parking at " + ((FromTos[i][0] + FromTos[i][1]) / 2));
        }
    }

    /**
     * {@link CA}
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean goes(int From, int To, int CarIndex) {
        return (ALLCAs[CarIndex].goes(From, To, UpCalls, DownCalls));
    }

    /**
     * Forwards the request to the corresponding {@link SimpleALLCA}
     *
     * @param CC DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     *
     * @see CA
     * @see SimpleALLCA
     */
    public void issueCommand(Car CC, int AbsFloor) {
        for (int i = 0; i < Cars.length; i++) {
            if (Cars[i].equals(CC)) {
                ALLCAs[i].issueCommand(AbsFloor);

                return;
            }
        }

        throw new RuntimeException("There is no such Car! Error #442");
    }

    /**
     * {@link org.jscience.architecture.lift.Tickable}
     */
    public void tick() {
        for (int i = 0; i < NoF; i++) {
            InOutput IOP = World.getInput(i);

            UpCalls[i] = IOP.getUp();
            DownCalls[i] = IOP.getDown();
        }

        boolean[] MaskedUpCalls = new boolean[NoF];
        boolean[] MaskedDownCalls = new boolean[NoF];

        for (int i = 0; i < Cars.length; i++) {
            for (int j = 0; j < NoF; j++) {
                MaskedUpCalls[j] = (UpCalls[j]) && (j >= FromTos[i][0]) &&
                    (j <= FromTos[i][1]);
                MaskedDownCalls[j] = (DownCalls[j]) && (j >= FromTos[i][0]) &&
                    (j <= FromTos[i][1]);
            }

            ALLCAs[i].tick(MaskedUpCalls, MaskedDownCalls);
        }
    }
}
