package org.jscience.astronomy;

import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * The StarBackground class provides support for the display of a background of fake or real stars.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//The night sky background of visible stars as seen from within a starSystem, especially the surface of planets within
//with may be extra information on constellations and actual stars
public class StarBackground extends Background {

    private Set knownObjects;//stars, rings, constellations, natural and artificial satellites and why not nebulas and galaxies

    public StarBackground() {

        knownObjects = Collections.EMPTY_SET;

    }

    //create an infinite sphere and puts it into the background and uses the texture
    public StarBackground(Texture texture) {

        this.knownObjects = Collections.EMPTY_SET;

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);

        setApplicationBounds(bounds);

        BranchGroup backGeoBranch = new BranchGroup();
        Sphere sphereObj = new Sphere(1.0f, Sphere.GENERATE_NORMALS |
                Sphere.GENERATE_NORMALS_INWARD |
                Sphere.GENERATE_TEXTURE_COORDS, 45);
        Appearance backgroundApp = sphereObj.getAppearance();
        if (texture != null)
            backgroundApp.setTexture(texture);
        backGeoBranch.addChild(sphereObj);
        setGeometry(backGeoBranch);

    }

    public Set getKnownAstralBodies() {

        return knownObjects;

    }

    public void setKnownAstralBodies(Set knownObjects) {

        Iterator iterator;
        Boolean good;

        iterator = knownObjects.iterator();
        good = true;

        while (iterator.hasNext() && good) {
            good = iterator.next() instanceof AstralBody;
        }

        if (!good) throw new IllegalArgumentException("The Set of known objects must contain only AstralBodies.");

        this.knownObjects = knownObjects;

    }

    public void addKnownAstralBodies(AstralBody knownObject) {

        knownObjects.add(knownObject);

    }

    public void removeKnownAstralBodies(AstralBody knownObject) {

        knownObjects.remove(knownObject);

    }

    /**
     * public static BranchGroup createTexturedBackground(String imageName) {
     * <p/>
     * BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
     * <p/>
     * // Create the root of the branch graph
     * BranchGroup objRoot = new BranchGroup();
     * <p/>
     * // Create a Transformgroup to scale all objects so they
     * // appear in the scene.
     * TransformGroup objScale = new TransformGroup();
     * Transform3D t3d = new Transform3D();
     * t3d.setScale(0.4);
     * objScale.setTransform(t3d);
     * objRoot.addChild(objScale);
     * <p/>
     * // Create the transform group node and initialize it to the
     * // identity.  Enable the TRANSFORM_WRITE capability so that
     * // our behavior code can modify it at runtime.
     * TransformGroup objTrans = new TransformGroup();
     * objScale.addChild(objTrans);
     * <p/>
     * AstronomyTextureLoader tex = new AstronomyTextureLoader(imageName, new String("RGB"));
     * StarBackground bg = new StarBackground(tex.getTexture());
     * objTrans.addChild(bg);
     * <p/>
     * // Shine it with two lights.
     * Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
     * Color3f lColor2 = new Color3f(0.2f, 0.2f, 0.1f);
     * Vector3f lDir1 = new Vector3f(-1.0f, -1.0f, -1.0f);
     * Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
     * DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
     * DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
     * lgt1.setInfluencingBounds(bounds);
     * lgt2.setInfluencingBounds(bounds);
     * objScale.addChild(lgt1);
     * objScale.addChild(lgt2);
     * <p/>
     * return objRoot;
     * <p/>
     * }
     */

    /* "My god...it's full of stars!"
     * Create a background of Stars
     */
    public static StarBackground createRandomStarsBackground(int numStars) {

        StarBackground background = new StarBackground();
        final BoundingSphere infiniteBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
        background.setApplicationBounds(infiniteBounds);
        BranchGroup bg = new BranchGroup();

        final java.util.Random rand = new java.util.Random();
        PointArray starfield = new PointArray(20000, PointArray.COORDINATES | PointArray.COLOR_3);
        float[] point = new float[3];
        float[] brightness = new float[3];
        for (int i = 0; i < numStars; i++) {
            point[0] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
            point[1] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
            point[2] = (rand.nextInt(2) == 0) ? rand.nextFloat() * -1.0f : rand.nextFloat();
            starfield.setCoordinate(i, point);
            final float mag = rand.nextFloat();
            brightness[0] = mag;
            brightness[1] = mag;
            brightness[2] = mag;
            starfield.setColor(i, brightness);
        }
        bg.addChild(new Shape3D(starfield));

        background.setGeometry(bg);

        return background;

    }

}
