package org.jscience.mathematics.geometry.csg;

import org.jscience.mathematics.geometry.Plane3D;
import org.jscience.mathematics.geometry.Vector3D;
import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for CSG operations.
 */
public class CSGUtils {

    public static final double EPSILON = 1e-5;

    // Classification constants
    public static final int COPLANAR = 0;
    public static final int FRONT = 1;
    public static final int BACK = 2;
    public static final int SPANNING = 3;

    /**
     * Splits a polygon by a plane.
     * 
     * @param plane         the splitting plane
     * @param polygon       the polygon to split
     * @param coplanarFront list to populate with coplanar front polygons
     * @param coplanarBack  list to populate with coplanar back polygons
     * @param front         list to populate with front polygons
     * @param back          list to populate with back polygons
     */
    public static void splitPolygon(Plane3D plane, Polygon polygon, List<Polygon> coplanarFront,
            List<Polygon> coplanarBack,
            List<Polygon> front, List<Polygon> back) {

        int polygonType = 0;
        List<Integer> types = new ArrayList<>();

        for (Vertex v : polygon.vertices) {
            Real t = plane.distance(new Vector3D(v.pos.getX(), v.pos.getY(), v.pos.getZ()));
            int type = COPLANAR;
            if (t.doubleValue() > EPSILON)
                type = FRONT;
            else if (t.doubleValue() < -EPSILON)
                type = BACK;

            polygonType |= type;
            types.add(type);
        }

        switch (polygonType) {
            case COPLANAR:
                // Check normal direction to decide front or back coplanar
                // We need the polygon's plane. Polygon now should use Plane3D.
                if (plane.getNormal().dot(polygon.plane.getNormal()).doubleValue() > 0)
                    coplanarFront.add(polygon);
                else
                    coplanarBack.add(polygon);
                break;
            case FRONT:
                front.add(polygon);
                break;
            case BACK:
                back.add(polygon);
                break;
            case SPANNING:
                List<Vertex> f = new ArrayList<>();
                List<Vertex> b = new ArrayList<>();
                for (int i = 0; i < polygon.vertices.size(); i++) {
                    int j = (i + 1) % polygon.vertices.size();
                    int ti = types.get(i);
                    int tj = types.get(j);
                    Vertex vi = polygon.vertices.get(i);
                    Vertex vj = polygon.vertices.get(j);

                    if (ti != BACK)
                        f.add(vi);
                    if (ti != FRONT)
                        b.add(vi);

                    if ((ti | tj) == SPANNING) {
                        // Span, interpolate
                        // t = -dist_i / (dist_j - dist_i)
                        Real dist_i = plane.distance(new Vector3D(vi.pos.getX(), vi.pos.getY(), vi.pos.getZ()));
                        Real dist_j = plane.distance(new Vector3D(vj.pos.getX(), vj.pos.getY(), vj.pos.getZ()));

                        Real t = dist_i.negate().divide(dist_j.subtract(dist_i));

                        Vertex v = vi.interpolate(vj, t);
                        f.add(v);
                        b.add(v);
                    }
                }
                if (f.size() >= 3)
                    front.add(new Polygon(f));
                if (b.size() >= 3)
                    back.add(new Polygon(b));
                break;
        }
    }
}
