package org.jscience.astronomy;

import java.awt.*;

import javax.media.j3d.*;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;


/**
 * The Rings class provides support for planet or star asteroid rings.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a group of very small asteroids and dust that form a ring around an AstralBody
//rings are treated as a whole: for example for Saturn you would build one instance of this class also there are many rings
//the reason is that you actually can't really count rings as if they were real objects
public class Rings extends AstralBody {
    /** DOCUMENT ME! */
    private static final Color3f COLOR = new Color3f(Color.yellow);

    /** DOCUMENT ME! */
    private static final int VERTEX = 80;

    /** DOCUMENT ME! */
    private static final TexCoord2f[] texCoord = {
            new TexCoord2f(0.0f, 0.0f), new TexCoord2f(1.0f, 0.0f),
            new TexCoord2f(1.0f, 1.0f), new TexCoord2f(0.0f, 1.0f)
        };

    /**
     * DOCUMENT ME!
     */
    private double innerRadius;

    /**
     * DOCUMENT ME!
     */
    private double outerRadius;

    /**
     * DOCUMENT ME!
     */
    private Texture texture;

    /**
     * DOCUMENT ME!
     */
    private Color3f color;

    /**
     * DOCUMENT ME!
     */
    private int vertex;

    //a ring is shown as a texture stretched in a circular flat disk starting at inner radius and expanding to outer radius
    /**
     * Creates a new Rings object.
     *
     * @param name DOCUMENT ME!
     * @param innerRadius DOCUMENT ME!
     * @param outerRadius DOCUMENT ME!
     */
    public Rings(String name, double innerRadius, double outerRadius) {
        super(name);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.texture = null;
        this.color = COLOR;
        this.vertex = VERTEX;
    }

    //a ring is shown as a texture stretched in a circular flat disk starting at inner radius and expanding to outer radius
    /**
     * Creates a new Rings object.
     *
     * @param name DOCUMENT ME!
     * @param innerRadius DOCUMENT ME!
     * @param outerRadius DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param vertex DOCUMENT ME!
     */
    public Rings(String name, double innerRadius, double outerRadius,
        Color3f color, int vertex) {
        super(name);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.texture = null;
        this.color = color;
        this.vertex = vertex;
    }

    /**
     * Creates a new Rings object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     * @param innerRadius DOCUMENT ME!
     * @param outerRadius DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param vertex DOCUMENT ME!
     */
    public Rings(String name, double mass, double innerRadius,
        double outerRadius, Color3f color, int vertex) {
        super(name, mass);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.texture = null;
        this.color = color;
        this.vertex = vertex;

        Group group;
        Shape3D shape3D;
        group = new Group();
        shape3D = new Shape3D();
        shape3D.setGeometry(createGeometry());
        shape3D.setAppearance(createAppearance());
        group.addChild(shape3D);
        setGroup(group);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getInnerRadius() {
        return innerRadius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param innerRadius DOCUMENT ME!
     */
    public void setInnerRadius(double innerRadius) {
        this.innerRadius = innerRadius;
        updateShape();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getOuterRadius() {
        return outerRadius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param outerRadius DOCUMENT ME!
     */
    public void setOuterRadius(double outerRadius) {
        this.outerRadius = outerRadius;
        updateShape();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param texture DOCUMENT ME!
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
        updateShape();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color3f getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color3f color) {
        this.color = color;
        updateShape();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVertexCount() {
        return vertex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vertex DOCUMENT ME!
     */
    public void setVertexCount(int vertex) {
        this.vertex = vertex;
        updateShape();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getShape3D() {
        return (Shape3D) getGroup().getChild(0);
    }

    //to be used after you have changed the inner radius, outer radius, color, texture or the vertex
    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();

        getShape3D().setGeometry(createGeometry());
        getShape3D().setAppearance(createAppearance());
    }

    /**
     * Creates the geometry of the saturn ring system.
     *
     * @return Geometry the geometry
     */
    private Geometry createGeometry() {
        QuadArray ring = new QuadArray(vertex,
                /*QuadArray.NORMALS |*/
            QuadArray.COORDINATES | QuadArray.COLOR_3 |
                QuadArray.TEXTURE_COORDINATE_2);

        int v = 0;
        double a = 0.0;
        double fixAngle = (2.0 * StrictMath.PI) / (vertex - 2);

        while (v < vertex) {
            ring.setCoordinate(v++,
                new Point3d(innerRadius * StrictMath.cos(a), 0.0,
                    innerRadius * StrictMath.sin(a)));
            ring.setCoordinate(v++,
                new Point3d(outerRadius * StrictMath.cos(a), 0.0,
                    outerRadius * StrictMath.sin(a)));

            a = v * fixAngle;

            ring.setCoordinate(v++,
                new Point3d(outerRadius * StrictMath.cos(a), 0.0,
                    outerRadius * StrictMath.sin(a)));
            ring.setCoordinate(v++,
                new Point3d(innerRadius * StrictMath.cos(a), 0.0,
                    innerRadius * StrictMath.sin(a)));
        }

        for (int i = 0; i < vertex; i++) {
            ring.setTextureCoordinate(0, i, texCoord[i % 4]);
            ring.setColor(i, color);
        }

        return ring;
    }

    /**
     * Set the appearance of the saturn ring system whith the texture.
     *
     * @return the Appearance of the ring system with his texture.
     */
    private Appearance createAppearance() {
        // Ambient-diffuse-reflection coefficient
        Color3f diffAmb = new Color3f(1.0f, 1.0f, 1.0f);

        // Diffuse-reflection coefficient
        Color3f reflDiff = new Color3f(1.0f, 1.0f, 1.0f);

        // Specular-reflection coefficient (reflectance function)
        Color3f reflSpec = new Color3f(1.0f, 1.0f, 1.0f);

        // c = shininess: cos^c in the specular reflection
        float c = 1;

        // Emitted light
        Color3f lumEmise = new Color3f(0.2f, 0.2f, 0.2f);

        Appearance appearance = new Appearance();

        // Set up the optical properties.
        appearance.setMaterial(new Material(diffAmb, lumEmise, reflDiff,
                reflSpec, c));

        // Loading of the texture
        //AstronomyTextureLoader newTextureLoader = new AstronomyTextureLoader("images/rings.jpg");

        //Texture texture;
        try {
            //texture = newTextureLoader.getTexture();
            appearance.setTexture(texture);

            // Application mode of the texture
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE); // there still are: BLEND, DECAL,

            //                  and REPLACE
            appearance.setTextureAttributes(texAttr);
        } catch (NullPointerException e) {
        }

        return appearance;
    }
}
