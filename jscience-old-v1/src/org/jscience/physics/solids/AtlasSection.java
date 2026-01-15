/*
 * Created on Mar 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids;

import org.jscience.physics.solids.geom.AtlasPosition;
import org.jscience.physics.solids.result.StressResult;

import java.util.ArrayList;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AtlasSection extends AtlasObject {

    protected double area = 0.0;
    protected double Iy = 0.0;
    protected double Iz = 0.0;
    protected double Iyz = 0.0;
    protected double J = 0.0;
    protected double As = 0.0;
    protected double Qy = 0.0;
    protected double Qz = 0.0;
    protected double yBar = 0.0;
    protected double zBar = 0.0;
    protected double RhoX = 0.0;
    protected double RhoY = 0.0;
    protected double alpha = 0.0;
    protected double IpMax = 0.0;
    protected double IpMin = 0.0;
    protected double RhoMax = 0.0;
    protected double RhoMin = 0.0;
    protected double K1 = 0.0;
    protected double K2 = 0.0;
    protected double K3 = 0.0;

    protected AtlasPosition ShearCenter = null;
    protected ArrayList<AtlasPosition> srp = new ArrayList<AtlasPosition>();
    protected ArrayList<AtlasPosition> outLine = new ArrayList<AtlasPosition>();

    public AtlasSection() {
    }

    public AtlasSection(ArrayList<AtlasPosition> OL) {
        this.outLine = OL;
        calculate();
    }

    public abstract StressResult[] computeSectionStress();

    /**
     * @return Returns the area.
     */
    public double getArea() {
        return area;
    }

    /**
     * @param area The area to set.
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * @return Returns the as.
     */
    public double getAs() {
        return As;
    }

    /**
     * @param as The as to set.
     */
    public void setAs(double as) {
        As = as;
    }

    /**
     * @return Returns the Iy.
     */
    public double getIy() {
        return Iy;
    }

    /**
     * @param Iy The Iy to set.
     */
    public void setIy(double Iy) {
        this.Iy = Iy;
    }

    /**
     * @return Returns the Iz.
     */
    public double getIz() {
        return Iz;
    }

    /**
     * @param Iz The Iz to set.
     */
    public void setIz(double Iz) {
        this.Iz = Iz;
    }

    /**
     * @return Returns the j.
     */
    public double getJ() {
        return J;
    }

    /**
     * @param j The j to set.
     */
    public void setJ(double j) {
        J = j;
    }

    /**
     * @return Returns the outLine.
     */
    public ArrayList<AtlasPosition> getOutLine() {
        return outLine;
    }

    /**
     * @param outLine The outLine to set.
     */
    public void setOutLine(ArrayList<AtlasPosition> outLine) {
        this.outLine = outLine;
    }

    /**
     * @param sp The stress recovery points to set.
     */
    public void setSRP(ArrayList<AtlasPosition> sp) {
        this.srp = sp;
    }

    /**
     * @return Returns the array of stress recovery points
     */
    public ArrayList<AtlasPosition> getSRP() {
        return srp;
    }

    /**
     * @return Returns the shearCenter.
     */
    public AtlasPosition getShearCenter() {
        return ShearCenter;
    }

    /**
     * @param shearCenter The shearCenter to set.
     */
    public void setShearCenter(AtlasPosition shearCenter) {
        ShearCenter = shearCenter;
    }

    private void calculate() {

        double tarea = 0.0;
        double sumay = 0.0;
        double sumaz = 0.0;
        double irefy = 0.0;
        double irefz = 0.0;
        double irefyz = 0.0;
        double dy, dz, ar, at, dry, dty, drz, dtz, ry, ty, rz, tz, iory, ioty, iorz, iotz, ioyz;
        double ipry, ipty, iprz, iptz, ipryz, iptyz;
        AtlasPosition ap1, ap2;

        for (int i = 0; i < outLine.size(); i++) {
            ap1 = outLine.get(i);
            if (i == outLine.size()) {
                ap2 = outLine.get(0);
            } else {
                ap2 = outLine.get(i + 1);
            }
            dy = ap2.getY() - ap1.getY();
            dz = ap2.getZ() - ap1.getZ();
            ar = -dy * ap1.getZ();
            at = 0.5 * (-dy) * dz;
            dry = 0.5 * (ap1.getY() + ap2.getY());
            dty = (ap1.getY() + (2.0 * ap2.getY())) / 3.0;
            drz = 0.5 * ap1.getZ();
            dtz = (2.0 * ap1.getZ() + ap2.getZ()) / 3.0;
            ry = ar * dry;
            ty = at * dty;
            rz = ar * drz;
            tz = at * dtz;
            iory = ar * ap1.getZ() * ap1.getZ() / 12.0;
            ioty = at * dz * dz / 18.0;
            iorz = ar * dy * dy / 12.0;
            iotz = at * dy * dy / 18.0;
            ioyz = -Math.abs(at * dy * dz / 36.0);
            ipry = ar * drz * drz;
            ipty = at * dtz * dtz;
            iprz = ar * dry * dry;
            iptz = at * dty * dty;
            ipryz = ar * dry * drz;
            iptyz = at * dty * dtz;
            tarea = tarea + ar + at;
            sumay = sumay + ry + ty;
            sumaz = sumaz + rz + tz;
            irefy = irefy + iory + ioty + ipry + ipty;
            irefz = irefz + iorz + iotz + iprz + iptz;
            irefyz = irefyz + ioyz + ipryz + iptyz;
        }
        area = Math.abs(tarea);
        Qy = sumay;
        Qz = sumaz;
        if (tarea != 0.0) {
            yBar = sumay / tarea;
            zBar = sumaz / tarea;
        }
        Iy = Math.abs(irefy - tarea * zBar * zBar);
        Iz = Math.abs(irefz - tarea * yBar * yBar);
        Iyz = irefyz - tarea * yBar * zBar;
        if (tarea < 0.0) {
            Qy = -Qy;
            Qz = -Qz;
            Iyz = -Iyz;
        }
        RhoY = Math.sqrt(Iy / area);

    }
}
