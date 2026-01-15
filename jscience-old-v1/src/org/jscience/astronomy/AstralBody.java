package org.jscience.astronomy;


//import com.dautelle.physics.*;
//import com.dautelle.physics.models.*;
import com.sun.j3d.utils.image.TextureLoader;

import org.jscience.physics.PhysicsConstants;
import org.jscience.physics.kinematics.RigidBody3D;

import org.jscience.util.CircularReferenceException;
import org.jscience.util.Named;

import java.awt.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.media.j3d.*;

import javax.vecmath.Color3f;


/**
 * The AstralBody class provides the basic support for all elements with a
 * mass in space.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a natural space body orbitting in the universe, either wandering or in a star sytstem
//all children should be small in size (mass) compared to this AstralBody as it is used to compute object motions
public abstract class AstralBody extends RigidBody3D implements Named {
    /**
     * DOCUMENT ME!
     */
    private final static int NUMBEROFPOINTS = 50;

    /**
     * DOCUMENT ME!
     */
    private String name; //common name

    /**
     * DOCUMENT ME!
     */
    private Group shape; //shape (mostly never change)

    /**
     * DOCUMENT ME!
     */
    private AstralBody parent;

    /**
     * DOCUMENT ME!
     */
    private Set children;

    /**
     * DOCUMENT ME!
     */
    private Ellipse ellipse;

    /**
     * Creates a new AstralBody object.
     *
     * @param name DOCUMENT ME!
     */
    public AstralBody(String name) {
        this(name, null, 0);
    }

    /**
     * Creates a new AstralBody object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public AstralBody(String name, double mass) {
        this(name, null, mass);
    }

    //use another constructor if you want to set a null shape
    /**
     * Creates a new AstralBody object.
     *
     * @param name DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public AstralBody(String name, Group shape, double mass) {
        super(mass);

        if ((name != null) && (name.length() > 0) && (shape != null)) {
            this.name = name;
            this.setMass(mass);
            this.setCharge(0); //actually may be we should override the corresponding methods to private
            this.shape = shape;
            this.parent = null;
            this.children = Collections.EMPTY_SET;
            this.ellipse = null;
        } else {
            throw new IllegalArgumentException(
                "The AstralBody constructor doesn't accept null name (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return shape;
    }

    /**
     * DOCUMENT ME!
     *
     * @param shape DOCUMENT ME!
     */
    public void setGroup(Group shape) {
        this.shape = shape;
    }

    //if an AstralBody hasn't a star as parent, it is a wanderingBody
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasStar() {
        //look for a star as (distant) parent
        AstralBody element;

        element = this;

        while (element.getParent() != null) {
            element = element.getParent();
        }

        return (element instanceof Star);
    }

    //note that an AstralBody can have a parent Star but this Star may not be assigned yet to a StarSystem
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Star getStar() {
        AstralBody element;

        element = this;

        while (element.getParent() != null) {
            element = element.getParent();
        }

        if (element instanceof Star) {
            return (Star) element;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getStars() {
        return getStarSystem().getStars();
    }

    //returns null if not in a star system
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StarSystem getStarSystem() {
        AstralBody element;

        element = this;

        while (element.getParent() != null) {
            element = element.getParent();
        }

        if (element instanceof Star) {
            return element.getStarSystem();
        } else {
            return null;
        }
    }

    //doesn't work for wandering bodies
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Galaxy getGalaxy() {
        StarSystem starSystem;

        starSystem = getStarSystem();

        if (starSystem != null) {
            return starSystem.getGalaxy();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Universe getUniverse() {
        Galaxy galaxy;

        galaxy = getGalaxy();

        if (galaxy != null) {
            return galaxy.getUniverse();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild() {
        return !children.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild(AstralBody child) {
        return children.contains(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        //perhaps we should return a copy of the Set to be sure that the contents are not modified and AstralBody Tree structure is destroyed
        return children;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllChildren() {
        Iterator iterator;
        HashSet result;

        iterator = children.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((AstralBody) iterator.next()).getAllChildren());
        }

        return result;
    }

    //all elements should be of class AstralBody
    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setChildren(Set children) throws CircularReferenceException {
        Iterator iterator;
        Object currentElement;
        AstralBody child;

        if (children != null) {
            iterator = children.iterator();

            while (iterator.hasNext()) {
                currentElement = iterator.next();

                if (currentElement instanceof AstralBody) {
                    child = (AstralBody) currentElement;

                    if ((child.getParent() == null) && (child != this)) {
                        if ((child.getClass() != Star.class) &&
                                (child.getClass() != Nebula.class)) {
                            child.setParent(this);
                        } else {
                            throw new IllegalArgumentException(
                                "Children of astral bodies should contain only astral bodies (but not stars nor nebulas).");
                        }
                    } else {
                        throw new CircularReferenceException(
                            "Cannot add AstralBody child that has already a parent.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The children Set must contain only AstralBodies.");
                }
            }

            this.children = children;
        } else {
            throw new IllegalArgumentException(
                "The children Set must be not null.");
        }
    }

    //we cannot add self or a (distant) parent
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean addChild(AstralBody child) throws CircularReferenceException {
        //synchronized (this) {
        if (child != null) {
            if ((child != this) && (child.getParent() == null)) {
                if ((child.getClass() != Star.class) &&
                        (child.getClass() != Nebula.class)) {
                    child.setParent(this);

                    return children.add(child);
                } else {
                    throw new IllegalArgumentException(
                        "Children of astral bodies should contain only astral bodies (but not stars nor nebulas).");
                }
            } else {
                throw new CircularReferenceException(
                    "Cannot add AstralBody child that has already a parent.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }

        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean removeChild(AstralBody child) {
        //synchronized (this) {
        if (child != null) {
            if (children.contains(child)) {
                child.setParent(null);

                return children.remove(child);
            } else {
                throw new IllegalArgumentException(
                    "The child is not a child of this.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }

        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasParent() {
        return parent != null;
    }

    //to set the parent, use element.getParent().removeChild(element);newParent.addChild(element);
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AstralBody getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    private void setParent(AstralBody child) {
        parent = child;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasEllipse() {
        return getEllipse() != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Ellipse getEllipse() {
        return ellipse;
    }

    //you shouldn't use this method for wanderingObjects as after all, they are wandering
    /**
     * DOCUMENT ME!
     *
     * @param ellipse DOCUMENT ME!
     */
    public void setEllipse(Ellipse ellipse) {
        this.ellipse = ellipse;
    }

    //tries to build an ellipse for this object using its motion parameters
    /**
     * DOCUMENT ME!
     *
     * @param astralBody DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeDistance(AstralBody astralBody) {
        if (astralBody != null) {
            return Math.abs(getPosition().subtract(astralBody.getPosition())
                                .norm());
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param astralBody1 DOCUMENT ME!
     * @param astralBody2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double computeDistance(AstralBody astralBody1,
        AstralBody astralBody2) {
        if ((astralBody1 != null) && (astralBody2 != null)) {
            return Math.abs(astralBody1.getPosition()
                                       .subtract(astralBody2.getPosition())
                                       .norm());
        } else {
            return 0;
        }
    }

    //orbit period (year duration, in seconds)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeOrbitalPeriod() {
        if (hasEllipse() && (getStar() != null)) {
            return Math.sqrt((Math.pow(getEllipse().getMajorAxisLength() / 2, 3) * 4 * Math.PI * Math.PI) / (PhysicsConstants.G * (getStar()
                                                                                                                                       .getMass() +
                getMass())));
        } else {
            return 0;
        }
    }

    //The eccentricity of an ellipse is equal to the distance between its focuses divided by the major axis.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeReceivedLight() {
        StarSystem starSystem;
        Iterator iterator;
        Star star;
        double result;

        if (this.getClass() != Star.class) {
            starSystem = getStarSystem();

            if (starSystem != null) {
                iterator = starSystem.getStars().iterator();
                result = 0;

                while (iterator.hasNext()) {
                    star = (Star) iterator.next();
                    result += (star.computeAllEmittedLight() / (4 * Math.PI * Math.pow(computeDistance(
                            star), 2)));
                }

                return result;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Image loadImage(String fileName) {
        Image image;

        try {
            image = Toolkit.getDefaultToolkit()
                           .createImage((java.awt.image.ImageProducer) ((getClass()
                                                                             .getResource(fileName)).getContent()));

            return image;
        } catch (Exception ex) {
            System.out.println("Texture " + fileName + " not found or loaded.");

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     * @param emit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Appearance loadTexture(String fileName, boolean emit) {
        Appearance app;
        TextureLoader loader;
        Texture2D texture;
        TextureAttributes texAttr;
        Color3f white;
        Color3f black;

        app = new Appearance();

        loader = new TextureLoader(loadImage(fileName), null);
        texture = (Texture2D) loader.getTexture();
        texture.setMinFilter(texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(texture.BASE_LEVEL_LINEAR);

        texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);

        app.setTexture(texture);

        white = new Color3f(1.0f, 1.0f, 1.0f);
        black = new Color3f(0f, 0f, 0f);

        if (emit) {
            app.setMaterial(new Material(black, white, black, black, 4.0f));
        } else {
            app.setMaterial(new Material(white, black, white, white, 4.0f));
        }

        return app;
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        ColorLineOrbit colorOrbit;
        TransformGroup transformGroup;
        Transform3D transform3D;

        if (hasEllipse()) {
            colorOrbit = new ColorLineOrbit(Color.RED);
            colorOrbit.setPositions(getEllipse().getOrbit(NUMBEROFPOINTS));
            transformGroup = new TransformGroup();
            transform3D = new Transform3D();
            transformGroup.setTransform(transform3D);
            transformGroup.addChild(colorOrbit);
        }
    }
}
