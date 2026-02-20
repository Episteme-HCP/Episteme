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
package org.jscience.core.media.vision.detection;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.jscience.core.mathematics.ml.neural.backends.ONNXRuntimeBackend;
import org.jscience.core.mathematics.linearalgebra.Tensor;

/**
 * Wrapper for YOLO (You Only Look Once) Object Detection models.
 * <p>
 * Uses {@link ONNXRuntimeBackend} to execute YOLOv8 models.
 * Provides sophisticated pre-processing and post-processing (NMS).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class YoloDetector {
    private static final int INPUT_SIZE = 640;
    private static final float CONFIDENCE_THRESHOLD = 0.25f;
    private static final float IOU_THRESHOLD = 0.45f;

    private final ONNXRuntimeBackend.ONNXSession session;
    private final List<String> labels;

    /**
     * Represents a detected object.
     */
    public record Detection(String label, float confidence, float x1, float y1, float x2, float y2) {
        @Override
        public String toString() {
            return String.format("%s (%.2f): [%.1f, %.1f, %.1f, %.1f]", label, confidence, x1, y1, x2, y2);
        }
    }

    public YoloDetector(Path modelPath, List<String> labels) {
        this.labels = labels;
        try {
            this.session = org.jscience.core.mathematics.loaders.ONNXModelReader.getInstance().load(modelPath.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YOLO model", e);
        }
    }

    /**
     * Detects objects in an image.
     * @param imagePath path to the input image.
     * @return list of detected objects.
     */
    public List<Detection> detect(Path imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(imagePath.toFile());
            if (originalImage == null) throw new IOException("Could not read image: " + imagePath);

            // 1. Preprocess
            Tensor<Float> inputTensor = preprocess(originalImage);
            
            Map<String, Tensor<?>> inputs = new java.util.HashMap<>();
            inputs.put("images", inputTensor);
            
            // 2. Run Inference
            Map<String, Tensor<?>> outputs = session.run(inputs);
            
            // 3. Post-process
            return postprocess(outputs, originalImage.getWidth(), originalImage.getHeight());
        } catch (IOException e) {
            throw new RuntimeException("Image processing failed", e);
        }
    }

    private Tensor<Float> preprocess(BufferedImage img) {
        BufferedImage resized = new BufferedImage(INPUT_SIZE, INPUT_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, INPUT_SIZE, INPUT_SIZE, null);
        g.dispose();

        Tensor<Float> inputTensor = Tensor.zeros(Float.class, 1, 3, INPUT_SIZE, INPUT_SIZE);
        for (int y = 0; y < INPUT_SIZE; y++) {
            for (int x = 0; x < INPUT_SIZE; x++) {
                int rgb = resized.getRGB(x, y);
                inputTensor.set(((rgb >> 16) & 0xFF) / 255.0f, 0, 0, y, x); // R
                inputTensor.set(((rgb >> 8) & 0xFF) / 255.0f, 0, 1, y, x);  // G
                inputTensor.set((rgb & 0xFF) / 255.0f, 0, 2, y, x);       // B
            }
        }
        return inputTensor;
    }

    @SuppressWarnings("unchecked")
    private List<Detection> postprocess(Map<String, Tensor<?>> outputs, int imgW, int imgH) {
        // Output tensor shape [1, 84, 8400] for YOLOv8
        Tensor<Float> output = (Tensor<Float>) outputs.values().iterator().next();
        
        List<Detection> candidates = new ArrayList<>();
        int numDetections = 8400; // Fixed for YOLOv8 640x640
        int numClasses = labels.size();

        for (int i = 0; i < numDetections; i++) {
            // Find max class score
            float maxScore = -1f;
            int classId = -1;
            for (int c = 0; c < numClasses; c++) {
                float score = output.get(0, 4 + c, i);
                if (score > maxScore) {
                    maxScore = score;
                    classId = c;
                }
            }

            if (maxScore > CONFIDENCE_THRESHOLD) {
                float xCenter = output.get(0, 0, i);
                float yCenter = output.get(0, 1, i);
                float width = output.get(0, 2, i);
                float height = output.get(0, 3, i);

                // Convert from center coords to corner coords and scale to original image size
                float x1 = (xCenter - width / 2f) * imgW / INPUT_SIZE;
                float y1 = (yCenter - height / 2f) * imgH / INPUT_SIZE;
                float x2 = (xCenter + width / 2f) * imgW / INPUT_SIZE;
                float y2 = (yCenter + height / 2f) * imgH / INPUT_SIZE;

                candidates.add(new Detection(labels.get(classId), maxScore, x1, y1, x2, y2));
            }
        }
        
        return applyNMS(candidates);
    }

    private List<Detection> applyNMS(List<Detection> boxes) {
        if (boxes.isEmpty()) return boxes;
        boxes.sort(Comparator.comparingDouble(Detection::confidence).reversed());
        
        List<Detection> selected = new ArrayList<>();
        boolean[] active = new boolean[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) active[i] = true;

        for (int i = 0; i < boxes.size(); i++) {
            if (!active[i]) continue;
            Detection b1 = boxes.get(i);
            selected.add(b1);
            for (int j = i + 1; j < boxes.size(); j++) {
                if (active[j]) {
                    if (calculateIoU(b1, boxes.get(j)) > IOU_THRESHOLD) {
                        active[j] = false;
                    }
                }
            }
        }
        return selected;
    }

    private float calculateIoU(Detection b1, Detection b2) {
        float x1 = Math.max(b1.x1(), b2.x1());
        float y1 = Math.max(b1.y1(), b2.y1());
        float x2 = Math.min(b1.x2(), b2.x2());
        float y2 = Math.min(b1.y2(), b2.y2());
        float intersection = Math.max(0, x2 - x1) * Math.max(0, y2 - y1);
        float a1 = (b1.x2() - b1.x1()) * (b1.y2() - b1.y1());
        float a2 = (b2.x2() - b2.x1()) * (b2.y2() - b2.y1());
        return intersection / (a1 + a2 - intersection);
    }
}
