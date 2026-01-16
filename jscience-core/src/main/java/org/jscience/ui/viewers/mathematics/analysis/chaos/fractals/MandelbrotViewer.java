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

package org.jscience.ui.viewers.mathematics.analysis.chaos.fractals;

import org.jscience.io.Configuration;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.numbers.real.Real;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ArrayList;

/**
 * Interactive Mandelbrot Set Viewer.
 * Uses Real for all internal calculations, double only for display output.
 * All default values loaded from jscience.properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MandelbrotViewer extends AbstractViewer {

    private static final String CFG_PREFIX = "viewer.mandelbrot.default.";

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.mandelbrot.title");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("viewer.mandelbrot.param.minre",
                I18n.getInstance().get("viewer.mandelbrot.param.minre.desc"),
                -3, 1, 0.1, minRe.doubleValue(), v -> { minRe = Real.of(v); render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.maxre",
                I18n.getInstance().get("viewer.mandelbrot.param.maxre.desc"),
                -1, 3, 0.1, maxRe.doubleValue(), v -> { maxRe = Real.of(v); render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.minim",
                I18n.getInstance().get("viewer.mandelbrot.param.minim.desc"),
                -2, 2, 0.1, minIm.doubleValue(), v -> { minIm = Real.of(v); render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.maxim",
                I18n.getInstance().get("viewer.mandelbrot.param.maxim.desc"),
                -2, 2, 0.1, maxIm.doubleValue(), v -> { maxIm = Real.of(v); render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.maxiter",
                I18n.getInstance().get("viewer.mandelbrot.param.maxiter.desc"),
                50, 2000, 50, maxIter, v -> { maxIter = v.intValue(); render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.juliareal",
                I18n.getInstance().get("viewer.mandelbrot.param.juliareal.desc"),
                -2, 2, 0.01, juliaReal.doubleValue(), v -> { juliaReal = Real.of(v); if(juliaMode) render(); }));
        params.add(new NumericParameter("viewer.mandelbrot.param.juliaimag",
                I18n.getInstance().get("viewer.mandelbrot.param.juliaimag.desc"),
                -2, 2, 0.01, juliaImag.doubleValue(), v -> { juliaImag = Real.of(v); if(juliaMode) render(); }));
        params.add(new BooleanParameter("mandelbrot.mode.julia",
                I18n.getInstance().get("mandelbrot.mode.julia"),
                juliaMode,
                v -> {
                    juliaMode = v;
                    if (juliaMode) {
                        minRe = Real.of(-2.0);
                        maxRe = Real.of(2.0);
                        minIm = Real.of(-1.5);
                        maxIm = Real.of(1.5);
                    } else {
                        minRe = Real.of(Configuration.getDouble(CFG_PREFIX + "minre", -2.0));
                        maxRe = Real.of(Configuration.getDouble(CFG_PREFIX + "maxre", 1.0));
                        Real aspect = Real.of((double) HEIGHT / WIDTH);
                        Real reWidth = maxRe.subtract(minRe);
                        Real imHeight = reWidth.multiply(aspect);
                        Real centerIm = Real.ZERO;
                        minIm = centerIm.subtract(imHeight.divide(Real.TWO));
                        maxIm = centerIm.add(imHeight.divide(Real.TWO));
                    }
                    render();
                }));
        return params;
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    // All defaults loaded from Configuration
    private int maxIter = Configuration.getInt(CFG_PREFIX + "maxiter", 200);
    private Real minRe = Real.of(Configuration.getDouble(CFG_PREFIX + "minre", -2.0));
    private Real maxRe = Real.of(Configuration.getDouble(CFG_PREFIX + "maxre", 1.0));
    private Real minIm = Real.of(Configuration.getDouble(CFG_PREFIX + "minim", -1.2));
    private Real maxIm = Real.of(Configuration.getDouble(CFG_PREFIX + "maxim", 1.2));

    private Canvas canvas;
    private WritableImage image;

    private boolean juliaMode = false;
    private Real juliaReal = Real.of(Configuration.getDouble(CFG_PREFIX + "juliareal", -0.7));
    private Real juliaImag = Real.of(Configuration.getDouble(CFG_PREFIX + "juliaimag", 0.27015));

    private static final Real ESCAPE_RADIUS_SQ = Real.of(4.0);

    public MandelbrotViewer() {
        canvas = new Canvas(WIDTH, HEIGHT);
        image = new WritableImage(WIDTH, HEIGHT);

        StackPane canvasPane = new StackPane(canvas);
        setCenter(canvasPane);
        this.getStyleClass().add("viewer-root");

        canvas.setOnMouseMoved(e -> {
            if (juliaMode && e.isControlDown()) {
                Real xRatio = Real.of(e.getX() / WIDTH);
                Real yRatio = Real.of(e.getY() / HEIGHT);
                juliaReal = minRe.add(xRatio.multiply(maxRe.subtract(minRe)));
                juliaImag = maxIm.subtract(yRatio.multiply(maxIm.subtract(minIm)));
                render();
            }
        });

        canvas.setOnScroll(e -> {
            Real zoomFactor = Real.of((e.getDeltaY() > 0) ? 0.8 : 1.25);

            Real xRatio = Real.of(e.getX() / WIDTH);
            Real yRatio = Real.of(e.getY() / HEIGHT);
            Real mouseRe = minRe.add(xRatio.multiply(maxRe.subtract(minRe)));
            Real mouseIm = maxIm.subtract(yRatio.multiply(maxIm.subtract(minIm)));

            Real reW = maxRe.subtract(minRe).multiply(zoomFactor);
            Real imH = maxIm.subtract(minIm).multiply(zoomFactor);

            minRe = mouseRe.subtract(mouseRe.subtract(minRe).multiply(zoomFactor));
            maxRe = minRe.add(reW);
            minIm = mouseIm.subtract(mouseIm.subtract(minIm).multiply(zoomFactor));
            maxIm = minIm.add(imH);

            render();
        });

        class DragContext {
            double mouseAnchorX, mouseAnchorY;
        }
        DragContext dragContext = new DragContext();

        canvas.setOnMousePressed(e -> {
            dragContext.mouseAnchorX = e.getX();
            dragContext.mouseAnchorY = e.getY();
        });

        canvas.setOnMouseDragged(e -> {
            double dx = e.getX() - dragContext.mouseAnchorX;
            double dy = e.getY() - dragContext.mouseAnchorY;
            dragContext.mouseAnchorX = e.getX();
            dragContext.mouseAnchorY = e.getY();

            Real rePerPixel = maxRe.subtract(minRe).divide(Real.of(WIDTH));
            Real imPerPixel = maxIm.subtract(minIm).divide(Real.of(HEIGHT));

            minRe = minRe.subtract(rePerPixel.multiply(Real.of(dx)));
            maxRe = maxRe.subtract(rePerPixel.multiply(Real.of(dx)));
            maxIm = maxIm.add(imPerPixel.multiply(Real.of(dy)));
            minIm = minIm.add(imPerPixel.multiply(Real.of(dy)));

            render();
        });

        // Adjust maxIm to aspect ratio initially
        Real aspect = Real.of((double) HEIGHT / WIDTH);
        Real reWidth = maxRe.subtract(minRe);
        Real imHeight = reWidth.multiply(aspect);
        Real centerIm = minIm.add(maxIm).divide(Real.TWO);
        minIm = centerIm.subtract(imHeight.divide(Real.TWO));
        maxIm = centerIm.add(imHeight.divide(Real.TWO));

        render();
    }

    private void render() {
        PixelWriter pw = image.getPixelWriter();
        Real reFactor = maxRe.subtract(minRe).divide(Real.of(WIDTH - 1));
        Real imFactor = maxIm.subtract(minIm).divide(Real.of(HEIGHT - 1));

        for (int y = 0; y < HEIGHT; y++) {
            Real cIm = maxIm.subtract(imFactor.multiply(Real.of(y)));
            for (int x = 0; x < WIDTH; x++) {
                Real cRe = minRe.add(reFactor.multiply(Real.of(x)));
                Real Z_re, Z_im;
                Real K_re, K_im;

                if (juliaMode) {
                    Z_re = cRe;
                    Z_im = cIm;
                    K_re = juliaReal;
                    K_im = juliaImag;
                } else {
                    Z_re = Real.ZERO;
                    Z_im = Real.ZERO;
                    K_re = cRe;
                    K_im = cIm;
                }

                boolean isInside = true;
                int n = 0;
                for (; n < maxIter; n++) {
                    Real Z_re2 = Z_re.multiply(Z_re);
                    Real Z_im2 = Z_im.multiply(Z_im);
                    if (Z_re2.add(Z_im2).compareTo(ESCAPE_RADIUS_SQ) > 0) {
                        isInside = false;
                        break;
                    }
                    Real newIm = Z_re.multiply(Z_im).multiply(Real.TWO).add(K_im);
                    Z_re = Z_re2.subtract(Z_im2).add(K_re);
                    Z_im = newIm;
                }
                Color color = isInside ? Color.BLACK : Color.hsb((n * 7) % 360, 0.8, 1.0);
                pw.setColor(x, y, color);
            }
        }

        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("MandelbrotViewer.desc", "MandelbrotViewer description");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
