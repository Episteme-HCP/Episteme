/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GaltonBoardDemo extends AbstractSimulationDemo {

    @Override
    protected Node createViewerNode() {
        return new GaltonBoardViewerInternal();
    }

    @Override
    protected String getLongDescription() {
        return getDescription();
    }

    @Override
    public String getName() {
        return "Galton Board";
    }

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    /**
     * Internal viewer implementation for Galton Board.
     */
    private static class GaltonBoardViewerInternal extends AbstractViewer implements Simulatable {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 800;
        private static final int ROWS = 12;
        private static final double PEG_SPACING = 40;
        private static final double START_X = WIDTH / 2;
        private static final double START_Y = 100;

        private List<Ball> balls = new ArrayList<>();
        private int[] bins = new int[ROWS + 1];
        private Random rand = new Random();
        private AnimationTimer timer;
        private boolean isRunning = false;
        private Canvas canvas;

        private static class Ball {
            double x, y, vx, vy;
            boolean settled = false;

            Ball(double x, double y) {
                this.x = x;
                this.y = y;
            }
        }

        public GaltonBoardViewerInternal() {
            canvas = new Canvas(WIDTH, HEIGHT);
            setCenter(canvas);

            timer = new AnimationTimer() {
                long lastAdd = 0;

                @Override
                public void handle(long now) {
                    if (now - lastAdd > 50_000_000) { // Add ball every 50ms
                        balls.add(new Ball(START_X + (rand.nextDouble() - 0.5), START_Y - 20));
                        lastAdd = now;
                    }
                    update();
                    draw(canvas.getGraphicsContext2D());
                }
            };
        }

        @Override public String getCategory() { return "Mathematics"; }
        @Override public String getName() { return "Galton Board"; }

        @Override public void play() { if (timer != null) timer.start(); isRunning = true; }
        @Override public void pause() { if (timer != null) timer.stop(); isRunning = false; }
        @Override public void stop() {
            pause();
            balls.clear();
            for(int i=0; i<bins.length; i++) bins[i] = 0;
            if (canvas != null) draw(canvas.getGraphicsContext2D());
        }
        @Override public void step() {
            update();
            if (canvas != null) draw(canvas.getGraphicsContext2D());
        }
        @Override public void setSpeed(double multiplier) { }
        @Override public boolean isPlaying() { return isRunning; }

        private void update() {
            Iterator<Ball> it = balls.iterator();
            double pegRadius = 4;
            double ballRadius = 4;
            double collisionDist = pegRadius + ballRadius;

            while (it.hasNext()) {
                Ball b = it.next();
                if (b.settled) continue;

                b.vy += 0.15;
                b.y += b.vy;
                b.x += b.vx;
                b.vx *= 0.99;

                for (int r = 0; r < ROWS; r++) {
                    double pegY = START_Y + r * PEG_SPACING;
                    double rowStartX = WIDTH / 2 - r * PEG_SPACING / 2.0;
                    for (int c = 0; c <= r; c++) {
                        double pegX = rowStartX + c * PEG_SPACING;
                        double dx = b.x - pegX;
                        double dy = b.y - pegY;
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        if (dist < collisionDist && dist > 0) {
                            double overlap = collisionDist - dist;
                            double nx = dx / dist;
                            double ny = dy / dist;
                            b.x += nx * overlap;
                            b.y += ny * overlap;
                            double dotProduct = b.vx * nx + b.vy * ny;
                            b.vx -= 1.5 * dotProduct * nx;
                            b.vy -= 1.5 * dotProduct * ny;
                            b.vx += (rand.nextDouble() - 0.5) * 0.5;
                            b.vx *= 0.7;
                            b.vy *= 0.7;
                        }
                    }
                }

                if (b.x < 50) { b.x = 50; b.vx = Math.abs(b.vx) * 0.5; }
                if (b.x > WIDTH - 50) { b.x = WIDTH - 50; b.vx = -Math.abs(b.vx) * 0.5; }

                double floorY = START_Y + ROWS * PEG_SPACING + 50;
                if (b.y > floorY) {
                    b.settled = true;
                    int bin = (int) ((b.x - (WIDTH / 2 - ROWS * PEG_SPACING / 2)) / PEG_SPACING);
                    if (bin >= 0 && bin <= ROWS) bins[bin]++;
                    it.remove();
                }
            }
        }

        private void draw(GraphicsContext gc) {
            gc.setFill(Color.rgb(30, 30, 30));
            gc.fillRect(0, 0, WIDTH, HEIGHT);
            gc.setFill(Color.GRAY);
            for (int r = 0; r < ROWS; r++) {
                double rowY = START_Y + r * PEG_SPACING;
                double rowStartX = WIDTH / 2 - r * PEG_SPACING / 2.0;
                for (int c = 0; c <= r; c++) {
                    gc.fillOval(rowStartX + c * PEG_SPACING - 3, rowY - 3, 6, 6);
                }
            }
            gc.setFill(Color.RED);
            for (Ball b : balls) gc.fillOval(b.x - 4, b.y - 4, 8, 8);
            double baseX = WIDTH / 2 - ROWS * PEG_SPACING / 2.0;
            double baseY = HEIGHT - 50;
            gc.setFill(Color.LIGHTBLUE);
            for (int i = 0; i <= ROWS; i++) {
                double h = bins[i] * 5;
                gc.fillRect(baseX + i * PEG_SPACING - 10, baseY - h, 20, h);
            }
        }
    }
}


