/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class GradientFilter extends ImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 611926880411013769L;

    /** DOCUMENT ME! */
    public final static int LINEAR = 0;

    /** DOCUMENT ME! */
    public final static int BILINEAR = 1;

    /** DOCUMENT ME! */
    public final static int RADIAL = 2;

    /** DOCUMENT ME! */
    public final static int CONICAL = 3;

    /** DOCUMENT ME! */
    public final static int BICONICAL = 4;

    /** DOCUMENT ME! */
    public final static int SQUARE = 5;

    /** DOCUMENT ME! */
    public final static int INT_LINEAR = 0;

    /** DOCUMENT ME! */
    public final static int INT_CIRCLE_UP = 1;

    /** DOCUMENT ME! */
    public final static int INT_CIRCLE_DOWN = 2;

    /** DOCUMENT ME! */
    public final static int INT_SMOOTH = 3;

    /** DOCUMENT ME! */
    private float angle = 0;

    /** DOCUMENT ME! */
    private int color1 = 0xff000000;

    /** DOCUMENT ME! */
    private int color2 = 0xffffffff;

    /** DOCUMENT ME! */
    private Point p1 = new Point(0, 0);

    /** DOCUMENT ME! */
    private Point p2 = new Point(64, 64);

    /** DOCUMENT ME! */
    private boolean repeat = false;

    /** DOCUMENT ME! */
    private float x1;

    /** DOCUMENT ME! */
    private float y1;

    /** DOCUMENT ME! */
    private float dx;

    /** DOCUMENT ME! */
    private float dy;

    /** DOCUMENT ME! */
    private Colormap colormap = null;

    /** DOCUMENT ME! */
    private int type;

    /** DOCUMENT ME! */
    private int interpolation = INT_LINEAR;

    /** DOCUMENT ME! */
    private int paintMode = PixelUtils.NORMAL;

/**
     * Creates a new GradientFilter object.
     */
    public GradientFilter() {
    }

/**
     * Creates a new GradientFilter object.
     *
     * @param p1            DOCUMENT ME!
     * @param p2            DOCUMENT ME!
     * @param color1        DOCUMENT ME!
     * @param color2        DOCUMENT ME!
     * @param repeat        DOCUMENT ME!
     * @param type          DOCUMENT ME!
     * @param interpolation DOCUMENT ME!
     */
    public GradientFilter(Point p1, Point p2, int color1, int color2,
        boolean repeat, int type, int interpolation) {
        this.p1 = p1;
        this.p2 = p2;
        this.color1 = color1;
        this.color2 = color2;
        this.repeat = repeat;
        this.type = type;
        this.interpolation = interpolation;
        colormap = new LinearColormap(color1, color2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param point1 DOCUMENT ME!
     */
    public void setPoint1(Point point1) {
        this.p1 = point1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPoint1() {
        return p1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param point2 DOCUMENT ME!
     */
    public void setPoint2(Point point2) {
        this.p2 = point2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPoint2() {
        return p2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param interpolation DOCUMENT ME!
     */
    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInterpolation() {
        return interpolation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     */
    public void setAngle(float angle) {
        this.angle = angle;
        p2 = new Point((int) (64 * Math.cos(angle)),
                (int) (64 * Math.sin(angle)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paintMode DOCUMENT ME!
     */
    public void setPaintMode(int paintMode) {
        this.paintMode = paintMode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPaintMode() {
        return paintMode;
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        int rgb1;
        int rgb2;
        float x1;
        float y1;
        float x2;
        float y2;
        x1 = p1.x;
        x2 = p2.x;

        if ((x1 > x2) && (type != RADIAL)) {
            y1 = x1;
            x1 = x2;
            x2 = y1;
            y1 = p2.y;
            y2 = p1.y;
            rgb1 = color2;
            rgb2 = color1;
        } else {
            y1 = p1.y;
            y2 = p2.y;
            rgb1 = color1;
            rgb2 = color2;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        float lenSq = (dx * dx) + (dy * dy);
        this.x1 = x1;
        this.y1 = y1;

        if (lenSq >= Float.MIN_VALUE) {
            dx = dx / lenSq;
            dy = dy / lenSq;

            if (repeat) {
                dx = dx % 1.0f;
                dy = dy % 1.0f;
            }
        }

        this.dx = dx;
        this.dy = dy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        initialize();
        consumer.setDimensions(width, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void setColorModel(ColorModel model) {
        consumer.setColorModel(ColorModel.getRGBdefault());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        byte[] pixels, int off, int scansize) {
        int[] ipixels = new int[w * h];
        int inIndex = off;
        int outIndex = 0;

        for (int iy = 0; iy < h; iy++) {
            for (int ix = 0; ix < w; ix++)
                ipixels[outIndex++] = model.getRGB(pixels[inIndex++]);

            inIndex += (scansize - w);
        }

        switch (type) {
        case LINEAR:
        case BILINEAR:
            linearGradient(ipixels, x, y, w, h);

            break;

        case CONICAL:
        case BICONICAL:
            conicalGradient(ipixels, w, h);

            break;

        case SQUARE:
            squareGradient(ipixels, w, h);

            break;
        }

        consumer.setPixels(x, y, w, h, model, ipixels, 0, w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param pixels DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param scansize DOCUMENT ME!
     */
    public void setPixels(int x, int y, int w, int h, ColorModel model,
        int[] pixels, int off, int scansize) {
        int[] ipixels = new int[w * h];
        int inIndex = off;
        int outIndex = 0;

        for (int iy = 0; iy < h; iy++) {
            for (int ix = 0; ix < w; ix++)
                ipixels[outIndex++] = model.getRGB(pixels[inIndex++]);

            inIndex += (scansize - w);
        }

        switch (type) {
        case LINEAR:
        case BILINEAR:
            linearGradient(ipixels, x, y, w, h);

            break;

        case RADIAL:
            radialGradient(ipixels, w, h);

            break;

        case CONICAL:
        case BICONICAL:
            conicalGradient(ipixels, w, h);

            break;

        case SQUARE:
            squareGradient(ipixels, w, h);

            break;
        }

        consumer.setPixels(x, y, w, h, model, ipixels, 0, w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param rowrel DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    private void repeatGradient(int[] pixels, int w, int h, float rowrel,
        float dx, float dy) {
        int off = 0;

        for (int y = 0; y < h; y++) {
            float colrel = rowrel;
            int j = w;
            int rgb;

            while (--j >= 0) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(map(ImageMath.triangle(colrel)));
                } else {
                    rgb = colormap.getColor(map(ImageMath.mod(colrel, 1.0f)));
                }

                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                        paintMode);
                off++;
                colrel += dx;
            }

            rowrel += dy;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param rowrel DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    private void singleGradient(int[] pixels, int w, int h, float rowrel,
        float dx, float dy) {
        int off = 0;

        for (int y = 0; y < h; y++) {
            float colrel = rowrel;
            int j = w;
            int rgb;

            if (colrel <= 0.0) {
                rgb = colormap.getColor(0);

                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                            paintMode);
                    off++;
                    colrel += dx;
                } while ((--j > 0) && (colrel <= 0.0));
            }

            while ((colrel < 1.0) && (--j >= 0)) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(map(ImageMath.triangle(colrel)));
                } else {
                    rgb = colormap.getColor(map(colrel));
                }

                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                        paintMode);
                off++;
                colrel += dx;
            }

            if (j > 0) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(0.0f);
                } else {
                    rgb = colormap.getColor(1.0f);
                }

                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                            paintMode);
                    off++;
                } while (--j > 0);
            }

            rowrel += dy;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    private void linearGradient(int[] pixels, int x, int y, int w, int h) {
        float rowrel = ((x - x1) * dx) + ((y - y1) * dy);

        if (repeat) {
            repeatGradient(pixels, w, h, rowrel, dx, dy);
        } else {
            singleGradient(pixels, w, h, rowrel, dx, dy);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    private void radialGradient(int[] pixels, int w, int h) {
        int off = 0;
        float radius = distance(p2.x - p1.x, p2.y - p1.y);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float distance = distance(x - p1.x, y - p1.y);
                float ratio = distance / radius;

                if (repeat) {
                    ratio = ratio % 2;
                } else if (ratio > 1.0) {
                    ratio = 1.0f;
                }

                int rgb = colormap.getColor(map(ratio));
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                        paintMode);
                off++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    private void squareGradient(int[] pixels, int w, int h) {
        int off = 0;
        float radius = Math.max(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float distance = Math.max(Math.abs(x - p1.x), Math.abs(y -
                            p1.y));
                float ratio = distance / radius;

                if (repeat) {
                    ratio = ratio % 2;
                } else if (ratio > 1.0) {
                    ratio = 1.0f;
                }

                int rgb = colormap.getColor(map(ratio));
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                        paintMode);
                off++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixels DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    private void conicalGradient(int[] pixels, int w, int h) {
        int off = 0;
        float angle0 = (float) Math.atan2(p2.x - p1.x, p2.y - p1.y);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float angle = (float) (Math.atan2(x - p1.x, y - p1.y) - angle0) / (ImageMath.TWO_PI);
                angle += 1.0f;
                angle %= 1.0f;

                if (type == BICONICAL) {
                    angle = ImageMath.triangle(angle);
                }

                int rgb = colormap.getColor(map(angle));
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off],
                        paintMode);
                off++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float map(float v) {
        if (repeat) {
            v = (v > 1.0) ? (2.0f - v) : v;
        }

        switch (interpolation) {
        case INT_CIRCLE_UP:
            v = ImageMath.circleUp(ImageMath.clamp(v, 0.0f, 1.0f));

            break;

        case INT_CIRCLE_DOWN:
            v = ImageMath.circleDown(ImageMath.clamp(v, 0.0f, 1.0f));

            break;

        case INT_SMOOTH:
            v = ImageMath.smoothStep(0, 1, v);

            break;
        }

        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float distance(float a, float b) {
        return (float) Math.sqrt((a * a) + (b * b));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Other/Gradient Fill...";
    }
}
