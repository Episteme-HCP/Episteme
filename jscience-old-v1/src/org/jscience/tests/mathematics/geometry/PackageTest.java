/*
 * package private のメソッドをテストするためのクラス
 *
 * Copyright 2000 by Information-technology Promotion Agency, Japan
 * Copyright 2000 by Precision Modeling Laboratory, Inc., Tokyo, Japan
 * Copyright 2000 by Software Research Associates, Inc., Tokyo, Japan
 *
 * $Id: PackageTest.java,v 1.2 2007-10-21 21:09:04 virtualcall Exp $
 */

package org.jscience.mathematics.geometry;

/**
 * package private のメソッドをテストするためのクラス
 *
 * @author Information-technology Promotion Agency, Japan
 * @version $Revision: 1.2 $, $Date: 2007-10-21 21:09:04 $
 */

public class PackageTest {

    public PackageTest() {
        super();
    }

    /**
     * $B%F%9%H$N7k2L$, true なら "OK" を false なら "NG" を表示する
     *
     * @param result $B%F%9%H7k2L
     */
    private void resultPrint(boolean result) {
        if (result) {
            System.out.println("OK.");
        } else {
            System.out.println("NG.");
        }
    }

    /**
     * **********************************************************************
     * <p/>
     * PointOnPoint1D.coordinates()
     * <p/>
     * ***********************************************************************
     */

    /*
    * PointOnPoint1D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point1D coordinatesPointOnPoint1D(PointOnPoint1D point) {
        return point.coordinates();
    }

    /**
     * **********************************************************************
     * <p/>
     * Point2D
     * <p/>
     * ***********************************************************************
     */

    /*
    * Test 2.2.59 - 60 のテスト
    * (Point2D の package private method のテスト)
    *
    * @param point テスト用の点インスタンス
    */
    public void test2_2_59to60(Point2D point) {
        Point2D orig = point;

        /*
        * 2.2.59 to3D() のテスト(3D化)
        *
        * [テスト項目] メソッドを実行し、xy平面に射影した2次元の点を
        *              返すことを確認する。
        */
        System.out.print("// Test 2.2.59 : ");
        Point3D p1 = orig.to3D();
        Point3D p2 = orig.to3D(0);
        Point3D p3 = orig.to3D(100);
        resultPrint(p1 instanceof Point3D && p2 instanceof Point3D &&
                p3 instanceof Point3D &&
                orig.x() == p1.x() && orig.x() == p2.x() &&
                orig.x() == p3.x() && orig.y() == p1.y() &&
                orig.y() == p2.y() && orig.y() == p3.y() &&
                p1.z() == 0 && p2.z() == 0 && p3.z() == 100);

        /*
        * 2.2.60 literal() のテスト(CartesianPoint2D へのオブジェクト変換)
        *
        * [テスト項目] CartesianPoint2D のオブジェクトに変換された点を
        *              返すことを確認する。
        */
        System.out.print("// Test 2.2.60 : ");
        resultPrint(orig.literal() instanceof CartesianPoint2D);
    }

    /**
     * **********************************************************************
     * <p/>
     * PointOnPoint2D.coordinates()
     * PointOnCurve2D.coordinates()
     * <p/>
     * ***********************************************************************
     */

    /*
    * PointOnPoint2D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point2D coordinatesPointOnPoint2D(PointOnPoint2D point) {
        return point.coordinates();
    }

    /*
    * PointOnCurve2D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point2D coordinatesPointOnCurve2D(PointOnCurve2D point) {
        return point.coordinates();
    }

    /*************************************************************************
     *
     * IntersectionPoint2D
     *
     *************************************************************************/

    /**
     * IntersectionPoint2D のオブジェクト生成のテスト用関数
     * (Test 2.2.04(正常ケース), 2.2.64 に使用)
     * <p/>
     * $B0J2<$N%F%9%HMQ$NE@$NG[Ns$rJV$9!#
     * <p/>
     * point[0-1] : Test 2.3.04 用インスタンス
     * point[2-4] : Test 2.3.64 用インスタンス
     *
     * @return $B8rE@$NG[Ns
     */
    public Point2D[] intersectionPoint2DCreateTestNormal() {
        CartesianPoint2D coord;
        Line2D[] line = new Line2D[2];
        PointOnCurve2D[] pocs = new PointOnCurve2D[2];
        IntersectionPoint2D[] points = new IntersectionPoint2D[5];

        coord = Point2D.of(1, 2);
        line[0] = new Line2D(Point2D.of(0, 2), Vector2D.of(1, 0));
        line[1] = new Line2D(Point2D.of(1, 0), Vector2D.of(0, 1));
        pocs[0] = new PointOnCurve2D(line[0], 1);
        pocs[1] = new PointOnCurve2D(line[1], 2);

        // Test 2.2.4 形状要素 x 形状要素、coordinatesあり (1)
        points[0] = new IntersectionPoint2D(coord, pocs[0], pocs[1], true);

        // Test 2.2.64 形状要素 x 形状要素、coordinatesなし
        points[2] = new IntersectionPoint2D(pocs[0], pocs[1], true);

        // Test 2.2.64 線 x 線、coordinatesあり
        points[3] = new IntersectionPoint2D(coord, line[0], 1, line[1], 2, true);
        // Test 2.2.64 線 x 線、coordinatesなし
        points[4] = new IntersectionPoint2D(line[0], 1, line[1], 2, true);

        // Test 2.2.4 形状要素 x 形状要素、coordinatesあり (2)
        // (他のテスト用インスタンス)
        coord = Point2D.of(100, 100);
        pocs[0] = new PointOnCurve2D(new Circle2D(Point2D.of(0, 100), 100), 0);
        pocs[1] = new PointOnCurve2D(new Circle2D(Point2D.of(90, 100), 10), 0);
        points[1] = new IntersectionPoint2D(coord, pocs[0], pocs[1], true);

        return points;
    }

    /**
     * IntersectionPoint2D のオブジェクト生成のテスト用関数
     * (Test 2.2.04 エラーケースに使用)
     *
     * @return $BA4$F$N%F%9%H%1!<%9$GNc30$,JV$5$l$?>l9g$O true を、
     *         $B$$$:$l$+%F%9%H%1!<%9$G%*%V%8%'%/%H$,@8@.$5$l$?>l9g$O
     *         false を返す。
     */
    public boolean intersectionPoint2DCreateTestError() {
        CartesianPoint2D coord;
        Line2D[] line = new Line2D[2];
        PointOnCurve2D[] pocs = new PointOnCurve2D[2];
        IntersectionPoint2D[] points = new IntersectionPoint2D[5];

        coord = Point2D.of(1, 2);
        line[0] = new Line2D(Point2D.of(0, 2), Vector2D.of(1, 0));
        line[1] = new Line2D(Point2D.of(1, 0), Vector2D.of(0, 1));
        pocs[0] = new PointOnCurve2D(line[0], 1);
        pocs[1] = new PointOnCurve2D(line[1], 2);

        IntersectionPoint2D point;
        boolean flag = true;

        // coordinates が交点で無い
        coord = new CartesianPoint2D(1 + 1, 2 + 1);
        try {
            point = new IntersectionPoint2D(coord, pocs[0], pocs[1], true);
            flag &= false;
        } catch (InvalidArgumentValue e) {
            flag &= true;
        }

        // coordinates が交点と距離の許容誤差の2倍離れている
        double tol = coord.getToleranceForDistance();
        coord = new CartesianPoint2D(1 + (tol * 2), 2);
        try {
            point = new IntersectionPoint2D(coord, pocs[0], pocs[1], true);
            flag &= false;
        } catch (InvalidArgumentValue e) {
            flag &= true;
        }

        // coordinates が null
        try {
            point = new IntersectionPoint2D(null, pocs[0], pocs[1], true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        // pointOnGeometry1 が null
        coord = new CartesianPoint2D(1, 2);
        try {
            point = new IntersectionPoint2D(coord, null, pocs[1], true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        // pointOnGeometry2 が null
        try {
            point = new IntersectionPoint2D(coord, pocs[0], null, true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        return flag;
    }

    /**
     * **********************************************************************
     * <p/>
     * Point3D
     * <p/>
     * ***********************************************************************
     */

    /*
    * 2.3.57 rotateZ() のテスト(回転させた点)
    *
    * @param point テスト用の点のインスタンス
    */
    private void test2_3_57(Point3D point) {
        Axis2Placement3D axis =
                new Axis2Placement3D(Point3D.origin,
                        Vector3D.zUnitVector,
                        Vector3D.xUnitVector);
        CartesianTransformationOperator3D trans =
                new CartesianTransformationOperator3D(axis, 1.0);

        System.out.print("// Test 2.2.57 : ");
        resultPrint(point.rotateZ(trans, 0.0, 1.0).
                identical(Point3D.of(-2, 1, 3)));
        System.out.println();
    }

    /*
    * 2.2.58 to2D() のテスト(2D化)
    *
    * @param point テスト用の点のインスタンス
    */
    private void test2_3_58(Point3D point) {
        System.out.print("// Test 2.2.58 : ");
        Point2D p1 = point.to2D();
        Point2D p2 = point.to2D();
        resultPrint(p1 instanceof Point2D && p2 instanceof Point2D &&
                point.x() == p1.x() && point.x() == p2.x() &&
                point.y() == p1.y() && point.y() == p2.y());
    }

    /*
    * 2.2.59 literal() のテスト(CartesianPoint2D へのオブジェクト変換)
    *
    * @param point テスト用の点のインスタンス
    */
    private void test2_3_59(Point3D point) {
        System.out.print("// Test 2.2.59 : ");
        resultPrint(point.literal() instanceof CartesianPoint3D);
    }

    /*
    * Test 2.3.57 - 59
    *
    * @param point テスト用の点のインスタンス
    */
    public void test2_3_57to59(Point3D point) {
        test2_3_57(point);
        test2_3_58(point);
        test2_3_59(point);
    }

    /**
     * **********************************************************************
     * <p/>
     * PointOnPoint3D.coordinates()
     * PointOnCurve3D.coordinates()
     * PointOnSurface3D.coordinates()
     * <p/>
     * ***********************************************************************
     */

    /*
    * PointOnPoint3D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point3D coordinatesPointOnPoint3D(PointOnPoint3D point) {
        return point.coordinates();
    }

    /*
    * PointOnCurve3D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point3D coordinatesPointOnCurve3D(PointOnCurve3D point) {
        return point.coordinates();
    }

    /*
    * PointOnSurface3D.coordinates()
    *
    * @param point テスト用の点のインスタンス
    */
    public Point3D coordinatesPointOnSurface3D(PointOnSurface3D point) {
        return point.coordinates();
    }

    /*************************************************************************
     *
     * IntersectionPoint3D
     *
     *************************************************************************/

    /**
     * IntersectionPoint3D のオブジェクト生成のテスト用関数
     * (Test 2.3.04(正常ケース), 2.3.65 に使用)
     * <p/>
     * point[0-1]  : Test 2.3.04 用インスタンス
     * point[2-10] : Test 2.3.65 用インスタンス
     *
     * @return $B8rE@$NG[Ns
     */
    public static Point3D[] intersectionPoint3DCreateTestNormal() {
        CartesianPoint3D coord;
        Line3D[] line = new Line3D[2];
        PointOnCurve3D[] pocs = new PointOnCurve3D[2];
        IntersectionPoint3D[] points = new IntersectionPoint3D[11];
        IntersectionPoint3D point;

        coord = Point3D.of(1, 2, 3);
        line[0] = new Line3D(Point3D.of(0, 2, 3),
                Vector3D.of(1, 0, 0));
        line[1] = new Line3D(Point3D.of(1, 0, 3),
                Vector3D.of(0, 1, 0));
        pocs[0] = new PointOnCurve3D(line[0], 1);
        pocs[1] = new PointOnCurve3D(line[1], 2);

        // Test 2.3.04 形状要素 vs 形状要素、coordinatesあり (1)
        points[0] = new IntersectionPoint3D(coord, pocs[0], pocs[1], true);
        System.out.println("//\t [case 1: geometry vs geometry, coordinates]");
        System.out.println("//\t points[0] = ");
        points[0].output(System.out);

        // Test 2.3.65 形状要素 vs 形状要素、coordinatesなし
        points[2] = new IntersectionPoint3D(pocs[0], pocs[1], true);
        System.out.println("//\t [case 2: geometry vs geometry, no coordinates]");
        System.out.println("//\t points[2] = ");
        points[2].output(System.out);

        // Test 2.3.65 線 vs 線、coordinatesあり
        points[3] = new IntersectionPoint3D(coord, line[0], 1, line[1], 2, true);
        System.out.println("//\t [case 3: line vs line, coordinates]");
        System.out.println("//\t points[3] = ");
        points[3].output(System.out);

        // Test 2.3.65 線 vs 線、coordinatesなし
        points[4] = new IntersectionPoint3D(line[0], 1, line[1], 2, true);
        System.out.println("//\t [case 4: line vs line, no coordinates]");
        System.out.println("//\t points[4] = ");
        points[4].output(System.out);

        // Test 2.3.65 線 vs 面、coordinatesあり */
        Plane3D plane = new Plane3D(Point3D.origin, Vector3D.xUnitVector);
        coord = Point3D.of(0, 2, 3);
        points[5] = new IntersectionPoint3D(coord, line[0], 0, plane, 3, -2, true);
        System.out.println("//\t [case 5: line vs surface, coordinates]");
        System.out.println("//\t points[5] = ");
        points[5].output(System.out);

        // Test 2.3.65 線 vs 面、coordinatesなし */
        points[6] = new IntersectionPoint3D(line[0], 0, plane, 3, -2, true);
        System.out.println("//\t [case 6: line vs surface, no coordinates]");
        System.out.println("//\t points[6] = ");
        points[6].output(System.out);

        // Test 2.3.65 面 vs 線、coordinatesあり */
        points[7] = new IntersectionPoint3D(coord, plane, 3, -2, line[0], 0, true);
        System.out.println("//\t [case 7: surface vs line, coordinates]");
        System.out.println("//\t points[7] = ");
        points[7].output(System.out);

        // Test 2.3.65 面 vs 線、coordinatesなし */
        points[8] = new IntersectionPoint3D(plane, 3, -2, line[0], 0, true);
        System.out.println("//\t [case 8: surface vs line, no coordinates]");
        System.out.println("//\t points[8] = ");
        points[8].output(System.out);

        // Test 2.3.65 面 vs 面、coordinatesあり */
        Plane3D plane2 = new Plane3D(Point3D.origin,
                Vector3D.yUnitVector);
        points[9] = new IntersectionPoint3D(Point3D.origin,
                plane, 0, 0,
                plane2, 0, 0, true);
        System.out.println("//\t [case 9: surface vs surface, coordinates]");
        System.out.println("//\t points[9] = ");
        points[9].output(System.out);

        // Test 2.3.65 面 vs 面、coordinatesなし */
        points[10] = new IntersectionPoint3D(plane, 0, 0,
                plane2, 0, 0, true);
        System.out.println("//\t [case 10: surface vs surface, no coordinates]");
        System.out.println("//\t points[10] = ");
        points[10].output(System.out);

        // Test 2.2.04 形状要素 vs 形状要素、coordinatesあり (2)
        // (他のテスト用インスタンス)
        Circle3D circle1 =
                new Circle3D(new Axis2Placement3D(Point3D.of(0, 100, 100),
                        Vector3D.zUnitVector,
                        Vector3D.xUnitVector),
                        100);

        Circle3D circle2 =
                new Circle3D(new Axis2Placement3D(Point3D.of(90, 100, 100),
                        Vector3D.zUnitVector,
                        Vector3D.xUnitVector),
                        10);

        pocs[0] = new PointOnCurve3D(circle1, 0);
        pocs[1] = new PointOnCurve3D(circle2, 0);
        points[1] = new IntersectionPoint3D(Point3D.of(100, 100, 100),
                pocs[0], pocs[1], true);

        return points;
    }

    /**
     * IntersectionPoint3D のオブジェクト生成のテスト用関数
     * (Test 2.3.04 エラーケース に使用)
     *
     * @return $BA4$F$N%F%9%H%1!<%9$GNc30$,JV$5$l$?>l9g$O true を、
     *         $B$$$:$l$+%F%9%H%1!<%9$G%*%V%8%'%/%H$,@8@.$5$l$?>l9g$O
     *         false を返す。
     */
    public static boolean intersectionPoint3DCreateTestError() {
        CartesianPoint3D coord;
        Line3D[] line = new Line3D[2];
        PointOnCurve3D[] pocs = new PointOnCurve3D[2];
        IntersectionPoint3D point;

        coord = Point3D.of(1, 2, 3);
        line[0] = new Line3D(Point3D.of(0, 2, 3),
                Vector3D.of(1, 0, 0));
        line[1] = new Line3D(Point3D.of(1, 0, 3),
                Vector3D.of(0, 1, 0));
        pocs[0] = new PointOnCurve3D(line[0], 1);
        pocs[1] = new PointOnCurve3D(line[1], 2);
        boolean flag = true;

        // coordinates が交点で無い
        coord = new CartesianPoint3D(1 + 1, 2 + 1, 3);
        try {
            point = new IntersectionPoint3D(coord, pocs[0], pocs[1], true);
            flag &= false;
        } catch (InvalidArgumentValue e) {
            flag &= true;
        }

        // coordinates が交点と距離の許容誤差の2倍離れている
        double tol = coord.getToleranceForDistance();
        coord = new CartesianPoint3D(1 + (tol * 2), 2, 3);
        try {
            point = new IntersectionPoint3D(coord, pocs[0], pocs[1], true);
            flag &= false;
        } catch (InvalidArgumentValue e) {
            flag &= true;
        }

        // coordinates が null
        try {
            point = new IntersectionPoint3D(null, pocs[0], pocs[1], true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        // pointOnGeometry1 が null
        coord = new CartesianPoint3D(1, 2, 3);
        try {
            point = new IntersectionPoint3D(coord, null, pocs[1], true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        // pointOnGeometry2 が null
        try {
            point = new IntersectionPoint3D(coord, pocs[0], null, true);
            flag &= false;
        } catch (java.lang.NullPointerException e) {
            flag &= true;
        }

        return flag;
    }

    /*************************************************************************
     *
     * ParametricSurface3D
     *
     *************************************************************************/

    /**
     * Test 5.1.53
     */
    public int type(ParametricSurface3D surface) {
        return surface.type();
    }

    /*************************************************************************
     *
     * CylindricalSurface3D
     *
     *************************************************************************/

    /**
     * Test 5.1.57
     */
    public void cylinderTest() {
        CylindricalSurface3D cylA =
                new CylindricalSurface3D(Point3D.origin,
                        Vector3D.zUnitVector, 1.0);

        CylindricalSurface3D cylB =
                new CylindricalSurface3D(Point3D.origin,
                        Vector3D.xUnitVector, 2.0);
        // Test 5.1.57.1 equals() のテスト
        System.out.print("// Test 5.1.57.1 : ");
        resultPrint(cylA.equals(cylA) && !cylA.equals(cylB));

        // Test 5.1.57.2 getAxis() のテスト
        System.out.print("// Test 5.1.57.2 : ");
        resultPrint(Point3D.origin.isOn(cylA.getAxis()) &&
                cylA.getAxis().dir().
                        identicalDirection(Vector3D.zUnitVector));
    }

    /*************************************************************************
     *
     * ConicalSurface3D
     *
     *************************************************************************/

    /**
     * Test 5.1.58
     */
    public void coneTest() {
        ConicalSurface3D cone =
                new ConicalSurface3D(Point3D.of(0.0, 0.0, 1.0),
                        Vector3D.zUnitVector,
                        1.0, 0.7853981633974483);

        // Test 5.1.58.1 apex() のテスト
        System.out.print("// Test 5.1.58.1 : ");
        resultPrint(cone.apex().identical(Point3D.origin));

        // Test 5.1.58.2 getAxis() のテスト
        System.out.print("// Test 5.1.58.2 : ");
        resultPrint(Point3D.of(0.0, 0.0, 1.0).isOn(cone.getAxis()) &&
                cone.getAxis().dir().
                        identicalDirection(Vector3D.zUnitVector));
    }
}
