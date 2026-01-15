/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import org.jscience.media.pictures.filters.math.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

import java.io.Serializable;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
interface ElevationMap {
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getHeightAt(int x, int y);
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class LightFilter extends WholeImageFilter implements Serializable {
    /** DOCUMENT ME! */
    public final static int COLORS_FROM_IMAGE = 0;

    /** DOCUMENT ME! */
    public final static int COLORS_CONSTANT = 1;

    /** DOCUMENT ME! */
    public final static int COLORS_FROM_ENVIRONMENT = 2;

    /** DOCUMENT ME! */
    public final static int BUMPS_FROM_IMAGE = 0;

    /** DOCUMENT ME! */
    public final static int BUMPS_FROM_MAP = 1;

    /** DOCUMENT ME! */
    public final static int BUMPS_FROM_BEVEL = 2;

    /** DOCUMENT ME! */
    public final static int AMBIENT = 0;

    /** DOCUMENT ME! */
    public final static int DISTANT = 1;

    /** DOCUMENT ME! */
    public final static int POINT = 2;

    /** DOCUMENT ME! */
    public final static int SPOT = 3;

    /** DOCUMENT ME! */
    private float bumpHeight;

    /** DOCUMENT ME! */
    private float viewDistance = 10000.0f;

    /** DOCUMENT ME! */
    Material material;

    /** DOCUMENT ME! */
    private Vector lights;

    /** DOCUMENT ME! */
    int diffuseColor;

    /** DOCUMENT ME! */
    int specularColor;

    /** DOCUMENT ME! */
    private int colorSource = COLORS_FROM_IMAGE;

    /** DOCUMENT ME! */
    private int bumpSource = BUMPS_FROM_IMAGE;

    /** DOCUMENT ME! */
    private Function2D bumpFunction;

    /** DOCUMENT ME! */
    private Image environmentMap;

    /** DOCUMENT ME! */
    private int[] envPixels;

    /** DOCUMENT ME! */
    private int envWidth = 1;

    /** DOCUMENT ME! */
    private int envHeight = 1;

    /** DOCUMENT ME! */
    private Vector3D l;

    /** DOCUMENT ME! */
    private Vector3D v;

    /** DOCUMENT ME! */
    private Vector3D n;

    /** DOCUMENT ME! */
    private ARGB shadedColor;

    /** DOCUMENT ME! */
    private ARGB diffuse_color;

    /** DOCUMENT ME! */
    private ARGB specular_color;

    /** DOCUMENT ME! */
    private Vector3D tmpv;

    /** DOCUMENT ME! */
    private Vector3D tmpv2;

    /** DOCUMENT ME! */
    public NormalEvaluator normalEvaluator = new NormalEvaluator(); //FIXME

    /** DOCUMENT ME! */
    private int[] rgb = new int[4];

/**
     * Creates a new LightFilter object.
     */
    public LightFilter() {
        lights = new Vector(); //fixme

        //		addLight(new AmbientLight());//fixme
        addLight(new DistantLight());
        bumpHeight = 1.0f;
        material = new Material();
        diffuseColor = -1;
        specularColor = -1;
        l = new Vector3D();
        v = new Vector3D();
        n = new Vector3D();
        shadedColor = new ARGB();
        diffuse_color = new ARGB();
        specular_color = new ARGB();
        tmpv = new Vector3D();
        tmpv2 = new Vector3D();
    }

    /**
     * DOCUMENT ME!
     *
     * @param bumpFunction DOCUMENT ME!
     */
    public void setBumpFunction(Function2D bumpFunction) {
        this.bumpFunction = bumpFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Function2D getBumpFunction() {
        return bumpFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bumpHeight DOCUMENT ME!
     */
    public void setBumpHeight(float bumpHeight) {
        this.bumpHeight = bumpHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBumpHeight() {
        return bumpHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewDistance DOCUMENT ME!
     */
    public void setViewDistance(float viewDistance) {
        this.viewDistance = viewDistance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getViewDistance() {
        return viewDistance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param diffuseColor DOCUMENT ME!
     */
    public void setDiffuseColor(int diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDiffuseColor() {
        return diffuseColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param environmentMap DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void setEnvironmentMap(Image environmentMap) {
        this.environmentMap = environmentMap;

        if (environmentMap != null) {
            PixelGrabber pg = new PixelGrabber(environmentMap, 0, 0, -1, -1,
                    null, 0, -1);

            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                throw new RuntimeException("interrupted waiting for pixels!");
            }

            if ((pg.status() & ImageObserver.ABORT) != 0) {
                throw new RuntimeException("image fetch aborted");
            }

            envPixels = (int[]) pg.getPixels();
            envWidth = pg.getWidth();
            envHeight = pg.getHeight();
        } else {
            envWidth = envHeight = 1;
            envPixels = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image getEnvironmentMap() {
        return environmentMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colorSource DOCUMENT ME!
     */
    public void setColorSource(int colorSource) {
        this.colorSource = colorSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColorSource() {
        return colorSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bumpSource DOCUMENT ME!
     */
    public void setBumpSource(int bumpSource) {
        this.bumpSource = bumpSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBumpSource() {
        return bumpSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param light DOCUMENT ME!
     */
    public void addLight(Light light) {
        lights.addElement(light);
    }

    /**
     * DOCUMENT ME!
     *
     * @param light DOCUMENT ME!
     */
    public void removeLight(Light light) {
        lights.removeElement(light);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getLights() {
        return lights;
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        if ((status == 1) || (status == 4)) {
            consumer.imageComplete(status);

            return;
        }

        int width = transformedSpace.width;
        int height = transformedSpace.height;
        int index = 0;
        int[] outPixels = new int[width * height];
        float width45 = Math.abs(6.0f * bumpHeight);
        boolean invertBumps = bumpHeight < 0;
        float Nz = 1530.0f / width45;
        Vector3D position = new Vector3D(0.0f, 0.0f, 0.0f);
        Vector3D viewpoint = new Vector3D((float) width / 2.0f,
                (float) height / 2.0f, viewDistance);
        Vector3D normal = new Vector3D(0.0f, 0.0f, Nz);
        ARGB diffuseColor = new ARGB(this.diffuseColor);
        ARGB specularColor = new ARGB(this.specularColor);
        Function2D bump = bumpFunction;

        if ((bumpSource == BUMPS_FROM_IMAGE) || (bump == null)) { //FIXME-creates image function for bevels
            bump = new ImageFunction2D(inPixels, width, height,
                    ImageFunction2D.CLAMP);
        }

        Vector3D v1 = new Vector3D();
        Vector3D v2 = new Vector3D();
        Vector3D n = new Vector3D();
        Light[] lightsArray = new Light[lights.size()];
        lights.copyInto(lightsArray);

        for (int i = 0; i < lightsArray.length; i++)
            lightsArray[i].prepare(width, height);

        // Loop through each source pixel
        for (int y = 0; y < height; y++) {
            float ny = y;
            position.y = y;

            for (int x = 0; x < width; x++) {
                float nx = x;

                // Calculate the normal at this point
                if (bumpSource != BUMPS_FROM_BEVEL) {
                    // Complicated and slower method
                    // Calculate four normals using the gradients in +/- X/Y directions
                    int count = 0;
                    normal.x = normal.y = normal.z = 0;

                    float m0 = width45 * bump.evaluate(nx, ny);
                    float m1 = (x > 0)
                        ? ((width45 * bump.evaluate(nx - 1.0f, ny)) - m0) : (-2);
                    float m2 = (y > 0)
                        ? ((width45 * bump.evaluate(nx, ny - 1.0f)) - m0) : (-2);
                    float m3 = (x < (width - 1))
                        ? ((width45 * bump.evaluate(nx + 1.0f, ny)) - m0) : (-2);
                    float m4 = (y < (height - 1))
                        ? ((width45 * bump.evaluate(nx, ny + 1.0f)) - m0) : (-2);

                    if ((m1 != -2) && (m4 != -2)) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        v1.crossProduct(v2, n);
                        n.normalize();

                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }

                        normal.add(n);
                        count++;
                    }

                    if ((m1 != -2) && (m2 != -2)) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = -1.0f;
                        v2.z = m2;
                        v1.crossProduct(v2, n);
                        n.normalize();

                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }

                        normal.add(n);
                        count++;
                    }

                    if ((m2 != -2) && (m3 != -2)) {
                        v1.x = 0.0f;
                        v1.y = -1.0f;
                        v1.z = m2;
                        v2.x = 1.0f;
                        v2.y = 0.0f;
                        v2.z = m3;
                        v1.crossProduct(v2, n);
                        n.normalize();

                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }

                        normal.add(n);
                        count++;
                    }

                    if ((m3 != -2) && (m4 != -2)) {
                        v1.x = 1.0f;
                        v1.y = 0.0f;
                        v1.z = m3;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        v1.crossProduct(v2, n);
                        n.normalize();

                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }

                        normal.add(n);
                        count++;
                    }

                    // Average the four normals
                    normal.x /= count;
                    normal.y /= count;
                    normal.z /= count;
                } else {
                    if (normalEvaluator != null) {
                        normalEvaluator.getNormalAt(x, y, width, height, normal);
                    }
                }

                if (invertBumps) {
                    normal.x = -normal.x;
                    normal.y = -normal.y;
                }

                position.x = x;

                if (normal.z >= 0) {
                    // Get the material colour at this point
                    if (colorSource == COLORS_FROM_IMAGE) {
                        diffuseColor.setColor(inPixels[index]);
                    } else if ((colorSource == COLORS_FROM_ENVIRONMENT) &&
                            (environmentMap != null)) {
                        //FIXME-too much normalizing going on here
                        tmpv2.set(viewpoint);
                        tmpv2.z = 100.0f; //FIXME
                        tmpv2.subtract(position);
                        tmpv2.normalize();
                        tmpv.set(normal);
                        tmpv.normalize();
                        tmpv.reflect(tmpv2);
                        tmpv.normalize();
                        diffuseColor.setColor(getEnvironmentMap(tmpv, inPixels,
                                width, height));
                    }

                    // Shade the pixel
                    ARGB c = phongShade(position, viewpoint, normal,
                            diffuseColor, specularColor, material, lightsArray);
                    int alpha = inPixels[index] & 0xff000000;
                    int rgb = c.argbValue() & 0x00ffffff;
                    outPixels[index++] = alpha | rgb;
                } else {
                    outPixels[index++] = 0;
                }
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     * @param viewpoint DOCUMENT ME!
     * @param normal DOCUMENT ME!
     * @param diffuseColor DOCUMENT ME!
     * @param specularColor DOCUMENT ME!
     * @param material DOCUMENT ME!
     * @param lightsArray DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ARGB phongShade(Vector3D position, Vector3D viewpoint,
        Vector3D normal, ARGB diffuseColor, ARGB specularColor,
        Material material, Light[] lightsArray) {
        shadedColor.setColor(diffuseColor);
        shadedColor.multiply(material.ambientIntensity);

        for (int i = 0; i < lightsArray.length; i++) {
            Light light = lightsArray[i];

            //FIXME-normalize outside loop
            n.set(normal);
            n.normalize();
            l.set(light.position);

            if (light.type != DISTANT) {
                l.subtract(position);
            }

            l.normalize();

            float nDotL = n.innerProduct(l);

            if (nDotL >= 0.0) {
                float dDotL = 0;

                v.set(viewpoint);
                v.subtract(position);
                v.normalize();

                // Spotlight
                if (light.type == SPOT) {
                    dDotL = light.direction.innerProduct(l);

                    if (dDotL < light.cosConeAngle) {
                        continue;
                    }
                }

                n.multiply(2.0f * nDotL);
                n.subtract(l);

                float rDotV = n.innerProduct(v);

                float rv;

                if (rDotV < 0.0) {
                    rv = 0.0f;
                } else {
                    rv = (float) Math.pow(rDotV, material.highlight);
                }

                // Spotlight
                if (light.type == SPOT) {
                    dDotL = light.cosConeAngle / dDotL;

                    float e = dDotL;
                    e *= e;
                    e *= e;
                    e *= e;
                    e = (float) Math.pow(dDotL, light.focus * 10) * (1 - e);

                    //					e = (float)Math.pow(e, light.focus*10)*(1 - dDotL);
                    rv *= e;
                    nDotL *= e;
                }

                diffuse_color.setColor(diffuseColor);
                diffuse_color.multiply(material.diffuseReflectivity);
                diffuse_color.multiply(light.realColor);
                diffuse_color.multiply(nDotL);
                specular_color.setColor(specularColor);
                specular_color.multiply(material.specularReflectivity);
                specular_color.multiply(light.realColor);
                specular_color.multiply(rv);
                diffuse_color.add(specular_color);
                diffuse_color.clamp();
                shadedColor.add(diffuse_color);
            }
        }

        shadedColor.clamp();

        return shadedColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param normal DOCUMENT ME!
     * @param inPixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getEnvironmentMap(Vector3D normal, int[] inPixels, int width,
        int height) {
        if (environmentMap != null) {
            float angle = (float) Math.acos(-normal.y);

            float x;
            float y;
            y = angle / ImageMath.PI;

            if ((y == 0.0f) || (y == 1.0f)) {
                x = 0.0f;
            } else {
                float f = normal.x / (float) Math.sin(angle);

                if (f > 1.0f) {
                    f = 1.0f;
                } else if (f < -1.0f) {
                    f = -1.0f;
                }

                x = (float) Math.acos(f) / ImageMath.PI;
            }

            // A bit of emprirical scaling....
            x = ((x - 0.5f) * 1.2f) + 0.5f;
            y = ((y - 0.5f) * 1.2f) + 0.5f;
            x = ImageMath.clamp(x * envWidth, 0, envWidth - 1);
            y = ImageMath.clamp(y * envHeight, 0, envHeight - 1);

            int ix = (int) x;
            int iy = (int) y;

            float xWeight = x - ix;
            float yWeight = y - iy;
            int i = (envWidth * iy) + ix;
            int dx = (ix == (envWidth - 1)) ? 0 : 1;
            int dy = (iy == (envHeight - 1)) ? 0 : envWidth;
            rgb[0] = envPixels[i];
            rgb[1] = envPixels[i + dx];
            rgb[2] = envPixels[i + dy];
            rgb[3] = envPixels[i + dx + dy];

            return ImageMath.bilinearInterpolate(xWeight, yWeight, rgb);
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Stylize/Light Effects...";
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class NormalEvaluator {
        /** DOCUMENT ME! */
        public final static int RECTANGLE = 0;

        /** DOCUMENT ME! */
        public final static int ROUNDRECT = 1;

        /** DOCUMENT ME! */
        public final static int ELLIPSE = 2;

        /** DOCUMENT ME! */
        public final static int LINEAR = 0;

        /** DOCUMENT ME! */
        public final static int SIN = 1;

        /** DOCUMENT ME! */
        public final static int CIRCLE_UP = 2;

        /** DOCUMENT ME! */
        public final static int CIRCLE_DOWN = 3;

        /** DOCUMENT ME! */
        public final static int SMOOTH = 4;

        /** DOCUMENT ME! */
        public final static int PULSE = 5;

        /** DOCUMENT ME! */
        public final static int SMOOTH_PULSE = 6;

        /** DOCUMENT ME! */
        public final static int THING = 7;

        /** DOCUMENT ME! */
        private int margin = 10;

        /** DOCUMENT ME! */
        private int shape = RECTANGLE;

        /** DOCUMENT ME! */
        private int bevel = LINEAR;

        /** DOCUMENT ME! */
        private int cornerRadius = 15;

        /**
         * DOCUMENT ME!
         *
         * @param margin DOCUMENT ME!
         */
        public void setMargin(int margin) {
            this.margin = margin;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getMargin() {
            return margin;
        }

        /**
         * DOCUMENT ME!
         *
         * @param cornerRadius DOCUMENT ME!
         */
        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getCornerRadius() {
            return cornerRadius;
        }

        /**
         * DOCUMENT ME!
         *
         * @param shape DOCUMENT ME!
         */
        public void setShape(int shape) {
            this.shape = shape;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getShape() {
            return shape;
        }

        /**
         * DOCUMENT ME!
         *
         * @param bevel DOCUMENT ME!
         */
        public void setBevel(int bevel) {
            this.bevel = bevel;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getBevel() {
            return bevel;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param width DOCUMENT ME!
         * @param height DOCUMENT ME!
         * @param normal DOCUMENT ME!
         */
        public void getNormalAt(int x, int y, int width, int height,
            Vector3D normal) {
            float distance = 0;
            normal.x = normal.y = 0;
            normal.z = 0.707f;

            switch (shape) {
            case RECTANGLE:

                if (x < margin) {
                    if ((x < y) && (x < (height - y))) {
                        normal.x = -1;
                    }
                } else if ((width - x) <= margin) {
                    if (((width - x - 1) < y) && ((width - x) <= (height - y))) {
                        normal.x = 1;
                    }
                }

                if (normal.x == 0) {
                    if (y < margin) {
                        normal.y = -1;
                    } else if ((height - y) <= margin) {
                        normal.y = 1;
                    }
                }

                distance = Math.min(Math.min(x, y),
                        Math.min(width - x - 1, height - y - 1));

                break;

            case ELLIPSE:

                float a = width / 2;
                float b = height / 2;
                float a2 = a * a;
                float b2 = b * b;
                float dx = x - a;
                float dy = y - b;
                float x2 = dx * dx;
                float y2 = dy * dy;
                distance = (b2 - ((b2 * x2) / a2)) - y2;

                float radius = (float) Math.sqrt(x2 + y2);
                distance = (0.5f * distance) / ((a + b) / 2); //FIXME

                if (radius != 0) {
                    normal.x = dx / radius;
                    normal.y = dy / radius;
                }

                break;

            case ROUNDRECT:
                distance = Math.min(Math.min(x, y),
                        Math.min(width - x - 1, height - y - 1));

                float c = Math.min(cornerRadius, Math.min(width / 2, height / 2));

                if (((x < c) || ((width - x) <= c)) &&
                        ((y < c) || ((height - y) <= c))) {
                    if ((width - x) <= c) {
                        x -= (width - c - c - 1);
                    }

                    if ((height - y) <= c) {
                        y -= (height - c - c - 1);
                    }

                    dx = x - c;
                    dy = y - c;
                    x2 = dx * dx;
                    y2 = dy * dy;
                    radius = (float) Math.sqrt(x2 + y2);
                    distance = c - radius;
                    normal.x = dx / radius;
                    normal.y = dy / radius;
                } else if (x < margin) {
                    normal.x = -1;
                } else if ((width - x) <= margin) {
                    normal.x = 1;
                } else if (y < margin) {
                    normal.y = -1;
                } else if ((height - y) <= margin) {
                    normal.y = 1;
                }

                break;
            }

            distance /= margin;

            if (distance < 0) {
                normal.z = -1;
                normal.normalize();

                return;
            }

            float dx = 1.0f / margin;
            float z1 = bevelFunction(distance);
            float z2 = bevelFunction(distance + dx);
            float dz = z2 - z1;
            normal.z = dx;
            normal.x *= dz;
            normal.y *= dz;

            /*
                                    if (dz == 0)
                                            normal.z = 1e10;
                                    else {
                                            float f = dz/(1.0/margin);
                                            normal.x /= f;
                                            normal.y /= f;
                                            normal.z *= f;
                                    }
            */
            normal.normalize();
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private float bevelFunction(float x) {
            x = ImageMath.clamp(x, 0.0f, 1.0f);

            switch (bevel) {
            case LINEAR:
                return ImageMath.clamp(x, 0.0f, 1.0f);

            case SIN:
                return (float) Math.sin((x * Math.PI) / 2);

            case CIRCLE_UP:
                return ImageMath.circleUp(x);

            case CIRCLE_DOWN:
                return ImageMath.circleDown(x);

            case SMOOTH:
                return ImageMath.smoothStep(0.1f, 0.9f, x);

            case PULSE:
                return ImageMath.pulse(0.0f, 1.0f, x);

            case SMOOTH_PULSE:
                return ImageMath.smoothPulse(0.0f, 0.1f, 0.5f, 1.0f, x);

            case THING:
                return (float) ((x < 0.2) ? Math.sin((x / 0.2 * Math.PI) / 2)
                                          : (0.5 +
                (0.5 * Math.sin(1 + ((x / 0.6 * Math.PI) / 2)))));
            }

            return x;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    static class ARGB {
        /** DOCUMENT ME! */
        public float a;

        /** DOCUMENT ME! */
        public float r;

        /** DOCUMENT ME! */
        public float g;

        /** DOCUMENT ME! */
        public float b;

/**
         * Creates a new ARGB object.
         */
        public ARGB() {
            this(0, 0, 0, 0);
        }

/**
         * Creates a new ARGB object.
         *
         * @param a DOCUMENT ME!
         * @param r DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public ARGB(int a, int r, int g, int b) {
            this.a = (float) a / 255.0f;
            this.r = (float) r / 255.0f;
            this.g = (float) g / 255.0f;
            this.b = (float) b / 255.0f;
        }

/**
         * Creates a new ARGB object.
         *
         * @param a DOCUMENT ME!
         * @param r DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public ARGB(float a, float r, float g, float b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }

/**
         * Creates a new ARGB object.
         *
         * @param v DOCUMENT ME!
         */
        public ARGB(ARGB v) {
            a = v.a;
            r = v.r;
            g = v.g;
            b = v.b;
        }

/**
         * Creates a new ARGB object.
         *
         * @param v DOCUMENT ME!
         */
        public ARGB(int v) {
            this((v >> 24) & 255, (v >> 16) & 255, (v >> 8) & 255, v & 255);
        }

/**
         * Creates a new ARGB object.
         *
         * @param c DOCUMENT ME!
         */
        public ARGB(Color c) {
            this(c.getRGB());
        }

        /**
         * DOCUMENT ME!
         *
         * @param a DOCUMENT ME!
         * @param r DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public void setColor(int a, int r, int g, int b) {
            this.a = (float) a / 255.0f;
            this.r = (float) r / 255.0f;
            this.g = (float) g / 255.0f;
            this.b = (float) b / 255.0f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param a DOCUMENT ME!
         * @param r DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public void setColor(float a, float r, float g, float b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void setColor(ARGB v) {
            a = v.a;
            r = v.r;
            g = v.g;
            b = v.b;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void setColor(int v) {
            setColor((v >> 24) & 255, (v >> 16) & 255, (v >> 8) & 255, v & 255);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int argbValue() {
            a = 1.0f;

            int ia = (int) (255.0 * a);
            int ir = (int) (255.0 * r);
            int ig = (int) (255.0 * g);
            int ib = (int) (255.0 * b);

            return (ia << 24) | (ir << 16) | (ig << 8) | ib;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float length() {
            return (float) Math.sqrt((r * r) + (g * g) + (b * b));
        }

        /**
         * DOCUMENT ME!
         */
        public void normalize() {
            float l = length();

            if (l != 0.0) {
                multiply(1.0f / l);
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void clamp() {
            if (a < 0.0f) {
                a = 0.0f;
            } else if (a > 1.0f) {
                a = 1.0f;
            }

            if (r < 0.0f) {
                r = 0.0f;
            } else if (r > 1.0f) {
                r = 1.0f;
            }

            if (g < 0.0) {
                g = 0.0f;
            } else if (g > 1.0) {
                g = 1.0f;
            }

            if (b < 0.0f) {
                b = 0.0f;
            } else if (b > 1.0f) {
                b = 1.0f;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param f DOCUMENT ME!
         */
        public void multiply(float f) {
            r *= f;
            g *= f;
            b *= f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void add(ARGB v) {
            r += v.r;
            g += v.g;
            b += v.b;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void subtract(ARGB v) {
            r -= v.r;
            g -= v.g;
            b -= v.b;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void multiply(ARGB v) {
            r *= v.r;
            g *= v.g;
            b *= v.b;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return a + " " + r + " " + g + " " + b + ")";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    static class Vector3D {
        /** DOCUMENT ME! */
        public float x;

        /** DOCUMENT ME! */
        public float y;

        /** DOCUMENT ME! */
        public float z;

/**
         * Creates a new Vector3D object.
         */
        public Vector3D() {
        }

/**
         * Creates a new Vector3D object.
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param z DOCUMENT ME!
         */
        public Vector3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

/**
         * Creates a new Vector3D object.
         *
         * @param v DOCUMENT ME!
         */
        public Vector3D(Vector3D v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param z DOCUMENT ME!
         */
        public void set(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void set(Vector3D v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float length() {
            return (float) Math.sqrt((x * x) + (y * y) + (z * z));
        }

        /**
         * DOCUMENT ME!
         */
        public void normalize() {
            float l = length();

            if (l != 0.0f) {
                multiply(1.0f / l);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param f DOCUMENT ME!
         */
        public void multiply(float f) {
            x *= f;
            y *= f;
            z *= f;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void add(Vector3D v) {
            x += v.x;
            y += v.y;
            z += v.z;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void subtract(Vector3D v) {
            x -= v.x;
            y -= v.y;
            z -= v.z;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void multiply(Vector3D v) {
            x *= v.x;
            y *= v.y;
            z *= v.z;
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float innerProduct(Vector3D v) {
            return (x * v.x) + (y * v.y) + (z * v.z);
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         * @param result DOCUMENT ME!
         */
        public void crossProduct(Vector3D v, Vector3D result) {
            result.x = (y * v.z) - (z * v.y);
            result.y = (z * v.x) - (x * v.z);
            result.z = (x * v.y) - (y * v.x);
        }

        // Reflects v about "this" (the normal)
        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         */
        public void reflect(Vector3D v) {
            float n = 2.0f * innerProduct(v);
            multiply(n);
            subtract(v);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "(" + x + " " + y + " " + z + ")";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public static class Material {
        /** DOCUMENT ME! */
        float ambientIntensity;

        /** DOCUMENT ME! */
        float diffuseReflectivity;

        /** DOCUMENT ME! */
        float specularReflectivity;

        /** DOCUMENT ME! */
        float highlight;

        /** DOCUMENT ME! */
        float reflectivity;

/**
         * Creates a new Material object.
         */
        public Material() {
            ambientIntensity = 0.5f;
            diffuseReflectivity = 0.8f;
            specularReflectivity = 0.9f;
            highlight = 3.0f;
            reflectivity = 0.0f;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public static class Light implements Cloneable {
        /** DOCUMENT ME! */
        int type = AMBIENT;

        /** DOCUMENT ME! */
        Vector3D position;

        /** DOCUMENT ME! */
        Vector3D direction;

        /** DOCUMENT ME! */
        ARGB realColor;

        /** DOCUMENT ME! */
        int color = 0xffffffff;

        /** DOCUMENT ME! */
        float intensity;

        /** DOCUMENT ME! */
        float azimuth;

        /** DOCUMENT ME! */
        float elevation;

        /** DOCUMENT ME! */
        float focus = 0.5f;

        /** DOCUMENT ME! */
        float centreX = 0.5f;

        /** DOCUMENT ME! */
        float centreY = 0.5f;

        /** DOCUMENT ME! */
        float coneAngle = ImageMath.PI / 6;

        /** DOCUMENT ME! */
        float cosConeAngle;

        /** DOCUMENT ME! */
        float distance = 100.0f;

/**
         * Creates a new Light object.
         */
        public Light() {
            this((135 * ImageMath.PI) / 180.0f, 0.5235987755982988f, 1.0f);
        }

/**
         * Creates a new Light object.
         *
         * @param azimuth   DOCUMENT ME!
         * @param elevation DOCUMENT ME!
         * @param intensity DOCUMENT ME!
         */
        public Light(float azimuth, float elevation, float intensity) {
            this.azimuth = azimuth;
            this.elevation = elevation;
            this.intensity = intensity;
        }

        /**
         * DOCUMENT ME!
         *
         * @param azimuth DOCUMENT ME!
         */
        public void setAzimuth(float azimuth) {
            this.azimuth = azimuth;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getAzimuth() {
            return azimuth;
        }

        /**
         * DOCUMENT ME!
         *
         * @param elevation DOCUMENT ME!
         */
        public void setElevation(float elevation) {
            this.elevation = elevation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getElevation() {
            return elevation;
        }

        /**
         * DOCUMENT ME!
         *
         * @param distance DOCUMENT ME!
         */
        public void setDistance(float distance) {
            this.distance = distance;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getDistance() {
            return distance;
        }

        /**
         * DOCUMENT ME!
         *
         * @param intensity DOCUMENT ME!
         */
        public void setIntensity(float intensity) {
            this.intensity = intensity;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getIntensity() {
            return intensity;
        }

        /**
         * DOCUMENT ME!
         *
         * @param coneAngle DOCUMENT ME!
         */
        public void setConeAngle(float coneAngle) {
            this.coneAngle = coneAngle;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getConeAngle() {
            return coneAngle;
        }

        /**
         * DOCUMENT ME!
         *
         * @param focus DOCUMENT ME!
         */
        public void setFocus(float focus) {
            this.focus = focus;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getFocus() {
            return focus;
        }

        /**
         * DOCUMENT ME!
         *
         * @param color DOCUMENT ME!
         */
        public void setColor(int color) {
            this.color = color;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getColor() {
            return color;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         */
        public void setCentreX(float x) {
            centreX = x;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getCentreX() {
            return centreX;
        }

        /**
         * DOCUMENT ME!
         *
         * @param y DOCUMENT ME!
         */
        public void setCentreY(float y) {
            centreY = y;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getCentreY() {
            return centreY;
        }

        /**
         * DOCUMENT ME!
         *
         * @param width DOCUMENT ME!
         * @param height DOCUMENT ME!
         */
        public void prepare(int width, int height) {
            float lx = (float) (Math.cos(azimuth) * Math.cos(elevation));
            float ly = (float) (Math.sin(azimuth) * Math.cos(elevation));
            float lz = (float) Math.sin(elevation);
            direction = new Vector3D(lx, ly, lz);
            direction.normalize();

            if (type != DISTANT) {
                lx *= distance;
                ly *= distance;
                lz *= distance;
                lx += (width * centreX);
                ly += (height * (1 - centreY));
            }

            position = new Vector3D(lx, ly, lz);
            realColor = new ARGB(color);
            realColor.multiply(intensity);
            cosConeAngle = (float) Math.cos(coneAngle);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object clone() {
            try {
                Light copy = (Light) super.clone();

                return copy;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Light";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class AmbientLight extends Light {
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Ambient Light";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class PointLight extends Light {
/**
         * Creates a new PointLight object.
         */
        public PointLight() {
            type = POINT;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Point Light";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class DistantLight extends Light {
/**
         * Creates a new DistantLight object.
         */
        public DistantLight() {
            type = DISTANT;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Distant Light";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class SpotLight extends Light {
/**
         * Creates a new SpotLight object.
         */
        public SpotLight() {
            type = SPOT;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Spotlight";
        }
    }
}
