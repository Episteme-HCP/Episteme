/*
 * Created on Jan 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GaussPoint {

    private double[] wt;
    private double[] pt;
    private int numIP;

    public GaussPoint(int d) throws AtlasException {
        this.numIP = d;
        initialize();
    }

    private void initialize() throws AtlasException {
        if (numIP < 2) {
            throw new AtlasException(" Must have at least 2 Gauss Points.");
        } else if (numIP > 8) {
            throw new AtlasException(" Cannot have more than 8 Gauss Points.");
        } else {
            pt = new double[numIP];
            wt = new double[numIP];
        }

        if (numIP == 2) {
            // integration points
            pt[0] = -0.5773502691;
            pt[1] = 0.5773502691;
            // weights
            wt[0] = 1.0;
            wt[1] = 1.0;
        } else if (numIP == 3) {
            // integration points
            pt[0] = -0.7745966692;
            pt[1] = 0.0;
            pt[2] = 0.7745966692;
            // weights
            wt[0] = 0.5555555555;
            wt[1] = 0.8888888888;
            wt[2] = 0.5555555555;
        } else if (numIP == 4) {
            // integration points
            pt[0] = -0.8611363116;
            pt[1] = -0.3399810436;
            pt[2] = 0.3399810436;
            pt[3] = 0.8611363116;
            // weights
            wt[0] = 0.3478548451;
            wt[1] = 0.6521451549;
            wt[2] = 0.6521451549;
            wt[3] = 0.3478548451;
        } else if (numIP == 5) {
            // integration points
            pt[0] = -0.9061798459;
            pt[1] = -0.5384693101;
            pt[2] = 0.0;
            pt[3] = 0.5384693101;
            pt[4] = 0.9061798459;
            // weights
            wt[0] = 0.2369268851;
            wt[1] = 0.4786286705;
            wt[2] = 0.5688888888;
            wt[3] = 0.4786286705;
            wt[4] = 0.2369268851;
        } else if (numIP == 6) {
            // integration points
            pt[0] = -0.9324695142;
            pt[1] = -0.6612093865;
            pt[2] = -0.2386191860;
            pt[3] = 0.2386191860;
            pt[4] = 0.6612093865;
            pt[5] = 0.9324695142;
            // weights
            wt[0] = 0.1713244924;
            wt[1] = 0.3607615730;
            wt[2] = 0.4679139346;
            wt[3] = 0.4679139346;
            wt[4] = 0.3607615730;
            wt[5] = 0.1713244924;
        } else if (numIP == 7) {
            // integration points
            pt[0] = -0.9491079123;
            pt[1] = -0.7415311856;
            pt[2] = -0.4058451514;
            pt[3] = 0.0;
            pt[4] = 0.4058451514;
            pt[5] = 0.7415311856;
            pt[6] = 0.9491079123;
            // weights
            wt[0] = 0.1294849662;
            wt[1] = 0.2797053915;
            wt[2] = 0.3818300505;
            wt[3] = 0.4179591837;
            wt[4] = 0.3818300505;
            wt[5] = 0.2797053915;
            wt[6] = 0.1294849662;
        } else if (numIP == 8) {
            // integration points
            pt[0] = -0.9602898565;
            pt[1] = -0.7966664774;
            pt[2] = -0.5255324099;
            pt[3] = -0.1834346425;
            pt[4] = 0.1834346425;
            pt[5] = 0.5255324099;
            pt[6] = 0.7966664774;
            pt[7] = 0.9602898565;
            // weights
            wt[0] = 0.1012285363;
            wt[1] = 0.2223810345;
            wt[2] = 0.3137066459;
            wt[3] = 0.3626837834;
            wt[4] = 0.3626837834;
            wt[5] = 0.3137066459;
            wt[6] = 0.2223810345;
            wt[7] = 0.1012285363;
        }
    }

    public double getWeight(int i) {
        return wt[i];
    }

    public double getPoint(int i) {
        return pt[i];
    }

}
