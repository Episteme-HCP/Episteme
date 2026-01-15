package org.jscience.architecture.lift;

import java.util.Random;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a fairly simple {@code PassengerGenerator} that beheaves like a
 * handful of Poisson-distributions. It actually uses pseudorandom numbers to
 * generate a new {@link Passenger}. In every Tick for each floor {@code X}, a
 * new {@link Passenger} from floor {@code X} to a random non-{@code X} floor
 * is generated if and only if {@code Gammas[X]>=PR} where {@code PR} is the
 * latest pseudorandom number. The pseudorandom number generator can be
 * initialized to a specific value through {@code RandomSeed}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public class PoissonPassengerGenerator extends PassengerGenerator {
    /**
     * DOCUMENT ME!
     */
    private final double[] Gammas;

    /**
     * DOCUMENT ME!
     */
    private int NoF = 0;

    /**
     * DOCUMENT ME!
     */
    private Random MyRandom;

/**
     * Calls {@code PoissonPassengerGenerator(newdouble[World.getNoF()])}.
     */
    public PoissonPassengerGenerator() {
        this(new double[World.getNoF()]);
    }

/**
     * Calls {@code PoissonPassengerGenerator(newdouble[World.getNoF()], RandomSeed)}.
     */
    public PoissonPassengerGenerator(long RandomSeed) {
        this(new double[World.getNoF()], RandomSeed);
    }

/**
     * Constructs a new {@code PoissonPassengerGenerator} using the {@code GammaValues} to
     * initialize the {@code Gamma}s mentioned in the class descripiton.
     */
    public PoissonPassengerGenerator(double[] GammaValues) {
        super();
        NoF = World.getNoF();

        if (GammaValues == null) {
            throw new RuntimeException("Invalid Parameter! GammaValues==null!");
        }

        if (GammaValues.length != NoF) {
            throw new RuntimeException(
                "Invalid Parameter! GammaValues.length!=NoF!");
        }

        Gammas = new double[NoF];

        for (int i = 0; i < NoF; i++) {
            setGamma(i, GammaValues[i]);
        }

        MyRandom = new Random();
    }

/**
     * Constructs a new {@code PoissonPassengerGenerator} using the {@code GammaValues} to
     * initialize the {@code Gamma}s and {@code RandomSeed} to initialize the pseudorandom number
     * generator mentioned in the class descripiton.
     */
    public PoissonPassengerGenerator(double[] GammaValues, long RandomSeed) {
        this(GammaValues);
        MyRandom = new Random(RandomSeed);
    }

    /* Tickable */
    public void Tick() {
        for (int i = 0; i < Gammas.length; i++) {
            if (MyRandom.nextDouble() <= Gammas[i]) {
                int To;

                do {
                    To = (int) (MyRandom.nextDouble() * ((double) NoF));
                } while (To == i);

                Passengers.add(new Passenger(i, To));
            }
        }
    }

    /* Tickable */
    public String getName() {
        return ("PoissonPassengerGenerator");
    }

    /* Tickable */
    public String getVersion() {
        return ("0.0.2");
    }

    /**
     * Sets the Gamma of the {@code AbsFloor}th floor to {@code
     * NewGamma}.
     *
     * @param AbsFloor DOCUMENT ME!
     * @param NewGamma DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void setGamma(int AbsFloor, double NewGamma) {
        if ((NewGamma >= 0.0) && (NewGamma <= 1.0)) {
            Gammas[AbsFloor] = NewGamma;
        } else {
            throw new RuntimeException(
                "Invalid Parameter! NewGamma should be in [0,1]!");
        }
    }
}
