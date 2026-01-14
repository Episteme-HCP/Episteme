/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.architecture.traffic.configuration;

import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.infrastructure.*;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class PanelFactory {
    /** DOCUMENT ME! */
    public static final int TYPE_SIM = 1;

    /** DOCUMENT ME! */
    public static final int TYPE_EDIT = 2;

    /** DOCUMENT ME! */
    protected int TYPE;

    /** DOCUMENT ME! */
    protected ConfigDialog confd;

    /** DOCUMENT ME! */
    protected GeneralPanel gp = null;

    /** DOCUMENT ME! */
    protected NetTunnelPanel ntp = null;

    /** DOCUMENT ME! */
    protected EditDrivelanePanel edp = null;

    /** DOCUMENT ME! */
    protected EditEdgeNodePanel eep = null;

    /** DOCUMENT ME! */
    protected EditJunctionPanel ejp = null;

    /** DOCUMENT ME! */
    protected EditRoadPanel erp = null;

    /** DOCUMENT ME! */
    protected SimDrivelanePanel sdp = null;

    /** DOCUMENT ME! */
    protected SimEdgeNodePanel sep = null;

    /** DOCUMENT ME! */
    protected SimJunctionPanel sjp = null;

    /** DOCUMENT ME! */
    protected SimRoadPanel srp = null;

/**
     * Creates a new PanelFactory object.
     *
     * @param cd DOCUMENT ME!
     * @param t  DOCUMENT ME!
     */
    public PanelFactory(ConfigDialog cd, int t) {
        confd = cd;
        TYPE = t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    public ConfigPanel createPanel(Selection s) throws ConfigException {
        if (!s.isEmpty()) {
            Object firstObject = s.getSelectedObjects().getFirst();

            if (firstObject instanceof Node) {
                return getNodePanel((Node) firstObject);
            }

            if (firstObject instanceof Road) {
                return getRoadPanel((Road) firstObject);
            }

            if (firstObject instanceof Drivelane) {
                return getDrivelanePanel((Drivelane) firstObject);
            }

            throw new ConfigException("Unknown object type");
        }

        if (gp == null) {
            gp = new GeneralPanel(confd);
        }

        gp.reset();

        return gp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getNodePanel(Node n) throws ConfigException {
        if (n instanceof EdgeNode) {
            return getEdgeNodePanel((EdgeNode) n);
        }

        if (n instanceof Junction) {
            return getJunctionPanel((Junction) n);
        }

        if (n instanceof NetTunnel) {
            return getNetTunnelPanel((NetTunnel) n);
        }

        throw new ConfigException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getRoadPanel(Road r) throws ConfigException {
        if (TYPE == TYPE_SIM) {
            return createSRP(r);
        }

        if (TYPE == TYPE_EDIT) {
            return createERP(r);
        }

        throw new ConfigException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getDrivelanePanel(Drivelane l)
        throws ConfigException {
        if (TYPE == TYPE_SIM) {
            return createSDP(l);
        }

        if (TYPE == TYPE_EDIT) {
            return createEDP(l);
        }

        throw new ConfigException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getEdgeNodePanel(EdgeNode e) throws ConfigException {
        if (TYPE == TYPE_SIM) {
            return createSEP(e);
        }

        if (TYPE == TYPE_EDIT) {
            return createEEP(e);
        }

        throw new ConfigException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getJunctionPanel(Junction j) throws ConfigException {
        if (TYPE == TYPE_SIM) {
            return createSJP(j);
        }

        if (TYPE == TYPE_EDIT) {
            return createEJP(j);
        }

        throw new ConfigException("Unknown object type");
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigException DOCUMENT ME!
     */
    private ConfigPanel getNetTunnelPanel(NetTunnel n)
        throws ConfigException {
        return createNTP(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createSRP(Road r) {
        if (srp == null) {
            srp = new SimRoadPanel(confd, r);
        } else {
            srp.setRoad(r);
        }

        return srp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createERP(Road r) {
        if (erp == null) {
            erp = new EditRoadPanel(confd, r);
        } else {
            erp.setRoad(r);
        }

        return erp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createSDP(Drivelane l) {
        if (sdp == null) {
            sdp = new SimDrivelanePanel(confd, l);
        } else {
            sdp.setLane(l);
        }

        return sdp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createEDP(Drivelane l) {
        if (edp == null) {
            edp = new EditDrivelanePanel(confd, l);
        } else {
            edp.setLane(l);
        }

        return edp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createSEP(EdgeNode e) {
        if (sep == null) {
            sep = new SimEdgeNodePanel(confd, e);
        } else {
            sep.setEdgeNode(e);
        }

        return sep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createEEP(EdgeNode e) {
        if (eep == null) {
            eep = new EditEdgeNodePanel(confd, e);
        } else {
            eep.setEdgeNode(e);
        }

        return eep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createSJP(Junction j) {
        if (sjp == null) {
            sjp = new SimJunctionPanel(confd, j);
        } else {
            sjp.setJunction(j);
        }

        return sjp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createEJP(Junction j) {
        if (ejp == null) {
            ejp = new EditJunctionPanel(confd, j);
        } else {
            ejp.setJunction(j);
        }

        return ejp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ConfigPanel createNTP(NetTunnel n) {
        if (ntp == null) {
            ntp = new NetTunnelPanel(confd, n);
        } else {
            ntp.setNetTunnel(n);
        }

        return ntp;
    }
}
