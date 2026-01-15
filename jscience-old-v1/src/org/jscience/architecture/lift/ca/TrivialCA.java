package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.InOutput;
import org.jscience.architecture.lift.World;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is trivial {@link CA} that uses a {@link SimpleALLCA} for a {@link
 * Car}. It is simplex and can not even control multi-car systems.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:53 $
 */
public class TrivialCA implements CA {
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
    private BidirCollALLCA ALLCA = null;

/**
     * Constructor
     */
    public TrivialCA() {
        NoF = World.getNoF();
        UpCalls = new boolean[NoF];
        DownCalls = new boolean[NoF];
        ALLCA = new BidirCollALLCA(World.getCar(0));
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
        return (ALLCA.goes(From, To));
    }

    /**
     * Forwards the request to the corresponding {@link SimpleALLCA}
     *
     * @param CC DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     *
     * @see CA
     * @see SimpleALLCA
     */
    public void issueCommand(Car CC, int AbsFloor) {
        if (World.getCar(0).equals(CC)) {
            ALLCA.issueCommand(AbsFloor);

            return;
        }
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

        ALLCA.tick(UpCalls, DownCalls);
    }
}
