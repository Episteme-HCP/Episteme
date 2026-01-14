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

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jscience.ui.i18n.I18n;

/**
 * Interactive Mandelbrot Set Viewer.
 * Features Zoom, Pan, and parallel rendering.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MandelbrotViewer extends AbstractViewer {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("mandelbrot.title", "Mandelbrot Set");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private int maxIter = 200;

    private double minRe = -2.0;
    private double maxRe = 1.0;
    private double minIm = -1.2;
    private double maxIm = 1.2;

    private Canvas canvas;
    private WritableImage image;

    private boolean juliaMode = false;
    private double juliaReal = -0.7;
    private double juliaImag = 0.27015;

    public MandelbrotViewer() {
        canvas = new Canvas(WIDTH, HEIGHT);
        image = new WritableImage(WIDTH, HEIGHT);

        StackPane canvasPane = new StackPane(canvas);

        // Controls
        javafx.scene.control.ToggleButton modeBtn = new javafx.scene.control.ToggleButton(
                I18n.getInstance().get("mandelbrot.mode.julia"));
        modeBtn.setTranslateY(20);
        modeBtn.setTranslateX(20);
        javafx.scene.layout.VBox controls = new javafx.scene.layout.VBox(modeBtn);
        controls.setPickOnBounds(false); // overlay

        getChildren().addAll(canvasPane, controls);

        modeBtn.setOnAction(e -> {
            juliaMode = modeBtn.isSelected();
            // Reset view based on mode
            if (juliaMode) {
                minRe = -2.0;
                maxRe = 2.0;
                minIm = -1.5;
                maxIm = 1.5;
            } else {
                // Reset to Mandelbrot default and recalculate aspect
                minRe = -2.0;
                maxRe = 1.0;
                double aspect = (double) HEIGHT / WIDTH;
                double reWidth = maxRe - minRe;
                double imHeight = reWidth * aspect;
                double centerIm = 0.0; // Center at origin for Mandelbrot
                minIm = centerIm - imHeight / 2;
                maxIm = centerIm + imHeight / 2;
            }
            render();
        });

        // Mouse Move for dynamic Julia parameter (only if in Julia mode? Or maybe on
        // Ctrl key?)
        canvas.setOnMouseMoved(e -> {
            if (juliaMode && e.isControlDown()) {
                // Map mouse to complex plane (-2..2 in both? relative to window)
                juliaReal = minRe + (e.getX() / WIDTH) * (maxRe - minRe);
                juliaImag = maxIm - (e.getY() / HEIGHT) * (maxIm - minIm);
                render();
            }
        });

        // Interaction (Scroll/Pan)
        canvas.setOnScroll(e -> {
            double zoomFactor = (e.getDeltaY() > 0) ? 0.8 : 1.25;

            double mouseRe = minRe + (e.getX() / WIDTH) * (maxRe - minRe);
            double mouseIm = maxIm - (e.getY() / HEIGHT) * (maxIm - minIm);

            double reW = (maxRe - minRe) * zoomFactor;
            double imH = (maxIm - minIm) * zoomFactor;

            minRe = mouseRe - (mouseRe - minRe) * zoomFactor;
            maxRe = minRe + reW;
            minIm = mouseIm - (mouseIm - minIm) * zoomFactor;
            maxIm = minIm + imH;

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

            double rePerPixel = (maxRe - minRe) / WIDTH;
            double imPerPixel = (maxIm - minIm) / HEIGHT;

            minRe -= dx * rePerPixel;
            maxRe -= dx * rePerPixel;
            maxIm += dy * imPerPixel;
            minIm += dy * imPerPixel;

            render();
        });

        // Adjust maxIm to aspect ratio initially
        double aspect = (double) HEIGHT / WIDTH;
        double reWidth = maxRe - minRe;
        double imHeight = reWidth * aspect;
        double centerIm = (minIm + maxIm) / 2;
        minIm = centerIm - imHeight / 2;
        maxIm = centerIm + imHeight / 2;

        render();
    }

    private void render() {
        PixelWriter pw = image.getPixelWriter();
        double reFactor = (maxRe - minRe) / (WIDTH - 1);
        double imFactor = (maxIm - minIm) / (HEIGHT - 1);

        // Sequential for JavaFX safety
        for (int y = 0; y < HEIGHT; y++) {
            double cIm = maxIm - y * imFactor;
            for (int x = 0; x < WIDTH; x++) {
                double cRe = minRe + x * reFactor;
                double Z_re, Z_im;
                double K_re, K_im;

                if (juliaMode) {
                    Z_re = cRe;
                    Z_im = cIm;
                    K_re = juliaReal;
                    K_im = juliaImag;
                } else {
                    Z_re = 0;
                    Z_im = 0;
                    K_re = cRe;
                    K_im = cIm;
                }

                boolean isInside = true;
                int n = 0;
                for (; n < maxIter; n++) {
                    double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
                    if (Z_re2 + Z_im2 > 4) {
                        isInside = false;
                        break;
                    }
                    Z_im = 2 * Z_re * Z_im + K_im;
                    Z_re = Z_re2 - Z_im2 + K_re;
                }
                Color color = isInside ? Color.BLACK : Color.hsb((n * 7) % 360, 0.8, 1.0);
                pw.setColor(x, y, color);
            }
        }

        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
    }

}


